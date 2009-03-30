package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

class WelcomePageFlowBuilder extends BaseFlowBuilder {
	
	public WelcomePageFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId, "welcome");
    	setFlowEvent("welcomePageView");
 
    }
    
    // the welcome page flow just handles patient/project context events, which make the
    // context change and just return to the welcome page, because the welcome page is both the 
    // defaultPatientAction and defaultProjectAction for the module/section of the welcome
    // page (mylava/welcome)
    public void buildEventStates() throws FlowBuilderException {
    	
    	
    	addViewState("welcomePageView", null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)}, 
    			new Transition[] {},
    	null,null,null);
    	
    	
    	buildDefaultActionEndState();
    }
}



