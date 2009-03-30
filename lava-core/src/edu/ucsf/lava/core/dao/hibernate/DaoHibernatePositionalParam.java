package edu.ucsf.lava.core.dao.hibernate;

import edu.ucsf.lava.core.dao.LavaDaoPositionalParam;

public class DaoHibernatePositionalParam implements LavaDaoPositionalParam {

	int paramPos;
	Object paramValue;
	
	public DaoHibernatePositionalParam(int paramPos, Object paramValue) {
		this.paramPos = paramPos;
		this.paramValue = paramValue;
	}
	
	public String getType() {
		return this.TYPE_POSITIONAL;
	}


	public int getParamPos() {
		return paramPos;
	}

	public Object getParamValue() {
		return paramValue;
	}

	public void setParamPos(int paramPos) {
		this.paramPos = paramPos;
	}

	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}

}


