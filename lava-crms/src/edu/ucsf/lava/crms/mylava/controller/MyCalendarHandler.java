package edu.ucsf.lava.crms.mylava.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsCalendarComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;



public class MyCalendarHandler extends CrmsCalendarComponentHandler {
	
	public MyCalendarHandler() {
		super();
		this.setHandledList("myVisits","visit");
		this.setDatePropertyName("visitDate");
		this.setDefaultDisplayRange(CalendarDaoUtils.DISPLAY_RANGE_WEEK);
		this.setEntityForStandardSourceProvider(Visit.class);
	}
	
	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		filter.setParam("visitWith", CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request).getShortUserNameRev());
		filter.setAlias("patient","patient");
		filter.addDefaultSort("visitDate",false);
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
		
	
	
	
	
	
}
