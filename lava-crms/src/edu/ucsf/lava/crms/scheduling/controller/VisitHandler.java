package edu.ucsf.lava.crms.scheduling.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class VisitHandler extends CrmsEntityComponentHandler {
	protected static final String DELETE_VISIT_WITH_INSTRUMENTS_ERROR_CODE = "visit.deleteHasInstruments.command";
	

	
	public VisitHandler() {
		super();
		setHandledEntity("visit", edu.ucsf.lava.crms.scheduling.model.Visit.class);
		CrmsSessionUtils.setIsVisitContext(this);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"projName","visitType","visitLocation","visitWith","visitDate","visitStatus"});
	    return getRequiredFields();
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		// do not init project to current project, as user may not have permission to add visits
		// in that project
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		if (p != null){
			((Visit)command).setPatient(p);
		}
		return command;
	}

	

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		

		AuthUser user = CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request);
		Long uid = user.getId();
		Visit v = (Visit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Patient p = v.getPatient();
		Long id = p.getId();
		
		Map<String,String> projList = listManager.getDynamicList(getCurrentUser(request),
				"enrollmentStatus.patientProjects",	"patientId", 
                ((Visit)((ComponentCommand)command).getComponents().get("visit")).getPatient().getId(), Long.class);
		projList = CrmsAuthUtils.filterProjectListByPermission(getCurrentUser(request),
				CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
		dynamicLists.put("enrollmentStatus.patientProjects", projList);
		// if adding new visit, and there is no project specified yet, visit types list is empty and
		// will be refreshed when a project is selected		
		if (v.getProjName() != null) {
			dynamicLists.put("visit.visitTypes", listManager.getDynamicList("visit.visitTypes", 
					"projectName", ((Visit)((ComponentCommand)command).getComponents().get("visit")).getProjName(),	String.class));
			dynamicLists.put("visit.visitLocations", listManager.getDynamicList("visit.visitLocations", 
					"projectName", ((Visit)((ComponentCommand)command).getComponents().get("visit")).getProjName(),	String.class));
			dynamicLists.put("project.staffList", listManager.getDynamicList("project.projectStaffList", 
					"projectName", ((Visit)((ComponentCommand)command).getComponents().get("visit")).getProjName(),	String.class));
		}
		else {
			dynamicLists.put("visit.visitTypes", new HashMap<String, String>());
			dynamicLists.put("visit.visitLocations", new HashMap<String, String>());
			dynamicLists.put("project.staffList", new HashMap<String, String>());
			
		}
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}

	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		Visit visitFormBackingObj = (Visit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		// create a separate instance of the visit object for adding to the database. 
		// this is because user can repeatedly add visits, which means that prior to each add, the visit
		// id must be null so that the DAO knows this is an add, not an update. however, since the visit 
		// object just added is associated with a Hibernate session (because JTA transaction spans the entire 
		// request) it will have received an id and setting a Hibernate session entity's id to null (prior to 
		// the next add) results in a Hibernate exception. 
		// therefore, always creating a new instance of a visit object will ensure that the id will be null
		// for the DAO add. this new instance must be a copy of the visitFormBackingObj so that the
		// user data is saved. 
		
		Visit visitDaoObj = visitManager.getVisitPrototype(visitFormBackingObj.getProjName(), visitFormBackingObj.getVisitType());
		initializeNewCommandInstance(context, visitDaoObj); 
		PropertyUtils.copyProperties(visitDaoObj, visitFormBackingObj);

		// now put it into the command object for the save
		((ComponentCommand)command).getComponents().put(getDefaultObjectName(), visitDaoObj);
		
		
		Event result = super.doSaveAdd(context, command, errors);

		List<Instrument> instrumentPrototypes = instrumentManager.getInstrumentPrototypes(visitDaoObj.getProjName(),visitDaoObj.getVisitType());
		
		// do not auto populate if the Visit status is ‘CANCELLED’ or ‘NO SHOW’
		if (!visitDaoObj.getVisitStatus().contains("CANCELED") && !visitDaoObj.getVisitStatus().equals("NO SHOW")) {
			for(Instrument i : instrumentPrototypes){
				//TODO: need to instantiate the actual instrument type instead of Instrument because
				// other things may need to be done for a newly created instrument, such as calling its
				// markUnusedFields method
				i.setPatient(visitDaoObj.getPatient());
				i.setProjName(visitDaoObj.getProjName());
				i.setDcDate(visitDaoObj.dateWithoutTime(visitDaoObj.getVisitDate()));
				i.setDcStatus("Scheduled");
				i.setVisit(visitDaoObj);
				i.save();
			}
		}
		
						
		/* Custom handling may be desired on the instruments (just created) belonging
		 *   to custom visits.  To provide this, we add a hook here, overridable by a
		 *   subclass of visit.  This is preferred over subclassing the instruments themselves
		 *   when no new fields are to be added to the instrument model (requiring new database
		 *   tables).  We also could not use VisitHandler subclasses for this, since they are
		 *   not used during an 'add' event, by design (a visit's project type not known 
		 *   until "saveAdd").  */
		visitDaoObj.afterCreatePrototypedInstruments();
		
		
		
		// successful add visit message
		errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"info.visit.added"}, new Object[]{visitFormBackingObj.getVisitType()}, ""));
		
		// if repeatedly adding new visits, the Visit form backing object must be cleared out
		//  so that a) the id is null, as the DAO uses the id to determine whether doing an update
		//  or an insert, and b) so the fields on the form are cleared after they "Apply"
		if (ActionUtils.getEventName(context).equals("applyAdd")) {
			
			// above, a visit was created and put into command so it would be added to the database, but now put 
			// the form backing visit object back in command for the next add
			((ComponentCommand)command).getComponents().put(getDefaultObjectName(), visitFormBackingObj);
			
			// reset visitType and visitWith to null for next add. can leave projName, visitLocation,  
			// visitDate and visitStatus as is, because the liklihoood is that if user is adding 
			// multiple visits at once these properties will be the same. visit id must be set null so 
			// that DAO knows this is add, not an update
			
			((Visit)((ComponentCommand)command).getComponents().get(getDefaultObjectName())).setVisitType(null);
			((Visit)((ComponentCommand)command).getComponents().get(getDefaultObjectName())).setVisitWith(null);
		}
		
		return result;
	}


	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Event event = new Event(this, SUCCESS_FLOW_EVENT_ID);
		Visit visit = (Visit) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		// keep in mind that a visit's instruments cannot be edited if the status of the visit is 'SCHEDULED'
		

		// if a user is changing the date of a scheduled visit then can assume the patient is rescheduling the visit
		// and can change the instrument data collection date of all visit instruments to the new date.
		
		// also, if the user changes the visitStatus to 'SCHEDULED' then any instruments that had a dcStatus of 'Canceled'
		// should be changed back to 'Scheduled'. This facilitates the situation where a coordinator cancels a visit (so
		// all instruments get their dcStatus set to 'Canceled') and then reschedules the visit.
		
		// note: do not want to do this for completed visits where the instrument data has already been collected
		// because in that case the instrument dcDate may legitimately have been set to a different value than
		// the visit date
		
		if (visit.getVisitStatus().equals("SCHEDULED")) {
			// TODO: this is done without knowing whether the visitDate or visitStatus were actually changed. inefficient. look at changing
			// visitDate and visitStatus to be dirty tracked columns and then check isDirty here
			for (InstrumentTracking instr : visit.getInstruments()) {
				// additionally, while iterating through the instruments, if encounter an instrument that is data entered do
				// not allow the save because a visit cannot have a status of 'SCHEDULED' if there are instruments with data entered.
				// this generally cannot happen because an instrument can not be data entered until the visit status is 'COMPLETE' (or
				// some variation thereof) but a user could attempt to change such a visit's status back to 'SCHEDULED' and that should
				// not be permitted
				if (instr.getDeDate() != null) {
					errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"visit.statusScheduledWithInstrData.command"}, new Object[]{instr.getInstrType()}, ""));
					return new Event(this,ERROR_FLOW_EVENT_ID);
				}
				
				instr.setDcDate(visit.getVisitDate());
				instr.setDcStatus("Scheduled");
			}
		}

		// if the user changes the status of a visit to 'CANCELED' ('CANCELED%') or 'NO SHOW' then the instruments dcStatus
		// should also be set to 'Canceled'
		// however there should first be a check to see if any of the visit instruments have data entered
		if (visit.getVisitStatus().endsWith("CANCELED") || visit.getVisitStatus().equals("NO SHOW")) {
			for (InstrumentTracking instr : visit.getInstruments()) {
				if (instr.getDeDate() != null) {
					errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"visit.statusCanceledWithInstrData.command"}, new Object[]{visit.getVisitStatus(), instr.getInstrType()}, ""));

					return new Event(this,ERROR_FLOW_EVENT_ID);
				}
				instr.setDcStatus("Canceled");
			}
		}
		
		// make sure that the instruments have the same project as the visit (instruments project field cannot be edited so it should be kept 
		// in sync with the visit project)
		for (InstrumentTracking instr : visit.getInstruments()) {
			if (!visit.getProjName().equals(instr.getProjName())) {
				instr.setProjName(visit.getProjName());
			}
		}
	
		
		return super.doSave(context,command,errors);
	}



	@Override
	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Visit visit = (Visit) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		if(visit.hasInstruments()){
			 CoreSessionUtils.addFormError(sessionManager,request, new String[]{DELETE_VISIT_WITH_INSTRUMENTS_ERROR_CODE}, null);
			 return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		return super.doConfirmDelete(context, command, errors);
	}
	
	
	
	
	
	
	
}
