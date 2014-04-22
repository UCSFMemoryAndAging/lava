package edu.ucsf.lava.core.importer.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.CoreAuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.calendar.controller.CalendarHandlerUtils;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.importer.model.ImportLog;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.core.reporting.model.ReportSetup;
import edu.ucsf.lava.core.session.CoreSessionUtils;

// subclass BaseEntityComponentHandler even though there is not entity CRUD involved with importing, because
// there are still a number of methods that are useful and applicable, such as initBinder, which therefore do
// not have to be implemented
public class ImportHandler extends BaseEntityComponentHandler {

	public ImportHandler() {
		super();
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"import", "close"}));
		
//TODO: figure out authEvents
		authEvents = new ArrayList();
		
//NEED TO SET "import" as requiredFieldEvent so required field validation will be done
		
	
		// the following will set up "importSetup" as the defaultObjectName which is used for setting
		// the view component mode and view. the objectMap is also used to determine which objects should
		// be acted on by the persistence methods; while "importSetup" is not a persistent object, there
		// are no persistence events handled by this handler so no issues having it in objectMap
		setHandledEntity("importSetup", ImportSetup.class);
	}
	
	public Event authorizationCheck(RequestContext context) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action action = CoreSessionUtils.getCurrentAction(sessionManager, request);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		// the authorization event for import is "view"
		
		if (!authManager.isAuthorized(user,action,new CoreAuthorizationContext())) {
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{COMMAND_AUTHORIZATION_ERROR_CODE}, null);
			return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
		}
		
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	
	/**
	 * Create the filter used for the report query.   
	 */
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map backingObjects = new HashMap<String,Object>();
		ImportSetup importSetup = new ImportSetup();
		backingObjects.put(this.getDefaultObjectName(), importSetup);
		
		// put in the importLog which will be populated on import with import log / result and
		// will be displayed when import is complete
		ImportLog importLog = new ImportLog();
		backingObjects.put("importLog", importLog);
		
		return backingObjects;
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
// if get the MultipartFile binding working such that importFile is a property, not sure if could put it here
	    setRequiredFields(new String[]{"templateName"});
	    return getRequiredFields();
	}
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
	 	String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
	 	StateDefinition state = context.getCurrentState();
		ImportSetup importSetup = (ImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

	 	model = super.addReferenceData(context, command, errors, model);

	 	if (state.getId().equals("importSetup")) {
	 		// load in static lists for importSetup, e.g. list of all import templates
			this.addListsToModel(model, listManager.getStaticListsForEntity("importSetup"));
		}

	 	// get the list of all importLogs for the selected import template, to be displayed as a 
	 	// secondary list component in both "importSetup" and "importLog" flow states
	 	//TODO: setup LavaDaoFilter to query for this and put resulting list in a ScrollablePagedListHolder
	 	// via setSourceFromEntityList and put in model as "importLogs"
	 	
	 	
 
		return model;
	}
		
	

	
	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		String event = ActionUtils.getEventName(context);
		// note: do not use "event = ActionUtils.getEvent(request)" because it is possible for a flow
		//  to set an overrideEvent which will be different from the event request parameter
		
		if(event.startsWith("import")){
			return this.handleImportEvent(context,command,errors);
		}	
		else if(event.startsWith("close")){
			return this.handleCloseEvent(context,command,errors);
		}	
		else {
			return this.handleCustomEvent(context,command,errors);
		}
	}
		
	
	public Event handleImportEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doImport(context, command,errors);
	}
		
	protected Event doImport(RequestContext context, Object command, BindingResult errors) throws Exception {
		
		// check that MultipartFile was supplied
		
		// for the specified templateName, read the template mapping file into a Map (or two Maps; one with column names
		// as keys, one with property names as keys)
		
		// read the data file column headers
		// validate against template mapping file
		
		// create an array of entity.property to set for each CSV index
		
		// somehow using the entity name to get the entity class, instantiate an instance of the class
		
		// iterate thru the data file
		
		// should have a helper method here which Crms can subclass to do the Crms specific parts of the 
		// upload concening Patient, Project, Visit
		
		// for each row read there will be an array of the data values
		// using the prepared array of the entity.property names, use reflection to set each data value on the
		// entity
		
		// record successful and failed records in the importLog
		// record any warnings/alerts/info msgs in the importLog
		
		//TODO: validation
		
		// upon successful completion persist the data file to the repository
		// upon success or failure, persist the importLog
		
		// upon returning SUCCESS Event the flow will transition to "importLog" state which will
		// be the same jsp (crms/importer/import/import.jsp) but will display completely different
		// fields (could either display the importLog properties requiring metadata or could output
		// data string 
		// note: the CrmsImportLog will have Crms specific log fields so the Crms jsp will be used
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	

	public Event handleCloseEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doClose(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doClose(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}



	
}



