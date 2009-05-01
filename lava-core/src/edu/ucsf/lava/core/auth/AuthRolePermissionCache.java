package edu.ucsf.lava.core.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.model.AuthPermission;
import edu.ucsf.lava.core.webflow.builder.FlowTypeBuilder;


public class AuthRolePermissionCache implements Serializable{
	protected final Log logger = LogFactory.getLog(getClass());
	protected Date cacheModified;
	protected Map <String,Map<String,Boolean>> cache;
	public Date getCacheModified() {
		return cacheModified;
	}


	public void initialize(Map<String,Action> actionsIn, List<AuthPermission> permissionsIn){
		
		List<Action> actions = new ArrayList<Action>(actionsIn.size());
		Map<String,List<AuthPermission>> permissions = new HashMap();
		List<AuthPermission> defaultPermissions = new ArrayList();
		Map <String,Map<String,Boolean>> workingCache;
		//get unique list of all actions
		for(Action a : actionsIn.values()){
			if(!actions.contains(a)){
				actions.add(a);
			}
		}
		
		//organize permissions by role and extract the default permissions into a separate list
		for(AuthPermission p : permissionsIn){
			String roleName = p.getRole().getRoleName();
			if(roleName.equals(AuthPermission.DEFAULT_PERMISSION_ROLE)){
				defaultPermissions.add(p);
			}else{
				if(!permissions.containsKey(roleName)){
					permissions.put(roleName, new ArrayList());
				}
				permissions.get(roleName).add(p);
			}
		}
		workingCache = new HashMap<String,Map<String,Boolean>>(actions.size());

		
		//add each action / event to the cache and update the permit/deny flag for each role
		for (Action a : actions){
			FlowTypeBuilder builder = a.getFlowTypeBuilder();
			if(builder!=null){
				String[] events = builder.getEvents();
				
				for(int i=0;i<events.length;i++){
					String key = a.makeCacheKey(events[i]);
					if(!workingCache.containsKey(key)){
						workingCache.put(key,new HashMap<String,Boolean>());
					}
					for(String role: permissions.keySet()){
						workingCache.get(key)
							.put(role,new Boolean(isPermitted(a,events[i],defaultPermissions,permissions.get(role))));
						logger.debug("roleAuthPermissionCache Initialization: role="+role + "; key="+key+" permitted="+workingCache.get(key).get(role).toString());
					}
				}
			}
		}
		
		this.cache = workingCache;
		this.cacheModified = new Date();
	}
	
	
	/**
	 * Determines whether the action/event is permitted based on the set of default permissions and "role" permissions passed in
	 * 
	 * Permissions are evaluated in the following precidence
	 * 		1) a "deny"  role permission = deny
	 * 		2) a "deny" default permission of higher precidence than all permit "role permissions" = deny  
	 * 		3) a "permit" role permission = permit
	 * 		4) a "permit" default permission = permit
	 * 
	 * @param action
	 * @param event
	 * @param defaultPermissions
	 * @param rolePermissions
	 * @return
	 */
	protected boolean isPermitted(Action action, String event,List<AuthPermission> defaultPermissions,List<AuthPermission> rolePermissions){
		
		//look for a matching deny in the rolePermissions
		for (AuthPermission p : rolePermissions){
			if(p.matches(action,event) && p.denies()){
				return false;
			}
		}
		//look for matching denies in the defaultPermissions
		List<AuthPermission> defaultDenyMatches = new ArrayList<AuthPermission>();
		for(AuthPermission p : defaultPermissions){
			if(p.denies() && p.matches(action,event)){
				defaultDenyMatches.add(p);
			}
		}
		
		//look for a permit in the rolePermissions that is not "overridden" by a default deny
		for(AuthPermission p : rolePermissions){
			if(p.permits() && p.matches(action,event)){
				boolean overrode = false;
				for(AuthPermission dd : defaultDenyMatches){
					if(p.isOverriddenBy(dd)){
						overrode = true;
					}
				}
				if(!overrode){
					return true;
				}
			}
		}
		
		//if there are any matching denies and we get to this point then not permitted
		if(defaultDenyMatches.size()>0){
			return false;
		}
		//now look for a matching permitting default permission
		for(AuthPermission p : defaultPermissions){
			if(p.permits() && p.matches(action,event)){
				return true;
			}
		}
		//action/event not matched by any permit or deny permission so denied 	
		return false;
			
	}
	//return the pre-calculated permission for the action/mode based on the role.  If not found in the cache
	//deny access by default. 
	public Boolean isAuthorized(String role,Action a){
		String key = a.makeCacheKey();
		if(cache.containsKey(key) && cache.get(key).containsKey(role)){
			return cache.get(key).get(role);
		}
		return false;
	}
	
	
	
}
