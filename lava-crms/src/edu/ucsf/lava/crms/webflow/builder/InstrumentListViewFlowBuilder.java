package edu.ucsf.lava.crms.webflow.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.engine.support.ConfigurableFlowAttributeMapper;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;
import edu.ucsf.lava.core.webflow.builder.FlowInfo;

/**
 * Java-based flow builder that builds an instrument listing flow.
 * <p>
 * This encapsulates the page flow of viewing a list of instruments 
 * It is typically the root flow of a flow conversation, where the 
 * subflows are the CRUD instrument flows. note that the instrument
 * CRUD subflows could be the shared subflows, i.e.
 * lava.assessment.instrument.instrument.* or instrument specific
 * subflows, e.g. lava.assessment.instrument.medications.*
 */
class InstrumentListViewFlowBuilder extends BaseFlowBuilder {

	public InstrumentListViewFlowBuilder(LavaFlowRegistrar registry,String actionId) {
		super(registry, actionId);
		setFlowEvent("view");
	}	 
    
    public void buildInputMapper() throws FlowBuilderException {
    	// put the "id" into flowScope where is will be accessed in the FormAction to set entity 
    	// context (setContextFromScope), which in turn is used by the list handlers to retrieve 
    	// the list
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since lists are typically top level flows, the
    	// "id" here typically comes from "requestParameter.id"
    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
    	getFlow().setInputMapper(new DefaultAttributeMapper().addMapping(idMapping));
    }

    public void buildEventStates() throws FlowBuilderException {
    	// for clarity only, so easy to see what view it is when debugging
    	String listView = getFlowEvent();
    	
    	ArrayList<Transition> viewTransitions = new ArrayList<Transition>();
    	
    	//add a close transistion for lists that are subflows of other lists
    	// objectName = action target, e.g. "visitInstruments"
    	viewTransitions.add(transition(on(objectName + "__close"), to("finish"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
        
    	// print to PDF
    	viewTransitions.add(transition(on(objectName + "__print"), to("print")));
    	// export to Excel file
    	viewTransitions.add(transition(on(objectName + "__export"), to("export")));
    	
    	// list filter events, navigation, etc. need bind and handleFlowEvent actions
    	// note: this is a catch-all transition, so event-specific transitions should appear before this
    	viewTransitions.add(transition(on(new StringBuffer("${lastEvent.id.startsWith('").append(objectName).append("__')}").toString()), 
        		to(listView), 
        		ifReturnedSuccess(new Action[]{
        			invoke("bind", formAction), 
        			invoke("handleFlowEvent", formAction)})));
    	
    	viewTransitions.addAll(this.buildSubFlowTransitions());
    	
    	// handle patient context change/clear, project context change/clear
      	
        // common actions across instrument entity CRUD subflow transitions below
        
    	addViewState(listView, 
    			null, 
    			formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),		
    			null,
    			null,
    			null);

    	buildDefaultActionEndState();
    }
    
    
    // overriding to support the instrumentGroup subflow, which requires creating an input mapping
    // for the instrumentGroup structure (created in this flow, prior to transitioning to the 
    // instrumentGroup subflow)
    protected List<Transition> buildSubFlowTransitions(){
    	// do the standard loop
    	ArrayList<Transition> transitions = new ArrayList<Transition>();
    	for(String subFlowId : subFlowActionIds){
    		List<FlowInfo> subFlowInfoList = getSubFlowInfo(subFlowId); 
						
    		for(FlowInfo subFlowInfo: subFlowInfoList){
    	    	if (subFlowInfo.getTarget().equals("instrumentGroup")){
		    		// the general problem being solved here is we have variations on an event which should 
		    		// result in different handling for the event, but all variations on the event should
		    		// result in the same transition to the same state
		    		
		    		// for the instrumentGroup subflow the event will be "view", "edit", "delete" etc. (whatever
		    		// action is to be performed on each instrument in the group, where "edit" gets translated
		    		// by the handler to "enter", "enterReview" or "upload" based on the particular instrument
		    		// it will be handled by the InstrumentGroupHandler which is a secondary handler in an instrument 
		    		// list flow)
	
		    		// however, the event may have a suffix appended, e.g. "_prototype" which indicates to the
		    		// InstrumentGroupHandler what the group should be created from. e.g. if no suffix create from
		    		// user selected items, if "_prototype" suffix then create from project-visit group prototype,etc.
		    		
		    		// while the event dictates how the group should be created, the event should transition to 
		    		// the same instrument group subflow state, regardless of whether there is an event suffix or not,
		    		// because once the group is created, all groups are handled in a standard way. so, use
		    		// the startsWith method in the transition
			    	transitions.add(transition(on(new StringBuffer("${lastEvent.id.startsWith('").append(subFlowInfo.getTarget()).append("__").append(subFlowInfo.getEvent()).append("')}").toString()),
			    			to(subFlowInfo.getTarget() + "__" + subFlowInfo.getEvent()), 
							   ifReturnedSuccess(new Action[]{invoke("bind", formAction), invoke("handleFlowEvent", formAction)})));
		    	}
	    	else {
		    		// create standard base transition
			    	transitions.add(transition(on(subFlowInfo.getTarget() + "__" + subFlowInfo.getEvent()), to(subFlowInfo.getTarget() + "__" + subFlowInfo.getEvent())));
		    	}
		    }
    	}
    	
    	return transitions;
    }
    
    // override because different transitions and mappers and action states than the base class version
    // supports, in order to facilitate going from enter subflow to collect subflow, and vice versa, which
    // involve terminating one subflow and having this parent flow transition into the other subflow
    
    // also overriding to support the instrumentGroup subflow, which requires creating an input mapping
    // for the instrumentGroup structure
    protected void buildSubFlowStates(){

    	for(String subFlowId : subFlowActionIds){
    		List<FlowInfo> subFlowInfoList = getSubFlowInfo(subFlowId); 
				
			for(FlowInfo subFlowInfo: subFlowInfoList){
				
		    	// create subflow transitions to be used for the subflow states created below.
				// for the enter and collect subflows, add transitions to support switching
				// from one to the other and vice versa 
		    	Transition[] transitions;
		    	
		    	// this is the standard transition from the end of a subflow back to the listView view state (via
		    	// the subFlowReturnState action state, which refreshes the list data before it is redisplayed)
				Transition subFlowReturnFinishTransition = transition(on("finish"), to("subFlowReturnState"));
				Transition subFlowReturnFinishCancelTransition = transition(on("finishCancel"), to("subFlowReturnState"));
				
		    	// create attribute-mapper input-mapper to pass entity "id" for subflow states created below, which comes
		    	// from the request parameter submitted on each list action link
		        ConfigurableFlowAttributeMapper paramMapper = new ConfigurableFlowAttributeMapper();
		        // when switching from enter subflow to collect subflow or vice versa, those subflows terminate and 
		        // this parent subflow resumes. however, there will not be a request parameter. so need an output mapper
		        // to get the id from the enter or collect subflow, and then need to map either the requestParameter.id
		        // if entering the subflows normally (i.e. user clicking on list action), or the flowScope.id (populated
		        // by the output mapping) if coming thru from enter to collect or vice versa. 
		        paramMapper.addInputMapping(mapping().source("${requestParameters.id != null ? requestParameters.id : flowScope.id}").target("id").value());
		        // add general purpose request parameters passed to all subflows
		        paramMapper.addInputMapping(mapping().source("requestParameters.param").target("param").value());
		        paramMapper.addInputMapping(mapping().source("requestParameters.param2").target("param2").value());
		        paramMapper.addInputMapping(mapping().source("requestParameters.param3").target("param3").value());

				
		    	if (subFlowInfo.getTarget().equals("instrumentGroup")){
			        paramMapper.addInputMapping(mapping().source("flowScope.group").target("group").value());
			        // return the group in an output mapper so that the selected items can be re-selected, since
			        // they are lost when subFlowReturnState does a refresh (which must be done in case the group
			        // flow added a missing instrument)
			        paramMapper.addInputMapping(mapping().source("flowScope.group").target("group").value());
			        paramMapper.addOutputMapping(mapping().source("group").target("flowScope.group").value());
		    		transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition};
		    	}
		    	else if (subFlowInfo.getEvent().equals("enter")) {
		    		// this only applies to "enter" not "enterReview", because do not have a switch to/from collect
			        // flow from enterReview flow. the switch is a legacy of switching between enter and collect 
			        // and since getting rid of "collect" flow will probably get rid of all of this and just go with 
			        // the superclass behavior only collect and enter flows have output mapper, since they go back thru 
			        // this flow when going from one to the other. but does not hurt to have unused output mapping.
		    		
			        paramMapper.addOutputMapping(mapping().source("id").target("flowScope.id").value());
		    		// since the subflow could be for the "instrument" target or for an instrument specific target,
		    		// e.g. "medications", when returning from enter subflow, need to know whether to go to the
		    		// "medications__collect" subflow or the "instrument__collect" subflow. to achieve this, map
		    		// the target into the subflow, and the subflow in turn maps the target back to this parent
		    		// flow into flowScope.target (via an output mapper), and this flow scope target value is then
		    		// used in enterSubFlowReturnState/collectSubFlowReturnState to determine the state to go to 
		    		paramMapper.addInputMapping(mapping().source(new StringBuffer("'").append(subFlowInfo.getTarget()).append("'").toString()).target("target").value());
		            paramMapper.addOutputMapping(mapping().source("target").target("flowScope.target").value());
		    		Transition enterSwitchToCollectTransition = transition(on("finishSwitchToCollect"), to("enterSubFlowReturnState"));
		    		transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition, enterSwitchToCollectTransition};
		    	}
		    	else if (subFlowInfo.getEvent().equals("collect")) {
			        // only collect and enter flows have output mapper, since they go back thru this flow when
			        // going from one to the other. but does not hurt to have unused output mapping.
			        paramMapper.addOutputMapping(mapping().source("id").target("flowScope.id").value());
		    		paramMapper.addInputMapping(mapping().source(new StringBuffer("'").append(subFlowInfo.getTarget()).append("'").toString()).target("target").value());
		            paramMapper.addOutputMapping(mapping().source("target").target("flowScope.target").value());
		    		Transition collectSwitchToEnterTransition = transition(on("finishSwitchToEnter"), to("collectSubFlowReturnState"));
		    		transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition, collectSwitchToEnterTransition};
		    	}
		    	else {
		    		transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition};
		    	}

		    	// create the subflow state for a given target (e.g. "instrument" or "medications") and
		    	// action (e.g. "add","delete","view","enter","collect","upload")
				addSubflowState(subFlowInfo.getTarget()+"__"+subFlowInfo.getEvent(),
						flow(subFlowInfo.getActionId()+ "." + subFlowInfo.getEvent()),
						paramMapper,  
						transitions);
				
    		}
    	}

    	// add action state which refreshes the command components and transitions to getFlowEvent
    	// this way, a parent entity view flow will reflect changes in an entity edit subflow, and
    	// a parent list flow will reflect entities added/deleted in add/delete entity subflows, as
    	// well as entity changes in an entity edit subflow
    	// note that if returning from the group subflow, the refresh handler also re-selects any 
    	// selected items as these are lost when the list is refreshed. the group flow returns the
    	// group in an output mapper (included any new items added in the group subflow) to enable this.
   	addActionState("subFlowReturnState", 
       				invoke("refreshFormObject", formAction), 
   					transition(on("success"), to(getFlowEvent())));
    	
    	addActionState("enterSubFlowReturnState", 
   				invoke("refreshFormObject", formAction), 
					transition(on("success"), to("${flowScope.target}__collect")));
    	
    	addActionState("collectSubFlowReturnState", 
   				invoke("refreshFormObject", formAction), 
					transition(on("success"), to("${flowScope.target}__enter")));
    }
    
}



