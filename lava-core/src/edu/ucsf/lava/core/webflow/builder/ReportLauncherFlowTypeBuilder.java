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
		// for itself and for the individual report flows (report flows are subflows of the reportLauncher, but not vice
		// versa). preventing this is critical because it prevents recursion when building the flows
		
		// additionally, since the trainingLauncher flow is also a subflow of every flow, there would be a deadlock 
		// condition during flow building if the reportLauncher flow had a trainingLauncher subflow and vice versa, 
		// so prevent the reportLauncher from being a subflow of trainingLauncher, which is fine since will not need to 
		// launch a report from the trainingLauncher flow
		if(!flowType.equals("reportLauncher") && !flowType.equals("report") && !flowType.equals("trainingLauncher")){
			String target = ActionUtils.getTarget(subFlowActionId);
			subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
		}
		
		
		return subFlowInfo;
	}
	
}
