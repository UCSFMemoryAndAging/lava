package edu.ucsf.lava.core.manager;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.audit.AuditManager;
import edu.ucsf.lava.core.auth.AuthManager;
import edu.ucsf.lava.core.environment.EnvironmentManager;
import edu.ucsf.lava.core.list.ListManager;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.scope.ScopeManager;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.spring.LavaBeanUtils;
import edu.ucsf.lava.core.view.ViewManager;
import edu.ucsf.lava.core.webflow.WebflowManager;



public class CoreManagerUtils extends LavaBeanUtils {

	
	public static Managers getManagers(){
		return (Managers)get("managers");
	}
	
	
	public static ActionManager getActionManager(Managers managers){
		return (ActionManager) managers.get(ActionManager.ACTION_MANAGER_NAME);
	}

	public static ActionManager getActionManager(){
		return getActionManager(getManagers());
	}
	
	public static AuthManager getAuthManager(Managers managers){
		return (AuthManager) managers.get(AuthManager.AUTH_MANAGER_NAME);
	}

	public static AuthManager getAuthManager(){
		return getAuthManager(getManagers());
	}	
	
	
	public static AuditManager getAuditManager(Managers managers){
		return (AuditManager) managers.get(AuditManager.AUDIT_MANAGER_NAME);
	}
	
	public static AuditManager getAuditManager(){
		return getAuditManager(getManagers());
	}

	public static EnvironmentManager getEnvironmentManager(Managers managers){
		return (EnvironmentManager) managers.get(EnvironmentManager.ENVIRONMENT_MANAGER_NAME);
	}
	
	public static EnvironmentManager getEnvironmentManager(){
		return getEnvironmentManager(getManagers());
	}

		
	
	public static ScopeManager getScopeManager(Managers managers){
		return (ScopeManager) managers.get(ScopeManager.SCOPE_MANAGER_NAME);
	}

	public static ScopeManager getScopeManager(){
		return getScopeManager(getManagers());
		
	}
	
	public static SessionManager getSessionManager(Managers managers){
		return (SessionManager) managers.get(SessionManager.SESSION_MANAGER_NAME);
	}

	public static SessionManager getSessionManager(){
		return getSessionManager(getManagers());
	}

	public static ListManager getListManager(Managers managers){
		return (ListManager) managers.get(ListManager.LIST_MANAGER_NAME);
	}

	public static ListManager getListManager(){
		return getListManager(getManagers());
	}

	public static MetadataManager getMetadataManager(Managers managers){
		return (MetadataManager) managers.get(MetadataManager.METADATA_MANAGER_NAME);
	}

	public static MetadataManager getMetadataManager(){
		return getMetadataManager(getManagers());
	}

	public static WebflowManager getWebflowManager(Managers managers){
		return (WebflowManager) managers.get(WebflowManager.WEBFLOW_MANAGER_NAME);
	}

	public static WebflowManager getWebflowManager(){
		return getWebflowManager(getManagers());
	}
	
	public static ViewManager getViewManager(Managers managers){
		return (ViewManager) managers.get(ViewManager.VIEW_MANAGER_NAME);
	}

	public static ViewManager getViewManager(){
		return getViewManager(getManagers());
	}
}
