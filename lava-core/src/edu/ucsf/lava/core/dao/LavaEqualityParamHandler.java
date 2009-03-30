package edu.ucsf.lava.core.dao;

public class LavaEqualityParamHandler implements LavaParamHandler {
	private String propertyName;
	
	public LavaEqualityParamHandler(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean handleParam(LavaDaoFilter filter, String propertyName) {
		if(propertyName.equalsIgnoreCase(this.propertyName)){
			filter.addDaoParam(filter.daoEqualityParam(propertyName,filter.getParam(propertyName)));
			return true;
		}
		return false;
	}

}
