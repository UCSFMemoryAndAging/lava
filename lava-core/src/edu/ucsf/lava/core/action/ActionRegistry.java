package edu.ucsf.lava.core.action;

import java.util.HashMap;
import java.util.Map;

import edu.ucsf.lava.core.action.model.Action;


public class ActionRegistry {
	private Map<String,Action> actions = new HashMap<String, Action>();

	public Map<String, Action> getActions() {
		return actions;
	}
	
	public Map<String,Action> getActionsCopy(){
		Map<String,Action> actionsCopy = new HashMap<String,Action>(actions.size());
		for(String key:actions.keySet()){
			actionsCopy.put(key,getAction(key));
		}
		return actionsCopy;
	}
	
	public boolean containsAction(String actionId){
		return actions.containsKey(actionId);
	}
	
	public Action getActionInternalCopy(String ActionId) {
		if(actions.containsKey(ActionId)){
			return actions.get(ActionId);
		}
		return null;
		
	}
	
	public void addAction(String key,Action action){
		actions.put(key, action);
	}

	public Action getAction(String ActionId) {
		if(actions.containsKey(ActionId)){
			return (Action)actions.get(ActionId).clone();
		}
		return null;
		
	}

	public void setActions(Map<String, Action> actions) {
		this.actions = actions;
	}
	
	
	
}
