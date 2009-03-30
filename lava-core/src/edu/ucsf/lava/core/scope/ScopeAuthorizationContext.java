package edu.ucsf.lava.core.scope;

import edu.ucsf.lava.core.auth.AuthorizationContext;

/**
 * Extends the Authorization Context interface to include methods supporting 
 * scope (e.g. core, crms) based Authorization Contexts.  
 * 
 * @author jhesse
 *
 */
public interface ScopeAuthorizationContext extends AuthorizationContext {

	
	
	/**
	 * Return the identifier of the scope for the authorization context.  This will always 
	 * be the same for every instance of an scope authorization context subclass (e.g. 'core' for 
	 * any CoreAuthScopeContext)  
	 * @return
	 */
	public String getScope();
	
	
	
}
