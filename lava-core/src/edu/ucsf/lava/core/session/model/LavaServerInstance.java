package edu.ucsf.lava.core.session.model;

import java.util.Date;

import edu.ucsf.lava.core.manager.AppInfo;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


public class LavaServerInstance extends EntityBase {

	public static EntityManager MANAGER = new EntityBase.Manager(LavaServerInstance.class);
	public static String SERVER_UNINITIALIZED = "UNINITIALIZED";
		
	private String serverDescription;
	private Date createTime;
	private Date disconnectTime;
	private Long disconnectWarningMinutes;
	private String disconnectMessage;
	
	
	public LavaServerInstance() {
		super();
		this.setAudited(false);
		createTime = new Date();
		this.setServerDescription(SERVER_UNINITIALIZED);
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getServerDescription() {
		return serverDescription;
	}
	public void setServerDescription(String serverDescription) {
		this.serverDescription = serverDescription;
	}
	public void setServerDescription(AppInfo appInfo) {
		StringBuffer desc = new StringBuffer("[SERVER-ENV:").append(appInfo.getServerInfo())
			.append("][HOST:").append(appInfo.getServerHostName()).append("][ADDR:").append(appInfo.getServerAddr())
			.append("][PORT:").append(appInfo.getServerPort()).append("][SERVLET-PATH:").append(appInfo.getServletPath())
			.append("][INSTANCE:").append(appInfo.getInstanceName()).append("][VERSION:").append(appInfo.getVersion())
			.append("][DB:").append(appInfo.getDatabaseName()).append("]");
		
		this.serverDescription = desc.toString();
	}
	public String toString(){
		StringBuffer buffer = new StringBuffer(super.toString()).append("{");
		if (id != null){buffer.append(" serverInstanceId=").append(id.toString());}
		if (serverDescription != null){buffer.append(" serverDescription=").append(serverDescription);}
		if (createTime != null){buffer.append(" createTime=").append(createTime.toString());}
		buffer.append("}");
		return buffer.toString();
	
	}
	


	public String getDisconnectMessage() {
		return disconnectMessage;
	}
	public void setDisconnectMessage(String disconnectMessage) {
		this.disconnectMessage = disconnectMessage;
	}
	public Long getDisconnectWarningMinutes() {
		return disconnectWarningMinutes;
	}
	public void setDisconnectWarningMinutes(Long disconnectMinutes) {
		this.disconnectWarningMinutes = disconnectMinutes;
	}
	public Date getDisconnectTime() {
		return disconnectTime;
	}
	public void setDisconnectTime(Date disconnectTime) {
		this.disconnectTime = disconnectTime;
	}
	

	
}
