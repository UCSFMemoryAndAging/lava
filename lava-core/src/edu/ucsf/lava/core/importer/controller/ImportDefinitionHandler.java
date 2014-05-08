package edu.ucsf.lava.core.importer.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.file.exception.AlreadyExistsFileAccessException;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;
import static edu.ucsf.lava.core.file.ImportRepositoryStrategy.*;


public class ImportDefinitionHandler extends BaseEntityComponentHandler {
	
	public ImportDefinitionHandler() {
		super();
		setHandledEntity("importDefinition", ImportDefinition.class);
		this.setRequiredFields(new String[]{"name"});
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ImportDefinition definition = (ImportDefinition) command;
		LavaFile mappingFile = new LavaFile();
		mappingFile.setContentType(IMPORT_DEF_MAPPING_FILE_TYPE);
		mappingFile.setRepositoryId(IMPORT_REPOSITORY_ID);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		mappingFile.setFileStatusBy(user.getShortUserNameRev());
		mappingFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));
		definition.setMappingFile(mappingFile);
		return definition;
	} 
	
	
	// this is called in getUploadFile which uploads the specified file and populates the mappingFile LavaFile properties
	protected LavaFile getLavaFileBackingObject(RequestContext context, Map components, BindingResult errors) throws Exception{
		ImportDefinition definition = (ImportDefinition) components.get(getDefaultObjectName());
		if(LavaFile.class.isAssignableFrom(definition.getMappingFile().getClass())){
			return (LavaFile)definition.getMappingFile();
		}
		return null;
	}
	

	public Event handleSaveAddEvent(RequestContext context, Object command,BindingResult errors) throws Exception {
		MultipartFile uploadFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
		if(uploadFile != null && uploadFile.getSize() != 0){
			// getUploadFile will populate the mappingFile property due to getLavaBackingFileObject override
			LavaFile mappingFile = this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
			// doSaveAdd will save the importDefinition and mappingFile LavaFile properties. however, because the 
			// command object is not a LavaFile, but rather an importDefinition that has a LavaFile property, 
			// the afterCreate and afterUpdate triggers are not called on the LavaFile property so the file is 
			// not written to the repository. so that must be done explicitly, and should be done before doSaveAdd
			// because it populates the fileId and location properties of LavaFile
			try {
				mappingFile.saveFile();
			} catch (AlreadyExistsFileAccessException e){
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.fileExistsException"}, null, ""));
				return new Event(this,ERROR_FLOW_EVENT_ID);	
			} catch (FileAccessException e){
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.genericException"}, null, ""));
				return new Event(this,ERROR_FLOW_EVENT_ID);			
//				} catch (IOException e){
//					errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.noFileException"}, null, ""));
//					return new Event(this,ERROR_FLOW_EVENT_ID);	
			} catch (Exception e){
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.genericException"}, null, ""));
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}

			return this.doSaveAdd(context, command, errors);
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "importDefinition.noMappingFile", null);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
	}
	
	
	public Event handleSaveEvent(RequestContext context, Object command,BindingResult errors) throws Exception {
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		LavaFile mappingFile = getLavaFileBackingObject(context, ((ComponentCommand)command).getComponents(), errors);
		
		// only change lavaFile if MultipartFile is specified, i.e. it is not required here since it has already
		// been uploaded and written to the repository by handleSaveAdd
		// in other words, the existing mapping file can not be removed, only replaced by a new mapping file
//see about combingin logic with handleSaveAdd. diff is that mappingFile will already exist and needs to
//be deleted		
		MultipartFile uploadFile = context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile");
		if(uploadFile != null && uploadFile.getSize() != 0){
			// getUploadFile will populate the mappingFile property due to getLavaBackingFileObject override
			mappingFile.deleteFile();
			mappingFile = this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
//TODO: write helper at level of BaseEntityComponentHandler to do this try / catch error checking, used here, above
// and by doUploadFile
//hopefully that will resolve IOException weirdness below		
			try {
				mappingFile.saveFile();
			}
			catch (AlreadyExistsFileAccessException e){
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.fileExistsException"}, null, ""));
				return new Event(this,ERROR_FLOW_EVENT_ID);	
			} catch (FileAccessException e){
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.genericException"}, null, ""));
				return new Event(this,ERROR_FLOW_EVENT_ID);			
//			} catch (IOException e){
//				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.noFileException"}, null, ""));
//				return new Event(this,ERROR_FLOW_EVENT_ID);	
			} catch (Exception e){
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.genericException"}, null, ""));
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
		}

		// do this after saving the LavaFile to the repository because that populates some LavaFile properties
		return this.doSave(context, command, errors);
	}
	
	
}
