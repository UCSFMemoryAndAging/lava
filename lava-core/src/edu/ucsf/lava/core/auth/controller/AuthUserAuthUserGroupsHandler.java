package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserGroup;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;



public class AuthUserAuthUserGroupsHandler extends BaseListComponentHandler {

	
	public AuthUserAuthUserGroupsHandler(){
		this.setHandledList("authUserAuthUserGroups","authUserGroup");
		this.setEntityForStandardSourceProvider(AuthUserGroup.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter daoFilter = AuthUserGroup.newFilterInstance(getCurrentUser(request));
		AuthUser user = (AuthUser)components.get("authUser");
		daoFilter.setAlias("group", "group");
		daoFilter.setAlias("user", "user");
		daoFilter.addDefaultSort("group.groupName",true);
		daoFilter.addParamHandler(new LavaEqualityParamHandler("user.id"));
		daoFilter.setParam("user.id", user.getId());
		return daoFilter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {

	}
	

}
