package edu.ucsf.lava.core.webflow.builder;

import java.util.ArrayList;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * Java-based flow builder that builds the view entity flow, parameterized
 * for a specific entity type.
 * <p>
 * This encapsulates the page flow of viewing an entity's details, and is
 * typically involved in a larger flow conversation which may include a 
 * list flow, and an edit entity subflow.
 */
public class EntityViewFlowBuilder extends BaseFlowBuilder {
	
	public EntityViewFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("view");
    }
    
    public void buildInputMapper() throws FlowBuilderException {
    	super.buildInputMapper();
    	AttributeMapper inputMapper = getFlow().getInputMapper();
    	
    	// put the "id" into flowScope where it will be accessed in the FormAction (createFormObject)
    	// to retrieve the entity (it is also accessed to set entity context in setContextFromScope)
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since entity CRUD flows are typically subflows, "id"
    	// here typically comes from a parent flow input mapper
    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
    	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(idMapping));
    }


    public void buildEventStates() throws FlowBuilderException {
    	
    	ArrayList<Transition> viewTransitions = new ArrayList<Transition>();
    	
    	// edit/close/download transitions
    	viewTransitions.add(transition(on(objectName + "__download"), to("download")));
    	viewTransitions.add(transition(on(objectName + "__edit"), to("edit")));
    	viewTransitions.add(transition(on(objectName + "__print"), to("print")));
    	viewTransitions.add(transition(on(objectName + "__close"), to("finish"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
    	
    	//get subflow transitions   	
    	viewTransitions.addAll(this.buildSubFlowTransitions());
    
        // support a list secondary component for nav events only, since just a reference list
        viewTransitions.add(buildListNavigationTransitions(getFlowEvent()));
        
        // custom event support
        viewTransitions.addAll(this.buildCustomEventTransitions(this.objectName));
    	
    	addViewState(getFlowEvent(), 
    			null, formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),
    			null, null, null);

   
    	
    	// add the edit subflow
    	addSubflowState("edit", 
    			flow(actionId + ".edit"), 
    			this.requestParametersMapper, 
    			transition(on("${lastEvent.id.startsWith('finish')}"), to("subFlowReturnState")));
    	
    	//add the download subflow 
    	addSubflowState("download", 
    			flow(actionId + ".download"), 
    			this.requestParametersMapper, 
    			transition(on("finish"), to("subFlowReturnState")));
    	
    	addViewState("print", 
    			null, formAction.getCustomReportSelector(), // report view name = view name + "Report", used as key into lava-reports.xml
    			new Action[]{invoke("prepareToRender",formAction)},
    			null, // no transitions since PDF not accepting input. user uses Back button to get back to previous state (could
     			      // construct hyperlinks in PDF report to link back to flow)
    			null,null,null); 

 
    	buildDefaultActionEndState();
    }
}



