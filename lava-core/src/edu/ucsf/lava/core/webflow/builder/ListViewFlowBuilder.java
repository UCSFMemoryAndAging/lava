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
 * Java-based flow builder that builds a listing flow, parameterized for
 * the specific listing.
 * <p>
 * This encapsulates the page flow of viewing a list of entities of a 
 * given type. It is typically the root flow of a flow conversation,
 * where the subflows are the CRUD entity flows.
 */
class ListViewFlowBuilder extends BaseFlowBuilder {
	

	
	public ListViewFlowBuilder(LavaFlowRegistrar registry,
			String actionId) {
		super(registry, actionId);
		setFlowEvent("view");
	}	 
    	
    
    public void buildInputMapper() throws FlowBuilderException {
    	super.buildInputMapper();
    	AttributeMapper inputMapper = getFlow().getInputMapper();

    	// put the "id" into flowScope where is will be accessed in the FormAction to set entity 
    	// context (setContextFromScope), which in turn is used by the list handlers to retrieve 
    	// the list
    	// the "id" attribute in the flow input map could either come from a request parameter 
    	// when the flow is launched as a top level flow or from a parent flow that is providing 
    	// this as input to the subflow. since lists are typically top level flows, the
    	// "id" here typically comes from "requestParameter.id"
    	Mapping idMapping = mapping().source("id").target("flowScope.id").value();
    	getFlow().setInputMapper(((DefaultAttributeMapper)inputMapper).addMapping(idMapping));
    }

    public void buildEventStates() throws FlowBuilderException {
    	
    	String listView = getFlowEvent();
    	
    	
    	ArrayList<Transition> viewTransitions = new ArrayList<Transition>();
    	
    	//add a close transistion for lists that are subflows of other lists 
    	viewTransitions.add(transition(on(objectName + "__close"), to("finish"), ifReturnedSuccess(invoke("handleFlowEvent", formAction))));
        
    	// print to PDF
    	viewTransitions.add(transition(on(objectName + "__print"), to("print")));
    	// export to Excel file
    	viewTransitions.add(transition(on(objectName + "__export"), to("export")));
    	
    	// list filter events, navigation, etc. need bind and handleFlowEvent actions
    	// note: this is a catch-all transition, so event-specific transitions should appear before this
    	viewTransitions.add(transition(on(new StringBuffer("${lastEvent.id.startsWith('").append(objectName).append("__')}").toString()), 
        		to(listView), 
        		ifReturnedSuccess(new Action[]{
        			invoke("bind", formAction), 
        			invoke("handleFlowEvent", formAction)})));
    	
    	//get subflow transitions   	
    	viewTransitions.addAll(this.buildSubFlowTransitions());
    	
       
    	
    	addViewState(listView, 
    			null, 
    			formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),		
          		null,
    			null,
    			null);

    	
    	// for report engine generated view states, the getCustomReportSelector returns the generated
    	// report view name, i.e. the same view name generated for jsp view with "Report" appended, which
    	// is mapped to the report design file in lava-reports.xml
    	// note that the actual render format (pdf, xls, etc.) is determined by setting the "format" key
    	// in the model, and for lists this is done in addReferenceData based upon the name of the view state
    	addViewState("print", 
    			null, formAction.getCustomReportSelector(), 
    			new Action[]{invoke("prepareToRender",formAction)},
    			null, // no transitions since PDF not accepting input. user uses Back button to get back to previous state (could
    			      // construct hyperlinks in PDF report to link back to flow)
    			null,null,null); 

    	addViewState("export", 
    			null, formAction.getCustomReportSelector(), 
    			new Action[]{invoke("prepareToRender",formAction)},
    			null, // no transitions since Excel does not accept input. user uses Back button to get back to previous state
    			null,null,null); 

    	
    	buildDefaultActionEndState();
    }

  
}



