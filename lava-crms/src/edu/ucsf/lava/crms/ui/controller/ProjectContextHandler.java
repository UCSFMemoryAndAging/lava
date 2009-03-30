package edu.ucsf.lava.crms.ui.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.controller.CrmsContextChangeComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.model.Project;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectContextHandler extends CrmsContextChangeComponentHandler{


	public ProjectContextHandler(){
		super();
		Map<String,Class> handledObject = new HashMap<String,Class>();
		handledObject.put("projectContext", ProjectContextFilter.class);
		this.setHandledObjects(handledObject);	
	}
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map backingObject = new HashMap<String,Object>();
		ProjectContextFilter filter = new ProjectContextFilter();
		Project project= CrmsSessionUtils.getCurrentProject(sessionManager,request);
		filter.setProject(CrmsSessionUtils.getCurrentProject(sessionManager,request));
		backingObject.put("projectContext",filter);
		return backingObject;
	}
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put("context.projectList", 
			listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request), "context.projectList"));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}		

	

	public Event postEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProjectContextFilter filter = ((ProjectContextFilter)((ComponentCommand)command).getComponents().get("projectContext"));
		filter.setProject(CrmsSessionUtils.getCurrentProject(sessionManager,request));
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	protected void doContextChange(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager,request);
		Project currProject = CrmsSessionUtils.getCurrentProject(sessionManager,request);
		Patient currPatient = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		ProjectContextFilter filter = ((ProjectContextFilter)((ComponentCommand)command).getComponents().get("projectContext"));
		// is user sets project context to blank, same effect as the Clear Project link
		if (StringUtils.isEmpty(filter.getProjectName())) {
			doContextClear(context, command, errors);
		}
		else {
			//if project requested is different than current project than shift project context.
			if(currProject == null ||
					(StringUtils.isNotEmpty(filter.getProjectName()) 
					  && !filter.getProjectName().equalsIgnoreCase(currProject.getName()))){
			
				//	first change project context
				CrmsSessionUtils.setCurrentProject(sessionManager,request,filter.getProjectName());
				Project newProject = CrmsSessionUtils.getCurrentProject(sessionManager,request);
				filter.setProject(newProject);

				//now check to see if patient is associated with the project, and if not, clear the current patient.
				//if the current project context is not set (i.e. unfiltered), do not clear the patient
				if (newProject != null) {
					if(currPatient != null && !newProject.isPatientAssociatedWithProject(currPatient)){				
						//not associated with project so clear out current patient
						
						// add informational message
						// add form error to the session
						// note: just adding an object error to the errors object would not display, because on project 
						// context change which clears patient, a FlowDefinitionRedirect is done to start a new flow at 
						// DefaultProjectAction (since there is no longer a current patient), which means a new command 
						// object is used, so any errors associated with the old command object are lost. need a mechanism 
						// for errors that should transcend starting new flows, i.e. store the error in session scope here, 
						// and then have CustomViewSelector restore these errors by associating with the new command object 
						// and clearing them from session scope
						CoreSessionUtils.addFormError(sessionManager,request, new String[]{"info.projectContext.clearPatient"}, new Object[]{currPatient.getFullNameRev(),filter.getProjectName()});

						CrmsSessionUtils.setCurrentPatient(sessionManager,request,(Patient)null);
						// set flag in request scope to indicate to flow that new flow should begin at
						// the defaultProjectAction
						setDoDefaultActionFlag(context);
					}
				}
			}
		}
	}

	protected void doContextClear(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager,request);
		ProjectContextFilter filter = ((ProjectContextFilter)((ComponentCommand)command).getComponents().get("projectContext"));
		CrmsSessionUtils.clearCurrentProject(sessionManager, request);
		filter.setProject((Project)null);
	}
	
}
