package edu.ucsf.lava.core.environment;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.mx.util.MBeanServerLocator;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.manager.AppInfo;
import edu.ucsf.lava.core.session.SessionManager;

public class JBossDelegate implements ApplicationServerDelegate {

	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());


	
	/**
	 * Get the instance name of the running web application.  Initially tried to 
	 * call the servet spec to get the servet context, but we need this before 
	 * the first request is issued, and on the version of JBoss we were using this
	 * returned a "" without a servlet context.   So, Charlie wrote this routine to 
	 * determine this by examining the path to the web.xml file. 
	 */
	public String getInstanceName(){
		     // in exploded WAR deployment (i.e. development) the path will be something like:
			//   /home/ctoohey/project/webapp/mac.war/WEB-INF/web.xml, in a regular WAR deployment, 
			//  JBoss explodes the WAR into a temporary directory 
			// (../server/SERVER_NAME/tmp/...) and the path will be something like:
			//   /usr/local/jboss-4.0.2/server/dev-ctoohey/tmp/deploy/tmp52223mac.war/WEB-INF/web.xml
			//   where the webapp name is always preceded by "tmpNNNNN.." where N is a digit
			// in either example, the goal is to parse out "mac" as the webapp instance name
	String instanceName = new String();
  
	ResourceLoader resourceLoader = new DefaultResourceLoader();
  	Resource resource = resourceLoader.getResource("WEB-INF/web.xml");
	Log logger = LogFactory.getLog(ActionManager.class);
       try {
	        //logger.info("abolutePath=" + resource.getFile().getAbsolutePath());
	        //logger.info("canonicalPath=" + resource.getFile().getCanonicalPath());
	        logger.info("WEB-INF/web.xml path=" + resource.getFile().getPath());
	        String path = resource.getFile().getPath();
	        
	        // parse out the  instance name from the path
	                
	        int endIndex = path.indexOf(".war/WEB-INF");
	        if (endIndex == -1){
		    // on Windows platform look forith path separator of '\'
		    endIndex = path.indexOf(".war\\WEB-INF");
	        }

	        // 	starting from endIndex, look backwards for the beginning index

	        // first look for path separator on Unix platforms, '/'
	        int beginIndex = path.lastIndexOf("/", endIndex);
	        // if on Windows platform, look for '\' instead
	        if (beginIndex == -1) {
	        	beginIndex = path.lastIndexOf("\\", endIndex);
	        }

	        // 	now determine whether this is an exploded WAR deployment or a WAR 
	        // file deployment
	        int tmpIndex = path.indexOf("tmp", beginIndex);
	        if (tmpIndex == -1) {
	        // if not a tmp dir, this is an exploded WAR deployment
	        	instanceName = path.substring(beginIndex+1, endIndex); 
	        }
	        else {
		    // this is a WAR file deployment exploded into a tmp directory where
		    // the webapp directory is "tmpNNNN...WEBAPPNAME.war"
		    // tmpIndex is position at 't', and endIndex is positioned at '.'
	        	String tempStr = path.substring(tmpIndex+3, endIndex); 
	        	// tempStr = NNNN...WEBAPPNAME
	        	// search for the first character after the digits
	        	int i=0;
	        	for (i=0; i<tempStr.length(); i++) {
	        		if (tempStr.charAt(i) < '0' || tempStr.charAt(i) > '9') {
	        			break;
	        		}
	        	}
	        	instanceName = tempStr.substring(i);
	        	// 	the exploded tmp dir may also have "-exp" appended to the name, e.g.
	        	// "tmpNNNN...WEBAPPNAME-exp.war", so look for "-exp" and remove if present
	        	tmpIndex = instanceName.indexOf("-exp");
	        	if (tmpIndex != -1) {
	        		instanceName = instanceName.substring(0,tmpIndex);
	        	}
	        }
	        
	        /*
	         * now strip anything after an "_" from the name.  This allows running multiple 
	         * copies of the same instance named app in the same tomcat server, e.g.
	         *  'instancename' and 'instancename_dev' would both load with the 'dev' instance name
	         */
	        int underscoreIndex = instanceName.indexOf("_");
	        if(underscoreIndex != -1){
	        	instanceName = instanceName.substring(0, underscoreIndex);
	        }
	        
	        
       }
       catch (IOException ex) {
      	logger.error("resourceLoader exception trying to find context path", ex);
      }
      logger.info("instanceName=<" + instanceName + ">");
      return instanceName;
	}
	
	
	

}
