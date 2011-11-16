package edu.ucsf.lava.core.webflow.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.engine.FlowExecutionExceptionHandler;
import org.springframework.webflow.engine.State;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.TransitionCriteria;
import org.springframework.webflow.engine.builder.AbstractFlowBuilder;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.engine.support.ActionTransitionCriteria;
import org.springframework.webflow.engine.support.ConfigurableFlowAttributeMapper;
import org.springframework.webflow.engine.support.TransitionCriteriaChain;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.ScopeType;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.webflow.CustomFlowExceptionHandler;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

// class that adds functionality to AbstractFlowBuilder that is common
// to all FlowBuilders
public abstract class BaseFlowBuilder extends AbstractFlowBuilder {
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	protected String objectName;  
	protected String formActionName;
	protected String actionId;
	protected edu.ucsf.lava.core.action.model.Action lavaAction;
	// flowEvent is named after the event or sequence of events that the flow handles, e.g. edit, 
	// view, collect. it is used to name the first state of the flow following the start states
	protected String flowEvent;  
	// the initial view state is named after flowEvent, but if the flow has multiple view states
	// keep track of the most recent in a flow scope attribute when transitioning to subflows so
	// that if the subflow is unauthorized, can return to the correct view state
	protected String mostRecentViewState; 
	protected LavaFlowRegistrar registry;
	protected Map<String,edu.ucsf.lava.core.action.model.Action> actions;
	protected LavaComponentFormAction formAction;
	protected List<String> subFlowActionIds;
    protected ConfigurableFlowAttributeMapper flowScopeIdMapper;
    protected ConfigurableFlowAttributeMapper subflowInputOutputMapper;
    
	//convenience constructor -- objectName is usually the action target
	public BaseFlowBuilder(LavaFlowRegistrar registry,	String actionId){
		this(registry,actionId,null);
	}
	

	public BaseFlowBuilder(LavaFlowRegistrar registry,	String actionId, String formActionName) {
        super(registry.getServiceLocator());
        
        //setup local variables with useful references`
        this.registry = registry;
        this.actions = registry.getActions();
        this.subFlowActionIds = actions.get(actionId).getSubFlows();
        // this is the mapper for passing the flowScope "id" attribute to custom subflows
        flowScopeIdMapper = new ConfigurableFlowAttributeMapper();
        flowScopeIdMapper.addInputMapping(mapping().source("flowScope.id").target("id").value());
        
        // this is the mapper for passing attributes (parameters) to and from subflows, e.g. passing request parameters for list CRUD actions
        subflowInputOutputMapper = new ConfigurableFlowAttributeMapper();
        
        // input mappers
        // note: any subflows which will receive an attribute value from a parent flow must implement buildInputMapper
        // and have an inputMapping for each inputMapping below that it will use
        
        // add the "id" request parameter to be passed to all subflows
        subflowInputOutputMapper.addInputMapping(mapping().source("${requestParameters.id != null ? requestParameters.id : flowScope.id}").target("id").value());        
        // add general purpose request parameters passed to all subflows
        subflowInputOutputMapper.addInputMapping(mapping().source("requestParameters.param").target("param").value());
        subflowInputOutputMapper.addInputMapping(mapping().source("requestParameters.param2").target("param2").value());
        subflowInputOutputMapper.addInputMapping(mapping().source("requestParameters.param3").target("param3").value());
        subflowInputOutputMapper.addInputMapping(mapping().source("requestParameters.param4").target("param4").value());
        
        // output mappers
        // note: any subflows which will return an attribute via an outputMapper must implement buildOutputMapper 
        // and have a mapping for each outputMapping below that it will use to return the attribute value to
        // the parent flow. also, the subflow handler will need to put the value into flowScope so that the subflow
        // outputMapper can retrieve the value and in order to map it in buildOutputMapper
        
        // for flows that add a new entity, this will allow the parent flow to get the id of that new entity from
        // flowScope.newId 
        subflowInputOutputMapper.addOutputMapping(mapping().source("subflowNewId").target("flowScope.subflowNewId").value());
        // have subflows return their action id in case the parent flow needs to know which subflow just returned
        subflowInputOutputMapper.addOutputMapping(mapping().source("subflowActionId").target("flowScope.subflowActionId").value());
        //TODO: consider adding output parameters "outParam", "outParam1", etc. to subflowInputOutputMapper, akin to 
        //the "param" input parameters so that subflows can return additional data to their parent as part of the 
        //build-in flow functionality
        
        this.formActionName = formActionName;
        if (actionId == null){
        	logger.error("FlowBuilder created without actionId");
        	return;
        }
        
        this.actionId = actionId;
        this.objectName =  this.getBaseObjectName(actionId);
        // localFormActionBeanNamePrefix facilitates using instance specific FormAction beans 
        String localFormActionName = registry.getLocalFormActionName(actionId);
        
        
        //setup FormAction
        if (this.formActionName !=null){
        	this.formAction = (LavaComponentFormAction) getFlowServiceLocator().getAction(this.formActionName + "FormAction");
        }else if (localFormActionName != null) {
        	this.formAction = (LavaComponentFormAction) getFlowServiceLocator().getAction(localFormActionName + "FormAction");
        } else {
        	// use the default FormAction bean (all instruments will share a flow be instrumentFormAction)
       		this.formAction = (LavaComponentFormAction) getFlowServiceLocator().getAction(actions.get(actionId).getTarget() + "FormAction");
       	}
	}
	
// FlowBuilder interface method implementations	(see Interface FlowBuilder javadocs)
	// The FlowBuilder public methods are invoked by a webflow FlowAssembler to build a flow definition. 
	// Some of these, like buildInputMapper, are defined in the specific flow subclasses. 
	
	/**
	 * buildInputMapper
	 * 
	 * If subclasses override, they should first call this superclass method and then obtain the
	 * AttributeMapper that was set here, pass it to setInputMapper while calling its add method
	 * to add additional input mappings, e.g.
	 *   super.buildInputMapper();
	 *   AttributeMapper inputMapper = getFlow().getInputMapper();
	 *   Mapping idMapping = mapping().source("id").target("flowScope.id").value();
	 *   getFlow().setInputMapper((DefaultAttributeMapper)inputMapper).addMapping(idMapping));
	 */
	public void buildInputMapper() throws FlowBuilderException {
		// make the "param", "param1" and "param2" request parameters available to all subflows.
		// this means that whenever a subflow state is created, the subflowInputOutputMapper
		// should be passed as the FlowAttributeMapper
    	Mapping paramMapping = mapping().source("param").target("flowScope.param").value();
    	Mapping param2Mapping = mapping().source("param2").target("flowScope.param2").value();
    	Mapping param3Mapping = mapping().source("param3").target("flowScope.param3").value();
    	Mapping param4Mapping = mapping().source("param4").target("flowScope.param4").value();
    	getFlow().setInputMapper(new DefaultAttributeMapper().addMapping(paramMapping).addMapping(param2Mapping).addMapping(param3Mapping).addMapping(param4Mapping));
	}
	
    public void buildStates() throws FlowBuilderException {
    	buildStartStates();
    	buildEventStates();
        buildContextChangeState();
    	buildSubFlowStates();
    	buildErrorState();
    	buildFinishEndStates();
    	buildUnauthorizedEndState();
    	addEndState("unhandled");
    }

    public void buildGlobalTransitions() throws FlowBuilderException {
    	// warning: some subclasses override this
    	
    	// to support the flow authorization scheme, whenever a subflow which has been started is
    	// unauthorized (note: have to start the flow to obtain the entity to determine whether or not
    	// the action is authorized), the subflow terminates signalling the "unauthorized" event to
    	// the parent flow, which uses a global transition to return to its view state (which will
    	// display the ObjectError message). 
    	// note: when entering a root flow that is not authorized, the end state will redirect to the 
    	// error page, and there is no global transition involved
    	
    	// to support context change events which result in a new flow execution (i.e. a
    	// FlowDefinitionRedirect), all flows should have a transition on the "defaultAction"
    	// and "contextChange" events;
    	
    	// when a defaultAction event occurs, the transition goes to the "defaultAction" 
    	// end state of the flow. if the flow is a subflow, the subflow terminates and the parent
    	// flow receives the "defaultAction"  which is why
    	// these events must be handled. the parent flow in turn transitions to its own end states
    	// for these events. this continues until the flow execution reaches the root flow. in a 
    	// root flow, these end states go to the views specified for them, which are FlowDefinitionRedirects
    	// to the defaultAction  views, respectively
    	
    	// note: this means that all flows which can generate the "defaultAction"  context events, 
    	// as well as all flows which have subflows that can 
    	// generate these events, should have "defaultAction"  end
    	// state.   flows which exist within a modal
    	// decorator probably should not generate the context events (because context switching 
    	// is not allowed when modal) and if they do not have subflows which generate the context
    	// events, they do not need the end states or these global transitions, but it does not
    	// hurt for them to have them
    	this.getFlow().getGlobalTransitionSet().addAll(new Transition[] {
    			// when subflow is unauthorized, return to the view state which was active when the
    			// flow was suspended and the subflow started 
    			transition(on("unauthorized"), to("${flowScope.mostRecentViewState}")),
    			transition(on("defaultAction"), to("defaultAction")),
    			transition(on(new String("${lastEvent.id.endsWith('__contextChange') || lastEvent.id.endsWith('__contextClear')}")), to("contextChangeDecision"),
    		    		ifReturnedSuccess(new Action[]{
    		            		invoke("bind", formAction), 
    		            		invoke("handleFlowEvent", formAction)}))
    			});
    }
    
    
    
    public void buildExceptionHandlers() throws FlowBuilderException {
    	FlowExecutionExceptionHandler handler = new CustomFlowExceptionHandler(); 
   		this.getFlow().getExceptionHandlerSet().add(handler);
    }

    
// ABSTRACT METHOD     
	//implement this function to define all states / transisitons specific to the event
	protected abstract void buildEventStates() throws FlowBuilderException;
	
    
    
// HELPER METHODS    
	//Get the target of the base actionId
	private String getBaseObjectName(String actionId){
		String baseActionId = getBaseActionId(actionId);
		return actions.get(baseActionId).getTarget();
		
	}
	
	//gets the actionId of the customized action if any (finds base action underlying any length customizing chain) 
	//returns itself if it does not customize any other flows/actions
	//should probably be added to the actionService
	private String getBaseActionId(String actionId){
		if(actions.get(actionId).getCustomizedFlow()!=null){
			return getBaseActionId(actions.get(actionId).getCustomizedFlow());
		}
		return actionId;
		
	}
	
    protected void buildStartStates(){
    	List<String> customFlows = this.registry.getActions().get(actionId).getCustomizingFlows();
    	Iterator<String> iterator = customFlows.iterator();
    	
    	
        
        
    	Transition nextEventTransition;
    	String customSubFlowId = null;
    	String customSubFlow = null;
    	if(iterator.hasNext()){
    		// what this is doing is that if the flow has a customizing flow, that customizing
    		// flow will be given the opportunity to handle the flow, where handling the flow
    		// means a) the customizing flow handler will be used, and b) the current action will
    		// be set for the customizing flow which will in turn result in using the view (jsp)
    		// corresponding to the customizing flow 
    		// note: to handle the flow, the customizing flow handler must override and return the 
    		// success event from preSetupFlowDirector and the continue event from postSetupFlowDirector
    		customSubFlowId = iterator.next();
    		customSubFlow = ActionUtils.getTarget(customSubFlowId) + "__" + getFlowEvent();
    		nextEventTransition = transition(on("continue"),to(customSubFlow));
    	}else{
    		// if there is not a customizing flow, then all of the flowSetupState actions will
    		// execute and postSetupFlowDirector will return the continue event resulting in
    		// a transition to the initial view state for the flow
    		nextEventTransition = transition(on("continue"),to(getFlowEvent()));
    	}
    	
    	//build Start State  (can either continue to standard event or to a custom subflow. 
    	// if this is for a custom subflow, it can also return unhandled if it does not handle the 
    	// flow for this situation.
    	//Action States execute a series of Actions until a transition matches the event
    	//returned by the Action (i.e. invoke) or, until the list of Actions is exhausted
    	
    	// TODO: currently, even if a customizing flow will take effect, the following actions are
    	// all executed for the base flow first, which is unnecessary. instead, a "hasCustomizingFlow" 
    	// method could be the first method invoked and if it returns true then it should transition to that
    	// customizing flow (i.e. to the subflow state for the customizing flow), skipping all of these actions. 
    	// however, just because a customizing flow exists does not mean it will handle the flow in a given 
    	// case, so if it does not, then these actions need to be executed for the base action in the case 
    	// where no customizing flow handles it. 
    	// since there are relatively few customizing flows, this was not done initially and the performance hit 
    	// has not been large
    	
    	addActionState("flowSetupState", null,
       			new Action[]{
       				// returns "success", "unhandled" or global transition event
       				invoke("preSetupFlowDirector", formAction),
       				// returns "success" unless exception thrown
       				invoke("setupForm", formAction),
    				// returns "success", "unauthorized"
   					invoke("authorizationCheck", formAction),
   					// initialize the most recent view state to the initial view state, used for returning to the 
   					// desired view state when subflows return if they are unauthorized. view states other 
   					// than the initial which transition into subflows should set this flow scope attribute to their
   					// view state in a transition action, prior to transitioning to the subflow
   					invoke("initMostRecentViewState", formAction),
       				// return "continue", "unhandled" or global transition event
       				invoke("postSetupFlowDirector", formAction)},
       			new Transition[] {
    				transition(on("unauthorized"), to("unauthorized")),    			
       				nextEventTransition,
       				//only the custom subflows will use the unhandled end state
       				transition(on("unhandled"), to("unhandled"))},
       			null,null,null);
        	
    	//if there were no custom states then return
    	while (customSubFlow != null){
    		String currentSubFlowId = customSubFlowId;
    		String currentSubFlow = customSubFlow;
        	
    		if(iterator.hasNext()){
    			// when a custom flow does not handle the flow and their are more custom flows,
    			// transition from the custom subflow directly to the next custom subflow
    			customSubFlowId = iterator.next();
    			customSubFlow = ActionUtils.getTarget(customSubFlowId) + "__" + getFlowEvent();
    			nextEventTransition = transition(on("unhandled"),to(customSubFlow));
        	}else{
        		// when a custom flow does not handle the flow and their are no more custom
        		// flows, transition from teh custom subflow to the event state of the default flow
        		customSubFlow = null;
        		customSubFlowId = null;
        		nextEventTransition = transition(on("unhandled"),to(getFlowEvent()));
        	}
    		// add a subflow for each custom flow, which will either handle the flow and signal "finish"
    		// or not handle the flow and signal "unhandled". if the custom flow did handle the flow, 
    		// the default flow should transition directly to its "finish" end state to terminate itself.
    		addSubflowState(currentSubFlow, flow(currentSubFlowId + "." + getFlowEvent()), flowScopeIdMapper,
    				new Transition[] {nextEventTransition,
				transition(on("finish"), to("finish")),
				transition(on("finishCancel"), to("finishCancel"))});
		}
    	
			
    	
    }
    
    protected List<FlowInfo> getSubFlowInfo(String subFlowId){
		if(!actions.containsKey(subFlowId)){
			return new ArrayList<FlowInfo>(){};
		}
    	edu.ucsf.lava.core.action.model.Action subFlowAction =  actions.get(subFlowId);
    	FlowTypeBuilder subFlowTypeBuilder = subFlowAction.getFlowTypeBuilder();
		return subFlowTypeBuilder.getSubFlowInfo(this.actionId,
					actions.get(this.actionId).getFlowType(), subFlowAction.getId(), this.actions); 
	
    }
    
    protected List<Transition> buildSubFlowTransitions(){
    	//	 create attribute-mapper input-mapper to pass "id" to view/edit/delete subflows
    	
    	ArrayList<Transition> transitions = new ArrayList<Transition>();
	    
    	for(String subFlowId : subFlowActionIds){
    		List<FlowInfo> subFlowInfoList = getSubFlowInfo(subFlowId); 
						
			for(FlowInfo subFlowInfo: subFlowInfoList){
			    	transitions.add(transition(on(subFlowInfo.getTarget() + "__" + subFlowInfo.getEvent()), to(subFlowInfo.getTarget() + "__" + subFlowInfo.getEvent()))); 
		    }
    	}
    	
    	return transitions;
    }
    
    protected void buildSubFlowStates(){
    	// this buildSubFlowStates method handles the "automated" building of subFlow states based on
    	// action parent/child relationships defined for actions. but additional subFlow states which
    	// are not reflected in those relationships may be needed, and so there may be subFlow states
    	// beyond those created in this method, in the buildEventStates method of subclasses
    	
    	for(String subFlowId : subFlowActionIds){
    		List<FlowInfo> subFlowInfoList = getSubFlowInfo(subFlowId); 
				
    		for(FlowInfo subFlowInfo: subFlowInfoList){
    			// if there is an instance specific version of the subflow, it replaces the subflow
    			// (but only if the subflow action has an actual flow, i.e. flowType is not "none")
    			// this is determined in ActionService. getEffectiveActionIdForFlowId
				addSubflowState(subFlowInfo.getTarget()+"__"+subFlowInfo.getEvent(),
						flow(registry.getActionManager().getEffectiveAction(subFlowInfo.getActionId()).getId() + "." + subFlowInfo.getEvent()),
						subflowInputOutputMapper,  
						new Transition[] {transition(on("finish"), to("subFlowReturnState")),
										  transition(on("finishCancel"), to("subFlowReturnState"))});						
    		}
    	}

    	// add action state(s) which refresh the command components and transition to getFlowEvent.    	
    	// this way, a parent entity view flow will reflect changes in an entity edit subflow, and
    	// a parent list flow will reflect entities added/deleted in add/delete entity subflows, as
    	// well as entity changes in an entity edit subflow
    	buildSubFlowReturnStates();
    }
    
	// this implementation covers the simple case where all of a flow's subflows return to the same 
    // state and that state does a refresh and returns to the flowEvent state. otherwise, override.
    protected void buildSubFlowReturnStates() {
    	addActionState("subFlowReturnState", 
    		invoke("refreshFormObject", formAction), 
    		transition(on("success"), to(getFlowEvent())));
    }
    
    
    // this is optionally called by flow builders that need custom events for a state's transitions.
    // pass in objectName instead of using member field so that it can be overridden if needed when
    // composing the custom events (e.g.if custom event is for a secondary FormAction handler)
    protected List<Transition> buildCustomEventTransitions(String objectName){
        // action events "custom", "custom2" and "custom3" provide a generic way for handling
        // custom events that return to the view, while "customEnd", "customEnd2" or "customEnd3"
    	// provide a generic way for handling custom events that finish the flow, i.e. transition
    	// to the "finish" end state
    	
    	// note: all of the transitions invoke customBind so that Spring will bind the request parameters
    	// to the form properties, i.e. the model object or DTO properties. if using these events with
    	// an instrument, because customBind is being used instead of customBindResultFields, there
    	// will be no required field errors detected, and any comboRadioSelect controls will not be
    	// bound properly. if either of these become an issue, then we will need a set of custom events
    	// for standard entities and a separate set of custom events for instruments
    	
    	ArrayList<Transition> transitions = new ArrayList<Transition>();
    	transitions.add(transition(on(objectName + "__custom"), to(getFlowEvent()), 
           	ifReturnedSuccess(new Action[]{
           		invoke("customBind", formAction), 
           		invoke("handleFlowEvent", formAction)})));
    	transitions.add(transition(on(objectName + "__custom2"), to(getFlowEvent()), 
               	ifReturnedSuccess(new Action[]{
               		invoke("customBind", formAction), 
               		invoke("handleFlowEvent", formAction)})));
    	transitions.add(transition(on(objectName + "__custom3"), to(getFlowEvent()), 
               	ifReturnedSuccess(new Action[]{
               		invoke("customBind", formAction), 
               		invoke("handleFlowEvent", formAction)})));
    	transitions.add(transition(on(objectName + "__customEnd"), to("finish"), 
           	ifReturnedSuccess(new Action[]{
           		invoke("customBind", formAction), 
           		invoke("handleFlowEvent", formAction)})));
    	transitions.add(transition(on(objectName + "__customEnd2"), to("finish"), 
               	ifReturnedSuccess(new Action[]{
               		invoke("customBind", formAction), 
               		invoke("handleFlowEvent", formAction)})));
    	transitions.add(transition(on(objectName + "__customEnd3"), to("finish"), 
               	ifReturnedSuccess(new Action[]{
               		invoke("customBind", formAction), 
               		invoke("handleFlowEvent", formAction)})));
    	return transitions;
    }
	
    
    // error view-state
	// CustomFlowExceptionHandler, configured in buildExceptionHandlers, handles uncaught
	// exceptions and directs them to this view-state.
	// every flow definition must call this method within buildStates so it has an error view-state
    protected State buildErrorState() {
    	// errorReferenceData adds the reference data which the error page needs to create the correct links
    	// for the tabs
    	return addViewState("error", "/error", invoke("errorReferenceData",formAction), (Transition) null);
    }
    
	
	// a variation on AbstractFlowBuilder ifReturnedSuccess that allows multiple
	// actions to execute in a transition, in a given sequence, and if one of them 
	// fails, no more actions execute and flow does not transition out of the state
	protected TransitionCriteria ifReturnedSuccess(Action[] actions) {
		TransitionCriteriaChain chain = new TransitionCriteriaChain();
		for (Action action : actions) {
			chain.add(new ActionTransitionCriteria(action));
		}
		return chain;
	}
	
	
	
	

	// list navigation transition builder
	// for entity/instrument flows that either have a secondary handler which is a BaseListComponentHandler, or just a 
	// secondary component for detail records, where the primary handler handles the list navigation for this secondary
	// component (e.g. upload instruments, in which case only prevPage, nextPage and recordNav are needed for simple 
	// reference list navigation, so InstrumentHandler only has handlers for those)
	protected Transition buildListNavigationTransitions(String viewState) {
		return transition(on(new String("${lastEvent.id.endsWith('__prevPage') || lastEvent.id.endsWith('__nextPage') ||" +
				" lastEvent.id.endsWith('__recordNav') || lastEvent.id.endsWith('__pageSize') || lastEvent.id.contains('__sort_') || lastEvent.id.endsWith('__clearSort') ||" +
				" lastEvent.id.endsWith('__clearFilter') || lastEvent.id.endsWith('__toggleFilter') || lastEvent.id.endsWith('__applyFilter')}")),
				to(viewState), 
      		ifReturnedSuccess(new Action[]{
        			invoke("bind", formAction), 
        			invoke("handleFlowEvent", formAction)}));
	}
	
	

	
	
	
	protected void buildFinishEndStates() {
		// if this is a subflow, terminate and signal event "finish" or "finishCancel" to the parent flow,
		// where "finishCancel" is used when the user cancels out of the flow
		// if this is a root flow, start a new flow which is logically the most natural choice for this
		// flow, i.e. the defaultPatientActionViewSelector
		// note: in rare situations, for entity's where there may not be a current patient in context,
		// e.g. Doctor, getDefaultPatientActionViewSelector resolves to getDefaultProjectActionViewSelector
		addEndState("finish", 
						new Action[]{new SetAction(settableExpression("actionId"), ScopeType.FLOW, expression("'" + this.actionId + "'"))},
						formAction.getDefaultActionViewSelector(),
						null,
						null,
						null);
		
		addEndState("finishCancel", 
				new Action[]{new SetAction(settableExpression("actionId"), ScopeType.FLOW, expression("'" + this.actionId + "'"))},
				formAction.getDefaultActionViewSelector(),
				null,
				null,
				null);
	}

	protected State buildUnauthorizedEndState() {
		// if this is a subflow, terminate and signal event "unauthorized" to the parent flow
		// if this is a root flow, redirect to the error page
		//TODO: when action history implemented, if this is a root flow, redirect to the root flow
		// of the previous user action
		//UPDATE: this is not working for root flows because the redirect results in HTTP 404, the 
		// page could not be found. for the moment, for root flows, throwing an exception which takes
		// them to the error page via the CustomFlowExceptionHandler. 
		// solutions: so that the code does not have to throw the exception (and have separate logic 
		// for root flows and subflows when unauthorized) could create an "error" flow and then here 
		// use a FlowDefinitionRedirect redirect to that flow, or, create an error controller which
		// just shows the error jsp, but the trick is that the error page needs to have the core '
		// reference data in the model, so really want a solution integrated with LavaComponentFormAction
		return addEndState("unauthorized", "externalRedirect:/error");
	}
	
		    
   
	//contextChange decision state
  
    	
	protected State buildContextChangeState() {
		return addDecisionState("contextChangeDecision", null,
			new Transition[] {
				// if context change requires a redirect to the default action, start a new flow at defaultAction
				transition(on(new StringBuffer("${requestScope.contextChangeResult").append(" == 'defaultAction'}").toString()), to("defaultAction")),
					
				transition(on(new StringBuffer("${requestScope.contextChangeResult").append(" != 'defaultAction'}").toString()), to(getFlowEvent()),
					ifReturnedSuccess(new Action[]{
       					new SetAction(settableExpression("eventOverride"), ScopeType.FLASH, 
       							expression(new StringBuffer("'").append(objectName).append("__refresh'").toString())),
						invoke("handleFlowEvent",formAction)}))},
						null, null, null);	
			}
		
	protected State buildDefaultActionEndState() {
	  	return addEndState("defaultAction", 
    			null,
    			formAction.getDefaultActionViewSelector(),
    			null,
    			null,
    			null);
	}
	
	
	public String getFlowEvent() {
		return flowEvent;
	}

	public void setFlowEvent(String flowEvent) {
		this.flowEvent = flowEvent;
	}
	
}
