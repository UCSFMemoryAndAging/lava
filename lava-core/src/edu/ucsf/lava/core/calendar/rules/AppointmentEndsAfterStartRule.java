package edu.ucsf.lava.core.calendar.rules;

import java.util.Map;

import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.type.DateRange;

/**
 * Simple check to make sure that appointment ends date/time follows the start date/time
 * @author jhesse
 *
 */
public class AppointmentEndsAfterStartRule implements AppointmentRule {

	public static String ENDS_AFTER_START_ERROR_MESSAGE_CODE = "appointmentEndsAfterStartRule.errorMessage";
	public boolean appliesTo(Appointment appointment) {
		return true;
	}

	public boolean isViolatedBy(Appointment appointment, Map<String, Object[]> errors) {
	if(!appliesTo(appointment)){return false;}

	DateRange range = appointment.getDateRange();
	if(range == null || !range.hasStart() || !range.hasEnd()){ return false;}
	
	if(range.getStart().after(range.getEnd())){
		errors.put(ENDS_AFTER_START_ERROR_MESSAGE_CODE,null);
			return true;
	}
	return false;
}

	
}
