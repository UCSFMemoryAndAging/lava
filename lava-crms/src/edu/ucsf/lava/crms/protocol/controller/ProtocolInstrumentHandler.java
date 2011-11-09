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
import edu.ucsf.lava.crms.protocol.model.ProtocolInstrument;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.protocol.model.ProtocolVisit;

public class ProtocolInstrumentHandler extends CrmsEntityComponentHandler {

	public ProtocolInstrumentHandler() {
		super();
		setHandledEntity("protocolInstrument", edu.ucsf.lava.crms.protocol.model.ProtocolInstrument.class);
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

		ProtocolInstrument protocolInstrument = (ProtocolInstrument) backingObjects.get(getDefaultObjectName());
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		filter.addDaoParam(filter.daoNamedParam("instrumentId", protocolInstrument.getId()));
		ProtocolTracking instrumentOptions = (ProtocolTracking) EntityBase.MANAGER.findOneByNamedQuery("protocol.instrumentTree", filter);
		backingObjects.put("instrumentTree", instrumentOptions);
		
		return backingObjects;
	}
	
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command){
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		ProtocolInstrument protocolInstrument = (ProtocolInstrument) command;
		ProtocolVisit protocolVisit = (ProtocolVisit) ProtocolVisit.MANAGER.getOne(EntityBase.newFilterInstance().addIdDaoEqualityParam(Long.valueOf(context.getFlowScope().getString("param"))));
		protocolVisit.addInstrument(protocolInstrument);
		protocolInstrument.setProjName(protocolVisit.getProjName());
		return command;
	}
	
}
