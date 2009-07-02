package edu.ucsf.lava.core.calendar;

import java.util.Date;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.type.DateRange;

public class CalendarDaoUtils {

	
	public static final String START_TIME_PARAM = "startTime";
	public static final String END_TIME_PARAM = "endTime";
	
	/**
	 * return a complex dao param that encodes the logic for comparing a date range to start and end date properties
	 * 
	 * @param range
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getDateRangeParam(String startTimeProp, String endTimeProp, DateRange range, LavaDaoFilter filter){
			
		if(range==null || filter == null){return null;}
		
		return filter.daoOr(
							filter.daoAnd(
									filter.daoGreaterThanOrEqualParam(startTimeProp, range.getStart()),
									filter.daoLessThanParam(startTimeProp, range.getEnd())),
							filter.daoAnd(
									filter.daoLessThanOrEqualParam(endTimeProp, range.getEnd()),
									filter.daoGreaterThanParam(endTimeProp, range.getStart()))
						    );
		}
	

	/**
	 * Convenience function with default start and end date param names. 
	 * @param range
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getDateRangeParam(DateRange range, LavaDaoFilter filter){
		return getDateRangeParam(START_TIME_PARAM,END_TIME_PARAM,range,filter);
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
		filter.setAlias("attendees.attendee", "attendee");
		filter.setAlias("attendee.user", "attendee_user");
		return filter.daoEqualityParam("attendee_user.id",user.getId());	
		}
	}