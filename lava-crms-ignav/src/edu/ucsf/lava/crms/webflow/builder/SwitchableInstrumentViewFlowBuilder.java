package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseEntityComponentHandler.CONFIRM_LOGIC;
import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;

import java.util.ArrayList;

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
import edu.ucsf.lava.crms.webflow.builder.InstrumentViewFlowBuilder;

public class SwitchableInstrumentViewFlowBuilder extends InstrumentViewFlowBuilder {

	public SwitchableInstrumentViewFlowBuilder(LavaFlowRegistrar registry,
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
        paramMapper.addInputMapping(mapping().source("requestParameters.param4").target("param4").value());
        
    	// add the outputMappings for the enter and collect subflow states needed for instrument__switch
        paramMapper.addOutputMapping(mapping().source("id").target("flowScope.id").value());
        paramMapper.addOutputMapping(mapping().source("switchEvent").target("flowScope.switchEvent").value());
         
        // SWITCHABLE: send GROUP_MAPPING to the following subflows 
		//   this is so that the navigation bar (actions.jsp) of the subflows can list the same custom
		//   instrumentGroup instead of seeing nothing and having to recreate it */
        paramMapper.addInputMapping(mapping().source("flowScope."+GROUP_MAPPING).target(GROUP_MAPPING).value());
        // SWITCHABLE: receive GROUP_MAPPING from the following subflows
        //   same precautionary reasons as stated in buildOutputMapper()
        paramMapper.addOutputMapping(mapping().source(GROUP_MAPPING).target("flowScope."+GROUP_MAPPING).value());
        
        // LOGICCHECKS
        paramMapper.addInputMapping(mapping().source("flowScope."+CONFIRM_LOGIC).target(CONFIRM_LOGIC).value());
        paramMapper.addOutputMapping(mapping().source(CONFIRM_LOGIC).target("flowScope."+CONFIRM_LOGIC).value());
        
        // add the enterReview subflow
    	addSubflowState("enterReview", 
    			flow(actionId+".enterReview"), 
    			paramMapper, 
    			new Transition[] {
        				transition(on("finish"), to("subFlowReturnState")),
        				transition(on("finishCancel"), to("subFlowCancelReturnState"))});
    	
        // add the upload subflow
    	addSubflowState("upload", 
    			flow(actionId+".upload"), paramMapper,
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowCancelReturnState"))});
    	
    	// Finish state used for a canceled version change, just do a normal subflow return.
    	addSubflowState("changeVersion", 
    			flow(actionId+".changeVersion"), 
    			paramMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowCancelReturnState")),
    				transition(on("versionChanged"), to("finish"))});
    	
    	addSubflowState("status", 
    			flow(actionId+".status"), 
    			paramMapper,
    			new Transition[] {
    				// do not need transition on "finishCancel" here since the InstrumentStatusFlow does not
    				// have a Cancel, just Close (which returns "finish" to this parent flow subFlowState)
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
    				transition(on("finishCancel"), to("subFlowCancelReturnState")),
    				transition(on("finishSwitch"), to("finishSwitch"))});
    	
        // add the enter subflow
    	addSubflowState("enter", 
    			flow(actionId+".enter"), 
    			paramMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowCancelReturnState")),
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
    	
    	buildDefaultActionEndState();
    }
	
	public void buildOutputMapper() throws FlowBuilderException {
		super.buildOutputMapper();
	   	AttributeMapper outputMapper = getFlow().getOutputMapper();
		
	   	// SWITCHABLE: send GROUP_MAPPING back to parent flow
		//   this is just for precaution in case this flow or subflows would have altered it
	   	Mapping igMapping = mapping().source("flowScope."+GROUP_MAPPING).target(GROUP_MAPPING).value();
		getFlow().setOutputMapper(((DefaultAttributeMapper)outputMapper).addMapping(igMapping));
	}

	
}
