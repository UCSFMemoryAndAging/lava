package edu.ucsf.lava.core.importer.controller;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;


public class ImportLogHandler extends BaseEntityComponentHandler {
	public ImportLogHandler() {
		super();
		setHandledEntity("authRole", AuthRole.class);
		this.setRequiredFields(new String[]{
				"roleName"});
	}

	
	
	

	
	

}
