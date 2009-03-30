package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.Caregiver;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectCaregiversHandler extends CrmsListComponentHandler {
	
	public ProjectCaregiversHandler() {
		super();
	    this.setHandledList("projectCaregivers","caregiver");
		this.setEntityForStandardSourceProvider(Caregiver.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterProjectContext(sessionManager,request,Caregiver.newFilterInstance(getCurrentUser(request)));
		filter.addDefaultSort("patient.fullNameRev",true);
		filter.addDefaultSort("fullNameRev",true);
		filter.setAlias("patient","patient");
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}


}
