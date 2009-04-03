package edu.ucsf.lava.core.util;

import java.util.Date;

public class TimePart extends Date{

	public TimePart() {
		super();

	}

	public TimePart(long date) {
		super(date);
	
	}

	public Date toDate(){
		 return new Date(this.getTime());
	}
	
}
