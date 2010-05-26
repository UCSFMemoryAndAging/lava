package edu.ucsf.lava.crms.auth;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.AuthManager;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.manager.CoreManagerUtils;

public class CrmsAuthUtils {
	protected static AuthManager authManager;
	
	

	/**
	 * filterProjectListByPermission
	 * 
	 * Views which contain lists of projects need to make sure that the list does not
	 * contain a project for which the user does not have permission. The lists are filtered
	 * for project access authorization in the dynamic list creation process, but have not 
	 * been filtered for permission authorization. 
	 * 
	 * e.g. add consent view should only list those projects in which the user has
	 *      permission to add consents
	 * 
	 * @return
	 */
	public static Map<String,String> filterProjectListByPermission(AuthUser user, Action action,Map<String,String> projectList) {
	
		AuthManager authManager = getAuthManager();
		Map<String,String> filteredList = new LinkedHashMap<String,String>();
		for (Entry<String,String> entry : projectList.entrySet()) {
			if (entry.getKey().length() == 0 || authManager.isAuthorized(user, action, new CrmsAuthorizationContext(entry.getKey()))) {
				filteredList.put(entry.getKey(), entry.getValue());
			}
		}
		return filteredList;
	}
	
	public static AuthManager getAuthManager(){
		if(authManager==null){
			authManager = CoreManagerUtils.getAuthManager();
		}
		return authManager;
	}
	
}
