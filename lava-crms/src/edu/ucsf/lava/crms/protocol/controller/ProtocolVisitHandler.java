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
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfigOption;
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

		// put options in command object for display
		backingObjects.put("visitConfigOptions", protocolVisit.getProtocolVisitConfig().getOptions());

		return backingObjects;
	}
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolVisit protocolVisit = (ProtocolVisit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// generate a list of existing visits that are candidates to fulfill this ProtocolVisit.
		// get all of the existing visits that match the projName and visitType of each ProtocolVisitOptionConfig.
		// order the list by proximity to the visit's scheduling window
		Map allMatchingVisits = new LinkedHashMap();
		
		// rearrange the options list so that the defaults are listed first, to be used in generating a list
		// of projName/visitTypes that can fulfill the visit requirement
		// note: there may be multiple default 
		List<ProtocolVisitConfigOption> visitTypes = new ArrayList<ProtocolVisitConfigOption>();
		for (ProtocolVisitConfigOption option : protocolVisit.getProtocolVisitConfig().getOptions()) {

			
			
			//TODO: defaultOption not implemented yet
			//if (option.getDefaultOption()) {
			//	visitTypes.add(0, new StringBuffer(option.getVisitTypeProjName()).append("=").append(option.getVisitType()).toString());
			//}
			//else {
				// hopefully there is never an '=' character in a projName? (there may be '-' so can not use that)
////					visitTypes.add(new StringBuffer(option.getProjName()).append("=").append(option.getVisitType()).toString());
			//}
		}
		
		//NOTE: currently allows visit from any project to fulfill the protocol, regardless of the protocol's project,
		// to enable fulfillment for co-enrolled patients 
		
		// generate a list of visits for each visitType, ordered by proximity to the visit's
		// timepointConfig's scheduling window
		Date proximityDate = protocolVisit.getProtocolTimepoint().getSchedAnchorDate();
		// if a Visit has not been assigned to the first timepoint yet, the scheduling window and anchor date
		// can not be calculated. in this case use the current date so the result is ordered by the most recently
		// collected instruments
		if (proximityDate == null) {
			proximityDate = new Date();
		}
		// iterate thru the visit options retrieving a list of matching visits for each, and appending
		// each list on the end of the allMatchingVisits Map. 
		// since the mapped ordering of the visit options is by effective date (descending) and within a
		// given effective date by the default flag (where default options come first), this resulting merged 
		// list of matching visits across all options should be in order of most desired matches to least 
		// desired (and from above, within a given option, matches will be ordered by proximity)
		List<String> options = new ArrayList<String>();
		for (ProtocolVisitConfigOption option : protocolVisit.getProtocolVisitConfig().getOptions()) {
			Map matchingVisits = listManager.getDynamicList(getCurrentUser(request), "protocol.matchingVisits", 
					new String[]{"patientId", "visitProjName", "visitType", "schedAnchorDate", "effDate", "expDate"},
					new Object[]{protocolVisit.getPatient().getId(), 
						option.getProjName(),  
						option.getVisitType(), // visitType
						proximityDate, option.getEffectiveEffDate(), option.getEffectiveExpDate()}, 
					new Class[]{Long.class, String.class, String.class, Date.class, Date.class, Date.class});	
//TODO:  ASSUME WILL NEED TO REMOVE THE BLANK ENTRY FROM LISTS AFTER THE FIRST ONE IF MULTIPLE OPTIONS						
			allMatchingVisits.putAll(matchingVisits);
			
			// construct a list of options for the view to informing the user what is valid to aid them
			// if adding a new Visit to assign 
//LOOKING AT JUST CONSTRUCTING THIS IN THE jsp from the options				
			StringBuffer sb = new StringBuffer(option.getProjName()).append("-").append(option.getVisitType());
			sb.append("(").append(option.getEffectiveEffDate()).append("-").append(option.getEffectiveExpDate()).append(")");
			options.add(sb.toString());
		}
		dynamicLists.put("protocol.matchingVisits", allMatchingVisits);

/*** SHOULD NOT NEED ANYMORE. NUKE AFTER TESTING			
			// put the list of options in the model for informational display purposes
			// not using list from above because using different separator
//TODO: include effDate/expDate for each 
			List<String> options = new ArrayList<String>();
			for (ProtocolVisitOptionConfig option : protocolVisit.getProtocolVisitConfig().getOptions()) {
				//TODO: ck effDate,expDate and if not currently effective, ignore					
				//TODO: defaultOption not implemented yet
				//if (option.getDefaultOption()) {
				//	visitTypes.add(0, new StringBuffer(option.getVisitTypeProjName()).append("=").append(option.getVisitType()).toString());
				//}
				//else {
					options.add(new StringBuffer(option.getVisitTypeProjName()).append("-").append(option.getVisitType()).toString());
				//}
			}
***/			
		model.put("visitConfigOptions", options);
		
		model.put("dynamicLists", dynamicLists);
		
		// so protocolConfig label can appear on page title 
		model.put("protocolConfigLabel", protocolVisit.getProtocolTimepoint().getProtocol().getProtocolConfig().getLabel());
		
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
				
				//TODO: see subFlowReturnHook comments on what to do about the visit's instruments
			}
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
	
	/**
	 * Override as this method is invoked when returning from a subflow, and for the Add Visit subflow, need to
	 * validate and add the newly created Visit, and if a visit template resulted in creation of instruments when
	 * the Visit was created, validate and add those too.
	 */
	public void subFlowReturnHook(RequestContext context, Object command, BindingResult errors) throws Exception {
		ProtocolVisit protocolVisit = (ProtocolVisit)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		// do not call super.subFlowReturnHook as do not want to refresh this upon return from the Add Visit subflow. 
		// that would result in losing any pending modifications the user may have made.
		
		// the Edit ProtocolVisit flow is the parent flow and spawns the Add Visit subflow. because the user may have
		// made edits when they click Add Visit, the edits are neither saved or cancelled; rather they remain in
		// the command object in the Edit ProtocolVisit flow's flow scope, and when the Add Visit subflow returns
		// they are still in flow scope
		
		// note about the mechanisms used to maintain pending user modifications: 
		// 1) eventButton.tag is used for Add Visit so that the form is posted and properties are submitted. additionally, 
		//    the transition from an edit flow to a subflow binds properties to the command object. 
		// 2) the event for the subflow is set as a combination of the "component" and "action" parameters to eventButton.tag
		//    i.e. component="visit" action="add" results in eventId = "visit__add"
		//    since lava.crms.scheduling.visit.visit has been configured as a subflow of lava.crms.protocol.assignment.protocolVisit,
		//    the submission of the "visit__add" event results in the pausing of the Edit ProtocolVisit flow and invokes 
		//    the Add Visit subflow
		// 3) when the user closes the Add Visit subflow, this method is called as soon as the Edit ProtocolVisit resumes. 
		//    the Add Visit subflow passes the id of the newly created visit via a flow output mapper which puts it in the
		//    flow scope of the Edit Protocol visit flow in "subflowEntityId". also, the action id of the Add Visit subflow
		//    is mapped to flow scope in "subflowActionId". this would be particularly useful if the Edit ProtocolVisit flow
		//    spawned multiple subflows and needed to know which subflow was returning.
		// 4) after this method completes, the Edit ProtocolVisit flow resumes, rendering its view (with any pending user
		//    modifications intact), and awaits user input
		
		String subflowActionId = context.getFlowScope().getString("subflowActionId");
		// if subflowActionId not defined, must be returning from a subflow that does not map it back, e.g. 
		// protocolVisit edit returning to protocolVisit view. this can be ignored, as the only subflow
		// return to process is Add Visit and that will define subflowActionId
		if (subflowActionId != null && ActionUtils.getTarget(subflowActionId).equals("visit")) {
			Long subflowEntityId = context.getFlowScope().getLong("subflowEntityId");
			if (subflowEntityId != null) { // if user cancelled the Add Visit there will be no subflowEntityId is flowScope
				Visit v = (Visit) Visit.MANAGER.getById(subflowEntityId);
				
				//TODO: the plan is to pass the allowable ProjName/VisitTypes to Add Visit so it will restrict the Add
				//to those, guaranteeing that the added Visit fulfills the Protocol Visit Configuration
				//however, until that is done, validate the ProjName/VisitType after the fact
				
				// validate the projName and visit type against those configured as options for this ProtocolVisit
				boolean typeMatch = false, dateMatch = false;
				ProtocolVisitConfigOption optionMatched = null; // used for error messaging
				for (ProtocolVisitConfigOption option : protocolVisit.getProtocolVisitConfig().getOptions()) {
					if (v.getProjName().equals(option.getProjName()) && v.getVisitType().equals(option.getVisitType())) {
						typeMatch = true;
						// now check whether the instrument is within the effective window for the option
						if ((option.getEffectiveEffDate() == null || !v.getVisitDate().before(option.getEffectiveEffDate())) 
								&& (option.getEffectiveExpDate() == null || !v.getVisitDate().after(option.getEffectiveExpDate()))) {
							optionMatched = option;
							dateMatch = true;
							break;
						}
					}
				}
				if (!typeMatch) {
					StringBuffer visitTypesSb = new StringBuffer();
					for (ProtocolVisitConfigOption option : protocolVisit.getProtocolVisitConfig().getOptions()) {
						visitTypesSb.append("  [").append(option.getProjName()).append(" - ").append(option.getVisitType()).append("]");
					}
					LavaComponentFormAction.createCommandError(errors, "protocol.visitTypeMismatch", new String[]{v.getVisitDescrip(),visitTypesSb.toString()});
				}
				else if (!dateMatch) {
					LavaComponentFormAction.createCommandError(errors, "protocol.instrDateMismatch", new Object[]{v.getVisitDescrip(), v.getVisitDate(), optionMatched.getEffectiveEffDate(), optionMatched.getEffectiveExpDate()});
				}
				else {
					// only set the id here, not the Visit object itself, as this needs to simulate user select of a Visit. if call
					// setVisit here, the visitId and visit.getId will be the same so on save it will think nothing has changed and
					// the new visit will not be assigned
					protocolVisit.setVisitId(v.getId());
					LavaComponentFormAction.createCommandError(errors, "info.protocol.visitAssigned", new String[]{v.getVisitDescrip()});
					
					// how to handle the new visits instruments. 
					
					// how to distinguish visit template instruments that were added when the visit was added? read the prototype
					// for the visit project and type?
					
					// what to do if ProtocolInstrument already has an associated instrument?
					
					// UI to present user with option of associating instruments:
					// secondary component on protocolVisit.jsp which is enabled via a flag and displays
					// all instruments associated with the visit. use group selection to select a group of 
					// visits. note this would requite VisitInstrumentsHandler as a secondary flow and
					// InstrumentGroupHandler as a tertiary flow and not even sure it would work. 
					// create a tag or jsp code similar to listSelectedItemsGroupRow.tag, where
					// eventActionButton should have 'instrumentGroup' as event prefix so the InstrumentGroupHandler
					// creates the group, but then rather than transitioning to the GroupFlowBuilder, in the
					// the EntityViewFlowBuilder have a transition on instrumentGroup back to the view,
					// but with an action (method in this class) that a) associates selected instruments
					// with ProtocolInstrument (where possible) and flags so that the list of instruments
					// is not presented anymore (the tree list will be displayed and that could display
					// each of the selected instruments)
					
					
				}
			}
		}
		else {
			super.subFlowReturnHook(context, command, errors);
		}
	}
	
}
