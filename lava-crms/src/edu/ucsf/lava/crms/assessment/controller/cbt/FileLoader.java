package edu.ucsf.lava.crms.assessment.controller.cbt;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public interface FileLoader {
	public void setDetailComponents(RequestContext context, Map backingObjects); 
	public Event loadData(RequestContext context, Object command, BindingResult errors) throws Exception; 
}
