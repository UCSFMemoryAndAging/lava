package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.Consent;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class ProjectConsentsHandler extends CrmsListComponentHandler {

	public ProjectConsentsHandler() {
		super();
		this.setHandledList("projectConsents","consent");
		this.setEntityForStandardSourceProvider(Consent.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = Consent.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("patient.fullNameRevNoSuffix",true);
		filter.setAlias("patient", "patient");
		return CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		//load up dynamic lists for the list filter
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		String project = CrmsSessionUtils.getCurrentProject(sessionManager,request)==null ? "%": CrmsSessionUtils.getCurrentProject(sessionManager,request).getName();
		dynamicLists.put("consent.consentTypes", listManager.getDynamicList("consent.consentTypes", 
				"projectName",project,String.class));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
		
}
