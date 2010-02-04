package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.binding.mapping.AttributeMapper;
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
public class InstrumentChangeVersionFlowBuilder extends BaseFlowBuilder {

	public InstrumentChangeVersionFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("changeVersion");
    }
    
	public void buildInputMapper() throws FlowBuilderException {
	   	super.buildInputMapper();
	   	AttributeMapper inputMapper = getFlow().getInputMapper();
	    	
	   	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(idMapping));
    }
	 
    public void buildEventStates() throws FlowBuilderException {
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
                    transition(on("instrument__confirmChangeVersion"), to("versionChanged"), 
                    		ifReturnedSuccess(new Action[] {invoke("customBind",formAction),
        							invoke("handleFlowEvent", formAction)})),
                    transition(on("instrument__cancelChangeVersion"), to("finishCancel"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction)))},
    			null, null, null);

    	addEndState("versionChanged", 
				null,
				formAction.getDefaultActionViewSelector(),
				null,
				null,
				null);
    	
    	 
    }
    
}



