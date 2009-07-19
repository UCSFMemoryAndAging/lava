package edu.ucsf.lava.core.calendar;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.type.DateRange;

public class CalendarLayoutUtils {

	
	public static Long getMinutesUntilStartFrom6AM(Appointment appointment){
		return getMinutesUntilStart(appointment,get6AMTime());
	}
	
	public static Long getMinutesUntilStartFrom12AM(Appointment appointment){
		return getMinutesUntilStart(appointment,get12AMTime());
	}
	public static Long getMinutesUntilStart(Appointment appointment, Time timeIn){
		if(appointment==null){return null;}
		DateRange range = appointment.getDateRange();
		if(range == null || !range.hasStart()){return null;}
		return appointment.getDateRange().getMinutesUntilStart(timeIn);
	}
	
	
	public static Time get6AMTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.HOUR_OF_DAY,6);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return new Time(calendar.getTimeInMillis());
	}
	
	
	public static Time get12AMTime(){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return new Time(calendar.getTimeInMillis());
	}
	
}
