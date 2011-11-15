package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentOptionConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;

public class ProtocolInstrumentOptionConfigHandler extends CrmsEntityComponentHandler {

	public ProtocolInstrumentOptionConfigHandler() {
		super();
		setHandledEntity("protocolInstrumentOptionConfig", ProtocolInstrumentOptionConfig.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label"});
	    return getRequiredFields();
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		ProtocolInstrumentOptionConfig instrumentOptionConfig = (ProtocolInstrumentOptionConfig) command;
		ProtocolInstrumentConfig instrumentConfig = (ProtocolInstrumentConfig) ProtocolInstrumentConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		// the collection association between ProtocolInstrumentConfig and ProtocolInstrumentOptionConfig need only be unidirectional
		// but was made bidirectional so that ProtocolInstrumentConfig does not have to be persisted in this handler (which 
		// would require doSaveAdd retrieving ProtocolInstrumentConfig again to persist it). ProtocolInstrumentConfig
		// addOption manages both ends of the association, but only have to persist the collection end, ProtocolInstrumentOptionConfig
		instrumentConfig.addOption(instrumentOptionConfig);
		instrumentOptionConfig.setProjName(instrumentConfig.getProjName());
		return command;
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrumentOptionConfig instrumentOptionConfig = (ProtocolInstrumentOptionConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for collectWinAnchorVisit, list of Visits belonging to this Timepoint
		
		//TODO: create list. see same list in ProtocolAssessmentTimepointHandler
		

		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	protected void handleCustomCollectWinAnchorVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrumentOptionConfig instrumentOptionConfig = (ProtocolInstrumentOptionConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(instrumentOptionConfig.getCustomCollectWinAnchorVisitId(),instrumentOptionConfig.getCustomCollectWinAnchorVisit())){
			if(instrumentOptionConfig.getCustomCollectWinAnchorVisitId()==null){
				instrumentOptionConfig.setCustomCollectWinAnchorVisit(null); 	//clear the association
			}else{
				ProtocolVisitConfig customCollectWinAnchorVisit = (ProtocolVisitConfig) ProtocolVisitConfig.MANAGER.getById(instrumentOptionConfig.getCustomCollectWinAnchorVisitId());
				instrumentOptionConfig.setCustomCollectWinAnchorVisit(customCollectWinAnchorVisit);
			}
		}
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
