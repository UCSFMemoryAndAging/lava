package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.people.model.AddPatientCommand;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class AddEnrollmentStatusHandler extends CrmsEntityComponentHandler {

	
	
	public AddEnrollmentStatusHandler() {
		super();
		this.setHandledEntity("addEnrollmentStatus", AddPatientCommand.class);
		CrmsSessionUtils.setIsPatientContext(this);
	}

	// override to check project list
	public Event authorizationCheck(RequestContext context, Object command) throws Exception {
		// first do standard permissions check
		Event returnEvent = super.authorizationCheck(context, command);

		if (returnEvent.getId().equals(this.SUCCESS_FLOW_EVENT_ID)) {
			// next, check that the user has permission to add enrollment status to at least one project
			HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
			Map<String,String> projList = listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
					"enrollmentStatus.patientUnassignedProjects", "patientId",
	                ((AddPatientCommand)((ComponentCommand)command).getComponents().get("addEnrollmentStatus")).getPatient().getId(), Long.class);
			projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
					CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
			if (projList.size() == 1) { // there will always be the blank entry, so list size 1 means empty list
				CoreSessionUtils.addFormError(sessionManager,request, new String[]{"authorization.noEnrollmentProjects.command"}, null);
				return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
			}
			return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
		}
		else {
			return returnEvent;
		}
	}
	

	/* 
	 * Check to see if the project has changed, if so, then change the enrollment status object
	 */
	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map components = ((ComponentCommand)command).getComponents();
		AddPatientCommand apc = (AddPatientCommand) components.get("addEnrollmentStatus");
		EnrollmentStatus es = apc.getEnrollmentStatus();
		EnrollmentStatus projectSpecific = enrollmentManager.getEnrollmentStatusPrototype(es.getProjName());
		//change the enrollment status type to the prototype...
		projectSpecific.setProjName(es.getProjName());
		projectSpecific.setPatient(es.getPatient());
		apc.setEnrollmentStatus(projectSpecific);
		return super.doReRender(context, command, errors);
	}

	
	protected String[] defineRequiredFields(RequestContext context, Object command) {
		if (ActionUtils.getEventName(context).equalsIgnoreCase("saveAdd")) {
			setRequiredFields(new String[]{
	 				"enrollmentStatus.projName",
	 				"status",
	 				"statusDate"});
			return getRequiredFields();
		}
		
		return new String[0];
	}
	
	//default patient based on current patient context and status date to current date. do not
    // default project as project list subject to permission filtering and may not have permission
	// to add the enrollment status in the current project
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		AddPatientCommand apc = (AddPatientCommand)command;
		apc.setPatient(CrmsSessionUtils.getCurrentPatient(sessionManager,request));
		apc.setStatusDate(new Date());
		apc.setEnrollmentStatus(enrollmentManager.getEnrollmentStatusPrototype(null));
		apc.getEnrollmentStatus().setPatient(apc.getPatient());
		return apc;
	}

	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		Map components = ((ComponentCommand)command).getComponents();
		AddPatientCommand apc = (AddPatientCommand) components.get("addEnrollmentStatus");
		EnrollmentStatus es = apc.getEnrollmentStatus();
		es.setStatus(apc.getStatus(),apc.getStatusDate());
		es.updateLatestStatusValues();
		es.save();
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
	
		Map<String,String> projList = listManager.getDynamicList(getCurrentUser(request),
				"enrollmentStatus.patientUnassignedProjects", "patientId",
                ((AddPatientCommand)((ComponentCommand)command).getComponents().get("addEnrollmentStatus")).getPatient().getId(), Long.class);
		projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
				CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
		dynamicLists.put("enrollmentStatus.patientUnassignedProjects", projList);
	
		String projName = ((AddPatientCommand)((ComponentCommand)command).getComponents().get("addEnrollmentStatus")).getEnrollmentStatus().getProjName();
		
		if (projName != null){
			//try for project status based on full projName
			dynamicLists.put("enrollmentStatus.projectStatus",listManager.getDynamicList("enrollmentStatus.projectStatus", 
					"projectName", projName, String.class));
			//if no entries found (size is 1 because "","" entry added to all dynamic lists), then use just project part as the project name leaving off unit 
			if (dynamicLists.get("enrollmentStatus.projectStatus").size()==1){
				String project = projectManager.getProject(projName).getProject();
				dynamicLists.put("enrollmentStatus.projectStatus",listManager.getDynamicList("enrollmentStatus.projectStatus", 
					"projectName", project, String.class));
			}
			//if no entries found based on projName or just project without the unit then get general statuses
			if (dynamicLists.get("enrollmentStatus.projectStatus").size()==1){
				dynamicLists.put("enrollmentStatus.projectStatus",listManager.getDynamicList("enrollmentStatus.projectStatus", 
						"projectName", "GENERAL", String.class));
			}
		}
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}




}
