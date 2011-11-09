package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolAssessmentTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointBase;

public class PatientProtocolAssessmentTimepointHandler extends PatientProtocolTimepointHandler {

	public PatientProtocolAssessmentTimepointHandler() {
		super();
		setHandledEntity("patientProtocolAssessmentTimepoint", edu.ucsf.lava.crms.protocol.model.PatientProtocolAssessmentTimepoint.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{/*"staff"*/});
	    return getRequiredFields();
	}
	
	
}
