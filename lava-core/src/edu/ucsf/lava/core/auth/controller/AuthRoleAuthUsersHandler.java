package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;



public class AuthRoleAuthUsersHandler extends BaseListComponentHandler {

	
	public AuthRoleAuthUsersHandler(){
		this.setHandledList("authRoleAuthUsers","authUserRole");
		this.setEntityForStandardSourceProvider(AuthUserRole.class);
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter daoFilter = AuthUserRole.newFilterInstance(getCurrentUser(request));
		AuthRole role = (AuthRole)components.get("authRole");
		daoFilter.setAlias("role", "role");
		daoFilter.setAlias("user", "user");
		daoFilter.addDefaultSort("user.userName",true);
		daoFilter.addParamHandler(new LavaEqualityParamHandler("role.id"));
		daoFilter.setParam("role.id", role.getId());
		return daoFilter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
	}

	@Override
	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter) {
		daoFilter.addDaoParam(daoFilter.daoNotNull("user.id"));
		return super.onPostFilterParamConversion(daoFilter);
	}
	
	

}
