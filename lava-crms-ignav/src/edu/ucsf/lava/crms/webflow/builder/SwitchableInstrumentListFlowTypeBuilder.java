package edu.ucsf.lava.crms.webflow.builder;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.crms.webflow.builder.InstrumentListFlowTypeBuilder;

public class SwitchableInstrumentListFlowTypeBuilder extends
		InstrumentListFlowTypeBuilder {

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		
		
		registry.registerFlowDefinition(assemble(actionId + ".view", 
						new SwitchableInstrumentListViewFlowBuilder(registry,actionId)));
		
			
	}
}
