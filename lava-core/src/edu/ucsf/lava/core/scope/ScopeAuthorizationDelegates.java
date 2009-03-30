package edu.ucsf.lava.core.scope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.model.Action;


/**
 * A simple class around the group of ScopeAuthorizationDelegate implementations.
 * @author jhesse
 *
 */
public class ScopeAuthorizationDelegates {
	
	private Map<String,ScopeAuthorizationDelegate> delegates = new HashMap<String, ScopeAuthorizationDelegate>();
	
	/**
	 * Gets the registered scope authorization delegates
	 * @return
	 */
	public Map<String, ScopeAuthorizationDelegate> getDelegates() {
		return delegates;
	}
	
	/**
	 * Gets the registered scope authorization delegates in an ordered 
	 * list based on their order id property (useful for when
	 * the delegates need to take coordinated actions in a fixed sequence).
	 * @return
	 */
	public List<ScopeAuthorizationDelegate> getOrderedDelegates(){
		ArrayList orderedDelegates = new ArrayList(delegates.values());
		Collections.sort(orderedDelegates);
		return orderedDelegates;
	}
	
	/**
	 * Get a particular scope authorization delegate for a specific scope
	 * @param scope
	 * @return returns null if no delegate found for the given scope
	 */
	public ScopeAuthorizationDelegate get(String scope) {
		if(delegates.containsKey(scope)){
			return delegates.get(scope);
		}
		return null;
	}
	

	
	
	
	/**
	 * Adds a delegate.  Will replace an existing delegate for a scope if the order id
	 * of the new scopeAuthorizationDelegate is lower than the existing ScopeAuthorizationDelegate's order id
	 * (otherwise the delegate is not added -- if the scope already has a lower ordered delegate assigned). 
	 * @param scopeAuthorizationDelegate
	 */
	public void addDelegate(ScopeAuthorizationDelegate scopeAuthorizationDelegate){
		if(scopeAuthorizationDelegate==null){return;}
		String scope = scopeAuthorizationDelegate.getHandledScope();
		if(delegates.containsKey(scope) && delegates.get(scope)!=null){
			if(scopeAuthorizationDelegate.getOrder() > delegates.get(scope).getOrder()){
				return; //the scope action delegate provided has a higher order id than an existing delegate for the scope...so discard...
			}
		}
		delegates.put(scope, scopeAuthorizationDelegate);
	}
	
}
