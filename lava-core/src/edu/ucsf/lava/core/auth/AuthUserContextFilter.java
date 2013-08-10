package edu.ucsf.lava.core.auth;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.session.model.LavaSession;
public class AuthUserContextFilter implements Filter, ManagersAware {
    protected final Log logger = LogFactory.getLog(getClass());
	

     protected SessionManager sessionManager;
    protected MetadataManager metadataManager; 
    public void init(FilterConfig filterConfig) throws ServletException {
            // TODO Auto-generated method stub
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse response,
                    FilterChain chain) throws IOException, ServletException {
		// set the currentUser if not yet set, unless going to the login page in which case there is no user yet
		HttpServletRequest request = (HttpServletRequest)servletRequest;
    	
    	if (request.getRemoteUser() != null) {
			AuthUser currentUser = CoreSessionUtils.getCurrentUser(sessionManager, request);
			if (currentUser == null) {
				AuthUser user = AuthUser.MANAGER.getByLogin(request.getRemoteUser());
				if(user==null){
					throw new AuthUserNotAuthorizedException(
							metadataManager.getMessage("authUserNotAuthorizedException.message",
									new Object[] {}, Locale.getDefault()));
				}
				if(user.getAccessAgreementDate() == null || user.getAccessAgreementDate().after(new Date())) {
					throw new AuthUserNotAuthorizedException(
							metadataManager.getMessage("authUserNotAuthorizedException.message",
									new Object[] {}, Locale.getDefault()));
				}
				user.setAuthUserContextInit(true);
				user.setEffectiveRoles(user.getUserRoles());
				if(user.getEffectiveRoles()==null || user.getEffectiveRoles().size()==0){
					throw new AuthNoCurrentUserRolesException(
							metadataManager.getMessage("authNoCurrentUserRoles.message",
									new Object[] {}, Locale.getDefault()));
				}
				CoreSessionUtils.setCurrentUser(sessionManager, request, user);
				
				LavaSession session = sessionManager.getLavaSession(request);
				if (session != null){
					session.setCurrentStatus(LavaSession.LAVASESSION_STATUS_ACTIVE);
					session.setUserId(user.getId());
					session.setUsername(user.getShortUserNameRev());
					session.setHostname(request.getRemoteAddr());
					session.setAccessTimestamp(new Timestamp(new Date().getTime()));
					sessionManager.saveLavaSession(session);
				}
				
				
				logger.info("set CurrentUser login=" + user.getLogin() + " id=" + user.getId());
			}
		}
   	
    	//do request (including additional auth user context filters). 
   		chain.doFilter(request,response);
   		
   		//clear the authUserContextInit flag.
   		AuthUser currentUser = CoreSessionUtils.getCurrentUser(sessionManager, request);
		if(currentUser!=null){
			currentUser.setAuthUserContextInit(false);
		}
        return;
    }

    public void destroy() {
            // TODO Auto-generated method stub

    }

	

    

	

	public void updateManagers(Managers managers) {
		this.sessionManager = CoreManagerUtils.getSessionManager(managers);
		this.metadataManager = CoreManagerUtils.getMetadataManager(managers);
	}

	


}
