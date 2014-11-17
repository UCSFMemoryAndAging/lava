package edu.ucsf.lava.core.importer.controller;

import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_DEF_MAPPING_FILE_TYPE;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.IMPORT_REPOSITORY_ID;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

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
		this.setRequiredFields(new String[]{"name", "dataFileFormat"});
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
	
	
	// this is called in getUploadFile which uploads the specified file and populates the mappingFile 
	// LavaFile properties and is called in getDownloadFileContent to download the file 
	protected LavaFile getLavaFileBackingObject(RequestContext context, Map components, BindingResult errors) throws Exception{
		ImportDefinition definition = (ImportDefinition) components.get(getDefaultObjectName());
		if(ImportFile.class.isAssignableFrom(definition.getMappingFile().getClass())){
			return (ImportFile)definition.getMappingFile();
		}
		return null;
	}
	
	
	protected Event doSaveAdd(RequestContext context, Object command,BindingResult errors) throws Exception {
		Event returnEvent;
		MultipartFile uploadFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
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
				return super.doSaveAdd(context, command, errors);
			}
			else {
				return returnEvent;
			}
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "importDefinition.noMappingFile", null);
			return new Event(this,ERROR_FLOW_EVENT_ID);
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
		
		// only change lavaFile if MultipartFile is specified, i.e. it is not required here since it has already
		// been uploaded and written to the repository by handleSaveAdd
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
		}

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

				
		mappingFile.deleteFile();

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
	
	
}
