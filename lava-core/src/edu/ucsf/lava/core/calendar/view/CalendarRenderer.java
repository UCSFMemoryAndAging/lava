package edu.ucsf.lava.core.calendar.view;

import java.awt.Color;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;

import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.calendar.controller.CalendarHandlerUtils;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.ResourceCalendar;
import edu.ucsf.lava.core.dao.LavaDateRangeOverlapParamHandler;
import edu.ucsf.lava.core.type.DateRange;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.core.view.model.BaseRenderer;
import edu.ucsf.lava.core.view.model.BaseRenderObject;
import edu.ucsf.lava.core.view.model.CssBox;
import edu.ucsf.lava.core.view.model.InvalidRendererAttributeException;
import edu.ucsf.lava.core.view.model.LavaBorderStyle;
import edu.ucsf.lava.core.view.model.RenderParams;

public class CalendarRenderer extends BaseRenderer {

	public CalendarRenderer() {
		super();
		this.addHandledEntityClass(Calendar.class);
		this.addHandledRenderObjectClass(CssBox.class);
	}

	public List<BaseRenderObject> createRenderObjects(Object entity, Class renderObjectClass, RenderParams renderParams) throws Exception {

		renderParams.checkRequiredParams();
		List<BaseRenderObject> renderObjects = new ArrayList<BaseRenderObject>();

		// retrieve render parameters, including the list of appointments/contents to display
		Date dateRangeStart = (Date)renderParams.getRenderParam("calendarDateStart");
		String displayRange = (String)renderParams.getRenderParam("calendarDateRange");
		String showDayLength = (String)renderParams.getRenderParam("dayLength");
		List apptIdList = (List)renderParams.getRenderParam("apptIdList");
		List apptContentList = (List)renderParams.getRenderParam("apptContentList");
		List apptUrlList = (List)renderParams.getRenderParam("apptUrlList");
		String dayClickEventUrl = (String)renderParams.getRenderParam("dayClickEventUrl");
		String timeClickEventUrl = (String)renderParams.getRenderParam("timeClickEventUrl");


		// determine begin and end time of viewable day area (nearest hour boundary)
		Calendar calendar = (Calendar)entity;
		Time dayViewStart;
		Time dayViewEnd;
		if (showDayLength.contentEquals(CalendarDaoUtils.SHOW_DAYLENGTH_WORKDAY)){
			dayViewStart = LavaDateUtils.getHourBeginTime(calendar.getWorkBeginTime());
			Integer endTimeMinutes = LavaDateUtils.toCalendar(calendar.getWorkEndTime()).get(java.util.Calendar.MINUTE);
			if (endTimeMinutes.equals(Integer.valueOf(0))){
				dayViewEnd = calendar.getWorkEndTime();
			} else {
				dayViewEnd = LavaDateUtils.getHourEndTime(calendar.getWorkEndTime());
			}
		} else {
			dayViewStart = LavaDateUtils.getDayStartTime();
			dayViewEnd = LavaDateUtils.getDayEndTime();
		}
		DateRange viewableTimeRange = new DateRange(dateRangeStart,dayViewStart,dateRangeStart,dayViewEnd);
		Integer viewableDays;
		Boolean displayDayHeader;
		Boolean displayTimeLabels;
		if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_WEEK)){
			viewableDays = 7;
			displayDayHeader = true;
			displayTimeLabels = true;
		} else if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
			viewableDays = LavaDateUtils.getDaysinMonth(dateRangeStart);
			displayDayHeader = true;
			displayTimeLabels = false;
		} else {
			//defaults to day view
			viewableDays = 1;
			displayDayHeader = false;
			displayTimeLabels = true;
		}

		// calculate calendar height dimensions
		Integer calendarHeightPerHour = null;
		Integer calendarContentHeight = null;
		Integer dayHeaderHeight = 0;
		if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_DAY) ||
				displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_WEEK)){
			// for day/week view, calendar height is dependent on hour box height and viewable range for day
			calendarHeightPerHour = intAttr("timeIntervalBoxesPerHour")*intAttr("timeIntervalBoxHeight");
			calendarContentHeight = (int)(calendarHeightPerHour * Math.round(((double)viewableTimeRange.getRangeInMinutes())/60.0));
		} else {
			// for month view, calendar height is dependent on number of viewable weeks
			java.util.Calendar cal = java.util.Calendar.getInstance();
			cal.setTime(dateRangeStart);
			Integer weekBegin = cal.get(java.util.Calendar.WEEK_OF_MONTH);
			cal.setTime(LavaDateUtils.addDays(dateRangeStart, viewableDays-1));
			Integer weekEnd = cal.get(java.util.Calendar.WEEK_OF_MONTH);
			calendarContentHeight = (weekEnd-weekBegin+1)*intAttr("monthDayBoxHeight");
		}
		if (displayDayHeader){
			if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_WEEK)){
				dayHeaderHeight = intAttr("weekDayHeaderBoxHeight");
			} else if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
				dayHeaderHeight = intAttr("monthDayHeaderBoxHeight");
			}
		}
		if (intAttr("calendarMaxHeight")!=null){
			calendarContentHeight = calendarContentHeight + intAttr("scrollBarSpacer");
			if (calendarContentHeight + dayHeaderHeight > intAttr("calendarMaxHeight")){
				calendarContentHeight = intAttr("calendarMaxHeight") - dayHeaderHeight;
			}
		}
		Integer calendarHeight = calendarContentHeight + dayHeaderHeight;

		// calculate calendar width dimensions
		Integer labelWidth = displayTimeLabels ? intAttr("timeLabelBoxWidth") : 0;
		Integer scrollBarWidth = intAttr("calendarMaxHeight")==null ?  0 : intAttr("scrollBarSpacer");
		Integer usableCalendarWidth = intAttr("calendarMaxWidth") - labelWidth - scrollBarWidth;
		Integer dayBoxWidth;
		if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_DAY)){
			dayBoxWidth = usableCalendarWidth;
		} else {
			dayBoxWidth = usableCalendarWidth/7;
			usableCalendarWidth = dayBoxWidth * 7; // shrinks to a width divisible by 7
		}
		Integer calendarWidth = usableCalendarWidth + scrollBarWidth + labelWidth;
		
		// main calendar box
		CssBox calendarBox = new CssBox();
		calendarBox.setClassStr(strAttr("calendarBoxClass"));
		calendarBox.setPositionAttribute("relative");
		calendarBox.setMargin(intAttr("calendarBoxMargin"));
		calendarBox.setPadding(intAttr("calendarBoxPadding"));
		calendarBox.setBorderWidth(intAttr("calendarBoxBorderWidth"));
		calendarBox.setHeight(calendarHeight);
		calendarBox.setWidth(calendarWidth);
		
		// calendar content box
		// (all calendar content except the header is nested within this box)
		CssBox calendarContentBox = new CssBox();
		calendarContentBox.setContentOverflow(intAttr("calendarMaxHeight")==null ? "hidden" : "scroll");
		calendarContentBox.setX(0);
		calendarContentBox.setY(dayHeaderHeight);
		calendarContentBox.setWidth(calendarWidth);
		calendarContentBox.setHeight(calendarContentHeight);
		
		
		// Css box templates
		
		// day header box template
		CssBox dayHeaderBox = new CssBox();
		if (displayDayHeader){
			if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_WEEK)){
				dayHeaderBox.setClassStr(strAttr("weekDayHeaderBoxClass"));
				dayHeaderBox.setFloatAttribute("left");
				dayHeaderBox.setMargin(intAttr("weekDayHeaderBoxMargin"));
				dayHeaderBox.setPadding(intAttr("weekDayHeaderBoxPadding"));
				dayHeaderBox.setBorderWidth(intAttr("weekDayHeaderBoxBorderWidth"));
				dayHeaderBox.setBoxWidth(dayBoxWidth);
				dayHeaderBox.setBoxHeight(dayHeaderHeight);
			} else if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
				dayHeaderBox.setClassStr(strAttr("monthDayHeaderBoxClass"));
				dayHeaderBox.setFloatAttribute("left");
				dayHeaderBox.setMargin(intAttr("monthDayHeaderBoxMargin"));
				dayHeaderBox.setPadding(intAttr("monthDayHeaderBoxPadding"));
				dayHeaderBox.setBorderWidth(intAttr("monthDayHeaderBoxBorderWidth"));
				dayHeaderBox.setBoxWidth(dayBoxWidth);
				dayHeaderBox.setBoxHeight(dayHeaderHeight);
			}
		}
	
		// hour and hour label box templates
		CssBox timeIntervalBox = new CssBox();
		if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_DAY) ||
				displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_WEEK)){
			timeIntervalBox.setClassStr(strAttr("timeIntervalBoxClass"));
			timeIntervalBox.setMargin(intAttr("timeIntervalBoxMargin"));
			timeIntervalBox.setPadding(intAttr("timeIntervalBoxPadding"));
			timeIntervalBox.setBorderWidth(intAttr("timeIntervalBoxBorderWidth"));
			timeIntervalBox.setBoxWidth(dayBoxWidth);
			timeIntervalBox.setBoxHeight(intAttr("timeIntervalBoxHeight"));
			timeIntervalBox.setzIndex(5); // places them in front of any background color boxes but behind appointment boxes
		}
		CssBox timeLabelBox = new CssBox();
		CssBox timeLabelHourBox = new CssBox();
		CssBox timeLabelSuffixBox = new CssBox();
		CssBox timeLabelAmPmSuffixBox = new CssBox();
		Integer timeLabelBoxHeight = null;
		if (displayTimeLabels){
			timeLabelBox.setClassStr(strAttr("timeLabelBoxClass"));
			timeLabelBox.setContentOverflow("hidden");
			timeLabelBox.setMargin(intAttr("timeLabelBoxMargin"));
			timeLabelBox.setPadding(intAttr("timeLabelBoxPadding"));
			timeLabelBox.setBorderWidth(intAttr("timeLabelBoxBorderWidth"));
			timeLabelBox.setBoxWidth(intAttr("timeLabelBoxWidth"));
			timeLabelBoxHeight = calendarHeightPerHour/intAttr("timeLabelBoxesPerHour");
			timeLabelBox.setBoxHeight(timeLabelBoxHeight);

			timeLabelHourBox.setClassStr(strAttr("timeLabelHourBoxClass"));
			timeLabelHourBox.setMargin(intAttr("timeLabelHourBoxMargin"));
			timeLabelHourBox.setPadding(intAttr("timeLabelHourBoxPadding"));
			timeLabelHourBox.setBorderWidth(intAttr("timeLabelHourBoxBorderWidth"));

			timeLabelSuffixBox.setClassStr(strAttr("timeLabelSuffixBoxClass"));
			timeLabelSuffixBox.setMargin(intAttr("timeLabelSuffixBoxMargin"));
			timeLabelSuffixBox.setPadding(intAttr("timeLabelSuffixBoxPadding"));
			timeLabelSuffixBox.setBorderWidth(intAttr("timeLabelSuffixBoxBorderWidth"));		

			timeLabelAmPmSuffixBox.setClassStr(strAttr("timeLabelAmPmSuffixBoxClass"));
			timeLabelAmPmSuffixBox.setMargin(intAttr("timeLabelAmPmSuffixBoxMargin"));
			timeLabelAmPmSuffixBox.setPadding(intAttr("timeLabelAmPmSuffixBoxPadding"));
			timeLabelAmPmSuffixBox.setBorderWidth(intAttr("timeLabelAmPmSuffixBoxBorderWidth"));
		}

		// month view box templates
		CssBox monthDayBox = new CssBox();
		CssBox monthDayLabelBox = new CssBox();
		CssBox monthDayContentBox = new CssBox();
		if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
			monthDayBox.setClassStr(strAttr("monthDayBoxClass"));
			monthDayBox.setFloatAttribute("left");
			monthDayBox.setContentOverflow("hidden");
			monthDayBox.setMargin(intAttr("monthDayBoxMargin"));
			monthDayBox.setPadding(intAttr("monthDayBoxPadding"));
			monthDayBox.setBorderWidth(intAttr("monthDayBoxBorderWidth"));
			monthDayBox.setBoxWidth(dayBoxWidth);
			monthDayBox.setBoxHeight(intAttr("monthDayBoxHeight"));
			monthDayBox.setzIndex(0);

			monthDayLabelBox.setClassStr(strAttr("monthDayLabelBoxClass"));
			monthDayLabelBox.setContentOverflow("hidden");
			monthDayLabelBox.setMargin(intAttr("monthDayLabelBoxMargin"));
			monthDayLabelBox.setPadding(intAttr("monthDayLabelBoxPadding"));
			monthDayLabelBox.setBorderWidth(intAttr("monthDayLabelBoxBorderWidth"));
			monthDayLabelBox.setBoxWidth(monthDayBox.getBoxWidth());
			monthDayLabelBox.setBoxHeight(intAttr("monthDayLabelBoxHeight"));
			monthDayBox.setzIndex(1);

			monthDayContentBox.setClassStr(strAttr("monthDayContentBoxClass"));
			monthDayContentBox.setContentOverflow("auto");
			monthDayContentBox.setMargin(intAttr("monthDayContentBoxMargin"));
			monthDayContentBox.setPadding(intAttr("monthDayContentBoxPadding"));
			monthDayContentBox.setBorderWidth(intAttr("monthDayContentBoxBorderWidth"));
			monthDayContentBox.setBoxWidth(monthDayBox.getBoxWidth());
			monthDayContentBox.setBoxHeight(intAttr("monthDayBoxHeight")-intAttr("monthDayLabelBoxHeight"));
			monthDayBox.setzIndex(2);
		}

		// appointment box template
		CssBox apptBox = new CssBox();
		if (!displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){
			apptBox.setClassStr(strAttr("apptBoxClass"));
			apptBox.setMargin(intAttr("apptBoxMargin"));
			apptBox.setPadding(intAttr("apptBoxPadding"));
			apptBox.setBorderWidth(intAttr("apptBoxBorderWidth"));
			apptBox.setBoxWidth(dayBoxWidth);
		}


		
		// loop through each day in viewable range and retrieve appointments
		Map<Integer,List<Appointment>> appts = new HashMap<Integer,List<Appointment>>();
		for (int dayOffset=0;dayOffset<viewableDays;dayOffset++){
			Date currentDay = LavaDateUtils.addDays(dateRangeStart, dayOffset);
			DateRange viewableDayRange = new DateRange(currentDay,dayViewStart,currentDay,dayViewEnd);
			List<Appointment> apptList = calendar.getAppointments(viewableDayRange);

			if (apptIdList == null){
				apptList.clear();
			}
			int idx=0;
			while (apptList.size()>idx){
				if (!apptIdList.contains(apptList.get(idx).getId())){
					// remove any appointments not included in the appointment id list
					apptList.remove(idx);
				} else if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH) &&
						apptList.get(idx).isAfterStart(dayViewStart)){
					// when in month view only show appts with startDate in current day
					apptList.remove(idx);
				}
				idx++;
			}
			Collections.sort(apptList);
			appts.put(dayOffset, apptList);
		}


		// render calendar

		java.util.Calendar cal = java.util.Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("MM/dd/yy");
		String currentDateStyle = strAttr("currentDateStyle");
		String previousDateStyle = strAttr("previousDateStyle");
		// render month view
		if (displayRange.contentEquals(CalendarDaoUtils.DISPLAY_RANGE_MONTH)){

			// day header
			String monthDayHeaderDateFormat = strAttr("monthDayHeaderDateFormat");
			if (displayDayHeader){
				if (displayTimeLabels){
					// spacer for hour labels
					CssBox cssBox = (CssBox)timeLabelBox.getCopy();
					cssBox.setBoxHeight(dayHeaderBox.getBoxHeight());
					calendarBox.addNestedCssBox(cssBox);
				}
				Date weekStart = LavaDateUtils.getWeekStartDate(dateRangeStart);
				for (int dayOffset=0;dayOffset<7;dayOffset++){
					Date headerDay = LavaDateUtils.addDays(weekStart,dayOffset);
					cal.setTime(headerDay);
					CssBox cssBox = (CssBox)dayHeaderBox.getCopy();
					cssBox.setContent(String.format(monthDayHeaderDateFormat, cal));
					calendarBox.addNestedCssBox(cssBox);
				}
			}

			// pad with empty boxes leading up to first of the month
			cal.setTime(dateRangeStart);
			Integer leadingBoxNum = cal.get(java.util.Calendar.DAY_OF_WEEK) - 1;
			for (int i=0;i<leadingBoxNum;i++){
				CssBox cssBox = (CssBox)monthDayBox.getCopy();
				calendarContentBox.addNestedCssBox(cssBox);
			}
			// box for each day of month
			for (int dayOffset=0;dayOffset<viewableDays;dayOffset++){
				Date currentDay = LavaDateUtils.addDays(dateRangeStart, dayOffset);

				// create a CSS box representing the day 
				CssBox cssBox = (CssBox)monthDayBox.getCopy();

				// apply style for today's date
				if (LavaDateUtils.getDatePart(currentDay).equals(LavaDateUtils.getDatePart(new Date()))){
					cssBox.setClassStr(cssBox.getClassStr().concat(" ").concat(currentDateStyle));
				}
				
				// apply style for previous date
				if (LavaDateUtils.getDatePart(currentDay).before(LavaDateUtils.getDatePart(new Date()))){
					cssBox.setClassStr(cssBox.getClassStr().concat(" ").concat(previousDateStyle));
				}

				// add url link for onClick event
				if (dayClickEventUrl!=null){
					StringBuffer urlParams = new StringBuffer();
					urlParams.append(dayClickEventUrl.contains("?") ? "&" : "?");
					urlParams.append("selectedDate=").append(df.format(currentDay));
					cssBox.setUrl(dayClickEventUrl.concat(urlParams.toString()));
				}

				// nested label box for date
				CssBox nestedCssBox = (CssBox)monthDayLabelBox.getCopy();
				nestedCssBox.setContent(String.valueOf(dayOffset+1));
				cssBox.addNestedCssBox(nestedCssBox);

				// nested box for list of appointments
				nestedCssBox = (CssBox)monthDayContentBox.getCopy();
				StringBuffer monthDayContentBoxContent = new StringBuffer();
				if (apptContentList != null){
					for (Appointment appt : appts.get(dayOffset)){
						Integer idx = apptIdList.indexOf(appt.getId());
						if (idx != -1){
							if (apptUrlList!=null){
								monthDayContentBoxContent.append("<a href=\"").append(apptUrlList.get(idx)).append("\">");
								monthDayContentBoxContent.append(apptContentList.get(idx)).append("</a><BR>");
							} else {
								monthDayContentBoxContent.append(apptContentList.get(idx)).append("<BR>");
							}
						}
					}
				}
				nestedCssBox.setContent(monthDayContentBoxContent.toString());
				cssBox.addNestedCssBox(nestedCssBox);

				// add completed dayBox box to main calendar box
				calendarContentBox.addNestedCssBox(cssBox);
			}

			// pad with empty boxes trailing after the last of the month
			cal.setTime(LavaDateUtils.addDays(dateRangeStart, viewableDays-1));
			Integer trailingBoxNum = java.util.Calendar.SATURDAY - cal.get(java.util.Calendar.DAY_OF_WEEK);
			for (int i=0;i<trailingBoxNum;i++){
				CssBox cssBox = (CssBox)monthDayBox.getCopy();
				calendarContentBox.addNestedCssBox(cssBox);
			}

		} else {
			// render day/week view

			// day header
			String weekDayHeaderDateFormat = strAttr("weekDayHeaderDateFormat");
			if (displayDayHeader){
				if (displayTimeLabels){
					// spacer for hour labels
					CssBox cssBox = (CssBox)timeLabelBox.getCopy();
					cssBox.setBoxHeight(dayHeaderBox.getBoxHeight());
					calendarBox.addNestedCssBox(cssBox);
				}
				for (int dayOffset=0;dayOffset<7;dayOffset++){
					Date currentDay = LavaDateUtils.addDays(dateRangeStart,dayOffset);
					cal.setTime(currentDay);
					CssBox cssBox = (CssBox)dayHeaderBox.getCopy();
					cssBox.setContent(String.format(weekDayHeaderDateFormat, cal));

					// apply style for today's date
					if (LavaDateUtils.getDatePart(currentDay).equals(LavaDateUtils.getDatePart(new Date()))){
						cssBox.setClassStr(cssBox.getClassStr().concat(" ").concat(currentDateStyle));
					}
					
					// apply style for previous date
					if (LavaDateUtils.getDatePart(currentDay).before(LavaDateUtils.getDatePart(new Date()))){
						cssBox.setClassStr(cssBox.getClassStr().concat(" ").concat(previousDateStyle));
					}

					calendarBox.addNestedCssBox(cssBox);
				}
			}

			// colored backgrounds based on hour range
			String nonWorkingHourStyle = strAttr("nonWorkingHourStyle");
			String nonWorkingDayStyle = strAttr("nonWorkingDayStyle");
			String workingHourStyle = strAttr("workingHourStyle");
			String peakUsageHourStyle = strAttr("peakUsageHourStyle");
			for (int dayOffset=0;dayOffset<viewableDays;dayOffset++){
				Date currentDay = LavaDateUtils.addDays(dateRangeStart, dayOffset);
				DateRange viewableDayRange = new DateRange(currentDay,dayViewStart,currentDay,dayViewEnd);
				cal.setTime(currentDay);
				Integer xPos = dayOffset * dayBoxWidth + labelWidth;
				
				// create a background layer for entirety of viewable day range
				CssBox bkgLayer = new CssBox();
				bkgLayer.setWidth(dayBoxWidth);
				bkgLayer.setX(xPos);
				bkgLayer.setY(0);
				bkgLayer.setBoxHeight((int)(viewableDayRange.getRangeInMinutes() * calendarHeightPerHour/60));
				bkgLayer.setzIndex(0); // puts behind all other overlapping elements
				
				// create a working hour layer
				if (calendar.getWorkDays()!=null && calendar.getWorkDays().contains(String.valueOf(cal.get(java.util.Calendar.DAY_OF_WEEK)))){
					bkgLayer.setClassStr(nonWorkingHourStyle);
					DateRange workingHourRange = new DateRange(currentDay,calendar.getWorkBeginTime(),currentDay,calendar.getWorkEndTime());
					DateRange boxRange = viewableDayRange.getOverlap(workingHourRange);
					if (boxRange.hasRange()){
						CssBox workLayer = new CssBox();
						workLayer.setClassStr(workingHourStyle);
						workLayer.setWidth(dayBoxWidth);
						workLayer.setX(xPos);
						workLayer.setY((int)(boxRange.getMinutesUntilStart(viewableDayRange.getStart())*calendarHeightPerHour/60));
						workLayer.setBoxHeight((int)(boxRange.getRangeInMinutes() * calendarHeightPerHour/60));
						workLayer.setzIndex(1); // puts in front of non-working hours color but behind peak hours and any other overlapping elements
						calendarContentBox.addNestedCssBox(workLayer);
					}
				} else {
					bkgLayer.setClassStr(nonWorkingDayStyle);
				}
				calendarContentBox.addNestedCssBox(bkgLayer);
				
				// create a peak usage layer (for resource calendars only)
				if (calendar instanceof ResourceCalendar){
					ResourceCalendar resCal = (ResourceCalendar)calendar;
					if (resCal.getPeakUsageDays()!=null && resCal.getPeakUsageDays().contains(String.valueOf(cal.get(java.util.Calendar.DAY_OF_WEEK)))){
						DateRange peakUsageHourRange = new DateRange(currentDay, resCal.getPeakUsageBeginTime(), currentDay, resCal.getPeakUsageEndTime());
						DateRange boxRange = viewableDayRange.getOverlap(peakUsageHourRange);
						if (boxRange.hasRange()){
							CssBox peakLayer = new CssBox();
							peakLayer.setClassStr(peakUsageHourStyle);
							peakLayer.setWidth(dayBoxWidth);
							peakLayer.setX(xPos);
							peakLayer.setY((int)(boxRange.getMinutesUntilStart(viewableDayRange.getStart())*calendarHeightPerHour/60));
							peakLayer.setBoxHeight((int)(boxRange.getRangeInMinutes() * calendarHeightPerHour/60));
							peakLayer.setzIndex(2); // puts in front of working hours color but behind peak hours and any other overlapping elements
							calendarContentBox.addNestedCssBox(peakLayer);
						}
					}
				}
			}
			
			

			
			
			// time labels
			Date boxDateTimeEnd = LavaDateUtils.getDateTime(dateRangeStart, dayViewEnd);
			cal.setTime(boxDateTimeEnd);
			Integer hourEnd = cal.get(java.util.Calendar.HOUR_OF_DAY);
			Date boxDateTimeStart = LavaDateUtils.getDateTime(dateRangeStart, dayViewStart);
			cal.setTime(boxDateTimeStart);
			Integer hourBegin = cal.get(java.util.Calendar.HOUR_OF_DAY);
			if (displayTimeLabels){
				CssBox containerCssBox = new CssBox();
				containerCssBox.setX(0);
				containerCssBox.setY(0);
				containerCssBox.setBoxWidth(intAttr("timeLabelBoxWidth"));
				Integer numBoxes = intAttr("timeLabelBoxesPerHour");
				Integer timeLabelIntervalMinutes = 60/numBoxes;
				Integer timeLabelBoxHeightRemainder = intAttr("timeIntervalBoxHeight") * numBoxes
							- timeLabelBoxHeight * numBoxes;				
				for (int hourNum=hourBegin;hourNum<=hourEnd;hourNum++){
					Integer hoursPart = hourNum>12 ? hourNum-12 : (hourNum>0 ? hourNum : 12);
					String amPm = hourNum>11 ? "PM" : "AM";
					Integer minutesPart = 0;
					for (int interval=1;interval<=numBoxes;interval++){
						CssBox cssBox = (CssBox)timeLabelBox.getCopy();
						if (timeLabelBoxHeightRemainder>0 && interval==numBoxes){
							// make final box taller to accommodate any rounding effect
							cssBox.setBoxHeight(timeLabelBoxHeight+timeLabelBoxHeightRemainder);
						}
						// add hour label
						CssBox nestedCssBox = (CssBox)timeLabelHourBox.getCopy();
						nestedCssBox.setContent(String.valueOf(hoursPart));
						cssBox.addNestedCssBox(nestedCssBox);
						// add minutes or AM/PM as suffix
						if (interval==1 && (hoursPart==12 || hourNum==hourBegin)){
							// use AM/PM suffix at noon/midnight and for first hour in range
							nestedCssBox = (CssBox)timeLabelAmPmSuffixBox.getCopy();
							nestedCssBox.setContent(amPm);
							cssBox.addNestedCssBox(nestedCssBox);
						} else {
							nestedCssBox = (CssBox)timeLabelSuffixBox.getCopy();
							nestedCssBox.setContent(String.format("%02d", minutesPart));
							cssBox.addNestedCssBox(nestedCssBox);
						}
						containerCssBox.addNestedCssBox(cssBox);
						minutesPart = minutesPart + timeLabelIntervalMinutes;
					}
				}
				calendarContentBox.addNestedCssBox(containerCssBox);
			}

			// time interval boxes
			Integer numBoxes = intAttr("timeIntervalBoxesPerHour");
			Integer timeIntervalBoxHeight = intAttr("timeIntervalBoxHeight");
			for (int dayOffset=0;dayOffset<viewableDays;dayOffset++){
				Integer xPos = dayOffset * dayBoxWidth + labelWidth;
				Integer yPos = 0;
				Date currentDay = LavaDateUtils.addDays(dateRangeStart, dayOffset);
				
				
				Integer timeIntervalMinutes = 60/numBoxes;
				for (int hourNum=hourBegin;hourNum<=hourEnd;hourNum++){
					Integer minutesPart = 0;
					for (int interval=1;interval<=numBoxes;interval++){
						CssBox cssBox = (CssBox)timeIntervalBox.getCopy();
						cssBox.setX(xPos);
						cssBox.setY(yPos);

						// apply style for today's date
						if (LavaDateUtils.getDatePart(currentDay).equals(LavaDateUtils.getDatePart(new Date()))){
							cssBox.setClassStr(cssBox.getClassStr().concat(" ").concat(currentDateStyle));
						}
						
						// apply style for previous date
						if (LavaDateUtils.getDatePart(currentDay).before(LavaDateUtils.getDatePart(new Date()))){
							cssBox.setClassStr(cssBox.getClassStr().concat(" ").concat(previousDateStyle));
						}

						// add url link for onClick event
						if (timeClickEventUrl!=null){
							StringBuffer urlParams = new StringBuffer();
							urlParams.append(timeClickEventUrl.contains("?") ? "&" : "?");
							urlParams.append("selectedDate=").append(df.format(currentDay));
							urlParams.append("&").append("selectedTime=").append(String.format("%02d:%02d:00", hourNum, minutesPart));
							cssBox.setUrl(timeClickEventUrl.concat(urlParams.toString()));
						}
						calendarContentBox.addNestedCssBox(cssBox);
						minutesPart += timeIntervalMinutes;
						yPos += timeIntervalBoxHeight;
					}
				}
			}




			// appointment boxes
			for (int dayOffset=0;dayOffset<viewableDays;dayOffset++){
				int zIndex=10; // puts appointments in front of other elements
				for (Appointment appt : appts.get(dayOffset)){
					Date currentDay = LavaDateUtils.addDays(dateRangeStart, dayOffset);
					DateRange viewableDayRange = new DateRange(currentDay,dayViewStart,currentDay,dayViewEnd);
					DateRange viewableAppointmentRange = viewableDayRange.getOverlap(appt.getDateRange());

					if (!viewableAppointmentRange.isEmpty()){
						// create CSS style content box
						CssBox cssBox = (CssBox)apptBox.getCopy();

						// highlight today's date
						if (LavaDateUtils.getDatePart(currentDay).equals(LavaDateUtils.getDatePart(new Date()))){
							cssBox.setClassStr(cssBox.getClassStr().concat(" ").concat(currentDateStyle));
						}

						cssBox.setX(dayOffset * dayBoxWidth + labelWidth);
						cssBox.setY((int)(viewableAppointmentRange.getMinutesUntilStart(viewableDayRange.getStart()) * calendarHeightPerHour/60));
						cssBox.setBoxHeight((int)(viewableAppointmentRange.getRangeInMinutes() * calendarHeightPerHour/60));
						cssBox.setzIndex(zIndex++); // places appointment in front of the time interval grid and any earlier appointments

						// build contents
						if (apptContentList != null){
							Integer idx = apptIdList.indexOf(appt.getId());
							if (idx != -1){
								cssBox.setContent((String)apptContentList.get(idx));
								cssBox.setTitle(((String)apptContentList.get(idx)).replaceAll("<.*>", " "));
								if (apptUrlList != null){
									cssBox.setUrl((String)apptUrlList.get(idx));
								}
							}
						}
						calendarContentBox.addNestedCssBox(cssBox);
					}
				}
			}
		}
		
		calendarBox.addNestedCssBox(calendarContentBox);
		renderObjects.add(calendarBox);

		return renderObjects;
	}

	@Override
	protected Boolean attrValid(String attrName, Object attrValue) {
		// check for validity of CSS Box properties
		// NOTE: check for correct class has already been done at this point by the calling
		//   routine as long as the correct attr method is used, so no need to check for
		
		// all attributes except calendarMaxHeight must have a value
		if (attrValue==null && !attrName.contentEquals("calendarMaxHeight")){return false;}
		
		// minutes per time interval box must not be a fractional value
		if (attrName.contentEquals("timeIntervalBoxesPerHour")){
			if (60 % (Integer)attrValue != 0){return false;}
		}
			
		return super.attrValid(attrName, attrValue);
	}

}
