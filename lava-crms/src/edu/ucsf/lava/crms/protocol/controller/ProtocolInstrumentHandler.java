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
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrument;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentConfigOption;

public class ProtocolInstrumentHandler extends CrmsEntityComponentHandler {

	public ProtocolInstrumentHandler() {
		super();
		setHandledEntity("protocolInstrument", edu.ucsf.lava.crms.protocol.model.ProtocolInstrument.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{});
	    return getRequiredFields();
	}

	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrument protocolInstrument = (ProtocolInstrument)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// generate a list of existing instruments that are candidates to fulfill this ProtocolInstrument
		// get all of the existing instruments that match the instrType of each ProtocolInstrumentOptionConfig.
		// order the list by proximity to the instrument's collection window
		Map allMatchingInstrs = new LinkedHashMap();

//TODO: just use instrument options directly in jsp		
		// create a list of instrTypes that can fulfill the instrument requirement. put the default
		// instrType at the beginning of the list
		List<String> instrTypes = new ArrayList<String>();
		for (ProtocolInstrumentConfigOption option : protocolInstrument.getProtocolInstrumentConfig().getOptions()) {
			//TODO: ck effDate,expDate and if not currently effective, ignore						
			//TODO: defaultOption not implemented yet
			//if (option.getDefaultOption()) {
			//	instrTypes.add(0, option.getInstrType());
			//}
			//else {
				instrTypes.add(option.getInstrType());
			//}
		}
		// put in model for informational display purposes
		model.put("instrConfigOptions", instrTypes);
		
		// generate a list of instruments for each instrType, ordered by data collection date proximity to the 
		// ProtocolInstrumentConfig's data collection window
		Date proximityDate = protocolInstrument.getCollectAnchorDate();
		// if a Visit has not been assigned such that the collection window and anchor date can not be calculated
		// yet, use the current date so the result is ordered by the most recently collected instruments
		if (proximityDate == null) {
			proximityDate = new Date();
		}
		
		// iterate thru the instrument options retrieving a list of instruments for each, and appending
		// each list on the end of the allMatchingInstrs Map. 
		// since the mapped ordering of the instrument options is by effective date (descending) and within a
		// given effective date by the default flag (where default options come first), this resulting merged 
		// list of matching instruments across all options should be in order of most desired matches 
		// to least desired (and from above, within a given option, matches will be ordered by proximity)
		for (ProtocolInstrumentConfigOption option : protocolInstrument.getProtocolInstrumentConfig().getOptions()) {
			Map matchingInstrs = listManager.getDynamicList(getCurrentUser(request), "protocol.matchingInstruments", 
					new String[]{"patientId", "instrType", "collectAnchorDate", "effDate", "expDate"},
					new Object[]{protocolInstrument.getPatient().getId(), 
						option.getInstrType(), 
						proximityDate, option.getEffectiveEffDate(), option.getEffectiveExpDate()}, 
					new Class[]{Long.class, String.class, Date.class, Date.class, Date.class});	
//ASSUME WILL NEED TO REMOVE THE BLANK ENTRY FROM LISTS AFTER THE FIRST ONE IF MULTIPLE OPTIONS						
		
			allMatchingInstrs.putAll(matchingInstrs);
		}
		dynamicLists.put("protocol.matchingInstruments", allMatchingInstrs);
		
		model.put("dynamicLists", dynamicLists);
		
		// so protocolConfig label can appear on page title 
		model.put("protocolConfigLabel", protocolInstrument.getProtocolVisit().getProtocolTimepoint().getProtocol().getProtocolConfig().getLabel());
		
		return super.addReferenceData(context, command, errors, model);
	}
	
	/**
	 * Helper method to facilitate changing the Instrument associated with the ProtocolInstrument
	 * after the user has changed the instrument in the view, binding a new instrId.
	 * 
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handleInstrumentChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrument protocolInstrument = (ProtocolInstrument)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(protocolInstrument.getInstrId(),protocolInstrument.getInstrument())){
			if(protocolInstrument.getInstrId()==null){
				protocolInstrument.setInstrument(null); 	//clear the association
			}else{
				InstrumentTracking instrTracking = (InstrumentTracking) InstrumentTracking.MANAGER.getById(protocolInstrument.getInstrId(), Instrument.newFilterInstance(getCurrentUser(request)));
				Class instrClass = instrumentManager.getInstrumentClass(instrTracking.getInstrTypeEncoded());
				Instrument instr = (Instrument) Instrument.MANAGER.getById(instrClass, protocolInstrument.getInstrId());
				protocolInstrument.setInstrument(instr);
			}
		}
	}
	
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleInstrumentChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleInstrumentChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}
	
	/**
	 * Override as this method is invoked when returning from a subflow, and for the Add Instrument subflow, need to
	 * validate and add the newly created instrument.
	 */
	public void subFlowReturnHook(RequestContext context, Object command, BindingResult errors) throws Exception {
		ProtocolInstrument protocolInstrument = (ProtocolInstrument)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		// do not call super.subFlowReturnHook as do not want to refresh this upon return from the Add Instrument subflow. 
		// that would result in losing any pending modifications the user may have made.
		
		// the Edit ProtocolInstrument flow is the parent flow and spawns the Add Instrument subflow. because the user 
		// may have made edits when they click Add Instrument, the edits are neither saved or cancelled; rather they 
		// remain in the command object in the Edit ProtocolInstrument flow's flow scope, and when the Add Instrument
		// subflow returns they are still in flow scope
		// note: comments in ProtocolVisitHandler subFlowReturnHook explain the mechanics of invoking subflows from edit 
		// flows in detail
		
		
		//TODO: disable selection of instrument (via add or existing) if Visit not associated with its ProtocolVisit yet
		//TODO: on Add Instrument, fix the visitType to ProtocolVisit visit.visitType (and ideally, restrict the list of
		//instrTypes to add to the list of ProtocolInstrumentOptionConfig options. could do both of these by passing params
		//to the Add Instrument flow and modify InstrumentHandler to process these (in same way, could restrict Add Visit
		//visitTypes to those defined in its options)
		
		String subflowActionId = context.getFlowScope().getString("subflowActionId");
		// if subflowActionId not defined, must be returning from a subflow that does not map it back, e.g. 
		// protocolInstrument edit returning to protocolInstrument view. this can be ignored, as the only subflow
		// return to process is Add Visit and that will define subflowActionId
		if (subflowActionId != null && ActionUtils.getSection(subflowActionId).equals("instrument")) {
			Long subflowEntityId = context.getFlowScope().getLong("subflowEntityId");
			if (subflowEntityId != null) { // if user cancelled the Add Visit there will be no subflowEntityId is flowScope
				// instruments have to be retrieved by type, so even if know the id, can not retrieve it without the proper type,
				// so have to get an InstrumentTracking object for the instrument, and then get the instrument 
				InstrumentTracking instrTracking = (InstrumentTracking) InstrumentTracking.MANAGER.getById(subflowEntityId);
				Class instrClass = instrumentManager.getInstrumentClass(instrTracking.getInstrTypeEncoded());
				Instrument instr = (Instrument) Instrument.MANAGER.getById(instrClass, subflowEntityId);
				
				//TODO: possibly: pass the allowable InstrType to Add Instrument so it will restrict the Add
				//to those, guaranteeing that the added Instrument fulfills the Protocol Instruemnt Configuration
				//however, until that is done, validate the InstrType after the fact
				
				// validate the instrument type against the instrument types configured as options for this ProtocolInstrument
				boolean typeMatch = false, dateMatch = false;
				ProtocolInstrumentConfigOption optionMatched = null; // used for error messaging
				for (ProtocolInstrumentConfigOption option : protocolInstrument.getProtocolInstrumentConfig().getOptions()) {
					if (instr.getInstrType().equals(option.getInstrType())) {
						typeMatch = true;
						// now check whether the instrument is within the effective window for the option
						if ((option.getEffectiveEffDate() == null || !instr.getDcDate().before(option.getEffectiveEffDate())) 
								&& (option.getEffectiveExpDate() == null || !instr.getDcDate().after(option.getEffectiveExpDate()))) {
							optionMatched = option;
							dateMatch = true;
							break;
						}
					}
				}
				if (!typeMatch) {
					StringBuffer instrTypesSb = new StringBuffer();
					for (ProtocolInstrumentConfigOption option : protocolInstrument.getProtocolInstrumentConfig().getOptions()) {
						instrTypesSb.append("  [").append(option.getInstrType()).append("]");
					}
					LavaComponentFormAction.createCommandError(errors, "protocol.instrTypeMismatch", new String[]{instr.getInstrType(), instrTypesSb.toString()});
				}
				else if (!dateMatch) {
					LavaComponentFormAction.createCommandError(errors, "protocol.instrDateMismatch", new Object[]{instr.getInstrType(), instr.getDcDate(), optionMatched.getEffectiveEffDate(), optionMatched.getEffectiveExpDate()});
				}
				else {
					// only set the id here, not the Instrument object itself, as this needs to simulate user select of an Instrument. if call
					// setInstrument here, the instrId and instrument.getId will be the same so on save it will think nothing has changed and
					// the new instrument will not be assigned
					protocolInstrument.setInstrId(instr.getId());
					LavaComponentFormAction.createCommandError(errors, "info.protocol.instrAssigned", new String[]{instr.getInstrType()});
				}
			}
		}
		else {
			super.subFlowReturnHook(context, command, errors);
		}
	}
	
	
}
