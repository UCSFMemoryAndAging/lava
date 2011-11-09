package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.PatientProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;

public class PatientProtocolTimepointHandler extends CrmsEntityComponentHandler {

	public PatientProtocolTimepointHandler() {
		super();
	}

	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) return backingObjects;

		PatientProtocolTimepoint patientProtocolTimepoint = (PatientProtocolTimepoint) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("timepointId", patientProtocolTimepoint.getId()));
		PatientProtocolTracking patientProtocolTimepointTree = (PatientProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("patientProtocol.timepointTree", filter);
		backingObjects.put("patientProtocolTimepointTree", patientProtocolTimepointTree);
		return backingObjects;
	}
	
}
