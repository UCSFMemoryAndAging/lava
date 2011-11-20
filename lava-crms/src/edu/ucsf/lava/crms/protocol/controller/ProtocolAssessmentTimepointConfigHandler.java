package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;

public class ProtocolAssessmentTimepointConfigHandler extends ProtocolTimepointConfigHandler {

	public ProtocolAssessmentTimepointConfigHandler() {
		super();
		setHandledEntity("protocolAssessmentTimepointConfig", ProtocolAssessmentTimepointConfig.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
		// want to force user to create a complete protocol definition so that when a patient is
		// enrolled do not have to be concerned with things that are not defined
		
	    setRequiredFields(new String[]{"label", "schedWinSize", "schedWinOffset",
	    		"collectWinSize","collectWinOffset"
	    		});
	    return getRequiredFields();
	}
	
	
}
