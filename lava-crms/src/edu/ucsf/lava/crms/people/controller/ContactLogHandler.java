package edu.ucsf.lava.crms.people.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.ContactLog;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ContactLogHandler extends CrmsEntityComponentHandler {

	public ContactLogHandler (){
		super();
		this.setHandledEntity("contactLog", ContactLog.class);
		CrmsSessionUtils.setIsPatientContext(this);
		this.setRequiredFields(new String[]{"projName","method","logDate","staffInit"});
	}
	
	@Override
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		((ContactLog)command).setPatient(CrmsSessionUtils.getCurrentPatient(sessionManager,request));
		return command;
	}

	protected Long getContextIdFromRequest(RequestContext context){		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ContactLog c = (ContactLog) ContactLog.MANAGER.getOne(getFilterWithId(request,Long.valueOf(context.getFlowScope().getString("id"))));
		return c.getPatient().getId();
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		ContactLog cl = (ContactLog)((ComponentCommand)command).getComponents().get("contactLog");

		//get list using project name
		Map<String,String> projList = listManager.getDynamicList(getCurrentUser(request),
				"context.projectList");
		projList = filterProjectListByPermission(getCurrentUser(request),
				CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
		dynamicLists.put("context.projectList", projList);
		
		if (cl.getProjName()!= null){
			dynamicLists.put("project.staffList", listManager.getDynamicList("project.projectStaffList", 
						"projectName", cl.getProjName(),String.class));
			}
			else {
				dynamicLists.put("project.staffList", listManager.getDynamicList("patient.patientStaffList", 
						"patientId", cl.getPatient().getId(),Long.class));
			}
		
		
		
	
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
////public void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
	public void registerPropertyEditors(PropertyEditorRegistry registry) {
		super.registerPropertyEditors(registry);
		// register a property-specific custom editor for visitDate since it also requires a time component, so
		// as not to interfere with the class-specific custom editor for any java.util.Date properties, such as
		// visit.waitListDate
		// vital: Spring binding requires our syntax to not include the single quotes around the component entity name,
		// i.e. must pass in "components[visit].visitDate" and not "components['visit'].visitDate" to registerCustomEditor
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mma");
		registry.registerCustomEditor(Date.class, "components[contactLog].logDate", new CustomDateEditor(dateFormat, true));
	}
	
}
