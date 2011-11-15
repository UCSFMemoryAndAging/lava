package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;

public class ProtocolTimepointHandler extends CrmsEntityComponentHandler {

	public ProtocolTimepointHandler() {
		super();
	}

	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) return backingObjects;

		ProtocolTimepoint patientProtocolTimepoint = (ProtocolTimepoint) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("timepointId", patientProtocolTimepoint.getId()));
		ProtocolTracking protocolTimepointTree = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.timepointTree", filter);
		backingObjects.put("protocolTimepointTree", protocolTimepointTree);
		return backingObjects;
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepoint timepoint = (ProtocolTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		model.put("protocolConfigLabel", timepoint.getProtocol().getProtocolConfig().getLabel());
		return super.addReferenceData(context, command, errors, model);
	}
	
}
