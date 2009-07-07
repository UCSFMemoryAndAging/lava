package edu.ucsf.lava.core.calendar.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.type.DateRange;
/**
 * Generic rule that enforces a minimum and maximum reservation length.
 * 
 * @author jhesse
 *
 */
public class AppointmentLengthRule extends AbstractAppointmentRule {

	public static String MAX_LENGTH_RULE_ERROR_MESSAGE_CODE = "appointmentLengthRule.maxLengthErrorMessage";
	public static String MIN_LENGTH_RULE_ERROR_MESSAGE_CODE = "appointmentLengthRule.minLengthErrorMessage";
	
	public static Long NO_MAX_LENGTH = (long)-1;
	public static Long NO_MIN_LENGTH = (long)0;
	
	protected Long minLength;
	protected Long maxLength;
	
	
	
	public AppointmentLengthRule() {
		this(NO_MIN_LENGTH,NO_MAX_LENGTH);
	}

	public AppointmentLengthRule(Long minLength){
		this(minLength,NO_MAX_LENGTH);
	}

	
	public AppointmentLengthRule(Long minLength, Long maxLength){
		super();
		this.minLength = minLength;
		this.maxLength = maxLength;
	}

	public boolean appliesTo(Appointment reservation) {
		return true;
	}

	
	


	public boolean isViolatedBy(Appointment appointment, Map<String,Object[]>errors) {
		if(!appliesTo(appointment)){return false;}
		
		if(isLessThanMinLength(appointment)){
			errors.put(MIN_LENGTH_RULE_ERROR_MESSAGE_CODE, new Object[]{getLengthDesc(minLength)});
			return true;
		}
		if (isgreaterThanMaxLength(appointment)){
			errors.put(MAX_LENGTH_RULE_ERROR_MESSAGE_CODE, new Object[]{getLengthDesc(maxLength)});
			return true;
		}
		return false;
	}

	protected boolean isLessThanMinLength(Appointment appointment){
		if(NO_MIN_LENGTH.equals(minLength) || appointment==null) {return false;}
		
		DateRange range = appointment.getDateRange();
		if(range == null || !range.hasRange()){ return false;}
		Long length = range.getRangeInMinutes();
		if(length >= minLength){return false;}
		return true;
		
	}
	protected boolean isgreaterThanMaxLength(Appointment appointment){
		if(NO_MAX_LENGTH.equals(maxLength) || appointment==null) {return false;}
		
		DateRange range = appointment.getDateRange();
		if(range == null || !range.hasRange()){ return false;}
		Long length = range.getRangeInMinutes();
		if(length <= maxLength){return false;}
		return true;
		
	}

	protected String getLengthDesc(Long length){
		if (length == null){return "";}

		StringBuffer desc = new StringBuffer();
		if(length < 60){
			desc.append(length).append(" minute(s)").toString();		
		}else if (length < (60 * 24)){
			Long hours = length/60;
			desc.append(hours).append( " hour(s)");
			Long minutes = length % 60;
			if(minutes!=0){
				desc.append(" ").append(minutes).append(" minute(s)");
			}
		}else{
			Long days = length/(60*24);
			Long remainder = length % (60*24);
			Long hours = remainder / 60;
			Long minutes = remainder % 60;
			desc.append(days).append(" day(s)");
			if(hours!=0){
				desc.append(" ").append(hours).append( " hour(s)");
			}
			if(minutes!=0){
				desc.append(" ").append(minutes).append(" minute(s)");
			}
		}
		return desc.toString();
	}
	
	public Long getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(Long maxLength) {
		this.maxLength = maxLength;
	}

	public Long getMinLength() {
		return minLength;
	}

	public void setMinLength(Long minLength) {
		this.minLength = minLength;
	}
	

	
	
}
