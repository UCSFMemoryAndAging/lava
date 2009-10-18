package edu.ucsf.lava.core.scope;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionRegistry;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.DefaultActionStrategy;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.webflow.builder.FlowTypeBuilder;

/**
 * A base class that implements the core functionality and interface of a ScopeActionDelegate.
 * 
 * @author jhesse
 *
 */
public abstract class AbstractScopeActionDelegate implements Comparable<ScopeActionDelegate>, ScopeActionDelegate {

	protected static Long DEFAULT_ORDER=new Long(1000);
	
	protected ScopeActionDelegates scopeActionDelegates;
	protected String handledScope;
	protected Long order;
    protected DefaultActionStrategy defaultActionStrategy;    
	    
	
    


	/**
	 * Determine the default action based on the action passed in, defers logic using a strategy pattern
	 */
	public Action getDefaultAction(ActionManager actionManager,HttpServletRequest request, Action actionIn) {
		return defaultActionStrategy.getDefaultAction(actionManager,request, actionIn);
	}

	/**
	 * Determines what the actual action should be based on the current runtime conditions.  This base method 
	 * simply determines whether there is an instance specific customization for the action for the current
	 * runtime instance.  This should be subclassed in scope handlers if their are scope specific considerations
	 * that need to be examined to determine the effective action from a given action id.
	 * 
	 *  Note: the HttpServletRequest will be null when called outside the context of a current request (e.g. from
	 *  the flow builders).  Overrides of this method in subclasses need to check the request for null and degrade appropriately. 
	 */
	public Action resolveEffectiveAction(ActionManager actionManager, HttpServletRequest request, String actionId, ActionRegistry actionRegistry){
		
		//instance scope, e.g. demo.core.admin.auth.authUsers 
		String instanceActionId = ActionUtils.getActionIdWithInstance(actionId, actionManager.getWebAppInstanceName());
		if (actionRegistry.containsAction(instanceActionId)){
			return actionRegistry.getAction(instanceActionId);
		}else if(actionRegistry.containsAction(actionId)){
			return actionRegistry.getAction(actionId);
		}else{
			return null;
		}
	}

	/** 
	 * Use this implementation in call cases except prior to the time the ActionManager registry has been initialized.
	 */
	public Action resolveEffectiveAction(ActionManager actionManager,HttpServletRequest request, String actionId){
		return resolveEffectiveAction(actionManager, request, actionId, actionManager.getActionRegistry());
	}
	
	/**
	 * Determine whether to build a flow for the action.  The standard functionality is
	 * to not build flows for lava instance actions if there is a customized instance action
	 * defined for the current instance or if the flowtype = none. 
	 * 
	 */
	public boolean shouldBuildFlowsForAction(ActionManager actionManager, String actionId) {
		
		Action action = actionManager.getAction(actionId);
		if(action==null){  //shouldn't happen, but if it does we probably don't want to build a flow...
			return false; 
		}else if(action.getFlowType().equals(FlowTypeBuilder.FLOW_TYPE_NONE)){
			return false;
		}else if(action.getInstance().equals(ActionUtils.LAVA_INSTANCE_IDENTIFIER)){
			//this is a base "lava" action. 
			String instanceActionId = ActionUtils.getActionIdWithInstance(action.getId(), actionManager.getWebAppInstanceName());
			if(null!=actionManager.getAction(instanceActionId)){
				//there is an instance specific customization of this base "lava" action, so don't build the flow
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Get the flow id from the request.  Flow id's have the same format as action id's except
 	 * the flow id includes the additional "mode" part, while Action object stores 
	 * the mode as a param value under the key PROJECT_PARAMETER_KEY ("_do")
     * e.g. action id = lava.core.admin.auth.authUserRole with mode param/value "_do=edit"
     *        flow id = lava.core.admin.auth.authUserRole.edit
	 */
	public String extractFlowIdFromAction(ActionManager actionManager, HttpServletRequest request, Action action) {
		String actionMode = ActionUtils.getActionMode(request);
		if (actionMode.length() == 0) {
			actionMode = action.getDefaultFlowMode();
		}
		return new StringBuffer(action.getId()).append(ActionUtils.ACTION_ID_DELIMITER).append(actionMode).toString();
	}



	/** 
	 * Compares based on the "order" field.  Used by the collection of delegates to return a sorted list of delegates. 
	 */
	public int compareTo(ScopeActionDelegate scopeActionDelegate) throws ClassCastException {
	   
		return this.getOrder().compareTo(scopeActionDelegate.getOrder());    
	}
	
	
	public ScopeActionDelegates getScopeActionDelegates() {
		return scopeActionDelegates;
	}

	public void setScopeActionDelegates(ScopeActionDelegates scopeActionDelegates) {
		this.scopeActionDelegates = scopeActionDelegates;
		scopeActionDelegates.addDelegate(this);
	}

	public String getHandledScope() {
		return handledScope;
	}

	public void setHandledScope(String handledScope) {
		this.handledScope = handledScope;
	}



	public Long getOrder() {
		if(this.order==null){
			this.order = DEFAULT_ORDER;
		}
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	
}
