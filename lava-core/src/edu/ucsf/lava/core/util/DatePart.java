package edu.ucsf.lava.core.util;

import java.util.Date;

public class DatePart extends Date {

	public DatePart() {
		super();
	}

	public DatePart(long date) {
		super(date);
	
	}

	public Date toDate(){
		 return new Date(this.getTime());
	}
}
