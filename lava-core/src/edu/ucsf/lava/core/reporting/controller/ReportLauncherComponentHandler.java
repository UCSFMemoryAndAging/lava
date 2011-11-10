package edu.ucsf.lava.core.reporting.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.CoreAuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentHandler;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.reporting.model.ReportSetup;

public class ReportLauncherComponentHandler extends LavaComponentHandler {
	
	public ReportLauncherComponentHandler() {
		super();
		authEvents = new ArrayList(); // none of the report events require explicit permission
		
		// the refresh event does nothing in this case, but must be handled because project context changes
		// execute a refresh (and if "refresh" is not a handled event the event will not return
		// success and project context changes will be stuck in a decision state forever, until
		// stack overflow)
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"refresh"}));
		
		// there is no command object for this handler, since there is no data to bind, but still
		// need a default object name to set the component mode/view for the view layer, and a command
		// object is required by the view decorators, so just use ReportSetup (need to use some
		// subclass of LavaEntity as it must be Serializable for webflow)
		Map<String,Class> objectMap = new HashMap<String,Class>();
		objectMap.put("reportLauncher", ReportSetup.class);
		this.setHandledObjects(objectMap);
	}

	public Event authorizationCheck(RequestContext context) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action action = CoreSessionUtils.getCurrentAction(sessionManager, request);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
				
		
		// the authorization event for reports, including the launcher, is "view"

		
		if (!authManager.isAuthorized(user,action,new CoreAuthorizationContext())) {
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{COMMAND_AUTHORIZATION_ERROR_CODE}, null);
			return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
		}
		
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	public Event initMostRecentViewState(RequestContext context) throws Exception {
		if (context.getFlowScope().get("param") == null) { 
			context.getFlowScope().put("mostRecentViewState", "launcher");
		}
		else {
			context.getFlowScope().put("mostRecentViewState", "finish");
		}
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}

	public void initBinder(RequestContext context, Object command, DataBinder binder) {
		
	}
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map backingObjects = new HashMap<String,Object>();
		// there is no command object for this handler, since there is no data to bind, but a command
		// object is required by the view decorators, so just use ReportSetup (need to use some
		// subclass of LavaEntity as it must be Serializable for webflow)
		backingObjects.put(getDefaultObjectName(), new ReportSetup());
		return backingObjects;
	}


	public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		setComponentMode(request, getDefaultObjectName(), "vw");
		setComponentView(request, getDefaultObjectName(), "view");
	}


	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
	 	String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
	 	StateDefinition state = context.getCurrentState();
		ReportSetup reportSetup = (ReportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

	 	super.addReferenceData(context, command, errors, model);
 
		return model;
	}
		
	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

}
