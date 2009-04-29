package edu.ucsf.lava.core.session.model;


import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.type.LavaDateUtils;

public class LavaSession extends EntityBase {

	
	public static LavaSession.Manager MANAGER = new LavaSession.Manager();
	
	public static final String LAVASESSION_STATUS_ACTIVE="ACTIVE";
	public static final String LAVASESSION_STATUS_LOGOFF="LOGOFF";
	public static final String LAVASESSION_STATUS_EXPIRED="EXPIRED";
	public static final String LAVASESSION_STATUS_DISCONNECTED="DISCONNECTED";
	public static final String LAVASESSION_UNINITIALIZED_VALUE="UNINITIALIZED";
	
		

	private Long serverInstanceId;
	private Timestamp createTimestamp;
	private Timestamp accessTimestamp;
	private Timestamp expireTimestamp;
	private String currentStatus;
	private Long userId;
	private String username;
	private String hostname;
	private String httpSessionId;
	private Date disconnectDate;
	private Time disconnectTime;
	private String disconnectMessage;
	private String notes;
	private int hibernateVersion;
	public static final String LAVASESSION_STATUS_NEW="NEW";
	
	public LavaSession(){
		super();
		this.setAudited(false);
		this.createTimestamp = new Timestamp(new Date().getTime());
		this.accessTimestamp = this.createTimestamp;
		this.username = LAVASESSION_UNINITIALIZED_VALUE;
		this.hostname = LAVASESSION_UNINITIALIZED_VALUE;
		this.currentStatus = LavaSession.LAVASESSION_STATUS_NEW;
	}
	
	
	
	
	public boolean getPatientAuth() {
		return false;
	}



	public String getSessionDesc(){
		StringBuffer block = new StringBuffer();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		String createdDateAsText = null;
		if (createTimestamp != null) {
			createdDateAsText = dateFormat.format(createTimestamp);
		}
		
		block.append(StringUtils.defaultString(username,"[user]")).append(" - ");
		block.append(StringUtils.defaultString(hostname,"[host]")).append(" - ");
		block.append(StringUtils.defaultString(createdDateAsText,"[time]")).append(" - ");
		block.append(StringUtils.defaultString(currentStatus,"[status]"));
		
		return new String(block);
	}
	
	
	


	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	

	public String getCurrentStatus() {
		return currentStatus;
	}


	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}


	


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	

	


	public Timestamp getAccessTimestamp() {
		return accessTimestamp;
	}




	public void setAccessTimestamp(Timestamp accessTime) {
		this.accessTimestamp = accessTime;
	}




	public Timestamp getCreateTimestamp() {
		return createTimestamp;
	}




	public void setCreateTimestamp(Timestamp createTime) {
		this.createTimestamp = createTime;
	}




	public Date getDisconnectDate() {
		return disconnectDate;
	}




	public void setDisconnectDate(Date disconnectDate) {
		this.disconnectDate = disconnectDate;
	}




	public String getDisconnectMessage() {
		return disconnectMessage;
	}




	public void setDisconnectMessage(String disconnectMessage) {
		this.disconnectMessage = disconnectMessage;
	}




	public Time getDisconnectTime() {
		return disconnectTime;
	}




	public void setDisconnectTime(Time disconnectTime) {
		this.disconnectTime = disconnectTime;
	}
	
	public void setDisconnectDateTime(Date disconnect){
		setDisconnectDate(LavaDateUtils.getDatePart(disconnect));
		setDisconnectTime(LavaDateUtils.getTimePart(disconnect));
	}
	
	public Date getDisconnectDateTime(){
		return LavaDateUtils.getDateTime(getDisconnectDate(), getDisconnectTime());
	}



	public Timestamp getExpireTimestamp() {
		return expireTimestamp;
	}




	public void setExpireTimestamp(Timestamp expireTime) {
		this.expireTimestamp = expireTime;
	}




	public String toString(){
		StringBuffer buffer = new StringBuffer(super.toString()).append("{");
		if (id != null){buffer.append(" lavaSessionId=").append(id.toString());}
		if (httpSessionId != null){buffer.append(" httpSessionId=").append(httpSessionId);}
		if (userId != null){buffer.append(" userId=").append(userId.toString());}
		if (currentStatus != null){buffer.append(" status=").append(currentStatus);}
		if (username != null){buffer.append(" username=").append(username);}
		if (hostname != null){buffer.append(" hostname=").append(hostname);}
		buffer.append("}");
		return buffer.toString();
	
	}

	public String getHttpSessionId() {
		return httpSessionId;
	}

	public void setHttpSessionId(String httpSessionId) {
		this.httpSessionId = httpSessionId;
	}
	
	



	public Long getServerInstanceId() {
		return serverInstanceId;
	}




	public void setServerInstanceId(Long serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}


	public boolean isDisconnectTimeBefore(Date time){
		if (disconnectTime == null || disconnectDate == null || LavaDateUtils.getDateTime(disconnectDate,disconnectTime).after(time)){
			return false;
		}
		return true;
	}
	
	public boolean isDisconnectTimeBeforeNow(){
		return isDisconnectTimeBefore(new Date());
	}

	public boolean isExpireTimeBefore(Date time){
		if (expireTimestamp == null || expireTimestamp.after(time)){
			return false;
		}
		return true;
	}
	
	public boolean isExpireTimeBeforeNow(){
		return isExpireTimeBefore(new Date());
	}




	public int getHibernateVersion() {
		return hibernateVersion;
	}




	public void setHibernateVersion(int hibernateVersion) {
		this.hibernateVersion = hibernateVersion;
	}

	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(LavaSession.class);
		}
			
		
		
		//Lava Sessions with active status.
		public  List getCurrent(LavaServerInstance serverInstance,LavaDaoFilter filter) {
			filter.addDaoParam(filter.daoEqualityParam("serverInstanceId", serverInstance.getId()));
			filter.addDaoParam(filter.daoInParam("currentStatus", new Object[]{LAVASESSION_STATUS_ACTIVE,LavaSession.LAVASESSION_STATUS_NEW}));
			return get(LavaSession.class,filter);
		}
		
		/*
		 * Return the most recently created lava session for the http Session id
		 * The browser will keep the same http session id until it is closed, even 
		 * across lava sessions. 
		 */ 
		public  LavaSession getLavaSession(LavaServerInstance serverInstance,String httpSessionId) {
			LavaDaoFilter filter = newFilterInstance(null);
			filter.addDaoParam(filter.daoEqualityParam("serverInstanceId", serverInstance.getId()));
			filter.addDaoParam(filter.daoEqualityParam("httpSessionId",httpSessionId));
			filter.addSort("createTimestamp", false);
			List results = get(LavaSession.class,filter);
			if (results.size() >= 1){
				return (LavaSession)results.get(0);
			}
			return null;
		}
		
		public  List getForServerInstance(LavaServerInstance serverInstance,LavaDaoFilter filter) {
			filter.addDaoParam(filter.daoEqualityParam("serverInstanceId", serverInstance.getId()));
			return get(LavaSession.class,filter);
		}
	}
	
}
