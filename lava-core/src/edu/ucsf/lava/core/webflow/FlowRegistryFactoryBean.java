package edu.ucsf.lava.core.webflow;

import java.util.Map;

import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.AbstractFlowBuildingFlowRegistryFactoryBean;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.webflow.builder.FlowTypeBuilder;


/**
 * Populate a flow registry programatically, since all flows are defined programatically as 
 * opposed to XML files.
 *
 */
public class FlowRegistryFactoryBean extends AbstractFlowBuildingFlowRegistryFactoryBean implements ManagersAware {
		
		protected ActionManager actionManager;
		protected WebflowManager webflowManager;
		
		private FlowDefinitionRegistry flowRegistry;
		
    	/*
		 * Because actions are not known at the time this object is instantiated by the spring configuration, 
		 * register a "parent" flowRegistry on the registry created by the underling webflow implementation and put a reference
		 * to this factory bean in the webflow manager.  Then the webflowManager can register flows into this parent registy in the application
		 *  event listener once all the actions have been loaded. 
		 * 
		 */
		protected void doPopulate(FlowDefinitionRegistry registry) {
                this.flowRegistry = registry;
                webflowManager.setFlowRegistryFactoryBean(this);
                
		}
		
		public void initializeFlows(){
			new FlowRegistrar(getFlowServiceLocator(), actionManager).registerFlowDefinitions(flowRegistry);
        }

		public void updateManagers(Managers managers) {
			this.actionManager = CoreManagerUtils.getActionManager(managers);
			this.webflowManager = CoreManagerUtils.getWebflowManager(managers);
		}

		
}



