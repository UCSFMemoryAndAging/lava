package edu.ucsf.lava.crms.importer.controller;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.importer.controller.ImportDefinitionHandler;
import edu.ucsf.lava.crms.importer.model.CrmsImportDefinition;

/**
 * CrmsImportDefinitionHandler
 * 
 * Handles the CRUD for CrmsImportDefinition 
 * 
 * @author ctoohey
 *
 */
public class CrmsImportDefinitionHandler extends ImportDefinitionHandler {

	public CrmsImportDefinitionHandler() {
		super();
		setHandledEntity("importDefinition", CrmsImportDefinition.class);
	}
	
	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportDefinitionHandler instead of the core ImportDefinitionHandler. If scopes
	 * need to extend ImportDefinition further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
	

}
