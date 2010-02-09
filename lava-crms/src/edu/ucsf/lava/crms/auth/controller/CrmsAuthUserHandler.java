package edu.ucsf.lava.crms.auth.controller;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.controller.AuthUserHandler;
import edu.ucsf.lava.crms.auth.model.CrmsAuthUser;

//EMORY change: fixing bug where CrmsAuthUser not created (had to create this class)
public class CrmsAuthUserHandler extends AuthUserHandler {

	public CrmsAuthUserHandler() {
		super();
		setHandledEntity("authUser", CrmsAuthUser.class);
	}

	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsAuthUserHandler instead of the core AuthUser.  If scopes
	 * need to extend AuthUser further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
}
