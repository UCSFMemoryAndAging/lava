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
	 * return a complex dao param that encodes the logic for comparing a date range to start and end date properties
	 * 
	 * @param range
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getDateRangeParam(String startDateProp, String startTimeProp,
			String endDateProp, String endTimeProp, DateRange range, LavaDaoFilter filter){
			
		if(range==null || filter == null){return null;}
		
		return filter.daoOr(
							filter.daoAnd(
									filter.daoAnd(
											filter.daoGreaterThanOrEqualParam(startDateProp, LavaDateUtils.getDatePart(range.getStart())),
										    filter.daoGreaterThanOrEqualParam(startTimeProp, LavaDateUtils.getTimePart(range.getStart()))
								    ),
								    filter.daoAnd(
											filter.daoLessThanParam(startDateProp, LavaDateUtils.getDatePart(range.getEnd())),
									        filter.daoLessThanParam(startTimeProp, LavaDateUtils.getTimePart(range.getEnd()))
									)
							),
							filter.daoAnd(
									filter.daoAnd(
											filter.daoLessThanOrEqualParam(endDateProp, LavaDateUtils.getDatePart(range.getEnd())),
										    filter.daoLessThanOrEqualParam(endTimeProp, LavaDateUtils.getTimePart(range.getEnd()))
								    ),
								    filter.daoAnd(
											filter.daoGreaterThanParam(endDateProp, LavaDateUtils.getDatePart(range.getStart())),
									        filter.daoGreaterThanParam(endTimeProp, LavaDateUtils.getTimePart(range.getStart()))
									)
								)
						);
		}
	

	/**
	 * Convenience function with default start and end date param names. 
	 * @param range
	 * @param filter
	 * @return
	 */
	public static LavaDaoParam getDateRangeParam(DateRange range, LavaDaoFilter filter){
		return getDateRangeParam(START_DATE_PARAM,START_TIME_PARAM,END_DATE_PARAM,END_TIME_PARAM,range,filter);
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