package edu.ucsf.lava.crms.assessment.controller;

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
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.assessment.controller.InstrumentGroupHandler;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class VisitInstrumentsHandler extends CrmsListComponentHandler {

	public VisitInstrumentsHandler() {
		super();
		this.setHandledList("visitInstruments","instrument");
		CrmsSessionUtils.setIsVisitContext(this);
		this.setEntityForStandardSourceProvider(InstrumentTracking.class);
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = InstrumentTracking.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("instrType",true);
		// determine the visit to use for the list of instruments. 
		// in case this is the secondary component list on the Add Instrument page, first check 
		// the visit id field submitted on the form (the form is resubmitted every time the user 
		// changes the visit so that the list of instruments can be updated). however, on the initial
		// GET, the field will not be submitted, so then check if there is a currentVisit
		// in context
		Instrument instrument = ((Instrument)components.get("instrument"));
		Long visitId = null;
		if (instrument != null) {
			visitId = instrument.getVisit().getId();
		}
		if (visitId != null) {
			filter.setParam("visit.id",visitId);
		}
		else {
			Visit v = CrmsSessionUtils.getCurrentVisit(sessionManager, request);
			if (v != null) {
				filter.setParam("visit.id",v.getId());
			}
			else {
				filter.setParam("visit.id",new Long(0));
			}
		}
		filter.addParamHandler(new LavaEqualityParamHandler("visit.id"));
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		// when used as secondary component list on Add Instrument, could change Visit which issues the
		// refresh event, so need to update the visit id in the filter
		Instrument instrument = ((Instrument)components.get("instrument"));
		if (instrument != null) {
			Long visitId = instrument.getVisit().getId();
			if (visitId != null) {
				filter.setParam("visit.id", visitId);
			}
		}
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		// if this is the primary component, the componentView and componentMode will be set correctly in
		// BaseListEntityHandler, but if this list is used as a secondary component, it's prepareDefaultModelAndView
		// in BaseListEntityHandler will not be called, since the primary component's default will be used, so
		// it will not get a chance to set its component mode and view within prepareDefaultModelAndView, so
		// set them here to have them added to the reference data
		model.put("instrumentConfig", instrumentManager.getInstrumentConfig());
		
		// set the project name and visit type in flow scope which is then used by the secondary 
		// instrumentGroupHandler in addReferenceData to determine whether a project-visit instrument
		// group prototype exists and if so, add info to the model to flag the view, as the 
		// visitInstruments view will display instrument group user functionality if an instrument
		// group prototype exists
		// project name and visit type are also used by instrumentGroupHandler in creating the 
		// instrumentGroup from the prototype

		// instruments can be added when there is no current visit, where this handler is called
		// to supply a list of visits for the add instrument dropdown, so check for null 
		Visit v = CrmsSessionUtils.getCurrentVisit(sessionManager, request);
		if (v != null) {
			context.getFlowScope().put(InstrumentGroupHandler.PROTOTYPE_PROJ_NAME, v.getProjName());
			context.getFlowScope().put(InstrumentGroupHandler.PROTOTYPE_VISIT_TYPE, v.getVisitType());
		}
		
		return super.addReferenceData(context, command, errors, model);
	}
	
	
	public Event preEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		if(ActionUtils.getEventId(context).startsWith("instrumentGroup__")){
			// set the defaultObjectName in flow scope so the instrumentGroupHandler knows
			// which component to use to get the list of selected list items
			context.getFlowScope().put(InstrumentGroupHandler.SOURCE_COMPONENT, getDefaultObjectName());
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	public Event postEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		// make sure list of visit instruments is refreshed when doing "Add Instrument" via applyAdd,
		//  since the same page is shown and the new instrument should appear in the list
		String event = ActionUtils.getEventName(context);
		if(event.equals("applyAdd") || event.equals("refresh")){
			// when adding instruments, the user can select the visit, so update the visit in the list filter
			ScrollablePagedListHolder entityList = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get("visitInstruments");
			entityList.setFilter(extractFilterFromRequest(context, ((ComponentCommand)command).getComponents()));
			((BaseListSourceProvider)entityList.getSourceProvider()).setListHandler(this);
			entityList.doRefresh();
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
}
