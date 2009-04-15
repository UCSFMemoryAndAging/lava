package edu.ucsf.lava.core.util;

import static edu.ucsf.lava.core.util.LavaDateUtils.EMPTY_DATE_PART_CALENDAR;
import static edu.ucsf.lava.core.util.LavaDateUtils.EMPTY_DATE_PART_DATE;
import static edu.ucsf.lava.core.util.LavaDateUtils.EMPTY_DATE_PART_FLAG;
import static edu.ucsf.lava.core.util.LavaDateUtils.toCalendar;

import java.util.Calendar;
import java.util.Date;

/*
 * Utility class for working with the LavaDate class.  
 * 
 * Has special methods to determine whether a date property that is 
 * initialized actually has "empty" date and time parts   The current implementation
 * uses bitwise logic and the milliseconds property to store and retrieve
 * flags for empty date and time parts. 
 * 
 *  
 * @author jhesse
 */
public class LavaDateUtils {


	public static final int EMPTY_TIME_PART_FLAG = 4;
	public static final int EMPTY_DATE_PART_FLAG = 8;
	public static final int EMPTY_DATETIME_FLAG = 12;
	
	public static final Date EMPTY_DATE_PART_DATE = new Date(EMPTY_DATE_PART_FLAG);
	public static final Date EMPTY_TIME_PART_DATE = new Date(EMPTY_TIME_PART_FLAG);
	public static final Date EMPTY_DATETIME_DATE = new Date(EMPTY_DATETIME_FLAG);
	
	public static final Calendar EMPTY_TIME_PART_CALENDAR = toCalendar(EMPTY_TIME_PART_DATE);
	public static final Calendar EMPTY_DATE_PART_CALENDAR = toCalendar(EMPTY_DATE_PART_DATE);
	public static final Calendar EMPTY_DATETIME_CALENDAR = toCalendar(EMPTY_DATETIME_DATE);

	
	
	/**
	 * Utility method that inspects a date to determine
	 * if the date part (Month, Day, Year) is empty. 
	 * Empty is defined by this method as either the 
	 * date itself is null or the Month/Day/Year is 
	 * equal to the Month/Day/Year when a date is initialized to 
	 * 0 and the Millisecond value holds a special 
	 * "empty date part" flag value.
	 */
	public static boolean isDatePartEmpty(Date date){
		if(date == null || date.equals(EMPTY_DATE_PART_DATE)){return true;}
		
		Calendar calendar = toCalendar(date);
		if(calendar.get(Calendar.YEAR) == EMPTY_DATE_PART_CALENDAR.get(Calendar.YEAR) &&
			calendar.get(Calendar.MONTH) == EMPTY_DATE_PART_CALENDAR.get(Calendar.MONTH) &&
			calendar.get(Calendar.DAY_OF_MONTH) == EMPTY_DATE_PART_CALENDAR.get(Calendar.DAY_OF_MONTH)){
			//matches empty date part on Month/Date/Year so now do bitwise check to see of empty_date_part flag is set
			if(EMPTY_DATE_PART_FLAG ==	(calendar.get(Calendar.MILLISECOND) & EMPTY_DATE_PART_FLAG)){
				return true;
			}
						
		}
		return false;
	}
	
	
	/**
	 * Utility method that inspects a date to determine
	 * if the time part (Hours/Minutes/Seconds) is empty. 
	 * Empty is defined by this method as either the 
	 * date itself is null or the Hours/Minutes/Seconds is 
	 * equal to the Hours/Minutes/Seconds when a date is initialized to 
	 * 0 and the Millisecond value holds a special 
	 * "empty time part" flag value.
	 */
	public static boolean isTimePartEmpty(Date date){
		if(date == null || date.equals(EMPTY_TIME_PART_DATE)){return true;}
		
		Calendar calendar = toCalendar(date);
		if(calendar.get(Calendar.HOUR_OF_DAY) == EMPTY_TIME_PART_CALENDAR.get(Calendar.HOUR_OF_DAY) &&
			calendar.get(Calendar.MINUTE) == EMPTY_TIME_PART_CALENDAR.get(Calendar.MINUTE) &&
			calendar.get(Calendar.SECOND) == EMPTY_TIME_PART_CALENDAR.get(Calendar.SECOND)){
			//matches empty date part on Hours/Minutes/Seconds so now do bitwise check to see of empty_time_part flag is set
			if(EMPTY_TIME_PART_FLAG ==	(calendar.get(Calendar.MILLISECOND) & EMPTY_TIME_PART_FLAG)){
				return true;
			}
						
		}
		return false;
	}
	
	/**
	 * Set the Month/Day/Year part of the date using the Month/Day/Year in datePart.
	 * Handles nulls and special date empty values
	 * @param date
	 * @param datePart
	 * @return returns the date for method chaining.
	 */
	public static Date setDatePart(Date date, Date datePart){
		if (date==null){return date;}
		Calendar dateCal = toCalendar(date);
		Calendar datePartCal = toCalendar(datePart==null ? EMPTY_DATE_PART_DATE : datePart);
		dateCal.set(Calendar.DAY_OF_MONTH, datePartCal.get(Calendar.DAY_OF_MONTH));
		dateCal.set(Calendar.MONTH, datePartCal.get(Calendar.MONTH));
		dateCal.set(Calendar.YEAR, datePartCal.get(Calendar.YEAR));
		if(isDatePartEmpty(datePart)){
			dateCal.set(Calendar.MILLISECOND,EMPTY_DATE_PART_FLAG | dateCal.get(Calendar.MILLISECOND));
		}
		return date;
		
	}
	
	/**
	 * Set the Hour/Minuts/Seconds part of the date using the Hour/Minute/Seconds in timePart.
	 * Handles nulls and special time empty values
	 * @param date
	 * @param timePart
	 * @return returns the date for method chaining.
	 */
	public static Date setTimePart(Date date, Date timePart){
		if (date==null){return date;}
		Calendar dateCal = toCalendar(date);
		Calendar datePartCal = toCalendar(timePart==null ? EMPTY_TIME_PART_DATE : timePart);
		dateCal.set(Calendar.HOUR_OF_DAY, datePartCal.get(Calendar.HOUR_OF_DAY));
		dateCal.set(Calendar.MINUTE, datePartCal.get(Calendar.MINUTE));
		dateCal.set(Calendar.SECOND, datePartCal.get(Calendar.SECOND));
		if(isTimePartEmpty(timePart)){
			dateCal.set(Calendar.MILLISECOND,EMPTY_TIME_PART_FLAG | dateCal.get(Calendar.MILLISECOND));
		}
		return date;
		
	}
	
	public static Calendar toCalendar(Date date){
		if(date==null){return null;}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
		} 
}
