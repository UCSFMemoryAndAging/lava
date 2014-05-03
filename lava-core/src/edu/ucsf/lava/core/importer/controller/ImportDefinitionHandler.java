package edu.ucsf.lava.core.importer.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.file.exception.AlreadyExistsFileAccessException;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class ImportDefinitionHandler extends BaseEntityComponentHandler {
	
	public ImportDefinitionHandler() {
		super();
		setHandledEntity("importDefinition", ImportDefinition.class);
		this.setRequiredFields(new String[]{"name"});
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		ImportDefinition definition = (ImportDefinition) command;
		definition.setMappingFile(new LavaFile());
//TODO: initialize file status or set them later when selected in handleSaveAddEvent
// e.g.
//		crmsFile.setFileStatusBy(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request).getShortUserNameRev());
//		crmsFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));

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
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		ImportDefinition definition = (ImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		
		if(null != context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile")){
			// getUploadFile will populate the mappingFile property due to getLavaBackingFileObject override
			LavaFile mappingFile = this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
			returnEvent = this.doSaveAdd(context, command, errors);
			if (returnEvent.equals(SUCCESS_FLOW_EVENT_ID)) {
				// doSave will save the importDefinition and mappingFile LavaFile properties. however, because the 
				// command object is not a LavaFile, but rather an importDefinition that has a LavaFile property, 
				// the afterCreate and afterUpdate triggers are not called on the LavaFile property so the file is 
				// not written to the repository. so that must be done explicitly
				try {
					mappingFile.afterCreate();
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
			}				

			return returnEvent;
		}
		else {
			LavaComponentFormAction.createCommandError(errors, "importDefinition.noMappingFile", null);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
	}
	
	
	public Event handleSaveEvent(RequestContext context, Object command,BindingResult errors) throws Exception {
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		LavaFile mappingFile = null;
		
		// only change lavaFile if MultipartFile is specified, i.e. it is not required here since it has already
		// been uploaded and written to the repository by handleSaveAdd
		if(null != context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile")){
			// getUploadFile will populate the mappingFile property due to getLavaBackingFileObject override
			mappingFile = this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
		}

		returnEvent = this.doSave(context, command, errors);

		if(null != context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile")){
//TODO: write helper at level of BaseEntityComponentHandler to do this try / catch error checking, used here, above
// and by doUploadFile
//hopefully that will resolve IOException weirdness below		
			try {
				mappingFile.afterUpdate();
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
		
		// trigger the writing of the file to the repository (update will first delete the existing file if one exists)
		return returnEvent;
	}
	
	
}
