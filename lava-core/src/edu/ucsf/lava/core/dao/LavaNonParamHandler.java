package edu.ucsf.lava.core.dao;

/**
 * This param handler is used for non-parameters, i.e. i.e. the handler does not 
 * convert the param to a daoParam by adding it to daoParams because the param will
 * not be involved in a query. The existence of this handler is to allow using the
 * the LavaDaoFilter for parameters that are related to, but not part of the query,
 * such as the format of the list in which the query results are presented.
 * 
 * The difference between this handler and LavaIgnoreParamHandler is that the
 * parameters in this handler are not cleared by the LavaDaoFilter clearParams
 * method (e.g. when a list Filter is reset or toggled), so that the parameter
 * values are retained.
 */

public class LavaNonParamHandler implements LavaParamHandler {
	private String propertyName;
	public LavaNonParamHandler(String propertyName) {
		this.propertyName = propertyName;
	}

	public boolean handleParam(LavaDaoFilter filter, String propertyName) {
		if(propertyName.equalsIgnoreCase(this.propertyName)){
			return true;
		}
		return false;
	}

}
