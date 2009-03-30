package edu.ucsf.lava.core.auth.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;


public class AuthUserHandler extends BaseEntityComponentHandler {
	public AuthUserHandler() {
		super();
		setHandledEntity("authUser", AuthUser.class);
		this.setRequiredFields(new String[]{
				"userName",
				"login",
				"effectiveDate"});
	}

	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		((AuthUser)command).setEffectiveDate(AuthUser.dateWithoutTime(new Date()));
		return command;
	}

	
	

}
