package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;



public class AuthGroupAuthUserRolesHandler extends BaseListComponentHandler {

	
	public AuthGroupAuthUserRolesHandler(){
		this.setHandledList("authGroupAuthUserRoles","authUserRole");
		this.setEntityForStandardSourceProvider(AuthUserRole.class);
	}
	
	@Override
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter daoFilter = AuthUserRole.newFilterInstance(getCurrentUser(request));
		AuthGroup group = (AuthGroup)components.get("authGroup");
		daoFilter.setAlias("role", "role");
		daoFilter.setAlias("group", "group");
		daoFilter.addDefaultSort("role.roleName",true);
		daoFilter.addParamHandler(new LavaEqualityParamHandler("group.id"));
		daoFilter.setParam("group.id", group.getId());
		return daoFilter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {

	}
	
}
