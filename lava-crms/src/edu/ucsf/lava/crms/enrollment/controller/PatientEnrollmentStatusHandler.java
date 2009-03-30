package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientEnrollmentStatusHandler extends CrmsListComponentHandler {

	public PatientEnrollmentStatusHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledList("patientEnrollmentStatus","enrollmentStatus");
		this.setEntityForStandardSourceProvider(EnrollmentStatus.class);
	}


	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EnrollmentStatus.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("projName",true);
		return CrmsSessionUtils.setFilterPatientContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}



}
