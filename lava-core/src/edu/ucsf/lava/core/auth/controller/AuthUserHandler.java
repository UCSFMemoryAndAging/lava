package edu.ucsf.lava.core.auth.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.auth.PasswordDelegates;
import edu.ucsf.lava.core.auth.dto.AuthUserPasswordDto;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;


public class AuthUserHandler extends BaseEntityComponentHandler {
	
	public static final String INITIAL_AUTHENTICATION_TYPE = "authUserInitialAuthenticationType";
	public AuthUserHandler() {
		super();
		setHandledEntity("authUser", AuthUser.class);
		this.setRequiredFields(new String[]{
				"userName",
				"login",
				"effectiveDate",
				"authenticationType"});
	}

	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		((AuthUser)command).setEffectiveDate(AuthUser.dateWithoutTime(new Date()));
		return command;
	}
	
	/**
	 * Add custom list for Authentication types based on currently configured password delegates
	 */
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		model = super.addReferenceData(context, command, errors, model);
		addDynamicListToModel(model, "authUser.authenticationType", authManager.getAuthenticationTypesList());
		return model;
		}


	/**
	 * get the current authentication type and put it in flow scope so we can check it on save. 
	 */
	@Override
	public Map getBackingObjects(RequestContext context, Map components) {
		Map backingObjects = super.getBackingObjects(context, components);
		AuthUser authUser = (AuthUser)backingObjects.get(this.getDefaultObjectName());
		if(authUser!=null){
			context.getFlowScope().put(INITIAL_AUTHENTICATION_TYPE, authUser.getAuthenticationType());
		}
		return backingObjects;
	}


	/**
	 * If the authenticationType has changed, then intialize the authentication details fields 
	 */
	@Override
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		AuthUser authUser = (AuthUser)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		if(authUser!=null){
			String initialAuthType = context.getFlowScope().getRequiredString(INITIAL_AUTHENTICATION_TYPE);
			if(!StringUtils.equals(initialAuthType,authUser.getAuthenticationType())){
				authManager.getPasswordDelegate(authUser).initializeAuthenticationDetails(authUser);
			}
		}
		return super.doSave(context, command, errors);
	}



	/**
	 * Initialize the Authentication details fields based on the Password Delegate. 
	 */
	@Override
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		AuthUser authUser = (AuthUser)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		if(authUser!=null && authUser.getAuthenticationType()!=null){
			authManager.getPasswordDelegate(authUser).initializeAuthenticationDetails(authUser);
		}
		return super.doSaveAdd(context, command, errors);
	}

	




	

}

