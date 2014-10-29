package edu.ucsf.lava.crms.importer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.importer.controller.ImportDefinitionHandler;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.importer.model.CrmsImportDefinition;
import static edu.ucsf.lava.crms.importer.model.CrmsImportDefinition.*;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


/**
 * CrmsImportDefinitionHandler
 * 
 * Handles the CRUD for CrmsImportDefinition 
 * 
 * @author ctoohey
 *
 */
public class CrmsImportDefinitionHandler extends ImportDefinitionHandler {
	protected ProjectManager projectManager; 

	public CrmsImportDefinitionHandler() {
		super();
		setHandledEntity("importDefinition", CrmsImportDefinition.class);
		// projName is required if the import is inserting new Patients, EnrollmentStatuses, Visit and Instruments,
		// if dealing with pre-existing entities may not need projName, but for the most part will be creating the
		// instrument at a minimum so make it required
		this.setRequiredFields(StringUtils.mergeStringArrays(this.getRequiredFields(), 
			new String[]{"projName", "patientExistRule", "esExistRule", "visitExistRule", "instrExistRule", "instrType"}));
	}
	
	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportDefinitionHandler instead of the core ImportDefinitionHandler. If scopes
	 * need to extend ImportDefinition further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}

	public void updateManagers(Managers managers){
		super.updateManagers(managers);
		this.projectManager = CrmsManagerUtils.getProjectManager(managers);
	}
	
	@Override
	public Map addReferenceData(RequestContext context, Object command,	BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsImportDefinition crmsImportDefinition = (CrmsImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
	 	StateDefinition state = context.getCurrentState();

		//	load up dynamic lists
	 	String projName = crmsImportDefinition.getProjName(); 
	 	
		model = super.addReferenceData(context, command, errors, model); 
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
	 	if (state.getId().equals("add") || state.getId().equals("edit")) {
	 		// context.projectList
			// note that this list is filtered via projectAuth filter. CrmsAuthUser getAuthDaoFilters determines the projects to
			// which a user has some kind of access. However, the list must be further filtered based on permissions to make sure 
			// the user has the import permission for each project in the list.
			Map<String,String> projList = listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request), "context.projectList");
			projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
					CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
			dynamicLists.put("context.projectList", projList);

			if (projName != null) {
				// visit.visitTypes
				dynamicLists.put("visit.visitTypes", listManager.getDynamicList("visit.visitTypes", 
						"projectName", projName, String.class));
				// visit.visitLocations
				dynamicLists.put("visit.visitLocations", listManager.getDynamicList("visit.visitLocations", 
						"projectName", projName, String.class));
				// visit.visitWith
				dynamicLists.put("project.staffList", listManager.getDynamicList("project.projectStaffList", 
						"projectName", projName, String.class));
			
				// enrollmentStatus.projectStatus
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
		}
		return model; 
	}
	
	
	protected Event conditionalValidation(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsImportDefinition crmsImportDefinition = (CrmsImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

		// look at required fields. since they are conditionally determined, can not set them in the
		// standard way, since that takes place before binding, before properties have the values on
		// which conditional logic depends
		
		if (crmsImportDefinition.getEsExistRule().equals(MAY_OR_MAY_NOT_EXIST) || crmsImportDefinition.getEsExistRule().equals(MUST_NOT_EXIST)) {
			if (crmsImportDefinition.getEsStatus() == null) {
				LavaComponentFormAction.createCommandError(errors, "importDefinition.esStatus.required", null);
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
		}

		if (crmsImportDefinition.getVisitExistRule().equals(MAY_OR_MAY_NOT_EXIST) || crmsImportDefinition.getVisitExistRule().equals(MUST_NOT_EXIST)) {
			if (crmsImportDefinition.getVisitType() == null || crmsImportDefinition.getVisitLoc() == null || crmsImportDefinition.getVisitStatus() == null) {
				LavaComponentFormAction.createCommandError(errors, "importDefinition.visitFields.required", null);
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
		}

		if (crmsImportDefinition.getInstrExistRule().equals(MAY_OR_MAY_NOT_EXIST) || crmsImportDefinition.getInstrExistRule().equals(MUST_NOT_EXIST)) {
			if (crmsImportDefinition.getInstrDcStatus() == null) {
				LavaComponentFormAction.createCommandError(errors, "importDefinition.instrDcStatus.required", null);
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
		}
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	

	//TODO: in doSaveAdd,doSave validate that the mapping file has either a PIDN or firstName/lastName fields, and
	// a visitDate field. This would involve opening/reading the mapping file entity names (row 2) and property names
	// (row 3) into an array and searching them using ArrayUtils.indexOf 
	// presently this validation is being done in the CrmsImportHandler validateDataFile method but would
	// be better to catch it earlier, and when done here remove it from there	

	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		if (this.conditionalValidation(context, command, errors).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		return super.doSaveAdd(context, command, errors);
	}
	
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		if (this.conditionalValidation(context, command, errors).getId().equals(ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		return super.doSave(context, command, errors);
	}

	
}
