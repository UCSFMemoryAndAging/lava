package edu.ucsf.lava.crms.importer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.importer.controller.ImportHandler;
import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.importer.model.CrmsImportSetup;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

/**
 * CrmsImportHandler
 * 
 * Handles the crms specific part of importing a data file.
 * 
 * @author ctoohey
 *
 */
public class CrmsImportHandler extends ImportHandler {

	public CrmsImportHandler() {
		super();
		// the defaultObjectName should ideally be the same as the target part of the action which
		// uses this handler, i.e. lava.core.importer.import.import so target='import', because
		// the flow constructs event transitions using the target part of the action (at least for
		// customizing actions) while the decorator uses the defaultObjectName on eventButton that
		// will construct the event to be submitted which should match the transition
		setHandledEntity("import", CrmsImportSetup.class);
		setDefaultObjectBaseClass(ImportSetup.class);
		this.setRequiredFields(new String[]{
				"definitionName",
				"dataFileInput",
				"projName"});
	}

	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsImportHandler instead of the core ImportHandler.  If scopes
	 * need to extend Import further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
	
		
	
	//TODO: implement isAuthorized for project authorization (make sure proj auth flag is 
	// set on importSetup. assume it is ok that importSetup is not a persistent object
	
	
	
	
	
//NOTE: more than likely this will be a helper method override rather than doImport because the crms functionality
//is in the  middle of basic import setup and import finish stuff
	protected Event doImport(RequestContext context, Object command, BindingResult errors) throws Exception {
		
		// projNeme is required if the import is inserting new Patients, Visits and Instruments, so check to make
		// sure it has been specified in the definition mappingFile or in CrmsImportSetup
		//TODO: check for projName
		//?? would need to distinguish INSERT from UPDATE because if UPDATE would not necessarily need projName
		
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);

	}

	@Override
	public Map addReferenceData(RequestContext context, Object command,	BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
	 	StateDefinition state = context.getCurrentState();
		model = super.addReferenceData(context, command, errors, model); 
		CrmsImportSetup crmsImportSetup = (CrmsImportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
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
