package edu.ucsf.lava.crms.action;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionRegistry;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.scope.AbstractScopeActionDelegate;
import edu.ucsf.lava.crms.action.model.CrmsAction;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;
import edu.ucsf.lava.crms.webflow.builder.InstrumentCommonFlowTypeBuilder;



public class CrmsActionDelegate extends AbstractScopeActionDelegate {

	public static final String CRMS_PATIENT_AUTH_FILTER = "patientAuth";
	public static final String CRMS_PROJECT_AUTH_FILTER = "projectAuth";

	public static String DEFAULT_PATIENT_ACTION = "defaultPatientAction";
	
	public CrmsActionDelegate() {
		super();
		this.handledScope = CrmsSessionUtils.CRMS_SCOPE;
		this.defaultActionStrategy = new CrmsDefaultActionStrategy();
	}


	/**
	 * Loop through all the actions in the registry (after the core action handler has configured the registry) 
	 * and add defaultPatientAction keys to the registry.  This supports the distinction between default action 
	 * lookups when there is a current patient and when there is no current patient. 
	 */
	public ActionRegistry onReloadActionDefinitions(ActionManager actionManager, ActionRegistry registry) {
		HashSet<String> actionsKeySet = new HashSet(registry.getActions().keySet()); 
		
		//loop through actions adding aliases for default patient actions
		for (String actionId : actionsKeySet){
			if(!ActionUtils.isDefaultActionKey(actionId)){
			
				Action action = registry.getActionInternalCopy(actionId);
				if(CrmsAction.class.isAssignableFrom(action.getClass())){
					CrmsAction crmsAction = (CrmsAction)action;
					if(crmsAction.getPatientModuleDefault()){
						registry.addAction(ActionUtils.getDefaultModuleActionKey(action.getId(),DEFAULT_PATIENT_ACTION),action);
					}
				
					if(crmsAction.getPatientSectionDefault()){
						registry.addAction(ActionUtils.getDefaultActionKey(action.getId(),DEFAULT_PATIENT_ACTION),action);
					}
					if(crmsAction.getPatientHomeDefault()){
						registry.addAction(ActionUtils.getDefaultHomeActionKey(DEFAULT_PATIENT_ACTION),action);
					}
				}
			}
		}
		
		
		//if not patient home default then make it the same as the default home action 
		if(!registry.containsAction(ActionUtils.getDefaultHomeActionKey(DEFAULT_PATIENT_ACTION))){
			registry.addAction(ActionUtils.getDefaultHomeActionKey(DEFAULT_PATIENT_ACTION),
								registry.getActionInternalCopy(ActionUtils.getDefaultHomeActionKey()));
		}
		
		/*	loop through actions again making sure each section has a patient default, if not then make the section default the patient section default.
		 *  Do the same for module.  
		 */
		Set modulesDone = new HashSet();
		Set sectionsDone = new HashSet();
	
		for (String actionId : actionsKeySet){
			if(!ActionUtils.isDefaultActionKey(actionId)){
				if(!modulesDone.contains(ActionUtils.getModule(actionId))){
					if(!registry.containsAction(ActionUtils.getDefaultModuleActionKey(actionId,DEFAULT_PATIENT_ACTION))){
						registry.addAction(ActionUtils.getDefaultModuleActionKey(actionId,DEFAULT_PATIENT_ACTION),
								registry.getActionInternalCopy(ActionUtils.getDefaultModuleActionKey(actionId)));
					}
					modulesDone.add(ActionUtils.getModule(actionId));
				}
			
				if(!sectionsDone.contains(ActionUtils.getSection(actionId))){
					if(!registry.containsAction(ActionUtils.getDefaultActionKey(actionId,DEFAULT_PATIENT_ACTION))){
						registry.addAction(ActionUtils.getDefaultActionKey(actionId,DEFAULT_PATIENT_ACTION),
								registry.getActionInternalCopy(ActionUtils.getDefaultActionKey(actionId)));
					}
					sectionsDone.add(ActionUtils.getSection(actionId));
				}
			}
		}
		return registry;
	}


	/**
	 * Override to ignore actions that use the InstrumentCommonFlowTypeBuilder. 
	 */
	public boolean shouldBuildFlowsForAction(ActionManager actionManager, String actionId) {
		Action action = actionManager.getAction(actionId);
		if(action==null || action.getFlowType().equals(InstrumentCommonFlowTypeBuilder.INSTRUMENT_COMMON_FLOW_TYPE)){
			return false;
		}
		return super.shouldBuildFlowsForAction(actionManager, actionId);
	}


	/**
	 * Determine the flow id.
	 * 
	 */
	public String extractFlowIdFromAction(ActionManager actionManager, HttpServletRequest request, Action action) {
		
		String actionMode = ActionUtils.getActionMode(request);
		if (actionMode.length() == 0) {
			actionMode = action.getDefaultFlowMode();
		}
		
		//    for instrument entity CRUD flows, the TARGET_ID_INDEX part is changed from 
		//    instrTypeEncoded (e.g. "cdr") to "instrument", meaning that there is just one
		//    instrument flow definition for each CRUD operation (add,delete,enter,collect,view)
		//    shared across all instruments, because instruments have identical flow definitions
		//    unless there is a need for a custom flow definition. this also simplifies building 
		//    flow definitions for instrument list flows, because there need only be one set of 
		//    instrument CRUD events and subflows for add/delete/enter/collect/view, instead of a 
		//    set for each instrument type, which would make for a very large flow definition
		// 2a) the caveat is that if there is an instrument specific flow, then the target is not
		//    changed, e.g. lava.crms.assessment.instrument.medications has flows built for it because
		//    it needs the MedicationsHandler for instrument specific handling
		// 3) diagnosis is a special case of instrument, where the CRUD flows for any version of 
		//    the diagnosis actions map to a generic flow where the TARGET_ID_INDEX part is
		//    "macdiagnosis", e.g. action lava.crms.assessment.diagnosis.macdiagnosis2_7_0.enter
		//    maps to flow id lava.crms.assessment.diagnosis.macdiagnosis.enter. 

    	 
		// if this is an instrument entity CRUD flow which uses common flow definitions, special handling is
		// required to modify the target part, as explained above, so that the flow is shared
		// however, if an instrument has its own flow (so it can have its own custom handler), i.e. flowType == "instrument", 
		// then it has instrument specific flow id's (e.g. assessment.instrument.medications.view|enter|..) and should not be 
		// given the assessment.instrument.instrument.view|enter|... flow id's
		
		String actionId = action.getId();
		
		if (ActionUtils.getScope(actionId).equals("crms") 
				&& ActionUtils.getModule(actionId).equals("assessment") 
				&& ActionUtils.getSection(actionId).equals("instrument")
				&& action.getFlowType().equals(InstrumentCommonFlowTypeBuilder.INSTRUMENT_COMMON_FLOW_TYPE)) {				
			
			Action instrumentCommonAction = (Action) action.clone();
			instrumentCommonAction.setTarget("instrument");
			actionId = instrumentCommonAction.getId();
		} 
		//TODO: refactor out into an instance specific action handler. 
		else if(ActionUtils.getScope(actionId).equals("crms") &&
				ActionUtils.getModule(actionId).equals("assessment") && 
				ActionUtils.getSection(actionId).equals("diagnosis") &&
				ActionUtils.getTarget(actionId).startsWith("macdiagnosis")) {
				//macdiagnosis versions all share the same flow id's, assessment.diagnosis.macdiagnosis.view|enter..., so
		     	//here effectively mapping all versions of macdiagnosis to the same flow id
				Action macDiagnosisAction = (Action) action.clone();
				macDiagnosisAction.setTarget("macdiagnosis");
				actionId = macDiagnosisAction.getId();
		}else{
			//not a special case for crms actions, so call the superclass. 
			return super.extractFlowIdFromAction(actionManager, request,action);
		}
		return new StringBuffer(actionId).append(ActionUtils.ACTION_ID_DELIMITER).append(actionMode).toString();
		
	}


	
	
}
	

