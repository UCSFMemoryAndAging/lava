package edu.ucsf.lava.core.dao;

import java.text.DateFormat;
import java.util.Date;
public class LavaDateRangeParamHandler implements LavaParamHandler {
	private String propertyName;
	private String startDateParam;
	private String endDateParam;
	
	public static final String START_CODE = "Start";
	public static final String END_CODE = "End";
	
	public LavaDateRangeParamHandler(String propertyName) {
		this.propertyName = propertyName;
		this.startDateParam = this.propertyName.concat(this.START_CODE);
		this.endDateParam = this.propertyName.concat(this.END_CODE);
		
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
			filter.addDaoParam(filter.daoBetweenParam(propertyName,
		    				startDate,endDate));
			return true;
		}else if(paramName.equalsIgnoreCase(endDateParam)){
			return true; //always handle the end param as well
		}
		return false;
	}

}
