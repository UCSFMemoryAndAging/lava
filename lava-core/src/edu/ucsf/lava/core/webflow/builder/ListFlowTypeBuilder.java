package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class ListFlowTypeBuilder extends BaseFlowTypeBuilder {

	public ListFlowTypeBuilder(){
		super("list");
		setEvents(new String[]{
				"view"});
		setDefaultFlowMode("view");
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {		
			
			
			registry.registerFlowDefinition(assemble(actionId + ".view", 
						new ListViewFlowBuilder(registry,actionId)));

				/* TODO: implement edit list flow builder (when we have editable lists)
				registry.registerFlowDefinition(assemble(actionId + ".edit", 
						new ListEditFlowBuilder(serviceLocator, actionId,subFlowEntities, localFormActionBeanNamePrefix)));
				*/
		}

	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		
		if(flowType.equals("list")||flowType.equals("instrumentList")||flowType.equals("entity")){
			
			subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
			
		}
		
		return subFlowInfo;
	}
	

}
