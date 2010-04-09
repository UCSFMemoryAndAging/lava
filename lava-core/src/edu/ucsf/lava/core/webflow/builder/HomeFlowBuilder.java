package edu.ucsf.lava.core.webflow.builder;


import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

class HomeFlowBuilder extends BaseFlowBuilder {
	
	public HomeFlowBuilder(LavaFlowRegistrar registry, String actionId) {
		// now sure why "home" is explicity passed as the formActionName (which means that even if there
		// is an instance specific home action, its form action should just be homeFormAction and not
		// INSTANCEHomeFormAction). could be because the HomeFlowBuilder was used for both "welcome" and
		// "home" action target parts.
    	super(registry, actionId, "home");
    	setFlowEvent("homeView");
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	
    	addViewState("homeView", null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)}, 
    			new Transition[] {},
    	null,null,null);
    	
    	
    	buildDefaultActionEndState();
    }
}



