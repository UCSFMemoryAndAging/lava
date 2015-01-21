package edu.ucsf.lava.crms.enrollment.controller;

import edu.ucsf.lava.crms.file.controller.BaseCrmsFileHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ConsentAttachmentHandler extends BaseCrmsFileHandler {

	public ConsentAttachmentHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledEntity("consentAttachment", CrmsFile.class);
		// do not want the enrollStatId as done in the superclass as then the attachment
		// will be assigned to the EnrollmentStatus record and not the Consent record
		this.setRequiredFields(new String[]{"contentType"});
	}

	
}