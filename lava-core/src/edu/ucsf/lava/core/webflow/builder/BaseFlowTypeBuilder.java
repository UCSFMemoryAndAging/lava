package edu.ucsf.lava.core.webflow.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.webflow.definition.registry.FlowDefinitionHolder;
import org.springframework.webflow.definition.registry.StaticFlowDefinitionHolder;
import org.springframework.webflow.engine.builder.FlowAssembler;
import org.springframework.webflow.engine.builder.FlowBuilder;

import edu.ucsf.lava.core.action.model.Action;


abstract public class BaseFlowTypeBuilder implements FlowTypeBuilder {

	private String type;
	private String[] events;
	private String defaultFlowMode;
	

	public BaseFlowTypeBuilder(String type){
		this.type = type;
	}
  
	protected List<String> getSubFlowActionIds(Map<String,Action> actions, String actionId){
			return ((Action)actions.get(actionId)).getSubFlows();
		}
	
	protected FlowDefinitionHolder assemble(String flowId, FlowBuilder flowBuilder) {
        return new StaticFlowDefinitionHolder(new FlowAssembler(flowId, flowBuilder).assembleFlow());
	}
	
	public List<FlowInfo>getSubFlowInfo(String actionId, String parentFlowType,
				String subFlowActionId,	Map actions){
    	return new ArrayList<FlowInfo>();
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String[] getEvents() {
		if(events==null) {return new String[]{};}
		return events;
	}

	public void setEvents(String[] events) {
		this.events = events;
	}

	public String getDefaultFlowMode() {
		return defaultFlowMode;
	}

	public void setDefaultFlowMode(String defaultFlowMode) {
		this.defaultFlowMode = defaultFlowMode;
	}

	
	
}


