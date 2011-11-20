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
import edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTrackingConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;

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
		
		ProtocolConfig protocolConfig = (ProtocolConfig) ProtocolConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		protocolConfig.addProtocolTimepointConfig(protocolTimepoint);
		protocolTimepoint.setProjName(protocolConfig.getProjName());
		
		return command;
	}

	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolTimepointConfig timepointConfig = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for schedWinRelativeTimepoint
		// a timepoint config has a scheduling window which can be relative to any other timepoint (unless it is flagged as
		// the first timepoint). generate a list of all of the timepoint configs belonging to the same protocol config as 
		// this timepoint config (excluding this timepoint config, since a timepoint scheduling window would not relative to
		// itself) 
		Map schedWinRelativeTimepointList = listManager.getDynamicList(getCurrentUser(request),"protocol.schedWinRelativeTimepoint", 
			new String[]{"protocolId", "timepointId"}, 
			new Object[]{timepointConfig.getProtocolConfig().getId(), flowMode.equals("add") ? -1 : timepointConfig.getId()},
			new Class[]{Long.class, Long.class});		
		dynamicLists.put("protocol.schedWinRelativeTimepoint", schedWinRelativeTimepointList);
		

		// list for primaryProtocolVisitConfig, list of ProtocolVisitConfigs belonging to this 
		// ProtocolTimepointConfig 
		// TODO: given that no visits exist when this Timepoint is just being added, need an approach 
		// for this. wizard? adding VisitConfig (and InstrumentConfig) when a TimepointConfig is added?
		// UPDATE: Yes, Add Timepoint should use a DTO that accepts ProjName/VisitType and InstrType input
		// and use those to build out the ProtocolVisitConfig/ProtocolVisitOptionConfig, and 
		// ProtocolInstrumentConfig/ProtocolInstrumentOptionConfig
		Map primaryProtocolVisitConfigList = listManager.getDynamicList(getCurrentUser(request),"protocol.primaryProtocolVisitConfig", 
			new String[]{"timepointConfigId"}, 
			new Object[]{flowMode.equals("add") ? -1 : timepointConfig.getId()},
			new Class[]{Long.class});		
		dynamicLists.put("protocol.primaryProtocolVisitConfig", primaryProtocolVisitConfigList);	

		model.put("dynamicLists", dynamicLists);
		
		// need to flag the view as to whether this is marked as the first ProtocolTimepointConfig
		model.put("firstTimepointFlag", isFirstProtocolTimepointConfig(timepointConfig, flowMode));
		
		return super.addReferenceData(context, command, errors, model);
	}

	/**
	 * Helper method to determine if the ProtocolTimepointConfig is marked as the first timepoint config.
	 * 
	 * @param timepointConfig
	 * @param flowMode
	 * @return
	 */
	protected Boolean isFirstProtocolTimepointConfig(ProtocolTimepointConfig timepointConfig, String flowMode) {
		// if adding, the new ProtocolTimepointConfig does not have an id yet since hasn't been saved, so can
		// not set it as the first timepoint config on ProtocolConfig, so can not just call 
		// isFirstProtocolTimepointConfig here
		
		// if there is just one ProtocolTimepointConfig in ProtocolConfig's collection, return true, as the
		// business rule is if there is only one timepoint config it must be marked as the first (this
		// covers whether editing existing or adding new, because if adding, the ProtocolTimepointConfig has 
		// already been added to the ProtocolConfig collection in initializeNewCommandInstance)
		if (timepointConfig.getProtocolConfig().getProtocolTimepointConfigs().size() == 1) {
			return Boolean.TRUE;
		}
		// if there is more than one, then if adding, this can not be marked as the first timepoint yet since
		// it does not exist yet (has not been saved). if editing existing, then call the standard method
		// to determine if this is the first (it is guaranteed that one will be marked as first as long as
		// there is one or more in existence)
		else {
			if (flowMode.equals("add")) {
				return Boolean.FALSE;
			}
			else {
				return timepointConfig.isFirstProtocolTimepointConfig();
			}
		}
	}


	protected Event checkConditionalRequired(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolTimepointConfig timepointConfig = ((ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName()));

		// look at required fields. since they are conditionally determined, can not set them in the
		// standard way, since that takes place before binding, before properties have the values on
		// which conditional logic depends
		
		// cannot enforce not-null for the scheduling window properties because they will be null for the first timepoint.
		// however, do not want them to be null for subsequent problems as that will alleviate issues enforcing the
		// protocol over time and in particular will allow calculating a daysFromProtocolStart for every subsequent
		// timepoint, which is in turn used to order the timepoints
		
		if (!isFirstProtocolTimepointConfig(timepointConfig, flowMode)) { 
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
		ProtocolAssessmentTimepointConfig timepointConfig = (ProtocolAssessmentTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
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
		
//TODO: if mapping firstTimepoint as an association in ProtocolConfig works, then it is possible that
// on deleting a ProtocolTimepointConfig will take care of clearing that association before the delete
// need to check this.
		
// but if mapping as association does not work (because mapping an association to a collection item of the same entity)
// then definitely on deleting have to explictly set the ProtocolConfig firstTimepointId to null here to clear it		
		ProtocolTimepointConfig tp = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		
		return super.deleteHandledObjects(context, components, errors);
	}	
	
}
