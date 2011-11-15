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
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitOptionConfig;
import edu.ucsf.lava.crms.scheduling.model.Visit;

public class ProtocolVisitHandler extends CrmsEntityComponentHandler {

	public ProtocolVisitHandler() {
		super();
		setHandledEntity("protocolVisit", edu.ucsf.lava.crms.protocol.model.ProtocolVisit.class);
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

		ProtocolVisit protocolVisit = (ProtocolVisit) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("visitId", protocolVisit.getId()));
		ProtocolTracking protocolVisitTree = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.visitTree", filter);
		backingObjects.put("protocolVisitTree", protocolVisitTree);
		return backingObjects;
	}
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolVisit protocolVisit = (ProtocolVisit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		Map allMatchingVisits = new LinkedHashMap();
		
		if (protocolVisit.getTimepoint().getClass().isAssignableFrom(ProtocolAssessmentTimepoint.class)) {
			// TODO: will there be a standard mechanism for updating Protocol calculations when entering Protocol handlers?
			// more likely, an update will commence whenever there are modifications to the protocol configuration,
			// the protocol, and any associated entities which are fulfilling the protocol configuration 
			protocolVisit.getTimepoint().calcSchedWinDates();
				
			// create a list of visitTypes that can fulfill the visit requirement. put the default
			// visitType at the beginning of the list
			List<String> visitTypes = new ArrayList<String>();
			for (ProtocolVisitOptionConfig option : protocolVisit.getVisitConfig().getVisitOptions()) {
				//TODO: ck effDate,expDate and if not currently effective, ignore						
				if (option.getDefaultOption()) {
					visitTypes.add(0, option.getVisitType());
				}
				else {
					visitTypes.add(option.getVisitType());
				}
			}
			
			// generate a list of visits for each visitType, ordered by proximity to the visit's
			// timepointConfig's scheduling window
			for (String visitType : visitTypes) {
				Map matchingVisits = listManager.getDynamicList(getCurrentUser(request), "protocol.matchingVisits", 
						new String[]{"patientId", "visitType", "schedWinDate"},
						new Object[]{protocolVisit.getPatient().getId(), 
							visitType, 
							protocolVisit.getTimepoint().getSchedWinPivot()}, 
						new Class[]{Long.class, String.class, Date.class});	
	//ASSUME WILL NEED TO REMOVE THE BLANK ENTRY FROM LISTS AFTER THE FIRST ONE						
				allMatchingVisits.putAll(matchingVisits);
			}
			dynamicLists.put("protocol.matchingVisits", allMatchingVisits);
		}
		
		model.put("dynamicLists", dynamicLists);
		
		// so protocolConfig label can appear on page title 
		model.put("protocolConfigLabel", protocolVisit.getTimepoint().getProtocol().getProtocolConfig().getLabel());
		
		return super.addReferenceData(context, command, errors, model);
	}
	

	/**
	 * Helper method to facilitate changing the Visit associated with the ProtocolVisit
	 * after the user has changed the visit in the view, binding a new visitId.
	 * 
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handleVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolVisit protocolVisit = (ProtocolVisit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(protocolVisit.getVisitId(),protocolVisit.getVisit())){
			if(protocolVisit.getVisitId()==null){
				protocolVisit.setVisit(null); 	//clear the association
			}else{
				Visit visit = (Visit) Visit.MANAGER.getById(protocolVisit.getVisitId(), Visit.newFilterInstance(getCurrentUser(request)));
				protocolVisit.setVisit(visit);
			}
		}
		else {
			protocolVisit.setVisit(null); 	//clear the association
			protocolVisit.setVisitId(null);
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
