package edu.ucsf.lava.core.scope;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.LavaManager;
/**
 * A central manager implementation that acts as a facade to scope specific
 * functionality.   Primarily supports abstracting scope specific code from 
 * the core lava scope class implementations. 
 * 
 * @author jhesse
 *
 */
public class ScopeManager extends LavaManager {

	public static String SCOPE_MANAGER_NAME = "scopeManager";
	protected ScopeActionDelegates actionDelegates;
	protected ScopeAuthorizationDelegates authorizationDelegates;
	protected ScopeSessionAttributeHandlers sessionAttributeHandlers;
	
	public ScopeManager(){
		super(SCOPE_MANAGER_NAME);
	}
	
	//Action Delegates
	/**
	 * Returns the scopeActionDelegates collection.
	 * @return
	 */
	public ScopeActionDelegates getActionDelegates() {
		return actionDelegates;
	}

	/**
	 * Sets the scopeActionDelegates (used by spring to inject the collection
	 * into the Scope manager). 
	 * @param actionDelegates
	 */
	public void setActionDelegates(ScopeActionDelegates actionDelegates) {
		this.actionDelegates = actionDelegates;
	}
	
	
	/**
	 * Returns the scope action delegate for the given scope. 
	 * @param scope
	 * @return
	 */
	public ScopeActionDelegate getActionDelegate(String scope){
		return this.actionDelegates.get(scope);
	}
	
	/**
	 * Returns the scope action delegate for the scope of the given action. 
	 * @param action
	 * @return
	 */
	public ScopeActionDelegate getActionDelegate(Action action){
		return this.getActionDelegate(action.getScope());
	}
	
	
	//Authorization delegates
	/**
	 * Returns the scopeAuthorizationDelegates collection.
	 * @return
	 */
	public ScopeAuthorizationDelegates getAuthorizationDelegates() {
		return authorizationDelegates;
	}

	/**
	 * Sets the scopeAuthorizationDelegates (used by spring to inject the collection
	 * into the Scope manager). 
	 * @param actionDelegates
	 */
	public void setAuthorizationDelegates(ScopeAuthorizationDelegates authorizationDelegates) {
		this.authorizationDelegates = authorizationDelegates;
	}
	
	
	/**
	 * Returns the scope authorization delegate for the given scope. 
	 * @param scope
	 * @return
	 */
	public ScopeAuthorizationDelegate getAuthorizationDelegate(String scope){
		return this.authorizationDelegates.get(scope);
	}
	
	/**
	 * Returns the scope authorization delegate for the scope of the given action. 
	 * @param action
	 * @return
	 */
	public ScopeAuthorizationDelegate getAuthorizationDelegate(Action action){
		return this.getAuthorizationDelegate(action.getScope());
	}

	/**
	 * Returns the scope session attributes handlers collection.
	 * @return
	 */
	public ScopeSessionAttributeHandlers getSessionAttributeHandlers() {
		return sessionAttributeHandlers;
	}

	/**
	 * Sets the scope session attributes handlers collection.
	 * @param sessionAttributeHandlers
	 */
	public void setSessionAttributeHandlers(
			ScopeSessionAttributeHandlers sessionAttributeHandlers) {
		this.sessionAttributeHandlers = sessionAttributeHandlers;
	}


	
	
	
}
