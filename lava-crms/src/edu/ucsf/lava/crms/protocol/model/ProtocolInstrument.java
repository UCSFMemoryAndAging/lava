package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ProtocolInstrument extends ProtocolInstrumentBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrument.class);
	
	public ProtocolInstrument(){
		super();
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getOptions()};
	}

	private Boolean optional;
	private String type; //????
	private String category; //????
	private String defaultCompStatus;
	private String defaultCompReason;
	private String defaultCompNote;
	
	/**
	 * Convenience method to return ProtocolVisit instead of ProtocolVisitBase.
	 */
//	public ProtocolVisit getVisit() {
//		return (ProtocolVisit) super.getVisit();
//	}
	
	public Boolean getOptional() {
		return optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getDefaultCompStatus() {
		return defaultCompStatus;
	}
	public void setDefaultCompStatus(String defaultCompStatus) {
		this.defaultCompStatus = defaultCompStatus;
	}
	public String getDefaultCompReason() {
		return defaultCompReason;
	}
	public void setDefaultCompReason(String defaultCompReason) {
		this.defaultCompReason = defaultCompReason;
	}
	public String getDefaultCompNote() {
		return defaultCompNote;
	}
	public void setDefaultCompNote(String defaultCompNote) {
		this.defaultCompNote = defaultCompNote;
	}
}
