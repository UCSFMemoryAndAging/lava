package edu.ucsf.lava.core.webflow.builder;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 *  Change Password Flow
 *  
 *  Note: if we end up using this flow for password expiration handling then 
 *  we may need a decision state to determine whether the password is expired
 *  and redirect to a logout on the cancel transition. 
 */
class PasswordChangeFlowBuilder extends BaseFlowBuilder {
	
	public PasswordChangeFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("change");
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
    	            transition(on(objectName + "__cancel"), to("finishCancel"), 
                        	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    transition(on(objectName + "__change"), to("finish"), 
                       	ifReturnedSuccess(new Action[]{
                       		invoke("customBind", formAction),
                       		invoke("handleFlowEvent", formAction)}))},
    			null, null, null);
    	 
   
	 	}
}



