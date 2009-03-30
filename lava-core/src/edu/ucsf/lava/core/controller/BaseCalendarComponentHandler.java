package edu.ucsf.lava.core.controller;

import static edu.ucsf.lava.core.controller.CalendarHandlerUtils.CUSTOM_DATE_FILTER_END_PARAM;
import static edu.ucsf.lava.core.controller.CalendarHandlerUtils.CUSTOM_DATE_FILTER_START_PARAM;
import static edu.ucsf.lava.core.controller.CalendarHandlerUtils.DISPLAY_RANGE_MONTH;
import static edu.ucsf.lava.core.controller.CalendarHandlerUtils.DISPLAY_RANGE_PARAM;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.core.dao.LavaIgnoreParamHandler;
import edu.ucsf.lava.core.model.EntityBase;

abstract public class BaseCalendarComponentHandler extends BaseListComponentHandler {
	protected String defaultDisplayRange=DISPLAY_RANGE_MONTH;
	protected String datePropertyName;
	protected String startDateParam;
	protected String endDateParam;
	
	public BaseCalendarComponentHandler() {
		super();
		defaultEvents.addAll(CalendarHandlerUtils.getDefaultEvents());
	}

	public String getDatePropertyName() {
		return datePropertyName; 
	}

	public void setDatePropertyName(String datePropertyName) {
		this.datePropertyName = datePropertyName;
		this.startDateParam = datePropertyName.concat("Start"); 
		this.endDateParam = datePropertyName.concat("End");    
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
	
		filter.addParamHandler(new LavaDateRangeParamHandler(datePropertyName));
		filter.addParamHandler(new LavaIgnoreParamHandler(DISPLAY_RANGE_PARAM));
		filter.addParamHandler(new LavaIgnoreParamHandler(CUSTOM_DATE_FILTER_START_PARAM));
		filter.addParamHandler(new LavaIgnoreParamHandler(CUSTOM_DATE_FILTER_END_PARAM));
		filter = prepareFilter(context,filter);
		return this.setDateFilterParams(context,filter);
	}
	
	//override in subclass to do additional Filter initialization
	// e.g. adding custom param handlers or default sorts. 
	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter){
		return filter;
	}
	
	protected boolean missingDateFilterParams(LavaDaoFilter filter){
		return CalendarHandlerUtils.missingDateFilterParams(filter, this.startDateParam, this.endDateParam);
	}
	
	protected void handleCustomDateFilter(LavaDaoFilter filter){
		CalendarHandlerUtils.handleCustomDateFilter(filter, this.startDateParam, this.endDateParam);
	}
	
	protected void setDefaultFilterParams(LavaDaoFilter filter){
		CalendarHandlerUtils.setDefaultFilterParams(filter, this.defaultDisplayRange, this.startDateParam, this.endDateParam);
	}
	
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		//	handle filter events
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		setDateFilterParams(context,(LavaDaoFilter)plh.getFilter());
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.refresh();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}
	
	public LavaDaoFilter setDateFilterParams(RequestContext context, LavaDaoFilter filter)
	{
		return CalendarHandlerUtils.setDateFilterParams(context, filter, this.defaultDisplayRange, this.startDateParam, this.endDateParam);
	}
			
	
	public String getDefaultDisplayRange() {
		return defaultDisplayRange;
	}

	public void setDefaultDisplayRange(String defaultDisplayRange) {
		this.defaultDisplayRange = defaultDisplayRange;
	}

	
	
	

	public LavaDaoFilter onPreFilterParamConversion(LavaDaoFilter daoFilter) {
		handleCustomDateFilter(daoFilter); //only really need to do this on loadList not LoadELements...not sure if this will be a problem..
		if(missingDateFilterParams(daoFilter)){
			setDefaultFilterParams(daoFilter);
		}
		return daoFilter;
	}

	@Override
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components) {
		// TODO Auto-generated method stub
		
	}



}
