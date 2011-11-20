package edu.ucsf.lava.core.webflow.builder;

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

/**
 * Java-based flow builder that builds the add entity flow, parameterized
 * for a specific entity type.
 * <p>
 * This encapsulates the page flow of adding a new entity, and is
 * typically involved in a larger flow conversation with a parent list flow. 
 */
public class EntityAddFlowBuilder extends BaseFlowBuilder {
	
	public EntityAddFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("add");
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	TransitionCriteria reRenderEventTransitionCriteria;
   		reRenderEventTransitionCriteria = ifReturnedSuccess(new Action[]{
         		invoke("customBind", formAction),
          		invoke("handleFlowEvent", formAction)});
   		
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
    				// required field validation does not take place on refreshRefData so no required
    				// field errors will be shown
                    transition(on(objectName + "__reRender"), to("add"), reRenderEventTransitionCriteria),
                    transition(on(objectName + "__cancelAdd"), to("finishCancel"), 
                        	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    transition(on(objectName + "__saveAdd"), to("finish"), 
                       	ifReturnedSuccess(new Action[]{
                       		invoke("customBind", formAction),
                       		invoke("handleFlowEvent", formAction)})),
                    transition(on(objectName + "__applyAdd"), to("add"), 
                    	ifReturnedSuccess(new Action[]{
                    		invoke("customBind", formAction),
                    		invoke("handleFlowEvent", formAction)})),
                    transition(on(objectName + "__close"), to("finish"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    // support a list secondary component for nav events only, since just a reference list
                    buildListNavigationTransitions("add")},
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



