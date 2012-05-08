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
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;

public class ProtocolTimepointHandler extends CrmsEntityComponentHandler {

	public ProtocolTimepointHandler() {
		super();
		setHandledEntity("protocolTimepoint", edu.ucsf.lava.crms.protocol.model.ProtocolTimepoint.class);
	}

	
	protected String[] defineRequiredFields(RequestContext context, Object command) {
	    setRequiredFields(new String[]{});
	    return getRequiredFields();
	}

	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		Map backingObjects = super.getBackingObjects(context, components);
		
		//do nothing on add
		if (flowMode.equals("add")) return backingObjects;

		ProtocolTimepoint patientProtocolTimepoint = (ProtocolTimepoint) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("timepointId", patientProtocolTimepoint.getId()));
		ProtocolTracking protocolTimepointTree = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.timepointTree", filter);
		backingObjects.put("protocolTimepointTree", protocolTimepointTree);
		
		return backingObjects;
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolTimepoint timepoint = (ProtocolTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		model.put("protocolConfigLabel", timepoint.getProtocol().getProtocolConfig().getLabel());
		
		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		// list for primaryProtocolVisit, list of ProtocolVisits belonging to this ProtocolTimepoint 
		Map primaryProtocolVisitList = listManager.getDynamicList(getCurrentUser(request),"protocol.primaryProtocolVisit", 
			new String[]{"timepointId"}, 
			new Object[]{timepoint.getId()},
			new Class[]{Long.class});		
		dynamicLists.put("protocol.primaryProtocolVisit", primaryProtocolVisitList);		
		
		// signal if this is the last timepoint of a chain of repeating timepoints so that user can manually
		// add more repeating timepoints
		if (timepoint.getProtocolTimepointConfig().getRepeating() != null && timepoint.getProtocolTimepointConfig().getRepeating()) {
			model.put("repeatingTimepoint", Boolean.TRUE);
			Short lastRepeatNum = 0;
			// get the repeatNum of the last timepoint in the chain
			for (ProtocolTimepoint protocolTimepoint : timepoint.getProtocol().getProtocolTimepoints()) {
				if (timepoint.getProtocolTimepointConfig().getId().equals(protocolTimepoint.getProtocolTimepointConfig().getId())) {
					if (protocolTimepoint.getRepeatNum() > lastRepeatNum) {
						lastRepeatNum = protocolTimepoint.getRepeatNum();
					}
				}
			}
			// see if this is the last timepoint
			if (timepoint.getRepeatNum().equals(lastRepeatNum)) {
				model.put("lastRepeatNum", 1);
			}
		}
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	protected Event doCreateRepeatingTimepoints(RequestContext context, Object command, BindingResult errors) throws Exception {
		ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if (protocolTimepoint.getNewRepeatNum() == null) {
			// conditional required field error (only required when adding repeating timepoints)
			LavaComponentFormAction.createRequiredFieldError(errors, "newRepeatNum", getDefaultObjectName());
			LavaComponentFormAction.createCommandErrorsForFieldErrors(errors);
			return new Event(this,ERROR_FLOW_EVENT_ID);		
		}
		else {
			if (!protocolTimepoint.nextRepeatingExists()) {
				// retrieve the entire ProtocolTimepointConfig tree as it will be needed in createTimepointFromConfig (could have these
				// retrieved in getAssociationsToInitialize but since only needed for adding repeating timepoints, not need to do it
				// always)
				LavaDaoFilter filter = EntityBase.newFilterInstance();
				filter.addDaoParam(filter.daoNamedParam("protocolTimepointConfigId", protocolTimepoint.getProtocolTimepointConfig().getId()));
				ProtocolTimepointConfig completeProtocolTimepointConfig = (ProtocolTimepointConfig) EntityBase.MANAGER.findOneByNamedQuery("protocol.completeProtocolTimepointConfigTree", filter);
				for (short i=1; i<= protocolTimepoint.getNewRepeatNum(); i++) {
					ProtocolTimepoint repeatingProtocolTimepoint = 
						ProtocolTimepoint.createRepeatingTimepointFromConfig(protocolTimepoint.getProtocol(), completeProtocolTimepointConfig, (short)(protocolTimepoint.getRepeatNum()+i));
					protocolTimepoint.getProtocol().addProtocolTimepoint(repeatingProtocolTimepoint);
				}
				
				// info. success message
				LavaComponentFormAction.createCommandError(errors, "info.protocol.repeatingCreated", new Object[]{protocolTimepoint.getNewRepeatNum()});
			}
			else {
				// error that next repeating timepont already exists so can not add
				LavaComponentFormAction.createCommandError(errors, "protocol.nextRepeatingExists");
				return new Event(this,ERROR_FLOW_EVENT_ID);		
			}
		}
		
		protocolTimepoint.getProtocol().save();
		return new Event(this,SUCCESS_FLOW_EVENT_ID);	
	}


	
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		String eventName = ActionUtils.getEventName(context);
		if(eventName.equals("custom")){
			return this.doCreateRepeatingTimepoints(context,command,errors);
		}		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
}
