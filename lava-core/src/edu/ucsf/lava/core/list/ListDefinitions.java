package edu.ucsf.lava.core.list;

import java.util.HashMap;
import java.util.Map;

import edu.ucsf.lava.core.list.model.BaseListConfig;


public class ListDefinitions {
	private Map<String,BaseListConfig> definitions = new HashMap<String, BaseListConfig>();

	public Map<String, BaseListConfig> getDefinitions() {
		return definitions;
	}

	public BaseListConfig get(String listName) {
		if(definitions.containsKey(listName)){
			return definitions.get(listName);
		}
		return null;
		
	}

	public void setDefinitions(Map<String, BaseListConfig> definitions) {
		this.definitions = definitions;
	}
	
	public void addListDefinition(BaseListConfig listDefinition){
		this.definitions.put(listDefinition.getName(),listDefinition);
	}
	
	
	
}
