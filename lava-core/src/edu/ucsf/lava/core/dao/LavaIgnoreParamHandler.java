package edu.ucsf.lava.core.dao;

public class LavaIgnoreParamHandler implements LavaParamHandler {
	private String propertyName;
	public LavaIgnoreParamHandler(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean handleParam(LavaDaoFilter filter, String propertyName) {
		if(propertyName.equalsIgnoreCase(this.propertyName)){
			return true;
		}
		return false;
	}

}
