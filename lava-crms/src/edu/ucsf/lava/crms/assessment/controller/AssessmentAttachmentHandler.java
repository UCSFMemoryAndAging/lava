package edu.ucsf.lava.crms.assessment.controller;

import edu.ucsf.lava.crms.file.controller.BaseCrmsFileHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class AssessmentAttachmentHandler extends BaseCrmsFileHandler {

	public AssessmentAttachmentHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledEntity("assessmentAttachment", CrmsFile.class);
	}

	
}
