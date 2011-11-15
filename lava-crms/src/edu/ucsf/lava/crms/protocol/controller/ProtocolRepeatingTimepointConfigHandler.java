package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolRepeatingTimepointConfig;

public class ProtocolRepeatingTimepointConfigHandler extends ProtocolTimepointConfigHandler {

	public ProtocolRepeatingTimepointConfigHandler() {
		super();
		setHandledEntity("protocolRepeatingTimepointConfig", ProtocolRepeatingTimepointConfig.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label",
	    		"total","interval"
	    		});
	    return getRequiredFields();
	}

}
