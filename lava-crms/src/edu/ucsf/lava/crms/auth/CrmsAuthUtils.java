package edu.ucsf.lava.crms.auth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.AuthManager;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.crms.scheduling.model.Visit;

public class CrmsAuthUtils {
	protected static AuthManager authManager;
	
	public static AuthManager getAuthManager(){
		if(authManager==null){
			authManager = CoreManagerUtils.getAuthManager();
		}
		return authManager;
	}

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

	/**
	 * filterVisitListByPermission
	 * 
	 * Views which contain lists of visits need to make sure that the list does not
	 * contain a visit for a project for which the user does not have permission. The
	 * lists are filtered for project access authorization in the dynamic list creation
	 * process, but have not been filtered for permission authorization.
	 * 
	 * Also optionally filter based on locked status.
	 * 
	 * e.g. the add instrument view should not list visits for projects for which the user
	 *      does not have permission to add instruments, nor locked
	 *      
	 */
	public static Map<String,String> filterVisitListByPermission(AuthUser user, Action action, 
			Map<String,String> visitList, boolean removeLockedVisits) {
		
		AuthManager authManager = getAuthManager();
		Map<String,String> filteredList = new LinkedHashMap<String,String>();
		
		for (Entry<String,String> entry : visitList.entrySet()) {
			// include the blank entry in the new list
			if (entry.getKey().length() == 0) {
				filteredList.put(entry.getKey(), entry.getValue());
			}
			else {
				// visit list has already been filtered for auth access, so no need to pass user when creating new filter
				Visit v = (Visit)Visit.MANAGER.getById(Long.valueOf(entry.getKey()),Visit.newFilterInstance(user));
				
				if (entry.getKey().length() == 0 || 
						(authManager.isAuthorized(user, action, new CrmsAuthorizationContext(v.getProjName())) &&
						 (!removeLockedVisits ||!v.getLocked()))) {
					filteredList.put(entry.getKey(), entry.getValue());
				}
			}
		}
		
		return filteredList;
	}
	
	public static Map<String,String> filterVisitListByPermission(AuthUser user, Action action, Map<String,String> visitList) {
		return filterVisitListByPermission(user, action, visitList, false);
	}
	
	public static List<Visit> filterVisitListByPermission(AuthUser user, Action action, 
			List<Visit> visitList, boolean removeLockedVisits) {
		
		AuthManager authManager = getAuthManager();
		List<Visit> filteredList = new ArrayList<Visit>();

		if (visitList == null) return null;
		for (Visit visit : visitList) {
			Visit v = (Visit) Visit.MANAGER.getById(Long.valueOf(visit.getId()), Visit.newFilterInstance(user));

			if (v != null
				&& authManager.isAuthorized(user, action, new CrmsAuthorizationContext(v.getProjName()))
				&& !(removeLockedVisits && v.getLocked())) {
				filteredList.add(v);
			}
		}

		return filteredList;
	}
	
	public static List<Visit> filterVisitListByPermission(AuthUser user, Action action, List<Visit> visitList) {
		return filterVisitListByPermission(user, action, visitList, false);
	}
	
}
