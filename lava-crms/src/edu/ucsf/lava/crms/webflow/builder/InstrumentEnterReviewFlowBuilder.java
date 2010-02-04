package edu.ucsf.lava.crms.webflow.builder;

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
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

/**
 * Java-based flow builder that builds the enterReview instrument flow, parameterized
 * for by specific instrument type.
 * <p>
 * This encapsulates the page flow of instrument data entry with a review, and is
 * typically involved in a larger flow conversation which may include a 
 * parent list flow and/or a parent view flow
 */
public class InstrumentEnterReviewFlowBuilder extends BaseFlowBuilder {
	public InstrumentEnterReviewFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
		setFlowEvent("enterReview");
	}

    public void buildInputMapper() throws FlowBuilderException {
	   	super.buildInputMapper();
	   	AttributeMapper inputMapper = getFlow().getInputMapper();

    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
    	
    	// the "target" attribute is used when switching from collect to enter by the instrument list flow  
    	Mapping targetMapping = mapping().source("target").target("flowScope.target").value();

    	// set the flow input mapper
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(idMapping).addMapping(targetMapping));
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	ArrayList<Transition> enterStateTransitions = new ArrayList<Transition>();
    	
    	enterStateTransitions.add(transition(on("instrument__enterSave"), to("review"), 
				ifReturnedSuccess(new Action[]{
						// special result field custom bind here allows binding user-selected missing data code
						// to all blank result fields 
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
    	enterStateTransitions.add(transition(on("instrument__save"), to("enterReview"), 
		  		ifReturnedSuccess(new Action[]{
            		invoke("customBindResultFieldsIgnoreErrors", formAction), 
            		invoke("handleFlowEvent", formAction)})));

    	

    	// custom events
    	enterStateTransitions.addAll(this.buildCustomEventTransitions("instrument"));
    	
    	//get subflow transitions   	
    	enterStateTransitions.addAll(this.buildSubFlowTransitions());
    	
        // support a list secondary component for nav events only, since just a reference list
        enterStateTransitions.add(buildListNavigationTransitions(getFlowEvent()));
        
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			enterStateTransitions.toArray(new Transition[0]),
    			null, null, null);
    	
    	addViewState("review", 
    			null, formAction.getCustomViewSelector(),
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] {
				transition(on("instrument__enterReviewSave"), to("mandatoryVerifyDecision"), 
    					ifReturnedSuccess(new Action[]{
    						invoke("customBind", formAction),
    						// saveReview event handler updates dvStatus and saves
    						invoke("handleFlowEvent", formAction)})),
   				transition(on("instrument__enterReviewRevise"), to(getFlowEvent())),
   		    	// user chooses to verify
   		    	transition(on("instrument__enterReviewVerify"), to("doubleEnter"))},
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
      					ifReturnedSuccess(invoke("customBindResultFieldsIgnoreErrors", formAction)))},
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
						invoke("handleFlowEvent", formAction)}))},
       			null, null, null);
   						
    	addViewState("editStatus", 
    			null, formAction.getCustomViewSelector(),
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] {
    			transition(on("instrument__statusSave"), to("finish"), 
					ifReturnedSuccess(new Action[]{
						invoke("customBind", formAction), 
						invoke("handleFlowEvent", formAction)}))},
   	       		null, null, null);
    	
    	
    	// special flow termination for switching from enter to collect flow. enter flow can only be
    	// entered from the instrument list flow or the view instrument flow. in either case, know that
    	// this is a subflow and will return to a parent flow. 
    	// by signalling "finishSwitchToCollect", the parent flow knows that it should transition into
    	// the collect flow 
    	addEndState("finishSwitchToCollect"); 
    }
    
    public void buildGlobalTransitions() throws FlowBuilderException {
    	// do not need to call super because this flow is modal so not patient/project context events 
    	
		// cancel behaves differently here than in standard list-view-edit entity flow where the 
		// entity is not saved and refreshed to its persistent state. in this flow, entity is saved
		// between each view-state transition, so cancel just cancels any changes on the current
		// view-state, and transitions out of the flow; changes made in prior view-states within this
		// flow have been saved. 
		// TODO: allow user to cancel all changes made in flow and return the instrument to the state 
		// it was in upon entering flow, but still save instrument on view-state transtions (requires 
		// a deep virgin instrument copy made upon entering flow)
   		this.getFlow().getGlobalTransitionSet().add(
   				transition(on("instrument__cancel"), to("finishCancel"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
   		this.getFlow().getGlobalTransitionSet().add(transition(on("unauthorized"), to("${flowScope.mostRecentViewState}")));
    }
}



