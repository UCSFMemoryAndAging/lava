package edu.ucsf.lava.crms.webflow.builder;

import static edu.ucsf.lava.core.controller.BaseGroupComponentHandler.GROUP_MAPPING;

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
import edu.ucsf.lava.crms.webflow.builder.InstrumentStatusFlowBuilder;

public class SwitchableInstrumentStatusFlowBuilder extends InstrumentStatusFlowBuilder {

	public SwitchableInstrumentStatusFlowBuilder(LavaFlowRegistrar registry,
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
        
    	addSubflowState("editStatus", 
    			flow(actionId+".editStatus"), 
    			paramMapper, 
    			new Transition[] {
    				transition(on("finishCancel"), to("subFlowCancelReturnState")),
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishSwitch"), to("finishSwitch"))});
   	
    	// Finish state used for a canceled version change, just do a normal subflow return.
    	addSubflowState("changeVersion", 
    			flow(actionId+".changeVersion"), 
    			paramMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowCancelReturnState")),
    				transition(on("versionChanged"), to("finish"))});
    	
   
    	
    	addEndState("finishSwitch");
    	
    	buildDefaultActionEndState();
   }
	
	@Override
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
		/* SWITCHABLE: add states that are done in BaseFlowBuilder (that were skipped by InstrumentStatusFlowBuilder)
		 *   since we are NOT using modal decorators for instruments anymore, which InstrumentStatusFlowBuilder assumed
		 */
		this.getFlow().getGlobalTransitionSet().addAll(new Transition[] {
    			// have cancel
				
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
