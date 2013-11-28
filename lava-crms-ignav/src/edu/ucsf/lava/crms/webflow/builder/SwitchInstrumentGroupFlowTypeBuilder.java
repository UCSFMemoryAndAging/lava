package edu.ucsf.lava.crms.webflow.builder;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.crms.webflow.builder.InstrumentGroupFlowTypeBuilder;

/* The SwitchInstrumentGroup flow is used to navigate arbitrarily through an instrument group.
 */

public class SwitchInstrumentGroupFlowTypeBuilder extends
		InstrumentGroupFlowTypeBuilder {

	public SwitchInstrumentGroupFlowTypeBuilder() {
		super();
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		
		registry.registerFlowDefinition(assemble(actionId + ".view", 
				new SwitchGroupFlowBuilder(registry,actionId)));

		// the edit action is used to encompass the "enter", "enterReview", "upload" individual
		// instrument actions, which are assumed to be mutually exclusive for a given instrument.
		// this way, an edit group flow can iterate thru individual instruments regardless of which
		// edit-like flow they support
		registry.registerFlowDefinition(assemble(actionId + ".edit", 
				new SwitchGroupFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".status", 
				new SwitchGroupFlowBuilder(registry,actionId)));
			
		registry.registerFlowDefinition(assemble(actionId + ".delete", 
				new SwitchGroupFlowBuilder(registry,actionId)));
		
		registry.registerFlowDefinition(assemble(actionId + ".bulkDelete", 
				new SwitchGroupFlowBuilder(registry,actionId)));
	}
}
