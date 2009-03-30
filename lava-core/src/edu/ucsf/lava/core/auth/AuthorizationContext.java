package edu.ucsf.lava.core.auth;

import java.util.List;

import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.model.LavaEntity;

public interface AuthorizationContext {
	
	public static String NO_CONTEXT_VALUE = "";
	public static String DEFAULT_WILDCARD = "*";

	/**
	 * Returns false if the authorization context has no context keys (i.e. if there is  
	 * no "runtime" context that would affect authorization).  
	 * @return
	 */
	public boolean hasContext();
	
	/**
	 * Returns the list of context key values (i.e. the names of the contexts).
	 * @return
	 */
	public List<String> getContextKeys();
	
	/**
	 * Returns whether the context key exists in this authorization context. 
	 */
	public boolean hasContextKey(String key);
		
	/**
	 * Returns the current value of the context represented by key
	 * @param key the context name
	 * @return
	 */
	public Object getContextValue(String key);
	
	/**
	 * Sets the current value of the context represented by key
	 * @param key the context name
	 * @param value the value
	 * @return
	 */
	public void setContextValue(String key, Object value);
	
	
	
	
	/** 
	 * Returns the wildcard value for the context key.  The wildcard value
	 * is the value that matches all values set for the context key. 
	 * @param key the context name
	 * @return
	 */
	public Object getContextWildcard(String key);
	
	/**
	 * Determines if the matchContext matches this Authorization context.
	 * The particulars of the match logic are deferred to implementations of this interface. 
	 * @param matchContext
	 * @return
	 */
	public boolean matches(AuthorizationContext matchContext);
	
	/**
	 * Determines if the context value associated with key in this
	 * authorization context matches the value associated with the same key
	 * in the matchContext.  The particulars of the match logic is deferred to implementations of 
	 * this interface. 
	 * implementations. 
	 * @param key
	 * @param matchContext
	 * @return
	 */
	public boolean matches(String key, AuthorizationContext matchContext);
	
	/**
	 * Determines if the context value associated with key in this
	 * authroization context matches the matchValue.  The particulars of the match logic 
	 * are deferred to implementations of this interface. 
	 * @param key
	 * @param matchValue
	 * @return
	 */
	
	public boolean matches(String key, Object matchValue);
	
	/**
	 * Return a unique key representing the specific combination of 
	 * context key/value pairs set in this authroization context. 
	 * @return
	 */
	public String getCacheKey();
	
	/**
	 * Sets the context from a lava entity, as a convenience it returns
	 * itself from the method to support typical method chaining call signature. 
	 */
	public AuthorizationContext setContext(LavaEntity entity);
	
	/** 
	 * Sets the context from a list of lava entities, as a convenience it returns
	 * itself from the method to support typical method chaining call signature.
	 */
	public AuthorizationContext setContext(ScrollablePagedListHolder list);
}
