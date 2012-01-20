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
import edu.ucsf.lava.crms.protocol.model.ProtocolConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolConfigTracking;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProtocolConfigHandler extends CrmsEntityComponentHandler {
	protected static final String DELETE_PROTOCOL_WITH_PATIENTS_ERROR_CODE = "protocol.deleteHasEnrolledPatients.command";

	public ProtocolConfigHandler() {
		super();
		setHandledEntity("protocolConfig", edu.ucsf.lava.crms.protocol.model.ProtocolConfig.class);
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

		// retrieve the entire protocolConfig hierarchy tree for display in protocolConfig View mode
		ProtocolConfig protocolConfig = (ProtocolConfig) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("protocolConfigId", protocolConfig.getId()));
		ProtocolConfigTracking protocolConfigTree = (ProtocolConfigTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.protocolConfigTree", filter);
		backingObjects.put("protocolConfigTree", protocolConfigTree);
		
		return backingObjects;
	}
	

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolConfig protocolConfig = (ProtocolConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put("context.projectList", 
			listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request), "context.projectList"));

		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		if (!flowMode.equals("add")) {
			// list for selecting the first ProtocolTimepointConfig, so retrieve all ProtocolTimepointConfigs for this ProtocolConfig
			Map allTimepointConfigsList = listManager.getDynamicList(getCurrentUser(request),"protocol.allTimepointConfigs", 
					new String[]{"protocolConfigId"}, 
					new Object[]{protocolConfig.getId()},
					new Class[]{Long.class});		
			dynamicLists.put("protocol.allTimepointConfigs", allTimepointConfigsList);
		}
		model.put("dynamicLists", dynamicLists);
		
		return super.addReferenceData(context, command, errors, model);
	}
	
	

	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolConfig protocolConfig = (ProtocolConfig) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
//TODO: WRITE THIS METHOD		
//		if(protocol.hasPatientsEnrolled()){
//			 CoreSessionUtils.addFormError(sessionManager,request, new String[]{DELETE_PROTOCOL_WITH_PATIENTS_ERROR_CODE}, null);
//			 return new Event(this,ERROR_FLOW_EVENT_ID);
//		}
		
		// call the superclass to delete this object
		return super.doConfirmDelete(context, command, errors);
	}
	
	
}
