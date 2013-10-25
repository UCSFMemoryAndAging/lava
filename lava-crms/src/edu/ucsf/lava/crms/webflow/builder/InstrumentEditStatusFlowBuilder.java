package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseEntityComponentHandler.CONFIRM_LOGIC;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.ScopeType;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

/**
 * Java-based flow builder that builds the enter instrument flow, parameterized
 * for by specific instrument type.
 * <p>
 * This encapsulates the page flow of instrument data entry, and is
 * typically involved in a larger flow conversation which may include a 
 * parent list flow and/or a parent view flow
 */
public class InstrumentEditStatusFlowBuilder extends BaseFlowBuilder {
	public InstrumentEditStatusFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
		setFlowEvent("editStatus");
	}

    public void buildInputMapper() throws FlowBuilderException {
	   	super.buildInputMapper();
	   	AttributeMapper inputMapper = getFlow().getInputMapper();
	    	
	   	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(idMapping));
	   	
	   	// LOGICCHECKS
    	// receive CONFIRM_LOGIC from parent flow
	   	Mapping confirmLogicMapping = mapping().source(CONFIRM_LOGIC).target("flowScope."+CONFIRM_LOGIC).value();
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(confirmLogicMapping));
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	
     			addViewState(getFlowEvent(), 
    	    			null, formAction.getCustomViewSelector(),
    	    			new Action[]{invoke("prepareToRender",formAction)},
    	    			new Transition[] {
    	    			transition(on("instrument__statusSave"), to("finish"), 
    						ifReturnedSuccess(new Action[]{
    							invoke("customBind", formAction), 
    							invoke("handleFlowEvent", formAction)})),
						transition(on("instrument__switch"), to("finishSwitch"), 
							ifReturnedSuccess(new Action[]{
								invoke("customBind", formAction),
								new SetAction(settableExpression("eventOverride"), ScopeType.FLASH, expression("${'instrument__statusSave'}")),
								invoke("handleFlowEvent", formAction),
								new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
						    	new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
							}))				
		    			},
    	   	       		null, null, null);

       	addEndState("finishSwitch"); 
    }
    
    public void buildGlobalTransitions() throws FlowBuilderException {
    	// do not need to call super because this flow is modal so not patient/project context events 
    	
		// cancel behaves differently here than in standard list-view-edit entity flow where the 
		// entity is not saved and refreshed to its persistent state. in this flow, entity is saved
		// between each view-state transition, so cancel just cancels any changes on the current
		// view-state, and transitions out of the flow; changes made in prior view-states within this
		// flow have been saved. 
		// TODO: allow user to cancel all changes made in flow and return the instrument to the state 
		// it was in upon entering flow, but still save instrument on view-state transtions (requires 
		// a deep virgin instrument copy made upon entering flow)
   		this.getFlow().getGlobalTransitionSet().add(
   				transition(on("instrument__cancel"), to("finishCancel"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
   		this.getFlow().getGlobalTransitionSet().add(transition(on("unauthorized"), to("${flowScope.mostRecentViewState}")));
    }
    
	public void buildOutputMapper() throws FlowBuilderException {
		// for switching from this instrument enter subflow to another instrument subflow. this flow must pass
	    // the mapping attributes back to the parent flow to tell it which instrument ("id") and subflow ("switchEvent")
		// to transition to. these are put into flow scope when the "instrument__switch" event is handled.
		Mapping idMapping = mapping().source("flowScope.id").target("id").value();
		Mapping switchEventMapping = mapping().source("flowScope.switchEvent").target("switchEvent").value();
		getFlow().setOutputMapper(new DefaultAttributeMapper().addMapping(idMapping).addMapping(switchEventMapping));
		
		// LOGICCHECKS
		// in case this flow altered the confirmLogic value
		AttributeMapper outputMapper = getFlow().getOutputMapper();
		Mapping confirmLogicMapping = mapping().source("flowScope."+CONFIRM_LOGIC).target(CONFIRM_LOGIC).value();
		getFlow().setOutputMapper(((DefaultAttributeMapper)outputMapper).addMapping(confirmLogicMapping));
	}
}



