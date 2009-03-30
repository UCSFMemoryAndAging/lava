package edu.ucsf.lava.crms.people.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.people.model.Task;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class TaskHandler extends CrmsEntityComponentHandler {

	public TaskHandler (){
		super();
		this.setHandledEntity("task", Task.class);
		CrmsSessionUtils.setIsPatientContext(this);

	}
	
	@Override
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Task t = (Task)command;
		t.setPatient(CrmsSessionUtils.getCurrentPatient(sessionManager,request));
		t.setTaskStatus("OPEN");
		t.setOpenedDate(t.dateWithoutTime(new Date()));
		return command;
	}

	protected Long getContextIdFromRequest(RequestContext context){		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Task t = (Task) Task.MANAGER.getOne(getFilterWithId(request,Long.valueOf(context.getFlowScope().getString("id"))));
		return t.getPatient().getId();
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		Task t = (Task)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		Patient p = t.getPatient();

		if(p != null){
			Map<String,String> projList = listManager.getDynamicList(getCurrentUser(request),
				"enrollmentStatus.patientProjects",	"patientId", p.getId(), Long.class);
			projList = 	filterProjectListByPermission(getCurrentUser(request),
					CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
			dynamicLists.put("enrollmentStatus.patientProjects", projList);
		}
		
		if (t.getProjName()!= null){
			dynamicLists.put("project.staffList", listManager.getDynamicList("project.projectStaffList", 
						"projectName", t.getProjName(),String.class));
			}
			else {
				dynamicLists.put("project.staffList", listManager.getDynamicList("patient.patientStaffList", 
						"patientId", t.getPatient().getId(),Long.class));
			}
		
		
		
	
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	

	
}
