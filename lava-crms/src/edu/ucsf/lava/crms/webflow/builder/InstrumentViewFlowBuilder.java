package edu.ucsf.lava.crms.webflow.builder;

import java.util.ArrayList;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.engine.support.ConfigurableFlowAttributeMapper;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowBuilder;

/**
 * Java-based flow builder that builds the view entity flow, parameterized
 * for a specific entity type.
 * <p>
 * This encapsulates the page flow of viewing an entity's details, and is
 * typically involved in a larger flow conversation which may include a 
 * list flow, and an edit entity subflow.
 */
class InstrumentViewFlowBuilder extends BaseFlowBuilder {
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
    	
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),
    			null, null, null);

    	// create attribute-mapper input-mapper to pass "id" from flow scope to edit subflow
        ConfigurableFlowAttributeMapper idMapper = new ConfigurableFlowAttributeMapper();
        // note: do not need an output mapping for enter to collect and vice versa like the
        // InstrumentListViewFlow needs, because the id of the entity is in flow scope since
        // this is the view entity flow
        idMapper.addInputMapping(mapping().source("flowScope.id").target("id").value());

        // add the enter subflow
    	addSubflowState("enter", 
    			flow(actionId+".enter"), 
    			idMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				// supports going from collect subflow directly to enter subflow
    				transition(on("finishSwitchToCollect"), to("enterSubFlowReturnState"))});

        // add the enterReview subflow
    	addSubflowState("enterReview", 
    			flow(actionId+".enterReview"), 
    			idMapper, 
    			new Transition[] {transition(on("${lastEvent.id.startsWith('finish')}"), to("subFlowReturnState"))});
    	
        // add the collect subflow
    	addSubflowState("collect", 
    			flow(actionId+".collect"), idMapper,
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				// supports going from enter subflow directly to collect subflow
    				transition(on("finishSwitchToEnter"), to("collectSubFlowReturnState"))});
    	
        // add the collect subflow
    	addSubflowState("upload", 
    			flow(actionId+".upload"), idMapper,
    			new Transition[] {transition(on("${lastEvent.id.startsWith('finish')}"), to("subFlowReturnState"))});
    	
    	// Finish state used for a canceled version change, just do a normal subflow return.
    	addSubflowState("changeVersion", 
    			flow(actionId+".changeVersion"), 
    			idMapper, 
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState")),
    				transition(on("finishCancel"), to("subFlowReturnState")),
    				transition(on("versionChanged"), to("finish"))});
    	
       	addActionState("enterSubFlowReturnState", 
   				invoke("refreshFormObject", formAction), 
					transition(on("success"), to("collect")));
    	
    	addActionState("collectSubFlowReturnState", 
   				invoke("refreshFormObject", formAction), 
					transition(on("success"), to("enter")));

    	addSubflowState("status", 
    			flow(actionId+".status"), idMapper,
    			new Transition[] {
    				transition(on("finish"), to("subFlowReturnState"))});
    	    	
    	addViewState("print", 
    			null, formAction.getCustomReportSelector(), // report view name = view name + "Report", used as key into reportView.properties
    			new Action[]{invoke("prepareToRender",formAction)},
    			null, // no transitions since PDF does not accept input. user uses Back button to get back to previous state
    			null,null,null); 
    	
    	
    }
}



