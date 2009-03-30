package edu.ucsf.lava.core.auth;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.ucsf.lava.core.action.model.Action;

public class AuthUserPermissionCache implements Serializable{

	protected Date cacheInitialized = new Date();
	protected Map <String,Map<String,Boolean>> cache = new HashMap<String,Map<String,Boolean>>();
	public Date getInitializedTime() {
		return cacheInitialized;
	}
	
	
	
	
	public void clearCache(){
		this.cache.clear();
		cacheInitialized = new Date();
		
	}
	
	
	
	public Boolean  isAuthCheckInCache(Action action, AuthorizationContext authContext){
		String key = action.makeCacheKey();
		if(!cache.containsKey(key) || !cache.get(key).containsKey(authContext.getCacheKey())) {return false;} 
		return true;
		}
	
	public Boolean isAuthorized(Action action, AuthorizationContext authContext) {
		String key = action.makeCacheKey();
		if(!cache.containsKey(key) || authContext == null){
			return null; //should never be called if key is not in cache...or if authContext not supplied...
		}
		if(cache.get(key).containsKey(authContext.getCacheKey())){
			return cache.get(key).get(authContext.getCacheKey());
		}
		return false;
	}

	public void cacheAuthCheck(Action action,AuthorizationContext authContext,Boolean authorized){
		String key = action.makeCacheKey();
		if(!cache.containsKey(key)){
			cache.put(key,new HashMap<String,Boolean>());
		}else{
			cache.get(key).put(authContext.getCacheKey(), authorized);
		}
	}
	
	

}

