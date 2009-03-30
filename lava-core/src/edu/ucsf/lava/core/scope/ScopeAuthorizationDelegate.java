package edu.ucsf.lava.core.scope;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.AuthRolePermissionCache;
import edu.ucsf.lava.core.auth.AuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.model.LavaEntity;

/**
 * This interface uses a delegate pattern to enable extension of the AuthManager functionality.  
 * Each lava scope needs to have a authorization delegate implementation.   
 * 
 * @author jhesse
 *
 */
public interface ScopeAuthorizationDelegate {

	public String getHandledScope();
	public Long getOrder();
	
	/**
	 * 
	 * Determines whether permission is granted for the user to perform the specified action
	 * given the authorization context.  The abstractScopeAuthorizationDelegate is coded with the
	 * default implementation of the authorization check given a known authorization context. 
	 * 	 
	 * @param user
	 * @param action
	 * @param authorizationContext
	 * @return
	 */
	public boolean isAuthorized(AuthRolePermissionCache roleCache, AuthUser user, 
				Action action, AuthorizationContext authorizationContext);

	/**
	 * 
	 * Determines whether permission is granted for the user to perform the specified action
	 * given the entity.  The delegate will need to determine what the specific Authorization 
	 * Context(s) should be checked.  Generally these contexts will be checked using the 
	 * default implentation of isAuthorized(...AuthorizationContext authorizationContext). 
	 * 
	 * @param user
	 * @param action
	 * @param entity
	 * @return
	 */
	public boolean isAuthorized(AuthRolePermissionCache roleCache, AuthUser user,
			Action action, LavaEntity entity);

	/**
	 * 
	 * Determines whether permission is granted for the user to perform the specified action
	 * given the list.  The delegate will need to determine what the specific Authorization 
	 * Context(s) should be checked.  Generally these contexts will be checked using the 
	 * default implentation of isAuthorized(...AuthorizationContext authorizationContext). 
	 * 
	 * 
	 * @param user
	 * @param action
	 * @param list
	 * @return
	 */
	public boolean isAuthorized(AuthRolePermissionCache roleCache, AuthUser user, 
			Action action, ScrollablePagedListHolder list);
	
	/**
	 * Get a new instance of the scope specific AuthorizationContext
	 * 
	 */
	public AuthorizationContext newAuthorizationContext();
	
	/** 
	 * get a new instance of the scope context configured with the entity context
	 */
	public AuthorizationContext newAuthorizationContext(LavaEntity entity);
	
	/** 
	 * get a new instance of the scope context configured with matches all wildcards
	 */
	public AuthorizationContext newMatchesAllAuthorizationContext();
	
}
