package edu.ucsf.lava.crms.protocol.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolAssessmentTimepoint;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolVisit;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointBase;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitOption;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitOptionBase;
import edu.ucsf.lava.crms.scheduling.model.Visit;

public class PatientProtocolVisitHandler extends CrmsEntityComponentHandler {

	public PatientProtocolVisitHandler() {
		super();
		setHandledEntity("patientProtocolVisit", edu.ucsf.lava.crms.protocol.model.PatientProtocolVisit.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{});
	    return getRequiredFields();
	}
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) return backingObjects;

		PatientProtocolVisit patientProtocolVisit = (PatientProtocolVisit) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("visitId", patientProtocolVisit.getId()));
		PatientProtocolTracking patientProtocolVisitTree = (PatientProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("patientProtocol.visitTree", filter);
		backingObjects.put("patientProtocolVisitTree", patientProtocolVisitTree);
		return backingObjects;
	}
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		PatientProtocolVisit patientProtocolVisit = (PatientProtocolVisit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		Map allMatchingVisits = new LinkedHashMap();
		
		if (patientProtocolVisit.getPatientProtocolTimepoint().getClass().isAssignableFrom(PatientProtocolAssessmentTimepoint.class)) {
			// SEEMS LIKE THIS SHOULD BE DONE BY SOME OTHER MECHANISM, e.g. each time PatientProtocolTimepoint is loaded,
			// but for now do it here
			((PatientProtocolAssessmentTimepoint)patientProtocolVisit.getPatientProtocolTimepoint()).calcSchedWinDates();
				
			// create a list of visitTypes that can fulfill the visit requirement. put the default
			// visitType at the beginning of the list
			List<String> visitTypes = new ArrayList<String>();
			for (ProtocolVisitOptionBase optionBase : patientProtocolVisit.getProtocolVisit().getOptions()) {
				ProtocolVisitOption option = (ProtocolVisitOption) optionBase;
				//TODO: ck effDate,expDate and if not currently effective, ignore						
				if (option.getDefaultOption()) {
					visitTypes.add(0, option.getVisitType());
				}
				else {
					visitTypes.add(option.getVisitType());
				}
			}
			
			// generate a list of visits for each visitType, ordered by proximity to the visit's
			// timepoint's scheduling window
			for (String visitType : visitTypes) {
				Map matchingVisits = listManager.getDynamicList(getCurrentUser(request), "patientProtocol.matchingVisits", 
						new String[]{"patientId", "visitType", "schedWinDate"},
						new Object[]{patientProtocolVisit.getPatient().getId(), 
							visitType, 
							((PatientProtocolTimepoint)patientProtocolVisit.getPatientProtocolTimepoint()).getSchedWinPivot()}, 
						new Class[]{Long.class, String.class, Date.class});	
	//ASSUME WILL NEED TO REMOVE THE BLANK ENTRY FROM LISTS AFTER THE FIRST ONE						
				allMatchingVisits.putAll(matchingVisits);
			}
			dynamicLists.put("patientProtocol.matchingVisits", allMatchingVisits);
		}
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	

	/**
	 * Helper method to facilitate changing the Visit associated with the PatientProtocolVisit
	 * after the user has changed the visit in the view, binding a new visitId.
	 * 
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handleVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		PatientProtocolVisit patientProtocolVisit = (PatientProtocolVisit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(patientProtocolVisit.getVisitId(),patientProtocolVisit.getVisit())){
			if(patientProtocolVisit.getVisitId()==null){
				patientProtocolVisit.setVisit(null); 	//clear the association
			}else{
				Visit visit = (Visit) Visit.MANAGER.getById(patientProtocolVisit.getVisitId(), Visit.newFilterInstance(getCurrentUser(request)));
				patientProtocolVisit.setVisit(visit);
			}
		}
		else {
			patientProtocolVisit.setVisit(null); 	//clear the association
			patientProtocolVisit.setVisitId(null);
		}
	}
	
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleVisitChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleVisitChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}
	
}
