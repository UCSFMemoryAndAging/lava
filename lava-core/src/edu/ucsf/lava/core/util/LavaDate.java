package edu.ucsf.lava.core.util;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import static edu.ucsf.lava.core.util.LavaDateUtils.*;
/**
 * Extension of the date class that supports distinct properties for 
 * the date and time parts, and also has advanced support for empty dates and times.
 * Because we use a single timestamp property for properties that 
 * have both date and time parts specified, there is no way to distinguish between
 * a 12:00 midnight setting that is simply and unset time and an actual time
 * set for 12:00 midnight.  We use btmask flags in the  milliseconds property to distinguish
 * between dates and times with the default intiialization value and with the empty or
 * "unset" value. 
 * 
 * There are sub properties pf the LavaDate that are used by spring to bind date parts and 
 * time parts to the LavaDate property (e.g. datePropertyName.timePart and datePropertyName.datePart).
 * There also is a timestamp property for hibernate to bind the actual wrapped date property 
 * to the database.  
 * 
 */
public class LavaDate{

	protected Date wrappedDate;
	
	
	/* Contructors */
	
	public LavaDate() {
		super();
		this.wrappedDate = new Date(EMPTY_DATETIME_DATE.getTime());
	}
	
	public LavaDate(long date) {
		this();
		this.wrappedDate = new Date(date);
		}
	
	public LavaDate(Date date) {
		this();
		setDate(date);
	}

	public LavaDate(Timestamp timestamp) {
		this();
		if(timestamp!=null){
			setDate(new Date(timestamp.getTime()));
		}
	}
	
	
	/* special null date and time functions */
	
	/**
	 * Determines whether the wrapped date property is empty.  Empty is 
	 * defined as either null or having an empty date part and an empty time part. 
	 * 
	 */
	public synchronized boolean isEmpty(){
		if(this.wrappedDate == null || 
			(isDateEmpty() && isTimeEmpty())){
			return true;
		}
		return false;
		
	}
	
	/**
	 * Determines whether the wrapped date property has an empty date part. 
	 * @return
	 */
	public synchronized  boolean isDateEmpty(){
		if(this.wrappedDate == null || isDatePartEmpty(this.wrappedDate)){
			return true;
			}
		return false;
	
	}
	

	/**
	 * Determines whether the wrapped date property has an empty date part. 
	 * @return
	 */
	public  synchronized boolean isTimeEmpty(){
		if(this.wrappedDate == null || isTimePartEmpty(this.wrappedDate)){
			return true;
			}
		return false;
	
	}
		
	public synchronized Date getDate(){
		if(wrappedDate == null){
			return null;
		}else{
			return new Date(wrappedDate.getTime());
		}
	}
	
	public synchronized void setDate(Date date){
		if(date==null){
			this.wrappedDate = null;
		}else{
			this.wrappedDate = new Date(date.getTime());
		}
	}
	
	/**
	 * Gets the date part (Month/Day/Year) only of the wrapped date
	 * @return null if date part is empty.
	 */
	public synchronized LavaDatePart getDatePart() {
		if(isDatePartEmpty(wrappedDate)){
			return (LavaDatePart) null;
		}
		return new LavaDatePart(wrappedDate.getTime());
	}
	
	/**
	 * Set the date part of the wrapped date, if the date passed in is null then
	 * set the wrapped date to the empty date part value; 
	 * @param datePart
	 */
	public synchronized void setDatePart(LavaDatePart datePart) {
		if(wrappedDate==null){
			wrappedDate = new Date(EMPTY_DATETIME_DATE.getTime());
		}
		wrappedDate = LavaDateUtils.setDatePart(wrappedDate,datePart);
	}
	/**
	 * Gets the time part (Hours/Minutes/SEconds) only of the wrapped date
	 * @return null if time part is empty.
	 */
	public synchronized LavaTimePart getTimePart() {
		if(isTimePartEmpty(wrappedDate)){
			return (LavaTimePart) null;
		}
		return new LavaTimePart(wrappedDate.getTime());
	}
	
	/**
	 * Set the time part of the wrapped date, if the time passed in is null then
	 * set the wrapped date to the empty time part value; 
	 * @param datePart
	 */
	public synchronized void setTimePart(LavaTimePart timePart) {
		if(wrappedDate==null){
			wrappedDate = new Date(EMPTY_DATETIME_DATE.getTime());
		}
		wrappedDate = LavaDateUtils.setTimePart(wrappedDate,timePart);
	}
	
	public synchronized Timestamp getTimestamp() {
		if(isEmpty()){return (Timestamp)null;}
		return new Timestamp(wrappedDate.getTime());
	}

	public synchronized void setTimestamp(Timestamp timestamp) {
		if(timestamp == null){
			wrappedDate = new Date(EMPTY_DATETIME_DATE.getTime());
		}else{
			wrappedDate.setTime(timestamp.getTime());
		}
	}

	
		
	
	
}
