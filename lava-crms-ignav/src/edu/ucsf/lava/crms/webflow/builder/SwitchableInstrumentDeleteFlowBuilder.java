package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;
import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_INDEX;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.builder.FlowBuilderException;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.crms.webflow.builder.InstrumentDeleteFlowBuilder;

public class SwitchableInstrumentDeleteFlowBuilder extends
		InstrumentDeleteFlowBuilder {

	public SwitchableInstrumentDeleteFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
	}

	@Override
	public void buildInputMapper() throws FlowBuilderException {
	   	super.buildInputMapper();
	   	AttributeMapper inputMapper = getFlow().getInputMapper();

	   	/* SWITCHABLE: receive GROUP_MAPPING & GROUP_INDEX from parent flow 
		 *   this is so that the BaseGroupComponentHandler's can add
		 *   the correct instrumentGroup backingobject even during deletes.  After deleting,
		 *   the second handler (SwitchInstrumentGroupHandler) updates this instrumentGroup,
		 *   altering the GROUP_INDEX as a result */
	   	Mapping groupMapping = mapping().source(GROUP_MAPPING).target("flowScope."+GROUP_MAPPING).value();
	   	Mapping groupIndexMapping = mapping().source(GROUP_INDEX).target("flowScope."+GROUP_INDEX).value();
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(groupMapping).addMapping(groupIndexMapping));
	}
	
	public void buildOutputMapper() throws FlowBuilderException {
		super.buildOutputMapper();
	   	AttributeMapper outputMapper = getFlow().getOutputMapper();
	   	
		// SWITCHABLE: return GROUP_INDEX back to parent since it would have changed if deleted
		Mapping groupIndexMapping = mapping().source("flowScope."+GROUP_INDEX).target(GROUP_INDEX).value();
		getFlow().setOutputMapper(((DefaultAttributeMapper)outputMapper).addMapping(groupIndexMapping));
	}
}
