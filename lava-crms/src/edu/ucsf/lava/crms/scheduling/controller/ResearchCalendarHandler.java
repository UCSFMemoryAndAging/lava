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
		this.setHandledList("researchVisits","visit");
		this.setDatePropertyName("visitDate");
		this.setEntityForStandardSourceProvider(Visit.class);
	}

	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter, Map components) {
		// quick filter settings
		filter.setActiveQuickFilter("Scheduled / Complete Only");
		filter.addQuickFilter("Scheduled / Complete Only", filter.daoNot(
															filter.daoOr(
																	filter.daoLikeParam("visitStatus","%Canceled%"),
																	filter.daoLikeParam("visitStatus", "%No Show%"))));
		
		filter.addQuickFilter("Canceled / No Show Only",  filter.daoOr(
															filter.daoLikeParam("visitStatus","%Canceled%"),
															filter.daoLikeParam("visitStatus", "%No Show%")));
		filter.addQuickFilter("All Visits",  null);
		
		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		return filter; 
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}

	
 
}
