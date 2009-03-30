package edu.ucsf.lava.core.webflow.builder;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.engine.Flow;
import org.springframework.webflow.engine.builder.FlowBuilderException;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * This class is used to modify flows previously build by another FlowBuilder.  Instantiate with the 
 * flow to modify and all the utility functions of BaseFlowBuilder can be used to add to the Flow. 
 * 
 * This seriously violates the encapsulation of the FlowBuilder / FlowAssembler web flow classes, but
 * was the least awful solution to needing to add post flow builder customization while keeping all the
 * flow code using the same building logic and coding paradigms.  It looks like enough other stuff will break
 * when we go to webflow 2.0 that taking this approach here is not going to be the biggest thing we have to deal with
 * 
 * @author jhesse
 *
 */
public abstract class BaseFlowModifier extends BaseFlowBuilder {

	
	
	protected Flow modifyFlow;
	
	public BaseFlowModifier(LavaFlowRegistrar registry, String actionId, String formActionName) {
		super(registry, actionId,formActionName);
	}
	public BaseFlowModifier(LavaFlowRegistrar registry, String actionId) {
		super(registry, actionId);
	}
	public Flow getFlow() {
		return modifyFlow;
	}
	public void setModifyFlow(Flow modifyFlow) {
		this.modifyFlow = modifyFlow;
	}
	
	public FlowDefinition modifyFlow(FlowDefinition flow){
		if(Flow.class.isAssignableFrom(flow.getClass())){
			setModifyFlow((Flow)flow);
			return doModifyFlow();
		}
		return flow;
	}
	
	
	/**
	 * override this method to customize the flow using the AbstractFlowBuilder convenience methods.
	 */
	protected abstract FlowDefinition doModifyFlow();
		
	
	
	//do nothing on these methods as this class is not designed to build new flows.  
	public void buildStates() throws FlowBuilderException {}
	public void init(String flowId, AttributeMap attributes) throws FlowBuilderException {}
	public void buildExceptionHandlers() throws FlowBuilderException {}
	public void buildGlobalTransitions() throws FlowBuilderException {}
	public void buildEndActions() throws FlowBuilderException {}
	public void buildInlineFlows() throws FlowBuilderException {}
	public void buildInputMapper() throws FlowBuilderException {}
	public void buildOutputMapper() throws FlowBuilderException {}
	public void buildStartActions() throws FlowBuilderException {}
	public void buildVariables() throws FlowBuilderException {}
	public void dispose() {}
	protected void buildEventStates() throws FlowBuilderException {}
		

	
	
	
	
	
	
	
}
