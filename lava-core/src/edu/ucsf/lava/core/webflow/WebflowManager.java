package edu.ucsf.lava.core.webflow;

import edu.ucsf.lava.core.manager.LavaManager;

public class WebflowManager extends LavaManager {

	public static String WEBFLOW_MANAGER_NAME = "webflowManager";
	FlowRegistryFactoryBean flowRegistryFactoryBean;

	public WebflowManager(){
		super(WEBFLOW_MANAGER_NAME);
	}
	
	public void initializeFlows(){
		if(flowRegistryFactoryBean!=null){
			flowRegistryFactoryBean.initializeFlows();
		}
	}
	
	public FlowRegistryFactoryBean getFlowRegistryFactoryBean() {
		return flowRegistryFactoryBean;
	}

	public void setFlowRegistryFactoryBean(
			FlowRegistryFactoryBean flowRegistryFactoryBean) {
		this.flowRegistryFactoryBean = flowRegistryFactoryBean;
	}
	
	
	
	
	
}
