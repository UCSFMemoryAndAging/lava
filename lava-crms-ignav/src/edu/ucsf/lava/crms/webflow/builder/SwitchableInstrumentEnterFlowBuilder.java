package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;

import java.util.ArrayList;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.action.SetAction;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;
import org.springframework.webflow.execution.ScopeType;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.crms.webflow.builder.InstrumentEnterFlowBuilder;

public class SwitchableInstrumentEnterFlowBuilder extends InstrumentEnterFlowBuilder {

	public SwitchableInstrumentEnterFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
	}

	@Override
	public void buildInputMapper() throws FlowBuilderException {
	   	super.buildInputMapper();
	   	AttributeMapper inputMapper = getFlow().getInputMapper();

	   	/* SWITCHABLE: receive GROUP_MAPPING from parent flow 
		 *   this is so that the navigation bar (actions.jsp) can list the same instrumentGroup
		 *   as of the parent flow instead of seeing nothing and having to recreate it */
	   	Mapping igMapping = mapping().source(GROUP_MAPPING).target("flowScope."+GROUP_MAPPING).value();
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(igMapping));
	}
	
	@Override
	public void buildEventStates() throws FlowBuilderException {
	    	ArrayList<Transition> enterStateTransitions = new ArrayList<Transition>();
	    	
	    	// this allows for a file to be uploaded to populate data and remain in the enter view 
	    	enterStateTransitions.add(transition(on("instrument__upload"), to("enter"), 
					ifReturnedSuccess(new Action[]{
							invoke("customBindResultFieldsIgnoreErrors", formAction), 
							invoke("handleFlowEvent", formAction)})));
	    	
	    	// SWITCHABLE: we wish to _finish_ after saving (i.e. go back to instrument list)
			//   and _not_ go back to status screen (nor possible double entry)
	    	//enterStateTransitions.add(transition(on("instrument__enterSave"), to("mandatoryVerifyDecision"), 
	    	enterStateTransitions.add(transition(on("instrument__enterSave"), to("finish"),
					ifReturnedSuccess(new Action[]{
							// special result field custom bind here allows binding user-selected missing data code
							// to all blank result fields 
							invoke("customBindResultFields", formAction), 
							invoke("handleFlowEvent", formAction)})));
	    	
	    	// user chooses to verify
	    	enterStateTransitions.add(transition(on("instrument__enterVerify"), to("doubleEnter"), 
					ifReturnedSuccess(new Action[]{
							invoke("customBindResultFields", formAction), 
							invoke("handleFlowEvent", formAction)})));
	    	
	    	enterStateTransitions.add(transition(on("${lastEvent.id.equals('instrument__hideCodes') || lastEvent.id.equals('instrument__showCodes')}"), 
					to("enter"), 
					// skip display of field error checking on hide/show codes, since just returns user to same page
					ifReturnedSuccess(invoke("customBindResultFieldsIgnoreErrors", formAction))));
	    	
	    	// the "reRender" event is used for re-rendering via uitags, without the need to save
	    	enterStateTransitions.add(transition(on("instrument__reRender"), to("enter"),
	            	ifReturnedSuccess(new Action[]{
	                		invoke("customBindResultFieldsIgnoreErrors", formAction), 
	                		invoke("handleFlowEvent", formAction)})));
	    	
	    	// like "reRender" in that there is no validation, but unlike "reRender" it does a save
	    	enterStateTransitions.add(transition(on("instrument__save"), to("enter"), 
			  		ifReturnedSuccess(new Action[]{
	            		invoke("customBindResultFieldsIgnoreErrors", formAction), 
	            		invoke("handleFlowEvent", formAction)})));
	    	
	    	// the "instrument__switch" event is used to switch to another instrument, where the id for that instrument
	    	// is in the "id" request parameter, and the flow to be used on that instrument is specified as an
	    	// event in the "switchEvent" request parameter. note that id can be the same as the current 
	    	// instrument id, where switchEvent can then be used to switch the current instrument from the
	    	// enter flow to another flow, e.g. to the collect flow
	    	enterStateTransitions.add(transition(on("instrument__switch"), to("finishSwitch"), 
					ifReturnedSuccess(new Action[]{
							// special result field custom bind here allows binding user-selected missing data code
							// to all blank result fields 
							invoke("customBindResultFields", formAction),
							// save the instrument so that edits are not lost. this requires setting the event to save
							// before handleFlowEvent is called
							// note: for expression, string is treated as an expression, so to return a literal string, must enclose in ',
							// and actually, the ${} are not necessary since expression treats its parameter as an expression by default,
							// but just showing this usage to illustrate the point
							new SetAction(settableExpression("eventOverride"), ScopeType.FLASH, expression("${'instrument__enterSave'}")),
							invoke("handleFlowEvent", formAction),
							// the request parameter values must be put into flow scope so that the buildOutputMapper
							// can reference them and pass them back to the parent instrument list flow (it may be
							// that buildOutputMapper could just reference requestParameters directly instead of
							// having to put these attributes in flow scope here, but did not try that)
							// note: the next two calls just demonstrate that with expression, if there is only a single expression within
							// the string, the ${} are optional, as the string is evaluated as an expression by default
		    				new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
		    				new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
							})));
	    	

	    	// custom events
	    	enterStateTransitions.addAll(this.buildCustomEventTransitions("instrument"));
	    	
	    	//get subflow transitions   	
	    	enterStateTransitions.addAll(this.buildSubFlowTransitionsWithBind());
	    	
	        // support a list secondary component for nav events only, since just a reference list
	        enterStateTransitions.add(buildListNavigationTransitions(getFlowEvent()));
	        
	    	addViewState(getFlowEvent(), 
	    			null, formAction.getCustomViewSelector(),
	    			// setupForm called in flowSetupState so no need to call here
	    			new Action[]{invoke("prepareToRender",formAction)},
	    			enterStateTransitions.toArray(new Transition[0]),
	    			null, null, null);
	    	
	    	// decide whether user must verify via double enter based on the flag set in the handler per
	    	// the project/instrument verification configuration
			addDecisionState("mandatoryVerifyDecision", null,
					new Transition[] {
						transition(on("${flowScope.mandatoryDoubleEnter}"), to("doubleEnter")),
						transition(on("${!flowScope.mandatoryDoubleEnter}"), to("editStatus"))},
					null, null, null);	

	    	
	    	addViewState("doubleEnter", 
	    			null, formAction.getCustomViewSelector(),
	    			new Action[]{invoke("prepareToRender",formAction)},
	    			new Transition[] {
						transition(on("instrument__doubleEnterSave"), to("doubleEnterCompare"), 
	    					ifReturnedSuccess(new Action[]{
	    						// special result field custom bind here allows binding user-selected missing data code
	    						// to all blank result fields 
	    						invoke("customBindResultFields", formAction), 
	    						invoke("handleFlowEvent", formAction)})),
	    				transition(on("instrument__doubleEnterDefer"), to("editStatus"), 
	   						// sets dvStatus to "Defer"
	       					ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
	       				transition(on("${lastEvent.id.equals('instrument__hideCodesDoubleEnter') || lastEvent.id.equals('instrument__showCodesDoubleEnter')}"), 
	      					to("doubleEnter"), 
	    					// skip display of field error checking on hide/show codes, since just returns user to same page
	      					ifReturnedSuccess(invoke("customBindResultFieldsIgnoreErrors", formAction))),
	   					transition(on("instrument__switch"), to("finishSwitch"), 
							ifReturnedSuccess(new Action[]{
								new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
			    				new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
							}))				
	    				},
	      			null, null, null);

	    	addViewState("doubleEnterCompare", 
	    			null, formAction.getCustomViewSelector(),
	    			new Action[]{invoke("prepareToRender",formAction)},
	    			new Transition[] {
	    			transition(on("instrument__doubleEnterCompare"), to("editStatus"), 
						ifReturnedSuccess(new Action[]{
							// special result field custom bind here allows binding user-selected missing data code
							// to all blank result fields 
							invoke("customBindResultFields", formAction), 
							// if compare fails, handleFlowEvent returns error Event so transition is aborted, user is
							// returned to this view-state so they can fix the doubleEnter comparison errors
							invoke("handleFlowEvent", formAction)})),
					transition(on("${lastEvent.id.equals('instrument__hideCodes') || lastEvent.id.equals('instrument__showCodes')}"), 
	   					to("doubleEnterCompare"), 
						// skip display of field error checking on hide/show codes, since just returns user to same page
	   					ifReturnedSuccess(new Action[] {
	   						invoke("customBindResultFieldsIgnoreErrors", formAction),
	   						// force handling of compare event so that any compare errors are still displayed
	     					new SetAction(settableExpression("eventOverride"), ScopeType.FLASH, expression("'instrument__compare'")),
							invoke("handleFlowEvent", formAction)})),
					transition(on("instrument__switch"), to("finishSwitch"), 
						ifReturnedSuccess(new Action[]{
							new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
		    				new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
						}))				
	    			},
	       			null, null, null);
	   						
	    	addViewState("editStatus", 
	    			null, formAction.getCustomViewSelector(),
	    			new Action[]{invoke("prepareToRender",formAction)},
	    			new Transition[] {
	    			transition(on("instrument__revise"), to(this.getFlowEvent())),  
	    			transition(on("instrument__statusSave"), to("finish"), 
						ifReturnedSuccess(new Action[]{
							invoke("customBind", formAction), 
							invoke("handleFlowEvent", formAction)})),
					transition(on("instrument__switch"), to("finishSwitch"), 
						ifReturnedSuccess(new Action[]{
							invoke("customBind", formAction),
		 					new SetAction(settableExpression("eventOverride"), ScopeType.FLASH, expression("${'instrument__statusSave'}")),
							invoke("handleFlowEvent", formAction),
							new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
		    				new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
						}))				
	    			},
	   	       		null, null, null);
	    	
	    	// special flow termination for switching from this instrument enter subflow to a subflow  
	    	// for another instrument (where that could be the same instrument, but just a different subflow). 
	    	// this termination returns the "finishSwitch" event to the root parent flow, an instrument list flow 
	    	// (possibly via an instrument view flow,/ if this enter flow was entered via instrument view). 
	    	// the instrument list flow then transitions to the instrument subflow that was specified via
	    	// request parameters when sending the "instrument__switch" event to this flow
	    	addEndState("finishSwitch"); 
	    }

	public void buildOutputMapper() throws FlowBuilderException {
		super.buildOutputMapper();
	   	AttributeMapper outputMapper = getFlow().getOutputMapper();
		
	   	// SWITCHABLE: send GROUP_MAPPING back to parent flow
		//   this is just for precaution in case this flow or subflows would have altered it
	   	Mapping igMapping = mapping().source("flowScope."+GROUP_MAPPING).target(GROUP_MAPPING).value();
		getFlow().setOutputMapper(((DefaultAttributeMapper)outputMapper).addMapping(igMapping));
	}
	
	@Override
	public void buildGlobalTransitions() throws FlowBuilderException {
		/* SWITCHABLE: add states that are done in BaseFlowBuilder (that were skipped by InstrumentEnterFlowBuilder)
		 *   since we are NOT using modal decorators for instruments anymore, which InstrumentEnterFlowBuilder assumed
		 */
		this.getFlow().getGlobalTransitionSet().addAll(new Transition[] {
    			// when subflow is unauthorized, return to the view state which was active when the
    			// flow was suspended and the subflow started 
				transition(on("instrument__cancel"), to("finishCancel"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
    			transition(on("unauthorized"), to("${flowScope.mostRecentViewState}")),
    			transition(on("defaultAction"), to("defaultAction")),
    			transition(on(new String("${lastEvent.id.endsWith('__contextChange') || lastEvent.id.endsWith('__contextClear')}")), to("contextChangeDecision"),
    		    		ifReturnedSuccess(new Action[]{
    		            		invoke("bind", formAction), 
    		            		invoke("handleFlowEvent", formAction)}))
    			});

	}

	
}
