package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthPermission;
import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;



public class AuthRoleAuthPermissionsHandler extends BaseListComponentHandler {

	
	public AuthRoleAuthPermissionsHandler(){
		this.setHandledList("authRoleAuthPermissions","authPermission");
		this.setEntityForStandardSourceProvider(AuthPermission.class);
		
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter daoFilter = AuthPermission.newFilterInstance(getCurrentUser(request));
		AuthRole role = (AuthRole)components.get("authRole");
		daoFilter.setAlias("role", "role");
		daoFilter.addDefaultSort("permitDeny",true);
		daoFilter.addDefaultSort("module",true);
		daoFilter.addDefaultSort("section",true);
		daoFilter.addDefaultSort("target",true);
		daoFilter.addDefaultSort("mode",true);
		daoFilter.addParamHandler(new LavaEqualityParamHandler("role.id"));
		daoFilter.setParam("role.id", role.getId());
		return daoFilter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
		}
	
	
	
}
