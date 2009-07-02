package edu.ucsf.lava.core.webflow.builder;


import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

class HomeFlowBuilder extends BaseFlowBuilder {
	
	public HomeFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId, "home");
    	setFlowEvent("homeView");
 
    }
    
    // the welcome page flow just handles patient/project context events, which make the
    // context change and just return to the welcome page, because the welcome page is both the 
    // defaultPatientAction and defaultProjectAction for the module/section of the welcome
    // page (mylava/welcome)
    public void buildEventStates() throws FlowBuilderException {
    	
    	
    	addViewState("homeView", null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)}, 
    			new Transition[] {},
    	null,null,null);
    	
    	
    	buildDefaultActionEndState();
    }
}



