package edu.ucsf.lava.core.view.model;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderParams {

	protected Map<String,Object> renderParams = new HashMap<String,Object>();
	protected Map<String,Class> requiredParams = new HashMap<String,Class>();
	protected Map<String,Class> optionalParams = new HashMap<String,Class>();
	
	public static final Boolean REQUIRED_PARAM = true;
	public static final Boolean OPTIONAL_PARAM = false;
	
	public void clearRenderParams(){
		renderParams = new HashMap<String,Object>();
	}
	
	public Map<String, Object> getRenderParams() {
		return renderParams;
	}
	
	public void setRenderParams(String parameters) throws Exception {
		// parse parameter/value pairs and set parameter values
		if (parameters != null){
			String[] parsed = parameters.split(",");
			if (parsed.length != 0 &&  parsed.length % 2 == 0){
				for (int i=0; i<parsed.length; i=i+2){
					setRenderParam(parsed[i],parsed[i+1]);
				}
			}
		}
	}
	
	public Object getRenderParam(String paramName){
		return renderParams.get(paramName);
	}
	
	public void setRenderParam(String paramName, Object paramValue) throws Exception {
		Map<String,Class> supportedParams = getSupportedParams();
		if (supportedParams.containsKey(paramName)){
			if (supportedParams.get(paramName).equals(paramValue.getClass())){
				if (validRenderParamValue(paramName, paramValue)){
					renderParams.put(paramName, paramValue);
				} else {
					throw new InvalidRenderParamValueException("Render parameter " + paramName + " does not support value of " + paramValue.toString());
				}
			} else {
				// try to cast from String to proper type
				if (paramValue.getClass().equals(String.class)){
					if (supportedParams.get(paramName).equals(java.util.Date.class)){
						DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
						try {
							java.util.Date dateValue = df.parse((String)paramValue);
							renderParams.put(paramName, dateValue);
						} catch (ParseException e) {
							throw new RenderParamClassException("Problem converting from String to java.util.Date for render parameter " + paramName + " with value of " + (String)paramValue);
						}
					} else if (supportedParams.get(paramName).equals(java.lang.Boolean.class)){
						renderParams.put(paramName, Boolean.valueOf((String)paramValue));
					} // TODO: Add support for other classes as necessary
				} else {
					throw new RenderParamClassException("Render parameter " + paramName + " must be of type " + supportedParams.get(paramName).getName());
				}
			}
		} else {
			throw new UnsupportedRenderParamException("Render parameter " + paramName + " is not supported by " + this.getClass().getName());
		}
	}
	
	public void addRenderParam(String paramName, Class clazz, Boolean required){
		if (required){
			requiredParams.put(paramName, clazz);
		} else {
			optionalParams.put(paramName, clazz);
		}
	}
	
	public void addRenderParamElement(String paramName, Object paramElementValue) throws Exception {
		// convenience method for adding a single item to a render parameter of type List
		Map<String,Class> supportedParams = getSupportedParams();
		if (supportedParams.containsKey(paramName)){
			if (supportedParams.get(paramName).equals(java.util.List.class)){
				List paramValue = (List)renderParams.get(paramName);
				if (paramValue == null){
					paramValue = new ArrayList();
				}
				paramValue.add(paramElementValue);
				if (validRenderParamValue(paramName, paramValue)){
					renderParams.put(paramName, paramValue);
				} else {
					throw new InvalidRenderParamValueException("Invalid value " + paramValue.toString() + " of render parameter " + paramName);
				}
			} else {
				throw new RenderParamClassException("Render parameter " + paramName + " is not of type java.util.List");
			}
		} else {
			throw new UnsupportedRenderParamException("Render parameter " + paramName + " is not supported by " + this.getClass().getName());
		}
		
	}
	
	public Map<String,Class> getSupportedParams(){
		Map<String,Class> supportedParams = new HashMap<String,Class>();
		supportedParams.putAll(requiredParams);
		supportedParams.putAll(optionalParams);
		return supportedParams;
	}
	
	public void checkRequiredParams() throws Exception {
		for (String key : requiredParams.keySet()){
			if (!renderParams.containsKey(key) || renderParams.get(key)==null){
				throw new MissingRenderParamException("Render parameter " + key + " is required by " + this.getClass().getName());
			}
		}
	}
	
	public Boolean validRenderParamValue(String paramName, Object paramValue) {
		// override in subclass if parameter value validation is desired
		return true;
	}
	

}
