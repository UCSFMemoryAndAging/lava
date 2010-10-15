package edu.ucsf.lava.crms.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.BaseFlowTypeBuilder;
import edu.ucsf.lava.core.webflow.builder.FlowInfo;

public class InstrumentFlowTypeBuilder extends BaseFlowTypeBuilder {
	// the events array is used by the authorization system to build a permissions cache
	// (on application deployment) based on action/event 

	// NOTE: it is not clear as to whether all of the custom events should be subject to authorization, but do so for now.
	// there are no flows built for the custom events, but they are listed here so that they are subject to 
	// authorization. custom events are events that take place within a given flow. since authorization is
	// enforced with flow granularity, the handler authCustomEvent method allows overriding this and enforcing
	// authorization for a given event within the flow
	public static final String[] INSTRUMENT_EVENTS = new String[]{"view","enter","enterReview","collect","upload",
		"add","delete","status","editStatus","changeVersion","custom","custom2","custom3","custom4","custom5"};

	public InstrumentFlowTypeBuilder() {
		super("instrument");
		setEvents(INSTRUMENT_EVENTS);
		setDefaultFlowMode("view");
	}


	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		
		// add instrument
		registry.registerFlowDefinition(assemble(actionId + ".add", 
				new InstrumentAddFlowBuilder(registry,actionId)));
		
    	// delete instrument
		registry.registerFlowDefinition(assemble(actionId + ".delete", 
				new InstrumentDeleteFlowBuilder(registry,actionId)));
		
		// edit status
		registry.registerFlowDefinition(assemble(actionId + ".editStatus", 
				new InstrumentEditStatusFlowBuilder(registry,actionId)));
				
		// edit status
		registry.registerFlowDefinition(assemble(actionId + ".changeVersion", 
				new InstrumentChangeVersionFlowBuilder(registry,actionId)));
		
		//	view status
		registry.registerFlowDefinition(assemble(actionId + ".status", 
				new InstrumentStatusFlowBuilder(registry,actionId)));

    	// collect instrument
		registry.registerFlowDefinition(assemble(actionId + ".collect", 
				new InstrumentCollectFlowBuilder(registry,actionId)));

    	// enter instrument
		registry.registerFlowDefinition(assemble(actionId + ".enter", 
				new InstrumentEnterFlowBuilder(registry,actionId)));
		
    	// enter instrument with review
		registry.registerFlowDefinition(assemble(actionId + ".enterReview", 
				new InstrumentEnterReviewFlowBuilder(registry,actionId)));
		
    	// upload instrument
		registry.registerFlowDefinition(assemble(actionId + ".upload", 
				new InstrumentUploadFlowBuilder(registry,actionId)));
		
		// view instrument (references enter and collect so must be last to be built)
		registry.registerFlowDefinition(assemble(actionId + ".view", 
			new InstrumentViewFlowBuilder(registry,actionId)));
	}

	
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = super.getSubFlowInfo(actionId, flowType, subFlowActionId,	actions);
		
		if(flowType.equals("instrumentList") || flowType.equals("list") || flowType.equals("entity") ){
			
			String target = ActionUtils.getTarget(subFlowActionId);
			
   			subFlowInfo.add(new FlowInfo(subFlowActionId,"add"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"delete"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"editStatus"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"changeVersion"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"status"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"collect"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"enter"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"enterReview"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"upload"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
		}
		else if(flowType.equals("instrumentGroup")){
			subFlowInfo.add(new FlowInfo(subFlowActionId,"view"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"enter"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"enterReview"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"upload"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"status"));
			subFlowInfo.add(new FlowInfo(subFlowActionId,"delete"));
		}
		
		return subFlowInfo;
	}
	
}