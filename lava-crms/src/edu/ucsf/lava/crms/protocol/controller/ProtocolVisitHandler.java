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
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;

public class ProtocolVisitHandler extends CrmsEntityComponentHandler {

	public ProtocolVisitHandler() {
		super();
		setHandledEntity("protocolVisit", edu.ucsf.lava.crms.protocol.model.ProtocolVisit.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label"});
	    return getRequiredFields();
	}
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) return backingObjects;

		ProtocolVisit protocolVisit = (ProtocolVisit) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("visitId", protocolVisit.getId()));
		ProtocolTracking visitTree = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.visitTree", filter);
		backingObjects.put("visitTree", visitTree);
				
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		// set the parent node parts for this new node, passed as request parameters. the 
		// visit part, i.e. nodeVisitPart, is set after saving and an auto-generated id 
		// from the database that can be used for the nodeVisitPart
		// note: using the auto-generated id facilitates multi-threaded usage
		ProtocolVisit protocolVisit = (ProtocolVisit) command;
		ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint) ProtocolTimepoint.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		protocolTimepoint.addVisit(protocolVisit);
		protocolVisit.setProjName(protocolTimepoint.getProjName());
		return command;
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolVisit protocolVisit = (ProtocolVisit) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
}
