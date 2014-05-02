package edu.ucsf.lava.crms.importer.controller;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.importer.controller.ImportHandler;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.crms.importer.model.CrmsImportSetup;

/**
 * CrmsImportHandler
 * 
 * Handles the crms specific part of importing a data file.
 * 
 * @author ctoohey
 *
 */
public class CrmsImportHandler extends ImportHandler {

	public CrmsImportHandler() {
		super();
		setHandledEntity("importSetup", CrmsImportSetup.class);
		setDefaultObjectBaseClass(ImportSetup.class);
		this.setRequiredFields(new String[]{
				"templateName",
				"dataFileInput",
				"projName"});
	}

	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportHandler instead of the core ImportHandler.  If scopes
	 * need to extend Import further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
	
		
	
	//TODO: implement isAuthorized for project authorization (make sure proj auth flag is 
	// set on importSetup. assume it is ok that importSetup is not a persistent object
	
	
	
	
	
//NOTE: more than likely this will be a helper method override rather than doImport because the crms functionality
//is in the  middle of basic import setup and import finish stuff
	protected Event doImport(RequestContext context, Object command, BindingResult errors) throws Exception {
		
		// projNeme is required if the import is inserting new Patients, Visits and Instruments, so check to make
		// sure it has been specified in the template mappingFile or in CrmsImportSetup
		//TODO: check for projName
		//?? would need to distinguish INSERT from UPDATE because if UPDATE would not necessarily need projName
		
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);

	}

	@Override
	public Map addReferenceData(RequestContext context, Object command,
			BindingResult errors, Map model) {

		CrmsImportSetup crmsImportSetup = (CrmsImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

		return super.addReferenceData(context, command, errors, model);
	}

	
	
}
