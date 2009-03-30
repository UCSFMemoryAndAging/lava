package edu.ucsf.lava.core.webflow;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.engine.ViewSelector;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.ViewSelection;
import org.springframework.webflow.execution.support.ApplicationView;
import org.springframework.webflow.execution.support.FlowExecutionRedirect;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;


public class CustomViewSelector implements ViewSelector, Serializable, ManagersAware {

    protected SessionManager sessionManager;
    public CustomViewSelector() {};
	
	public boolean isEntrySelectionRenderable(RequestContext context) {
		// enforce alwaysRedirectOnPause = true, i.e. since not using ApplicationViewSelector
		// (with literal views specified in flow definition), not using alwaysRedirectOnPause 
		// configuration attribute, so basically hard-coding it to on by returning false here
		return false;
	}

	// on entry to selecting the view, return an execution redirect, which will result in 
	// a call to makeRefreshSelection to get the view name for the redirect 
	public ViewSelection makeEntrySelection(RequestContext context) {
		return FlowExecutionRedirect.INSTANCE;
	}

	// return the view that should be redirected to, which could be an instance or
	// project specific view (which is why this CustomViewSelector was written)
	// note: webflow automatically appends the _flowExecutionKey to the returned view 
	public ViewSelection makeRefreshSelection(RequestContext context) {
		// the currentAction is set in FlowListener whenever a view-state is entered, so at
		// this point know that the currentAction is correct and can use it to generate the
		// application view
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager, request);
		String viewName = currentAction.getActionView();
		return new ApplicationView(viewName, context.getModel().asMap());  
	}
	
	public void updateManagers(Managers managers) {
			this.sessionManager = CoreManagerUtils.getSessionManager(managers);
	
	}
	
}
