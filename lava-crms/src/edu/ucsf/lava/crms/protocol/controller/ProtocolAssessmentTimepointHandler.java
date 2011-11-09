package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.crms.protocol.model.ProtocolAssessmentTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointBase;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;

public class ProtocolAssessmentTimepointHandler extends ProtocolTimepointHandler {

	public ProtocolAssessmentTimepointHandler() {
		super();
		setHandledEntity("protocolAssessmentTimepoint", ProtocolAssessmentTimepoint.class);
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
		
		// list for collectWinAnchorVisit
		//    need list of Visits belonging to this Timepoint (which of course, do not exist when this Timepoint is 
		//    just being added - believe there are some notes addressing this (create Timepoint and Visits together??)
		ProtocolTimepointBase protocolTimepoint = (ProtocolTimepointBase)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Map collectWinAnchorVisitList = listManager.getDynamicList(getCurrentUser(request),"protocol.collectWinAnchorVisit", 
			new String[]{"timepointId"}, 
			new Object[]{flowMode.equals("add") ? -1 : protocolTimepoint.getId()},
			new Class[]{Long.class});		
		dynamicLists.put("protocol.collectWinAnchorVisit", collectWinAnchorVisitList);		
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	

	protected void handleCollectWinAnchorVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolAssessmentTimepoint protocolTimepoint = (ProtocolAssessmentTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(protocolTimepoint.getCollectWinAnchorVisitId(),protocolTimepoint.getCollectWinAnchorVisit())){
			if(protocolTimepoint.getCollectWinAnchorVisitId()==null){
				protocolTimepoint.setCollectWinAnchorVisit(null); 	//clear the association
			}else{
				ProtocolVisit collectWinAnchorVisit = (ProtocolVisit) ProtocolVisit.MANAGER.getById(protocolTimepoint.getCollectWinAnchorVisitId());
				protocolTimepoint.setCollectWinAnchorVisit(collectWinAnchorVisit);
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
