package edu.ucsf.lava.core.dao;

import java.io.Serializable;

public interface LavaParamHandler extends Serializable {
		
	public boolean handleParam(LavaDaoFilter filter, String propertyName);
	
		
}
