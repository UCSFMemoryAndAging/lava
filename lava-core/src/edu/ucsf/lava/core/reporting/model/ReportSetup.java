package edu.ucsf.lava.core.reporting.model;

import java.io.Serializable;

import edu.ucsf.lava.core.dao.LavaDaoFilter;


// class for report setup data
public class ReportSetup implements Serializable {
	private String format;
	private LavaDaoFilter filter;

	public LavaDaoFilter getFilter() {
		return filter;
	}
	public void setFilter(LavaDaoFilter filter) {
		this.filter = filter;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
}
