package edu.ucsf.lava.crms.webflow.builder;

import org.springframework.binding.mapping.AttributeMapper;
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

/**
 * Java-based flow builder that builds the enter instrument flow, parameterized
 * for by specific instrument type.
 * <p>
 * This encapsulates the page flow of instrument data entry, and is
 * typically involved in a larger flow conversation which may include a 
 * parent list flow and/or a parent view flow
 */
public class InstrumentStatusFlowBuilder extends BaseFlowBuilder {
	public InstrumentStatusFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
		setFlowEvent("status");
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
    				transition(on("instrument__changeVersion"), to("changeVersion")), 
    				transition(on("instrument__editStatus"), to("editStatus")), 
    				transition(on("instrument__close"), to("finish"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
    		    	// the "instrument__switch" event is used to switch to another instrument, where the id for that instrument
    		    	// is in the "id" request parameter, and the flow to be used on that instrument is specified as an
    		    	// event in the "switchEvent" request parameter. note that id can be the same as the current 
    		    	// instrument id, where switchEvent can then be used to switch the current instrument from the
    		    	// view flow to another flow, e.g. to the enter flow
    				transition(on("instrument__switch"), to("finishSwitch"), 
    					ifReturnedSuccess(new Action[]{
    					// the request parameter values must be put into flow scope so that the buildOutputMapper
    					// can reference them and pass them back to the parent instrument list flow (it may be
    					// that buildOutputMapper could just reference requestParameters directly instead of
    					// having to put these attributes in flow scope here, but did not try that)
    			    	new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
	    				new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
					}))
    			},
       	       	null, null, null);

//    	 create attribute-mapper input-mapper to pass "id" from flow scope to edit subflow
        ConfigurableFlowAttributeMapper idMapper = new ConfigurableFlowAttributeMapper();
        // note: do not need an output mapping for enter to collect and vice versa like the
        // InstrumentListViewFlow needs, because the id of the entity is in flow scope since
        // this is the view entity flow
        idMapper.addInputMapping(mapping().source("flowScope.id").target("id").value());

    	
        
    	addSubflowState("editStatus", 
    			flow(actionId+".editStatus"), 
    			this.requestParametersMapper, 
    			new Transition[] {
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				transition(on("finish"), to("subFlowReturnState"))});
   	
    	// Finish state used for a canceled version change, just do a normal subflow return.
    	addSubflowState("changeVersion", 
    			flow(actionId+".changeVersion"), 
    			this.requestParametersMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				transition(on("versionChanged"), to("finish"))});
    	
   
    	
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
   				transition(on("instrument__cancel"), to("finish"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
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



