package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class EnrollmentStatusHandler extends CrmsEntityComponentHandler {
	protected static final String DELETE_LAST_ENROLLMENT_STATUS_ERROR_CODE = "enrollmentStatus.lastStatus.command";
	
	public EnrollmentStatusHandler() {
		super();
		this.setHandledEntity("enrollmentStatus", EnrollmentStatus.class);
		CrmsSessionUtils.setIsPatientContext(this);
	}

	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		if (p != null){
			((EnrollmentStatus)command).setPatient(p);
		}
		return command;
	}

	protected Long getContextIdFromRequest(RequestContext context){		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		EnrollmentStatus e = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(getFilterWithId(request,Long.valueOf(context.getFlowScope().getString("id"))));
		return e.getPatient().getId();
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		dynamicLists.put("enrollmentStatus.referralSource", listManager.getDynamicList("enrollmentStatus.referralSource", 
				"projectName",((EnrollmentStatus)((ComponentCommand)command).getComponents().get("enrollmentStatus")).getProjName(), 
                String.class));
		
		model.put("dynamicLists", dynamicLists);
		
		return super.addReferenceData(context, command, errors, model);
	}



	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		EnrollmentStatus es  = ((EnrollmentStatus)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName()));
		
		if(es.isLastEnrollmentStatus()){
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{DELETE_LAST_ENROLLMENT_STATUS_ERROR_CODE}, null);
			 return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return super.doConfirmDelete(context, command, errors);
		
	}

	
	
	
	
}
