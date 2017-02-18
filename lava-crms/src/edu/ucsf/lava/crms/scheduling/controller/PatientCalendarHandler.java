package edu.ucsf.lava.crms.scheduling.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsCalendarComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class PatientCalendarHandler extends CrmsCalendarComponentHandler{

	public PatientCalendarHandler() {
		super();
		this.setHandledList("patientVisits","visit");
		this.setDatePropertyName("visitDate");
		this.setDefaultDisplayRange(CalendarDaoUtils.DISPLAY_RANGE_ALL);
		this.setEntityForStandardSourceProvider(Visit.class);
	}


	
	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//TODO: undo this kludge ---keeps it from exploding when no patient in context
		if (CrmsSessionUtils.getCurrentPatient(sessionManager,request)==null){
			CrmsSessionUtils.setCurrentPatient(sessionManager,request,new Long(0));
		}
		
		// quick filter settings
		filter.setActiveQuickFilter("Scheduled / Complete Only");
		filter.addQuickFilter("Scheduled / Complete Only", filter.daoNot(
															filter.daoOr(
																	filter.daoLikeParam("visitStatus","%CANCELED%"),
																	filter.daoLikeParam("visitStatus", "%NO SHOW%"))));
		
		filter.addQuickFilter("Canceled / No Show Only",  filter.daoOr(
															filter.daoLikeParam("visitStatus","%CANCELED%"),
															filter.daoLikeParam("visitStatus", "%NO SHOW%")));
		filter.addQuickFilter("All Visits",  null);
		
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("visitDate", false);
		filter.addDefaultSort("visitTime", false);
		return CrmsSessionUtils.setFilterPatientContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}

	}
