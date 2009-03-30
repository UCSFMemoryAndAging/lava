package edu.ucsf.lava.core.scope;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionRegistry;
import edu.ucsf.lava.core.action.model.Action;

/**
 * This interface uses a delegate pattern to enable extension of the ActionManager functionality.  
 * Each lava scope needs to have a delegate implementation.   
 * 
 * @author jhesse
 *
 */
public interface ScopeActionDelegate {
	public String getHandledScope();
	public Long getOrder();
	Action getDefaultAction(ActionManager actionManager,HttpServletRequest request, Action actionIn);
	public ActionRegistry onReloadActionDefinitions(ActionManager actionManager,ActionRegistry registry);
	public boolean shouldBuildFlowsForAction(ActionManager actionManager,String actionId);
	public Action resolveEffectiveAction(ActionManager actionManager,HttpServletRequest request, String actionId);
	public String extractFlowIdFromAction(ActionManager actionManager,HttpServletRequest request, Action action);

		
}
