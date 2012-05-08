package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientSchedulingStatusHandler extends BaseSchedulingStatusHandler {

	public PatientSchedulingStatusHandler() {
		super();
		this.setHandledList("patientSchedulingStatus","protocolStatusList");
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		LavaDaoFilter filter = super.extractFilterFromRequest(context, components);
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		return CrmsSessionUtils.setFilterPatientContext(sessionManager,request,filter);
	}
	
}
