package edu.ucsf.lava.core.session;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.manager.AppInfo;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.scope.ScopeManager;
import edu.ucsf.lava.core.session.model.LavaServerInstance;
import edu.ucsf.lava.core.session.model.LavaSession;


public class SessionManager extends LavaManager{
	 
		public static String SESSION_MANAGER_NAME = "sessionManager";
		protected final Log logger = LogFactory.getLog(getClass());
		private List<LavaSessionPolicyHandler> policyHandlers; //injected
		protected LavaServerInstance serverInstance;
		protected MetadataManager metadataManager;
		protected ScopeManager scopeManager;
		protected LavaServerInstanceDelegate lavaServerInstanceDelegate;  //abstracts servlet container specific implementation details
		
		
		public SessionManager(){
			super(SESSION_MANAGER_NAME);
		}
		
		
		
		public void updateManagers(Managers managers) {
			super.updateManagers(managers);
			metadataManager = CoreManagerUtils.getMetadataManager(managers);
			scopeManager = CoreManagerUtils.getScopeManager(managers);
		}

		
		
		// Session Attribute Methods
		public Map getContextFromSession(HttpServletRequest request){
			return scopeManager.getSessionAttributeHandlers().getContextFromSession(request);
		}

		public void setSessionAttribute(HttpServletRequest request,String attribute, Object object){
			scopeManager.getSessionAttributeHandlers().setAttribute(request, attribute, object);
		}
	
	
		public Object getSessionAttribute(HttpServletRequest request,String attribute){
			return scopeManager.getSessionAttributeHandlers().getAttribute(request, attribute);
		}

			
		

			
	
	
		
		
		//LAVA Session Methods
		
		//calling from a request scope (so also initialize user and hostname if not already done
		public LavaSession getLavaSession(HttpServletRequest request) {
			LavaSession session = getLavaSession(request.getSession());
			if(session == null){return null;}
			initializeUserAndHostNames(session, request);
			session.save();
			return session;
		}
		
		
		public LavaSession getLavaSession(HttpSession httpSession) {
			if(httpSession == null){
    			return null;
    			}
    		
    		LavaSession session = LavaSession.MANAGER.getLavaSession(getLavaServerInstance(),httpSession.getId());
			if (session == null){
    			return null;
    			}
			return session;
		}
	
	
		public void removeLavaSessionFromCache(LavaSession session){
			
			session.release(true);
		}
	
		
		protected void initializeUserAndHostNames(LavaSession session, HttpServletRequest request){
			if(session.getHostname() == LavaSession.LAVASESSION_UNINITIALIZED_VALUE){
				session.setHostname(getHostname(request));
			}
    		if(session.getUsername() == LavaSession.LAVASESSION_UNINITIALIZED_VALUE && ((HttpServletRequest)request).getRemoteUser()!= null){
    			session.setUsername(((HttpServletRequest)request).getRemoteUser());
    		}
		}
		
		protected String getHostname(HttpServletRequest request){
			//first check for proxy header
			String host = request.getHeader("X-Forwarded-For");
			if(host==null || host.equals("")){
				host = request.getRemoteAddr();
				if(host==null || host.equals("")){
					host = LavaSession.LAVASESSION_UNINITIALIZED_VALUE;
				}
			}else if(host.contains(",")){
				host = host.substring(0, host.indexOf(",")-1);
			}
			return host;
		}
			
		public boolean hasSessionExpired(LavaSession session,HttpServletRequest request){
			for (LavaSessionPolicyHandler handler: policyHandlers){
				if(handler.handlesSession(session, request)){
					boolean result = handler.hasSessionExpired(session, request);
					session.save();
					return result;
				}
			}
			logger.info("Unhandled session in sessionManager.hasSessionExpired"+ session.toString());
			return false;
			
		}
		public boolean shouldSessionDisconnect(LavaSession session,HttpServletRequest request){
			for (LavaSessionPolicyHandler handler: policyHandlers){
				if(handler.handlesSession(session, request)){
					boolean result = handler.shouldSessionDisconnect(session, request);
					session.save();
					return result;
				}
			}
			logger.info("Unhandled session in sessionManager.shouldSessionDisconnect"+ session.toString());
			return false;
			
		}
		
		public boolean willSessionDisconnectSoon(LavaSession session,HttpServletRequest request){
			for (LavaSessionPolicyHandler handler: policyHandlers){
				if(handler.handlesSession(session, request)){
					//the should session disconnect needs to be called to ensure that all 
					//disconnect handlers are checked.  In practice, this will likely 
					//already have been called before this method...but this is to make sure. 
					handler.shouldSessionDisconnect(session, request);
					boolean result = handler.isDisconnectTimeWithinWarningWindow(session, request);
					session.save();
					return result;
				}
			}
			logger.info("Unhandled session in sessionManager.willSessionDisconnectSoon"+ session.toString());
			return false;
		}
		
		
		public void doSessionExpire(LavaSession session,HttpSession httpSession){
			if(!session.isExpireTimeBeforeNow()){
				session.setExpireTime(new Date());
			}
			session.setCurrentStatus(LavaSession.LAVASESSION_STATUS_EXPIRED);
			session.save();
			invalidateHttpSession(httpSession);
			
		}
		
		public void doSessionLogoff(LavaSession session,HttpSession httpSession){
				if(session.getCurrentStatus().equals(LavaSession.LAVASESSION_STATUS_ACTIVE)){
					session.setDisconnectTime(new Date());
					session.setCurrentStatus(LavaSession.LAVASESSION_STATUS_LOGOFF);
					session.save();
				}
				invalidateHttpSession(httpSession);
			}

		
		public void doSessionDisconnect(LavaSession session,HttpSession httpSession){
			if(!session.isDisconnectTimeBeforeNow()){
				session.setDisconnectTime(new Date());
			}
			session.setCurrentStatus(LavaSession.LAVASESSION_STATUS_DISCONNECTED);
			session.save();
			invalidateHttpSession(httpSession);
			
		}
		
	
		
		public void setSessionAccessTimeToNow(LavaSession session,HttpServletRequest request){
			session.setAccessTime(new Date());
			session.save();
		}
		
		public void updateSessionExpiration(LavaSession session, HttpServletRequest request){
			for (LavaSessionPolicyHandler handler: policyHandlers){
				if(handler.handlesSession(session, request)){
					session.setExpireTime(handler.determineExpireTime(session, request));
					session.save();
					updateHttpSessionExpiration(session,request);
					return;
				}
			}
			logger.info("Unhandled session in sessionManager.updateSessionExpiration"+ session.toString());
		}

		
		protected void invalidateHttpSession(HttpSession httpSession){
			if(httpSession != null){httpSession.invalidate();}
		}
		
		protected void updateHttpSessionExpiration(LavaSession session,HttpServletRequest request){
			HttpSession httpSession = request.getSession();
			if(httpSession == null){return;}
			httpSession.setMaxInactiveInterval(-1);
			/*for (LavaSessionPolicyHandler handler: policyHandlers){
				if(handler.handlesSession(session, request)){
					httpSession.setMaxInactiveInterval(handler.getSecondsUntilExpiration(session, request));
					return;
				}
			}
			logger.info("Unhandled session in sessionManager.updateHttpSessionExpiration"+ session.toString());
			*/
		}
		
		public LavaSessionHttpRequestWrapper setExpirationMessage(LavaSession session,HttpServletRequest request){
			LavaSessionHttpRequestWrapper requestWrapper = new LavaSessionHttpRequestWrapper(request);
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, zzz");
			requestWrapper.setLavaSessionMonitoringMessage(
					metadataManager.getMessage(LavaSessionHttpRequestWrapper.LAVASESSION_EXPIRED_MESSAGE_CODE,
							new Object[]{dateFormat.format(session.getDisconnectTime()),
							session.getDisconnectMessage()},Locale.getDefault()));
			return requestWrapper;	
		
		}
		public LavaSessionHttpRequestWrapper setDisconnectMessage(LavaSession session,HttpServletRequest request){
			LavaSessionHttpRequestWrapper requestWrapper = new LavaSessionHttpRequestWrapper(request);
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, zzz");
			requestWrapper.setLavaSessionMonitoringMessage(
					metadataManager.getMessage(LavaSessionHttpRequestWrapper.LAVASESSION_DISCONNECTED_MESSAGE_CODE,
							new Object[]{dateFormat.format(session.getDisconnectTime()),
							session.getDisconnectMessage()},Locale.getDefault()));
			return requestWrapper;
		
		}
		public LavaSessionHttpRequestWrapper setPendingDisconnectMessage(LavaSession session,HttpServletRequest request){
			LavaSessionHttpRequestWrapper requestWrapper = new LavaSessionHttpRequestWrapper(request);
			SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a, zzz");
			requestWrapper.setLavaSessionMonitoringMessage(
					metadataManager.getMessage(LavaSessionHttpRequestWrapper.LAVASESSION_PENDING_DISCONNECT_MESSAGE_CODE,
							new Object[]{dateFormat.format(session.getDisconnectTime()),
							session.getDisconnectMessage()},Locale.getDefault()));
			return requestWrapper;	
		
		}
		
	

		
		public LavaServerInstance createLavaServerInstance(AppInfo appInfo){
			serverInstance = (LavaServerInstance)LavaServerInstance.MANAGER.create();
			if(lavaServerInstanceDelegate==null || appInfo==null){
				serverInstance.setServerDescription("No Description for Server Instance");
			}else{
				serverInstance.setServerDescription(lavaServerInstanceDelegate.getServerDescription(appInfo));
			}
			serverInstance.save();
			serverInstance.refresh();
			return serverInstance;
		}
		
		public LavaServerInstance getLavaServerInstance(){
			if(serverInstance != null){
				return serverInstance;
			}
			return createLavaServerInstance(null);
			
		}

		public List<LavaSessionPolicyHandler> getPolicyHandlers() {
			return policyHandlers;
		}

		public void setPolicyHandlers(List<LavaSessionPolicyHandler> policyHandlers) {
			this.policyHandlers = policyHandlers;
			for (LavaSessionPolicyHandler handler: this.policyHandlers){
				handler.setSessionMonitor(this);
			}
		}



		public LavaServerInstanceDelegate getLavaServerInstanceDelegate() {
			return lavaServerInstanceDelegate;
		}



		public void setLavaServerInstanceDelegate(
				LavaServerInstanceDelegate lavaServerInstanceDelegate) {
			this.lavaServerInstanceDelegate = lavaServerInstanceDelegate;
		}

	

		
		
		

}