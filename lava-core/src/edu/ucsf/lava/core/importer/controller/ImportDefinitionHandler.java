package edu.ucsf.lava.core.importer.controller;

import java.io.IOException;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.file.exception.AlreadyExistsFileAccessException;
import edu.ucsf.lava.core.file.exception.FileAccessException;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.model.ImportTemplate;


public class ImportTemplateHandler extends BaseEntityComponentHandler {
	
	public ImportTemplateHandler() {
		super();
		setHandledEntity("importTemplate", ImportTemplate.class);
//TODO: look into adding a property to bind the MultiPart file to and see if can somehow make that
//a required field
		this.setRequiredFields(new String[]{"name"});
	}

	
	public Event handleSaveAddEvent(RequestContext context, Object command,BindingResult errors) throws Exception {
		if(this.uploadFileParameterExists(context, command, errors)){
			//TODO: error msg
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}

// see BaseLavaFileComponentHandler for what to do here
// in particular see how UI sets the contentStatus and contentType from dropdown lists. not sure
//  what these are or if they are important and what they would be here
// would be useful to use attachments in the demo app (w jhesse default single repo setup) to see this,
//  or probably the same in oldlava 
		LavaFile lavaFile = this.getUploadFile(context, ((ComponentCommand)command).getComponents(), errors);
	
// here we will be saving the importTemplate including its LavaFile component, not just a LavaFile, but still want all
// of this exception handling since saving the file
// assume that saving the file determins the files repo, which generates its fileId (createFileId)
	
// also need to do this on handleSaveEvent since user may upload a new file - so need to check to see if 
// it is a different file, or perhaps just upload it every time since it may be the same filename with edits	
	
		if(lavaFile==null){
			//TODO: error msg
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		try {
			this.doSave(context, command, errors);
		} catch (AlreadyExistsFileAccessException e){
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.fileExistsException"}, null, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);	
		} catch (FileAccessException e){
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.genericException"}, null, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);			
		} catch (IOException e){
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.noFileException"}, null, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);	
		} catch (Exception e){
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.uploadFile.genericException"}, null, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}

		
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	/**
	 * Utility method to see if a file has been uploaded
	 * @param context
	 * @param command
	 * @param errors
	 * @return
	 */
	protected boolean uploadFileParameterExists(RequestContext context, Object command, BindingResult errors){
		return (null != context.getRequestParameters().getMultipartFile(getDefaultObjectName() + "_uploadFile"));
	}

}
