package edu.ucsf.lava.crms.controller;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.StaticPageHandler;

public class CrmsStaticPageHandler extends StaticPageHandler {

	public CrmsStaticPageHandler() {
		super();
	}

	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsStaticPageHandler instead of the core StaticPageHandler.  If scopes
	 * need to extend StaticPage further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}

}
