package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class ImportFlowTypeBuilder extends BaseFlowTypeBuilder {

	public static final String[] ENTITY_EVENTS = new String[]{"edit"};
	
	public ImportFlowTypeBuilder(){
		super("import");
		setEvents(ENTITY_EVENTS);
		setDefaultFlowMode("edit");
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		registry.registerFlowDefinition(assemble(actionId + ".edit", 
						new ImportFlowBuilder(registry,actionId)));
			
	}

	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		return subFlowInfo;
	}

}
