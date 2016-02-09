package edu.ucsf.lava.crms.assessment.controller;

import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.crms.file.controller.BaseCrmsFileHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class AssessmentAttachmentHandler extends BaseCrmsFileHandler {

	public AssessmentAttachmentHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledEntity("assessmentAttachment", CrmsFile.class);
		this.setRequiredFields(new String[]{"contentType"});
		
	}

	@Override
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		((CrmsFile)command).setCategory("Instrument");
		return super.initializeNewCommandInstance(context, command);
	}

	
}
