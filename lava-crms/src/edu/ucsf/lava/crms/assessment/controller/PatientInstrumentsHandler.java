package edu.ucsf.lava.crms.assessment.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.crms.assessment.model.InstrumentConfig;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class PatientInstrumentsHandler extends CrmsListComponentHandler {

	public PatientInstrumentsHandler() {
		super();
		this.setHandledList("patientInstruments","instrument");
		CrmsSessionUtils.setIsPatientContext(this);
		this.setEntityForStandardSourceProvider(InstrumentTracking.class);
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = InstrumentTracking.newFilterInstance(getCurrentUser(request));
		CrmsSessionUtils.setFilterPatientContext(sessionManager,request,filter);

		// quick filter settings
		filter.setActiveQuickFilter("Scheduled / Complete / Incomplete");
		filter.addQuickFilter("Scheduled / Complete / Incomplete", filter.daoNot(filter.daoLikeParam("dcStatus","%Canceled%")));
		filter.addQuickFilter("Scheduled",  filter.daoEqualityParam("dcStatus","Scheduled"));
		filter.addQuickFilter("Complete",  filter.daoLikeParam("dcStatus","Complete%"));
		filter.addQuickFilter("Incomplete",  filter.daoOr(filter.daoLikeParam("dcStatus","Incomplete%"), filter.daoEqualityParam("dcStatus", "Unknown")));
		filter.addQuickFilter("Canceled",  filter.daoLikeParam("dcStatus","%Canceled%"));
		filter.addQuickFilter("All Instruments",  null);

		filter.addParamHandler(new LavaDateRangeParamHandler("dcDate"));
		filter.addParamHandler(new LavaDateRangeParamHandler("deDate"));
		filter.addDefaultSort("dcDate",true);
		filter.addDefaultSort("instrType",true);
		filter.setAlias("visit", "visit");
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
		
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		super.addReferenceData(context, command, errors, model);
		model.put("instrumentConfig", instrumentManager.getInstrumentConfig());
		return model;
	}
	
	
	/**
	 * Exclude hidden instruments.
	 */
	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter) {
		Map<String,InstrumentConfig> instrConfigMap = instrumentManager.getInstrumentConfig();
		List<String> dxInstrTypes = null;
		for (InstrumentConfig instrConfig : instrConfigMap.values()) {
			if (instrConfig.getHidden()) {
				daoFilter.addDaoParam(daoFilter.daoNot(daoFilter.daoEqualityParam("instrType", instrConfig.getInstrType())));
			}
		}
		return daoFilter;
	}
	
}
