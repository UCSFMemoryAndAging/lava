package edu.ucsf.lava.core.webflow.builder;

import edu.ucsf.lava.core.action.ActionUtils;


/**
 * Class used to structure the information needed by a parent flow to define subflows. 
 * Used for the FlowTypeBuilder::getSubFlowInfoForFlowType() routines. 
 *
 */
public class FlowInfo {
	
	
	private String target;
	private String actionId;
	private String event; 
	private boolean useIdMapper = true;
	
	public FlowInfo(String target,String actionId, String event, boolean useIdMapper){
		this.target = target;
		this.actionId = actionId;
		this.event = event;
		this.useIdMapper = useIdMapper;
		
	}
	//convenience constructor
	public FlowInfo(String type, String target,String actionId, String event){
		this(target,actionId,event,true);
	}
	//	convenience constructor, assumes target is target of actionId
	public FlowInfo(String actionId, String event){
		
		this(ActionUtils.getTarget(actionId),actionId,event,true);
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public boolean isUseIdMapper() {
		return useIdMapper;
	}
	public void setUseIdMapper(boolean useIdMapper) {
		this.useIdMapper = useIdMapper;
	}

}
