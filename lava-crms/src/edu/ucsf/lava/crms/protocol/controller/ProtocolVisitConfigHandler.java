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
		timepointConfig.addProtocolVisitConfig(visitConfig);
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

		// set a flag indicating whether this is the primary ProtocolVisitConfig for the view to display
		// that info
		model.put("primaryVisitConfigFlag", isPrimaryProtocolVisitConfig(visitConfig, flowMode));
		
		return super.addReferenceData(context, command, errors, model);
	}

	
	/**
	 * Helper method to determine if the ProtocolVisitConfig is marked as the primary visit config.
	 * 
	 * @param visitConfig
	 * @param flowMode
	 * @return
	 */
	protected Boolean isPrimaryProtocolVisitConfig(ProtocolVisitConfig visitConfig, String flowMode) {
		// if adding, the new ProtocolVisitConfig does not have an id yet since hasn't been saved, so can
		// not set it as the primary visit config on ProtocolTimepointConfig, so can not just call 
		// isPrimaryProtocolVisitConfig here
		
		// if there is just one ProtocolVisitConfig in ProtocolTimepointConfig's collection, return true, as the
		// business rule is if there is only one visit config it must be marked as the primary (this
		// covers whether editing existing or adding new, because if adding, the ProtocolVisitConfig has 
		// already been added to the ProtocolTimepointConfig collection in initializeNewCommandInstance)
		if (visitConfig.getProtocolTimepointConfig().getProtocolVisitConfigs().size() == 1) {
			return Boolean.TRUE;
		}
		// if there is more than one:
		else {
			// if adding, this can not be marked as the first visit config yet since  it does not exist 
			// yet (has not been saved). 
			if (flowMode.equals("add")) {
				return  Boolean.FALSE;
			}
			// if editing existing, then call the standard method to determine if this is the first (it 
			// is guaranteed that one will be marked as primary as long as there is one or more in existence)
			else {
				return visitConfig.isPrimaryProtocolVisitConfig();
			}
		}
	}	
	
	
}
