package edu.ucsf.lava.core.auth.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;

public class AuthUsersHandler extends BaseListComponentHandler { 

	public AuthUsersHandler() {
		super();
		this.setHandledList("authUsers","authUser");
		this.setEntityForStandardSourceProvider(AuthUser.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = AuthUser.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("shortUserNameRev",true);
		filter.addParamHandler(new LavaDateRangeParamHandler("accessAgreementDate"));
		filter.addParamHandler(new LavaDateRangeParamHandler("effectiveDate"));
		filter.addParamHandler(new LavaDateRangeParamHandler("expirationDate"));
			
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
	
	

}
