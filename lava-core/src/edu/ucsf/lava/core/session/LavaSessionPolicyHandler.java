package edu.ucsf.lava.core.session;



import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.session.model.LavaSession;
import edu.ucsf.lava.core.type.LavaDateUtils;

public class LavaSessionPolicyHandler {
	public static final Long DEFAULT_EXPIRATION_MINUTES = new Long(30);
	public static final Long DEFAULT_WARNING_MINUTES = new Long(5);
	public static final long MILLIS_PER_MINUTE = 60000;
	// note: this is overridden by bean configuration in core-session.xml
	protected Long expireMinutes = DEFAULT_EXPIRATION_MINUTES;  // number of seconds until a session expires
	protected Long warningMinutes = DEFAULT_WARNING_MINUTES;
	private static  Timestamp defaultExpDate;
	private SessionManager sessionManager; //reference back to calling session monitor
	
	
	
	
	
	//override in subclass to identify those sessions that are handled in a custom manner
	public boolean handlesSession(LavaSession session, HttpServletRequest request){
		return true;
	}
	
	
	
	
	
	public final boolean hasSessionExpired(LavaSession session,HttpServletRequest request){
		
		if (session.getCurrentStatus().equals(LavaSession.LAVASESSION_STATUS_EXPIRED)){
			return true;
		}
		
		session.setExpireTimestamp(determineExpireTime(session, request));
		if (session.isExpireTimeBeforeNow()){
			return true;
		}
		return false;
	}
	
	public final boolean shouldSessionDisconnect(LavaSession session,HttpServletRequest request){
		if (session.getCurrentStatus().equals(LavaSession.LAVASESSION_STATUS_DISCONNECTED)){
			return true;
		}
		
		session.setDisconnectDateTime(determineDisconnectDateTime(session,request));
		session.setDisconnectMessage(determineDisconnectMessage(session,request));
		
		if (session.isDisconnectTimeBeforeNow()){
			return true;
		}

		return false;
	}


	
	//override in subclass for custom  functionality
	public Date determineDisconnectDateTime(LavaSession session,HttpServletRequest request){
		return session.getDisconnectDateTime();
	}
	
	
	
	//override in subclass for custom  functionality
	public String determineDisconnectMessage(LavaSession session,HttpServletRequest request){
		return session.getDisconnectMessage();
	}
		
	//override in subclass for custom  functionality
	public Timestamp determineExpireTime(LavaSession session,HttpServletRequest request){
		if(session == null){ return new Timestamp(new Date().getTime()+(DEFAULT_EXPIRATION_MINUTES * MILLIS_PER_MINUTE));}
		Date now = new Date();
		
		if(session.getExpireTimestamp()!= null && 
				session.getExpireTimestamp().before(now)){
			return session.getExpireTimestamp();
		}
		
		if (session.getAccessTimestamp()!=null){
			return new Timestamp(session.getAccessTimestamp().getTime() + (expireMinutes * MILLIS_PER_MINUTE));
		}
		return new Timestamp(now.getTime() + (expireMinutes * MILLIS_PER_MINUTE));
		
	}
	public boolean isDisconnectTimeWithinWarningWindow(LavaSession session,HttpServletRequest request){
		if(session.getDisconnectDateTime() == null) {return false;} // no disconnect time
		if(session.isDisconnectTimeBeforeNow()){return false;} //already disconnected
		
		Date warningTime = new Date(session.getDisconnectTime().getTime() - (warningMinutes * MILLIS_PER_MINUTE)); 
		if(warningTime.before(new Date())){
			return true;
		}
		return false;
	}
	
	public int getSecondsUntilExpiration(LavaSession session,HttpServletRequest request){
		
		if(session.getExpireTimestamp() == null){
			return getExpireMinutes().intValue();
		}else if (session.isExpireTimeBeforeNow()){
			return 0;
		}else{
			return new Long(((session.getExpireTimestamp().getTime() - new Date().getTime())/1000)).intValue();
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
