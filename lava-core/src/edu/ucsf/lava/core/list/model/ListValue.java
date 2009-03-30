package edu.ucsf.lava.core.list.model;

// created to use OR/M to query the underlying ListValue table, so this
//    class is modeled after that table

public class ListValue {
	private Long id;
	private Long listId;
	private String valueKey;
	private String valueDesc;
	private Integer orderIndex;
	
	public ListValue() {}
	
	public Long getId() {return this.id;}
	public void setId(Long id) {this.id = id;}
	
	public Long getListId() {return this.listId;}
	public void setListId(Long listId) {this.listId = listId;}
	
	public String getValueKey() {return this.valueKey;}
	public void setValueKey(String valueKey) {this.valueKey = valueKey;}
	
	public String getValueDesc() {return this.valueDesc;}
	public void setValueDesc(String valueDesc) {this.valueDesc = valueDesc;}
	
	public Integer getOrderIndex() {return this.orderIndex;}
	public void setOrderIndex(Integer orderIndex) {this.orderIndex = orderIndex;}
}
