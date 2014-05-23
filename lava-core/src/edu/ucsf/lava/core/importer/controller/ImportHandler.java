package edu.ucsf.lava.core.importer.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.CoreAuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.importer.model.ImportLog;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_DATE_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_TIME_FORMAT;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_DATA_FILE_TYPE;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_REPOSITORY_ID;


// subclass BaseEntityComponentHandler even though there is not entity CRUD involved with importing, because
// there are still a number of methods that are useful and applicable, such as initBinder, which therefore do
// not have to be implemented
public class ImportHandler extends BaseEntityComponentHandler {

	public ImportHandler() {
		super();

		// the following will set up "import" as the defaultObjectName which is used for setting
		// the view component mode and view. the objectMap is also used to determine which objects should
		// be acted on by the persistence methods; while "import" is not a persistent object, there
		// are no persistence events handled by this handler so no issues having it in objectMap
		
		// the defaultObjectName should ideally be the same as the target part of the action which
		// uses this handler, i.e. lava.core.importer.import.import so target='import', because
		// the flow constructs event transitions using the target part of the action (at least for
		// customizing actions) while the decorator uses the defaultObjectName on eventButton that
		// will construct the event to be submitted which should match the transition
		setHandledEntity("import", ImportSetup.class);
		
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"import", "close"}));
		
//TODO: figure out authEvents
		authEvents = new ArrayList();
		
		this.requiredFieldEvents.addAll(Arrays.asList("import"));
		this.setRequiredFields(new String[]{"definitionId"});
		
		DateConverter dateConverter = new DateConverter(null);
		dateConverter.setPatterns(new String[]{DEFAULT_DATE_FORMAT});
		ConvertUtils.register(dateConverter, java.util.Date.class);
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
		ImportSetup importSetup = (ImportSetup) initializeNewCommandInstance(context,EntityBase.MANAGER.create(getDefaultObjectClass()));
		backingObjects.put(this.getDefaultObjectName(), importSetup);
		
		// put in the importLog which will be populated on import with import log / result and
		// will be displayed when import is complete
		ImportLog importLog = new ImportLog();
		backingObjects.put("importLog", importLog);
		
		return backingObjects;
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
	 	String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
	 	StateDefinition state = context.getCurrentState();

	 	ImportSetup importSetup = (ImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

	 	model = super.addReferenceData(context, command, errors, model);

	 	model.put("flowState", state.getId());
	 	
	 	if (state.getId().equals("edit")) {
	 		// load in static lists for importSetup, e.g. list of all import definitions
			this.addListsToModel(model, listManager.getStaticListsForEntity("import"));
			
			//	load up dynamic lists
			Map<String,String> definitionList = listManager.getDynamicList("importDefinition.definitions"); 
			dynamicLists.put("importDefinition.definitions", definitionList);
		}

	 	// get the list of all importLogs for the selected import definition, to be displayed as a 
	 	// secondary list component in both "edit" and "result" flow states
	 	//TODO: setup LavaDaoFilter to query for this and put resulting list in a ScrollablePagedListHolder
	 	// via setSourceFromEntityList and put in model as "importLogs"
	 	
		model.put("dynamicLists", dynamicLists);			
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
	
	
	// this is called in getUploadFile which uploads the specified file and populates the mappingFile LavaFile properties
	protected LavaFile getLavaFileBackingObject(RequestContext context, Map components, BindingResult errors) throws Exception{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaFile dataFile = new LavaFile();
		dataFile.setContentType(IMPORT_DATA_FILE_TYPE);
		dataFile.setRepositoryId(IMPORT_REPOSITORY_ID);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		dataFile.setFileStatusBy(user.getShortUserNameRev());
		dataFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));
		return dataFile;
		/*
		ImportSetup importSetup = (ImportSetup) components.get(getDefaultObjectName());
		if(LavaFile.class.isAssignableFrom(importSetup.getDataFile().getClass())){
			return (LavaFile)importSetup.getDataFile();
		}
		return null;
		 * 
		 */
	}
	
		
	protected Event doImport(RequestContext context, Object command, BindingResult errors) throws Exception {
		ImportSetup importSetup = (ImportSetup) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		// definition and mapping file
		importSetup.setImportDefinition(ImportDefinition.findOneById(importSetup.getDefinitionId()));
		LavaFile mappingFile = importSetup.getImportDefinition().getMappingFile();
		
		// data file
		MultipartFile dataMultipartFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
		if (dataMultipartFile == null || dataMultipartFile.getSize() == 0) {
			//TODO: error msg that no data file specified
			LavaComponentFormAction.createCommandError(errors, "No Data File Specified");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}
		
		// definition mapping and data arrays
//TODO: redo these comments with a definition for each array		
		// dataColumns are the column headers in the data file so are specific to the data file and can be anything. the same column
		// headers are in the definition mapping file and these are stored in columns array. the properties array is mapped to columns
		// in the definition mapping file and the values are class property names that can be set via reflection
	
		// note that the data file column headers match up with the definition mapping file property names, in terms of array indices,
		// so that do not actually need the data file column headers in the mapping file for import purposes, but they are there
		// for clarity and guidance in creating the definition mapping file. plus it provides a validation step to compare the data
		// file column names with the definition mapping file column names to make sure that the import is as intended
		
		Scanner fileScanner = new Scanner(new ByteArrayInputStream(mappingFile.getContent()), "UTF-8");
		String currentLine; 
		// line 1 is columns, line 2 is properties
		if (fileScanner.hasNextLine()) {
			currentLine = fileScanner.nextLine().trim();
//TODO: need file format separator(comma or tab) in ImportDefinition			
			importSetup.setMappingCols(currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
		}
		else {
			//TODO: error msg: definition mapping file does not contain the first row of column names
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}
		if (fileScanner.hasNextLine()) {
			currentLine = fileScanner.nextLine().trim();
//TODO: need file format separator(comma or tab) in ImportDefinition			
			importSetup.setMappingProps(currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
		}
		else {
			//TODO: error msg: definition mapping file does not contain the second row of property names
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}


//			Scanner lineScanner = new Scanner(currentLine).useDelimiter("\t");
//			Scanner lineScanner = new Scanner(currentLine).useDelimiter(",");

//			while (lineScanner.hasNext()) {
//				String nextField = lineScanner.next();
//			}
		
		
		// create an array of entity.property to set for each CSV index
		
		// somehow using the entity name to get the entity class, instantiate an instance of the class
		
		// iterate thru the data file
		
		// should have a helper method here which Crms can subclass to do the Crms specific parts of the 
		// upload concerning Patient, Project, Visit
		
		// for each row read there will be an array of the data values
		// using the prepared array of the entity.property names, use reflection to set each data value on the
		// entity, i.e. array indices of entity.property names and data file values match up
		
		// record successful and failed records in the importLog
		// record any warnings/alerts/info msgs in the importLog
		
		//TODO: validation
		
		// upon successful completion write the data file to the repository
		// upon success or failure, persist the importLog
		
		// upon returning SUCCESS Event the flow will transition to "importLog" state which will
		// be the same jsp (crms/importer/import/import.jsp) but will display completely different
		// fields (could either display the importLog properties requiring metadata or could output
		// data string)
		// note: the CrmsImportLog will have Crms specific log fields so the Crms jsp will be used
		
//OTHER:		
		// on import:
			// read the definition mapping file into a Map (data file column keys to entity.property values)
			// read the data file column headers, iterating thru the definition Mapping file to find each
			//  column in the mapping file and store the CSV index (instead of current technique of having
			//  a var for each column index, could have Map fpr each entity mapping propety to the CSV index
			//  and since the # entity's and enitty types will be variable across all definitions could 
			//  dynamically create a Map of these entity Maps
		
			// upon reading a row of data, process Patient Map first then Caregiver, Contact Info and other
			// Patient module entities, then Visit, then assessments
			// read row of data into CSV array and then use the indexes from the Entity map to get the
			// value for a given property and use reflection to set the value on the property
		
			// TODO will be a validation step to query the metadata for an entity.property to validate
		 	// against a range/dropdown, etc.
		
		
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



