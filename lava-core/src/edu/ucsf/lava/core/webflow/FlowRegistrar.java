package edu.ucsf.lava.core.webflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.webflow.definition.registry.FlowDefinitionHolder;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.FlowServiceLocator;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.webflow.builder.FlowInfo;
import edu.ucsf.lava.core.webflow.builder.FlowTypeBuilder;



public class FlowRegistrar implements LavaFlowRegistrar {
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	

    private FlowDefinitionRegistry flowRegistry;
    private FlowServiceLocator serviceLocator;
    private ActionManager actionManager;
    private Map<String,Action> actions;
	private List flowsBuilt = new ArrayList(500){};
    
    
    public FlowRegistrar(FlowServiceLocator serviceLocator, ActionManager actionManager) {
            this.serviceLocator = serviceLocator;
            this.actionManager = actionManager;
          
            this.actions = actionManager.getActionRegistry().getActions();
    }

    //pass through function to encapsulate registry. a given flow has already been assembled into a SWF FlowDefinitionHolder.
    //a flow is assembled within the FlowTypeBuilder for a given flow type. after assembly of one of its flows,
    //the FlowTypeBuilder calls this method to register the flow. any subflows and customizing flows of the flow are
    //guranteed to have been built and registered before the flow can be built and registered (this is managed by the buildFlow 
    //method below)
    public void registerFlowDefinition(FlowDefinitionHolder flowDefinitionHolder){
    	flowRegistry.registerFlowDefinition(flowDefinitionHolder);
    }
	
    
    // the whole flow building process is initiated when the Spring Web Flow infrastructure calls the 
    // doPopulate method on the flow registry (FlowRegistryFactoryBeans). doPopulate creates this registrar
    // and calls this method, which iterates thru the actions and builds all of the flows.
    public void registerFlowDefinitions(FlowDefinitionRegistry registry) {
    	
    	logger.info("registering flow definitions.");
    	//can these lines be added to constructor? Or do we need to wait until the method is called 
    	this.flowRegistry = registry;
    	actions = actionManager.getActionRegistry().getActions();
    	
   	
    	 /*  loop through all actions, calling the appropriate flow type builder. We need to make
         *   sure that all flows are built in the correct order (e.g. all subflows are built before
         *   the parent flows that reference them. This is not necessarily as simple as
         *   etitities before lists.  So, we will do a depth first traversal of the actions
         *   following each actions' subflows collection until we reach an action without subflows.
         *   We will build that action and then recurse back up through the actions building them 
         *   as we go along.  We will maintain a list of built actions so that we know where we have
         *   been. 
     	*/	

    	for (String actionId : actions.keySet()){
    		//get id from the action to avoid using the default project/patient action lookup keys and lava scoped action aliases. 
    		this.buildFlow(actions.get(actionId).getId());
    	}
    	
    	
    	logger.info("flow definition registration completed.");
    }
    
    //  recursive function to build flows (builds all subflows and customizing subflows first)
    protected void buildFlow(String actionId){
    	
    	/** debugging specific flow...
		if (actionId.equals("lava.core.importer.import.import")) {
				logger.debug("stopping prior to building of flow to debug");
		}
    	*/
    	
    	
    	if(!actionManager.shouldBuildFlowsForAction(actionId)){
    		logger.debug("No flow built for actionId: " + actionId );
       		flowsBuilt.add(actionId);
       		return;
    	}
    	
    	//return if flow already built
    	if (flowsBuilt.contains(actionId)) {return;}
    	
    	logger.debug("actionId="+actionId);
    	Action action = actions.get(actionId);
    	if(action == null){
    		logger.error("actionId: " + action.getId() + " not found in actions collection.");
    		return;
    	}
    	//recurse through subFlows building them when needed...
    	for (String subFlowActionId: action.getSubFlows()){
    		// do NOT call shouldBuildFlowForAction on the subFlowActionId at this point, because
    		// if it is a default action which has an instance action defined then should continue
    		// on to getEffectiveAction and then recursively call buildFlow on subFlowActionId so that 
    		// the instance subFlow is built before the parent flow which references it
    			
			Action subFlowAction = actionManager.getEffectiveAction(subFlowActionId);
			logger.debug("Subflow actionId: "+subFlowAction.getId()+" found for actionId: "+action.getId());
			
			//need to obtain the flows that will be built for this subflow...if there are none then we don't want to recurse and
			//build this subflow.
			List<FlowInfo> subFlowInfoList = subFlowAction.getFlowTypeBuilder().getSubFlowInfo(actionId, 
       							action.getFlowType(), subFlowAction.getId(), this.actions); 
       	
			// must put a special conditions here to keep from recursing 
			// forever if a flow is a subflow of itself..e.g. self join model
			if(!subFlowAction.getId().equalsIgnoreCase(actionId) && 
	    			//we also should not recurse if the subflow is the same as the subflow that
					//this action customizes...that would also put us in an infinite loop
					!subFlowAction.getId().equalsIgnoreCase(action.getCustomizedFlow()) &&
	    			// also, if the subFlows would not result in any subFlowStates being built, then
					// do not recurse (e.g. reportLauncher is subflow of every flow, individual reports
					// are subflows of reportLauncher, reportLauncher is subflow of individual reports (because
					// it is a subflow of every flow) but the ReportLauncherFlowTypeBuilder does not
					// build subFlowStates for the individual report flows). to check this, have to obtain 
		    		// the subflows of this subflow to see if there are any.
					!subFlowInfoList.isEmpty()) {
			
	    		// note that it is impossible for flow A to have flow B as a subflow and flow B to 
	    		// have flow A as a subflow, because one could not be built without the existence
	    		// of the other, essentially a deadlock condition. e.g. the training launcher and the
	    		// report launcher are subflows of all flows, which means they are subflows of each
	    		// other and would violate this rule. thus it is up to the flow type builders to 
	    		// exclude a given flow type from being a subflow in its getSubFlowInfo method. in 
	    		// this example, this is resolved as follows: the report launcher flow type builder 
	    		// does not allow the report launcher to be a subflow of the training launcher 
	    		// (because it would not be needed), while the training launcher flow type builder 
	    		// does allow the training launcher to be a subflow of the report launcher (because it 
	    		// could be needed)
				
				buildFlow(subFlowAction.getId());
			}
    	}
    	//recurse through customizingFlows
    	for (String customizingFlowId: action.getCustomizingFlows()){
    		logger.debug("Customizing Subflow actionId: "+customizingFlowId+" found for actionId: "+action.getId());

    		// if customizing flow is itself instance customized then build the instance flow
    		Action customizingAction = actionManager.getAction(customizingFlowId);
			// make sure this is a base "lava" action. 
    		if(customizingAction.getInstance().equals(ActionUtils.LAVA_INSTANCE_IDENTIFIER)){
    			String instanceActionId = ActionUtils.getActionIdWithInstance(customizingAction.getId(), actionManager.getWebAppInstanceName());
    			if (actionManager.getAction(instanceActionId) != null) {
    				customizingFlowId = instanceActionId;
    			}
    		}
    		buildFlow(customizingFlowId);
    	}
    	
    	
    	FlowTypeBuilder builder = action.getFlowTypeBuilder(); 
    	// the flow type builder assembles each flow that belongs to the flow type, by creating a subclass of SWF 
    	// AbstractFlowBuilder which contains the instructions for building the flow definition, and then creating a SWF
    	// FlowAssembler and passing the AbstractFlowBuilder subclass instance to its assembleFlow method.
    	builder.RegisterFlowTypeDefinitions(this, action.getId());
    	logger.debug("Registering flow definition for "+action.getId() + " using builder for flowType " +builder.getType());
    	flowsBuilt.add(action.getId());
    	return;
    }	
		
  

	
	
	public String getLocalFormActionName(String actionId){
		String localFormActionName = null;
		
		// find out if there is a local instance specific action to override this action, i.e.
		// check to see if their is an action with the context path of the current webapp
		// instance as the action's SCOPE_IDENTIFIER
		// if so, override the FormAction used in the flow definition with the local one. 
		
		// note that customizing flows use this facility, e.g. in the mac instance, the action
		// mac.enrollment.status.dimondEnrollmentStatus with flowType="entityFlowAction" results
		// in creation of each of the CRUD flows, all with flow id's beginning with 
		// mac.enrollment.status.dimondEnrollmentStatus
		// this results in the construction of the flow FormAction name "macDimondEnrollmentStatusFormAction"
		// within this method, and this is the bean that is used by the flow. within the flow,
		// this in turn sets the current action to mac.enrollment.status.dimondEnrollmentStatus
		// and CustomViewSelector then resolves to a view path of:
		// local/mac/enrollment/status/dimondEnrollmentStatus which resolves to a jsp
		    	
		String instanceActionId = new StringBuffer(actionManager.getWebAppInstanceName()).
			append(ActionUtils.ACTION_ID_DELIMITER).
			append(ActionUtils.getScope(actionId)).
			append(ActionUtils.ACTION_ID_DELIMITER).
			append(ActionUtils.getModule(actionId)).
			append(ActionUtils.ACTION_ID_DELIMITER).
			append(ActionUtils.getSection(actionId)).
			append(ActionUtils.ACTION_ID_DELIMITER).
			append(ActionUtils.getTarget(actionId)).toString();
		if (actionManager.getActionRegistry().containsAction(instanceActionId)) {
			// if exists, want to change the FormAction bean used in the flow definition,
			localFormActionName = actionManager.getWebAppInstanceName() + StringUtils.capitalize(ActionUtils.getTarget(actionId));
		}
		return localFormActionName;
		
	}



	public Map<String, Action> getActions() {
		return actions;
	}

	public ActionManager getActionManager() {
		return actionManager;
	}

	

	public FlowServiceLocator getServiceLocator() {
		return serviceLocator;
	}

	public FlowDefinitionRegistry getFlowRegistry() {
		return flowRegistry;
	}
	
	

}
