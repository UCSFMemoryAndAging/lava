package edu.ucsf.lava.crms.scheduling.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsCalendarComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;


public class ResearchCalendarHandler extends CrmsCalendarComponentHandler {

	public ResearchCalendarHandler() {
		super();
		this.setHandledList("researchVisits","visits");
		this.setDatePropertyName("visitDate");
		this.setEntityForStandardSourceProvider(Visit.class);
	}

	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		return filter; 
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}

	
 
}
