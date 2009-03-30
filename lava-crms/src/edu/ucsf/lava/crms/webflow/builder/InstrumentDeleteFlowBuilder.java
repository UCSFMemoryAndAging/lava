package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;


/**
 * Java-based flow builder that builds the delete instrument flow.
 * <p>
 * This encapsulates the page flow of deleting an entity, and is typically
 * typically involved in a larger flow conversation which includes a parent 
 * list flow.
 */
class InstrumentDeleteFlowBuilder extends BaseFlowBuilder {

	public InstrumentDeleteFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("delete");
    }
    
    public void buildInputMapper() throws FlowBuilderException {
    	// put the "id" into flowScope where it will be accessed in the FormAction (createFormObject)
    	// to retrieve the entity (it is also accessed to set entity context in setContextFromScope)
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since entity CRUD flows are typically subflows, "id"
    	// here typically comes from a parent flow input mapper
    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();

    	// put the "instrTypeEncoded" info flowScope where it will be accessed in the InstrumentHandler
    	// getBackingObjects to determine the class of the instrument to load. the "instrTypeEncoded" 
    	// is put into the input map for this flow in the FlowListener sessionStarting method  
    	Mapping instrTypeEncodedMapping = mapping().source("instrTypeEncoded").target("flowScope.instrTypeEncoded").value();

    	// set the flow input mapper with the above mappings
    	getFlow().setInputMapper(new DefaultAttributeMapper().addMapping(idMapping).addMapping(instrTypeEncodedMapping));
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
                    transition(on("instrument__confirmDelete"), to("finish"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    transition(on("instrument__cancelDelete"), to("finishCancel"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    // support a list secondary component for nav events only, since just a reference list
                    buildListNavigationTransitions(getFlowEvent())},
    			null, null, null);


    }
    
}



