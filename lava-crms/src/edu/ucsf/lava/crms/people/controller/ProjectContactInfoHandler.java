package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.ContactInfo;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectContactInfoHandler extends CrmsListComponentHandler {

	public ProjectContactInfoHandler() {
		this.setHandledList("projectContactInfo","contactInfo");
		this.setEntityForStandardSourceProvider(ContactInfo.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = ContactInfo.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("patient.fullNameRevNoSuffix",true);
		filter.setAlias("patient", "patient");
		return CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}


}
