package edu.ucsf.lava.crms.enrollment.controller;

import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.crms.file.controller.BaseCrmsFileHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class EnrollmentAttachmentHandler extends BaseCrmsFileHandler {

	public EnrollmentAttachmentHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledEntity("enrollmentAttachment", CrmsFile.class);
		this.setRequiredFields(new String[]{"contentType"});
	}
	
	@Override
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		((CrmsFile)command).setCategory("Enrollment");
		return super.initializeNewCommandInstance(context, command);
	}

}
