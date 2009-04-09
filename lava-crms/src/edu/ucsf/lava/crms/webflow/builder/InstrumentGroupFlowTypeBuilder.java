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
		setEvents(new String[]{"view", "edit", "status", "delete", "deleteAll"});
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		
		registry.registerFlowDefinition(assemble(actionId + ".view", 
				new GroupFlowBuilder(registry,actionId)));

		// the edit action is used to encompass the "enter", "enterReview", "upload" individual
		// instrument actions, which are assumed to be mutually exclusive for a given instrument.
		// this way, an edit group flow can iterate thru individual instruments regardless of which
		// edit-like flow they support
		registry.registerFlowDefinition(assemble(actionId + ".edit", 
			new GroupFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".status", 
				new GroupFlowBuilder(registry,actionId)));
			
		registry.registerFlowDefinition(assemble(actionId + ".delete", 
				new GroupFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".deleteAll", 
				new GroupFlowBuilder(registry,actionId)));
	}

	
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"edit"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"status"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"delete"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"deleteAll"));
		return subFlowInfo;
	}
	
}