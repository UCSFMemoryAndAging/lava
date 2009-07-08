package edu.ucsf.lava.core.calendar;

import java.util.Date;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.type.DateRange;
import edu.ucsf.lava.core.type.LavaDateUtils;

public class CalendarDaoUtils {

	public static final String START_DATE_PARAM = "startDate";
	public static final String START_TIME_PARAM = "startTime";
	public static final String END_DATE_PARAM = "endDate";
	public static final String END_TIME_PARAM = "endTime";
	
	/**
	 * return a complex dao param that encodes the logic for determining whether the daterange passed in overlaps with the start and end date properties in 
	 * the database. 
	 * 
	 * @param range
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getDateRangeOverlapParam(String startDateProp, String startTimeProp,
			String endDateProp, String endTimeProp, DateRange range, LavaDaoFilter filter){
			
		if(range==null || filter == null){return null;}
		
		return filter.daoDateAndTimeOverlapsParam(startDateProp, startTimeProp, endDateProp, endTimeProp, 
				LavaDateUtils.getDatePart(range.getStart()),LavaDateUtils.getTimePart(range.getStart()),
				LavaDateUtils.getDatePart(range.getEnd()),LavaDateUtils.getTimePart(range.getEnd()));
		}
	

	/**
	 * Convenience function with default start and end date param names. 
	 * @param range
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getDateRangeOverlapParam(DateRange range, LavaDaoFilter filter){
		return getDateRangeOverlapParam(START_DATE_PARAM,START_TIME_PARAM,END_DATE_PARAM,END_TIME_PARAM,range,filter);
		}
	
	
	/**
	 * return a dao param that encodes the logic for determining whether an appointment includes a particular user
	 * as an attendee
	 * @param user
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getOrganizerParam(AuthUser user, LavaDaoFilter filter){
			
		if(user==null || filter == null){return null;}
		filter.setAlias("organizer", "organizer");
		return filter.daoEqualityParam("organizer.id",user.getId());	

	}
	/**
	 * return a dao param that encodes the logic for determining whether an appointment includes a particular user
	 * as an attendee
	 * @param user
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getAttendeesParam(AuthUser user, LavaDaoFilter filter){
			
		if(user==null || filter == null){return null;}
		filter.setAlias("attendees", "attendees");
		filter.setAlias("attendees.user", "attendee_user");
		return filter.daoEqualityParam("attendee_user.id",user.getId());	
		}
	}