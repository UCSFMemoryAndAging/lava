package edu.ucsf.lava.core.scope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.model.Action;


/**
 * A simple class around the group of ScopeActionDelegate implementations.
 * @author jhesse
 *
 */
public class ScopeActionDelegates {
	
	private Map<String,ScopeActionDelegate> delegates = new HashMap<String, ScopeActionDelegate>();
	
	/**
	 * Gets the registered scope action delegates
	 * @return
	 */
	public Map<String, ScopeActionDelegate> getDelegates() {
		return delegates;
	}
	
	/**
	 * Gets the registered scope action delegates in an ordered 
	 * list based on their order id property (useful for when
	 * the delegates need to take coordinated actions in a fixed sequence
	 * e.g. the onReleadActionDefinitions()). 
	 * @return
	 */
	public List<ScopeActionDelegate> getOrderedDelegates(){
		ArrayList orderedDelegates = new ArrayList(delegates.values());
		Collections.sort(orderedDelegates);
		return orderedDelegates;
	}
	
	/**
	 * Get a particular scope action delegate for a specific scope
	 * @param scope
	 * @return returns null if no delegate found for the given scope
	 */
	public ScopeActionDelegate get(String scope) {
		if(delegates.containsKey(scope)){
			return delegates.get(scope);
		}
		return null;
	}
	

	/**
	 * Adds a delegate.  Will replace an existing delegate for a scope if the order id
	 * of the new scopeActionDelegate is lower than the existing scopeActionDelegates order id
	 * (otherwise the delegate is not added -- if the scope already has a lower ordered delegate assigned). 
	 * @param scopeActionDelegate
	 */
	public void addDelegate(ScopeActionDelegate scopeActionDelegate){
		if(scopeActionDelegate==null){return;}
		String scope = scopeActionDelegate.getHandledScope();
		if(delegates.containsKey(scope) && delegates.get(scope)!=null){
			if(scopeActionDelegate.getOrder() > delegates.get(scope).getOrder()){
				return; //the scope action delegate provided has a higher order id than an existing delegate for the scope...so discard...
			}
		}
		delegates.put(scope, scopeActionDelegate);
	}
	
}
