package edu.ucsf.lava.crms.mylava.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.LavaComponentHandler;
import edu.ucsf.lava.core.model.EntityPlaceholder;


public class FormsDownloadHandler extends LavaComponentHandler {
  public FormsDownloadHandler() {
    super();
    defaultEvents = new ArrayList(Arrays.asList(new String[]{"refresh"}));    
    this.setDefaultObjectName("formsdownload");
    Map handledObjects = new HashMap<String,Class>();
    handledObjects.put("formsdownload", EntityPlaceholder.class);
    this.setHandledObjects(handledObjects);
  }

  public Map getBackingObjects(RequestContext context, Map components) {
    Map model = new HashMap();
    model.put("formsdownload", new EntityPlaceholder());
    return model;
  }

  public void prepareToRender(RequestContext context, Object command, BindingResult errors) { }  
  public void initBinder(RequestContext context, Object command, DataBinder binder) { }
  
  public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
    return new Event(this,SUCCESS_FLOW_EVENT_ID);
  }

}
