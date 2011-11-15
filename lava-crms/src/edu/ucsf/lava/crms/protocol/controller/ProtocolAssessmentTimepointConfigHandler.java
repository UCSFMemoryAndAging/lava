package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;

public class ProtocolAssessmentTimepointConfigHandler extends ProtocolTimepointConfigHandler {

	public ProtocolAssessmentTimepointConfigHandler() {
		super();
		setHandledEntity("protocolAssessmentTimepointConfig", ProtocolAssessmentTimepointConfig.class);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
		// want to force user to create a complete protocol definition so that when a patient is
		// enrolled do not have to be concerned with things that are not defined
		
	    setRequiredFields(new String[]{"label",
// TODO: not clear yet if these should be required or not	    		
	    		//"collectAnchorNode","collectWindowType","collectWindowDays","collectWindowOffset"
	    		});
	    return getRequiredFields();
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for collectWinAnchorVisit, list of Visits belonging to this Timepoint 
		// TODO: given that no visits exist when this Timepoint is just being added, need an approach for this. wizard? right now,
		// user needs to add instrument, add visit(s), go back to instrument
		
		ProtocolTimepointConfig timepointConfig = (ProtocolTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Map collectWinAnchorVisitList = listManager.getDynamicList(getCurrentUser(request),"protocol.collectWinAnchorVisit", 
			new String[]{"timepointConfigId"}, 
			new Object[]{flowMode.equals("add") ? -1 : timepointConfig.getId()},
			new Class[]{Long.class});		
		dynamicLists.put("protocol.collectWinAnchorVisit", collectWinAnchorVisitList);		
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	

	protected void handleCollectWinAnchorVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolAssessmentTimepointConfig timepointConfig = (ProtocolAssessmentTimepointConfig)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(timepointConfig.getCollectWinAnchorVisitId(),timepointConfig.getCollectWinAnchorVisit())){
			if(timepointConfig.getCollectWinAnchorVisitId()==null){
				timepointConfig.setCollectWinAnchorVisit(null); 	//clear the association
			}else{
				ProtocolVisitConfig collectWinAnchorVisit = (ProtocolVisitConfig) ProtocolVisitConfig.MANAGER.getById(timepointConfig.getCollectWinAnchorVisitId());
				timepointConfig.setCollectWinAnchorVisit(collectWinAnchorVisit);
			}
		}
	}

	
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleCollectWinAnchorVisitChange(context, command, errors);
		return super.doSaveAdd(context, command, errors);
	}

	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handleCollectWinAnchorVisitChange(context, command, errors);
		return super.doSave(context, command, errors);
	}
	

	
	
}
