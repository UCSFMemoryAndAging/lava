package edu.ucsf.lava.core.calendar.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.calendar.model.ResourceCalendar;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;

public class ResourceCalendarsHandler extends BaseListComponentHandler {

	public ResourceCalendarsHandler() {
		super();
		this.setHandledList("resourceCalendars","resourceCalendar");
		this.addListedEntityName("calendar");
		this.setEntityForStandardSourceProvider(ResourceCalendar.class);
	}
	
	
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  ResourceCalendar.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("name",true);
		return filter;
	}


	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
}


