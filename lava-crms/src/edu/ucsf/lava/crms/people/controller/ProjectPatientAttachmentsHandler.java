package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectPatientAttachmentsHandler extends CrmsListComponentHandler {

	public ProjectPatientAttachmentsHandler() {
		super();
		this.setHandledList("projectPatientAttachments", CrmsFile.class, "crmsFile");
		this.setEntityForStandardSourceProvider(CrmsFile.class);
	}

	@Override
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterProjectContext(sessionManager, request, CrmsFile.newFilterInstance(this.getCurrentUser(request)));
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("patient.fullNameRev", true);
		filter.addDefaultSort("fileStatusDate",false);
		return filter;
	}

	@Override
	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager, request, filter);
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		//load up dynamic lists for the list filter
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		if (CrmsSessionUtils.getCurrentProject(sessionManager,request)==null) {
			// if no current project, include consent types across all projects
			dynamicLists.put("crmsFile.contentType", listManager.getDynamicList("crmsFile.contentType")); 
		}
		else {
			dynamicLists.put("crmsFile.contentType", listManager.getDynamicList("crmsFile.projectContentType", 
					"projectName",CrmsSessionUtils.getCurrentProject(sessionManager,request).getName(),String.class));
		}
		model.put("dynamicLists", dynamicLists);

		return super.addReferenceData(context, command, errors, model);
	}
	
}
