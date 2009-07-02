package edu.ucsf.lava.core.action;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.model.Action;

public class CoreDefaultActionStrategy implements DefaultActionStrategy {
	
	
	
	
	
	public Action getDefaultAction(ActionManager actionManager,HttpServletRequest request, Action actionIn)
	{
		return this.getDefaultAction(actionManager, request, actionIn, ActionUtils.DEFAULT_ACTION_IDENTIFIER);
	}
	
	
	/**
	 * Find the default action based on the current action
	 * 
	 * The action delegate makes sure there is a default action for every section.  If there is 
	 * no default action for the section, then the action delegate sets up the module default action as 
	 * the section default, and if no module default, it uses the default home action. 
	 */
	
	public Action getDefaultAction(ActionManager actionManager,HttpServletRequest request, Action actionIn, String defaultIdentifier)
	{
		String defaultActionId = null;
		ActionRegistry actionRegistry = actionManager.getActionRegistry();
		String instanceName =  actionManager.getWebAppInstanceName();
		
		defaultActionId = ActionUtils.getDefaultActionKeyWithInstance(actionIn.getId(),instanceName, defaultIdentifier);
		if(actionRegistry.containsAction(defaultActionId)){
			return (Action) actionRegistry.getAction(defaultActionId);
		}
		
		defaultActionId = ActionUtils.getDefaultActionKey(actionIn.getId(), defaultIdentifier);
		if(actionRegistry.containsAction(defaultActionId)){
			return (Action) actionRegistry.getAction(defaultActionId);
		}
		
		//if nothing else, then return the default home action id (we should never get here).
		return (Action) actionRegistry.getAction(ActionUtils.getDefaultHomeActionKey(actionIn.getId()));
		
		
		
	}
	
	

}
