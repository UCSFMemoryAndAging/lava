package edu.ucsf.lava.crms.people.controller;

import edu.ucsf.lava.crms.file.controller.BaseCrmsFileHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientAttachmentHandler extends BaseCrmsFileHandler {

	public PatientAttachmentHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledEntity("patientAttachment", CrmsFile.class);
	}

	
}
