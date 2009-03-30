package edu.ucsf.lava.crms.webflow.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;
import edu.ucsf.lava.core.webflow.builder.FlowInfo;

/**
 * This flow type builder simply serves to register events will be 
 * registered with the authorization structures for specific 
 * instrument types.  Specific Instrument flows are not needed because the 
 * base instrument action/flows are used.  By extending the instrumentFlowTypeBuilder 
 * we get all the events defined for instruments.  We override all the functions to 
 * ensure that no flows get built. 
 * @author jhesse
 *
 */
public class InstrumentCommonFlowTypeBuilder extends InstrumentFlowTypeBuilder {
	public static String INSTRUMENT_COMMON_FLOW_TYPE = "instrumentCommon";
	public InstrumentCommonFlowTypeBuilder() {
		super();
		this.setType(INSTRUMENT_COMMON_FLOW_TYPE);
	}

	public void RegisterFlowTypeDefinitions(LavaFlowRegistrar registry,String actionId) {
		//now specific flows for instrument types...flows are handled by the base instrument action
		
	}

	//return empty list (not sure what would happen if we return null...probably nothing...
	public List<FlowInfo> getSubFlowInfo(String actionId, String flowType, String subFlowActionId, Map actions) {
		List<FlowInfo> subFlowInfo = new ArrayList<FlowInfo>();
		return subFlowInfo;
	}
	
}