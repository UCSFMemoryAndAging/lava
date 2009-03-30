package edu.ucsf.lava.core.list.model;

import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class LavaList extends EntityBase  {
	
	public static EntityManager MANAGER = new EntityBase.Manager(LavaList.class);
	
	
	private String listName;
	private Boolean numericKey;
	//private java.util.List listValues;
	private Set listValues;
	
	
	
	
	public LavaList() {
		this.setAudited(false);
	}
	
	public String getListName() {return this.listName;}
	public void setListName(String listName) {this.listName = listName;}
	
	public Boolean getNumericKey() {return this.numericKey;}
	public void setNumericKey(Boolean numericKey) {this.numericKey = numericKey;}
	
	//public java.util.List getListValues() {return this.listValues;}
	//public void setListValues(java.util.List listValues) {this.listValues = listValues;}
	public Set getListValues() {return this.listValues;}
	public void setListValues(Set listValues) {this.listValues = listValues;}
	

	

	
	
	
	
	
	
	
}
