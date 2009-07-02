package edu.ucsf.lava.core.webflow.builder;


import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowTypeBuilder;


public class HomeFlowTypeBuilder extends BaseFlowTypeBuilder {

	public HomeFlowTypeBuilder() {
		super("home");
		setEvents(new String[]{
				"view"});
		setDefaultFlowMode("view");
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		
		registry.registerFlowDefinition(assemble(actionId + ".view", 
				new HomeFlowBuilder( registry,actionId)));
			
		
		
		
	}

}
