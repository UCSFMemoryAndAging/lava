package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class ImportFlowTypeBuilder extends BaseFlowTypeBuilder {

	public ImportFlowTypeBuilder(){
		super("entity");
		setEvents(new String[]{"view"});
		setDefaultFlowMode("view");
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {		
			registry.registerFlowDefinition(assemble(actionId + ".view", 
						new ListViewFlowBuilder(registry,actionId)));
	}

	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		return subFlowInfo;
	}

}
