package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.Doctor;
import edu.ucsf.lava.crms.people.model.PatientDoctor;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class DoctorPatientsHandler extends CrmsListComponentHandler {

	public DoctorPatientsHandler() {
		super();
		this.setHandledList("doctorPatients","patientDoctor");
	
		this.setEntityForStandardSourceProvider(PatientDoctor.class);
	}
	


	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  CrmsSessionUtils.setFilterProjectContext(sessionManager,request,PatientDoctor.newFilterInstance(getCurrentUser(request)));
		filter.setAlias("patient", "patient");
		filter.setAlias("doctor", "doctor");
		filter.addDefaultSort("patient.fullNameRev",true);
		filter.addParamHandler(new LavaEqualityParamHandler("doctor.id"));
		if (components.containsKey("doctor")){
			filter.setParam("doctor.id",((Doctor)components.get("doctor")).getId());
		}
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}




	
		
		
}
