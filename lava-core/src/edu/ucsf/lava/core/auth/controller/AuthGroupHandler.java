package edu.ucsf.lava.core.auth.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;


public class AuthGroupHandler extends BaseEntityComponentHandler {
	public AuthGroupHandler() {
		super();
		setHandledEntity("authGroup", AuthGroup.class);
		this.setRequiredFields(new String[]{
				"groupName",
				"effectiveDate"});
	}

	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		((AuthGroup)command).setEffectiveDate(AuthGroup.dateWithoutTime(new Date()));
		return command;
	}

	
	

}
