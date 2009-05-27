package edu.ucsf.lava.core.home.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.CoreAuthorizationContext;
import edu.ucsf.lava.core.auth.PasswordDelegate;
import edu.ucsf.lava.core.auth.dto.AuthUserPasswordDto;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.controller.LavaComponentHandler;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.session.CoreSessionUtils;

public class ChangePasswordHandler extends LavaComponentHandler {

	
	public ChangePasswordHandler() {
		super();
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"change","cancel"}));
		authEvents = new ArrayList(Arrays.asList(new String[]{"change"}));
		Map<String,Class> objectMap = new HashMap<String,Class>();
		objectMap.put("changePassword",AuthUserPasswordDto.class);
		setHandledObjects(objectMap);
		
	}
	

	/**
	 * Get the backing currentUser entity and populate an authUserPasswordDto object
	 *  
	 */
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		FlowDefinition flow = context.getActiveFlow();
		StateDefinition state = context.getCurrentState();
		String eventId = ActionUtils.getEventId(context);
		Map backingObjects = new HashMap<String,Object>();
		
		
		//this handler only supports a current user changing their own password, so just use the current user
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		backingObjects.put(this.getDefaultObjectName(), new AuthUserPasswordDto(user,authManager.getPasswordDelegate(user)));
		return backingObjects;
	}
	
	 public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		String eventName = ActionUtils.getEventName(context);
	
		if(eventName.equals("cancel")){
			return this.handleCancelEvent(context,command,errors);
		}
		else if(eventName.equals("change")){
			return this.handleChangeEvent(context,command,errors);
		}else {
			return this.handleCustomEvent(context,command,errors);
		}
	}

		
	//override this in subclass to handle custom action buttons. 
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
		
	public Event handleCancelEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doCancel(context,command,errors);
	}
		
	protected Event doCancel(RequestContext context, Object command, BindingResult errors) throws Exception{
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	public Event handleChangeEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doChange(context,command,errors);
	}
	
	protected Event doChange(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUserPasswordDto dto = (AuthUserPasswordDto)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		AuthUser currentUser = CoreSessionUtils.getCurrentUser(sessionManager, request);
		
		Map<String,ArrayList> changePasswordErrors = new LinkedHashMap<String,ArrayList>();
		if(authManager.changePassword(currentUser, dto.getOldPassword(), dto.getNewPassword(), dto.getNewPasswordConfirm(), changePasswordErrors)){
			return new Event(this,SUCCESS_FLOW_EVENT_ID);
		}
		//if we get here than the password was not changed, report the error, clear the dto and return
		for(Entry<String,ArrayList> error : changePasswordErrors.entrySet()){
			LavaComponentFormAction.createCommandError(errors, error.getKey(), (error.getValue()==null ? null : error.getValue().toArray()));
		}
		dto.clear();
		return new Event(this,ERROR_FLOW_EVENT_ID);
			
	}
		
	/**
	 * Check to ensure the current user has permission to change password. 
	 * 
	 * 
	 */
	public Event authorizationCheck(RequestContext context, Object command) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		Action action = actionManager.getCurrentAction(request);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager,request);
		
		if (isAuthEvent(context)) {
			if (!authManager.isAuthorized(user,action,new CoreAuthorizationContext())) {
				if (context.getFlowExecutionContext().getActiveSession().isRoot()) {
					throw new RuntimeException(this.getMessage(COMMAND_AUTHORIZATION_ERROR_CODE));
				}else{
					CoreSessionUtils.addFormError(sessionManager, request, new String[]{COMMAND_AUTHORIZATION_ERROR_CODE}, null);
					return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
				}
			}
		}
			
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
		
	 public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		 // check the flow id, and if necessary, the event id, to determine how to set componentMode
		 // and componentView for this component
		 HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		 String event = ActionUtils.getEventName(context);
		 String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		 StateDefinition state = context.getCurrentState();
			setComponentMode(request, getDefaultObjectName(), "dc");
			setComponentView(request, getDefaultObjectName(), "change");
		
	}


	public void initBinder(RequestContext context, Object command, DataBinder binder) {
	}

	

		 
	 
	
		

}
