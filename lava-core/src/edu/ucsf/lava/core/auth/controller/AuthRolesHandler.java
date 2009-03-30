package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;

public class AuthRolesHandler extends BaseListComponentHandler { 

	public AuthRolesHandler() {
		super();
		this.setHandledList("authRoles","authRole");
		this.setEntityForStandardSourceProvider(AuthRole.class);
		this.pageSize = 25;
		
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  AuthRole.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("roleName",true);
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
	
	


}
