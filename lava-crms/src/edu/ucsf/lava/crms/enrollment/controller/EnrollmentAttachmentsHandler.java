package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class EnrollmentAttachmentsHandler extends CrmsListComponentHandler {

	
	
	
	public EnrollmentAttachmentsHandler() {
		super();
		this.setHandledList("enrollmentAttachments", CrmsFile.class, "crmsFile");
		this.setEntityForStandardSourceProvider(CrmsFile.class);
	}

	@Override
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterPatientContext(sessionManager, request, CrmsFile.newFilterInstance(this.getCurrentUser(request)));
		filter.setAlias("enrollmentStatus", "enrollmentStatus");
		filter.addDefaultSort("enrollmentStatus.projName", true);
		filter.addDefaultSort("fileStatusDate",true);
		filter.addDaoParam(filter.daoNull("visitId"));
		return filter;
	}

	@Override
	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
		// TODO Auto-generated method stub

	}

}
