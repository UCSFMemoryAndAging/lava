package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;
import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_INDEX;
import static edu.ucsf.lava.core.controller.BaseEntityComponentHandler.CONFIRM_LOGIC;

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

import edu.ucsf.lava.core.controller.BaseGroupComponentHandler;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.FlowInfo;
import edu.ucsf.lava.core.webflow.builder.GroupFlowBuilder;

public class SwitchGroupFlowBuilder extends GroupFlowBuilder {

	public SwitchGroupFlowBuilder(LavaFlowRegistrar registry, String actionId) {
		super(registry, actionId);
	}

	@Override
	public void buildInputMapper() throws FlowBuilderException {
    	super.buildInputMapper();
    	AttributeMapper inputMapper = getFlow().getInputMapper();

    	// SWITCHABLE: Delete subflows may delete an entity from the GROUP_MAPPING, which may affect the GROUP_INDEX that
    	//   specifies next in group, so must get GROUP_INDEX back from subflows as well in case changed
    	Mapping groupIndexMapping = mapping().source(GROUP_INDEX).target("flowScope." + GROUP_INDEX).value();
    	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(groupIndexMapping));
	}
	
	@Override
	public void buildEventStates() throws FlowBuilderException {
    	
		addDecisionState("preIterate", null,
    		new Transition[] {
    			transition(on("${activeFlow.id.endsWith('bulkDelete')}"), to("bulkDelete")),
    			/* SWITCHABLE: accommodate switching
    			 *   the default can no longer be iterate when using this subclass
    			 *   the active flow could be any parent flow, and at this point, we
    			 *    can't pick and choose what transitions where
    			 *   So in this case, SwitchGroupFlows will override GroupFlows
    			 *   This means that, instead, our switch _handling_ must consider the case where not
    			 *    enough parameters are given for switching and default back to iterating
    			 *    instead */
    			transition(on("${@java.lang.Boolean@TRUE}"), to("switching"),
    					ifReturnedSuccess(new SetAction(settableExpression(GROUP_INDEX), ScopeType.FLOW, expression("0"))))},
    		null, null, null);
    	
    	ArrayList<Transition> iterateStateTransitions = new ArrayList<Transition>();
    	
		// one of the keys to the design is that the handler returns a String which which will transition to a
		// target subflow of the same name, i.e. the return Strings match the subflow names that are created as
		// part of buildSubFlowStates, using transitions created by buildSubFlowTransitions    	
		// e.g. "instrument__enter" or "medications__delete"
    	// note the special handing for "bulkDelete" and "switching" where the "preIterate" state above transitions to
    	// the "bulkDelete" (or "switching") state and the "iterate" state is skipped entirely
    	iterateStateTransitions.addAll(this.buildSubFlowTransitions());
        iterateStateTransitions.add(transition(on("missing"), to("missing")));
        iterateStateTransitions.add(transition(on("groupError"), to("groupError")));
        iterateStateTransitions.add(transition(on("finish"), to("finish")));
        //SWITCHABLE: support SwitchGroupFlow switchMode changing
        iterateStateTransitions.add(transition(on("switchMode"), to("switchMode")));
        

        /* SWITCHABLE: added for switching */
        /*  the "switching" will assume an "id" given in the request */
        addActionState("switching", null,
       			new Action[]{
       				// returns String matching subflow transition or "missing", "groupError" or "finish"
    				// this is a wizard-like flow which automates the processing of the next entity in the list,
    				// so there are no explicit user events. therefore, in order to funnel preparing the next entity
    				// thru handleFlowEvent, set an eventOverride to simulate an event
    				new SetAction(settableExpression("eventOverride"), ScopeType.FLASH,	expression("'" + objectName + "__switch'")),
       				invoke("handleFlowEvent", formAction)},
        		iterateStateTransitions.toArray(new Transition[0]),
       			null,null,null);
        
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
   
    	// do this so that non-edit group switching can clear patient context, etc.
    	buildDefaultActionEndState();
	}
	
	// override because if subflow returns "finishCancel", cancel out of the group flow
    @Override
	protected void buildSubFlowStates(){
    	// SWITCHABLE: pass group to subflows in case they need it
    	subflowInputOutputMapper.addInputMapping(mapping().source("flowScope."+GROUP_MAPPING).target(GROUP_MAPPING).value());
    	subflowInputOutputMapper.addInputMapping(mapping().source("flowScope."+GROUP_INDEX).target(GROUP_INDEX).value());
    	// SWITCHABLE: pass groupIndex to subflows since they could change group (i.e. by deleting)
    	subflowInputOutputMapper.addOutputMapping(mapping().source(GROUP_INDEX).target("flowScope."+GROUP_INDEX).value());
		// LOGICCHECKS
		// to be able to pass CONFIRM_LOGIC between two instrument subflows
    	subflowInputOutputMapper.addInputMapping(mapping().source("flowScope."+CONFIRM_LOGIC).target(CONFIRM_LOGIC).value());
    	subflowInputOutputMapper.addOutputMapping(mapping().source(CONFIRM_LOGIC).target("flowScope."+CONFIRM_LOGIC).value());
    	
    	for(String subFlowId : subFlowActionIds){
    		List<FlowInfo> subFlowInfoList = getSubFlowInfo(subFlowId); 
				
    		for(FlowInfo subFlowInfo: subFlowInfoList){
    			// if there is an instance specific version of the subflow, it replaces the subflow
    			// (but only if the subflow action has an actual flow, i.e. flowType is not "none")
    			// this is determined in ActionService. getEffectiveActionIdForFlowId
				addSubflowState(subFlowInfo.getTarget()+"__"+subFlowInfo.getEvent(),
						flow(registry.getActionManager().getEffectiveAction(subFlowInfo.getActionId()).getId() + "." + subFlowInfo.getEvent()),
						subflowInputOutputMapper,  
						new Transition[] {transition(on("finish"), to("subFlowReturnState")),
										  transition(on("finishCancel"), to("subFlowCancelReturnState")),
										  /* EMORY change: subflow could return "finishSwitch", so handle it */
										  transition(on("finishSwitch"), to("subFlowSwitchReturnState"))});						
    		}
    	}

    	// add action state which refreshes the command components and transitions to getFlowEvent.    	
    	// this way, a parent entity view flow will reflect changes in an entity edit subflow, and
    	// a parent list flow will reflect entities added/deleted in add/delete entity subflows, as
    	// well as entity changes in an entity edit subflow
    	buildSubFlowReturnStates();
    }
    
	// a subflow returning "finishSwitch" means it finished one switch iteration, and is now
    //   returning to the parent (instrumentGroup) flow expecting it to move on to the next
    //   iteration.  Because of this, we then transition a subflow "finishSwitch" to the
    //   "switching" of this flow (instrumentGroup), which will spawn a new subflow accordiningly
    @Override
    protected void buildSubFlowReturnStates() {
    	super.buildSubFlowReturnStates();
    	
    	/* SWITCHABLE: added for finishSwitch */
    	// note that subFlowReturnHook does nothing, as nothing needs to be done, but action states must have
    	// an action
    	addActionState("subFlowSwitchReturnState", 
    			// entry action (note: the expression("false") puts an attribute of type Boolean in flow scope)
       			new Action[]{new SetAction(settableExpression("cancelled"), ScopeType.FLOW,	expression("false"))},
       			// actions
       			new Action[]{invoke("subFlowReturnHook", formAction)},
   				new Transition[]{transition(on("success"), to("switching"))},	
   				null,null,null);
    	
    }

	@Override
	protected void buildFinishEndStates() {
		super.buildFinishEndStates();
		// SWITCHABLE:: support SwitchGroupFlow switchMode changing
		//   switchMode should end the current flow and return to parent.
		//   The parent flow would then see take that switchMode state and transition
		//   to recreate the new subflow with the new mode
		addEndState("switchMode", 
				new Action[]{new SetAction(settableExpression("actionId"), ScopeType.FLOW, expression("'" + this.actionId + "'"))},
				formAction.getDefaultActionViewSelector(),
				null,
				null,
				null);
		
	}
}
