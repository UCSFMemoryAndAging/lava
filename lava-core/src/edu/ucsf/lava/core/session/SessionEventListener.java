package edu.ucsf.lava.core.session;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.session.model.LavaSession;

public class SessionEventListener implements HttpSessionListener {

	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	protected SessionManager sessionManager; 
	
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		HttpSession httpSession = sessionEvent.getSession();
		if(httpSession == null){
			logger.info("session created. id=null");
			return;
		}
		
		LavaSession lavaSession = new LavaSession();
		lavaSession.setServerInstanceId(getSessionManager().getLavaServerInstance().getId());
		lavaSession.setHttpSessionId(httpSession.getId());
		lavaSession.save();
		
		logger.info("session created. id=" + lavaSession.toString());
		
		
	
	}

	public void sessionDestroyed(HttpSessionEvent sessionEvent) {

		HttpSession httpSession = sessionEvent.getSession();
		if (httpSession == null){
			logger.info("session destroyed: null session");
			return;
		}
	
		LavaSession session = getSessionManager().getLavaSession(httpSession);
		if(session == null){
			logger.info("session destroyed: null session");
			return;
		}
			
		String status = session.getCurrentStatus();
		//if new or active then this session was expired
		//by the web server and not by the session monitor
		if (status.equals(LavaSession.LAVASESSION_STATUS_ACTIVE) || 
			status.equals(LavaSession.LAVASESSION_STATUS_NEW)){
			sessionManager.doSessionExpire(session, httpSession);
			}
		
		logger.info("session destroyed: " + session.toString());
	}
	

	

	public SessionManager getSessionManager(){
		if (sessionManager == null){
			sessionManager = CoreManagerUtils.getSessionManager();
		}
		return sessionManager;
	}

	
	
}
