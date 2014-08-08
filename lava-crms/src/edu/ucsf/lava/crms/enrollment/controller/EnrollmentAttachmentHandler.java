package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.crms.file.controller.BaseCrmsFileHandler;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class EnrollmentAttachmentHandler extends BaseCrmsFileHandler {

	public EnrollmentAttachmentHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledEntity("enrollmentAttachment", CrmsFile.class);
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		// cannot get request parameter value directly from the request because of post-redirect-get second request. instead. the
		// flow will pass request parameters with predefined names ("param", "param1", etc.) into flowScope to be accessed
		String parentView = context.getFlowScope().getString("param");
		model.put("parentView", parentView);
		return super.addReferenceData(context, command, errors, model);
	}

}
