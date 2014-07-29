package edu.ucsf.lava.core.webflow.builder;

import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * This defines an import flow where the initial state is where the user sets up the import (i.e. 
 * chooses import template and data file), the "import" event then imports the data and logs
 * the import results and transitions to the final state which displays the import log.
 */
public class ImportFlowBuilder extends BaseFlowBuilder {
	
	public ImportFlowBuilder(LavaFlowRegistrar registry, String actionId) {
		super(registry, actionId);
		
		// this is essentially the name of the initial view state, used by many of
		// the core transitions in the BaseFlowBuilder
		// note: to support customizing flows the flowEvent must be the same name as the flow mode, i.e. 
		// the last part of the flowId
		setFlowEvent("edit");
	}	 
    
    public void buildEventStates() throws FlowBuilderException {
       
    	addViewState("edit", 
    			null, 
    			formAction.getCustomViewSelector(),
    			// setupForm called in flowSetupState so no need to call here
    			// setupForm calls getBackingObjects which creates the LavaDaoFilter object which handles the 
    			// specified parameters for a given report, and puts it into the model within a ReportSetup command object
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
    				// when user selects an import template the page is refreshed to re-populate the
    				// secondary list of import logs for the selected template
        			// note: required field validation does not take place on refreshRefData so no required
        			// field errors will be shown
                    transition(on(objectName + "__reRender"), to("edit"), 
                       	ifReturnedSuccess(new Action[]{
                       		invoke("customBind", formAction),
                       		invoke("handleFlowEvent", formAction)})),
                    transition(on(objectName + "__import"), to("result"), 
                       	ifReturnedSuccess(new Action[]{
                       		invoke("customBind", formAction),
                       		invoke("handleFlowEvent", formAction)})),
                    transition(on(objectName + "__close"), to("finish"), 
                        	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    // support a list secondary component for nav events only, since just a reference list
                    buildListNavigationTransitions("edit")},
          		null,null,null);
    	
    	addViewState("result", 
    			null, formAction.getCustomViewSelector(), 
    			new Action[]{invoke("prepareToRender",formAction)},
    			new Transition[] { 
// on close event prob. either need to clear out the importSetup properties or redirect to this flow to 
// restart it as a root flow which will start with a fresh command object
                    transition(on(objectName + "__close"), to("edit"), 
                       	ifReturnedSuccess(invoke("handleFlowEvent", formAction))),
                    // support a list secondary component for nav events only, since just a reference list
                    buildListNavigationTransitions("result")},
    			null,null,null); 
    
    	buildDefaultActionEndState();
    }
  
}



