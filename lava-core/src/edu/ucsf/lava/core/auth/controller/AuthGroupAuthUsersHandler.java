package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.auth.model.AuthUserGroup;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;



public class AuthGroupAuthUsersHandler extends BaseListComponentHandler {

	
	public AuthGroupAuthUsersHandler(){
		this.setHandledList("authGroupAuthUsers","authUserGroup");
		this.setEntityForStandardSourceProvider(AuthUserGroup.class);
		}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter daoFilter = AuthUserGroup.newFilterInstance(getCurrentUser(request));
		AuthGroup group = (AuthGroup)components.get("authGroup");
		daoFilter.setAlias("group", "group");
		daoFilter.setAlias("user", "user");
		daoFilter.addDefaultSort("user.userName",true);
		daoFilter.addParamHandler(new LavaEqualityParamHandler("group.id"));
		daoFilter.setParam("group.id", group.getId());
		return daoFilter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
		}
	
	
	}
