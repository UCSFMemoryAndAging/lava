package edu.ucsf.lava.core.list.model;

import java.util.HashMap;
import java.util.Map;

public class EmptyListConfig extends DefaultListConfig {

	public static String EMPTY_LIST_CONFIG_NAME = "EMPTY_LIST_CONFIG";
	public EmptyListConfig() {
		super();
		this.setName(EMPTY_LIST_CONFIG_NAME);
	}
	
	//return an empty map
	public Map<String, String> getList(BaseListConfig codesToUse) {
		return new HashMap<String,String>();
	}
	
//	return an empty map
	public Map<String, String> getList(ListRequest request) {
		return new HashMap<String,String>();
	}

	
}
