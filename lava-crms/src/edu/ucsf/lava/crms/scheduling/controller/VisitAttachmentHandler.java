package edu.ucsf.lava.crms.scheduling.controller;

import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.crms.file.controller.BaseCrmsFileHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class VisitAttachmentHandler extends BaseCrmsFileHandler {

	public VisitAttachmentHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledEntity("visitAttachment", CrmsFile.class);
		this.setRequiredFields(new String[]{"contentType"});
	}

	@Override
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		((CrmsFile)command).setCategory("Visit");
		return super.initializeNewCommandInstance(context, command);
	}

	
}
