package edu.ucsf.lava.core.calendar.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.type.DateRange;
/**
 * Generic rule that enforces a maximum number of concurrent reservations for
 * a specific calendar.
 * 
 * @author jhesse
 *
 */
public class AppointmentConcurrencyRule extends AbstractAppointmentRule {

	public static String MAX_CONCURRENCY_RULE_ERROR_MESSAGE_CODE = "appointmentConcurrencyRule.maxConcurrencyErrorMessage";
	
	public static Long NO_MAX_CONCURRENCY = (long)-1;
	
	protected Long maxConcurrency;
	
	
	public AppointmentConcurrencyRule() {
		super();
		this.maxConcurrency = NO_MAX_CONCURRENCY;
	}



	public boolean appliesTo(Appointment appointment) {
		return true;
	}

	
	
	public boolean isViolatedBy(Appointment appointment, Map<String,Object[]> errors) {
		if(appliesTo(appointment) && exceedsMaxConcurrency(appointment)){
			errors.put(MAX_CONCURRENCY_RULE_ERROR_MESSAGE_CODE, new Object[]{maxConcurrency});
			return true;
		}
		return false;
	}

	
	protected boolean exceedsMaxConcurrency(Appointment appointment){
		if(NO_MAX_CONCURRENCY.equals(maxConcurrency) ||	appointment==null ||
				appointment.getCalendar()==null) {return false;}
		
		DateRange range = appointment.getDateRange();
		
		if(!range.hasRange()){return false;}
		
		Calendar calendar = appointment.getCalendar();
		
		List<Appointment> concurrentAppointments = calendar.getConcurrentAppointments(appointment);
		//quick check for single concurrency. 
		if(maxConcurrency == 1 && concurrentAppointments.size()>0){
			return true;
		}
		//quick check to see if the total number of appointments < maxConcurrency
		if(concurrentAppointments.size() + 1 < maxConcurrency ){
			return false;
		}
		
		
		/*
		 * now we need to all the periods of overlap between the new appointment and the existing appointments
		 * 
		 * First get the overlap between the new appointment and one of the appointments.  Then check to see how many
		 * of the other existing appointments share this overlap.  If we ever exceed the max concurrency, then this
		 * is a rule violation. 
		 * 
		 */ 
		for(Appointment target : concurrentAppointments){
			DateRange targetRange = target.getDateRange();
			if(targetRange!=null && targetRange.hasRange()){
				DateRange targetOverlap = targetRange.getOverlap(range);
				if(targetOverlap!=null & targetOverlap.hasRange()){
					Long count = new Long(1); // take the new appointmet into account (we will end up counting the target again in the loop)
					for(Appointment existing : concurrentAppointments){
						if(targetOverlap.overlaps(existing.getDateRange())){
							if(++count > maxConcurrency){
								return true;
							}
						}
					}
				}
			}
		}
		//if we get here, the appointment does not violate max concurrency. 
		return false;
		
	}



	public Long getMaxConcurrency() {
		return maxConcurrency;
	}



	public void setMaxConcurrency(Long maxConcurrency) {
		this.maxConcurrency = maxConcurrency;
	}


	

	
	
}
