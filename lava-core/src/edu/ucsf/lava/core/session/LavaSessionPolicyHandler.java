package edu.ucsf.lava.core.session;



import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.session.model.LavaSession;

public class LavaSessionPolicyHandler {
	public static final Long DEFAULT_EXPIRATION_MINUTES = new Long(30);
	public static final Long DEFAULT_WARNING_MINUTES = new Long(5);
	public static final long MILLIS_PER_MINUTE = 60000;
	protected Long expireMinutes = DEFAULT_EXPIRATION_MINUTES;  // number of seconds until a session expires
	protected Long warningMinutes = DEFAULT_WARNING_MINUTES;
	private static  Timestamp defaultExpDate;
	private SessionManager sessionManager; //reference back to calling session monitor
	
	
	
	public static final Timestamp getDefaultExpDate(){
		if (defaultExpDate == null){
			try{
				DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				defaultExpDate = new Timestamp(dfm.parse("9999-09-09 00:00:00").getTime());
			}catch(Exception e){}//do nothing
		}
		return defaultExpDate;
	}
	
	//override in subclass to identify those sessions that are handled in a custom manner
	public boolean handlesSession(LavaSession session, HttpServletRequest request){
		return true;
	}
	
	
	
	
	
	public final boolean hasSessionExpired(LavaSession session,HttpServletRequest request){
		
		if (session.getCurrentStatus().equals(LavaSession.LAVASESSION_STATUS_EXPIRED)){
			return true;
		}
		
		determineExpireTime(session, request);
		if (session.isExpireTimeBeforeNow()){
			return true;
		}
		return false;
	}
	
	public final boolean shouldSessionDisconnect(LavaSession session,HttpServletRequest request){
		if (session.getCurrentStatus().equals(LavaSession.LAVASESSION_STATUS_DISCONNECTED)){
			return true;
		}
		
		session.setDisconnectTime(determineDisconnectTime(session,request));
		session.setDisconnectMessage(determineDisconnectMessage(session,request));
		
		if (session.isDisconnectTimeBeforeNow()){
			return true;
		}

		return false;
	}


	
	//override in subclass for custom  functionality
	public Date determineDisconnectTime(LavaSession session,HttpServletRequest request){
		return session.getDisconnectTime();
	}
	
	//override in subclass for custom  functionality
	public String determineDisconnectMessage(LavaSession session,HttpServletRequest request){
		return session.getDisconnectMessage();
	}
		
	//override in subclass for custom  functionality
	public Date determineExpireTime(LavaSession session,HttpServletRequest request){
		if(session == null){ return getDefaultExpDate();}
		Date now = new Date();
		
		if(session.getExpireTime()!= null && 
				session.getExpireTime().before(now)){
			return session.getExpireTime();
		}
		
		if (session.getAccessTime()!=null){
			return new Date(session.getAccessTime().getTime() + (expireMinutes * MILLIS_PER_MINUTE));
		}
		return new Date(now.getTime() + (expireMinutes * MILLIS_PER_MINUTE));
		
	}
	public boolean isDisconnectTimeWithinWarningWindow(LavaSession session,HttpServletRequest request){
		if(session.getDisconnectTime() == null) {return false;} // no disconnect time
		if(session.isDisconnectTimeBeforeNow()){return false;} //already disconnected
		
		Date warningTime = new Date(session.getDisconnectTime().getTime() - (warningMinutes * MILLIS_PER_MINUTE)); 
		if(warningTime.before(new Date())){
			return true;
		}
		return false;
	}
	
	public int getSecondsUntilExpiration(LavaSession session,HttpServletRequest request){
		
		if(session.getExpireTime() == null || session.getExpireTime().equals(this.getDefaultExpDate())){
			return getExpireMinutes().intValue();
		}else if (session.isExpireTimeBeforeNow()){
			return 0;
		}else{
			return new Long(((session.getExpireTime().getTime() - new Date().getTime())/1000)).intValue();
		}
	}

	
	public  Long getWarningMinutes() {
		return warningMinutes;
	}

	public  void setWarningMinutes(Long warningMinutes) {
		this.warningMinutes = warningMinutes;
	}

	public Long getExpireMinutes() {
		return expireMinutes;
	}

	public void setExpireMinutes(Long expireMinutes) {
		this.expireMinutes = expireMinutes;
	}
	

	public SessionManager getSessionMonitor() {
		return sessionManager;
	}

	public void setSessionMonitor(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	
	
}
