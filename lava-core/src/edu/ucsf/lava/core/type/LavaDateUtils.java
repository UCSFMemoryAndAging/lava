package edu.ucsf.lava.core.type;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
	
	public static Time getTime(Integer hr, Integer min, Integer sec){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.HOUR_OF_DAY,hr);
		calendar.set(Calendar.MINUTE,min);
		calendar.set(Calendar.SECOND,sec);
		return new Time(calendar.getTimeInMillis());
	}
	
	public static Time getTime(String timeStr){
		return getTime(timeStr, null);
	}
	
	public static Time getTime(String timeStr, String timeFormatStr){
		if(timeStr==null){return null;}
		DateFormat dateFormat;
		try{
			if (timeFormatStr == null){
				return Time.valueOf(timeStr); // defaults to "hh:mm:ss" format
			} else {
				dateFormat = new SimpleDateFormat(timeFormatStr);
			}
			Date newDate = dateFormat.parse(timeStr);
			return getTimePart(newDate);
		}catch(Exception e){
			return null;
		}
		
	}

	public static Date getDatePart(Date date){
		if(date==null){return null;}
		
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);
		Calendar dateCal = toCalendar(date);
		cal.set(Calendar.MONTH, dateCal.get(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, dateCal.get(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.YEAR, dateCal.get(Calendar.YEAR));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}	
	
	public static Date getDate(String dateStr){
		return getDate(dateStr, null);
	}
	
	public static Date getDate(String dateStr, String dateFormatStr){
		if(dateStr==null){return null;}
		DateFormat dateFormat;
		try{
			if (dateFormatStr == null){
				dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
			} else {
				dateFormat = new SimpleDateFormat(dateFormatStr);
			}
			Date newDate = dateFormat.parse(dateStr);
			return newDate;
		}catch(Exception e){
			return null;
		}
	}	
	
	public static Date addDays(Date date, Integer offset){
		if(date==null){return null;}
		
		Calendar dateCal = toCalendar(date);
		dateCal.add(Calendar.DAY_OF_MONTH, offset);
		return dateCal.getTime();
	}
	
	public static Date addTime(Date date, Time timeOffset){
		if (date==null){return null;}
		if (timeOffset==null){return date;}
		
		Calendar dateCal = toCalendar(date);
		String[] parsedT = timeOffset.toString().split(":");
		dateCal.add(Calendar.HOUR_OF_DAY,Integer.valueOf(parsedT[0]));
		dateCal.add(Calendar.MINUTE,Integer.valueOf(parsedT[1]));
		dateCal.add(Calendar.SECOND,Integer.valueOf(parsedT[2]));
		return dateCal.getTime();		
	}
	
	public static Date setTimePart(Date date, Time time){
		return addTime(getDayStartDate(date),time);
	}
	
	public static Calendar toCalendar(Date date){
		if(date==null){return null;}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
		} 
	
	public static Date getDayStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}
	
	public static Time getDayStartTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return new Time(calendar.getTimeInMillis());
	}

	public static Date getDayEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}
	
	public static Time getDayEndTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return new Time(calendar.getTimeInMillis());
	}
	
	
	public static Date getMidnightOfDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY,00);
		calendar.set(Calendar.MINUTE,00);
		calendar.set(Calendar.SECOND,00);
		return calendar.getTime();
	}
	
	public static Time getHourBeginTime(Time pivotTime){
		Calendar timeCal = Calendar.getInstance();
		timeCal.setTime(pivotTime);
		timeCal.set(Calendar.MINUTE,0);
		timeCal.set(Calendar.SECOND,0);
		return new Time(timeCal.getTimeInMillis());
	}
	
	public static Time getHourEndTime(Time pivotTime){
		Calendar timeCal = Calendar.getInstance();
		timeCal.setTime(pivotTime);
		timeCal.set(Calendar.MINUTE,59);
		timeCal.set(Calendar.SECOND,59);
		return new Time(timeCal.getTimeInMillis());
	}

	public static Date getYearStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_YEAR,1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}
	
	public static Date getYearEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_YEAR,calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

	public static Date getMonthStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}
	
	public static Date getMonthEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}
	
	public static Integer getDaysinMonth(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public static Date getWorkWeekStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}

	public static Date getWorkWeekEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

	public static Date getWeekStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}

	public static Date getWeekEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

}
