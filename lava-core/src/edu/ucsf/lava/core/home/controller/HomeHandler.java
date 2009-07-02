package edu.ucsf.lava.core.home.controller;

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


public class HomeHandler extends LavaComponentHandler {


	public HomeHandler() {
		super();
		// must handle the refresh event even though there is nothing to refresh, to accomodate
		// context changes which stay within the flow and do a refresh after the context has changed
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"refresh"}));		
		this.setDefaultObjectName("home");
		Map handledObjects = new HashMap<String,Class>();
		handledObjects.put("home", EntityPlaceholder.class);
		this.setHandledObjects(handledObjects);
	}

	public Map getBackingObjects(RequestContext context, Map components) {
		// TODO Auto-generated method stub
		Map model = new HashMap();
		model.put("home", new EntityPlaceholder());
		return model;
	}

	public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
	}	

	public void initBinder(RequestContext context, Object command, DataBinder binder) {
	}
	
	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

}
