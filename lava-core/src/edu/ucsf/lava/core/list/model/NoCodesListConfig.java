package edu.ucsf.lava.core.list.model;

import java.util.HashMap;
import java.util.Map;

public class NoCodesListConfig extends BaseListConfig {

	
	public static String NO_CODES_CONFIG_NAME = "noCodes";
	public NoCodesListConfig() {
		super();
		this.defaultFormat = FORMAT_LABEL;
		this.defaultSort = SORT_NONE;
		this.setName(NO_CODES_CONFIG_NAME);
		
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
