package edu.ucsf.lava.core.list.model;

import java.util.ArrayList;
import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoNamedParam;
import edu.ucsf.lava.core.dao.LavaDaoParam;

/**
 * This class supports numeric ranges based on a large numeric range list in the database. 
 * This could be refactored to generate the list at runtime in java faster. 
 * 
 * @author jhesse
 *
 */
public class NumericRangeListConfig extends DefaultListConfig {

	public static String MIN_PARAM_NAME="min";
	public static String MAX_PARAM_NAME="max";
	
	private Long defaultMinValue = new Long(0);  //if we end up not specifying any defaults we will get and empty list
	private Long defaultMaxValue = new Long(-1);
	
	
	public NumericRangeListConfig() {
		super();
		this.defaultSort = SORT_VALUE_NUMERIC;
	}
	public Long getDefaultMaxValue() {
		return defaultMaxValue;
	}
	public void setDefaultMaxValue(Long defaultMaxValue) {
		this.defaultMaxValue = defaultMaxValue;
	}
	public Long getDefaultMinValue() {
		return defaultMinValue;
	}
	public void setDefaultMinValue(Long defaultMinValue) {
		this.defaultMinValue = defaultMinValue;
	}

	protected ListRequest onGetList(ListRequest request) {
		request = super.onGetList(request);
		
		List<LavaDaoParam> params = request.getParams();
		LavaDaoFilter filter = LavaList.newFilterInstance(); //for new params
		boolean minFound = false;
		boolean maxFound = false;
		
		if(params != null){
			//make sure min and max params are specified as Long
			for(LavaDaoParam param : params){
				if(param.getType().equals(LavaDaoParam.TYPE_NAMED)){
					LavaDaoNamedParam namedParam = (LavaDaoNamedParam)param;
					if(namedParam.getParamName().equalsIgnoreCase(MIN_PARAM_NAME)){
						if(namedParam.getParamValue() instanceof String){
							try{
								minFound = true;
								namedParam.setParamValue(Long.valueOf((String)namedParam.getParamValue()));
							}catch(NumberFormatException nfe){
								namedParam.setParamValue(defaultMinValue);  //this should make the list blow up...
							}
						}
					}else if(namedParam.getParamName().equalsIgnoreCase(MAX_PARAM_NAME)){
						if(namedParam.getParamValue() instanceof String){
							try{
								maxFound = true;
								namedParam.setParamValue(Long.valueOf((String)namedParam.getParamValue()));
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
	 * override the base class to generate the list in code rather than query the list table for a numeric series
	 */
	protected List<LabelValueBean> loadPrimaryQueryList(LavaDaoFilter filter) {
		
		Long minValue = null;
		Long maxValue = null;

		//get range
		for(LavaDaoParam param : filter.getDaoParams()){
			if(param.getType().equals(LavaDaoParam.TYPE_NAMED)){
				LavaDaoNamedParam namedParam = (LavaDaoNamedParam)param;
				if(namedParam.getParamName().equalsIgnoreCase(MIN_PARAM_NAME)){
					minValue = (Long)namedParam.getParamValue();
				}else if(namedParam.getParamName().equalsIgnoreCase(MAX_PARAM_NAME)){
					maxValue = (Long)namedParam.getParamValue();
				}
			}
		}
		//use defaults if min and/or max not found
		if(minValue==null){minValue=defaultMinValue;}
		if(maxValue==null){maxValue=defaultMaxValue;}
	
		int resultSize = maxValue.intValue() - minValue.intValue();
		List<LabelValueBean> results = new ArrayList<LabelValueBean>(resultSize < 0 ? 0 : resultSize);
		
		for(int i = minValue.intValue(); i < maxValue.intValue();i++){
			String value = Integer.toString(i);
			results.add(new LabelValueBean(value,value,Integer.valueOf(i)));
		}
		return results;
	}
	
	
	
	
	
	
}
