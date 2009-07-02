package edu.ucsf.lava.core.calendar;

import java.util.Map;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.calendar.model.Calendar;

/**
 * This interface uses a delegate pattern to support customization of calendar functionality
 * based on calendar types and/or specific instances of calendars. 
 * @author jhesse
 *
 */
public interface CalendarDelegate {

	/**
	 * Does this delegate handle the calendar
	 * @param calendar
	 * @return
	 */
	public boolean handlesCalendar(Calendar calendar);
	
	
	public Map<String,Appointment>getAppointmentTemplates(Calendar calendar, AuthUser user);
	

	
	
}
