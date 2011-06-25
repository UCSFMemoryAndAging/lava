package edu.ucsf.lava.crms.scheduling.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
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
		visitDaoObj = (Visit)visitFormBackingObj.clone(); 

		// now put it into the command object for the save
		((ComponentCommand)command).getComponents().put(getDefaultObjectName(), visitDaoObj);
		
		
		Event result = super.doSaveAdd(context, command, errors);
		//TODO: need to instantiate the actual instrument type instead of Instrument because
		// other things may need to be done for a newly created instrument, such as calling its
		// markUnusedFields method
		List<Instrument> instrumentPrototypes = instrumentManager.getInstrumentPrototypes(visitDaoObj.getProjName(),visitDaoObj.getVisitType());
		for(Instrument i : instrumentPrototypes){
			i.setPatient(visitDaoObj.getPatient());
			i.setProjName(visitDaoObj.getProjName());
			i.setDcDate(visitDaoObj.dateWithoutTime(visitDaoObj.getVisitDate()));
			i.setDcStatus("Scheduled");
			i.setVisit(visitDaoObj);
			i.save();
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
