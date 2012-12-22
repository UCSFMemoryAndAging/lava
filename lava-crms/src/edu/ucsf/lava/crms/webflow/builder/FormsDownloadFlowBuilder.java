package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

public class FormsDownloadFlowBuilder extends BaseFlowBuilder {
  public FormsDownloadFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    super(registry, actionId, "formsdownload");
    setFlowEvent("formsDownloadView");
  }
  public void buildEventStates() throws FlowBuilderException {
    addViewState("formsDownloadView", null, formAction.getCustomViewSelector(),
                  new Action[]{invoke("prepareToRender",formAction)}, 
                  new Transition[] {},
                  null,null,null);
    buildDefaultActionEndState();
  }
}



