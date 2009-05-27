package edu.ucsf.lava.core.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.action.model.Action;


/**
 * A simple class around the group of PasswordDelegate implementations.
 * @author jhesse
 *
 */
public class PasswordDelegates {
	
	private Map<String,PasswordDelegate> delegates = new HashMap<String, PasswordDelegate>();
	
	/**
	 * Return the registered delegate maps 
	 * @return
	 */
	public Map<String, PasswordDelegate> getDelegates() {
		return delegates;
	}
	
	/**
	 * Get a particular password delegate for a specific authentication type
	 * @param scope
	 * @return returns null if no delegate found for the given type
	 */
	public PasswordDelegate get(String authenticationType) {
		if(delegates.containsKey(authenticationType)){
			return delegates.get(authenticationType);
		}
		return null;
	}
	

	/**
	 * Adds a delegate.  Will replace an existing delegate for a authetication type if the order id
	 * of the new PasswordDelegate is lower than the existing PasswordDelegate order id
	 * (otherwise the delegate is not added -- if the authentication type already has a lower ordered delegate assigned). 
	 * @param passwordDelegate
	 */
	public void addDelegate(PasswordDelegate passwordDelegate){
		if(passwordDelegate==null){return;}
		String authenticationType = passwordDelegate.getAuthenticationType();
		if(delegates.containsKey(authenticationType) && delegates.get(authenticationType)!=null){
			if(passwordDelegate.getOrder() > delegates.get(authenticationType).getOrder()){
				return; //the password delegate provided has a higher (less priority) order number than an existing delegate for the authentication type...so discard...
			}
		}
		delegates.put(authenticationType, passwordDelegate);
	}
	
}
