package edu.ucsf.lava.crms.scheduling.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class PatientVisitsHandler extends CrmsListComponentHandler {

	public PatientVisitsHandler() {
		super();
		this.setHandledList("patientVisits","visits");
		this.setEntityForStandardSourceProvider(Visit.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  CrmsSessionUtils.setFilterPatientContext(sessionManager,request,Visit.newFilterInstance(getCurrentUser(request)));
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("visitDate", false);
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
	
	public Event postEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		// make sure list of patient visits is refreshed when doing "Add Visit" via applyAdd
		String event = ActionUtils.getEventName(context);
		if(event.equals("applyAdd")){
			ScrollablePagedListHolder entityList = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get("patientVisits");
			((BaseListSourceProvider)entityList.getSourceProvider()).setListHandler(this);
			entityList.doRefresh();
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	    		
   
    
}
