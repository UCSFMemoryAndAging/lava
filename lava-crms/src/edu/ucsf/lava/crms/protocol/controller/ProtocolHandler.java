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
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProtocolHandler extends CrmsEntityComponentHandler {
	public ProtocolHandler() {
		super();
		setHandledEntity("protocol", edu.ucsf.lava.crms.protocol.model.Protocol.class);
		CrmsSessionUtils.setIsPatientContext(this);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
		setRequiredFields(new String[]{"assignedDate"});
		return getRequiredFields();
	}
	
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		if (flowMode.equals("add")) return backingObjects;

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

	protected Long getContextIdFromRequest(RequestContext context){		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Protocol protocol = (Protocol) Protocol.MANAGER.getOne(getFilterWithId(request,Long.valueOf(context.getFlowScope().getString("id"))));
		return protocol.getPatient().getId();
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Protocol protocol = (Protocol)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		
		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		Map allProtocols = listManager.getDynamicList(getCurrentUser(request),"protocol.allProtocolConfigs");
		dynamicLists.put("protocol.allProtocolConfigs", allProtocols);
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Protocol protocol = (Protocol)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Patient patient = protocol.getPatient();
//		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);

		// retrieve the ProtocolConfig and create a parallel structure, i.e. Protocol associatd with the patient,
		// for tracking the patients status in the protocol
		// note: patientProtocol.protocolId is populated directly from view based on user input
		
		// use a named query to retrieve the entire Protocol tree, as retrieving it via criteria query (getOne)
		// will only initialize the timepoints collection, but nothing below that. the HQL named query eagerly
		// fetches every level of the protocol tree (except the options since those are not needed here)
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("protocolConfigId", protocol.getProtocolConfigId()));
		ProtocolConfig protocolConfig = (ProtocolConfig) EntityBase.MANAGER.findOneByNamedQuery("protocol.completeProtocolConfigTree", filter);
		
		// for now a protocol is only associated with one project, so that is the projName used for patientProtocol
		String projName = protocolConfig.getProjName();
		
		// patientProtocol already has the patient set
		protocol.setProtocolConfig(protocolConfig);
		protocol.setNodeType(protocolConfig.getNodeType());
		protocol.setProjName(protocolConfig.getProjName());
		
		for (ProtocolTimepointConfig timepointConfig: protocolConfig.getProtocolTimepointConfigs()) {
			// setup to handle repeating timepoints
			// if this is a repeating timepoint, create the specified initial number
			int numRepeatingTimepoints = 1;
			if (timepointConfig.getRepeating() != null && timepointConfig.getRepeating()) {
				numRepeatingTimepoints = timepointConfig.getRepeatInitialNum();
			}
			
			for (short i=1; i <= numRepeatingTimepoints; i++) {
				ProtocolTimepoint protocolTimepoint = null;
				if (timepointConfig.getRepeating() != null && timepointConfig.getRepeating()) {
					protocolTimepoint = ProtocolTimepoint.createRepeatingTimepointFromConfig(protocol, timepointConfig, i);
				}
				else {
					protocolTimepoint = ProtocolTimepoint.createTimepointFromConfig(protocol, timepointConfig);
				}
				
				// add the timepoint to the protocol for the patient
				protocol.addProtocolTimepoint(protocolTimepoint);
			}			
		}
//		protocol.orderTimepoints();
	
		// as this is a non-standard add, i.e. not adding via an add view which will return to a list view,
		// do not need to do the standard doSaveAdd, and in fact the refresh that is done has caused problems
		// because for some reason in some cases the ProtocolTimepoint ProtocolTimepointConfig is set null 
		// as a result of the refresh and this causes a NullPointerException in ProtocolTimepoint compareTo
		// so skip that and just to the save part
		//return saveHandledObjects(context, ((ComponentCommand)command).getComponents(), errors);
		return super.doSaveAdd(context, command, errors);
	}
	
	
}
