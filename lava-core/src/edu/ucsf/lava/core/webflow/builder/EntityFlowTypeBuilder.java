package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class EntityFlowTypeBuilder extends BaseFlowTypeBuilder {

	public EntityFlowTypeBuilder() {
		super("entity");
		setEvents(new String[]{
				"view",
				"edit",
				"delete",
				"add",
				"download"});
		setDefaultFlowMode("view");
	}

	// build the entity flow definitions for a given entity, any entity flows which 
	// are referred to as subflows within another entity flow must be created first, 
	// e.g. the view flows have the edit flow as a subflow, so the edit flow definition 
	//must be built first so that the view flow definition can refer to it
	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
			
			registry.registerFlowDefinition(assemble(actionId + ".add", 
					new EntityAddFlowBuilder(registry,actionId)));
			
			registry.registerFlowDefinition(assemble(actionId + ".delete", 
						new EntityDeleteFlowBuilder(registry,actionId)));
				
			registry.registerFlowDefinition(assemble(actionId + ".edit", 
						new EntityEditFlowBuilder(registry,actionId)));

			registry.registerFlowDefinition(assemble(actionId + ".download", 
						new EntityDownloadFlowBuilder(registry,actionId)));
		
			registry.registerFlowDefinition(assemble(actionId + ".view", 
   						new EntityViewFlowBuilder(registry,actionId)));
			
			}

	@Override
	/**
	 * For flows that have "entity" type subflows, those flows must determine which subflows
	 * to create (as well as creating transitions to those subflows). This method returns the
	 * information needed when flows of type "entity" are used as subflows.
	 * 
	 * @param	actionId	action of the parent flow
	 * @param	flowType	flow type of the parent flow
	 * @param	subFlowActionid	action of the subflows, where a mode is appended to the action to generate the flow id
	 * @param	actions	the actions Map used to obtain action information
	 */
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		
		if(flowType.equals("list")||flowType.equals("instrumentList")||flowType.equals("instrument")||flowType.equals("entity")){
			
			String target = ActionUtils.getTarget(subFlowActionId);
			
			//refactor this into crms scope code....
			if (target.equals("enrollmentStatus")) {
    			subFlowInfo.add(new FlowInfo(target, "lava.crms.enrollment.status.addEnrollmentStatus","add",false));
			}
    		else if (target.equals("patient")) {
    			subFlowInfo.add(new FlowInfo(target, "lava.crms.people.patient.addPatient","add",false));
    		}
    		else {
    			subFlowInfo.add(new FlowInfo(subFlowActionId,"add"));
    		}
			subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"download"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"edit"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"delete"));
		}
		
		return subFlowInfo;
	}
	
}
	
	

