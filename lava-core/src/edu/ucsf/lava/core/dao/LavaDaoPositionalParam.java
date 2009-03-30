package edu.ucsf.lava.core.dao;

public interface LavaDaoPositionalParam extends LavaDaoParam {

	public int getParamPos();
	public Object getParamValue();
	public void setParamPos(int paramPos);
	public void setParamValue(Object paramValue);
}
