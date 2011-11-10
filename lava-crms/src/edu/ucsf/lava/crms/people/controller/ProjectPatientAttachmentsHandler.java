package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

}
