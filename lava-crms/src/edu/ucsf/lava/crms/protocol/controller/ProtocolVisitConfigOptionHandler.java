package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfigOption;

public class ProtocolVisitConfigOptionHandler extends CrmsEntityComponentHandler {

	public ProtocolVisitConfigOptionHandler() {
		super();
		setHandledEntity("protocolVisitConfigOption", edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfigOption.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label","visitTypeProjName","visitType"});
	    return getRequiredFields();
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		ProtocolVisitConfigOption visitOptionConfig = (ProtocolVisitConfigOption) command;
		ProtocolVisitConfig visitConfig = (ProtocolVisitConfig) ProtocolVisitConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		// the collection association between ProtocolVisitConfig and ProtocolVisitOptionConfig need only be unidirectional
		// but was made bidirectional so that the ProtocolVisitConfig does not have to be persisted in this handler (which 
		// would require doSaveAdd retrieving ProtocolVisitConfig again to persist it). ProtocolVisitConfig
		// addOption manages both ends of the association, but only have to persist the collection end, ProtocolVisitOptionConfig
		visitConfig.addOption(visitOptionConfig);
		visitOptionConfig.setProjName(visitConfig.getProjName());
		visitOptionConfig.setProjName(visitConfig.getProjName());
		
		// the visit option has its own projName to allow visits to fulfill the protocol that are associated with a project
		// other than the protocol's project. this facilitates protocol subjects that are co-enrolled in two or more projects
		// where visits from any of the projects could be used to fulfill the protocol for that subject.
		// default this to the protocol's project
		visitOptionConfig.setProjName(visitConfig.getProjName());
		return command;
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolVisitConfigOption visitOptionConfig = (ProtocolVisitConfigOption)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		dynamicLists.put("context.projectList", listManager.getDynamicList(getCurrentUser(request), "context.projectList"));
		
		// if user has not specified a project for listing project visitTypes, use the project 
		// of this protocol (note: this should generally not be necessary since the visitTypeProjName is 
		// defaulted to the protocol's project at creation, but the user could have set it to blank)
		if (visitOptionConfig.getProjName() == null) {
			visitOptionConfig.setProjName(visitOptionConfig.getProjName());
		}
		dynamicLists.put("visit.visitTypes", listManager.getDynamicList("visit.visitTypes", 
			"projectName", visitOptionConfig.getProjName(), String.class));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
}
