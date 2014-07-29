package edu.ucsf.lava.core.importer.controller;

import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.importer.model.ImportLog;

/**
 * ImportLogHandler
 * 
 * Handles the CRUD for ImportLog. Only view is supported for ImportLog, i.e. no add, edit, delete
 * as each ImportLog is created by the import action, and being a log, is not editable thereafter.
 * 
 * @author ctoohey
 *
 */
public class ImportLogHandler extends BaseEntityComponentHandler {

	public ImportLogHandler() {
		super();
		setHandledEntity("importLog", ImportLog.class);
	}


}
