package edu.ucsf.lava.core.dao;

import java.text.DateFormat;
import java.util.Date;

import edu.ucsf.lava.core.type.DateRange;
import edu.ucsf.lava.core.type.LavaDateUtils;

public class LavaDateRangeOverlapParamHandler implements LavaParamHandler {

	public static final String DEFAULT_PARAM_NAME = "overlapDateRange";
	public static final String DEFAULT_START_DATE_PROP = "startDate";
	public static final String DEFAULT_START_TIME_PROP = "startTime";
	public static final String DEFAULT_END_DATE_PROP = "endDate";
	public static final String DEFAULT_END_TIME_PROP = "endTime";
	public static final String START_CODE = "Start";
	public static final String END_CODE = "End";

	private String propertyName;
	private String startDateParam;
	private String endDateParam;
	protected String startDatePropertyName = DEFAULT_START_DATE_PROP;
	protected String startTimePropertyName = DEFAULT_START_TIME_PROP;
	protected String endDatePropertyName = DEFAULT_END_DATE_PROP;
	protected String endTimePropertyName = DEFAULT_END_TIME_PROP;

	public LavaDateRangeOverlapParamHandler(String propertyName) {
		this.propertyName = propertyName;
		this.startDateParam = this.propertyName.concat(this.START_CODE);
		this.endDateParam = this.propertyName.concat(this.END_CODE);
	}

	public LavaDateRangeOverlapParamHandler(){
		this(DEFAULT_PARAM_NAME);
	}

	public String getStartDatePropertyName() {
		return startDatePropertyName;
	}

	public void setStartDatePropertyName(String startDatePropertyName) {
		this.startDatePropertyName = startDatePropertyName;
	}

	public String getStartTimePropertyName() {
		return startTimePropertyName;
	}

	public void setStartTimePropertyName(String startTimePropertyName) {
		this.startTimePropertyName = startTimePropertyName;
	}

	public String getEndDatePropertyName() {
		return endDatePropertyName;
	}

	public void setEndDatePropertyName(String endDatePropertyName) {
		this.endDatePropertyName = endDatePropertyName;
	}

	public String getEndTimePropertyName() {
		return endTimePropertyName;
	}

	public void setEndTimePropertyName(String endTimePropertyName) {
		this.endTimePropertyName = endTimePropertyName;
	}

	public boolean handleParam(LavaDaoFilter filter, String paramName) {

		if (paramName.equalsIgnoreCase(startDateParam)){
			Date startDate;
			Date endDate;

			Object startDateParamValue = filter.getParam(startDateParam);
			Object endDateParamValue = filter.getParam(endDateParam);

			//convert start date param to a date object or return
			if(startDateParamValue == null){
				return true;
			}
			if (Date.class.isInstance(startDateParamValue)){
				startDate = (Date)startDateParamValue;
			}else{
				try{
					startDate =	DateFormat.getDateInstance(DateFormat.SHORT).parse(startDateParamValue.toString());
				}catch(Exception e){
					return true;
				}
			}

			//if end date is null, use start date as end date, otherwise convert end date param to a date object
			if(endDateParamValue == null){
				endDate = startDate;
			}else if (Date.class.isInstance(endDateParamValue)){
				endDate = (Date)endDateParamValue;
			}else{
				try{
					endDate =	DateFormat.getDateInstance(DateFormat.SHORT).parse(endDateParamValue.toString());
				}catch(Exception e){
					endDate = startDate;
				}
			}
			filter.addDaoParam(filter.daoDateAndTimeOverlapsParam(startDatePropertyName, startTimePropertyName, endDatePropertyName, endTimePropertyName, 
					LavaDateUtils.getDatePart(startDate), LavaDateUtils.getTimePart(startDate), 
					LavaDateUtils.getDatePart(endDate), LavaDateUtils.getTimePart(endDate)));
			return true;
		}else if(paramName.equalsIgnoreCase(endDateParam)){
			return true; //always handle the end param as well
		}
		return false;
	}


}
