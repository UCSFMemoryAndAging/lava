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
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;

public class ProtocolTimepointHandler extends CrmsEntityComponentHandler {

	public ProtocolTimepointHandler() {
		super();
		setHandledEntity("timepoint", edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint.class);
	}

	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) return backingObjects;

		ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("timepointId", protocolTimepoint.getId()));
		ProtocolTracking timepointTree = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.timepointTree", filter);
		
		backingObjects.put("timepointTree", timepointTree);
		
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint) command;
		
		Protocol protocol = (Protocol) Protocol.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		protocol.addTimepoint(protocolTimepoint);
		protocolTimepoint.setProjName(protocol.getProjName());
		
		return command;
	}

	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for schedAnchorNode
		// a timepoint scheduling can only be relative to timepoints that precede it in the protocol,
		// so retrieve a list of those timepoints. if this is a new timepoint, then all existing timepoints
		// should be in the list
		ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Map schedWinAnchorTimepointList = listManager.getDynamicList(getCurrentUser(request),"protocol.schedWinAnchorTimepoint", 
			new String[]{"protocolId", "timepointId"}, 
			new Object[]{protocolTimepoint.getProtocol().getId(), flowMode.equals("add") ? -1 : protocolTimepoint.getId()},
			new Class[]{Long.class, Long.class});		
		dynamicLists.put("protocol.schedWinAnchorTimepoint", schedWinAnchorTimepointList);		
		
		model.put("dynamicLists", dynamicLists);
		
		return super.addReferenceData(context, command, errors, model);
	}


	protected Event checkConditionalRequired(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepoint tp = ((ProtocolTimepoint)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName()));

		// look at required fields. since they are conditionally determined, can not set them in the
		// standard way, since that takes place before binding, before properties have the values on
		// which conditional logic depends
		
		// cannot enforce not-null for the scheduling window properties because they will be null for the first timepoint.
		// however, do not want them to be null for subsequent problems as that will alleviate issues enforcing the
		// protocol over time and in particular will allow calculating a daysFromProtocolStart for every subsequent
		// timepoint, which is in turn used to order the timepoints
		
		if (!tp.getFirstTimepoint()) { 
			if (tp.getSchedWinAnchorTimepointId() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "schedWinAnchorTimepointId", getDefaultObjectName());
			}
			if (tp.getSchedWinDaysFromAnchor() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "schedWinDaysFromAnchor", getDefaultObjectName());
			}
			if (tp.getSchedWinSize() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "schedWinSize", getDefaultObjectName());
			}
			if (tp.getSchedWinOffset() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "schedWinOffset", getDefaultObjectName());
			}
		}
		
		if (errors.hasFieldErrors()) {
			LavaComponentFormAction.createCommandErrorsForFieldErrors(errors);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}

		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	
	protected void handleSchedWinAnchorTimepointChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(protocolTimepoint.getSchedWinAnchorTimepointId(),protocolTimepoint.getSchedWinAnchorTimepoint())){
			if(protocolTimepoint.getSchedWinAnchorTimepointId()==null){
				protocolTimepoint.setSchedWinAnchorTimepoint(null); 	//clear the association
			}else{
				ProtocolTimepoint schedWinAnchorTimepoint = (ProtocolTimepoint) ProtocolTimepoint.MANAGER.getById(protocolTimepoint.getSchedWinAnchorTimepointId());
				protocolTimepoint.setSchedWinAnchorTimepoint(schedWinAnchorTimepoint);
			}
		}
	}
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		ProtocolTimepoint tp = (ProtocolTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		// if firstTimepoint null, explicitly set to 0 so don't have to check for null
		if (tp.getFirstTimepoint() == null) {
			tp.setFirstTimepoint(Boolean.FALSE);
		}
		
		if (this.checkConditionalRequired(context, command, errors).getId().equals(this.ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		this.handleSchedWinAnchorTimepointChange(context, command, errors);
		
		Event returnEvent = super.doSaveAdd(context, command, errors);
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			tp.getProtocol().orderTimepoints();
			tp.getProtocol().save();
		}
		
		return returnEvent;
	}

	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		ProtocolTimepoint tp = (ProtocolTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		if (this.checkConditionalRequired(context, command, errors).equals(new Event(this,ERROR_FLOW_EVENT_ID))) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		this.handleSchedWinAnchorTimepointChange(context, command, errors);

		Event returnEvent = super.doSave(context, command, errors);
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			tp.getProtocol().orderTimepoints();
			tp.getProtocol().save();
		}
		return returnEvent;
	}
	
}
