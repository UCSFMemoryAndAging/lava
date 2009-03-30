package edu.ucsf.lava.core.session;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LavaSessionHttpRequestWrapper extends HttpServletRequestWrapper {
		
	  private String lavaSessionMonitoringMessage;
	  	protected final Log logger = LogFactory.getLog(getClass());
	  	public static final String LAVASESSION_MONITORING_MESSAGE_PARAMETER = "lavaSessionMonitoringMessage";
	 	public static final String LAVASESSION_EXPIRED_MESSAGE_CODE = "lavaSession.expired.message";
	 	public static final String LAVASESSION_DISCONNECTED_MESSAGE_CODE = "lavaSession.disconnected.message";
	 	public static final String LAVASESSION_PENDING_DISCONNECT_MESSAGE_CODE = "lavaSession.pendingDisconnect.message";

		  
	  public LavaSessionHttpRequestWrapper(HttpServletRequest request) {
			super(request);
	  }

	  public String[] getParameterValues(String parameter) {
		  if(parameter.equals(LAVASESSION_MONITORING_MESSAGE_PARAMETER)){
			  if(this.lavaSessionMonitoringMessage != null){
			  return new String[]{this.lavaSessionMonitoringMessage};
			  }
		  }
		 return super.getParameterValues(parameter);
	  }

	

	@Override
	public Map getParameterMap() {
		Map paramMap = new HashMap();
		paramMap.putAll(super.getParameterMap());
		if(this.lavaSessionMonitoringMessage!= null){
			paramMap.put(LAVASESSION_MONITORING_MESSAGE_PARAMETER,this.lavaSessionMonitoringMessage);

		}
		return paramMap;
	}

	@Override
	public String getParameter(String name) {
		if(name.equals(LAVASESSION_MONITORING_MESSAGE_PARAMETER)){
			  if(this.lavaSessionMonitoringMessage != null){
				  return this.lavaSessionMonitoringMessage;
			  }
		}
		return super.getParameter(name);
	}

	public String getLavaSessionMonitoringMessage() {
		return lavaSessionMonitoringMessage;
	}

	public void setLavaSessionMonitoringMessage(String lavaSessionMonitoringMessage) {
		this.lavaSessionMonitoringMessage = lavaSessionMonitoringMessage;
	}
	
	  
}