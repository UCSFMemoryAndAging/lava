package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

/**
 * Java-based flow builder that builds the enter instrument flow, parameterized
 * for by specific instrument type.
 * <p>
 * This encapsulates the page flow of instrument data entry, and is
 * typically involved in a larger flow conversation which may include a 
 * parent list flow and/or a parent view flow
 */
public class InstrumentEditStatusFlowBuilder extends BaseFlowBuilder {
	public InstrumentEditStatusFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
		setFlowEvent("editStatus");
	}

    public void buildInputMapper() throws FlowBuilderException {
    	// put the "id" into flowScope where it will be accessed in the FormAction (createFormObject)
    	// to retrieve the entity (it is also accessed to set entity context in setContextFromScope)
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since entity CRUD flows are typically subflows, "id"
    	// here typically comes from a parent flow input mapper
    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
    	
    	// set the flow input mapper with the above mappings
    	getFlow().setInputMapper(new DefaultAttributeMapper().addMapping(idMapping));
    }
    
    public void buildEventStates() throws FlowBuilderException {
    	
     			addViewState(getFlowEvent(), 
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
    
	// for switching from enter to collet, which goes back thru the parent flow 
	// (InstrumentListViewFlow or InstrumentViewFlow)
	public void buildOutputMapper() throws FlowBuilderException {
		Mapping idMapping = mapping().source("flowScope.id").target("id").value();
		Mapping targetMapping = mapping().source("flowScope.target").target("target").value();
		getFlow().setOutputMapper(new DefaultAttributeMapper().addMapping(idMapping).addMapping(targetMapping));
	}
}



