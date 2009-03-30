package edu.ucsf.lava.core.scope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * A simple class around the group of ScopeSessionAttributeHandler implementations.
 * @author jhesse
 *
 */
public class ScopeSessionAttributeHandlers {
	
	protected Map<String,ScopeSessionAttributeHandler> handlers = new HashMap<String, ScopeSessionAttributeHandler>();
	protected ArrayList<ScopeSessionAttributeHandler> orderedHandlers = new ArrayList();
	/**
	 * Gets the registered scope session attribute handlers
	 * @return
	 */
	public Map<String, ScopeSessionAttributeHandler> getHandlers() {
		return handlers;
	}
	
	
	public List<ScopeSessionAttributeHandler> getOrderedHandlers(){
		return orderedHandlers;
	}
	
	/**
	 * Puts the registered scope session attribute handlers in an ordered 
	 * list based on their order id property.  Every handler that handles an attribute 
	 * will get a change to set or get the attribute value with later handlers able to override the 
	 * actions of earlier handlers.   
	 * @return
	 */
	protected void setOrderedHandlers(){
		ArrayList<AbstractScopeSessionAttributeHandler> ordered = new ArrayList(handlers.values());
		Collections.sort(ordered);
		this.orderedHandlers = new ArrayList<ScopeSessionAttributeHandler>(ordered);
	}
	/**
	 * Adds a handler.  If the handler has a lower order id than an existing handler for the 
	 * exact same scope, it will overright the higher ordered handler.  
	 * @param scopeSessionAttributeHandler
	 */
	public void addHandler(ScopeSessionAttributeHandler scopeSessionAttributeHandler){
		if(scopeSessionAttributeHandler==null){return;}
		String scope = scopeSessionAttributeHandler.getHandledScope();
		if(handlers.containsKey(scope) && handlers.get(scope)!=null){
			if(scopeSessionAttributeHandler.getOrder() > handlers.get(scope).getOrder()){
				return; //the scope session attribute handler provided has a higher order id than an existing handler for the same scope...so discard...
			}
		}
		handlers.put(scope, scopeSessionAttributeHandler);
		setOrderedHandlers();
	}
	
	/**
	 * Sets an attribute in session scope.  Defers stting logic to a chain of handlers.  
	 * @param sessionManager
	 * @param request
	 * @param attribute
	 * @param value
	 */
	public void setAttribute(HttpServletRequest request, String attribute, Object value){
		Object handledValue = value;
		for(ScopeSessionAttributeHandler handler : orderedHandlers){
			if(handler.handlesAttribute(attribute)){
				handledValue = handler.handleSetAttribute(request, attribute, handledValue);
			}
		}
	}
	
	/**
	 * Get an attribute from session scope. Defers retrieval logic to a chain of handlers. 
	 * @param sessionManager
	 * @param request
	 * @param attribute
	 * @param value
	 * @return
	 */
	public Object getAttribute(HttpServletRequest request, String attribute){
		Object handledValue = null;
		for(ScopeSessionAttributeHandler handler : orderedHandlers){
			if(handler.handlesAttribute(attribute)){
				handledValue = handler.handleGetAttribute(request, attribute, handledValue);
			}
		}
		return handledValue;
	}
	
	/**
	 * returns a map of all the session attributes that represent the runtime
	 * context of the application.  Which attributes to add to the context is
	 * deferred to the individial handlers.   Creates a unique set of all the 
	 * context attibutes from the handlers, then calls the getAttribute
	 * method to give each handler the opportunity to take part in retrieving the
	 * attributes. 
	 * @param request
	 * @return
	 */
	public Map getContextFromSession(HttpServletRequest request){
		HashSet<String> attributes = new HashSet<String>();
		Map<String,Object> context = new HashMap<String,Object>();
		for(ScopeSessionAttributeHandler handler : orderedHandlers){
			attributes.addAll(handler.getContextAttributes());
		}
		for(String attribute: attributes){
			context.put(attribute,getAttribute(request,attribute));		
		}
		return context;
	}
		

}
