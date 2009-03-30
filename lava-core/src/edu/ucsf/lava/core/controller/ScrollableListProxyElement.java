package edu.ucsf.lava.core.controller;

import java.io.Serializable;

public class ScrollableListProxyElement implements Serializable {

	public ScrollableListProxyElement() {
		
	}
	
	public ScrollableListProxyElement(Object rowNum, Object recId) {
		this.rowNum = rowNum;
		this.recId = recId;	
	}
	public ScrollableListProxyElement(Object rowNum) {
		this.rowNum = rowNum;
		this.recId = null;
	}
	
	private Object rowNum;
	private Object recId;

	public Object getRowNum() {
		return rowNum;
	}

	public void setRowNum(Object rowNum) {
		this.rowNum = rowNum;
	}

	public Object getRecId() {
		return recId;
	}

	public void setRecId(Object recId) {
		this.recId = recId;
	}
	
	

}
