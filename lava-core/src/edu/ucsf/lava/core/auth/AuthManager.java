package edu.ucsf.lava.core.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
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
	protected PasswordDelegates passwordDelegates;
	
	
	
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

	/**
	 * Method to authorize on action and entity, but to allow a different mode
	 * to be substituted for the action mode. This facilitates finer granularity  of 
	 * permissions than flow granularity, where action and mode are constant for the lifetime
	 * of the flow --- so events within the flow can be substituted for the mode and
	 * permissions can then be configured at the event level.
	 * See BaseEntityComponentHandler authCustomEvent.
	 * 
	 * @param user
	 * @param action
	 * @param mode
	 * @param entity
	 * @return
	 */
	public boolean isAuthorized(AuthUser user, Action action, String mode, LavaEntity entity) {
		// modify the action's mode, replacing it with the mode supplied by the caller
		action.setParam(ActionUtils.MODE_PARAMETER_NAME, mode);
		return this.isAuthorized(user, action, entity);
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

	/**
	 * Change the users password.  
	 * @param user
	 * @param oldPassword
	 * @param newPassword
	 * @param newPasswordConfirm
	 * @param errors
	 * @return
	 */
	public boolean changePassword(AuthUser user, String oldPassword, String newPassword, String newPasswordConfirm, Map<String,ArrayList>errors){
		if(user==null){
			errors.put("passwordDelegate.changePassword.unexpectedError", null);
			return false;
		}
		PasswordDelegate delegate = getPasswordDelegates().get(user.getAuthenticationType());
		if(delegate==null){
			errors.put("passwordDelegate.changePassword.unexpectedError", null);
			return false;
		}
		return delegate.changePassword(user, oldPassword, newPassword, newPasswordConfirm, errors);
		
	}
	
	/**
	 * Reset the users password.  
	 * @param user
	 * @param oldPassword
	 * @param newPassword
	 * @param newPasswordConfirm
	 * @param errors
	 * @return
	 */
	public boolean resetPassword(AuthUser user,  String newPassword, String newPasswordConfirm, Map<String,ArrayList>errors){
		
		PasswordDelegate delegate = getPasswordDelegate(user);
		if(delegate==null){
			errors.put("passwordDelegate.resetPassword.unexpectedError", null);
			return false;
		}
		return delegate.resetPassword(user, newPassword, newPasswordConfirm, errors);
	}
	
	
	public Map<String,String> getAuthenticationTypesList(){
		ArrayList<String> types = new ArrayList();
		types.addAll(getPasswordDelegates().getDelegates().keySet());
		Collections.sort(types);
		Map<String,String> list = new LinkedHashMap<String,String>();
		for(String type : types){
			list.put(type, type);
		}
		return list;
	}
	
	public PasswordDelegates getPasswordDelegates() {
		return passwordDelegates;
	}

	public PasswordDelegate getPasswordDelegate(AuthUser user){
		if(user==null || passwordDelegates == null){return null;}
		return passwordDelegates.get(user.getAuthenticationType());
		}
	public void setPasswordDelegates(PasswordDelegates passwordDelegates) {
		this.passwordDelegates = passwordDelegates;
	}


	



	
	
}
