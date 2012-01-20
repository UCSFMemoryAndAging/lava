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
import edu.ucsf.lava.crms.protocol.model.ProtocolConfigTracking;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentConfigOption;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;

public class ProtocolInstrumentConfigHandler extends CrmsEntityComponentHandler {

	public ProtocolInstrumentConfigHandler() {
		super();
		setHandledEntity("protocolInstrumentConfig", edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentConfig.class);
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

		ProtocolInstrumentConfig protocolInstrument = (ProtocolInstrumentConfig) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("instrumentConfigId", protocolInstrument.getId()));
		ProtocolConfigTracking instrumentOptionsConfig = (ProtocolConfigTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.instrumentConfigTree", filter);
		backingObjects.put("instrumentOptionsConfig", instrumentOptionsConfig);
		
		return backingObjects;
	}
	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrumentConfig instrumentConfig = (ProtocolInstrumentConfig) command;
		ProtocolVisitConfig visitConfig = (ProtocolVisitConfig) ProtocolVisitConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		visitConfig.addProtocolInstrumentConfig(instrumentConfig);
		instrumentConfig.setProjName(visitConfig.getProjName());
		
		// create the default option
		ProtocolInstrumentConfigOption instrOptionConfig = new ProtocolInstrumentConfigOption();
		//TODO: set as the defaultOption on this ProtocolInstrumentConfig
		instrOptionConfig.setProjName(visitConfig.getProjName());
		instrumentConfig.addOption(instrOptionConfig);
		
		return command;
	}
	
	protected void handleCustomCollectWinAnchorVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrumentConfig instrumentConfig = (ProtocolInstrumentConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(instrumentConfig.getCustomCollectWinProtocolVisitConfigId(),instrumentConfig.getCustomCollectWinProtocolVisitConfig())){
			if(instrumentConfig.getCustomCollectWinProtocolVisitConfigId()==null){
				instrumentConfig.setCustomCollectWinProtocolVisitConfig(null); 	//clear the association
			}else{
				ProtocolVisitConfig customCollectWinAnchorVisit = (ProtocolVisitConfig) ProtocolVisitConfig.MANAGER.getById(instrumentConfig.getCustomCollectWinProtocolVisitConfigId());
				instrumentConfig.setCustomCollectWinProtocolVisitConfig(customCollectWinAnchorVisit);
			}
		}
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolInstrumentConfig instrumentConfig = (ProtocolInstrumentConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for collectWinAnchorVisit, list of ProtocolVisitConfigs belonging to this ProtocolTimepointConfig (use 
		// same list that ProtocolTimepointConfigHandler uses for primaryProtocolVisitConfig
		Map primaryProtocolVisitConfigList = listManager.getDynamicList(getCurrentUser(request),"protocol.primaryProtocolVisitConfig", 
				new String[]{"timepointConfigId"}, 
				new Object[]{instrumentConfig.getProtocolVisitConfig().getProtocolTimepointConfig().getId()},
				new Class[]{Long.class});		
			dynamicLists.put("protocol.primaryProtocolVisitConfig", primaryProtocolVisitConfigList);	

		model.put("dynamicLists", dynamicLists);
		
		return super.addReferenceData(context, command, errors, model);
	}
	
	
	protected Event checkConditionalRequired(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolInstrumentConfig instrumentConfig = ((ProtocolInstrumentConfig)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName()));

		// look at required fields. since whether they are required or not is conditional based on user input, 
		// can not configure them in the standard way for required fields, since that takes place before binding, 
		// before properties have the values on which conditional logic depends
		
		if (instrumentConfig.getCustomCollectWinDefined()) {
			if (instrumentConfig.getCustomCollectWinProtocolVisitConfigId() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "customCollectWinProtocolVisitConfigId", getDefaultObjectName());
			}
			if (instrumentConfig.getCustomCollectWinOffset() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "customCollectWinOffset", getDefaultObjectName());
			}
			if (instrumentConfig.getCustomCollectWinSize() == null) {
				LavaComponentFormAction.createRequiredFieldError(errors, "customCollectWinSize", getDefaultObjectName());
			}
		}
		
		if (errors.hasFieldErrors()) {
			LavaComponentFormAction.createCommandErrorsForFieldErrors(errors);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}

		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		ProtocolInstrumentConfig instrConfig = (ProtocolInstrumentConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		ProtocolInstrumentConfigOption instrOptionConfig = instrConfig.getOptions().iterator().next();

		if (this.checkConditionalRequired(context, command, errors).getId().equals(this.ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		// because the built-in mechanism for required field checks does not work for properties in a detail record,
		// check required fields here. this is only a required field on Add Protocol Instrument Config, so it is
		// separate from checkConditionalRequired
		if (instrOptionConfig.getInstrType() == null) {
			LavaComponentFormAction.createRequiredFieldError(errors, "options[0].instrType", getDefaultObjectName());
		}		
		if (errors.hasFieldErrors()) {
			LavaComponentFormAction.createCommandErrorsForFieldErrors(errors);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		// finish off configuration of auto-added initial instrument option (the label is named after the instrType)
		instrOptionConfig.setLabel(instrOptionConfig.getInstrType());
		
		this.handleCustomCollectWinAnchorVisitChange(context, command, errors);

		Event returnEvent = super.doSaveAdd(context, command, errors);
		return returnEvent;
	}


	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		if (this.checkConditionalRequired(context, command, errors).getId().equals(this.ERROR_FLOW_EVENT_ID)) {
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		this.handleCustomCollectWinAnchorVisitChange(context, command, errors);
		return super.doSave(context, command, errors);
	}

}
