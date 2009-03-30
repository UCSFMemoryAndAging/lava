package edu.ucsf.lava.core.action.model;

import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.webflow.builder.FlowTypeBuilder;

public interface Action {

	public boolean equals(Object action);
	public boolean equalsId(Object action);
	
	public Object clone();
	
	public String getDescription();
	public void setDescription(String description);
	
	public String getId();
	public void setId(String id);


	public String getIdParamName();
	public void setIdParamName(String idParamName);
	
	public String getModule();
	public void setModule(String module);
	
	public String getInstance();
	public void setInstance(String instance);
	
	public String getScope();
	public void setScope(String scope);

	public String getSection();
	public void setSection(String section);

	public String getTarget();
	public void setTarget(String target);
	
	public Object getParam(Object key);
	public void setParam(Object key, Object value);
	
	public Map getParams();
	public void setParams(Map params);
	
	public boolean recordInActionHistory();
	
	public String getActionUrl();
	public String getActionUrlWithIdParam();
	public String getActionUrlWithModeParam();
	public String getActionUrlWithParams();
	public void setActionUrl(String actionUrl);
	
	
	
	
	
	public boolean getHomeDefault();
	public void setHomeDefault(boolean homeDefault);
	
	public boolean getModuleDefault();
	public void setModuleDefault(boolean moduleDefault);

	public boolean getSectionDefault();
	public void setSectionDefault(boolean sectionDefault);

	public FlowTypeBuilder getFlowTypeBuilder();
	public void setFlowTypeBuilder(FlowTypeBuilder flowTypeBuilder);
	public String getFlowType();
	public String getDefaultFlowMode();
	
	public void setParentFlow(String parentFlow);
	public void clearParentFlow(String parentFlow);
	public void clearParentFlows();
	public List<String> getParentFlows();
	public void setParentFlows(List<String> parentFlows);
	
	

	public void setSubFlow(String subFlow);
	public void clearSubFlow(String subFlow);
	public void clearSubFlows();
	public List<String> getSubFlows();
	public void setSubFlows(List<String> subFlows);

	public List<String> getCustomizingFlows();
	public void setCustomizingFlows(List<String> customizingFlows);
	public void clearCustomizingFlow(String customizingFlow);
	public void clearCustomizingFlows();
	public void setCustomizingFlow(String customizingFlow);

	
	public void setCustomizedFlow(String customizedFlow);
	public void clearCustomizedFlow();
	public String getCustomizedFlow();
	
	public void setViewBasePath(String viewBasePath);
	public String getViewBasePath();
	
	public void setCustomizedViewBasePath(String customizedViewBasePath);
	public String getCustomizedViewBasePath();
	
	public String getActionView();
	
	public boolean isCustomizedInstanceAction();
	

	

	public String makeCacheKey(String mode);
	public String makeCacheKey();
	
}

