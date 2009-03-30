package edu.ucsf.lava.crms.scheduling.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsCalendarComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class ProjectCalendarHandler extends CrmsCalendarComponentHandler {
	
	
	public ProjectCalendarHandler() {
		super();
		this.setHandledList("projectVisits","visits");
		this.setDatePropertyName("visitDate");
		this.setEntityForStandardSourceProvider(Visit.class);
	}

	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("visitDate", false);
		return CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}

	
	
}
