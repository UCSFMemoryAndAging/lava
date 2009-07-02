package edu.ucsf.lava.core.calendar.controller;

import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;

public class CalendarHandler extends BaseEntityComponentHandler {

	public CalendarHandler() {
		super();
		this.setHandledEntity("calendar", Calendar.class);
		this.setRequiredFields(new String[]{"name","description"});
		
	}

	
	
}
