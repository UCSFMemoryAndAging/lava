package edu.ucsf.lava.crms.webflow.builder;

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
 * Java-based flow builder that builds the collect instrument flow,
 * parameterized for by specific instrument type.
 * <p>
 * This encapsulates the page flow of instrument data collection, and is
 * typically involved in a larger flow conversation which may include a parent
 * list flow and/or a parent view flow
 */
class InstrumentCollectFlowBuilder extends BaseFlowBuilder {
	public InstrumentCollectFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
		setFlowEvent("collect");
	}

	public void buildInputMapper() throws FlowBuilderException {
    	// put the "id" into flowScope where it will be accessed in the FormAction (createFormObject)
    	// to retrieve the entity (it is also accessed to set entity context in setContextFromScope)
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since entity CRUD flows are typically subflows, "id"
    	// here typically comes from a parent flow input mapper
		Mapping idMapping = mapping().source("id").target("flowScope.id").value();
    	
    	// the "target" attribute is used when switching from collect to enter by the instrument list flow  
    	Mapping targetMapping = mapping().source("target").target("flowScope.target").value();

    	// set the flow input mapper
    	getFlow().setInputMapper(new DefaultAttributeMapper().addMapping(idMapping).addMapping(targetMapping));
	}

	public void buildEventStates() throws FlowBuilderException {
		addViewState(getFlowEvent(),
				null,
				formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
				new Action[] {invoke("prepareToRender", formAction)},
				new Transition[] {
						transition(
								on("instrument__collectSave"),
								to("review"),
								ifReturnedSuccess(new Action[] {
								// special result field custom bind here for
								// binding comboRadioSelect controls and for
										// binding user-selected missing data
										// code to all blank result fields
										invoke("customBindResultFields",
												formAction),
										// review event handler infers dc/de/dv
										// status values
										invoke("handleFlowEvent", formAction) })),
						transition(
								on("${lastEvent.id.equals('instrument__hideCodes') || lastEvent.id.equals('instrument__showCodes')}"),
		    					// skip display of field error checking on hide/show codes, since just returns user to same page
								to("collect"), ifReturnedSuccess(invoke(
										"customBindResultFieldsIgnoreErrors", formAction))),
						transition(
								on("instrument__collectEnter"),
								to("finishSwitchToEnter"),
								ifReturnedSuccess(new Action[] {
										// skip display of field error checking
										// if user switching to enter mode, as
										// they can
										// deal with any field errors once they
										// are in the mode they want to be in.
										// field errors
										// are still generated, but the special
										// customBindResultFieldsIgnoreErrors
										// returns the
										// success Event so that the transition
										// to enter goes thru, and any field
										// errors will
										// then be displayed on the enter view.
										// save any edits they have made before
										// switching
										invoke(
												"customBindResultFieldsIgnoreErrors",
												formAction),
										new SetAction(
												settableExpression("eventOverride"),
												ScopeType.FLASH,
												expression("${'instrument__save'}")),
										invoke("handleFlowEvent", formAction) })),
										
					    	// the "reRender" event is used for re-rendering via uitags, without the need to save
					    	transition(on("instrument__reRender"), to("collect"),
					            	ifReturnedSuccess(new Action[]{
					                		invoke("customBindResultFieldsIgnoreErrors", formAction), 
					                		invoke("handleFlowEvent", formAction)})),
					                		
	                    	// like "reRender" in that there is no validation, but unlike "reRender" it does a save
	                    	transition(on("instrument__save"), to("collect"), 
	                		  		ifReturnedSuccess(new Action[]{
	                            		invoke("customBindResultFieldsIgnoreErrors", formAction), 
	                            		invoke("handleFlowEvent", formAction)}))},
					                    	
				null, null, null);

		addViewState(
				"review",
				null,
				formAction.getCustomViewSelector(),
				new Action[] { invoke("prepareToRender", formAction) },
				new Transition[] {
						transition(
								on("instrument__collectReviewSave"),
								to("editStatus"),
								ifReturnedSuccess(new Action[] {
										invoke("customBind", formAction),
										// saveReview event handler updates
										// dvStatus and saves
										invoke("handleFlowEvent", formAction) })),
						transition(on("instrument__collectReviewRevise"), to("collect")),
						transition(on("instrument__collectReviewDefer"), to("editStatus"),
						// sets dvStatus to "Defer"
								// alternatively, could do the following, but
								// use handle method in case need to do other
								// things:
								// <set
								// attribute="${conversationScope.command.components['instrument']['dvStatus']"
								// value="${'Defer'}" scope="conversation"/>
								ifReturnedSuccess(invoke("handleFlowEvent",
										formAction))) }, null, null, null);

		addViewState("editStatus", null, formAction.getCustomViewSelector(),
				new Action[] { invoke("prepareToRender", formAction) },
				new Transition[] { transition(on("instrument__statusSave"),
						to("finish"), ifReturnedSuccess(new Action[] {
								invoke("customBind", formAction),
								invoke("handleFlowEvent", formAction) })) },
				null, null, null);
		
    	
	
		// special flow termination for switching from collect to enter flow.
		// collect flow can only be
		// entered from the instrument list flow or the view instrument flow. in
		// either case, know that
		// this is a subflow and will return to a parent flow.
		// by signalling "finishSwitchToEnter", the parent flow knows that it
		// should transition into
		// the enter flow
		addEndState("finishSwitchToEnter");
	}

	public void buildGlobalTransitions() throws FlowBuilderException {
		// do not need to call super because this flow is modal so not
		// patient/project context events

		// cancel behaves differently here than in standard list-view-edit
		// entity flow where the
		// entity is not saved and refreshed to its persistent state. in this
		// flow, entity is saved
		// between each view-state transition, so cancel just cancels any
		// changes on the current
		// view-state, and transitions out of the flow; changes made in prior
		// view-states within this
		// flow have been saved.
		// TODO: allow user to cancel all changes made in flow and return the
		// instrument to the state
		// it was in upon entering flow, but still save instrument on view-state
		// transtions (requires
		// a deep virgin instrument copy made upon entering flow)
		this.getFlow().getGlobalTransitionSet()
				.add(
						transition(on("instrument__cancel"), to("finishCancel"),
								ifReturnedSuccess(invoke("handleFlowEvent",
										formAction))));
   		this.getFlow().getGlobalTransitionSet().add(transition(on("unauthorized"), to("${flowScope.mostRecentViewState}")));
	}
	
	// for switching from collect to enter, which goes back thru the parent flow 
	// (InstrumentListViewFlow or InstrumentViewFlow)
	public void buildOutputMapper() throws FlowBuilderException {
		Mapping idMapping = mapping().source("flowScope.id").target("id").value();
		Mapping targetMapping = mapping().source("flowScope.target").target("target").value();		
		getFlow().setOutputMapper(new DefaultAttributeMapper().addMapping(idMapping).addMapping(targetMapping));
	}
}
