package edu.ucsf.lava.core.scope;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 * This interface uses a handler pattern to enable extension of the SessionManager functionality by
 * enabling a custom handling of specific session attributes.  Each handler is organized around the 
 * attributes defined at a particular scope
 * 
 * @author jhesse
 *
 */
public interface ScopeSessionAttributeHandler {
	
	/**
	 * Returns the scope of the Session Attribute Delegate
	 * @return
	 */
	public String getHandledScope();
	
	/**
	 * Returns the order (precedence) of the handler.  Each handler that handles an attribute 
	 * will be called in order (lowest to highest order) with the state of the object to assign to the 
	 * attribute passed on to each successive handler call. 
	 * @return
	 */
	public Long getOrder();
	
	/**
	 * returns whether this handler handles the attibute
	 * @param attribute
	 * @return
	 */
	public boolean handlesAttribute(String attribute);
	
	/**
	 * returns the list of handled attributes for the handler.
	 * @return
	 */
	public List<String> getHandledAttributes();
	
	/**
	 * add an attribute to the list of handled attributes, so it can be set in the session 
	 * @param handledAttribute name of attribute to handle
	 */
	public void addHandledAttribute(String handledAttribute);


	/** 
	 * returns the list of attributes handled by this handler that
	 * are considered part of the "runtime" context of the application
	 * @return
	 */
	public List<String> getContextAttributes();
	
	
	/**
	 * Take any actions necessary for setting the session attribute, return the end state of the value after handling to enable
	 * additional action by downstream handlers. 

	 * @param request
	 * @param attribute
	 * @param value
	 * @return
	 */
	public Object handleSetAttribute(HttpServletRequest request, String attribute, Object value);
	/**
	 * Get the attribute requested.  The value parameter will be set with the object retrieved by upstream handlers if any.  Otherwise
	 * it will be null.  

	 * @param request
	 * @param attribute
	 * @param value
	 * @return
	 */
	public Object handleGetAttribute(HttpServletRequest request, String attribute, Object value);
			
}
