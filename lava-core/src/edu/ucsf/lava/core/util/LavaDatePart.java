package edu.ucsf.lava.core.util;

import java.util.Calendar;
import java.util.Date;
import static edu.ucsf.lava.core.util.LavaDateUtils.*;


/**
 * Simple subclass of the Date class used to represent just the 
 * "date part" (i.e. Month/Day/Year) of a date property.  Allows
 * spring to easily bind date parts to properties using a syntax like
 * dateProperty.datePart
 * 
 *
 */
public class LavaDatePart extends Date {


	/**
	 * Initialize to empty date part.
	 *
	 */
	public LavaDatePart() {
		super(EMPTY_DATE_PART_DATE.getTime());
	}

	public LavaDatePart(long date) {
		super(date);
	
	}

	public Date toDate(){
		 return new Date(this.getTime());
	}
	
	
	public boolean isEmpty(){
		return isDatePartEmpty(this.toDate());
	}

	

}
