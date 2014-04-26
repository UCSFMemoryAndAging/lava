package edu.ucsf.lava.crms.importer.controller;

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
		setHandledEntity("crmsImportLog", CrmsImportLog.class);
	}

}
