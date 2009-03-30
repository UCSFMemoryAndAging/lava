package edu.ucsf.lava.core.list.model;

public class DefaultListConfig extends BaseListConfig {
	 public static String DEFAULT_SORT = SORT_LABEL_STRING;
	 public static String DEFAULT_FORMAT = FORMAT_LABEL;
	  
	public DefaultListConfig() {
		super();
		
		this.defaultSort = this.DEFAULT_SORT;
		this.defaultFormat = this.DEFAULT_FORMAT;
		this.defaultCodes = new NoCodesListConfig();
		
	}
	
}
