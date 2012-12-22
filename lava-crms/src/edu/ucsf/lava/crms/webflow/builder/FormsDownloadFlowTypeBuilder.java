package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.webflow.definition.registry.FlowDefinitionHolder;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowTypeBuilder;

public class FormsDownloadFlowTypeBuilder extends BaseFlowTypeBuilder {
  public FormsDownloadFlowTypeBuilder() {
    super("formsdownload");
    setEvents(new String[]{"view"});
    setDefaultFlowMode("view");
  }

  public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
    String flowid=actionId + ".view";
    FlowDefinitionHolder flowdef = assemble(flowid, new FormsDownloadFlowBuilder( registry,actionId));
    registry.registerFlowDefinition(flowdef);
  }

}
