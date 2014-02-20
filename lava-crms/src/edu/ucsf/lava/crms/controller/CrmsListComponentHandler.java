package edu.ucsf.lava.crms.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.enrollment.EnrollmentManager;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.scheduling.VisitManager;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;
import edu.ucsf.lava.crms.ui.controller.PatientContextFilter;
import static edu.ucsf.lava.crms.ui.controller.PatientContextHandler.PATIENT_CONTEXT;

public abstract class CrmsListComponentHandler extends BaseListComponentHandler {

	protected InstrumentManager instrumentManager;
	protected EnrollmentManager enrollmentManager;
	protected VisitManager visitManager; 
	protected ProjectManager projectManager; 
	
	public void updateManagers(Managers managers){
		super.updateManagers(managers);
		this.enrollmentManager = CrmsManagerUtils.getEnrollmentManager(managers);
		this.instrumentManager = CrmsManagerUtils.getInstrumentManager(managers);
		this.projectManager = CrmsManagerUtils.getProjectManager(managers);
		this.visitManager = CrmsManagerUtils.getVisitManager(managers);
	}

	
	public void subFlowReturnHook(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		super.subFlowReturnHook(context, command, errors);
		
		// when returning to list from subflow, the subflow may have set or changed the current patient context. 
		// however, the "Patient Context" search box will not reflect this, since resuming a parent flow will
		// resume it with whatever state it had when it paused for the subflow to run. thus the state of the
		// patient context will be whatever it was when entering the subflow. so must explicitly check to
		// see if patient context changed and if so, change it in the resuming list flow.
		PatientContextFilter patientContextFilter = (PatientContextFilter) ((ComponentCommand)command).getComponents().get(PATIENT_CONTEXT);
		Patient currentPatient = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		if(currentPatient!= null && (patientContextFilter.getPatient()==null || !patientContextFilter.getPatient().getId().equals(currentPatient.getId()))){
			patientContextFilter.setPatient(currentPatient);
		}
		
		// not doing this for Project Context because subflows do not change the Project Context 
	}

}
