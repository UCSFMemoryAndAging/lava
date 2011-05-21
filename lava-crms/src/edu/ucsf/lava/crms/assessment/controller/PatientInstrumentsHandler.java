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
	 * Exclude hidden and diagnosis instruments.
	 */
	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter) {
		Map<String,InstrumentConfig> instrConfigMap = instrumentManager.getInstrumentConfig();
		List<String> dxInstrTypes = null;
		for (InstrumentConfig instrConfig : instrConfigMap.values()) {
			if (instrConfig.getHidden()) {
				daoFilter.addDaoParam(daoFilter.daoNot(daoFilter.daoEqualityParam("instrType", instrConfig.getInstrType())));
			}
			if (instrConfig.getDiagnosis()) {
				if (dxInstrTypes == null) {
					dxInstrTypes = new ArrayList<String>();
				}
				dxInstrTypes.add(instrConfig.getInstrType());
			}
		}
		if (dxInstrTypes != null) {
			daoFilter.addDaoParam(daoFilter.daoNot(daoFilter.daoInParam("instrType", dxInstrTypes.toArray())));
		}
		return daoFilter;
	}
	
}
