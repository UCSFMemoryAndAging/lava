package edu.ucsf.lava.core.calendar.controller;

import java.sql.Time;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.home.model.PreferenceUtils;
import edu.ucsf.lava.core.type.DateRange;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.core.view.model.RenderParams;

public class CalendarHandlerUtils {
	

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
	public static final String PREFERENCE_CONTEXT = "calendar";
	
	
	public static List<String> getDefaultEvents() {
		return Arrays.asList(DISPLAY_YEAR_EVENT,DISPLAY_MONTH_EVENT,DISPLAY_WEEK_EVENT,DISPLAY_DAY_EVENT,DISPLAY_NOW_EVENT,
				DISPLAY_ALL_EVENT,NEXT_DATE_RANGE_EVENT,PREV_DATE_RANGE_EVENT,SHOW_FULLDAY_EVENT,SHOW_WORKDAY_EVENT);
	}

	
	public static void handleCustomDateFilter(LavaDaoFilter filter, String startDateParam, String endDateParam){
		Date startDate = CalendarDaoUtils.convertParamToDate(filter.getParam(CalendarDaoUtils.CUSTOM_DATE_FILTER_START_PARAM));
		if(startDate == null){
			return;
		}
		Date endDate = CalendarDaoUtils.convertParamToDate(filter.getParam(CalendarDaoUtils.CUSTOM_DATE_FILTER_END_PARAM),startDate,startDate);
		
		filter.setParam(startDateParam, startDate);
		filter.setParam(endDateParam, endDate);
		filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM, CalendarDaoUtils.DISPLAY_RANGE_CUSTOM);
	}
	
	
	public static boolean missingDateFilterParams(LavaDaoFilter filter, String startDateParam, String endDateParam){
		if((!CalendarDaoUtils.DISPLAY_RANGE_ALL.equalsIgnoreCase(filter.getParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM) != null ? filter.getParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM).toString():"")) && 
		(filter.getParam(startDateParam)==null || filter.getParam(endDateParam)==null)){
			return true;
		}
		return false;
	}
	
	
	public static void setDefaultDayLengthParam(LavaDaoFilter filter,  String defaultDayLength){
		// set to value in list of preferences
		if (filter.getAuthUser()!=null){
			defaultDayLength = PreferenceUtils.getPrefValue(filter.getAuthUser(), PREFERENCE_CONTEXT, CalendarDaoUtils.SHOW_DAYLENGTH_PARAM, defaultDayLength);
		}
		filter.setParam(CalendarDaoUtils.SHOW_DAYLENGTH_PARAM, defaultDayLength);
	}
	
	public static void setDefaultFilterParams(LavaDaoFilter filter,  String defaultDisplayRange, String startDateParam, String endDateParam){
		// set to value in list of preferences
		if (filter.getAuthUser()!=null){
			defaultDisplayRange = PreferenceUtils.getPrefValue(filter.getAuthUser(), PREFERENCE_CONTEXT, CalendarDaoUtils.DISPLAY_RANGE_PARAM, defaultDisplayRange);
		}
		
		filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM,defaultDisplayRange);
		if(defaultDisplayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_YEAR)){
			filter.setParam(startDateParam,LavaDateUtils.getYearStartDate(new Date()));
			filter.setParam(endDateParam,LavaDateUtils.getYearEndDate(new Date()));
		}else if(defaultDisplayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
			filter.setParam(startDateParam,LavaDateUtils.getMonthStartDate(new Date()));
			filter.setParam(endDateParam,LavaDateUtils.getMonthEndDate(new Date()));
		}else if (defaultDisplayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_WEEK)){
			filter.setParam(startDateParam,LavaDateUtils.getWeekStartDate(new Date()));
			filter.setParam(endDateParam,LavaDateUtils.getWeekEndDate(new Date()));
		}else if (defaultDisplayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_DAY)){
			filter.setParam(startDateParam,LavaDateUtils.getDayStartDate(new Date()));
			filter.setParam(endDateParam,LavaDateUtils.getDayEndDate(new Date()));
		}else if (defaultDisplayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_ALL)){
			filter.setParam(startDateParam,null);
			filter.setParam(endDateParam,null);	
		}
		
		
		
	}
	

	public static LavaDaoFilter setDayLengthParam(RequestContext context, LavaDaoFilter filter, String defaultDayLength){
		Map params = filter.getParams();
		String event = ActionUtils.getEventName(context);
		//Determine daylength to use  based on current event or current state of param
		if (ActionUtils.getEventName(context).equalsIgnoreCase(SHOW_FULLDAY_EVENT)){
			filter.setParam(CalendarDaoUtils.SHOW_DAYLENGTH_PARAM,CalendarDaoUtils.SHOW_DAYLENGTH_FULLDAY);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(SHOW_WORKDAY_EVENT)){
			filter.setParam(CalendarDaoUtils.SHOW_DAYLENGTH_PARAM,CalendarDaoUtils.SHOW_DAYLENGTH_WORKDAY);
		} else if (params.get(CalendarDaoUtils.SHOW_DAYLENGTH_PARAM) == null){
			// set to value in list of preferences
			if (filter.getAuthUser()!=null){
				defaultDayLength = PreferenceUtils.getPrefValue(filter.getAuthUser(), PREFERENCE_CONTEXT, CalendarDaoUtils.SHOW_DAYLENGTH_PARAM, defaultDayLength);
			}
			filter.setParam(CalendarDaoUtils.SHOW_DAYLENGTH_PARAM,defaultDayLength);
		}
		
		// set user preference
		if (filter.getAuthUser()!=null){
			PreferenceUtils.setPrefValue(filter.getAuthUser(), PREFERENCE_CONTEXT, CalendarDaoUtils.SHOW_DAYLENGTH_PARAM, params.get(CalendarDaoUtils.SHOW_DAYLENGTH_PARAM).toString());
		}
		return filter;
	}
	
	public static LavaDaoFilter setDateFilterParams(RequestContext context, LavaDaoFilter filter, String defaultDisplayRange, String startDateParam, String endDateParam)
	{
		Map params = filter.getParams();
		String displayRange;
		String event = ActionUtils.getEventName(context);
		//Determine display range based on current event or current state of param
		if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_ALL_EVENT)){
			filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM,CalendarDaoUtils.DISPLAY_RANGE_ALL);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_YEAR_EVENT)){
			filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM,CalendarDaoUtils.DISPLAY_RANGE_YEAR);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_MONTH_EVENT)){
			filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM,CalendarDaoUtils.DISPLAY_RANGE_MONTH);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_WEEK_EVENT)){
			filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM,CalendarDaoUtils.DISPLAY_RANGE_WEEK);
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_DAY_EVENT)){
			filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM,CalendarDaoUtils.DISPLAY_RANGE_DAY);
		} else if (params.get(CalendarDaoUtils.DISPLAY_RANGE_PARAM) == null){
			// set to value in list of preferences
			if (filter.getAuthUser()!=null){
				defaultDisplayRange = PreferenceUtils.getPrefValue(filter.getAuthUser(), PREFERENCE_CONTEXT, CalendarDaoUtils.DISPLAY_RANGE_PARAM, defaultDisplayRange);
			}
			filter.setParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM,defaultDisplayRange);
		} 

		displayRange = params.get(CalendarDaoUtils.DISPLAY_RANGE_PARAM).toString();
		if (!displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_CUSTOM)){
			//clear custom date filter fields if using a standard range
			filter.setParam(CalendarDaoUtils.CUSTOM_DATE_FILTER_START_PARAM,null);
			filter.setParam(CalendarDaoUtils.CUSTOM_DATE_FILTER_END_PARAM,null);
			// set user preference
			if (filter.getAuthUser()!=null){
				PreferenceUtils.setPrefValue(filter.getAuthUser(), PREFERENCE_CONTEXT, CalendarDaoUtils.DISPLAY_RANGE_PARAM, displayRange);
			}
		}
		
		
		
		/*determine the "pivot date" using the following preference order
		 1) selected date parameter from request url
		 2) current date if within display range
		 3) start date filter param
		 4) current date
		*/
		Date pivotDate=null;
		Date currentDate = new Date();
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		if(request.getParameterMap().containsKey("selectedDate")){
			pivotDate = LavaDateUtils.getDate(request.getParameter("selectedDate"));
		}
		if (pivotDate==null){
			Date startDate = CalendarDaoUtils.convertParamToDate(params.get(startDateParam),currentDate,currentDate);
			Date endDate = CalendarDaoUtils.convertParamToDate(params.get(endDateParam),currentDate,currentDate);
			if (new DateRange(startDate, endDate).contains(currentDate)){
				pivotDate = currentDate;
			} else {
				pivotDate = startDate;
			}
		}

		//now modify the pivot date based on the action
		if (ActionUtils.getEventName(context).equalsIgnoreCase(DISPLAY_NOW_EVENT)){
			pivotDate = currentDate;
		}else if (ActionUtils.getEventName(context).equalsIgnoreCase(NEXT_DATE_RANGE_EVENT)){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(pivotDate);
			if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_YEAR)){
				calendar.add(Calendar.YEAR,1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
				calendar.add(Calendar.MONTH,1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_WEEK) ){
				calendar.add(Calendar.WEEK_OF_MONTH,1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_DAY)){
				calendar.add(Calendar.DAY_OF_MONTH,1);
				pivotDate = calendar.getTime();
			}	
		}else if (ActionUtils.getEventName(context).equalsIgnoreCase(PREV_DATE_RANGE_EVENT)){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(pivotDate);
			if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_YEAR)){
				calendar.add(Calendar.YEAR,-1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
				calendar.add(Calendar.MONTH,-1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_WEEK) ){
				calendar.add(Calendar.WEEK_OF_MONTH,-1);
				pivotDate = calendar.getTime();
			}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_DAY)){
				calendar.add(Calendar.DAY_OF_MONTH,-1);
				pivotDate = calendar.getTime();
			}	
		}
		
		//now set actual date filter params based on pivotdate and date range
		if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_ALL)){
			filter.setParam(startDateParam,null);
			filter.setParam(endDateParam,null);
		}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_YEAR)){
			filter.setParam(startDateParam,LavaDateUtils.getYearStartDate(pivotDate));
			filter.setParam(endDateParam,LavaDateUtils.getYearEndDate(pivotDate));
		}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
			filter.setParam(startDateParam,LavaDateUtils.getMonthStartDate(pivotDate));
			filter.setParam(endDateParam,LavaDateUtils.getMonthEndDate(pivotDate));
		}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_WEEK)){
			filter.setParam(startDateParam,LavaDateUtils.getWeekStartDate(pivotDate));
			filter.setParam(endDateParam,LavaDateUtils.getWeekEndDate(pivotDate));
		}else if(displayRange.equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_DAY)){
			filter.setParam(startDateParam,LavaDateUtils.getDayStartDate(pivotDate));
			filter.setParam(endDateParam,LavaDateUtils.getDayEndDate(pivotDate));
		}
		return filter;
	}
	
}
