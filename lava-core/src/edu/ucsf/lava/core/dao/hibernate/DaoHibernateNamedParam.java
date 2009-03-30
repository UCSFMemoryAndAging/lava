package edu.ucsf.lava.core.dao.hibernate;

import edu.ucsf.lava.core.dao.LavaDaoNamedParam;

public class DaoHibernateNamedParam implements LavaDaoNamedParam {
	
	String paramName;
	Object paramValue;
	
	public String getType() {
		return this.TYPE_NAMED;
	}

	public DaoHibernateNamedParam(String paramName,Object paramValue) {
		this.paramName = paramName;
		this.paramValue = paramValue;
		
	}

	public String getParamName() {
		return paramName;
	}

	public Object getParamValue() {
		return paramValue;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

}
