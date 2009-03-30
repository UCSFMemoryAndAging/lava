package edu.ucsf.lava.crms.people.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Caregiver;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class CaregiverHandler extends CrmsEntityComponentHandler {
	
	public CaregiverHandler (){
		super();
		setHandledEntity("caregiver", edu.ucsf.lava.crms.people.model.Caregiver.class);
		CrmsSessionUtils.setIsPatientContext(this);
		 setRequiredFields(new String[]{
				"firstName",
				"lastName",
				"relation",
				"livesWithPatient",
				"active"});
	}
	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		if (p != null){
			((Caregiver)command).setPatient(p);
		}
		return command;
	}

	protected Long getContextIdFromRequest(RequestContext context){		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Caregiver c = (Caregiver) Caregiver.MANAGER.getOne(getFilterWithId(request,Long.valueOf(context.getFlowScope().getString("id"))));
		return c.getPatient().getId();
	}
}
