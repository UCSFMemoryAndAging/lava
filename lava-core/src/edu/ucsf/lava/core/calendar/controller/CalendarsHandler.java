package edu.ucsf.lava.core.calendar.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.calendar.model.Calendar;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;

public class CalendarsHandler extends BaseListComponentHandler {

	public CalendarsHandler() {
		super();
		this.setHandledList("calendars","calendar");
		this.setEntityForStandardSourceProvider(Calendar.class);
	}
	
	
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  Calendar.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("name",true);
		return filter;
	}


	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
}


