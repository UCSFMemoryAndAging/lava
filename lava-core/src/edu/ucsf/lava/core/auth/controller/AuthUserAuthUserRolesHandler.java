package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaIgnoreParamHandler;



public class AuthUserAuthUserRolesHandler extends BaseListComponentHandler {

	
	public AuthUserAuthUserRolesHandler(){
		this.setHandledList("authUserAuthUserRoles","authUserRole");
		this.setEntityForStandardSourceProvider(AuthUserRole.class);
		}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter daoFilter = AuthUserRole.newFilterInstance(getCurrentUser(request));
		AuthUser user = (AuthUser)components.get("authUser");
		daoFilter.setAlias("role", "role");
		daoFilter.setOuterAlias("user", "user");
		daoFilter.setOuterAlias("group", "group");
		daoFilter.setOuterAlias("group.users", "groupUsers");
		daoFilter.setOuterAlias("groupUsers.user", "groupUser");
		daoFilter.addDefaultSort("role.roleName",true);
		daoFilter.addParamHandler(new LavaIgnoreParamHandler("USER_ID"));
		daoFilter.setParam("USER_ID", user.getId());
		return daoFilter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
	
	}

	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter) {
		daoFilter.addDaoParam(
					daoFilter.daoOr(
						daoFilter.daoEqualityParam("groupUser.id", daoFilter.getParam("USER_ID")),
						daoFilter.daoEqualityParam("user.id", daoFilter.getParam("USER_ID"))));
		return daoFilter;
	}
}
