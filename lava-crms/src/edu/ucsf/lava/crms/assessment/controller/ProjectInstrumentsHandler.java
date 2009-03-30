package edu.ucsf.lava.crms.assessment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.controller.CrmsCalendarComponentHandler;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectInstrumentsHandler extends CrmsCalendarComponentHandler {

	public ProjectInstrumentsHandler() {
		super();
		this.setHandledList("projectInstruments","instrument");
		this.setDatePropertyName("dcDate");
		this.setEntityForStandardSourceProvider(InstrumentTracking.class);
	}

	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		filter.addDefaultSort("dcDate",true);
		filter.addDefaultSort("patient.fullNameRevNoSuffix", true);
		filter.addParamHandler(new LavaDateRangeParamHandler("dcDate"));
		filter.addParamHandler(new LavaDateRangeParamHandler("deDate"));
		filter.addDefaultSort("instrType",true);
		filter.setAlias("visit", "visit");
		filter.setAlias("patient", "patient");
		return CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		model.put("instrumentConfig", instrumentManager.getInstrumentConfig());
		return super.addReferenceData(context, command, errors, model);
	}
	
	
}
