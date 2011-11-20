package edu.ucsf.lava.core.webflow.builder;

import java.util.ArrayList;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * Java-based flow builder that builds the edit entity flow, parameterized
 * for a specific entity type.
 * <p>
 * This encapsulates the page flow of editing an entity's details, and is
 * typically involved in a larger flow conversation which may include a 
 * parent list flow and/or a parent view flow
 */
public class EntityEditFlowBuilder extends BaseFlowBuilder {
	
	public EntityEditFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("edit");
    }
    
    public void buildInputMapper() throws FlowBuilderException {
    	super.buildInputMapper();
    	AttributeMapper inputMapper = getFlow().getInputMapper();
    	
    	// put the "id" into flowScope where it will be accessed in the FormAction (createFormObject)
    	// to retrieve the entity (it is also accessed to set entity context in setContextFromScope)
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since entity CRUD flows are typically subflows, "id"
    	// here typically comes from a parent flow input mapper
    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
    	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(idMapping));
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	ArrayList<Transition> viewTransitions = new ArrayList<Transition>();
    	viewTransitions.add(transition(on(objectName + "__reRender"), to("edit"),
            	ifReturnedSuccess(new Action[]{
                		invoke("customBind", formAction), 
                		invoke("handleFlowEvent", formAction)})));
    	
    	viewTransitions.add(transition(on(objectName + "__save"), to("finish"), 
            	ifReturnedSuccess(new Action[]{
                		invoke("customBind", formAction), 
                		invoke("handleFlowEvent", formAction)})));
    	
    	viewTransitions.add(transition(on(objectName + "__cancel"), to("finishCancel"), 
            	ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
    	
    	// get subflow transitions
    	// note that since this is an edit flow, calling the "WithBind" version of this method which binds 
    	// before the transition so that any pending user edits are saved in flow scope and thus retained 
    	// when the subflow returns
    	viewTransitions.addAll(this.buildSubFlowTransitionsWithBind());
    	
    	// support a list secondary component for nav events only, since just a reference list
    	viewTransitions.add(buildListNavigationTransitions("edit"));
    	
        // custom event support
    	viewTransitions.addAll(this.buildCustomEventTransitions(this.objectName));
    	
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),
    			null, null, null);

    }
}



