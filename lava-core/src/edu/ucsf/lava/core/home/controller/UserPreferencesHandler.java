package edu.ucsf.lava.core.home.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.session.CoreSessionUtils;

public class UserPreferencesHandler extends BaseEntityComponentHandler {

	public UserPreferencesHandler() {
		super();
		setHandledEntity("userPreferences", AuthUser.class);
	}

	/**
	 * This method only works on the current user record, so override get backing objects to put
	 * a copy of the current user in the backing objects.
	 */
	@Override
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUser authUser = CoreSessionUtils.getCurrentUser(sessionManager,request);
		Map backingObjects = new HashMap<String,Object>();
		backingObjects.put(this.getDefaultObjectName(),AuthUser.MANAGER.getById(authUser.getId(), AuthUser.newFilterInstance()));
		return backingObjects;
	}


	//override and throw errors on the most critical methods that 
	//we want to make sure are never called by this handler. 
	@Override
	protected Event doAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}
	@Override
	protected Event doDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}
	@Override
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}

	@Override
	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}

	/**
	 * refresh the current user object after updating the record. 
	 */
	@Override
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		Event result = super.doSave(context, command, errors);
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUser currentUser = CoreSessionUtils.getCurrentUser(sessionManager, request);
		if(currentUser != null){
			currentUser.refresh();
		}
		return result;
	}
	
	
	
	
	
}
