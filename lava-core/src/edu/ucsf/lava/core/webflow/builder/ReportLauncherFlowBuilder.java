package edu.ucsf.lava.core.webflow.builder;

import java.util.ArrayList;

import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.State;
import org.springframework.webflow.engine.Transition;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * This defines a generalized report flow, where the initial state displays
 * the report parameter inputs as well as the desired report format (e.g. PDF, Excel)
 * and the final state displays the report in the desired format.
 */
class ReportLauncherFlowBuilder extends BaseFlowBuilder {
	
	public ReportLauncherFlowBuilder(LavaFlowRegistrar registry, String actionId) {
		super(registry, actionId);
		
		// hard-code object name because there is currently no CrmsReportLauncherComopnentHandler
		// so the lava-core ReportLauncherComponentHandler is used for both lava-core and lava-crms,
		// so want the events which are based on the objectName to be prefixed "reportLauncher_"
		this.objectName = "reportLauncher";
		
		// this is essentially the name of the initial state, used by many of
		// the core transitions in the BaseFlowBuilder
		setFlowEvent("decision");
	}
	
	// this flow will be invoked in one of two ways:
	// 1) report/report/reportLauncher
	//    In this case, the flow goes to the "launcher" view state, which is the Report Module/Section default 
	//    page with links to all reports
	// 2) report/report/reportLauncher?_flowExecutionKey=_....&_eventId=reportLauncher__view&param=REPORT_NAME
	//    In this case, the flow bypasses the "launcher" view state and goes directly to the subflow for the specified
	//    report. This usage is for links in left nav report actions.
	//
	// However, when 1) above is used such that the flow is paused in the "launcher" state displaying 
	// a page with report links, those links are in form 2) above, which results in a transition from 
	// the "launcher" state to the state identified by the "param" request parameter
	

	// the "param" request parameter identifies the report to generate into the flow. it is mapped
	// into the flow as a flowScope variable by BaseFlowBuilder buildInputMapper. if the "param" request 
	// paramter does not exist, it indicates that the report launcher view state should be displayed, 
	// rather than going to a specific report subflow
	

    public void buildEventStates() throws FlowBuilderException {
    	String decisionState = getFlowEvent();
    	
    	// the first state (beyond the standard flowSetupState action state) is a decision state that checks to
    	// see if a report request parameter ("param") was specified, and if so, transitions to the subflow for
    	// that report, and if not, transitions to the "launcher" view state which has links to all reports.
    	
    	// specific report subflows will have state id's such as "projectPatientStatus__view" and flow id's
    	// such as lava.reporting.reports.projectPatientStatus.view". these are created as part of the flow
    	// creation process by virtue of declaring an action for each report whose parent is the reportLauncher
    	// action, lava.reporting.reports.launcher, and defining the ReportFlowTypeBuilder getSubFlowInfo method.
    	
    	// the report launcher is an intermediate subflow of every action which has a link to a specific report,
    	// and makes it easy to configure specific report actions since they all have the same parent, while
    	// also serving as the reporting.reports module/section default view which has links to all reports
    	
		addDecisionState(decisionState, null,
				new Transition[] {
					transition(on("${flowScope.param == null}"), to("launcher")),
					transition(on("${flowScope.param != null}"), to("${flowScope.param}" + "__view"))},
				null, null, null);	

    	
    	// handle patient context change/clear, project context change/clear
		
        // these events will only be generated if the user is in the "launcher" view state, 
		// in which case they should return to the "launcher" view state, not the decision state
		// (which would just transition them to the "launcher" view state)

    	ArrayList<Transition> viewTransitions = new ArrayList<Transition>();
        // in the "launcher" view state, report links are displayed, and they all generate the "reportLauncher__view"
        // event (to be consistent with invoking this flow from a left nav report action) and specify the report
        // name in the "param" request parameter
    	// note that there is no need to call buildSubflowTransitions because the decisionState
        // and the following transition effectively replaces this
		viewTransitions.add(transition(on("reportLauncher__view"), to("${requestParameters.param}" + "__view")));

    	addViewState("launcher", 
    			null, 
    			formAction.getCustomViewSelector(),
    			new Action[]{invoke("prepareToRender",formAction)},
    			viewTransitions.toArray(new Transition[0]),		
          		null,
    			null,
    			null);

        // if there is a context change event it means that user was in the "launcher" view state,
        // so return to that state
    	buildDefaultActionEndState();
    }

	// override buildSubFlowReturnStates because it is completely different than the base class. instead 
	// it is a decisionState which  goes back to the "launcher" view state if the flowScope.param is null, or 
    // will return to a finish state if flowScope.param is not null, which will terminate the launcher flow 
    // and return to the parent flow from which the report launcher was initiated
	protected void buildSubFlowReturnStates() {
		addDecisionState("subFlowReturnState", null,
			new Transition[] {
				transition(on("${flowScope.param == null}"), to("launcher")),
				transition(on("${flowScope.param != null}"), to("finish"))},
			null, null, null);
	}
        	
}



