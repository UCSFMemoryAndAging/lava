package edu.ucsf.lava.core.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;

public class CalendarHandlerUtils {
	public static final String DISPLAY_RANGE_PARAM = "displayRange";
	public static final String DISPLAY_RANGE_YEAR = "Year";
	public static final String DISPLAY_RANGE_MONTH = "Month";
	public static final String DISPLAY_RANGE_WEEK = "Week";
	public static final String DISPLAY_RANGE_DAY = "Day";
	public static final String DISPLAY_RANGE_ALL = "All";
	public static final String DISPLAY_RANGE_CUSTOM = "Custom";
	public static final String SHOW_DAYLENGTH_PARAM = "showDayLength";
	public static final String SHOW_DAYLENGTH_FULLDAY = "Full Day";
	public static final String SHOW_DAYLENGTH_WORKDAY = "Work Day";
	
	public static final String DISPLAY_YEAR_EVENT = "displayYear";
	public static final String DISPLAY_MONTH_EVENT = "displayMonth";
	public static final String DISPLAY_WEEK_EVENT = "displayWeek";
	public static final String DISPLAY_DAY_EVENT = "displayDay";
	public static final String DISPLAY_NOW_EVENT = "displayNow";
	public static final String DISPLAY_ALL_EVENT = "displayAll";
	public static final String SHOW_FULLDAY_EVENT = "showFullDay";
	public static final String SHOW_WORKDAY_EVENT = "showWorkDay";
	
	

	public static final String NEXT_DATE_RANGE_EVENT = "nextDateRange";
	public static final String PREV_DATE_RANGE_EVENT = "prevDateRange";
	
	public static final String CUSTOM_DATE_FILTER_START_PARAM = "customDateStart";
	public static final String CUSTOM_DATE_FILTER_END_PARAM = "customDateEnd";

	
	public static List getDefaultEvents() {
		return Arrays.asList(new String[]{DISPLAY_YEAR_EVENT,DISPLAY_MONTH_EVENT,DISPLAY_WEEK_EVENT,DISPLAY_DAY_EVENT,DISPLAY_NOW_EVENT,
				DISPLAY_ALL_EVENT,NEXT_DATE_RANGE_EVENT,PREV_DATE_RANGE_EVENT,SHOW_FULLDAY_EVENT,SHOW_WORKDAY_EVENT});
	}

	
	public static void handleCustomDateFilter(LavaDaoFilter filter, String startDateParam, String endDateParam){
		Date startDate = CalendarHandlerUtils.convertParamToDate(filter.getParam(CUSTOM_DATE_FILTER_START_PARAM));
		if(startDate == null){
			return;
		}
		Date endDate = CalendarHandlerUtils.convertParamToDate(filter.getParam(CUSTOM_DATE_FILTER_END_PARAM),startDate,startDate);
		
		filter.setParam(startDateParam, startDate);
		filter.setParam(endDateParam, endDate);
		filter.setParam(DISPLAY_RANGE_PARAM, DISPLAY_RANGE_CUSTOM);
	}
	
	
	public static boolean missingDateFilterParams(LavaDaoFilter filter, String startDateParam, String endDateParam){
		if((!DISPLAY_RANGE_ALL.equalsIgnoreCase(filter.getParam(DISPLAY_RANGE_PARAM) != null ? filter.getParam(DISPLAY_RANGE_PARAM).toString():"")) && 
		(filter.getParam(startDateParam)==null || filter.getParam(endDateParam)==null)){
			return true;
		}
		return false;
	}
	
	
	public static void setDefaultDayLengthParam(LavaDaoFilter filter,  String defaultDayLength){
		filter.setParam(SHOW_DAYLENGTH_PARAM, defaultDayLength);
	}
	
	public static void setDefaultFilterParams(LavaDaoFilter filter,  String defaultDisplayRange, String startDateParam, String endDateParam){

		filter.setParam(DISPLAY_RANGE_PARAM,defaultDisplayRange);
		if(defaultDisplayRange.equalsIgnoreCase(DISPLAY_RANGE_YEAR)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getYearStartDate(new Date()));
			filter.setParam(endDateParam,CalendarHandlerUtils.getYearEndDate(new Date()));
		}else if(defaultDisplayRange.equalsIgnoreCase(DISPLAY_RANGE_MONTH)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getMonthStartDate(new Date()));
			filter.setParam(endDateParam,CalendarHandlerUtils.getMonthEndDate(new Date()));
		}else if (defaultDisplayRange.equalsIgnoreCase(DISPLAY_RANGE_WEEK)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getWeekStartDate(new Date()));
			filter.setParam(endDateParam,CalendarHandlerUtils.getWeekEndDate(new Date()));
		}else if (defaultDisplayRange.equalsIgnoreCase(DISPLAY_RANGE_DAY)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getDayStartDate(new Date()));
			filter.setParam(endDateParam,CalendarHandlerUtils.getDayEndDate(new Date()));
		}else if (defaultDisplayRange.equalsIgnoreCase(DISPLAY_RANGE_ALL)){
			filter.setParam(startDateParam,null);
			filter.setParam(endDateParam,null);	
		}
		
		
		
	}
	

	public static LavaDaoFilter setDayLengthParam(RequestContext context, LavaDaoFilter filter, String defaultDayLength)
	{
		Map params = filter.getParams();
		String event = ActionUtils.getEventName(context);
		//Determine daylength to use  based on current event or current state of param.  default is  work day 
		if (ActionUtils.getEventName(context).equalsIgnoreCase(SHOW_FULLDAY_EVENT)){
			filter.setParam(SHOW_DAYLENGTH_PARAM,SHOW_DAYLENGTH_FULLDAY);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(SHOW_WORKDAY_EVENT)){
			filter.setParam(SHOW_DAYLENGTH_PARAM,SHOW_DAYLENGTH_WORKDAY);
		} else if (params.get(SHOW_DAYLENGTH_PARAM) == null){
			filter.setParam(SHOW_DAYLENGTH_PARAM,defaultDayLength);
		} 
		return filter;
	}
	
	public static LavaDaoFilter setDateFilterParams(RequestContext context, LavaDaoFilter filter, String defaultDisplayRange, String startDateParam, String endDateParam)
	{
		Map params = filter.getParams();
		Date pivotDate;
		String displayRange;
		String event = ActionUtils.getEventName(context);
		//Determine display range based on current event or current state of param.  default is month
		if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_ALL_EVENT)){
			filter.setParam(DISPLAY_RANGE_PARAM,DISPLAY_RANGE_ALL);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_YEAR_EVENT)){
			filter.setParam(DISPLAY_RANGE_PARAM,DISPLAY_RANGE_YEAR);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_MONTH_EVENT)){
			filter.setParam(DISPLAY_RANGE_PARAM,DISPLAY_RANGE_MONTH);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_WEEK_EVENT)){
			filter.setParam(DISPLAY_RANGE_PARAM,DISPLAY_RANGE_WEEK);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_DAY_EVENT)){
			filter.setParam(DISPLAY_RANGE_PARAM,DISPLAY_RANGE_DAY);
		} else if (params.get(DISPLAY_RANGE_PARAM) == null){
			filter.setParam(DISPLAY_RANGE_PARAM,defaultDisplayRange);
		} 

		displayRange = params.get(DISPLAY_RANGE_PARAM).toString();
		//clear custom date filter fields if using a standard range
		if (!displayRange.equalsIgnoreCase(DISPLAY_RANGE_CUSTOM)){
			filter.setParam(CUSTOM_DATE_FILTER_START_PARAM,null);
			filter.setParam(CUSTOM_DATE_FILTER_END_PARAM,null);
		}
		
		
		
		//determine the "pivot date" based on the event, or the current start date param.  Default is current date
		//first attempt to get current start date
		pivotDate = CalendarHandlerUtils.convertParamToDate(params.get(startDateParam),new Date(),new Date());
		
		//now modify the pivot date based on the action
		if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_NOW_EVENT)){
			pivotDate = new Date();
		}else if (ActionUtils.getEventName(context).equalsIgnoreCase(NEXT_DATE_RANGE_EVENT)){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(pivotDate);
			if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_YEAR)){
				calendar.add(Calendar.YEAR,1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_MONTH)){
				calendar.add(Calendar.MONTH,1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_WEEK) ){
				calendar.add(Calendar.WEEK_OF_MONTH,1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_DAY)){
				calendar.add(Calendar.DAY_OF_MONTH,1);
				pivotDate = calendar.getTime();
			}	
		}else if (ActionUtils.getEventName(context).equalsIgnoreCase(PREV_DATE_RANGE_EVENT)){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(pivotDate);
			if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_YEAR)){
				calendar.add(Calendar.YEAR,-1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_MONTH)){
				calendar.add(Calendar.MONTH,-1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_WEEK) ){
				calendar.add(Calendar.WEEK_OF_MONTH,-1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_DAY)){
				calendar.add(Calendar.DAY_OF_MONTH,-1);
				pivotDate = calendar.getTime();
			}	
		}
		
		
		//now set actual date filter params based on pivotdate and date range
		if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_ALL)){
			filter.setParam(startDateParam,null);
			filter.setParam(endDateParam,null);
		}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_YEAR)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getYearStartDate(pivotDate));
			filter.setParam(endDateParam,CalendarHandlerUtils.getYearEndDate(pivotDate));
		}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_MONTH)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getMonthStartDate(pivotDate));
			filter.setParam(endDateParam,CalendarHandlerUtils.getMonthEndDate(pivotDate));
		}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_WEEK)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getWeekStartDate(pivotDate));
			filter.setParam(endDateParam,CalendarHandlerUtils.getWeekEndDate(pivotDate));
		}else if(displayRange.equalsIgnoreCase(DISPLAY_RANGE_DAY)){
			filter.setParam(startDateParam,CalendarHandlerUtils.getDayStartDate(pivotDate));
			filter.setParam(endDateParam,CalendarHandlerUtils.getDayEndDate(pivotDate));
		}
		return filter;
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
			

	public static Date getYearStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_YEAR,1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}
	
	public static Date getYearEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_YEAR,calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

	public static Date getMonthStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}
	
	public static Date getMonthEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

	public static Date getWorkWeekStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}

	public static Date getWorkWeekEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.FRIDAY);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

	public static Date getWeekStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}

	public static Date getWeekEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.DAY_OF_WEEK,Calendar.SATURDAY);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

	public static Date getDayStartDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		calendar.set(Calendar.MINUTE,0);
		calendar.set(Calendar.SECOND,0);
		return calendar.getTime();
	}

	public static Date getDayEndDate(Date pivotDate){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pivotDate);
		calendar.set(Calendar.HOUR_OF_DAY,23);
		calendar.set(Calendar.MINUTE,59);
		calendar.set(Calendar.SECOND,59);
		return calendar.getTime();
	}

	private static Date convertParamToDate(Object paramValue){
		return CalendarHandlerUtils.convertParamToDate(paramValue,null,null);
	}
	
	private static Date convertParamToDate(Object paramValue, Date useWhenNull){
		return CalendarHandlerUtils.convertParamToDate(paramValue,useWhenNull,null);
	}
	
	private static Date convertParamToDate(Object paramValue, Date useWhenNull, Date useWhenUnparseable){
		
		if (paramValue==null){
			return useWhenNull;
		}
		if(Date.class.isInstance(paramValue)){
			return (Date) paramValue;
		}
		// try converting String to datetime first in case the property is a datetime. if that fails, then just
		// convert to date 
		try{
			// use "h:mma" for time instead of the default "h:mm a" (DateFormat.SHORT) because the CalendarPopup
			// control returns date time as "h:mma"
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat();
			dateTimeFormat.applyPattern("M/d/yy h:mma");
			Date newDate = dateTimeFormat.parse(paramValue.toString());
			return newDate;
		}catch(Exception e){}
		try {
			Date newDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(paramValue.toString());
			return newDate;
		}catch(Exception e){
			return useWhenUnparseable;
		}
	}
	
}
