package edu.ucsf.lava.core.session;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.dao.LavaDao;
import edu.ucsf.lava.core.environment.EnvironmentManager;
import edu.ucsf.lava.core.manager.AppInfo;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.session.model.LavaServerInstance;
import edu.ucsf.lava.core.session.model.LavaSession;
import edu.ucsf.lava.core.spring.LavaBeanUtils;


public class SessionMonitoringFilter implements Filter, ManagersAware {
    protected final Log logger = LogFactory.getLog(getClass());
   
    protected SessionManager sessionManager;
    protected EnvironmentManager environmentManager;
   
	   
    
    
    public void init(FilterConfig filterConfig) throws ServletException {
      }

    public void doFilter(ServletRequest request, ServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
    		
    		
    	
    		HttpServletRequest httpRequest = (HttpServletRequest)request;
    		HttpSession httpSession = httpRequest.getSession();
    		if(httpSession == null){
    			logger.debug("SessionMonitoringFilter: No HttpSession");
    			this.chainDoFilter(request, response, chain);
    			return;
    			}
    		
    		ServletContext servletContext = httpSession.getServletContext();
    		//Initialize LavaServerInstance and AppInfo if not configured yet. 
    		// create an instance of AppInfo to store application-wide information at application scope
    		AppInfo appInfo = (AppInfo)servletContext.getAttribute(AppInfo.APP_INFO_ATTRIBUTE);
    		if(appInfo == null){
    			appInfo = new AppInfo();
    			//this if the first request, so setup the appInfo
    			appInfo.setVersion(servletContext.getInitParameter("webappVersion"));
    			
    			// get the name of the database that we are connected to for informational purposes
    			LavaDao lavaDao = (LavaDao) LavaBeanUtils.get("lavaDao");
    			String dbName = lavaDao.getDatabaseName();
    			if (dbName != null) {
    				appInfo.setDatabaseName(dbName);
    			}
    			
    			appInfo.setServerHostName(request.getServerName());
    			Integer port = request.getLocalPort();
    			if(port!=null){
    				appInfo.setServerPort(port.toString());
    			}
    			appInfo.setServerAddr(request.getLocalAddr());
    			appInfo.setServletPath(httpRequest.getContextPath());
    		
    			appInfo.setInstanceName(environmentManager.getInstanceName());
    			appInfo.setServerInfo(servletContext.getServerInfo());
    			
    			servletContext.setAttribute(AppInfo.APP_INFO_ATTRIBUTE, appInfo);
    		}
    		
    		LavaServerInstance lsi = sessionManager.getLavaServerInstance();
    		//first time through if not initialized
    		if(lsi.getServerDescription().equals(LavaServerInstance.SERVER_UNINITIALIZED)){
    			lsi.setServerDescription(appInfo);
    			//do not persist LavaServerInstance for now
    			//lsi.save();
    		}
    		
    		
    		
    		LavaSession session = sessionManager.getLavaSession(httpRequest);
			//LavaSession is created in the session listener, so if we don't have one, then nothing really to do here.   
    		if (session == null){
    			logger.debug("SessionMonitoringFilter: No LavaSession");
    			this.chainDoFilter(request, response, chain);
    			return;
    			}
			
    		logger.debug("SessionMonitoringFilter: LavaSession="+session.toString());
    		
    		if(sessionManager.hasSessionExpired(session,httpRequest)){
    			sessionManager.doSessionExpire(session,httpRequest.getSession());
    			logger.debug("SessionMonitoringFilter: Expired LavaSession="+session.toString());
    			// for some reason, if setting attribute on request, it is not there in login.jsp after
    			// Acegi redirects there, so setting message as a session attribute
    			httpRequest.getSession().setAttribute(LavaSessionHttpRequestWrapper.LAVASESSION_TERMINATION_MESSAGE_ATTRIBUTE, sessionManager.getExpirationMessage(session));    			
    			this.chainDoFilter(request, response, chain, session);
    			return;
    		}
    		
    		if(sessionManager.shouldSessionDisconnect(session,httpRequest)){
    	
    			sessionManager.doSessionDisconnect(session,httpRequest.getSession());
    			logger.debug("SessionMonitoringFilter: Disconnected LavaSession="+session.toString());
    			httpRequest.getSession().setAttribute(LavaSessionHttpRequestWrapper.LAVASESSION_TERMINATION_MESSAGE_ATTRIBUTE, sessionManager.getDisconnectMessage(session));    			
    			this.chainDoFilter(request, response, chain, session);
    			return;
    		}
    		
    		//toggle last accessed time and update the session expiration
    		sessionManager.setSessionAccessTimeToNow(session,httpRequest);
    		sessionManager.updateSessionExpiration(session,httpRequest);
    		
    		logger.debug("SessionMonitoringFilter: Active LavaSession="+session.toString());
        	
    		if(sessionManager.willSessionDisconnectSoon(session,httpRequest)){
    			logger.debug("SessionMonitoringFilter: Pending Disconnect LavaSession="+session.toString());
            	
    			// note: could not get messaging to work using an HttpServletRequestWrapper (returned by 
    			// setPendingDisconnectMessage) for sessionExpires and sessionDisconnect above, so used
    			// HttpSession attribute instead. but that may have been because the session terminated and
    			// Acegi was involved, redirecting to the login page. So wrapper may work in this situation,
    			// but if not, try setting message in HttpRequest or HttpSession attribute 
    			this.chainDoFilter(sessionManager.setPendingDisconnectMessage(session,httpRequest),
    					response, chain, session);
    			return;
    		}
        	
    		this.chainDoFilter(request, response, chain, session);
    		
    		return;
   	
    	
    	
    }

    public void destroy() {
            // TODO Auto-generated method stub

    }


	protected void chainDoFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException{
		this.chainDoFilter(request, response, chain, null);
		
	}
    
	protected void chainDoFilter(ServletRequest request, ServletResponse response,
            FilterChain chain, LavaSession session) throws IOException, ServletException{
		if(session != null){
			sessionManager.removeLavaSessionFromCache(session);
		}
		chain.doFilter(request, response);
		return;
		
		
	}

	public void updateManagers(Managers managers) {
		this.sessionManager = CoreManagerUtils.getSessionManager(managers);
		this.environmentManager = CoreManagerUtils.getEnvironmentManager(managers);
	}
	
	
	
	
}
