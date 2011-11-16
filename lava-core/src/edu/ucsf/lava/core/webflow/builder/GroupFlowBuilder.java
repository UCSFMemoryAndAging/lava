package edu.ucsf.lava.core.webflow.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.ScopeType;

import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

public class GroupFlowBuilder extends BaseFlowBuilder {
	public GroupFlowBuilder(LavaFlowRegistrar registry, String actionId) {
		super(registry, actionId);
		// for the purposes of this flow, the flow event is just the name of the state,
		// which the flowSetupState transitions to, i.e. the first state of the flow
		// beyond the standard flow setup
		setFlowEvent("preIterate");
	}

    public void buildInputMapper() throws FlowBuilderException {
    	super.buildInputMapper();
    	AttributeMapper inputMapper = getFlow().getInputMapper();

    	// parent flows map the group structure into input mapper "group" (the action to be 
    	// performed is not mapped in because it can be derived by the flow id for the group flow)
    	Mapping groupMapping = mapping().source(GROUP_MAPPING).target("flowScope." + GROUP_MAPPING).value();
    	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(groupMapping));
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	
    	addDecisionState("preIterate", null,
    		new Transition[] {
    			transition(on("${activeFlow.id.endsWith('bulkDelete')}"), to("bulkDelete")),
    			transition(on("${@java.lang.Boolean@TRUE}"), to("iterate"),
    					ifReturnedSuccess(new SetAction(settableExpression("groupIndex"), ScopeType.FLOW, expression("0"))))},
    		null, null, null);
    	
    	ArrayList<Transition> iterateStateTransitions = new ArrayList<Transition>();
    	
		// one of the keys to the design is that the handler returns a String which which will transition to a
		// target subflow of the same name, i.e. the return Strings match the subflow names that are created as
		// part of buildSubFlowStates, using transitions created by buildSubFlowTransitions    	
		// e.g. "instrument__enter" or "medications__delete"
    	// note the special handing for "bulkDelete" where the "preIterate" state above transitions to
    	// the "bulkDelete" state and the "iterate" state is skipped entirely
    	iterateStateTransitions.addAll(this.buildSubFlowTransitions());
        iterateStateTransitions.add(transition(on("missing"), to("missing")));
        iterateStateTransitions.add(transition(on("groupError"), to("groupError")));
        iterateStateTransitions.add(transition(on("finish"), to("finish")));

    	//Action States execute a series of Actions until a transition matches the event
    	//returned by the Action (i.e. invoke) or, until the list of Actions is exhausted
    	addActionState("iterate", null,
       			new Action[]{
       				// returns String matching subflow transition or "missing", "groupError" or "finish"
    				// this is a wizard-like flow which automates the processing of the next entity in the list,
    				// so there are no explicit user events. therefore, in order to funnel preparing the next entity
    				// thru handleFlowEvent, set an eventOverride to simulate an event
    				new SetAction(settableExpression("eventOverride"), ScopeType.FLASH,	expression("'" + objectName + "__next'")),
       				invoke("handleFlowEvent", formAction)},
        		iterateStateTransitions.toArray(new Transition[0]),
       			null,null,null);

    	addViewState("missing", 
    			null, 
    			formAction.getCustomViewSelector(),
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] {
                        transition(on(objectName + "__addMissing"), to("iterate"), 
                        	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                        transition(on(objectName + "__skipMissing"), to("iterate"))},
          		null,
    			null,
    			null);
    	
    	addViewState("groupError", 
    			null, 
    			formAction.getCustomViewSelector(),
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] {transition(on(objectName + "__close"), to("iterate"))},    			
          		null,
    			null,
    			null);

    	addViewState("bulkDelete", 
    			null, formAction.getCustomViewSelector(),
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
                    transition(on(objectName + "__confirmBulkDelete"), to("finish"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    transition(on(objectName + "__cancelBulkDelete"), to("finish"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    // support a list secondary component for nav events only, since just a reference list
                    buildListNavigationTransitions(getFlowEvent())},
    			null, null, null);
    }
    
    // override because if subflow returns "finishCancel", cancel out of the group flow
    protected void buildSubFlowStates(){
    	// pass group to subflows in case they need it
		subflowInputOutputMapper.addInputMapping(mapping().source("flowScope."+GROUP_MAPPING).target(GROUP_MAPPING).value());
		
    	for(String subFlowId : subFlowActionIds){
    		List<FlowInfo> subFlowInfoList = getSubFlowInfo(subFlowId); 
				
    		for(FlowInfo subFlowInfo: subFlowInfoList){
    			// if there is an instance specific version of the subflow, it replaces the subflow
    			// (but only if the subflow action has an actual flow, i.e. flowType is not "none" or
    			// "instrumentCommon" which is for instruments that use the shared common instrument flow)
    			// this is determined in ActionService. getEffectiveActionIdForFlowId
				addSubflowState(subFlowInfo.getTarget()+"__"+subFlowInfo.getEvent(),
						flow(registry.getActionManager().getEffectiveAction(subFlowInfo.getActionId()).getId() + "." + subFlowInfo.getEvent()),
						subflowInputOutputMapper,  
						new Transition[] {transition(on("finish"), to("subFlowReturnState")),
										  transition(on("finishCancel"), to("subFlowCancelReturnState"))});						
    		}
    	}

    	// add action state which refreshes the command components and transitions to getFlowEvent.    	
    	// this way, a parent entity view flow will reflect changes in an entity edit subflow, and
    	// a parent list flow will reflect entities added/deleted in add/delete entity subflows, as
    	// well as entity changes in an entity edit subflow
    	buildSubFlowReturnStates();
    }
    
    // override because if subflow is cancelled, cancel group flow. cancelled subflow will return "finishCancel"
    // instead of "finish"
    protected void buildSubFlowReturnStates() {
    	// note that refreshFormObject does nothing, as nothing needs to be done, but action states must have
    	// an action
    	addActionState("subFlowReturnState", 
    			invoke("refreshFormObject", formAction), 
    			transition(on("success"), to("iterate")));
    	
    	addActionState("subFlowCancelReturnState", 
    			invoke("refreshFormObject", formAction), 
    			transition(on("success"), to("finishCancel")));
    }

    	// return the group to the parent flow so that it can re-select selected items after it
    // does a refresh of the list, which loses selected item info but must be done in case
    // the list was modified, e.g. if the group flow added missing entities
	public void buildOutputMapper() throws FlowBuilderException {
		Mapping groupMapping = mapping().source("flowScope." + GROUP_MAPPING).target(GROUP_MAPPING).value();
		getFlow().setOutputMapper(new DefaultAttributeMapper().addMapping(groupMapping));
	}
    
}



