package edu.ucsf.lava.crms.auth;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.home.model.PreferenceUtils;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.crms.auth.model.CrmsAuthUser;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class CrmsAuthUserContextFilter implements Filter, ManagersAware {
	protected final Log logger = LogFactory.getLog(getClass());
	protected SessionManager sessionManager;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		
		CrmsAuthUser user = CrmsSessionUtils.getCrmsCurrentUser(sessionManager, httpServletRequest);
		
		if(user!=null && user.isAuthUserContextInit()) {
			List<String> userProjectAccess = user.getProjectAccessList();
			String newProjectContext = "";
			boolean newProjectContextSpecified = false;
			
			// initialize CrmsAuthUser context if haven't done already
			if(user!=null && !user.isCrmsAuthUserContextInit()) {
				// if startupProject preference found, then set projectChangeTo to this
				if(userProjectAccess.size() > 0) {
					String startupProject = PreferenceUtils.getPrefValue(user, "crmsAuthUser", "startupProject", null);
					// note: we allow the user to ensure their project context is always empty (""), even if they only have one project
					if (startupProject!=null && (startupProject.equals("") || userProjectAccess.contains(startupProject))) {
						newProjectContext = startupProject;
						newProjectContextSpecified = true;
					}
				}
				user.setCrmsAuthUserContextInit(true);
			}
			
			if(newProjectContextSpecified==false && userProjectAccess.size() == 1 && ! user.getAllProjectAccess() && !userProjectAccess.get(0).equals(CrmsAuthUser.PROJECT_LIST_PLACEHOLDER)) {
				newProjectContext = userProjectAccess.get(0);
				newProjectContextSpecified = true;
			}
			
			if (newProjectContextSpecified) {
				CrmsSessionUtils.setCurrentProject(sessionManager,httpServletRequest, newProjectContext);
				logger.info("set CurrentProject=" + newProjectContext);
			}
		}
		chain.doFilter(request, response);
		return;
	}

	public void init(FilterConfig filterConfig) throws ServletException {}
    public void destroy() {}

	
  	public void updateManagers(Managers managers) {
		this.sessionManager = CoreManagerUtils.getSessionManager(managers);
	}

	
	
}