package edu.ucsf.lava.core.calendar.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeOverlapParamHandler;
import edu.ucsf.lava.core.dao.LavaIgnoreParamHandler;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaCustomDateEditor;

public class AppointmentsHandler extends BaseCalendarComponentHandler {

	static protected String CALENDAR_COMPONENT_NAME="calendar";
	
	public AppointmentsHandler() {
		super();
		this.setOldStyleCalendar(false);
		this.setPageSize(99999); // set to a high number to prevent paging from occurring in the absence of paging controls
		this.setHandledList("appointments","appointment");
		this.setDatePropertyName("overlapDateRange");
		this.setEntityForStandardSourceProvider(Appointment.class);
	}
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		Map backingObjects  = super.getBackingObjects(context, components);
		
		if(request.getParameterMap().containsKey("id")){
			String calendarId = request.getParameter("id");
			if(StringUtils.isNumeric(calendarId)){
				Calendar rc = (Calendar)Calendar.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(calendarId)));
				if(rc!=null){
					backingObjects.put("calendar",rc);
				}
			}
		}
		return backingObjects;
	}



	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
	
			if(components.containsKey(CALENDAR_COMPONENT_NAME)){
				Calendar rc = (Calendar)components.get(CALENDAR_COMPONENT_NAME);
				if(rc!=null){
					filter.setParam("calendar.id", rc.getId());
				}
				
			}
		filter.setAlias("calendar","calendar");
		filter.addDefaultSort("startDate",true);
		filter.addDefaultSort("startTime",true);
		return filter;
	}
	
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	

	
	
}
