package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

/**
 * Java-based flow builder that builds the upload instrument flow, parameterized
 * for by specific instrument type.
 * <p>
 * This encapsulates the page flow of instrument file upload, and is
 * typically involved in a larger flow conversation which may include a 
 * parent list flow and/or a parent view flow
 */
public class InstrumentUploadFlowBuilder extends BaseFlowBuilder {
	public InstrumentUploadFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
		setFlowEvent("upload");
	}
    
    public void buildInputMapper() throws FlowBuilderException {
	   	super.buildInputMapper();
	   	AttributeMapper inputMapper = getFlow().getInputMapper();

	   	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
	   	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(idMapping));
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
    				transition(on("instrument__upload"), to("review"), 
    					ifReturnedSuccess(new Action[]{
    						// special result field custom bind here for binding comboRadioSelect controls and for
    			 			// binding user-selected missing data code to all blank result fields	
    						invoke("customBindResultFields", formAction),
    						// review event handler infers dc/de/dv status values
    						invoke("handleFlowEvent", formAction)})),
       	       		// support a details list for nav events only, since just a reference list
       	            buildListNavigationTransitions("upload")},
    			null, null, null);
    	

    	addViewState("review", 
    			null, formAction.getCustomViewSelector(),
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] {
				transition(on("instrument__uploadReviewSave"), to("editStatus")), 
   				transition(on("instrument__uploadReviewRevise"), to(getFlowEvent())),
   	       		// support a details list for nav events only, since just a reference list
   	            buildListNavigationTransitions("review")},
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



