package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class ReportFlowTypeBuilder extends BaseFlowTypeBuilder {

	public ReportFlowTypeBuilder(){
		super("report");
		setEvents(new String[]{
				"view"});
		setDefaultFlowMode("view");
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		registry.registerFlowDefinition(assemble(actionId + ".view", new ReportFlowBuilder(registry,actionId)));
	}
	
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);

		// all reports are subflows of the report launcher flow
		if(flowType.equals("reportLauncher")){
			subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
		}
		
		return subFlowInfo;
	}
	

}
