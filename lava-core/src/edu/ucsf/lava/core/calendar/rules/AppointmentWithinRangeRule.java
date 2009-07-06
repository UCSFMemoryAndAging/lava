package edu.ucsf.lava.core.calendar.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.type.DateRange;

public class AppointmentWithinRangeRule extends AbstractAppointmentRule {

	public static String OUT_OF_RANGE_RULE_ERROR_MESSAGE_CODE = "appointmentWithinRangeRule.outOfRangeErrorMessage";
	
	
	protected DateRange range;
	protected String rangeDescription;
	
	
	public boolean appliesTo(Appointment appointment) {
		return true;
	}


	public boolean isViolatedBy(Appointment appointment, Map<String,Object[]>errors) {
		if(appliesTo(appointment) && isOutOfRange(appointment)){
			errors.put(OUT_OF_RANGE_RULE_ERROR_MESSAGE_CODE, new Object[]{getRangeDescription()});
			return true;	
		}
		return false;
	}

	protected boolean isOutOfRange(Appointment appointment){
		if(appointment==null) {return false;}
		
		DateRange appointmentRange = appointment.getDateRange();
		if(appointmentRange == null || !appointmentRange.hasRange()){ return false;}
		if(range.contains(appointmentRange.getStart())&&
		   range.contains(appointmentRange.getEnd())){
			return false;
		}
		return true;
		
	}

	public DateRange getRange() {
		return range;
	}

	public void setRange(DateRange range) {
		this.range = range;
	}

	public String getRangeDescription() {
		if(rangeDescription!=null){return rangeDescription;}
		
		if(range!=null){
				return range.getShortRangeDesc();
		}else{
			return "";
		}
	}

	public void setRangeDescription(String rangeDescription) {
		this.rangeDescription = rangeDescription;
	}
	
	
	
}
