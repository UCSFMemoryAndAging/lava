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
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrument;
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrumentOption;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;

public class ProtocolInstrumentOptionHandler extends CrmsEntityComponentHandler {

	public ProtocolInstrumentOptionHandler() {
		super();
		setHandledEntity("protocolInstrumentOption", ProtocolInstrumentOption.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{"label"});
	    return getRequiredFields();
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		ProtocolInstrumentOption protocolInstrumentOption = (ProtocolInstrumentOption) command;
		ProtocolInstrument protocolInstrument = (ProtocolInstrument) ProtocolInstrument.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		// the collection association between ProtocolInstrument and ProtocolInstrumentOption need only be unidirectional
		// but was made bidirectional so that the ProtocolInstrument does not have to be persisted in this handler (which 
		// would require doSaveAdd retrieving ProtocolInstrument again to persist it). ProtocolInstrument (actually ProtocolNode)
		// addOption manages both ends of the association, but only have to persist the collection end, ProtocolInstrumentOption
		protocolInstrument.addOption(protocolInstrumentOption);
		protocolInstrumentOption.setProjName(protocolInstrument.getProjName());
		return command;
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrumentOption protocolInstrumentOption = (ProtocolInstrumentOption)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for collectWinAnchorVisit, list of Visits belonging to this Timepoint
		
		//TODO: create list. see same list in ProtocolAssessmentTimepointHandler
		

		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	protected void handleCustomCollectWinAnchorVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrumentOption protocolInstrumentOption = (ProtocolInstrumentOption)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(protocolInstrumentOption.getCustomCollectWinAnchorVisitId(),protocolInstrumentOption.getCustomCollectWinAnchorVisit())){
			if(protocolInstrumentOption.getCustomCollectWinAnchorVisitId()==null){
				protocolInstrumentOption.setCustomCollectWinAnchorVisit(null); 	//clear the association
			}else{
				ProtocolVisit customCollectWinAnchorVisit = (ProtocolVisit) ProtocolVisit.MANAGER.getById(protocolInstrumentOption.getCustomCollectWinAnchorVisitId());
				protocolInstrumentOption.setCustomCollectWinAnchorVisit(customCollectWinAnchorVisit);
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
