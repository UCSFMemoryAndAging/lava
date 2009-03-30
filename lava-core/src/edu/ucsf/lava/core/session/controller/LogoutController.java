package edu.ucsf.lava.core.session.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.session.model.LavaSession;

public class LogoutController extends AbstractController {
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	

	public ModelAndView handleRequestInternal(HttpServletRequest request,
										      HttpServletResponse response) throws Exception {
		
		SessionManager sessionManager = CoreManagerUtils.getSessionManager();
		
		LavaSession session = sessionManager.getLavaSession(request);
		if(session != null){
			sessionManager.doSessionLogoff(session, request.getSession());
			logger.info("session logoff: " + session.toString());
		}
		return new ModelAndView("redirect:" + request.getScheme() + "://" + request.getHeader("host") + request.getContextPath() + "/security/local" + request.getContextPath() + "/logout.jsp");
	}



	

	  
}
