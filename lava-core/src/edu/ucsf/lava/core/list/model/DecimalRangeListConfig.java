package edu.ucsf.lava.core.list.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoNamedParam;
import edu.ucsf.lava.core.dao.LavaDaoParam;

public class DecimalRangeListConfig extends BaseListConfig {

	
	public static String MIN_PARAM_NAME="min";
	public static String MAX_PARAM_NAME="max";
	
	private Float defaultMinValue = new Float(0);  //if we end up not specifying any defaults we will get and empty list
	private Float defaultMaxValue = new Float(-1);
	
	public DecimalRangeListConfig() {
		super();
		this.defaultSort = SORT_VALUE_DECIMAL;
	}
	

	public Float getDefaultMaxValue() {
		return defaultMaxValue;
	}




	public void setDefaultMaxValue(Float defaultMaxValue) {
		this.defaultMaxValue = defaultMaxValue;
	}




	public Float getDefaultMinValue() {
		return defaultMinValue;
	}




	public void setDefaultMinValue(Float defaultMinValue) {
		this.defaultMinValue = defaultMinValue;
	}




	protected ListRequest onGetList(ListRequest request) {
		request = super.onGetList(request);
		
		List<LavaDaoParam> params = request.getParams();
		LavaDaoFilter filter = LavaList.newFilterInstance(); //for new params
		boolean minFound = false;
		boolean maxFound = false;
		
		if(params != null){
			//make sure min and max params are specified as Float
			for(LavaDaoParam param : params){
				if(param.getType().equals(LavaDaoParam.TYPE_NAMED)){
					LavaDaoNamedParam namedParam = (LavaDaoNamedParam)param;
					if(namedParam.getParamName().equalsIgnoreCase(MIN_PARAM_NAME)){
						if(namedParam.getParamValue() instanceof String){
							try{
								minFound = true;
								namedParam.setParamValue(Float.valueOf((String)namedParam.getParamValue()));
							}catch(NumberFormatException nfe){
								namedParam.setParamValue(defaultMinValue);  //this should make the list blow up...
							}
						}
					}else if(namedParam.getParamName().equalsIgnoreCase(MAX_PARAM_NAME)){
						if(namedParam.getParamValue() instanceof String){
							try{
								maxFound = true;
								namedParam.setParamValue(Float.valueOf((String)namedParam.getParamValue()));
							}catch(NumberFormatException nfe){
								namedParam.setParamValue(defaultMaxValue); 
							}
						}
					}
				}
			}
		}
		//use defaults if min and/or max not found
		if(!minFound){
			request.addParam(filter.daoNamedParam(MIN_PARAM_NAME, defaultMinValue));
		}
		if(!maxFound){
			request.addParam(filter.daoNamedParam(MAX_PARAM_NAME, defaultMaxValue));
				
		}
		return request;
	}
	
	/** 
	 * override the base class to generate the list in code rather than query the list table for a numeric decimal series
	 */
	protected List<LabelValueBean> loadPrimaryQueryList(LavaDaoFilter filter) {
		
		Float minValue = null;
		Float maxValue = null;

		//get range
		for(LavaDaoParam param : filter.getDaoParams()){
			if(param.getType().equals(LavaDaoParam.TYPE_NAMED)){
				LavaDaoNamedParam namedParam = (LavaDaoNamedParam)param;
				if(namedParam.getParamName().equalsIgnoreCase(MIN_PARAM_NAME)){
					minValue = (Float)namedParam.getParamValue();
				}else if(namedParam.getParamName().equalsIgnoreCase(MAX_PARAM_NAME)){
					maxValue = (Float)namedParam.getParamValue();
				}
			}
		}
		//use defaults if min and/or max not found
		if(minValue==null){minValue=defaultMinValue;}
		if(maxValue==null){maxValue=defaultMaxValue;}

		int resultSize = 0;
		DecimalFormat decimalFormat = null;
		if (this.getDefaultFormat().equals(FORMAT_TWO_DECIMAL_PLACES)) {
			resultSize = Math.round(maxValue - minValue) * 100;
			decimalFormat = new DecimalFormat("#0.00");
		}
		else { // default to 1 decimal place
			resultSize = Math.round(maxValue - minValue) * 10;
			decimalFormat = new DecimalFormat("#0.0");
		}
		
		
		List<LabelValueBean> results = new ArrayList<LabelValueBean>(resultSize < 0 ? 0 : resultSize);
        int orderIndex = 0;
		
        for(float i = minValue.floatValue(); i <= maxValue.floatValue() + 0.01f;){
			String value = decimalFormat.format(i);
			// kludges to make sure no minus sign appears in front of zeros since it interferes with data entry (i.e. can't type 0 to get 0.0 or 0.00
			if (value.equals("-0.00")) value="0.00";
			if (value.equals("-0.0")) value="0.0";
			results.add(new LabelValueBean(value,value,Integer.valueOf(orderIndex++)));
			if (this.getDefaultFormat().equals(FORMAT_TWO_DECIMAL_PLACES)) {
				i = i + 0.01f;
			}
			else { // default to 1 decimal place
				i = i + 0.1f;
			}
		}
		return results;
	}
	
}
