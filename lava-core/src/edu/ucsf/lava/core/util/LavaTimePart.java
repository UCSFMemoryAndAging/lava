package edu.ucsf.lava.core.util;

import java.util.Calendar;
import java.util.Date;
import static edu.ucsf.lava.core.util.LavaDateUtils.*;

/**
 * Simple subclass of the Date class used to represent just the 
 * "time part" (i.e. Hours/Minutes/Seconds) of a date property.  Allows
 * spring to easily bind date parts to properties using a syntax like
 * dateProperty.timePart
 * 
 *  
 * @author jhesse
 *
 */
public class LavaTimePart extends Date {

	

	/**
	 * Initialize to empty date part.
	 *
	 */
	public LavaTimePart() {
		super(EMPTY_TIME_PART_DATE.getTime());
	}

	public LavaTimePart(long date) {
		super(date);
	
	}

	public Date toDate(){
		 return new Date(this.getTime());
	}
	
	
	public boolean isEmpty(){
		return isTimePartEmpty(this.toDate());
	}
	
	
}