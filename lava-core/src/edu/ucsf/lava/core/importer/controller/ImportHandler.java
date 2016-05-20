package edu.ucsf.lava.core.importer.controller;

import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_DATA_FILE_TYPE;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_REPOSITORY_ID;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.CSV_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.DEFAULT_DATE_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.TAB_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.SKIP_INDICATOR;
import static edu.ucsf.lava.core.webflow.builder.ImportFlowTypeBuilder.IMPORT_EVENTS;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
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

import au.com.bytecode.opencsv.CSVReader;

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
		
		// definition mapping and data arrays are defined as members of ImportSetup which is the (primary) command
		// object used in the import. the import definition mapping file is read into the mapping arrays and the data file
		// is read into the data arrays, and the import process then works with these arrays

		// data file
		MultipartFile dataMultipartFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
		if (dataMultipartFile == null || dataMultipartFile.getSize() == 0) {
			LavaComponentFormAction.createCommandError(errors, "No Data File Specified");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}
		
//TODO: add import event to audit events (persisted import data file in combination with importLog will essentially be the 
// import data audit log)		
		
		// mapping file 
		// note that import definition handling has already validated the mapping file contents, so can assume the mapping file is valid
		InputStream mappingFileContent = new ByteArrayInputStream(mappingFile.getContent());		
		CSVReader reader = null;
		if (importSetup.getImportDefinition().getDataFileFormat().equals(CSV_FORMAT)) {
			reader = new CSVReader(new InputStreamReader(mappingFileContent));
		}
		//TODO: tab delimited has not been tested
		else if (importSetup.getImportDefinition().getDataFileFormat().equals(TAB_FORMAT)) {
			reader = new CSVReader(new InputStreamReader(mappingFileContent), '\t');
		}
	
		// line 1 is columns, line 2 is entities, line 3 is properties
		
		// mapping file columns
		//opencsv readNext parses the record into a String array
		// call trimTrailingCommas in case there are extra blank columns at the end of the row (which coordinators setting up mapping
		// files often cannot detect because they cannot see them when viewing csv in Excel. would have to open the csv in a text
		// editor to see one or more consecutive commas at the end of the row)
		importSetup.setMappingCols(trimTrailingCommas(reader.readNext()));
		
		// mapping file entities
		importSetup.setMappingEntities(reader.readNext());

		// mapping file properties
		importSetup.setMappingProps(reader.readNext());

		// read in the data column headers
		ImportFile dataFile = (ImportFile) this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
		// dataFile needs definitionName for generating a location in the repository (folder is named after the definition name
		// with encoding as necessary
		dataFile.setDefinitionName(importLog.getDefinition().getName());
		importLog.setDataFile(dataFile);
		
		InputStream dataFileContent = new ByteArrayInputStream(dataFile.getContent());		
		reader = null;
		if (importSetup.getImportDefinition().getDataFileFormat().equals(CSV_FORMAT)) {
			reader = new CSVReader(new InputStreamReader(dataFileContent));
		}
		else if (importSetup.getImportDefinition().getDataFileFormat().equals(TAB_FORMAT)) {
			reader = new CSVReader(new InputStreamReader(dataFileContent), '\t');
		}

		// nextLine[] is an array of values from the line
		String [] nextLine;
		if ((nextLine = reader.readNext()) != null) {
			// call trimTrailingCommas in case there are extra blank columns at the end of the row. this should not happen
			// for the first row of column headers, but in case it does. this should NOT be done for the data rows because
			// data rows could very well have blank/empty/null values
			importSetup.setDataCols(trimTrailingCommas(nextLine));
		}
		if (validateAndMapDataFile(errors, importSetup.getImportDefinition(), importSetup).getId().equals(ERROR_FLOW_EVENT_ID)) {
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
	 * Generate a map of every mapping file index to a data file index, to be used in setting key data indices
	 * and setting the data file variable values on entity properties.
	 *
	 * Subclass override must either invoke the superclass method or create the dataColMappingCol Map data.
	 * 
	 * @param importDefinition
	 * @param importSetup
	 * @param errors
	 * @return
	 */
	protected Event validateAndMapDataFile(BindingResult errors, ImportDefinition importDefinition, ImportSetup importSetup) throws Exception {
			// check that data file does not have more than one instance of a column (doing the same for mapping file in 
			// ImportDefinitionHandler)
			int j,k;
			for (j=0; j<importSetup.getDataCols().length;j++) {
			    for (k=j+1;k<importSetup.getDataCols().length;k++) {
			        if (importSetup.getDataCols()[j].equalsIgnoreCase(importSetup.getDataCols()[k])) { 
						LavaComponentFormAction.createCommandError(errors, "Invalid data file. Column " + (j+1) + ". Data file column " + importSetup.getDataCols()[j] + " appears multiple times in data file");
						return new Event(this,ERROR_FLOW_EVENT_ID);
			        }
			    }
			}
			
			// Validate
			// a valid data file should have corresponding mapping information for each data column. if a data column should not be processed,
			// i.e. the values in that column should not be imported, there should still be mapping information indicating that the column
			// should be skipped, by prefixing the mapping column with SKIP:
			// by requiring that every column in the data file be mapped in some way, we are enforcing data integrity that no data values
			// will be accidentally skipped during import
			
			// in other words, the mapping file should not have fewer columns than the data file.
			// note that the mapping file may have more columns than the data file, because
			// a) a data file variable may map to more than one mapping file variables if the data value will be set on multiple entity properties
			// b) the mapping file may have DEFAULT values where the value for a particular property does not come from the data file but
			//    instead is set to a DEFAULT value


			// Map
			// generate a map of every mapping file index (except for default values) to a data file index, to be used in setting 
			// key data indices and setting the data file variable values on entity properties.
			// note: multiple mapping file indices could map to the same data file index, meaning that a given imported
			//       data variable value could be set on multiple entity properties
			// note: the index for any default value mappings in the mapping file will not be mapped to a data file index, i.e
			//       it will not be present in this map

			
			for (j=0; j < importSetup.getDataCols().length; j++) {
				int mappingColMatch = -1;
				for (k=0; k < importSetup.getMappingCols().length; k++) {
					if (importSetup.getDataCols()[j].equalsIgnoreCase(importSetup.getMappingCols()[k]) ||
							(SKIP_INDICATOR + importSetup.getDataCols()[j]).equalsIgnoreCase(importSetup.getMappingCols()[k])) {
						importSetup.getMappingColDataCol().put(k, j);
						mappingColMatch = k;
					}
				}
				if (mappingColMatch == -1) {
					LavaComponentFormAction.createCommandError(errors, "Cannot import this data file. Data file column " + importSetup.getDataCols()[j] + " is not mapped in mapping file");
					return new Event(this,ERROR_FLOW_EVENT_ID);
				}
			}
		
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}

	
	/**
	 * Find the index of a specified entity property in the data file, i.e. the column index of that property in the data file.
	 * Set the resulting index on that entity property's index property in ImportSetup (or subclass).
	 * Set the index to -1 if the property is not found in the mapping file so that there will be no attempt to import the property.
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
		// since using ArrayUtils.indexOf, matching is case sensitive. this is fine as the properties that are handled by 
		// this method are only the predefined / required properties, so the case sensitivity is in effect part of the
		// import mapping file specification
		// note that there is functionality allowing instrument properties to be case insensitive (as users may be more
		// prone to instrument variable name mistyping when creating mapping files, especially if many variables)
		while ((propIndex = ArrayUtils.indexOf(importSetup.getMappingProps(), propertyName, propIndex+1)) != -1) {
			if (importSetup.getMappingEntities()[propIndex].equalsIgnoreCase(entityName)) {
				// want the data file index that corresponds to the mapping file property, since they may be different,
				// so use the mappingColDataCol map
				BeanUtils.setProperty(importSetup, indexProperty, importSetup.getMappingColDataCol().get(propIndex)); 
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
		// set here in case handling was temporarily changed)
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

	
	/*
	 * This is a utility method that accommodates for the fact that mapping files and data files may inadvertently or
	 * for whatever reason have some trailing commas which should not be considered data elements. i.e. any string of
	 * consecutive commas at the end of the line should not represent blank elements. This method is used when parsing
	 * the header row of mapping files and data files, i.e. the column headers, and there should not be any blank
	 * column headers at the end of a row.
	 * 
	 * It should not be used for mappingEntities or mappingProps because those rows may have empty data elements by 
	 * design (if mappingEntities entry is blank it defaults to the instrument being imported. if mappingProps is blank
	 * the property name defaults to the column name).
	 * 
	 * It should not be used for data rows, where there could definitely be blank data elements.
	 */
	protected String[] trimTrailingCommas(String[] line) {
		List<String> trimmedLine = new ArrayList(Arrays.asList(line));
		for (int i = line.length-1; i >= 0; i--) {
			if (StringUtils.hasText(line[i])) {
				break;
			}
			else {
				trimmedLine.remove(i);
			}
		}
		return trimmedLine.toArray(new String[trimmedLine.size()]); 
	}

	
}



