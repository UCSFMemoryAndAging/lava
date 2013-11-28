package edu.ucsf.lava.crms.webflow.builder;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.crms.webflow.builder.SwitchableInstrumentEnterFlowBuilder;
import edu.ucsf.lava.crms.webflow.builder.InstrumentFlowTypeBuilder;
import edu.ucsf.lava.crms.webflow.builder.SwitchableInstrumentStatusFlowBuilder;


public class SwitchableInstrumentFlowTypeBuilder extends InstrumentFlowTypeBuilder {

	public SwitchableInstrumentFlowTypeBuilder() {
		super();
	}
	
	@Override
	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		super.RegisterFlowTypeDefinitions(registry, actionId);

		registry.registerFlowDefinition(assemble(actionId + ".delete", 
			new SwitchableInstrumentDeleteFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".view", 
			new SwitchableInstrumentViewFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".enter", 
			new SwitchableInstrumentEnterFlowBuilder(registry,actionId)));
	
		registry.registerFlowDefinition(assemble(actionId + ".enterReview", 
			new SwitchableInstrumentEnterReviewFlowBuilder(registry,actionId)));

		registry.registerFlowDefinition(assemble(actionId + ".status", 
			new SwitchableInstrumentStatusFlowBuilder(registry,actionId)));

		registry.registerFlowDefinition(assemble(actionId + ".editStatus", 
			new SwitchableInstrumentEditStatusFlowBuilder(registry,actionId)));
		

	}
	

}
