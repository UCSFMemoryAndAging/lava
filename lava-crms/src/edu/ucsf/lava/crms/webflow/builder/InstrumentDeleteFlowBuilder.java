package edu.ucsf.lava.crms.webflow.builder;

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
 * Java-based flow builder that builds the delete instrument flow.
 * <p>
 * This encapsulates the page flow of deleting an entity, and is typically
 * typically involved in a larger flow conversation which includes a parent 
 * list flow.
 */
public class InstrumentDeleteFlowBuilder extends BaseFlowBuilder {

	public InstrumentDeleteFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("delete");
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
                    transition(on("instrument__confirmDelete"), to("finish"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    transition(on("instrument__cancelDelete"), to("finishCancel"), 
                    	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
       				transition(on("instrument__switch"), to("finishSwitch"), 
       					ifReturnedSuccess(new Action[]{
           			    	new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
       	    				new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
   					})),
                    // support a list secondary component for nav events only, since just a reference list
                    buildListNavigationTransitions(getFlowEvent())},
    			null, null, null);


    }
    
}



