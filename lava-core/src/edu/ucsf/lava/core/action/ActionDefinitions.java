package edu.ucsf.lava.core.action;

import java.util.HashMap;
import java.util.Map;

import edu.ucsf.lava.core.action.model.Action;


public class ActionDefinitions {
	private Map<String,Action> definitions = new HashMap<String, Action>();

	public Map<String, Action> getDefinitions() {
		return definitions;
	}

	public Map<String,Action> getDefinitionsCopy(){
		Map<String,Action> definitionsCopy = new HashMap<String,Action>(definitions.size());
		for(String key:definitions.keySet()){
			definitionsCopy.put(key,getCopy(key));
		}
		return definitionsCopy;
	}
	
	public Action get(String ActionId) {
		if(definitions.containsKey(ActionId)){
			return definitions.get(ActionId);
		}
		return null;
		
	}

	public Action getCopy(String ActionId) {
		if(definitions.containsKey(ActionId)){
			return (Action)definitions.get(ActionId).clone();
		}
		return null;
		
	}
	public void setDefinitions(Map<String, Action> definitions) {
		this.definitions = definitions;
	}
	
	public void addActionDefinition(Action action){
		this.definitions.put(action.getId(), action);
	}
	
}
