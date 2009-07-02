package edu.ucsf.lava.core.manager;

import java.io.Serializable;

public class AppInfo implements Serializable {

	public static String APP_INFO_ATTRIBUTE = "appInfo";
	public static String UNKNOWN_VALUE = "{unknown}";
    private String version;
    private String databaseName;
    private String instanceName;
    private String serverInfo;
    private String serverHostName;
    private String serverAddr;
    private String serverPort;
    private String servletPath;
    
    
    public AppInfo() {}

    public String getVersion() {
	return this.version;
    }

    public void setVersion(String version) {
        if(version == null){
        	this.version = UNKNOWN_VALUE;
        }else{
        	this.version = version;
        }
    }

    public String getDatabaseName() {
	return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
    	 if(databaseName == null){
         	this.databaseName = UNKNOWN_VALUE;
         }else{
        	 this.databaseName = databaseName;
         }
    }
    
	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		 if(instanceName == null){
	         	this.instanceName = UNKNOWN_VALUE;
	     }else{
	    	 this.instanceName = instanceName;
	     }
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		 if(serverAddr == null){
	         	this.serverAddr = UNKNOWN_VALUE;
	     }else{
	    	 this.serverAddr = serverAddr;
	     }
	}
	
	public String getServletPath() {
		return servletPath;
	}

	public void setServletPath(String servletPath) {
		 if(servletPath == null){
	         	this.servletPath = UNKNOWN_VALUE;
		 }else{
			 this.servletPath = servletPath;
		 }
	}

	public String getServerHostName() {
		return serverHostName;
	}

	public void setServerHostName(String serverHostName) {
		 if(serverHostName == null){
	         	this.serverHostName = UNKNOWN_VALUE;
	     }else{
	    	 this.serverHostName = serverHostName;
	     }
	}

	public String getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(String serverInfo) {
		 if(serverInfo == null){
	         	this.serverInfo = UNKNOWN_VALUE;
	     }else{
	    	 this.serverInfo = serverInfo;
	     }
	}

	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		 if(serverPort == null){
	         	this.serverPort = UNKNOWN_VALUE;
	     }else{
	    	 this.serverPort = serverPort;
	     }
	}

    
    
}    
