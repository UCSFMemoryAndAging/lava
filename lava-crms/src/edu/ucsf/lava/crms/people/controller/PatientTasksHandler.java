package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.Task;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientTasksHandler extends CrmsListComponentHandler {
	
	public PatientTasksHandler(){
		this.setHandledList("patientTasks","task");
		this.setEntityForStandardSourceProvider(Task.class);
	}
	

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterPatientContext(sessionManager,request,Task.newFilterInstance(getCurrentUser(request)));
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("openedDate",false);
		return filter;
				
	}

	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
		

	
	
}
