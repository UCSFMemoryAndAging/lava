package edu.ucsf.lava.crms.assessment.controller.upload;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public interface FileLoader {
	public Event loadFile(RequestContext context, Object command, BindingResult errors) throws Exception; 
}
