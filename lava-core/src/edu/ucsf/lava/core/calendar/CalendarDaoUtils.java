package edu.ucsf.lava.core.calendar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.type.DateRange;
import edu.ucsf.lava.core.type.LavaDateUtils;

public class CalendarDaoUtils {

	public static final String DISPLAY_RANGE_YEAR = "Year";
	public static final String DISPLAY_RANGE_MONTH = "Month";
	public static final String DISPLAY_RANGE_WEEK = "Week";
	public static final String DISPLAY_RANGE_DAY = "Day";
	public static final String DISPLAY_RANGE_ALL = "All";
	public static final String DISPLAY_RANGE_CUSTOM = "Custom";
	public static final String SHOW_DAYLENGTH_FULLDAY = "Full Day";
	public static final String SHOW_DAYLENGTH_WORKDAY = "Work Day";
	
	public static final String SHOW_DAYLENGTH_PARAM = "showDayLength";
	public static final String DISPLAY_RANGE_PARAM = "displayRange";
	public static final String CUSTOM_DATE_FILTER_START_PARAM = "customDateStart";
	public static final String CUSTOM_DATE_FILTER_END_PARAM = "customDateEnd";
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
	
	// this method is useful when the custom date params, i.e. those used in the view to 
	// display the date range selected, should always reflect the date range, even when 
	// quick date selection is used (i.e. choosing day, week, month..). the calendar
	// list handlers and views consider these mutually exclusive; when the user filters by
	// custom dates the quick date selections are ignored (and hidden) and when quick
	// date selection is used, it is not reflected in the custom dates. however, for other
	// applications like the report handlers, the custom dates and the quick date selection
	// work in concert, so when the date range is set, regardless of whether it was set via
	// the custom date fields or by quick selection, the date range is reflected in the 
	// custom date fields
	
	// pass true for dateTime to format string with date and time, pass false to format just with date
	public static LavaDaoFilter updateCustomParamsFromDateParams(LavaDaoFilter filter, String startDateParam, String endDateParam, Boolean dateTime) {
		Date startDate, endDate;
		// for datetime dates, use "h:mma" for time instead of the default "h:mm a" (DateFormat.SHORT) because the CalendarPopup
		// control returns date time as "h:mma"
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat();
		dateTimeFormat.applyPattern("M/d/yy h:mma");
		if ((startDate = (Date) filter.getParam(startDateParam)) != null) {
			if (dateTime) {
				filter.setParam(CUSTOM_DATE_FILTER_START_PARAM, dateTimeFormat.format(startDate));
			}
			else { 
				filter.setParam(CUSTOM_DATE_FILTER_START_PARAM, DateFormat.getDateInstance(DateFormat.SHORT).format(startDate));
			}
		}
		
		if ((endDate = (Date) filter.getParam(endDateParam)) != null) {
			if (dateTime) {
				filter.setParam(CUSTOM_DATE_FILTER_END_PARAM, dateTimeFormat.format(endDate));
			}
			else {
				filter.setParam(CUSTOM_DATE_FILTER_END_PARAM, DateFormat.getDateInstance(DateFormat.SHORT).format(endDate));
			}
		}
		
		return filter;
	}
	
	public static Date convertParamToDate(Object paramValue){
		return convertParamToDate(paramValue,null,null);
	}
	
	public static Date convertParamToDate(Object paramValue, Date useWhenNull){
		return convertParamToDate(paramValue,useWhenNull,null);
	}
	
	public static Date convertParamToDate(Object paramValue, Date useWhenNull, Date useWhenUnparseable){
		// try converting String to datetime first in case the property is a datetime. if that fails, then just
		// convert to date 
		if (paramValue==null){
			return useWhenNull;
		}
		if(Date.class.isInstance(paramValue)){
			return (Date) paramValue;
		}
		// use "h:mma" for time instead of the default "h:mm a" (DateFormat.SHORT) because the CalendarPopup
		// control returns date time as "h:mma"
		Date newDate = LavaDateUtils.getDate((String)paramValue, "M/d/yy h:mma");
		if (newDate == null){
			newDate = LavaDateUtils.getDate((String)paramValue);
			if (newDate == null){
				return useWhenUnparseable;
			}
		}
		return newDate;

	}
	
	
	}