package edu.ucsf.lava.core.importer.controller;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;


public class ImportTemplateHandler extends BaseEntityComponentHandler {
	public ImportTemplateHandler() {
		super();
		setHandledEntity("authRole", AuthRole.class);
		this.setRequiredFields(new String[]{
				"roleName"});
	}

	
	
	//TODO: override Save to write tempalte mapping file to repository
	

	
	

}
