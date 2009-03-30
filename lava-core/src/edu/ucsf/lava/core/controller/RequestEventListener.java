package edu.ucsf.lava.core.controller;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestEventListener implements ServletRequestListener {
    protected final Log logger = LogFactory.getLog(getClass());
	
	//protected SessionService sessionService;
	//protected ActionService actionService;
    
	public void requestDestroyed(ServletRequestEvent sre) {
		// TODO Auto-generated method stub
	}

	/*
	public ActionService getActionService(ServletRequestEvent sre){
		if (actionService == null){
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(
			sre.getServletContext());
			actionService = (ActionService) ctx.getBean("actionService");
		}
		return actionService;
	}
	public SessionService getSessionService(ServletRequestEvent sre){
		if (sessionService == null){
			ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(
			sre.getServletContext());
			sessionService = (SessionService) ctx.getBean("sessionService");
		}
		return sessionService;
	}
	*/

	//note: this method is called before filter chains are applies to the request, which is why the 
	// currentUser can not be set here; it must be set after the authorization filters have run and
	// an authorized user has been set
	public void requestInitialized(ServletRequestEvent sre) {
		// set the original request URL as a request attribute, because once the controller forwards, this
		// is lost when the Acegi filter is turned on and javax.servlet.forward.request_uri incorrectly
		// returns the jsp instead. the original request URL is needed in some cases by the views to 
		// properly construct links.
		// note: when using web flow, the <form> action attribute is not set so that same request URL 
		// is used throughout the flow (with the _flowExecutionKey request parameter controlling the flow). 
		// so this requestUrl is not needed for the <form> action attribute anymore, with one exception: 
		// for component events (eventLink and eventButton tags), want to reposition the page to the 
		// component after the event request if the component is a secondary component, i.e. not at the top 
		// of the page. this is done by specifying #${component} on the <form> action URL, so in this case, 
		// the <form> action URL must be specified, and therefore this requestUrl request attribute is used 
		// to generate it, with the #${component} appended
		// also, the requetUrl is used in generating quicklinks, because the quicklink must exactly match
		// the full request URL to work within the browser, i.e. without sending a request 
		StringBuffer sb = new StringBuffer(sre.getServletRequest().getScheme()).append("://").append(sre.getServletRequest().getServerName())
			.append(":").append(sre.getServletRequest().getServerPort()).append(((HttpServletRequest) sre.getServletRequest()).getRequestURI());
		String queryString = null;
		if ((queryString = ((HttpServletRequest) sre.getServletRequest()).getQueryString()) != null) {
			sb.append("?").append(queryString);
		}
		sre.getServletRequest().setAttribute("requestUrl", sb.toString());
	}
}
