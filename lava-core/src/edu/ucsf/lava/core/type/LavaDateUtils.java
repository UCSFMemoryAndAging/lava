package edu.ucsf.lava.core.type;



import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.action.ActionUtils;

public class LavaDateUtils {

	
	public static Date getDateTime(Date date, Time time){
		if(date == null && time == null){return null;}
		
		Calendar dateCal = Calendar.getInstance();
		dateCal.setTime(new Date());
		dateCal.setTime( date==null ? new Date() : date);
		if(time!=null){
			Calendar timeCal = Calendar.getInstance();
			timeCal.setTime(time);
			dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
			dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
			dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
			dateCal.set(Calendar.MILLISECOND, 0);
		}
		return dateCal.getTime();
	}	
	
	
	public static Time getTimePart(Date date){
		if(date==null){return null;}
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		Calendar dateCal = toCalendar(date);
		cal.set(Calendar.HOUR_OF_DAY, dateCal.get(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, dateCal.get(Calendar.MINUTE));
		cal.set(Calendar.SECOND, dateCal.get(Calendar.SECOND));
		return new Time(cal.getTimeInMillis());
	}	
	

	public static Date getDatePart(Date date){
		if(date==null){return null;}
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		Calendar dateCal = toCalendar(date);
		cal.set(Calendar.MONTH, dateCal.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, dateCal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.YEAR, dateCal.get(Calendar.YEAR));
		return cal.getTime();
	}	
	
	public static Calendar toCalendar(Date date){
		if(date==null){return null;}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
		} 
	

	
	
}
