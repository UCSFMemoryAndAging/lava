package edu.ucsf.lava.core.scope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import edu.ucsf.lava.core.auth.AuthorizationContext;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.model.LavaEntity;


/**
 * Abstract base implementation of the Scope Authorization Context.  Subclasses should create default constructor that 
 * sets the scope and defines the context keys and wildcards for the scope.  All methods can be 
 * overridden to provide scope specific functionality.  Best practice is to provide specific getters and
 * setters for each context key so that the values can be easily accessed using bean introspection by entities of
 * the same scope.  
 * @author jhesse
 *
 */
public abstract class AbstractScopeAuthorizationContext implements ScopeAuthorizationContext {

	protected HashMap<String,Object>contextValues = new LinkedHashMap<String,Object>(); //linked hash map used to return the contextValues in a predicatable order for generating a unique cache key
	protected HashMap<String,Object>contextWildcards = new HashMap<String,Object>();
	protected String scope;
	
	
	
	
	
	/**
	 * Constructor that sets the scope identifier. Should be called by 
	 * each subclass constructor.
	 * @param scope
	 */
	public AbstractScopeAuthorizationContext(String scope) {
		super();
		this.scope=scope;
		}

	public List<String> getContextKeys() {
		return new ArrayList(contextValues.keySet());
	}
	/**
	 * Default implementation simply checks for key in internal map. 
	 * @param key
	 * @return
	 */
	public boolean hasContextKey(String key)
	{
		return contextValues.containsKey(key);
	}
	
	/**
	 * Default implementation.  Returns an empty string if 
	 * no context value is set for the key.  Returns null if the
	 * key does not exist. 
	 */
	public Object getContextValue(String key) {
		if(!hasContextKey(key)) return null;
		
		if(contextValues.get(key)==null){ return AuthorizationContext.NO_CONTEXT_VALUE;}
		
		return contextValues.get(key);
	}
	
	
	/**
	 * Adds a key to the underlying context map using the 
	 * default wildcard value
	 */
	protected void addContextKey(String key){
		addContextKey(key,DEFAULT_WILDCARD);
	}
	
	
	/**
	 * Adds a key to the underlying context map and assigns the 
	 * wildcard value
	 */
	protected void addContextKey(String key,Object wildcard){
		//don't add (and potentially overright the existing value if the key already exists
		if(!hasContextKey(key)){
			setContextValue(key, NO_CONTEXT_VALUE);
		}
		
		//we can reset the wildcard whether it exists or not (wildcard won't change)
		contextWildcards.put(key, wildcard);
	}
	
	public void setContextValue(String key, Object value){
		this.contextValues.put(key,value);
	}
	
	/**
	 * default implementation.  Returns the default wildcard if no
	 * specific wildcard is set for the context key. 
	 */
	public Object getContextWildcard(String key) {
		if(!contextWildcards.containsKey(key) || contextWildcards.get(key)==null){
			return AuthorizationContext.DEFAULT_WILDCARD;
		}
		return contextWildcards.get(key);
	}
	
	/**
	 * returns the scope of the AuthScopeContext.
	 */
	public String getScope() {
		return scope;
	}
	
	/**
	 * Default implementation of matching logic.  The idea is to try to match "this" context to the 
	 * matchContext.  Only ScopeAuthorizationContexts of the same scope can match.  Additionally, to match, 
	 * every key in this context that has an existing key in the matchContext must have a matching value,
	 * and all keys in the matchContext must have a matching value in this context (i.e. a match can be
	 * made even if this context has a key/value pair for which there is no matching key in the matchContext,
	 * but a match is not made if there is a key/value in the matchContext not present in this context).  
	 * 
	 * An individual key/pair match is thus defined as follows
	 * 
	 * result		this-key 		this-value				 	matchContext-key 	matchContext-value
	 * match		exists			any value,empty,wildcard 	missing				n/a
	 * match        exists			wildcard					exists              any value, empty, wildcard
	 * match		exists			any value,empty,wildcard	exists				wildcard
	 * match		exists 			any value,empty,wildcard	exists				same as this-value
	 * no match		missing			n/a							exists				any value,empty,wildcard
	 * no match 	exists			any value,empty				exists				different value from this-value
	 * 
	 * note: empty = null object or empty string								
	 */
	public boolean matches(AuthorizationContext matchContext) {
		//ensure that the matchContext is a scope authroization context, is not null and that the scopes match
		if(matchContext==null){return false;}
		if(!ScopeAuthorizationContext.class.isAssignableFrom(matchContext.getClass())){return false;}
		if(!getScope().equals(((ScopeAuthorizationContext)matchContext).getScope())){return false;}
		
		//handle the condition where the particular scope does not have any context implemented
		if(!hasContext() && !matchContext.hasContext()){return true;}
		
		
		//first check all the values in this AuthScopeContext against the matchContext
		for(String key : this.getContextKeys()){
			if(!this.matches(key,matchContext)){return false;}
		}
		
		//now check for any keys in matchContext that do not exist in this context
		for(String key : matchContext.getContextKeys()){
			if(!hasContextKey(key)){return false;}
		}
		
		//if we get here then we have a match;
		return true;
	
	}
	
	/**
	 * Default implementation.  See description of matches(AuthUserContext matchContext) above for
	 * the match logic. 
	 */
	public boolean matches(String key, AuthorizationContext matchContext) {
		//if the key does not exist in the matchContext then it is a match. 
		if(!matchContext.hasContextKey(key)){return true;}
		return matches(key,matchContext.getContextValue(key));
		}
	
	/**
	 * Default implementation.  See description of matches(AuthUserContext matchContext) above for
	 * the match logic. 
	 */
	public boolean matches(String key, Object matchValue) {
		/* if either value is a wildcard then it is a match
		 * note: we can use the local wildcard because AuthScopeContexts 
		 *       of the same scope must use the same wildcards for the same keys
		 */ 
		if(matchValue.equals(getContextWildcard(key))||
			getContextValue(key).equals(getContextWildcard(key))){return true;}
		
		//if the matchValue == the local value then it is a match
		if(matchValue.equals(getContextValue(key))){return true;}
		
		//if we get here then it is not a match
		return false;
	}
	/**
	 * Default implementation, returns false if there are no 
	 * keys in the internal map
	 */
	public boolean hasContext() {
		return !this.contextValues.isEmpty();
	}

	
	/**
	 * Default implementation that concatonates all keys and values
	 */
	public String getCacheKey() {
		StringBuffer cacheKey = new StringBuffer(getScope());
		for(String key : getContextKeys()){
			cacheKey.append(key).append("=").append(getContextValue(key).toString()).append(";");
		}
		return cacheKey.toString();
	}

	/**
	 * Default implementation to set the Context from an entity.
	 * This is basically a no-op method in the base implementation.
	 *
	 * The entity will need to be inspected by subclasses to determine
	 * if the entity is supported by the authContext.  
	 *
	 */
	public AuthorizationContext setContext(LavaEntity entity) {
		return this;
	}

	/**
	 * Default implementation to set the Context from a list of entities.
	 * This is basically a no-op method in the base implementation.
	 *
	 * Most subclasses will want to set the context to see if the user
	 * has permission for any context, as the list contents will be filtered 
	 * by the authorization filters.  If the user has permission in any context,
	 * then it is ok to authorize a list display action without first checking all 
	 * the items in the list (because of the filtering). 
	 */
	
	public AuthorizationContext setContext(ScrollablePagedListHolder list) {
		return this;
	}
	
	


	
	
}
