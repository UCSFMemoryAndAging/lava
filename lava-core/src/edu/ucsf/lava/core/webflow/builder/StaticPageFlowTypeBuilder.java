package edu.ucsf.lava.core.webflow.builder;

import org.springframework.webflow.definition.registry.FlowDefinitionHolder;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowTypeBuilder;

public class StaticPageFlowTypeBuilder extends BaseFlowTypeBuilder {
	
  public StaticPageFlowTypeBuilder() {
    super("staticPage");
    setEvents(new String[]{"view"});
    setDefaultFlowMode("view");
  }

  public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
    String flowid=actionId + ".view";
    FlowDefinitionHolder flowdef = assemble(flowid, new StaticPageFlowBuilder( registry,actionId));
    registry.registerFlowDefinition(flowdef);
  }

}