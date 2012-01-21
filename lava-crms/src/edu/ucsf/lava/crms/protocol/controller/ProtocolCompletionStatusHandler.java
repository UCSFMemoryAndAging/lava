package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

/** 
 * This is a "view" mode of the Protocol, with a tree that shows completion statuses. The properties
 * are not editable. It is intended for drilling down from a list of Protocols showing overall
 * Protocol completion status (currStatus), to view the statuses of individual components of the 
 * Protocol. 
 * 
 * @author ctoohey
 *
 */
public class ProtocolCompletionStatusHandler extends CrmsEntityComponentHandler {
	public ProtocolCompletionStatusHandler() {
		super();
		setHandledEntity("protocolCompletionStatus", edu.ucsf.lava.crms.protocol.model.Protocol.class);
	}

	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		// retrieve the entire patientProtocol hierarchy tree for display in patientProtocol View mode
		Protocol protocol = (Protocol) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("protocolId", protocol.getId()));
		ProtocolTracking protocolTree = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.protocolTree", filter);
		backingObjects.put("protocolTree", protocolTree);
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		// do not init project to current project; the protocol that is selected will determine the project
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Protocol protocol = (Protocol) command;
		
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		if (p != null){
			protocol.setPatient(p);
		}
		return command;
	}
	
}
