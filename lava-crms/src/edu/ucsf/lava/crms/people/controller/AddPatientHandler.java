package edu.ucsf.lava.crms.people.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.model.Project;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;
import edu.ucsf.lava.crms.ui.controller.PatientContextFilter;
import edu.ucsf.lava.crms.ui.controller.ProjectContextFilter;
import static edu.ucsf.lava.crms.enrollment.EnrollmentManager.ANY_PROJECT_KEY;

/**
 * @author jhesse
 *
 */
public class AddPatientHandler extends CrmsEntityComponentHandler {

	
	public AddPatientHandler() {
		this.setHandledEntity("addPatient", AddPatientCommand.class);
		CrmsSessionUtils.setIsPatientContext(this);
		}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		if (ActionUtils.getEventName(context).equalsIgnoreCase("saveAdd")) {
			Map components = ((ComponentCommand)command).getComponents();
			AddPatientCommand apc = (AddPatientCommand) components.get("addPatient");
			
			if(apc.getDeidentified()){
				setRequiredFields(new String[]{
		 				"subjectId",
		 				"patient.birthDate",
		 				"patient.gender",
		 				"enrollmentStatus.projName",
		 				"status",
		 				"statusDate"});
			}else{		
			setRequiredFields(new String[]{
	 				"patient.firstName",
	 				"patient.lastName",
	 				"patient.birthDate",
	 				"patient.gender",
	 				"enrollmentStatus.projName",
	 				"status",
	 				"statusDate"});
			}
			return getRequiredFields();
		}
		
		return new String[0];
	}
	
	//default status date to current date. do not default project to current project, because
	//there may not be one, and even if so, user may not have permission to add enrollmentStatus in
	//that project - the project list on the page is filtered based on user permissions
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		AddPatientCommand apc = (AddPatientCommand)command;
		apc.setStatusDate(Patient.dateWithoutTime(new Date()));
		apc.setPatient(new Patient());
		Project project = CrmsSessionUtils.getCurrentProject(sessionManager,request);
		String projUnitDesc = (project==null)? null : project.getProjUnitDesc();
		// passing ANY_PROJECT_KEY to the prototype will support instance specific customization, and 
		// if there is none, will default to the base EnrollmentStatus class. if there is a dynamic
		// customization the doReRender method will obtain the custom EnrollmentStatus subclass, and 
		// doReRender is guaranteed because projName is a required field
		apc.setEnrollmentStatus(enrollmentManager.getEnrollmentStatusPrototype(ANY_PROJECT_KEY));
		apc.getEnrollmentStatus().setProjName(projUnitDesc);
		return apc;
	}

	
	
	/* 
	 * Check to see if the project has changed, if so, then change the enrollment status object
	 */
	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map components = ((ComponentCommand)command).getComponents();
		AddPatientCommand apc = (AddPatientCommand) components.get("addPatient");
		EnrollmentStatus es = apc.getEnrollmentStatus();
		EnrollmentStatus projectSpecific = enrollmentManager.getEnrollmentStatusPrototype(es.getProjName());
		//if necessary, change the enrollment status type
		if(!projectSpecific.getClass().equals(es.getClass())){
			projectSpecific.setProjName(es.getProjName());
			apc.setEnrollmentStatus(projectSpecific);
		}
		
		return super.doReRender(context, command, errors);
	}

	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();

		Map components = ((ComponentCommand)command).getComponents();
		AddPatientCommand apc = (AddPatientCommand) components.get("addPatient");
	
		Patient p = apc.getPatient();
		
		if ((p!=null) && p.getBirthDate().after(new Date())) {
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"patient.futureBirthDate"}, new String[]{new SimpleDateFormat("MM/dd/yyyy").format(p.getBirthDate())}, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);  // could have continued to next hasErrors() check if there wasn't a getIgnoreMatches() check
		}
		
		if (errors.hasErrors() && !apc.getIgnoreMatches()){
			//TODO: add object error explaining why returning to page without adding
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}	
		
		if (apc.getDeidentified()){
			p.setLastName(Patient.DEIDENTIFIED);
			p.setFirstName(apc.getSubjectId());
			p.setMiddleName(null);
			p.setSuffix(null);
			p.setDegree(null);
		}
		
		String createdBy = CrmsSessionUtils.getCrmsCurrentUser(sessionManager,((ServletExternalContext)context.getExternalContext()).getRequest()).getUserName();
		if (createdBy != null && createdBy.length() > 25) {
			// have to truncate due to database mismatch in column lengths
			createdBy = createdBy.substring(0,24);
		}
		p.setCreatedBy(createdBy);
		p.setCreated(new Date());
		p.save();
		// set the newly added patient as the current patient in context
		PatientContextFilter patientContextFilter = (PatientContextFilter) ((ComponentCommand)command).getComponents().get("patientContext");
		patientContextFilter.setPatient(p);
		setContextFromScope(context, p.getId());
		
		EnrollmentStatus es = apc.getEnrollmentStatus();
		EnrollmentStatus projectSpecific = enrollmentManager.getEnrollmentStatusPrototype(es.getProjName());
		//if necessary, convert the es object to the projectSpecific type
		if(!projectSpecific.getClass().equals(es.getClass())){
			projectSpecific.setProjName(es.getProjName());
			es = projectSpecific;
		}
		es.setPatient(p);
		es.setStatus(apc.getStatus(),apc.getStatusDate());
		es.save();
		
		// if the new patient is not associated with the current project in context, clear the project in context
		// since this is a new patient, it will only be associated with one initial project, so just need to check that
		Project currProject = CrmsSessionUtils.getCurrentProject(sessionManager,request);
		if (currProject != null && !currProject.getProjUnitDesc().equals(es.getProjName())) {
			ProjectContextFilter projContextFilter = (ProjectContextFilter) ((ComponentCommand)command).getComponents().get("projectContext");
			projContextFilter.setProject((Project)null);
			CrmsSessionUtils.clearCurrentProject(sessionManager, request);
			// add informational message (need to use sessionServie.addFormError because add patient is a top level flow so
			// when this event is processed, the flow will end, and thus need to store the msg in the session, not the flow errors object)
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{"info.addPatient.clearProject"}, new Object[]{currProject.getName()});
		}
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		
		Map<String,String> projList = listManager .getDynamicList(getCurrentUser(request), "context.projectList");
		
		projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
				CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
		
		dynamicLists.put("addPatient.projectList", projList);
		
		
		
		String projName = ((AddPatientCommand)((ComponentCommand)command).getComponents().get("addPatient")).getEnrollmentStatus().getProjName();
		if (projName != null){
			//try for project status based on full projName
			dynamicLists.put("enrollmentStatus.projectStatus",listManager .getDynamicList("enrollmentStatus.projectStatus", 
					"projectName", projName, String.class));
			//if no entries found (size is 1 because "","" entry added to all dynamic lists), then use just project part as the project name leaving off unit 
			if (dynamicLists.get("enrollmentStatus.projectStatus").size()==1){
				String project = projectManager.getProject(projName).getProject();
				dynamicLists.put("enrollmentStatus.projectStatus",listManager .getDynamicList("enrollmentStatus.projectStatus", 
					"projectName", project, String.class));
			}
			//if no entries found based on projName or just project without the unit then get general statuses
			if (dynamicLists.get("enrollmentStatus.projectStatus").size()==1){
				dynamicLists.put("enrollmentStatus.projectStatus",listManager .getDynamicList("enrollmentStatus.projectStatus", 
						"projectName", "GENERAL", String.class));
			}
		}
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}



	
}
