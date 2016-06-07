package edu.ucsf.lava.core.importer.controller;

import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_DEF_MAPPING_FILE_TYPE;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_REPOSITORY_ID;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.CSV_FORMAT;
import static edu.ucsf.lava.core.importer.model.ImportDefinition.TAB_FORMAT;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import au.com.bytecode.opencsv.CSVReader;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.file.model.ImportFile;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;


public class ImportDefinitionHandler extends BaseEntityComponentHandler {
	
	public ImportDefinitionHandler() {
		super();
		setHandledEntity("importDefinition", ImportDefinition.class);
		this.setRequiredFields(new String[]{"name", "dataFileFormat", "startDataRow"});
		this.setSupportsAttachedFiles(true);
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ImportDefinition definition = (ImportDefinition) command;
		ImportFile mappingFile = new ImportFile();
		mappingFile.setContentType(IMPORT_DEF_MAPPING_FILE_TYPE);
		mappingFile.setRepositoryId(IMPORT_REPOSITORY_ID);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		mappingFile.setFileStatusBy(user.getShortUserNameRev());
		mappingFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));
		definition.setMappingFile(mappingFile);
		return definition;
	} 
	

	protected Event mappingFileValidation(RequestContext context, Object command, BindingResult errors) throws Exception{
		ImportDefinition definition = (ImportDefinition) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		ImportFile mappingFile = definition.getMappingFile();
			
		// row 1 of the mapping file
		String mappingCols[]; // array of the mapping file variable names
		// row 2 of the mapping file
		String mappingEntities[]; // array of the mapping file entity names (for instruments this is the instrument name 
								  // and must match the name in the Instrument table). if this is blank/null then it defaults
		  						  // to the first instrument name in the ImportDefinition 
		// row 3 of the mapping file
		String mappingProps[]; // array of the mapping file Java property names (case insensitive)

		// read the mapping file content into the above arrays
		InputStream mappingFileContent = new ByteArrayInputStream(mappingFile.getContent());		
		CSVReader reader = null;
		if (definition.getDataFileFormat().equals(CSV_FORMAT)) {
			reader = new CSVReader(new InputStreamReader(mappingFileContent));
		}
		//TODO: tab delimited has not been tested
		else if (definition.getDataFileFormat().equals(TAB_FORMAT)) {
			reader = new CSVReader(new InputStreamReader(mappingFileContent), '\t');
		}
		// nextLine[] is an array of values from the line
		String [] nextLine;
		
		// line 1 is columns, line 2 is entities, line 3 is properties
			
		// mapping file columns
		//opencsv readNext parses the record into a String array
		if ((nextLine = reader.readNext()) != null) {
			// call trimTrailingCommas in case there are extra blank columns at the end of the row (which coordinators setting up mapping
			// files often cannot detect because they cannot see them when viewing csv in Excel. would have to open the csv in a text
			// editor to see one or more consecutive commas at the end of the row)
			mappingCols = trimTrailingCommas(nextLine);
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "Invalid mapping file. Does not contain the first row of column names");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}
			
		// mapping file entities
		if ((nextLine = reader.readNext()) != null) {
			mappingEntities = nextLine;
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "Invalid mapping file. Does not contain the second row of entity names");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}

		// mapping file properties
		if ((nextLine = reader.readNext()) != null) {
			mappingProps = nextLine;
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "Invalid mapping file. Does contain the third row of property names");
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}

			
//TODO:	list all offending together instead of just first occurrence		
		// validate that there are not repeated entity.property in the mapping file
		int j,k;
		for (j=0; j<mappingProps.length;j++) {
			if (!StringUtils.hasText(mappingProps[j])) {
			continue;
		}
		    for (k=j+1;k<mappingProps.length;k++) {
				if (!StringUtils.hasText(mappingProps[k])) {
					continue;
				}
		        if (mappingProps[j].equalsIgnoreCase(mappingProps[k]) && mappingEntities[j].equalsIgnoreCase(mappingEntities[k]) ) { 
					LavaComponentFormAction.createCommandError(errors, "Invalid mapping file. Column " + (j+1) + ". Row 2 mapping entity name " + mappingEntities[j] + " and Row 3 mapping property name " + mappingProps[j] + " appear multiple times in mapping file");
					return new Event(this,ERROR_FLOW_EVENT_ID);
		        }
		    }
		}

//TODO:	list all offending together instead of just first occurrence		
		// validate that any default values in the mapping file have a property name specified, because the property cannot
		// default to the row 1 variable name if row 3 property is blank because row 1 is used to specify the default value
		for (j=0; j<mappingCols.length;j++) {
			if (mappingCols[j].startsWith(("DEFAULT:")) && !StringUtils.hasText(mappingProps[j])) {
				LavaComponentFormAction.createCommandError(errors, "DEFAULT Column " + j + ". Row 1 mapping " + mappingCols[j] + " must have a property name in Row 3");
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
		}

			
			
		//TODO: using PropertyUtils.describe (or something similar) verify that every entity.property in the mapping
		// file actually exists (would probably need to instantiate fake entities but want to do so outside the scope
		// of Hibernate)
		// note that this validation current takes place in the import handler, as setProperty will throw an exception
		// if entity.property does not exist. but would of course be better for the user if it was done now.
			
			
		return mappingFilePropertyValidation(context, command, errors, mappingCols, mappingEntities, mappingProps);
	}
	

	protected Event mappingFilePropertyValidation(RequestContext context, Object command, BindingResult errors, 
			String[] mappingCols, String[] mappingEntities, String[] mappingProps) throws Exception{
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	

	protected Event conditionalValidation(RequestContext context, Object command, BindingResult errors) throws Exception{
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
	// this is called in getUploadFile which uploads the specified file and populates the mappingFile 
	// LavaFile properties and is called in getDownloadFileContent to download the file 

	// note: utilizing the fact that every EntityViewFlowBuilder transitions on the download event to
	// EntityDownloadFlowBuilder subflow, which calls the BaseComponentFormAction prepareDownload which
	// calls LavaComponentHandler handleDownload (which calls this method to get the LavaFile to download)	
	protected LavaFile getLavaFileBackingObject(RequestContext context, Map components, BindingResult errors) throws Exception{
		ImportDefinition definition = (ImportDefinition) components.get(getDefaultObjectName());
		if(ImportFile.class.isAssignableFrom(definition.getMappingFile().getClass())){
			return (ImportFile)definition.getMappingFile();
		}
		return null;
	}
	
	
	protected Event doSaveAdd(RequestContext context, Object command,BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Event returnEvent;
		MultipartFile uploadFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
		ImportDefinition definition = (ImportDefinition) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		ImportFile mappingFile = definition.getMappingFile();
		
		if (this.conditionalValidation(context, command, errors).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		if(uploadFile != null && uploadFile.getSize() != 0){
			// doSaveAdd will save the importDefinition and mappingFile LavaFile properties. however, because the 
			// command object is not a LavaFile, but rather an importDefinition that has a LavaFile property, 
			// the afterCreate and afterUpdate triggers are not called on the LavaFile property so the file is 
			// not written to the repository. so that must be done explicitly, via LavaFile saveFile(), and must be 
			// done before doSaveAdd because it populates the fileId and location properties of LavaFile
			
			// using callback method to use core file operations exception handling 
			// have to use getDeclaredMethod instead of getMethod for non-public methods
			Method callbackMethod = BaseEntityComponentHandler.class.getDeclaredMethod("saveAddFileCallback", new Class[]{RequestContext.class, Object.class, BindingResult.class});
			returnEvent = fileOperationHandler(callbackMethod, context, command, errors);

			if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
				if (this.mappingFileValidation(context, command, errors).getId().equals(ERROR_FLOW_EVENT_ID)) {
					mappingFile.deleteFile();					
					return new Event(this, ERROR_FLOW_EVENT_ID);
				}
			}

			if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
				definition.setCreated(new Date());
				String createdBy = CoreSessionUtils.getCurrentUser(sessionManager, request).getUserName();
				if (createdBy != null && createdBy.length() > 25) {
					// have to truncate due to database mismatch in column lengths
					createdBy = createdBy.substring(0,24);
				}
				definition.setCreatedBy(createdBy);
				return super.doSaveAdd(context, command, errors);
			}
			else {
				return returnEvent;
			}
		}
		else {
			// allow save even if mapping file is not supplied as user may not have mapping file ready
			// but wants to start definition
			return super.doSaveAdd(context, command, errors);
		}
	}

	protected Event saveAddFileCallback(RequestContext context, Object command, BindingResult errors) throws Exception {
		ImportDefinition importDefinition = (ImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		ImportFile mappingFile = (ImportFile) getLavaFileBackingObject(context, ((ComponentCommand)command).getComponents(), errors);

		// adding a definition where user is specifying a mapping file for the first time

		// getUploadFile will populate the mappingFile property due to getLavaBackingFileObject override
		mappingFile = (ImportFile) this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
		mappingFile.setDefinitionName(importDefinition.getName());
		
		// since mapping files will be stored in a definition specific repository folder there is no 
		// chance of overwriting an existing file when adding a new definition, so do not need to use 
		// saveOrUpdateFile
		mappingFile.saveFile();
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	

	
	protected Event doSave(RequestContext context, Object command,BindingResult errors) throws Exception {
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);

		if (this.conditionalValidation(context, command, errors).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		// only change lavaFile if MultipartFile is specified, i.e. it is not required here since it has already
		// been uploaded and written to the repository by doSaveAdd
		// in other words, the existing mapping file can not be removed, only replaced by a new mapping file
		MultipartFile uploadFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
		if(uploadFile != null && uploadFile.getSize() != 0){
			// user has chosen to upload a mapping file to replace the current mappingFile so delete the current
			// file (deleteFile will archive the file in addition to deleting)
			// note: want same behavior even if user uploads same file as current file, because contents of file
			// have presumably changed which is why it is being re-uploaded
			
			// using callback method to use core file operations exception handling 
			// have to use getDeclaredMethod instead of getMethod for non-public methods
			Method callbackMethod = BaseEntityComponentHandler.class.getDeclaredMethod("saveFileCallback", new Class[]{RequestContext.class, Object.class, BindingResult.class});
			returnEvent = fileOperationHandler(callbackMethod, context, command, errors);	

			if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
				if (this.mappingFileValidation(context, command, errors).getId().equals(ERROR_FLOW_EVENT_ID)) {
					return new Event(this, ERROR_FLOW_EVENT_ID);
				}
			}
		}

		// allow save even if mapping file is not supplied as user may not have mapping file ready
		// but wants to save definition

		// do this after saving the LavaFile to the repository because that populates some LavaFile properties
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			return super.doSave(context, command, errors);
		}
		else {
			return returnEvent;
		}
	}

	/**
	 * Callback method to write the mappingFile to the import file repository.
	 */
	protected Event saveFileCallback(RequestContext context, Object command, BindingResult errors) throws Exception {
		ImportDefinition importDefinition = (ImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		ImportFile mappingFile = (ImportFile) getLavaFileBackingObject(context, ((ComponentCommand)command).getComponents(), errors);

		// editing a definition where user is changing the mapping file

				
		// editing a definition where user may either be adding a mapping file, or user may be changing the mapping file
		if (mappingFile != null && mappingFile.getFileId() != null) {
			mappingFile.deleteFile();
		}

		// getUploadFile will populate the mappingFile property due to getLavaBackingFileObject override
		mappingFile = (ImportFile) this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
		mappingFile.setDefinitionName(importDefinition.getName());
		
		// because existing file has already been deleted just call saveFile instead of saveOrUpdateFile
		// note: if did not delete file above, saveOrUpdateFile here would work if filename is the same, but
		// if new filename is different than current filename the current file would not be deleted by
		// saveOrUpdateFile
		mappingFile.saveFile();
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	/**
	 * Override to delete the mapping file if the definition is deleted.
	 */
	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception{
		ImportDefinition importDefinition = (ImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		Event returnEvent = super.doConfirmDelete(context, command, errors);
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			returnEvent = super.doDeleteFile(context, command, errors);
		}
		return returnEvent;
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
	private String[] trimTrailingCommas(String[] line) {
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
