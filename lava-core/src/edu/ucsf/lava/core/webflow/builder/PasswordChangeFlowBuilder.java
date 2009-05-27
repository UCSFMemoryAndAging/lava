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
	    	// put the "id" into flowScope where it will be accessed in the FormAction (createFormObject)
	    	// to retrieve the entity (it is also accessed to set entity context in setContextFromScope)
	    	// the "id" attribute in the flow input map could either come from a request parameter 
	    	// when the flow is launched as a top level flow or from a parent flow that is providing 
	    	// this as input to the subflow. since entity CRUD flows are typically subflows, "id"
	    	// here typically comes from a parent flow input mapper
	    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
	    	getFlow().setInputMapper(new DefaultAttributeMapper().addMapping(idMapping));
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



