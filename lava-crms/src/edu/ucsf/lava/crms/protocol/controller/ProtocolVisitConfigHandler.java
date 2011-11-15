package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.protocol.model.ProtocolTimepointConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTrackingConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig;

public class ProtocolVisitConfigHandler extends CrmsEntityComponentHandler {

	public ProtocolVisitConfigHandler() {
		super();
		setHandledEntity("protocolVisitConfig", edu.ucsf.lava.crms.protocol.model.ProtocolVisitConfig.class);
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

		ProtocolVisitConfig visitConfig = (ProtocolVisitConfig) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("visitConfigId", visitConfig.getId()));
		ProtocolTrackingConfig visitConfigTree = (ProtocolTrackingConfig) EntityBase.MANAGER.findOneByNamedQuery("protocol.visitConfigTree", filter);
		backingObjects.put("visitConfigTree", visitConfigTree);
				
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolVisitConfig visitConfig = (ProtocolVisitConfig) command;
		ProtocolTimepointConfig timepointConfig = (ProtocolTimepointConfig) ProtocolTimepointConfig.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		timepointConfig.addVisit(visitConfig);
		visitConfig.setProjName(timepointConfig.getProjName());
		return command;
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		ProtocolVisitConfig visitConfig = (ProtocolVisitConfig) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//	load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
}
