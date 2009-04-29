package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.Caregiver;
import edu.ucsf.lava.crms.people.model.ContactLog;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientContactLogHandler extends CrmsListComponentHandler {
	
	public PatientContactLogHandler(){
		this.setHandledList("patientContactLog","contactLog");
		this.setEntityForStandardSourceProvider(ContactLog.class);
	}
	

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		LavaDaoFilter filter = ContactLog.newFilterInstance(getCurrentUser(request));
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("logDate",false);
		filter.addDefaultSort("logTime",false);
		
		CrmsSessionUtils.setFilterPatientContext(sessionManager,request,filter);
		
		return filter;
				
	}

	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
		

	
}
