package edu.ucsf.lava.crms.enrollment.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.Consent;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ConsentHandler extends CrmsEntityComponentHandler {

	public ConsentHandler() {
		this.setHandledEntity("consent", Consent.class);
		CrmsSessionUtils.setIsPatientContext(this);
		this.setRequiredFields(new String[]{"projName","consentDate","consentType"});
	}

	protected Object initializeNewCommandInstance(RequestContext context,Object command){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		if (p != null){
			((Consent)command).setPatient(p);
		}
		// do not default project to current project, as user may not have permission to 
		// add consent in that project
		return command;
	}

	protected Long getContextIdFromRequest(RequestContext context){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Consent c = (Consent) Consent.MANAGER.getOne(getFilterWithId(request,Long.valueOf(context.getFlowScope().getString("id"))));
		return c.getPatient().getId();
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		Consent c = (Consent)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		Map<String,String> projList = listManager.getDynamicList(getCurrentUser(request),
				"enrollmentStatus.patientProjects", "patientId", CrmsSessionUtils.getCurrentPatient(sessionManager,request).getId(), Long.class);
		projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
				CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
		dynamicLists.put("enrollmentStatus.patientProjects", projList);

		dynamicLists.put("patient.caregivers", listManager.getDynamicList("patient.caregivers", 
				"patientId", CrmsSessionUtils.getCurrentPatient(sessionManager,request).getId(), Long.class));

		if (c.getProjName() != null) {
			dynamicLists.put("consent.consentTypes", listManager.getDynamicList("consent.consentTypes", "projectName", 
					c.getProjName(), String.class));
			dynamicLists.put("project.staffList", listManager.getDynamicList("project.projectStaffList", 
					"projectName", c.getProjName(),String.class));
		
		}
		else {
			dynamicLists.put("consent.consentTypes", new HashMap<String, String>());
			dynamicLists.put("project.staffList", new HashMap<String, String>());
		
			
		}
		
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}

}
