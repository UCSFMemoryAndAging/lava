package edu.ucsf.lava.core.auth.controller;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;


public class AuthRoleHandler extends BaseEntityComponentHandler {
	public AuthRoleHandler() {
		super();
		setHandledEntity("authRole", AuthRole.class);
		this.setRequiredFields(new String[]{
				"roleName"});
	}

	
	
	

	
	

}
