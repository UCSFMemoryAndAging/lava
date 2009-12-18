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
 * Java-based flow builder that builds the view entity flow, parameterized
 * for a specific entity type.
 * <p>
 * This encapsulates the page flow of viewing an entity's details, and is
 * typically involved in a larger flow conversation which may include a 
 * list flow, and an edit entity subflow.
 */
public class InstrumentViewFlowBuilder extends BaseFlowBuilder {
	public InstrumentViewFlowBuilder(LavaFlowRegistrar registry,String actionId) {
		super(registry, actionId);
		setFlowEvent("view");
	}

    public void buildInputMapper() throws FlowBuilderException {
    	// put the "id" into flowScope where it will be accessed in the FormAction (createFormObject)
    	// to retrieve the entity (it is also accessed to set entity context in setContextFromScope)
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since entity CRUD flows are typically subflows, "id"
    	// here typically comes from a parent flow input mapper
    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();

    	// set the flow input mapper
    	getFlow().setInputMapper(new DefaultAttributeMapper().addMapping(idMapping));
    }

    public void buildEventStates() throws FlowBuilderException {
    	ArrayList<Transition> viewTransitions = new ArrayList<Transition>();
    	
    	// CRUD transitions
    	viewTransitions.add(transition(on("instrument__enter"), to("enter"))); 
    	viewTransitions.add(transition(on("instrument__enterReview"), to("enterReview"))); 
    	viewTransitions.add(transition(on("instrument__collect"), to("collect")));
    	viewTransitions.add(transition(on("instrument__upload"), to("upload")));
    	viewTransitions.add(transition(on("instrument__status"), to("status")));
    	viewTransitions.add(transition(on("instrument__changeVersion"), to("changeVersion")));
    	viewTransitions.add(transition(on("instrument__print"), to("print")));
    	viewTransitions.add(transition(on("instrument__close"), to("finish"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
    	
    	//get subflow transitions   	
    	viewTransitions.addAll(this.buildSubFlowTransitions());

        // support a list secondary component for nav events only, since just a reference list
        viewTransitions.add(buildListNavigationTransitions(getFlowEvent()));
        
    	// the "instrument__switch" event is used to switch to another instrument, where the id for that instrument
    	// is in the "id" request parameter, and the flow to be used on that instrument is specified as an
    	// event in the "switchEvent" request parameter. note that id can be the same as the current 
    	// instrument id, where switchEvent can then be used to switch the current instrument from the
    	// view flow to another flow, e.g. to the enter flow
    	viewTransitions.add(transition(on("instrument__switch"), to("finishSwitch"), 
				ifReturnedSuccess(new Action[]{
						// the request parameter values must be put into flow scope so that the buildOutputMapper
						// can reference them and pass them back to the parent instrument list flow (it may be
						// that buildOutputMapper could just reference requestParameters directly instead of
						// having to put these attributes in flow scope here, but did not try that)
	    				new SetAction(settableExpression("id"), ScopeType.FLOW,	expression("requestParameters.id")),
	    				new SetAction(settableExpression("switchEvent"), ScopeType.FLOW, expression("${requestParameters.switchEvent}")),
						})));
        
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),
    			null, null, null);

    	// create attribute-mapper input-mapper to pass entity "id" to the subflow states created below
    	// note that this flow could be started as a subflow of an parent instrument list flow, in which
    	// case a requestParameters.id was put into flow scope by buildInputMapper. or, this flow could
    	// be resumed as a result of a terminating instrument enter or collect subflow, and if that subflow
    	// terminates with finishSwitch, the id is put into flow scope by the outputMapping that is used 
    	// when the enter and collect subflow states are created below 
    	
        ConfigurableFlowAttributeMapper paramMapper = new ConfigurableFlowAttributeMapper();
        paramMapper.addInputMapping(mapping().source("flowScope.id").target("id").value());

        // add general purpose request parameters passed to all subflows
        paramMapper.addInputMapping(mapping().source("requestParameters.param").target("param").value());
        paramMapper.addInputMapping(mapping().source("requestParameters.param2").target("param2").value());
        paramMapper.addInputMapping(mapping().source("requestParameters.param3").target("param3").value());

        // add the enterReview subflow
    	addSubflowState("enterReview", 
    			flow(actionId+".enterReview"), 
    			paramMapper, 
    			new Transition[] {transition(on("${lastEvent.id.startsWith('finish')}"), to("subFlowReturnState"))});
    	
        // add the upload subflow
    	addSubflowState("upload", 
    			flow(actionId+".upload"), paramMapper,
    			new Transition[] {transition(on("${lastEvent.id.startsWith('finish')}"), to("subFlowReturnState"))});
    	
    	// Finish state used for a canceled version change, just do a normal subflow return.
    	addSubflowState("changeVersion", 
    			flow(actionId+".changeVersion"), 
    			paramMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				transition(on("versionChanged"), to("finish"))});
    	
    	addSubflowState("status", 
    			flow(actionId+".status"), 
    			paramMapper,
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState"))});

    	// add the outputMappings for the enter and collect subflow states needed for instrument__switch
        paramMapper.addOutputMapping(mapping().source("id").target("flowScope.id").value());
        paramMapper.addOutputMapping(mapping().source("switchEvent").target("flowScope.switchEvent").value());
    	
        // add the collect subflow
    	addSubflowState("collect", 
    			flow(actionId+".collect"), 
    			paramMapper,
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				transition(on("finishSwitch"), to("finishSwitch"))});
    	
        // add the enter subflow
    	addSubflowState("enter", 
    			flow(actionId+".enter"), 
    			paramMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				transition(on("finishSwitch"), to("finishSwitch"))});

    	
    	addViewState("print", 
    			null, formAction.getCustomReportSelector(), // report view name = view name + "Report", used as key into reportView.properties
    			new Action[]{invoke("prepareToRender",formAction)},
    			null, // no transitions since PDF does not accept input. user uses Back button to get back to previous state
    			null,null,null); 
    	
    	// special flow termination for switching from this instrument view subflow to a subflow  
    	// for another instrument (where that could be the same instrument, but just a different subflow). 
    	// this termination returns the "finishSwitch" event to the root parent flow, an instrument list flow. 
    	// the instrument list flow then transitions to the instrument subflow that was specified via
    	// request parameters when sending the "instrument__switch" event to this flow
    	addEndState("finishSwitch"); 
    	
    }
    
	public void buildOutputMapper() throws FlowBuilderException {
		// for switching from this instrument view subflow to another instrument subflow. this flow must pass
	    // the mapping attributes back to the parent flow to tell it which instrument ("id") and subflow ("switchEvent")
		// to transition to. these are put into flow scope when the "instrument__switch" event is handled,
		// or, when returning from an enter subflow via the "finishSwitch" event
		// note: the switch could be triggered from an instrument enter subflow on handling the "instrument__switch"
		// event and is propagating it up to its parent flow, this instrument view flow, or, the switch could
		// be triggered by this instrument view flow handling the "instrument__switch" event 
		Mapping idMapping = mapping().source("flowScope.id").target("id").value();
		Mapping switchEventMapping = mapping().source("flowScope.switchEvent").target("switchEvent").value();
		getFlow().setOutputMapper(new DefaultAttributeMapper().addMapping(idMapping).addMapping(switchEventMapping));
	}
    
}



