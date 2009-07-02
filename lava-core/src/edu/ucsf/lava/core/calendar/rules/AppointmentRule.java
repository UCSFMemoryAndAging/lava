package edu.ucsf.lava.core.calendar.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.ucsf.lava.core.calendar.model.Appointment;


/**
 * Defines the contract for rules that apply to appointments. 
 * @author jhesse
 *
 */
public interface AppointmentRule {

	
	/**
	 * Tests the appointment against the rule, returning any errors as message codes with optional arguments.
	 * @param appointment
	 * @param errors 
	 * @return
	 */
	public boolean isViolatedBy(Appointment appointment,Map<String,Object[]>errors);
	
	
	
	/**
	 * Determines whether the rule applies to the appointment.  
	 * @param appointment
	 * @return true if the rule applies to the appointment
	 */
	public boolean appliesTo(Appointment appointment);	
	
	
	
	

}
