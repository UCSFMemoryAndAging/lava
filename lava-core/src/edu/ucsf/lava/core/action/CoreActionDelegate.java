package edu.ucsf.lava.core.action;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.scope.AbstractScopeActionDelegate;
import edu.ucsf.lava.core.session.CoreSessionUtils;

public class CoreActionDelegate extends AbstractScopeActionDelegate {
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    
    
	public CoreActionDelegate() {
		super();
		//the core scope handler should always be first. 
		this.handledScope = CoreSessionUtils.CORE_SCOPE;
		this.order = new Long(0);
		this.defaultActionStrategy = new CoreDefaultActionStrategy();
	}

	

	public ActionRegistry onReloadActionDefinitions(ActionManager actionManager, ActionRegistry registry) {
		
		//Loop through actions making sure all actions have complete record of subFlows and parentFlows
		//and customized flows and customizing flows
		
		MetadataManager metadataManager = CoreManagerUtils.getMetadataManager();
		for (String actionId : registry.getActions().keySet()){
			Action action = registry.getActionInternalCopy(actionId);
			
			//update description if found in metadata....
			action.setDescription(metadataManager.getMessage("action."+action.getId(),new Object[0],action.getDescription(),Locale.US));
			
			
			Iterator iterator = action.getSubFlows().iterator();
			while (iterator.hasNext()){
				String subActionId = (String)iterator.next();
				if(registry.containsAction(subActionId)){
					((Action)registry.getActionInternalCopy(subActionId)).setParentFlow(action.getId());
				}else{
					logger.error("subFlow actionId: "+subActionId + " for actionId: " + actionId + " not found in action registry.");
					iterator.remove();
				}
			}
			iterator = action.getParentFlows().iterator();
			while (iterator.hasNext()){
				String parentActionId = (String)iterator.next();
				if(registry.containsAction(parentActionId)){
					((Action)registry.getActionInternalCopy(parentActionId)).setSubFlow(action.getId());
				}else{
					logger.error("parentFlow actionId: "+parentActionId + " for actionId: " + actionId + " not found in action registry.");
					iterator.remove();
				}
			}
			String customizedActionId = action.getCustomizedFlow();
			if(customizedActionId != null){
				if(registry.containsAction(customizedActionId)){
					((Action)registry.getActionInternalCopy(customizedActionId)).setCustomizingFlow(action.getId());
				}else{
					logger.error("customizedFlow actionId: "+customizedActionId + " for actionId: " + actionId + " not found in action registry.");
					action.clearCustomizedFlow();
				}
			}
			iterator = action.getCustomizingFlows().iterator();
			while (iterator.hasNext()){
				String customizingActionId = (String)iterator.next();
				if(registry.containsAction(customizingActionId)){
					((Action)registry.getActionInternalCopy(customizingActionId)).setCustomizedFlow(action.getId());
				}else{
					logger.error("customizingFlow actionId: "+customizingActionId + " for actionId: " + actionId + " not found in action registry.");
					iterator.remove();
				}
			}
		}
		//now look at customized Flows and make sure the "customizing" flow has all the subflows of the 
		//flow that is being customized.  This means that customizing actions do not need to accurately
		//redefine all the subflows of the action they are customizing (i.e. they will inherit them)
		for (String actionId : registry.getActions().keySet()){
			Action action = registry.getActionInternalCopy(actionId);
			String customizedActionId = action.getCustomizedFlow();
			if(customizedActionId != null){
				if(registry.containsAction(customizedActionId)){
					action.setSubFlows(((Action)registry.getActionInternalCopy(customizedActionId)).getSubFlows());
				}
			}
		}
		
		HashSet<String> actionsKeySet = new HashSet(registry.getActions().keySet()); 
		
		//now add default action aliases for the actions for module and section defaults.
		
		for (String actionId : actionsKeySet){
			Action action = registry.getActionInternalCopy(actionId);
			
			if(action.getHomeDefault()){
				registry.addAction(ActionUtils.getDefaultHomeActionKey(),action);
					
			}
			if(action.getModuleDefault()){
				registry.addAction(ActionUtils.getDefaultModuleActionKey(action.getId()),action);
			}
				
			if(action.getSectionDefault()){
				registry.addAction(ActionUtils.getDefaultActionKey(action.getId()),action);
			}
		}
		
		//make sure there is a default home action.  If not then blow up
		if(!registry.containsAction(ActionUtils.getDefaultHomeActionKey())){
			throw new NoDefaultHomeActionException();
		}
			
		/*
		 * now loop through all actions to make sure that there is an identified default 
		 * action for each section, module.  If there is none, then add an alias for the 
		 * default to the next "lower" default.
		 * 
		 * e.g.  if no section default, then use the module default as section default
		 * 		 if no module default, use default home as the module default. 
		 */
		Set modulesDone = new HashSet();
		Set sectionsDone = new HashSet();
		
		for(String actionId : actionsKeySet){
			if(!ActionUtils.isDefaultActionKey(actionId)){
				if(!modulesDone.contains(ActionUtils.getModule(actionId))){
					if(!registry.containsAction(ActionUtils.getDefaultModuleActionKey(actionId))){
						//use default home action for module default, because no module default specified
						registry.addAction(ActionUtils.getDefaultModuleActionKey(actionId),
								registry.getActionInternalCopy(ActionUtils.getDefaultHomeActionKey()));
					}
					modulesDone.add(ActionUtils.getModule(actionId));
				}
				if(!sectionsDone.contains(ActionUtils.getSection(actionId))){
					if(!registry.containsAction(ActionUtils.getDefaultActionKey(actionId))){
						//use default module action for section default because no section default specified
						//note: we know there is a default module action because we just made sure of that. 
						registry.addAction(ActionUtils.getDefaultActionKey(actionId),
								registry.getActionInternalCopy(ActionUtils.getDefaultModuleActionKey(actionId)));
					}
					sectionsDone.add(ActionUtils.getSection(actionId));
				}
			}
		}
	
		
		return registry;
	}
	
	
	/**
	 * Utility method, if no default action for the section for the default action identifier, find the 
	 * most appropriate 
	 */

}
