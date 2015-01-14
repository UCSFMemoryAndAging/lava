package edu.ucsf.lava.crms.importer.controller;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.importer.controller.ImportLogHandler;
import edu.ucsf.lava.crms.importer.model.CrmsImportLog;

/**
 * CrmsImportLogHandler
 * 
 * Handles the CRUD for CrmsImportLog. Only view is supported for CrmsImportLog, i.e. no add, edit, delete
 * as each ImportLog is created by the import action, and being a log, is not editable thereafter.
 * 
 * @author ctoohey
 *
 */
public class CrmsImportLogHandler extends ImportLogHandler {
	
	public CrmsImportLogHandler() {
		super();
		setHandledEntity("importLog", CrmsImportLog.class);
	}
	
	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportLogHandler instead of the core ImportLogHandler.  If scopes
	 * need to extend ImportLog further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}

	

}
