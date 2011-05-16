package edu.ucsf.lava.crms.action;

import java.util.HashSet;
import java.util.Set;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionRegistry;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.scope.AbstractScopeActionDelegate;
import edu.ucsf.lava.crms.action.model.CrmsAction;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


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
						//use default module action for section default because no section default specified
						//note: we know there is a default module action because we just made sure of that. 								

						//EMORY change: since section defaultPatientAction was not found, our next best action
						// is the module _defaultPatientAction_, not the module defaultAction
						if(registry.containsAction(ActionUtils.getDefaultModuleActionKey(actionId,DEFAULT_PATIENT_ACTION))){
							registry.addAction(ActionUtils.getDefaultActionKey(actionId,DEFAULT_PATIENT_ACTION),
								registry.getActionInternalCopy(ActionUtils.getDefaultModuleActionKey(actionId,DEFAULT_PATIENT_ACTION)));
						} else {
							// EMORY note: possible TODO: we may wish the default action to now be the _section_ defaultAction
							//   before assigning it the module defaultAction

							registry.addAction(ActionUtils.getDefaultActionKey(actionId,DEFAULT_PATIENT_ACTION),
								registry.getActionInternalCopy(ActionUtils.getDefaultModuleActionKey(actionId)));
						}
					}
					sectionsDone.add(ActionUtils.getSection(actionId));
				}
			}
		}
		return registry;
	}

}
	

