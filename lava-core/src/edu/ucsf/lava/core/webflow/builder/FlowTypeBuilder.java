package edu.ucsf.lava.core.webflow.builder;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * This is an interface that defines a builder that knows how to build a set of flows for
 * a "type". Each action is of a particular "type" and the FlowTypeBuilder will be called
 * once for each action of that type.
 *
 */
public interface FlowTypeBuilder {
	
	public static final String DEFAULT_FLOW_MODE = "view";
	public static final String FLOW_TYPE_NONE = "none";
	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry, String actionId);	    
	public List<FlowInfo> getSubFlowInfo(String subActionId, String parentFlowType, String parentActionId, Map actions);
    public String getType();
    public String[] getEvents();
    public String getDefaultFlowMode();
    
    
	
	
		
		
}



