package edu.ucsf.lava.crms.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowTypeBuilder;
import edu.ucsf.lava.core.webflow.builder.FlowInfo;
import edu.ucsf.lava.core.webflow.builder.GroupFlowBuilder;

public class InstrumentGroupFlowTypeBuilder extends BaseFlowTypeBuilder {

	public InstrumentGroupFlowTypeBuilder() {
		super("instrumentGroup");
		// the events include all "instrument" FlowType events which can be executed 
		// on each instrument in an InstrumentGroup
		setEvents(new String[]{"view", "enter", "status", "delete", "deleteAll"});
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		
		//TODO: come up with better solution than redundant flows
		
		registry.registerFlowDefinition(assemble(actionId + ".view", 
				new GroupFlowBuilder(registry,actionId)));
		registry.registerFlowDefinition(assemble(actionId + ".view_group", 
				new GroupFlowBuilder(registry,actionId)));
			
			
		registry.registerFlowDefinition(assemble(actionId + ".enter", 
			new GroupFlowBuilder(registry,actionId)));
		registry.registerFlowDefinition(assemble(actionId + ".enter_group", 
				new GroupFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".status", 
				new GroupFlowBuilder(registry,actionId)));
		registry.registerFlowDefinition(assemble(actionId + ".status_group", 
				new GroupFlowBuilder(registry,actionId)));
			
		registry.registerFlowDefinition(assemble(actionId + ".delete", 
				new GroupFlowBuilder(registry,actionId)));
		registry.registerFlowDefinition(assemble(actionId + ".delete_group", 
				new GroupFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".deleteAll", 
				new GroupFlowBuilder(registry,actionId)));
	}

	
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"view_group"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"enter"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"enter_group"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"status"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"status_group"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"delete"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"delete_group"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"deleteAll"));
		return subFlowInfo;
	}
	
}