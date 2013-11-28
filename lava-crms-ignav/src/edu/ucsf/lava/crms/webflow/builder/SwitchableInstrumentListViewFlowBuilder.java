package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseEntityComponentHandler.CONFIRM_LOGIC;
import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;

import java.util.List;

import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.support.ConfigurableFlowAttributeMapper;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.ScopeType;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.FlowInfo;
import edu.ucsf.lava.crms.webflow.builder.InstrumentListViewFlowBuilder;

public class SwitchableInstrumentListViewFlowBuilder extends
		InstrumentListViewFlowBuilder {

	public SwitchableInstrumentListViewFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
	}

	@Override
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
				Transition subFlowReturnFinishCancelTransition = transition(on("finishCancel"), to("subFlowCancelReturnState"));

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
		        paramMapper.addInputMapping(mapping().source("requestParameters.param4").target("param4").value());
		        
		    	if (subFlowInfo.getTarget().equals("instrumentGroup")){
			        // return the group in an output mapper so that the selected items can be re-selected, since
			        // they are lost when subFlowReturnState does a refresh (which must be done in case the group
			        // flow added a missing instrument)
			        paramMapper.addInputMapping(mapping().source("flowScope."+GROUP_MAPPING).target(GROUP_MAPPING).value());
			        paramMapper.addOutputMapping(mapping().source(GROUP_MAPPING).target("flowScope."+GROUP_MAPPING).value());
			        
			        // LOGICCHECKS
			        paramMapper.addInputMapping(mapping().source("flowScope."+CONFIRM_LOGIC).target(CONFIRM_LOGIC).value());
			        paramMapper.addOutputMapping(mapping().source(CONFIRM_LOGIC).target("flowScope."+CONFIRM_LOGIC).value());
			        
			        // SWITCHABLE: support SwitchGroupFlow switchMode changing
			        //  This will transition instrumentGroup into a new mode, determined by the requestParameter "switchEvent" (e.g. from instrumentGroup__edit to instrumentGroup__view)
			        //  The "switchMode" event would have been received here only if the view requested a different mode than the current instrumentGroup flow.
			        Transition switchTransition = transition(on("switchMode"), to("${requestParameters.switchEvent}"));
			        transitions = new Transition[]{subFlowReturnFinishTransition, subFlowReturnFinishCancelTransition,switchTransition};
		    	}
		    	
		    	// for now, only implementing the "switchEvent" transition for the subflows which use it. to 
		    	// add an additional subflow, that flow builder will have to:
		    	// 1) handle the "instrument__switch" event
		    	// 2) store the id for the instrument to switch to in flow scope as "id"
		    	// 3) store the subflow state to switch to in flow scope as "switchEvent" (because the event 
		    	//    names match subflow state ids),
		    	// 4) do anything that might need to be done before terminating the flow, and transitioning to 
		    	//    a "finishSwitch" end state.
		    	// 5) add the "finishSwitch" end state
		    	// 6) add output mappings to map "id" and "switchEvent" back to the parent flow (not to be
		    	//    confused with below, which is adding output mappings to receive those mapping values
		    	//    and map them into this flow)
		    	else if (subFlowInfo.getEvent().equals("enter") || subFlowInfo.getEvent().equals("collect")
		    			|| subFlowInfo.getEvent().equals("view") || subFlowInfo.getEvent().equals("enterReview")
		    			|| subFlowInfo.getEvent().equals("status") || subFlowInfo.getEvent().equals("delete"))
		    	{

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

    	// add action states which allow the handler a hook into the subflow return process, which
    	// may be required to refresh the instrument in a parent flow after a subflow which modified
    	// the instrument (add/delete/edit) returns, so that changes are reflected in the instrument list

    	// note that if returning from the group subflow, the refresh handler also re-selects any 
    	// selected items as these are lost when the list is refreshed. the group flow returns the
    	// group in an output mapper (included any new items added in the group subflow) to enable this.
       	addActionState("subFlowReturnState",
    			// entry action (note: the expression("false") puts an attribute of type Boolean in flow scope)
       			new Action[]{new SetAction(settableExpression("cancelled"), ScopeType.FLOW,	expression("false"))},
       			// actions
       			new Action[]{invoke("subFlowReturnHook", formAction)},
       			new Transition[]{transition(on("success"), to(getFlowEvent()))},		
       			null,null,null);
    	
    	addActionState("subFlowCancelReturnState",
       			new Action[]{new SetAction(settableExpression("cancelled"), ScopeType.FLOW,	expression("true"))},
       			new Action[]{invoke("subFlowReturnHook", formAction)},
       			new Transition[]{transition(on("success"), to(getFlowEvent()))},		
       			null,null,null);
    	
    }    
	
}
