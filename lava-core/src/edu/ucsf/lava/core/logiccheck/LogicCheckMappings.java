package edu.ucsf.lava.core.logiccheck;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucsf.lava.core.logiccheck.model.LogicCheck;
import edu.ucsf.lava.core.model.EntityBase;

public class LogicCheckMappings {
	private Map<String, LogicCheckMapping> logiccheckMappings = new HashMap<String, LogicCheckMapping>();
	
	
	public Map<String, LogicCheckMapping> getLogiccheckMappings() {
		return logiccheckMappings;
	}

	public void setLogiccheckMappings(Map<String, LogicCheckMapping> logiccheckMappings) {
		this.logiccheckMappings = logiccheckMappings;
	}

	public LogicCheckMapping getLogiccheckMapping(EntityBase entity, LogicCheck lc){
		// find the IdMapping that handles this entity-property
		for(Entry<String, LogicCheckMapping> entry : logiccheckMappings.entrySet()) {
			if(entry.getValue().handlesEntityAndLogicCheck(entity, lc)){
				return entry.getValue();
			}
		}
		// it is not guaranteed that there is a default IdMapping; return null
		return null;
	}
	
	/**
	 * Adds an logiccheck mapping. 
	 * @param logiccheckMapping
	 */
	public void addLogiccheckMapping(LogicCheckMapping logiccheckMapping){
		if(logiccheckMapping!=null){
			this.logiccheckMappings.put(logiccheckMapping.getId(),logiccheckMapping);
		}
	}
	
}
