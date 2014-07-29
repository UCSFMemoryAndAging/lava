package edu.ucsf.lava.core.webflow.builder;


import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

public class StaticPageFlowBuilder extends BaseFlowBuilder {
	
  public StaticPageFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    super(registry, actionId);
    setFlowEvent("view");
  }
  
  public void buildEventStates() throws FlowBuilderException {
    addViewState("view", null, formAction.getCustomViewSelector(),
                  new Action[]{invoke("prepareToRender",formAction)}, 
                  new Transition[] {},
                  null,null,null);
    buildDefaultActionEndState();
  }
}

