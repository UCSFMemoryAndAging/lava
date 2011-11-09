package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProtocolHandler extends CrmsEntityComponentHandler {
	protected static final String DELETE_PROTOCOL_WITH_PATIENTS_ERROR_CODE = "protocol.deleteHasEnrolledPatients.command";

	public ProtocolHandler() {
		super();
		setHandledEntity("protocol", edu.ucsf.lava.crms.protocol.model.Protocol.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label","projName"});
	    return getRequiredFields();
	}
	
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) {return backingObjects;}

		// retrieve the entire protocol hierarchy tree, a List<ProtocolNode> for display in 
		// protocol View mode
		Protocol protocol = (Protocol) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("protocolId", protocol.getId()));
		ProtocolTracking protocolTree = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.protocolTree", filter);
		backingObjects.put("protocolTree", protocolTree);
		
		return backingObjects;
	}
	

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Protocol protocol = (Protocol)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put("context.projectList", 
			listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request), "context.projectList"));
		
		return super.addReferenceData(context, command, errors, model);
	}
	
	

	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Protocol protocol = (Protocol) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
//TODO: WRITE THIS METHOD		
//		if(protocol.hasPatientsEnrolled()){
//			 CoreSessionUtils.addFormError(sessionManager,request, new String[]{DELETE_PROTOCOL_WITH_PATIENTS_ERROR_CODE}, null);
//			 return new Event(this,ERROR_FLOW_EVENT_ID);
//		}
		
		// call the superclass to delete this object
		return super.doConfirmDelete(context, command, errors);
	}
	
	
}
