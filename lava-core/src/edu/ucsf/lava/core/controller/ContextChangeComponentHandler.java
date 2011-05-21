package edu.ucsf.lava.core.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;

public abstract class ContextChangeComponentHandler extends LavaComponentHandler {

public static final String CONTEXT_CHANGE_RESULT_KEY = "contextChangeResult";
	
	public ContextChangeComponentHandler(){
		super();
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"contextChange","contextClear"}));
		}
		
	protected abstract void doContextChange(RequestContext context, Object command, BindingResult errors) throws Exception;	
	protected abstract void doContextClear(RequestContext context, Object command, BindingResult errors) throws Exception;	
	

	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		String event = ActionUtils.getEventName(context);
		if (event.equals("contextClear")){
			doContextClear(context,command,errors);
		}
		else if (event.equals("contextChange")){
			doContextChange(context,command,errors);
		}	
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	protected boolean isDoDefaultActionFlag(RequestContext context){
		String result = (String) context.getRequestScope().get(CONTEXT_CHANGE_RESULT_KEY);
		return (result == null ? false : (result.equals("defaultAction") ? true : false));
	}
	
	protected void setDoDefaultActionFlag(RequestContext context){
		this.setContextChangeResult(context, "defaultAction");
	}
	
	protected void clearDoDefaultActionFlag(RequestContext context){
		this.setContextChangeResult(context, "");
	}
	protected void setContextChangeResult(RequestContext context,String result){
		context.getRequestScope().put(CONTEXT_CHANGE_RESULT_KEY,result);
	}
	
	
	public void initBinder(RequestContext context, Object command, DataBinder binder) {
	}

	public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		return;
	}
	
	

}
