package edu.ucsf.lava.core.file.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.file.model.LavaFile;

public class AttachmentsListHandler extends	BaseListComponentHandler {
	
	public AttachmentsListHandler(){
		this.setHandledList("attachments", "attachment");
		this.setEntityForStandardSourceProvider(LavaFile.class);
	}
	

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = LavaFile.newFilterInstance(getCurrentUser(request));
		return filter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
		
}
