package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthPermission;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;

public class AuthPermissionsHandler extends BaseListComponentHandler { 

	public AuthPermissionsHandler() {
		super();
		this.setHandledList("authPermissions","authPermission");
		this.setEntityForStandardSourceProvider(AuthPermission.class);
		this.pageSize = 25;
		
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = AuthPermission.newFilterInstance(getCurrentUser(request));
		filter.setAlias("role", "role");
		filter.addDefaultSort("role.roleName",true);
		filter.addDefaultSort("permitDeny",true);
		filter.addDefaultSort("module",true);
		filter.addDefaultSort("section",true);
		filter.addDefaultSort("target",true);
		filter.addDefaultSort("mode",true);
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
	
	
}
