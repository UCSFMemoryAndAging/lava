package edu.ucsf.lava.core.action;

import static edu.ucsf.lava.core.session.CoreSessionUtils.CURRENT_ACTION;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.environment.EnvironmentManager;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.scope.ScopeActionDelegate;
import edu.ucsf.lava.core.scope.ScopeManager;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;


public class ActionManager extends LavaManager {
	
	public static String ACTION_MANAGER_NAME="actionManager";
	
	
	protected ActionDefinitions actionDefinitions;
	protected ActionRegistry actionRegistry = new ActionRegistry();
	protected ScopeManager scopeManager;
	protected SessionManager sessionManager;
	protected EnvironmentManager environmentManager;
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

	
	public ActionManager(){
		super(ACTION_MANAGER_NAME);
	}
	
	public void updateManagers(Managers managers) {
		super.updateManagers(managers);
		scopeManager = CoreManagerUtils.getScopeManager(managers);
		sessionManager = CoreManagerUtils.getSessionManager(managers);
		environmentManager = CoreManagerUtils.getEnvironmentManager(managers);
	}


	/**
	 *  create a registry (map) of action lookup keys to actions from the 
	 *  actionDefinitions.  Encapsulates the logic for generating the registry
	 *  to the action scope delegates.  This facilitates abstracting knowledge of 
	 *  scope specific default action rules out of the core. 
	 */
	public void reloadActionDefinitions(){
		//Copy all action definitions into temp action registry
		ActionRegistry registry = new ActionRegistry();
		registry.setActions(actionDefinitions.getDefinitionsCopy());
		
		//pass the registry to each scope delegate (in order)
		for(ScopeActionDelegate delegate : scopeManager.getActionDelegates().getOrderedDelegates()){
			registry = delegate.onReloadActionDefinitions(this,registry);
		}
		//set the generated registry as the current actionRegistry
		this.actionRegistry = registry;
		
	}
	
	
	public Action getCurrentAction(HttpServletRequest request){
		return (Action)request.getSession().getAttribute(CURRENT_ACTION);
	}
	
	public void setCurrentAction(HttpServletRequest request, Action action){
		request.getSession().setAttribute(CURRENT_ACTION,action);
	}
	
	
	
	
		
	
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}


	/**
	 * Get the action using the id
	 * would be loaded by getEffectiveRuntimeAction()
	 * @param actionId
	 * @return
	 */
	public Action getAction(String actionId){
		if(actionRegistry.containsAction(actionId)){
			return (Action) actionRegistry.getAction(actionId);
		}
		return null;
	}
	

	/**
	 * Determine the effective "runtime" action based on the actionId passed in and the
	 * current runtime conditions (e.g. what instance is running). 
	 * 
	 * @param request
	 * @param actionId
	 * @return
	 */
	public Action getEffectiveAction(HttpServletRequest request, String actionId)
	{
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(ActionUtils.getScope(actionId));
		if(delegate==null){return null;}
		return delegate.resolveEffectiveAction(this,request,actionId);
		
		

	}
	
	/**
	 * Convenience method that determines the effective action (in the absense
	 * of an active request)
	 * 
	 * @param actionId
	 * @return
	 */
	public Action getEffectiveAction(String actionId)
	{
		return getEffectiveAction(null,actionId);
	}
	
	
   /**
    * Determines the flow id based on the request.  
    * @return
    */
	public String extractFlowIdFromRequest(HttpServletRequest request){
		Action action = this.getEffectiveAction(request, ActionUtils.getActionId(request));
		if(action==null){return null;}
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(action);
			if(delegate==null){return null;}
		return delegate.extractFlowIdFromAction(this, request, action);
	}
	
	
	
	
	
	/*
	 * Given an actionId, determine if there is a corresponding instance specific actionId
	 * for which flows will be built (in which case these instance specific flows will always
	 * be used instead of the corresponding core flows) or whether the core actionId should
	 * be used to build the flows.
	 * 
	 * @return effective actionId used to construct the flow id 
	 */
	public boolean shouldBuildFlowsForAction(String actionId) {
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(ActionUtils.getScope(actionId));
		if(delegate==null){return false;}
		return delegate.shouldBuildFlowsForAction(this,actionId);
	}
		

	public String getWebAppInstanceName(){
		if(environmentManager==null){ return "";}
		return environmentManager.getInstanceName();
		
	}
	
	
	public Action getDefaultAction(HttpServletRequest request,Action actionIn){
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(actionIn);
		if(delegate==null){return null;}
		return delegate.getDefaultAction(this,request, actionIn);
	}
	
	
	public Action getDefaultAction(HttpServletRequest request)
	{
		return getDefaultAction(request,CoreSessionUtils.getCurrentAction(sessionManager, request));
		
	}

	

	public ActionDefinitions getActionDefinitions() {
		return actionDefinitions;
	}

	public void setActionDefinitions(ActionDefinitions actionDefinitions) {
		this.actionDefinitions = actionDefinitions;
	}

	
	
	
	
}
