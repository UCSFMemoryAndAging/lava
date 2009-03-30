package edu.ucsf.lava.crms.webflow.builder;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowTypeBuilder;


public class WelcomeFlowTypeBuilder extends BaseFlowTypeBuilder {

	public WelcomeFlowTypeBuilder() {
		super("welcome");
		setEvents(new String[]{
				"view"});
		setDefaultFlowMode("view");
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		
		registry.registerFlowDefinition(assemble(actionId + ".view", 
				new WelcomePageFlowBuilder( registry,actionId)));
			
		
		
		
	}

}
