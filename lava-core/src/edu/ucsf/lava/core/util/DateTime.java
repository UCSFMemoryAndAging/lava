package edu.ucsf.lava.core.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Simple wrapper around a date that supports distinct properties for 
 * the date and time parts.  For simplicity, the date and time parts 
 * are just copies of the wrapped date, but the default spring binding
 * will only access the date and time parts respectively. 
 */
public class DateTime extends Date{

	public DateTime() {
		super();
	}
	
	public DateTime(long date) {
		super(date);
	}

	public Date toDate(){
		 return new Date(this.getTime());
	}
	
	/**
	 * return the date using the DatePartWrapper class
	 * @return
	 */
	synchronized public DatePart getDatePart() {
		return new DatePart(this.getTime());
	}
	
	/**
	 * Set the date part of the wrapped date. 
	 * @param datePart
	 */
	synchronized public void setDatePart(DatePart datePart) {
		//get calendar representations of the dates
		Calendar thisCal = getCalendar(this);
		Calendar datePartCal = getCalendar(datePart);
		//set the date part aspects of the date
		thisCal.set(Calendar.YEAR,datePartCal.get(Calendar.YEAR));
		thisCal.set(Calendar.MONTH,datePartCal.get(Calendar.MONTH));
		thisCal.set(Calendar.DAY_OF_MONTH,datePartCal.get(Calendar.DAY_OF_MONTH));
		
		this.setTime(thisCal.getTimeInMillis());
		
	}
	/**
	 * Return the date using the TimePartWrapper class
	 * @return
	 */
	synchronized public TimePart getTimePart() {
		return new TimePart(this.getTime());
	}
	
	/**
	 * Set the time part of the wrapped date
	 * @param timePart
	 */
	synchronized public void setTimePart(TimePart timePart) {
		// get calendar representations of the dates
		Calendar thisCal = getCalendar(this);
		Calendar timePartCal = getCalendar(timePart);
		//set the date part aspects of the date
		thisCal.set(Calendar.HOUR_OF_DAY,timePartCal.get(Calendar.HOUR_OF_DAY));
		thisCal.set(Calendar.MINUTE,timePartCal.get(Calendar.MINUTE));
		thisCal.set(Calendar.MILLISECOND,timePartCal.get(Calendar.MILLISECOND));
		
		this.setTime(thisCal.getTimeInMillis());
		
	}
	
	protected Calendar getCalendar(Date date){
		if(date==null){
			date = new Date(0);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	
	}
	
	
	
}
