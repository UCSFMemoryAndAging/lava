package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class PasswordFlowTypeBuilder extends BaseFlowTypeBuilder {

	public PasswordFlowTypeBuilder() {
		super("password");
		setEvents(new String[]{
				"change",
				"reset"});
		setDefaultFlowMode("change");
	}

	// build the entity flow definitions for a given entity, any entity flows which 
	// are referred to as subflows within another entity flow must be created first, 
	// e.g. the view flows have the edit flow as a subflow, so the edit flow definition 
	//must be built first so that the view flow definition can refer to it
	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
			
			registry.registerFlowDefinition(assemble(actionId + ".reset", 
					new PasswordResetFlowBuilder(registry,actionId)));
			
			registry.registerFlowDefinition(assemble(actionId + ".change", 
						new PasswordChangeFlowBuilder(registry,actionId)));
				
					
			}

	@Override
	/**	This method returns the information needed when flows of type "password" are used as subflows.
	 * 
	 * @param	actionId	action of the parent flow
	 * @param	flowType	flow type of the parent flow
	 * @param	subFlowActionid	action of the subflows, where a mode is appended to the action to generate the flow id
	 * @param	actions	the actions Map used to obtain action information
	 */
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		subFlowInfo.add(new FlowInfo(subFlowActionId,"reset"));
		subFlowInfo.add(new FlowInfo(subFlowActionId,"change"));
		return subFlowInfo;
	}
	
}
	
	

