package edu.ucsf.lava.core.webflow.builder;

import java.util.ArrayList;

import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * This defines a generalized report flow, where the initial state displays
 * the report parameter inputs as well as the desired report format (e.g. PDF, Excel)
 * and the final state displays the report in the desired format.
 */
class ReportFlowBuilder extends BaseFlowBuilder {
	
	public ReportFlowBuilder(LavaFlowRegistrar registry, String actionId) {
		super(registry, actionId);
		
		// since all reports share the same setup, they all use "reportSetup" as their 
		// component name instead of the standard procedure of using the target of a report 
		// action, e.g. projectPatientStatus, so set things up the same here in the flow. 
		// this way, the events across all reports will be "reportSetup__X"
		this.objectName = "reportSetup";
		
		// this is essentially the name of the initial view state, used by many of
		// the core transitions in the BaseFlowBuilder
		setFlowEvent("reportSetup");
	}	 
    	
    
    public void buildInputMapper() throws FlowBuilderException {
    }

    public void buildEventStates() throws FlowBuilderException {
    	String setupViewState = getFlowEvent();
    	ArrayList<Transition> viewTransitions = new ArrayList<Transition>();
    	
    	//add a close transistion for lists that are subflows of other lists 
    	viewTransitions.add(transition(on(objectName + "__close"), to("finish")));

    	// generate report (this transition must be before the next because the next
    	// appears to inadvertently modifies objectName)
    	viewTransitions.add(transition(on(objectName + "__generate"), to("reportGen"),
           		ifReturnedSuccess(new Action[]{
               			invoke("customBind", formAction),
               			invoke("handleFlowEvent", formAction)})));
    	
       	// date range, quick date selection events need bind and handleFlowEvent actions
    	// note: this is a catch-all transition, so event-specific transitions should appear before this
    	viewTransitions.add(transition(on(new StringBuffer("${lastEvent.id.startsWith('").append(objectName).append("__')}").toString()), 
       		to(setupViewState), 
       		ifReturnedSuccess(new Action[]{
       			invoke("bind", formAction), 
       			invoke("handleFlowEvent", formAction)})));
    	
       
    	addViewState(setupViewState, 
    			null, 
    			formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			// setupForm calls getBackingObjects which creates the LavaDaoFilter object which handles the 
    			// specified parameters for a given report, and puts it into the model within a ReportSetup command object
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),		
          		null,
    			null,
    			null);

    	
    	// for report engine generated view states, the getCustomReportSelector returns the generated
    	// report view name, i.e. the report name generated for jsp view with "Report" appended, which
    	// is mapped to the report design file in lava-reports.xml
    	// the format is specified by the user in the setupViewState state and set in the model per Jasper
    	// Reports expectations
    	addViewState("reportGen", 
    			null, formAction.getCustomReportSelector(), 
    			new Action[]{invoke("prepareToRender",formAction)},
    			null, // no transitions since PDF not accepting input. user uses Back button to get back to previous state (could
    			      // construct hyperlinks in PDF report to link back to flow)
    			null,null,null); 

    	
    
    	buildDefaultActionEndState();
    }

  
}



