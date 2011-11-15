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
import edu.ucsf.lava.crms.protocol.model.ProtocolConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTrackingConfig;

public class ProtocolTimepointConfigHandler extends CrmsEntityComponentHandler {

	public ProtocolTimepointConfigHandler() {
		super();
		setHandledEntity("timepointConfig", edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig.class);
	}

	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) return backingObjects;

		ProtocolTimepointConfig timepointConfig = (ProtocolTimepointConfig) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("timepointConfigId", timepointConfig.getId()));
		ProtocolTrackingConfig timepointConfigTree = (ProtocolTrackingConfig) EntityBase.MANAGER.findOneByNamedQuery("protocol.timepointConfigTree", filter);
		
		backingObjects.put("timepointConfigTree", timepointConfigTree);
		
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepointConfig protocolTimepoint = (ProtocolTimepointConfig) command;
		
		ProtocolConfig protocol = (ProtocolConfig) ProtocolConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
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
		// a timepoint config has a scheduling window which can be relative to any other timepoint (unless it is flagged as
		// the first timepoint). generate a list of all of the timepoint configs belonging to the same protocol config as 
		// this timepoint config (excluding this timepoint config, since a timepoint scheduling window would not relative to
		// itself) 
		ProtocolTimepointConfig timepointConfig = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Map schedWinAnchorTimepointList = listManager.getDynamicList(getCurrentUser(request),"protocol.schedWinAnchorTimepoint", 
			new String[]{"protocolId", "timepointId"}, 
			new Object[]{timepointConfig.getProtocolConfig().getId(), flowMode.equals("add") ? -1 : timepointConfig.getId()},
			new Class[]{Long.class, Long.class});		
		dynamicLists.put("protocol.schedWinAnchorTimepoint", schedWinAnchorTimepointList);		
		
		model.put("dynamicLists", dynamicLists);
		
		return super.addReferenceData(context, command, errors, model);
	}


	protected Event checkConditionalRequired(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepointConfig tp = ((ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName()));

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
		ProtocolTimepointConfig protocolTimepoint = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(protocolTimepoint.getSchedWinAnchorTimepointId(),protocolTimepoint.getSchedWinAnchorTimepoint())){
			if(protocolTimepoint.getSchedWinAnchorTimepointId()==null){
				protocolTimepoint.setSchedWinAnchorTimepoint(null); 	//clear the association
			}else{
				ProtocolTimepointConfig schedWinAnchorTimepoint = (ProtocolTimepointConfig) ProtocolTimepointConfig.MANAGER.getById(protocolTimepoint.getSchedWinAnchorTimepointId());
				protocolTimepoint.setSchedWinAnchorTimepoint(schedWinAnchorTimepoint);
			}
		}
	}
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		ProtocolTimepointConfig tp = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

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
			tp.getProtocolConfig().orderTimepoints();
			tp.getProtocolConfig().save();
		}
		
		return returnEvent;
	}

	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		ProtocolTimepointConfig tp = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		if (this.checkConditionalRequired(context, command, errors).equals(new Event(this,ERROR_FLOW_EVENT_ID))) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}

		this.handleSchedWinAnchorTimepointChange(context, command, errors);

		Event returnEvent = super.doSave(context, command, errors);
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			tp.getProtocolConfig().orderTimepoints();
			tp.getProtocolConfig().save();
		}
		return returnEvent;
	}
	
}
