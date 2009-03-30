package edu.ucsf.lava.core.session;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.mx.util.MBeanServerLocator;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.manager.AppInfo;

public class JBossLavaServerInstanceDelegate implements LavaServerInstanceDelegate {

	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

	public String getServerDescription(AppInfo appInfo) {
		StringBuffer serverDesc = new StringBuffer();
		try{
			
			MBeanServer mbs = MBeanServerLocator.locateJBoss();
		
			ObjectName serverInfo = new ObjectName("jboss.system:type=ServerInfo");
			Object hostAddress = mbs.getAttribute(serverInfo, "HostAddress");
			
			
			ObjectName serverConfig = new ObjectName("jboss.system:type=ServerConfig");
			Object serverName = mbs.getAttribute(serverConfig, "ServerName");

			String war = ActionManager.webappInstanceName;
		
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
			
			serverDesc.append("[host_addr=")
							.append(StringUtils.defaultString(hostAddress.toString()))
							.append("] [j2ee_env=jboss] [server=")
							.append(StringUtils.defaultString(serverName.toString()))
							.append("] [war=")
							.append(StringUtils.defaultString(war))
							.append("] [app_version=")
							.append(appInfo.getVersion())
							.append("] [started=")
							.append(dateFormat.format(new Date())).append("]");
			return serverDesc.toString();
		}
		catch(Exception e){
			logger.error("error creating unique server instance string." + e.toString());
			return new String();
		}
		
	}
	
	public void setSessionManager(SessionManager sessionManager){
		sessionManager.setLavaServerInstanceDelegate(this);
	}

}
