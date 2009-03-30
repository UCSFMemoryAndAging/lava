package edu.ucsf.lava.core.webflow;

import java.util.Map;

import org.springframework.webflow.definition.registry.FlowDefinitionHolder;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistrar;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.FlowServiceLocator;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.webflow.builder.FlowTypeBuilder;


public interface LavaFlowRegistrar extends FlowDefinitionRegistrar {

	
	public String getLocalFormActionName(String actionId);
	public Map<String, Action> getActions();
	public ActionManager getActionManager();
	public FlowServiceLocator getServiceLocator();
	public void registerFlowDefinition(FlowDefinitionHolder flowDefinitionHolder);
	public FlowDefinitionRegistry getFlowRegistry();

}
