package edu.ucsf.lava.core.auth;

import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.model.AuthPermission;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.scope.ScopeAuthorizationDelegate;
import edu.ucsf.lava.core.scope.ScopeManager;

public class AuthManager extends LavaManager implements UserDetailsService {

	public static String AUTH_MANAGER_NAME="authManager";
	
	public final static String ACTION_MODE_ADD = "add";
	protected ScopeManager scopeManager;
	protected ActionManager actionManager;
	protected AuthRolePermissionCache rolePermissionCache;
	
	public AuthManager(){
		super(AUTH_MANAGER_NAME);
	}
	
	
	public void updateManagers(Managers managers) {
		super.updateManagers(managers);
		scopeManager = CoreManagerUtils.getScopeManager(managers);
		actionManager = CoreManagerUtils.getActionManager(managers); 
	}
	
	public void initialize(){
		reloadRoleCache();
	}
	
	public void reloadRoleCache(){
		AuthRolePermissionCache roleCache = new AuthRolePermissionCache();
		roleCache.initialize(actionManager.getActionRegistry().getActions(), AuthPermission.MANAGER.get());
		this.setRolePermissionCache(roleCache);
	}
	
	/**
	 * isAuthorized
	 * 
	 * Determines whether permission is granted for the current user to perform the specified action
	 * within the specified authorization context.
	 * 	 
	 * @param user
	 * @param module
	 * @param section
	 * @param target
	 * @param mode
	 * @param projName
	 * @return
	 */
	public boolean isAuthorized(AuthUser user, Action action, AuthorizationContext authorizationContext) {
		
		//get the authorization delegate that handles the action's scope.
		ScopeAuthorizationDelegate delegate = scopeManager.getAuthorizationDelegate(action);
		if(delegate==null){return false;}
		
		return delegate.isAuthorized(rolePermissionCache, user, action, authorizationContext);
	}
		
	public boolean isAuthorized(AuthUser user, Action action, LavaEntity entity) {
		//get the authorization delegate that handles the action's scope.
		ScopeAuthorizationDelegate delegate = scopeManager.getAuthorizationDelegate(action);
		if(delegate==null){return false;}
		
		return delegate.isAuthorized(rolePermissionCache, user, action, entity);
	}

	public boolean isAuthorized(AuthUser user, Action action, ScrollablePagedListHolder list) {
		//get the authorization delegate that handles the action's scope.
		ScopeAuthorizationDelegate delegate = scopeManager.getAuthorizationDelegate(action);
		if(delegate==null){return false;}
		
		return delegate.isAuthorized(rolePermissionCache, user, action, list);
	}

	
	
	public synchronized AuthRolePermissionCache  getRolePermissionCache() {
		return rolePermissionCache;
	}



	public synchronized void setRolePermissionCache(AuthRolePermissionCache rolePermissionCache) {
		this.rolePermissionCache = rolePermissionCache;
	}


	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException, DataAccessException {
		
		AuthUser authUser = AuthUser.MANAGER.getByLogin(userName);
		if(authUser==null){
			throw new UsernameNotFoundException("");
		}
		
		return authUser;
	}






	
	
}
