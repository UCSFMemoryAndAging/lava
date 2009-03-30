package edu.ucsf.lava.core.dao;

/**
 * 
 * @author ctoohey
 * 
 * This is an alternative to LavaEqualityParamHandler which assumes that the property 
 * is a String, or more accurately, although the DAO knows the type of the property, 
 * it treats the property value submitted in the filter as a String, so if the property
 * type is not a String, a ClassCastException results.
 * 
 * This class allows the caller to specify the property type so that the property value
 * can be converted from a String to the appropriate type.
 */
public class LavaTypedEqualityParamHandler implements LavaParamHandler {
	private String propertyName;
	private Class propertyType;
	
	public LavaTypedEqualityParamHandler(String propertyName, Class propertyType) {
		this.propertyName = propertyName;
		this.propertyType = propertyType;
	}

	public boolean handleParam(LavaDaoFilter filter, String propertyName) {
		if(propertyName.equalsIgnoreCase(this.propertyName)){
			Object filterValue;
			
			// currently, not configuring custom property editors for filter fields, so
			// null does not get converted to the empty string, so check for null
			if (filter.getParam(propertyName) != null && ((String)filter.getParam(propertyName)).length() > 0) {

				// add types as necessary
			
				if (propertyType == Short.class) {
					filterValue = Short.valueOf((String)filter.getParam(propertyName));
				}
				else if (propertyType == Long.class) {
					filterValue = Long.valueOf((String)filter.getParam(propertyName));
				}
				else if (propertyType == Boolean.class) {
					String paramValue = (String)filter.getParam(propertyName);
					if(paramValue.equalsIgnoreCase("1") || paramValue.equalsIgnoreCase("yes") || paramValue.equalsIgnoreCase("true")){
						filterValue = Boolean.TRUE;
					}else{
						filterValue = Boolean.FALSE;
					}
				}
				else { // default to String
					filterValue = (String) filter.getParam(propertyName);
				}
				filter.addDaoParam(filter.daoEqualityParam(propertyName,filterValue));
			}
			return true;
		}
		return false;
	}

}
