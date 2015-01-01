package edu.ucsf.lava.core.scope;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A base class that implements the core functionality and interface of a ScopeSessionAttributeHanlder
 * 
 * @author jhesse
 *
 */
public abstract class AbstractScopeSessionAttributeHandler implements Comparable<ScopeSessionAttributeHandler>, ScopeSessionAttributeHandler {

	protected static Long DEFAULT_ORDER=new Long(1000);
	protected ScopeSessionAttributeHandlers attributeHandlers;
	protected String handledScope;
	protected Long order;
    protected Set<String> handledAttributes = new HashSet<String>();
    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

	
    


    /**
     * Return a list of handled Attributes
     */
	public List<String> getHandledAttributes() {
		
		return new ArrayList<String>(handledAttributes);
	}
	
	
	/**
	 * returns the list of attributes handled by this handler that
	 * are considered part of the "runtime" context of the application. 
	 * Default implementation is to return all handled attributes.
	 * 
	 */
	public List<String> getContextAttributes() {
		 return this.getHandledAttributes();
	}



	/**
	 * Adds an attribute to the list of handled attributes.
	 * @param handledAttribute
	 */
	public void addHandledAttribute(String handledAttribute) {
		this.handledAttributes.add(handledAttribute);
	}

	
	/**
	 * Default implementation, simply stores the attribute
	 */
	public Object handleGetAttribute(HttpServletRequest request, String attribute, Object value) {
		return getSessionAttribute(request, attribute);
	}

	/** 
	 * Returns whether the attribute is handled by this handler. 
	 */
	public boolean handlesAttribute(String attribute) {
		return handledAttributes.contains(attribute);
	}

	/**
	 * Default Implementation simply gets the attribute
	 */
	public Object handleSetAttribute(HttpServletRequest request, String attribute, Object value) {
		setSessionAttribute(request, attribute, value);
		return value;
	}

	/**
	 * returns the scope of this handler. 
	 */
	public String getHandledScope() {
		return handledScope;
	}

	
	public void setHandledScope(String handledScope) {
		this.handledScope = handledScope;
	}



	public Long getOrder() {
		if(this.order==null){
			this.order = DEFAULT_ORDER;
		}
		return order;
	}

	public void setOrder(Long order) {
		this.order = order;
	}

	/** 
	 * Compares based on the "order" field.  Used by the collection of handlers to build a sorted list of handlers. 
	 */
	public int compareTo(ScopeSessionAttributeHandler scopeSessionAttributeHandler) throws ClassCastException {
	   
		return order.compareTo(scopeSessionAttributeHandler.getOrder());    
	}
	
	/**
	 * Store the attribute in the session attributes of the request. 
	 * @param request
	 * @param attribute
	 * @param object
	 */
	protected void setSessionAttribute(HttpServletRequest request,String attribute, Object object){
		//if no active session then do nothing
		if(request.getSession(false)==null){return;}
		
		request.getSession().setAttribute(attribute,object);
	}

	/**
	 * get the attribute from the session attributes of the request. 
	 * @param request
	 * @param attribute
	 * @return
	 */
	protected Object getSessionAttribute(HttpServletRequest request,String attribute){
		//if no active session then return null
		if(request.getSession(false)==null){return null;}
		
		return request.getSession().getAttribute(attribute);
	}
	
	
	public void setScopeSessionAttributeHandlers(ScopeSessionAttributeHandlers scopeSessionAttributeHandlers) {
		this.attributeHandlers = scopeSessionAttributeHandlers;
		scopeSessionAttributeHandlers.addHandler(this);
	}

	/**
	 * clears the session attribute (sets it to null). 
	 * @param request
	 * @param attribute
	 */
	public void clearSessionAttribute(HttpServletRequest request,String attribute){
		handleSetAttribute(request,attribute,null);
	}
	
}
