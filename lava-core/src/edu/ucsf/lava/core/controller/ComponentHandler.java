package edu.ucsf.lava.core.controller;

import java.util.Map;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public interface ComponentHandler {
    public static final String SUCCESS_FLOW_EVENT_ID = "success";
	public static final String UNAUTHORIZED_FLOW_EVENT_ID = "unauthorized";
	public static final String ERROR_FLOW_EVENT_ID = "error";
    public static final String CONTINUE_FLOW_EVENT_ID = "continue";
	public static final String UNHANDLED_FLOW_EVENT_ID = "unhandled";

	public Event authorizationCheck(RequestContext context, Object command) throws Exception;
    public boolean isAuthEvent(RequestContext context);
	public Event initMostRecentViewState(RequestContext context) throws Exception;
	public Event preSetupFlowDirector(RequestContext context) throws Exception;
	public Event postSetupFlowDirector(RequestContext context, Object command) throws Exception;
	public Map getBackingObjects(RequestContext context, Map components);
	public void subFlowReturnHook(RequestContext context, Object command, BindingResult errors) throws Exception;
	public void registerPropertyEditors(PropertyEditorRegistry registry);
	public void initBinder(RequestContext context, Object command, DataBinder binder);
	public void prepareToRender(RequestContext context, Object command, BindingResult errors) throws Exception;
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model);
	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception;
	public Event postEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception;
	public Event preEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception;
	public boolean validateRequiredFieldsEvent(RequestContext context);
	public boolean handlesObject(String objectName);
	public boolean handlesEvent(RequestContext context);
	public boolean handlesDownload(RequestContext context);
	public Event handleDownload(RequestContext context, Object command, BindingResult errors) throws Exception;
	public void setContextFromScope(RequestContext context);
	public Map getHandledObjects();
	public String getDefaultObjectName();
	public String translatePropValueToText(String propName, Object propValue);
	public String translatePropValueToText(String propName, Object propValue, Map<String,Map<String,String>> dynamicLists);
	public String getPropLabel(String property);
	public String getSectionName(String sectionKey);
	public String getSectionInstructions(String sectionKey);
	public String getSectionInstructions(String sectionKey, Object[] args);
	public String getMessage(String key);	
	public String getMessage(String key, Object[] args);	
	public String getMessage(String key, Object[] args, Boolean reportPdfStyling);	
	public String getBindingComponentString(); 
}
