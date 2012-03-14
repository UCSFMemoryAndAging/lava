package edu.ucsf.lava.core.dao;

/**
 * This param handler is used for parameters that should not be part of the
 * standard query, i.e. the handler does not convert the param to a daoParam
 * by adding it to daoParams. Such parameters may be part of a custom query
 * done by a list handler. 
 * 
 * The difference between this handler and LavaNonParamHandler is that the
 * parameters in this handler are cleared by the LavaDaoFilter clearParams
 * method (e.g. when a list Filter is reset or toggled), because they are part
 * of a custom query, whereas LavaNonParamHandler params are not cleared.
 */

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
