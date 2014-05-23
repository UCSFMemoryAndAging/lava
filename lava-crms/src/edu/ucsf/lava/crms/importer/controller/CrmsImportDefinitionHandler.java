package edu.ucsf.lava.crms.importer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.importer.controller.ImportDefinitionHandler;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.importer.model.CrmsImportDefinition;
import edu.ucsf.lava.crms.importer.model.CrmsImportSetup;
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

	public CrmsImportDefinitionHandler() {
		super();
		setHandledEntity("importDefinition", CrmsImportDefinition.class);
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
	
	@Override
	public Map addReferenceData(RequestContext context, Object command,	BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//	load up dynamic lists
	 	StateDefinition state = context.getCurrentState();
		model = super.addReferenceData(context, command, errors, model); 
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		CrmsImportDefinition crmsImportDefinition = (CrmsImportDefinition) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
	 	if (state.getId().equals("edit")) {
			// note that this list is filtered via projectAuth filter. CrmsAuthUser getAuthDaoFilters determines the projects to
			// which a user has some kind of access. However, the list must be further filtered based on permissions to make sure 
			// the user has the import permission for each project in the list.
			Map<String,String> projList = listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request), "context.projectList");
			projList = CrmsAuthUtils.filterProjectListByPermission(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request),
					CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
			dynamicLists.put("context.projectList", projList);
		}
		model.put("dynamicLists", dynamicLists);
		return model; 
	}

}
