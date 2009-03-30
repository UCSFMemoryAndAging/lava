package edu.ucsf.lava.crms.auth.controller;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.controller.AuthUserRoleHandler;
import edu.ucsf.lava.crms.auth.model.CrmsAuthRole;
import edu.ucsf.lava.crms.auth.model.CrmsAuthUserRole;

public class CrmsAuthUserRoleHandler extends AuthUserRoleHandler {

	public CrmsAuthUserRoleHandler() {
		super();
		setHandledEntity("authUserRole", CrmsAuthUserRole.class);
		this.setRequiredFields(new String[]{
			"roleId",
			"project"});
	}

	@Override
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		super.addReferenceData(context, command, errors, model);
		this.addListsToModel(model,listManager.getStaticListsForEntity("crmsAuthUserRole"));
		return model;
	}

	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsAuthRoleHandler instead of the core AuthUserRole.  If scopes
	 * need to extend AuthRole further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
	
	
}
