package edu.ucsf.lava.crms.importer.controller;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.importer.controller.ImportTemplateHandler;

/**
 * CrmsImportTemplateHandler
 * 
 * Handles the CRUD for CrmsImportTemplate. 
 * 
 * @author ctoohey
 *
 */
public class CrmsImportTemplateHandler extends ImportTemplateHandler {

	public CrmsImportTemplateHandler() {
		super();
	}
	
	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportTemplateHandler instead of the core ImportTemplateHandler.  If scopes
	 * need to extend ImportTemplate further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
	

}
