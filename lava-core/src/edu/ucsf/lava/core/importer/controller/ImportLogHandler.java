package edu.ucsf.lava.core.importer.controller;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
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

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		// put individual log messages in a component for the view to display
	 	ImportLog importLog = (ImportLog) ((ComponentCommand)command).getComponents().get("importLog");
		ScrollablePagedListHolder logMessagesListHolder = new ScrollablePagedListHolder();
		logMessagesListHolder.setSourceFromEntityList(importLog.getMessages());
		((ComponentCommand)command).getComponents().put("importLogMessages", logMessagesListHolder);

		return model;
	}

}
