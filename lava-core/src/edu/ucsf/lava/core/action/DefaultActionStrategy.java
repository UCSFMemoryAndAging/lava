package edu.ucsf.lava.core.action;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.model.Action;

/**
 * A Strategy pattern interface used to abstract out the logic for determining what the 
 * default action is given an existing current action (e.g. what action to redirect to 
 * when an application context change or user input invalidates the current action).  
 * 
 * This strategy interface is used by the ScopeActionDelegate implementation to enable 
 * default action strategies to inherit functionality from other implementations independent
 * of (but parallel to) the ScopeActionDelegate implementation class heirarchy. 
 * @author jhesse
 *
 */
public interface DefaultActionStrategy {

	public Action getDefaultAction(ActionManager actionManager,HttpServletRequest request,  Action actionIn);

}
