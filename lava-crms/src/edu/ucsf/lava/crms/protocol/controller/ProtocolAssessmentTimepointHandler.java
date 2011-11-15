package edu.ucsf.lava.crms.protocol.controller;

import org.springframework.webflow.execution.RequestContext;

public class ProtocolAssessmentTimepointHandler extends ProtocolTimepointHandler {

	public ProtocolAssessmentTimepointHandler() {
		super();
		setHandledEntity("protocolAssessmentTimepoint", edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepoint.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{/*"staff"*/});
	    return getRequiredFields();
	}
	
	
}
