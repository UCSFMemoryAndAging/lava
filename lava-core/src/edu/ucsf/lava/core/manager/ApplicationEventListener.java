package edu.ucsf.lava.core.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.mx.util.MBeanServerLocator;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.auth.AuthManager;
import edu.ucsf.lava.core.dao.LavaDao;
import edu.ucsf.lava.core.list.ListManager;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.session.model.LavaServerInstance;
import edu.ucsf.lava.core.spring.LavaBeanUtils;
import edu.ucsf.lava.core.webflow.WebflowManager;

public class ApplicationEventListener implements ServletContextListener, ManagersAware {
	
	protected ListManager listManager;
	protected SessionManager sessionManager ;
	protected AuthManager authManager;
	protected ActionManager actionManager;
	protected MetadataManager metadataManager;
	protected WebflowManager webflowManager;
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	public ApplicationEventListener() {
    }

	
    public void contextInitialized(ServletContextEvent sce) {
    	this.updateManagers((Managers)LavaBeanUtils.get("managers"));
		ServletContext servletContext = sce.getServletContext();
		servletContext.log("loading webapp, in ApplicationEventListener.contextInitalized");
	
		logger.info("Initializing Metadata Manager");
		metadataManager.reloadViewProperties();
		logger.info("Initializing Action Manager");
		actionManager.reloadActionDefinitions();
		logger.info("Initializing Flow Definitions");
		webflowManager.initializeFlows();
		
		logger.info("Initializing List Manager");
		listManager.initStaticLists();

		logger.info("Initializing Authorization Manager");
		authManager.initialize();
		
	
		
	
		
    }
	
	
	public void contextDestroyed(ServletContextEvent sce) {
		// nothing to do
	}


	public void updateManagers(Managers managers) {
		
		listManager = CoreManagerUtils.getListManager(managers);
		 sessionManager = CoreManagerUtils.getSessionManager(managers);
		 authManager = CoreManagerUtils.getAuthManager(managers);
		 actionManager = CoreManagerUtils.getActionManager(managers);
		 metadataManager = CoreManagerUtils.getMetadataManager(managers);
		 webflowManager = CoreManagerUtils.getWebflowManager(managers);
		
		
	}
	
	
	
}
