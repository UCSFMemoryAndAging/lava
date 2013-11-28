package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.builder.FlowBuilderException;

import edu.ucsf.lava.core.controller.BaseGroupComponentHandler;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.crms.webflow.builder.InstrumentEditStatusFlowBuilder;

public class SwitchableInstrumentEditStatusFlowBuilder extends
		InstrumentEditStatusFlowBuilder {

	public SwitchableInstrumentEditStatusFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
	}

	@Override
	public void buildInputMapper() throws FlowBuilderException {
	   	super.buildInputMapper();
	   	AttributeMapper inputMapper = getFlow().getInputMapper();

	   	/* SWITCHABLE: receive GROUP_MAPPING from parent flow 
		 *   this is so that the navigation bar (actions.jsp) can list the same instrumentGroup
		 *   as of the parent flow instead of seeing nothing and having to recreate it */
	   	Mapping igMapping = mapping().source(GROUP_MAPPING).target("flowScope."+GROUP_MAPPING).value();
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(igMapping));
	}
	
	@Override
	public void buildOutputMapper() throws FlowBuilderException {
		super.buildOutputMapper();
	   	AttributeMapper outputMapper = getFlow().getOutputMapper();
		
	   	// SWITCHABLE: send GROUP_MAPPING back to parent flow
		//   this is just for precaution in case this flow or subflows would have altered it
	   	Mapping igMapping = mapping().source("flowScope."+GROUP_MAPPING).target(GROUP_MAPPING).value();
		getFlow().setOutputMapper(((DefaultAttributeMapper)outputMapper).addMapping(igMapping));
	}
}
