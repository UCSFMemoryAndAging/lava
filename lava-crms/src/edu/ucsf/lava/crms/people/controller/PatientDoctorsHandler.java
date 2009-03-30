package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.PatientDoctor;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientDoctorsHandler extends CrmsListComponentHandler {

	public PatientDoctorsHandler() {
		super();
		this.setHandledList("patientDoctors","patientDoctor");
		CrmsSessionUtils.setIsPatientContext(this);
		this.setEntityForStandardSourceProvider(PatientDoctor.class);
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterPatientContext(sessionManager,request, PatientDoctor.newFilterInstance(getCurrentUser(request)));
		filter.setAlias("doctor", "doctor");
		filter.setAlias("patient","patient");
		filter.addDefaultSort("doctor.fullNameRev", true);
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
	

}
