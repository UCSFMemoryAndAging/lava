package edu.ucsf.lava.crms.auth.controller;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.controller.AuthRoleHandler;
import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.crms.auth.model.CrmsAuthRole;

public class CrmsAuthRoleHandler extends AuthRoleHandler {
	
	public CrmsAuthRoleHandler(){
		super();
		setHandledEntity("authRole", CrmsAuthRole.class);
		this.setRequiredFields(new String[]{
			"roleName",
			"patientAccess",
			"phiAccess",
			"ghiAccess"});
	}

	@Override
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		super.addReferenceData(context, command, errors, model);
		this.addListsToModel(model,listManager.getStaticListsForEntity("crmsAuthRole"));
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

