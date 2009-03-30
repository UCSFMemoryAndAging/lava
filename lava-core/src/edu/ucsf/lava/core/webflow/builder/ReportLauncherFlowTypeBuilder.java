package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class ReportLauncherFlowTypeBuilder extends BaseFlowTypeBuilder {

	public ReportLauncherFlowTypeBuilder() {
		super("reportLauncher");
		setEvents(new String[]{
				"view"});
		setDefaultFlowMode("view");
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		registry.registerFlowDefinition(assemble(actionId + ".view", 
			new ReportLauncherFlowBuilder(registry,actionId)));
	}

	
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		
		// because the reportLauncher is configured to be a subflow of every flow for convenience of configuration,
		// it is erroneously a subflow of itself and of each individual report. so do not build report launcher subflows 
		// for itself and for the individual report flows. preventing this is critical because it prevents recursion 
		// when building the flows
		if(!flowType.equals("reportLauncher") && !flowType.equals("report")){
			String target = ActionUtils.getTarget(subFlowActionId);
			subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
		}
		
		
		return subFlowInfo;
	}
	
}
