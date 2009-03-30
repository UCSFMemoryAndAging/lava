package edu.ucsf.lava.core.webflow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.engine.ViewSelector;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;




public class DefaultActionViewSelector implements ViewSelector, Serializable,ManagersAware {

	
    protected SessionManager sessionManager;
    protected ActionManager actionManager;
	
	public DefaultActionViewSelector() {};
	
	/**
	 * Determine the flow id of the default action.  From the current action (set in the FlowListener 
	 * whenever a view-state is entered), we can identify what the current scope/module/section is and 
	 * redirect to the default action for that application area.  
	 * 
	 * The action manager routines to determine default action are designed to "degrade" gracefully 
	 * (i.e. if no default action defined for the section, it will return the default action for the module
	 * ...and so on.)
	 */
	public ViewSelection makeEntrySelection(RequestContext context) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager, request);
		Action defaultAction = actionManager.getDefaultAction(request, currentAction);
		
		/*
		 * We convert the action id into a flow id by appending the default mode for the action.
		 * Also, the Action returned from getDefaultAction may be an instance specific customization
		 * of a base action, but flows are not instance specific, so replace the instance with 
		 * the standard lava instance identifier in the flow id
		 */ 
		
		String flowId = new StringBuffer(ActionUtils.getActionIdWithLavaInstance(defaultAction.getId()))
								.append(".").append(defaultAction.getFlowTypeBuilder().getDefaultFlowMode()).toString();
		Map requestParams = new HashMap<String,String>();
		if(defaultAction.getParam(defaultAction.getIdParamName())!=null){
			requestParams.put(defaultAction.getIdParamName(), defaultAction.getParam(defaultAction.getIdParamName()));
		}
		return new FlowDefinitionRedirect(flowId, requestParams); 
	}
	
	public void updateManagers(Managers managers) {
		this.actionManager = CoreManagerUtils.getActionManager(managers);
		this.sessionManager = CoreManagerUtils.getSessionManager(managers);
	}	

	/**
	 * do not believe that this will get called for a FlowDefinitionRedirect, since the
	 * current flow will have terminated and a new flow created. this method is called when
	 * a FlowExecutionRedirect is done in a flow, either explicitly or because the config
	 * flag alwaysRedirectOnPause is on nevertheless, return the same thing as makeEntrySelection
	 */
	public ViewSelection makeRefreshSelection(RequestContext context) {
		return this.makeEntrySelection(context);
	}


	/**
	 * defaultActionViewSelector is only used for redirecting to a new flow, so always return false	
	 */
	public boolean isEntrySelectionRenderable(RequestContext context) {
		return false;
	}




}
