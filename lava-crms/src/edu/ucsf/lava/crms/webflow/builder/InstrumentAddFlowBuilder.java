package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.ScopeType;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

/**
 * Java-based flow builder that builds the add instrument flow, which is shared
 * among all instruments.
 * <p>
 * This encapsulates the page flow of adding a new entity, and is
 * typically involved in a larger flow conversation with a parent list flow. 
 */
public class InstrumentAddFlowBuilder extends BaseFlowBuilder {
	
	public InstrumentAddFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("add");
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	TransitionCriteria reRenderEventTransitionCriteria;
    	// add instruments shows secondary list of instruments, so need a transition action
    	// for the reRender event which refreshes the secondary list. this is not true for macdiagnosis
    	// so only do for assessment.instrument instruments
   		if (ActionUtils.getModule(actionId).equals("assessment") && 
     			ActionUtils.getSection(actionId).equals("instrument")) {      		
    		reRenderEventTransitionCriteria = ifReturnedSuccess(new Action[]{
              	invoke("customBind", formAction),
               	// update pre-populated dcDate,dcStatus based on selected visit
               	invoke("handleFlowEvent", formAction),
               	// need to refresh the secondary component list of instruments
               	new SetAction(settableExpression("eventOverride"), ScopeType.FLASH, 
						expression("'visitInstruments__refresh'")),
				invoke("handleFlowEvent",formAction),
				// reset the eventOverride so original event (reRender) is available if needed in render actions
				new SetAction(settableExpression("eventOverride"), ScopeType.FLASH, expression(null))
               	});
    	}
    	else {
    		// else if not instrument
    		reRenderEventTransitionCriteria = ifReturnedSuccess(invoke("customBind", formAction));
    	}
    	
   		
   		
   		
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
    				// required field validation does not take place on reRender so no required
    				// field errors will be shown
                    transition(on("instrument__reRender"), to(getFlowEvent()), reRenderEventTransitionCriteria),
                    transition(on("instrument__cancelAdd"), to("finishCancel"), 
                        	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    transition(on("instrument__saveAdd"), to("finish"), 
                       	ifReturnedSuccess(new Action[]{
                       		invoke("customBind", formAction),
                       		invoke("handleFlowEvent", formAction)})),
                    transition(on("instrument__applyAdd"), to(getFlowEvent()), 
                    	ifReturnedSuccess(new Action[]{
                    		invoke("customBind", formAction),
                    		invoke("handleFlowEvent", formAction)})),
                    transition(on("instrument__close"), to("finish"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    // support a list secondary component for nav events only, since just a reference list
                    buildListNavigationTransitions(getFlowEvent())},
    			null, null, null);
    	
    }
    
	public void buildOutputMapper() throws FlowBuilderException {
		// return the newly created entity id to the parent flow in case it needs to know
		Mapping newIdMapping = mapping().source("flowScope.newId").target("subflowEntityId").value();
		// return the action id of this subflow to the parent flow in case it needs to know
		Mapping actionIdMapping = mapping().source("flowScope.actionId").target("subflowActionId").value();
		getFlow().setOutputMapper(new DefaultAttributeMapper().addMapping(newIdMapping).addMapping(actionIdMapping));
	}

}



