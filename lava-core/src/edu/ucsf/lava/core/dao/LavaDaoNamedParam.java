package edu.ucsf.lava.core.dao;

public interface LavaDaoNamedParam extends LavaDaoParam {

	public String getParamName();
	public void setParamName(String name);
	public Object getParamValue();
	public void setParamValue(Object value);
	
}
