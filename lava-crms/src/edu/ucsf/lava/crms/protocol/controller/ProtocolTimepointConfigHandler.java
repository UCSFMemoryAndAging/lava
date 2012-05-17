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
import edu.ucsf.lava.crms.protocol.model.ProtocolConfigTracking;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;

public class ProtocolTimepointConfigHandler extends CrmsEntityComponentHandler {

	public ProtocolTimepointConfigHandler() {
		super();
		setHandledEntity("protocolTimepointConfig", edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label","schedWinSize","schedWinOffset"});
	    return getRequiredFields();
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
		ProtocolConfigTracking timepointConfigTree = (ProtocolConfigTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.timepointConfigTree", filter);
		
		backingObjects.put("timepointConfigTree", timepointConfigTree);
		
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepointConfig protocolTimepointConfig = (ProtocolTimepointConfig) command;
		
		ProtocolConfig protocolConfig = (ProtocolConfig) ProtocolConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		protocolConfig.addProtocolTimepointConfig(protocolTimepointConfig);
		protocolTimepointConfig.setProjName(protocolConfig.getProjName());

		// if adding the first timepoint, the most likely settings for the scheduling window offset/size will be
		// 0 so default them (unless there are multiple visits in the first timepoint, these values do not apply,
		// but they are required fields)
		if (protocolTimepointConfig.isFirstProtocolTimepointConfig()) {
			protocolTimepointConfig.setSchedWinOffset((short)0);
			protocolTimepointConfig.setSchedWinSize((short)0);
		}
		return command;
	}

	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolTimepointConfig timepointConfig = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for schedWinRelativeTimepoint
		// a timepoint config has a scheduling window which can be relative to any other timepoint, except for the first
		// timepoint, which is not relative to anything. generate a list of all of the timepoint configs belonging to the 
		// same protocol config as this timepoint config (excluding this timepoint config, since a timepoint scheduling window 
		// would not relative to itself) 
		Map schedWinRelativeTimepointList = listManager.getDynamicList(getCurrentUser(request),"protocol.schedWinRelativeTimepoint", 
			new String[]{"protocolId", "timepointId"}, 
			new Object[]{timepointConfig.getProtocolConfig().getId(), flowMode.equals("add") ? -1 : timepointConfig.getId()},
			new Class[]{Long.class, Long.class});		
		dynamicLists.put("protocol.schedWinRelativeTimepoint", schedWinRelativeTimepointList);
		

		// list for primaryProtocolVisitConfig, list of ProtocolVisitConfigs belonging to this 
		// ProtocolTimepointConfig 
		Map primaryProtocolVisitConfigList = listManager.getDynamicList(getCurrentUser(request),"protocol.primaryProtocolVisitConfig", 
			new String[]{"timepointConfigId"}, 
			new Object[]{flowMode.equals("add") ? -1 : timepointConfig.getId()},
			new Class[]{Long.class});		
		dynamicLists.put("protocol.primaryProtocolVisitConfig", primaryProtocolVisitConfigList);	

		model.put("dynamicLists", dynamicLists);
		
		// need to flag the view as to whether this is marked as the first ProtocolTimepointConfig
		model.put("firstTimepointFlag", timepointConfig.isFirstProtocolTimepointConfig());
		
		return super.addReferenceData(context, command, errors, model);
	}


	protected Event checkConditionalRequired(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolTimepointConfig timepointConfig = ((ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName()));

		// look at required fields. since whether they are required or not is conditional based on user input, 
		// can not configure them in the standard way for required fields, since that takes place before binding, 
		// before properties have the values on which conditional logic depends
		
		// cannot enforce not-null for the scheduling window properties because they will be null for the first timepoint.
		// however, do not want them to be null for subsequent timepoints as that will alleviate issues enforcing the
		// protocol over time and in particular will allow calculating a daysFromProtocolStart for every subsequent
		// timepoint, which is in turn used to order the timepoints (also, in determining completion status need to 
		// compare the current time relative to a time window, which is either the scheduling window or a collection
		// window, and collection window is optional and may not be defined, so make sure that scheduling window is defined)
		if (!timepointConfig.isFirstProtocolTimepointConfig()) { 
			if (timepointConfig.getSchedWinRelativeTimepointId() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "schedWinRelativeTimepointId", getDefaultObjectName());
			}
			if (timepointConfig.getSchedWinRelativeAmount() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "schedWinRelativeAmount", getDefaultObjectName());
			}
			if (timepointConfig.getSchedWinRelativeUnits() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "schedWinRelativeUnits", getDefaultObjectName());
			}
		}
		
		if (timepointConfig.getCollectWindowDefined()) {
			if (timepointConfig.getCollectWinOffset() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "collectWinOffset", getDefaultObjectName());
			}
			if (timepointConfig.getCollectWinSize() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "collectWinSize", getDefaultObjectName());
			}
		}
		
		if (timepointConfig.getRepeating() != null && timepointConfig.getRepeating()) {
			if (timepointConfig.getRepeatInterval() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "repeatInterval", getDefaultObjectName());
			}
			if (timepointConfig.getRepeatInterval() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "repeatIntervalUnits", getDefaultObjectName());
			}
			if (timepointConfig.getRepeatInitialNum() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "repeatInitialNum", getDefaultObjectName());
			}
		}
		
		if (errors.hasFieldErrors()) {
			LavaComponentFormAction.createCommandErrorsForFieldErrors(errors);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}

		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	
	protected void handleSchedWinRelativeTimepointChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepointConfig protocolTimepoint = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(protocolTimepoint.getSchedWinRelativeTimepointId(),protocolTimepoint.getSchedWinRelativeTimepoint())){
			if(protocolTimepoint.getSchedWinRelativeTimepointId()==null){
				protocolTimepoint.setSchedWinRelativeTimepoint(null); 	//clear the association
			}else{
				ProtocolTimepointConfig schedWinRelativeTimepoint = (ProtocolTimepointConfig) ProtocolTimepointConfig.MANAGER.getById(protocolTimepoint.getSchedWinRelativeTimepointId());
				protocolTimepoint.setSchedWinRelativeTimepoint(schedWinRelativeTimepoint);
			}
		}
	}

	protected void handlePrimaryProtocolVisitConfigChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepointConfig timepointConfig = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(timepointConfig.getPrimaryProtocolVisitConfigId(),timepointConfig.getPrimaryProtocolVisitConfig())){
			if(timepointConfig.getPrimaryProtocolVisitConfigId()==null){
				timepointConfig.setPrimaryProtocolVisitConfig(null); 	//clear the association
			}else{
				ProtocolVisitConfig primaryProtocolVisitConfig = (ProtocolVisitConfig) ProtocolVisitConfig.MANAGER.getById(timepointConfig.getPrimaryProtocolVisitConfigId());
				timepointConfig.setPrimaryProtocolVisitConfig(primaryProtocolVisitConfig);
			}
		}
	}

	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		ProtocolTimepointConfig tp = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		if (this.checkConditionalRequired(context, command, errors).getId().equals(this.ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		this.handleSchedWinRelativeTimepointChange(context, command, errors);
		this.handlePrimaryProtocolVisitConfigChange(context, command, errors);
		
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

		this.handleSchedWinRelativeTimepointChange(context, command, errors);
		this.handlePrimaryProtocolVisitConfigChange(context, command, errors);

		Event returnEvent = super.doSave(context, command, errors);
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			tp.getProtocolConfig().orderTimepoints();
			tp.getProtocolConfig().save();
		}
		return returnEvent;
	}

	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception{
		Map components = ((ComponentCommand)command).getComponents();
		
		ProtocolTimepointConfig tp = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());		
		if (tp.isFirstProtocolTimepointConfig()) {
			tp.getProtocolConfig().setFirstProtocolTimepointConfig(null);
			tp.getProtocolConfig().save();
		}
		
		return super.deleteHandledObjects(context, components, errors);
	}	
	
}
