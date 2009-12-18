package edu.ucsf.lava.crms.webflow.builder;

import java.util.ArrayList;
import java.util.List;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.engine.support.ConfigurableFlowAttributeMapper;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.ScopeType;

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
public class InstrumentListViewFlowBuilder extends BaseFlowBuilder {

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
    	
    	// the "instrument__switch" event is used to switch from one instrument subflow of this list flow
    	// to another instrument subflow, with just a single user action. this event should not apply
    	// to the list flow itself, but for the convenience of being able to use the same actions that
    	// are available in the instrument subflows (e.g. when the instruments are non-modal and the
    	// actions appear in the left navigation panel) while the parent list flow is current, have 
    	// a transition here which simply transitions into the subflow specified by the "switchEvent"
    	// request parameter
    	viewTransitions.add(transition(on("instrument__switch"), to("${requestParameters.switchEvent}"))); 
        
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
    // supports, in order to facilitate switching from one instrument subflow to another instrument's
    // subflow, which involve terminating one subflow and having this parent flow transition into the other 
    // subflow
    
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

				// general notes on mappers:
				// subflow state input mappings put data into the input map for the subflows buildInputMapper method
				// subflow state output mappings take data from the output map that the subflows buildOutputMapper method put there		    		
				
		    	// create attribute-mapper input-mapper to pass entity "id" for subflow states created below, which comes
		    	// from the request parameter submitted on each list action link
		        ConfigurableFlowAttributeMapper paramMapper = new ConfigurableFlowAttributeMapper();
		        // when switching from enter subflow to collect subflow, those subflows terminate and this parent subflow 
		        // resumes. however, there will not be a request parameter. so need an output mapper
		        // to get the id from the enter or collect subflow, and then need to map either the requestParameter.id
		        // if entering the subflows normally (i.e. user clicking on list action), or the flowScope.id (populated
		        // by the output mapping) if coming thru from enter to collect or vice versa. 
		        paramMapper.addInputMapping(mapping().source("${requestParameters.id != null ? requestParameters.id : flowScope.id}").target("id").value());
		        // add general purpose request parameters passed to all subflows
		        paramMapper.addInputMapping(mapping().source("requestParameters.param").target("param").value());
		        paramMapper.addInputMapping(mapping().source("requestParameters.param2").target("param2").value());
		        paramMapper.addInputMapping(mapping().source("requestParameters.param3").target("param3").value());
		        
		    	if (subFlowInfo.getTarget().equals("instrumentGroup")){
			        // return the group in an output mapper so that the selected items can be re-selected, since
			        // they are lost when subFlowReturnState does a refresh (which must be done in case the group
			        // flow added a missing instrument)
			        paramMapper.addInputMapping(mapping().source("flowScope.group").target("group").value());
			        paramMapper.addOutputMapping(mapping().source("group").target("flowScope.group").value());
		    		transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition};
		    	}
		    	
		    	// for now, only implementing the "switchEvent" transition for the subflows which use it. to 
		    	// add an additional subflow, that flow builder will have to handle the "instrument__switch" event,
		    	// store the id for the instrument to switch to in flow scope as "id", store the subflow state to 
		    	// switch toin flow scope as "switchEvent" (because the event names match subflow state ids),
		    	// do anything that might need to be done before terminating the flow, and transitioning to 
		    	// a "finishSwitch" end state
		    	else if (subFlowInfo.getEvent().equals("enter") || subFlowInfo.getEvent().equals("collect")
		    			|| subFlowInfo.getEvent().equals("view")) {

		    		// note: these subflow definitions can also be used to switch between "enter" and "collect" 
		    		// flow for an instrument, which was formerly done using a similar but less general technique.
		    		// to do this, the "id" passed back via the subflow output mapper is the same as the "id"
		    		// of the instrument in the subflow that is being terminated, i.e. still working with the
		    		// same instrument, just switching flows.
		    		// the switching does not apply "enterReview", because do not have a switch to/from collect 
		    		// flow from enterReview flow. however, getting rid of "collect" flow for now, so this 
		    		// switching is not used for that purpose right now.

		    		// the subflow must map to the target "id" in its buildOutputMapper method, to pass
		    		// back the id of the instrument to switch to, and to the target "switchEvent", to 
		    		// specify which subflow to transition to
			        paramMapper.addOutputMapping(mapping().source("id").target("flowScope.id").value());
			        paramMapper.addOutputMapping(mapping().source("switchEvent").target("flowScope.switchEvent").value());
			        // transition directly to the specified subflow for the specified instrument 
		    		Transition switchTransition = transition(on("finishSwitch"), to("${flowScope.switchEvent}"));
		    		transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition, switchTransition};
		    	}
		    	else {
		    		transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition};
		    	}

		    	// create the subflow state for a given target (e.g. "instrument" or "medications") and
		    	// action (e.g. "add","delete","view","enter","collect","upload")
				addSubflowState(subFlowInfo.getTarget()+"__"+subFlowInfo.getEvent(),
						flow(registry.getActionManager().getEffectiveAction(subFlowInfo.getActionId()).getId() + "." + subFlowInfo.getEvent()),
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
    }    
}



