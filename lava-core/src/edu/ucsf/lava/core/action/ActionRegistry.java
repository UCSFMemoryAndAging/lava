package edu.ucsf.lava.core.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.action.model.Action;


public class ActionRegistry {
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    
    
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
	/**
	 * Adds action to the registry using the key provided.  If there is an action in the registry for the key already then 
	 * the precedenceLevels are checked.  Same or higher precedence level overrides existing action. 
	 * @param key
	 * @param action
	 */
	public void addAction(String key,Action action){
		if(action == null){
			logger.error("Null action passed to addAction for key" + key);
			return;
			}
		Action existing = actions.get(key);
		if(existing != null){
			if(existing.getPrecedenceLevel() > action.getPrecedenceLevel()){
				logger.debug("Action id: " + action.getId() + " overridden by existing action id:" + existing.getId() + " for action registry key " + key);
				return;
			}else{
				logger.debug("Action id: " + action.getId() + " overiding action id:" + existing.getId() + " for action registry key " + key);
			}
		}
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
