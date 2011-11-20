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
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTrackingConfig;
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
		ProtocolTrackingConfig instrumentOptionsConfig = (ProtocolTrackingConfig) EntityBase.MANAGER.findOneByNamedQuery("protocol.instrumentConfigTree", filter);
		backingObjects.put("instrumentOptionsConfig", instrumentOptionsConfig);
		
		return backingObjects;
	}
	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrumentConfig instrumentConfig = (ProtocolInstrumentConfig) command;
		ProtocolVisitConfig visitConfig = (ProtocolVisitConfig) ProtocolVisitConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		visitConfig.addProtocolInstrumentConfig(instrumentConfig);
		instrumentConfig.setProjName(visitConfig.getProjName());
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
	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleCustomCollectWinAnchorVisitChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}

	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleCustomCollectWinAnchorVisitChange(context, command, errors);
		return super.doSave(context, command, errors);
	}

}
