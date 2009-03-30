package edu.ucsf.lava.core.dao;

import java.io.Serializable;

public interface LavaDaoParam extends Serializable {
	public static final String TYPE_CRITERION = "criterionParam";
	public static final String TYPE_NAMED = "namedParam";
	public static final String TYPE_POSITIONAL = "positionalParam";
	public String getType();

	 
}
