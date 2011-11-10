package edu.ucsf.lava.core.file.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.file.model.LavaFile;

/**
 * Common controller functionality for entites derived from LavaFile. 
 * 
 * @author jhesse
 *
 */
public class BaseLavaFileComponentHandler extends BaseEntityComponentHandler {

	public BaseLavaFileComponentHandler() {
		super();
		this.setHandledEntity("lavaFile", LavaFile.class);
		this.setRequiredFields(new String[]{"contentType"});
		this.setSupportsAttachedFiles(true);
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


	/**
	 * Handle the uploaded file. 
	 * 
	 */
	@Override
	public Event handleSaveAddEvent(RequestContext context, Object command,BindingResult errors) throws Exception {
		if(this.uploadFileParameterExists(context, command, errors)){
			return super.handleUploadFileEvent(context, command, errors);
		}else{
			return super.handleSaveAddEvent(context, command, errors);
		}
	}
	@Override
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		LavaFile lavaFile = (LavaFile) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		dynamicLists.put("lavaFile.statusBy", listManager.getDynamicList(getCurrentUser(request),"auth.users"));
		if(lavaFile.getContentType()!=null){
			dynamicLists.put("lavaFile.status", listManager.getDynamicList(getCurrentUser(request),
				"lavaFile.status","contentType",lavaFile.getContentType(),String.class));
		}else{
			dynamicLists.put("lavaFile.status", listManager.getDynamicList(getCurrentUser(request),
					"lavaFile.status","contentType","GENERIC",String.class));
				
		}
			dynamicLists.put("lavaFile.contentType", listManager.getDynamicList(getCurrentUser(request),
				"lavaFile.contentType"));
				
		return super.addReferenceData(context, command, errors, model);
	}

	
}
