package edu.ucsf.lava.core.logiccheck;

import java.util.HashMap;
import java.util.Map;

import edu.ucsf.lava.core.logiccheck.model.LogicCheckEntityConfig;

public class LogicCheckEntityConfigs {
	private Map<String,LogicCheckEntityConfig> entityConfigs = new HashMap<String, LogicCheckEntityConfig>();

	public Map<String, LogicCheckEntityConfig> getEntityConfigs() {
		return entityConfigs;
	}

	public LogicCheckEntityConfig get(String entityName) {
		if(entityConfigs.containsKey(entityName)){
			return entityConfigs.get(entityName);
		}
		return null;
		
	}

	public void setEntityConfigs(Map<String, LogicCheckEntityConfig> entityConfigs) {
		this.entityConfigs = entityConfigs;
	}
	
	public void addLogicCheckEntityConfig(LogicCheckEntityConfig config){
		this.entityConfigs.put(config.getEntityName(), config);
	}
	
}
