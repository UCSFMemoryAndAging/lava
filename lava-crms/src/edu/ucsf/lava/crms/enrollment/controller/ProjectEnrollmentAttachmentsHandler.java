package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectEnrollmentAttachmentsHandler extends CrmsListComponentHandler {

	
	
	
	public ProjectEnrollmentAttachmentsHandler() {
		super();
		this.setHandledList("projectEnrollmentAttachments", CrmsFile.class, "crmsFile");
		this.setEntityForStandardSourceProvider(CrmsFile.class);
	}

	@Override
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterProjectContext(sessionManager, request, CrmsFile.newFilterInstance(this.getCurrentUser(request)));
		filter.setAlias("patient", "patient");
		filter.addDefaultSort("patient.fullNameRev", true);
		filter.setAlias("enrollmentStatus", "enrollmentStatus");
		filter.addDefaultSort("enrollmentStatus.projName", true);
		filter.addDefaultSort("fileStatusDate",true);
		
		return filter;
	}

	@Override
	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager, request, filter);
	}

}
