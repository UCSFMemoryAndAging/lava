package edu.ucsf.lava.core.environment;

import java.io.IOException;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import edu.ucsf.lava.core.action.ActionManager;

public class Tomcat6Delegate implements ApplicationServerDelegate {


	/**
	 * Get the instance name of the running web application.  One way to do this is to 
	 * get a resource and check the path and then see what the .war file or directory is named.
	 * 
	 */
	public String getInstanceName(){
		 
	String instanceName = new String();
  
	ResourceLoader resourceLoader = new DefaultResourceLoader();
	//base path for resource on tomcat is WEB-INF\classes
  	Resource resource = resourceLoader.getResource("reportStyles.jrtx");
	Log logger = LogFactory.getLog(ActionManager.class);
       try {
	        String path = resource.getFile().getPath();
	        logger.debug("reportStyles.jrtx path=" + path);
	        
	        // parse out the  instance name from the path
	        int endIndex = path.indexOf("/WEB-INF");
	     
	        if (endIndex == -1){
	        	// on Windows platform look for the path separator of '\'
	        	endIndex = path.indexOf("\\WEB-INF");
	        }

	        
	        // first look for path separator on Unix platforms, '/'
	        int beginIndex = path.lastIndexOf("/", endIndex-1);
	        // if on Windows platform, look for '\' instead
	        if (beginIndex == -1) {
	        	beginIndex = path.lastIndexOf("\\", endIndex-1);
	        }
	        
	        //now strip  .war from the name
	        int warIndex = path.indexOf(".war", beginIndex);
	        if (warIndex == -1) {
	        	instanceName = path.substring(beginIndex+1,endIndex);
	        }else{

	        	instanceName = path.substring(beginIndex+1,warIndex);
	        }
	       
       }
       catch (IOException ex) {
      	logger.error("resourceLoader exception trying to find context path", ex);
      }
      logger.info("instanceName=<" + instanceName + ">");
      return instanceName;
	}

}
