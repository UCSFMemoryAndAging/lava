package edu.ucsf.lava.core.scope;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.AuthRolePermissionCache;
import edu.ucsf.lava.core.auth.AuthUserPermissionCache;
import edu.ucsf.lava.core.auth.AuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.model.LavaEntity;

/**
 *  * A base class that implements the core functionality and interface of a ScopeAuthorizationDelegate.
 * 
 * @author jhesse
 *
 */
public abstract class AbstractScopeAuthorizationDelegate implements ScopeAuthorizationDelegate {
	protected static Long DEFAULT_ORDER=new Long(1000);
	protected ScopeAuthorizationDelegates scopeAuthorizationDelegates;
	protected String handledScope;
	protected Long order;
	

	/**
	 * Default implementation of the authorization check to determine if a user is permitted to execute an action
	 * given an authorizationContext.  The action is checked against the role permissions for all the user roles 
	 * where the user role assignment authorization context is a match for the given authorization context.
	 * 
	 *  If the action isAuthorized for any of the roles then return true.  Regardless, cache the result in the userCache,
	 *  so that the result can be quickly accessed next time the user attempts to execute the action in the same 
	 *  authorization context. 
	 */
	public boolean isAuthorized(AuthRolePermissionCache roleCache, AuthUser user, Action action, AuthorizationContext authorizationContext) {
		
		AuthUserPermissionCache userCache = user.getPermissionCache();
		//make sure cache is still valid relative to the modfication time of the role cache
		//...clear user cache if the role cache has been updated.
		if(userCache.getInitializedTime().before(roleCache.getCacheModified())){
			userCache.clearCache();
		}
		
		//first check user cache
		if(userCache.isAuthCheckInCache(action, authorizationContext)){
			return userCache.isAuthorized(action, authorizationContext);
		}
		//next check in the roleCache for each role
		for(AuthUserRole r : user.getEffectiveRoles()){
			if(roleCache.isAuthorized(r.getRole().getRoleName(),action) && r.isContextAuthorized(authorizationContext)){
				//add to the user cache 
				userCache.cacheAuthCheck(action,authorizationContext,true);
				return true;
			}
		}
		//if we get here then the authCheck failed...cache the result in the user cache.
		userCache.cacheAuthCheck(action,authorizationContext,false);
		return false;
	}

	
	/**
	 * Authorization check for a particular user, action, and entity.  The default implementation simply retrieves the
	 * authorization context from the entity.  Subclasses will need to implement more complex logic if the authroization
	 * context for the entity needs pre-processing or iteration for the standard isAuthorized check.
	 */
	public boolean isAuthorized(AuthRolePermissionCache roleCache, AuthUser user, Action action, LavaEntity entity) {
		return this.isAuthorized(roleCache, user, action, newAuthorizationContext(entity));
	}


	/**
	 * Authorization check for a particular user, action, and list.  The default implemention simple checks that the user has
	 * permission for the action in any authorization context.  The contents of the list should be filtered by the dao layer
	 * authorization filtering, so there is no need to ensure that the user has permission to execute the action in the 
	 * context of each and every entity in the list.  As long as the user has permission on one of their roles, the action
	 * is authorized.  
	 */
	public boolean isAuthorized(AuthRolePermissionCache roleCache, AuthUser user, Action action, ScrollablePagedListHolder list) {
		return this.isAuthorized(roleCache, user, action, newMatchesAllAuthorizationContext());
		
	}

	
	


	public String getHandledScope() {
		return handledScope;
	}

	public void setHandledScope(String handledScope) {
		this.handledScope = handledScope;
	}



	public void setOrder(Long order) {
		this.order = order;
	}

	public Long getOrder() {
		if(this.order==null){
			this.order = DEFAULT_ORDER;
		}
		return order;
	}

	public ScopeAuthorizationDelegates getScopeAuthorizationDelegates() {
		return scopeAuthorizationDelegates;
	}

	public void setScopeAuthorizationDelegates(
			ScopeAuthorizationDelegates scopeAuthorizationDelegates) {
		this.scopeAuthorizationDelegates = scopeAuthorizationDelegates;
		scopeAuthorizationDelegates.addDelegate(this);
	}

	

}
