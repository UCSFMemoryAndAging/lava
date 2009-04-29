package edu.ucsf.lava.crms.scheduling.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.CalendarHandlerUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsCalendarComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class PatientCalendarHandler extends CrmsCalendarComponentHandler{

	public PatientCalendarHandler() {
		super();
		this.setHandledList("patientVisits","visits");
		this.setDatePropertyName("visitDate");
		this.setDefaultDisplayRange(CalendarHandlerUtils.DISPLAY_RANGE_ALL);
		this.setEntityForStandardSourceProvider(Visit.class);
	}


	
	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//TODO: undo this kludge ---keeps it from exploding when no patient in context
		if (CrmsSessionUtils.getCurrentPatient(sessionManager,request)==null){
			CrmsSessionUtils.setCurrentPatient(sessionManager,request,new Long(0));
		}
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("visitDate", false);
		filter.addDefaultSort("visitTime", false);
		return CrmsSessionUtils.setFilterPatientContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}

	}
