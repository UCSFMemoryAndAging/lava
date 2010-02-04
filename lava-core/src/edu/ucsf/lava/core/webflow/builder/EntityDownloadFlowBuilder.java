package edu.ucsf.lava.core.webflow.builder;

import org.springframework.binding.mapping.AttributeMapper;
import org.springframework.binding.mapping.DefaultAttributeMapper;
import org.springframework.binding.mapping.Mapping;
import org.springframework.webflow.engine.builder.FlowBuilderException;
import org.springframework.webflow.execution.Action;

import edu.ucsf.lava.core.webflow.LavaFlowRegistrar;

/**
 * Java-based flow builder that builds the download entity flow, parameterized
 * for a specific entity type.
 * <p>
 * This encapsulates the page flow of downloading a file (e.g. a PDF or MRI from the entity).
 * 
 */
class EntityDownloadFlowBuilder extends BaseFlowBuilder {
	

	public EntityDownloadFlowBuilder(LavaFlowRegistrar registry, String actionId) {
    	super(registry, actionId);
    	setFlowEvent("download");
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
    	
    	
    	
    	addViewState(getFlowEvent(), 
    			null, 
    			null, //no view selector because we will be returning a byte stream and mimietype to the browser
    			// 
    			new Action[]{invoke("prepareDownload",formAction)},
    			null,null, null, null);

    	
    	}
}



