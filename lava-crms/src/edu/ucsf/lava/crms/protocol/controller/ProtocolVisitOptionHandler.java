package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.project.model.Project;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitOption;

public class ProtocolVisitOptionHandler extends CrmsEntityComponentHandler {

	public ProtocolVisitOptionHandler() {
		super();
		setHandledEntity("protocolVisitOption", edu.ucsf.lava.crms.protocol.model.ProtocolVisitOption.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label"});
	    return getRequiredFields();
	}
	
	public Map getBackingObjects(RequestContext context, Map components) {
		// facilitates allowing user to specify visitType from project other than the project for
		// which the protocol is defined, so that patients that are co-enrolled in two or more 
		// projects will be accommodated
		// note: do not put this object in handledObjects as it is not a persistent object. only put
		// it in components so that binding works
		components.put("altProject", new Project());
		return super.getBackingObjects(context, components);
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		ProtocolVisitOption protocolVisitOption = (ProtocolVisitOption) command;
		ProtocolVisit protocolVisit = (ProtocolVisit) ProtocolVisit.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		// the collection association between ProtocolVisit and ProtocolVisitOption need only be unidirectional
		// but was made bidirectional so that the ProtocolVisit does not have to be persisted in this handler (which 
		// would require doSaveAdd retrieving ProtocolVisit again to persist it). ProtocolVisit (actually ProtocolNode)
		// addOption manages both ends of the association, but only have to persist the collection end, ProtocolVisitOption
		protocolVisit.addOption(protocolVisitOption);
		protocolVisitOption.setProjName(protocolVisit.getProjName());
		return command;
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolVisitOption protocolVisitOption = (ProtocolVisitOption)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Project altProject = (Project)((ComponentCommand)command).getComponents().get("altProject");

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		dynamicLists.put("context.projectList", listManager.getDynamicList(getCurrentUser(request), "context.projectList"));
		
		// if user has not specified a project for listing project visitTypes, use the project 
		// of this protocol 
		if (altProject.getProject() == null) {
			altProject.setProject(protocolVisitOption.getProjName());
		}
		dynamicLists.put("visit.visitTypes", listManager.getDynamicList("visit.visitTypes", 
			"projectName", altProject.getProject(), String.class));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
}
