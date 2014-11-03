package edu.ucsf.lava.core.importer.controller;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang.ArrayUtils;

import org.springframework.util.StringUtils;
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
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.file.model.ImportFile;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.importer.model.ImportLog;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.*;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_DATA_FILE_TYPE;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_REPOSITORY_ID;
import static edu.ucsf.lava.core.webflow.builder.ImportFlowTypeBuilder.IMPORT_EVENTS;


// subclass BaseEntityComponentHandler even though there is not entity CRUD involved with importing, because
// there are still a number of methods that are useful and applicable, such as initBinder, which therefore do
// not have to be implemented
public class ImportHandler extends BaseEntityComponentHandler {
	private ConvertUtilsBean convertUtilsBean;

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
		
		authEvents = new ArrayList(Arrays.asList(IMPORT_EVENTS));
		
		this.requiredFieldEvents.addAll(Arrays.asList("import"));
		this.setRequiredFields(new String[]{"definitionId"});

		// bean converters are set up in doImport
		this.convertUtilsBean = BeanUtilsBean.getInstance().getConvertUtils();
	}
	
	 public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		 // check the flow id, and if necessary, the event id, to determine how to set componentMode
		 // and componentView for this component
		 HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		 String event = ActionUtils.getEventName(context);
		 String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		 StateDefinition state = context.getCurrentState();

		if (state.getId().equals("edit")) {
			setComponentMode(request, getDefaultObjectName(), "dc");
			setComponentView(request, getDefaultObjectName(), "edit");
		}
		else if (state.getId().equals("result")) {
			setComponentMode(request, getDefaultObjectName(), "vw");
			setComponentView(request, getDefaultObjectName(), "view");
		}
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
	 * Create the setup object used to do the import, as well as the log object to log import messages.   
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
	 	ImportLog importLog = (ImportLog) ((ComponentCommand)command).getComponents().get("importLog");

	 	model = super.addReferenceData(context, command, errors, model);

	 	model.put("flowState", state.getId());
	 	
	 	if (state.getId().equals("edit")) {
	 		// load in static lists for importSetup, e.g. list of all import definitions
			this.addListsToModel(model, listManager.getStaticListsForEntity("import"));
			
			//	load up dynamic lists
			Map<String,String> definitionList = listManager.getDynamicList("importDefinition.definitions"); 
			dynamicLists.put("importDefinition.definitions", definitionList);
		}
	 	else if (state.getId().equals("result")){
			// individual log messages
			ScrollablePagedListHolder logMessagesListHolder = new ScrollablePagedListHolder();
			logMessagesListHolder.setPageSize(250);
			logMessagesListHolder.setSourceFromEntityList(importLog.getMessages());
			((ComponentCommand)command).getComponents().put("importLogMessages", logMessagesListHolder);
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
		LavaFile dataFile = new ImportFile();
		dataFile.setContentType(IMPORT_DATA_FILE_TYPE);
		dataFile.setRepositoryId(IMPORT_REPOSITORY_ID);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		dataFile.setFileStatusBy(user.getShortUserNameRev());
		dataFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));
		return dataFile;
	}
	
		
	protected Event doImport(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ImportSetup importSetup = (ImportSetup) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		ImportLog importLog = (ImportLog) ((ComponentCommand)command).getComponents().get("importLog");
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		importLog.setImportedBy(user.getUserName());

		// definition and mapping file
//TODO: eagerly load ImportDefinition instead of explicitly loading it		
		importSetup.setImportDefinition(ImportDefinition.findOneById(importSetup.getDefinitionId()));
		importLog.setDefinition(importSetup.getImportDefinition());
		LavaFile mappingFile = importSetup.getImportDefinition().getMappingFile();

		// setup BeanUtilConverters for setting imported data values on entity properties
		this.setupBeanUtilConverters(importSetup);
		
		// data file
		MultipartFile dataMultipartFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
		if (dataMultipartFile == null || dataMultipartFile.getSize() == 0) {
			//TODO: error msg that no data file specified
			LavaComponentFormAction.createCommandError(errors, "No Data File Specified");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}
		
//TODO: add import event to audit events (persisted import data file in combination with importLog will essentially be the 
// import data audit log)		
		
		// definition mapping and data arrays
//TODO: redo these comments with a definition for each String array:
// String mappingCols[];
// String mappingProps[];
// String dataCols[];
// String dataValues[];
		
		// dataColumns are the column headers in the data file so are specific to the data file and can be anything. the same column
		// headers are in the definition mapping file and these are stored in columns array. the properties array is mapped to columns
		// in the definition mapping file and the values are class property names that can be set via reflection
	
		// note that the data file column headers match up with the definition mapping file property names, in terms of array indices,
		// so that do not actually need the data file column headers in the mapping file for import purposes, but they are there
		// for clarity and guidance in creating the definition mapping file. plus it provides a validation step to compare the data
		// file column names with the definition mapping file column names to make sure that the import is as intended
		
		Scanner fileScanner = new Scanner(new ByteArrayInputStream(mappingFile.getContent()), "UTF-8");
		String currentLine; 
		// line 1 is columns, line 2 is entities, line 3 is properties
		
		// mapping file columns
		if (fileScanner.hasNextLine()) {
			currentLine = fileScanner.nextLine().trim();
			if (importSetup.getImportDefinition().getDataFileFormat().equals(CSV_FORMAT)) {
				importSetup.setMappingCols(StringUtils.commaDelimitedListToStringArray(currentLine));
			}
			else if (importSetup.getImportDefinition().getDataFileFormat().equals(TAB_FORMAT)) {
				//TODO: tab delimited has not been tested
				importSetup.setMappingCols(StringUtils.delimitedListToStringArray(currentLine, "\t"));
			}
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "Cannot import. Definition mapping file does not contain the first row of column names");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}
		
		// mapping file entities
		if (fileScanner.hasNextLine()) {
			currentLine = fileScanner.nextLine().trim();
			if (importSetup.getImportDefinition().getDataFileFormat().equals(CSV_FORMAT)) {
				importSetup.setMappingEntities(StringUtils.commaDelimitedListToStringArray(currentLine));
			}
			else if (importSetup.getImportDefinition().getDataFileFormat().equals(TAB_FORMAT)) {
				//TODO: tab delimited has not been tested
				importSetup.setMappingEntities(StringUtils.delimitedListToStringArray(currentLine, "\t"));
			}
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "Cannot import. Definition mapping file does not contain the second row of entity names");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}

		// mapping file properties
		if (fileScanner.hasNextLine()) {
			currentLine = fileScanner.nextLine().trim();
			if (importSetup.getImportDefinition().getDataFileFormat().equals(CSV_FORMAT)) {
				importSetup.setMappingProps(StringUtils.commaDelimitedListToStringArray(currentLine));
			}
			else if (importSetup.getImportDefinition().getDataFileFormat().equals(TAB_FORMAT)) {
				//TODO: tab delimited has not been tested
				importSetup.setMappingProps(StringUtils.delimitedListToStringArray(currentLine, "\t"));
			}
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "Cannot import. Definition mapping file does not contain the third row of property names");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}

		// read in the data column headers
		ImportFile dataFile = (ImportFile) this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
		// dataFile needs definitionName for generating a location in the repository (folder is named after the definition name
		// with encoding as necessary
		dataFile.setDefinitionName(importLog.getDefinition().getName());
		importLog.setDataFile(dataFile);
		fileScanner = new Scanner(new ByteArrayInputStream(dataFile.getContent()), "UTF-8");
		if (fileScanner.hasNextLine()) {
			currentLine = fileScanner.nextLine().trim();
			// should not need opencsv for column names, entity names, property names, just for data, but could use for everything
			if (importSetup.getImportDefinition().getDataFileFormat().equals(CSV_FORMAT)) {
				//importSetup.setDataCols(currentLine.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
				importSetup.setDataCols(StringUtils.commaDelimitedListToStringArray(currentLine));
			}
			else if (importSetup.getImportDefinition().getDataFileFormat().equals(TAB_FORMAT)) {
				//TODO: tab delimited files have not been tested
				//importSetup.setDataCols(currentLine.split("\t(?=([^\"]*\"[^\"]*\")*[^\"]*$)"));
				importSetup.setDataCols(StringUtils.delimitedListToStringArray(currentLine, "\t"));
			}
		}
		if (validateDataFile(errors, importSetup.getImportDefinition(), importSetup).getId().equals(ERROR_FLOW_EVENT_ID)) {

			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		// persist the dataFile file to the file repository. the dataFile itself is an ImportFile property of
		// the importLog record and will be persisted when importLog is persisted. 
		// note: this is not part of the BaseEntityComponentHandler fileOperationHandler as this is not
		// part of saving an entity via UI event handling. rather, the dataFile and importLog are saved
		// here as artifacts of the import action to import data
		
		// dataFile is saved as a historical record of the import and will never be replaced by another
		// file, so can just call saveFile, not saveOrUpdateFile
		dataFile.saveFile();

		
		// logImport.save() is not called here. it must therefore be called in a subclass or this
		// method should be refactored so that it is called here after it is populated.
		
		
		return new Event(this, SUCCESS_FLOW_EVENT_ID);		
	}

	
	/**
	 * Subclasses should override if there is additional validation that should take place before being reading 
	 * and importing data. 
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @param errors
	 * @return
	 */
	protected Event validateDataFile(BindingResult errors, ImportDefinition importDefinition, ImportSetup importSetup) throws Exception {
		// validate the mapping file data columns against the import file data columns as they should be
		// identical (mapping file is created by pasting data column headers into row 1)
		if (importSetup.getMappingCols().length != importSetup.getDataCols().length) {
			LavaComponentFormAction.createCommandError(errors, "Cannot import. Mismatch in number of columns in mapping file vs data file");
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		for (int i=0; i < importSetup.getMappingCols().length; i++) {
			if (!importSetup.getMappingCols()[i].equals(importSetup.getDataCols()[i])) {
				LavaComponentFormAction.createCommandError(errors, "Cannot import. Mapping file column name " + importSetup.getMappingCols()[i] + " does not exactly match column header in data file");
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
		}
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}

	
	/**
	 * Find the index of a specified entity property in the data file, i.e. the column index of that property in the data file.
	 * Set the resulting index on that entity property's index property in ImportSetup (or subclass).
	 * 
	 * @param importSetup
	 * @param indexProperty
	 * @param entityName
	 * @param propertyName
	 * @throws Exception
	 */
	protected void setDataFilePropertyIndex(ImportSetup importSetup, String indexProperty, String entityName, String propertyName) throws Exception {
		BeanUtils.setProperty(importSetup, indexProperty, -1);
		int propIndex = -1;
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), propertyName, propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase(entityName)) {
				BeanUtils.setProperty(importSetup, indexProperty, propIndex); 
				break;
			}
		}
	}

	

	public Event handleCloseEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doClose(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doClose(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	protected void setupBeanUtilConverters(ImportSetup importSetup) {
		// default to throwing exceptions on conversion errors (this is the default anyway, but explicitly
		// set here in case handling was temporarily changed
		this.getConvertUtilsBean().register(true, true, -1);
		
		// register a DateConverter to handle String to java.util.Date conversion
		DateConverter dateConverter = new DateConverter(null);
		// add any additional date formats to be converted to this array
		dateConverter.setPatterns(new String[]{importSetup.getImportDefinition().getDateFormat() != null ? importSetup.getImportDefinition().getDateFormat() : DEFAULT_DATE_FORMAT});
		this.getConvertUtilsBean().register(dateConverter, java.util.Date.class);
	}
	
	public ConvertUtilsBean getConvertUtilsBean() {
		return convertUtilsBean;
	}

	public void setConvertUtilsBean(ConvertUtilsBean convertUtilsBean) {
		this.convertUtilsBean = convertUtilsBean;
	}

	

	
}



