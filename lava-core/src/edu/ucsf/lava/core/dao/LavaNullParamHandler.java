package edu.ucsf.lava.core.dao;

public class LavaNullParamHandler implements LavaParamHandler {
	private String propertyName;
	
	public LavaNullParamHandler(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean handleParam(LavaDaoFilter filter, String propertyName) {
		if(propertyName.equalsIgnoreCase(this.propertyName)){
			filter.addDaoParam(filter.daoNull(propertyName));
			return true;
		}
		return false;
	}

}
