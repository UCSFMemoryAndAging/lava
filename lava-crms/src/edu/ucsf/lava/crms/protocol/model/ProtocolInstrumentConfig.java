package edu.ucsf.lava.crms.protocol.model;

import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ProtocolInstrumentConfig extends ProtocolInstrumentConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentConfig.class);
	
	public ProtocolInstrumentConfig(){
		super();
		this.setAuditEntityType("ProtocolInstrumentConfig");		
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getOptionsBase()};
	}

	private Boolean optional;
	private String type; //????
	private String category; //????
	private String defaultCompStatus;
	private String defaultCompReason;
	private String defaultCompNote;
	
	/**
	 * Convenience methods to convert ProtocolInstrumentConfigBase method types to types of 
	 * this subclass, since if an object of this class exists we know we can safely downcast. 
	 */
	public ProtocolVisitConfig getVisitConfig() {
		return (ProtocolVisitConfig) super.getVisitBase();
	}
	public void setVisitConfig(ProtocolVisitConfig protocolVisitBase) {
		super.setVisitBase(protocolVisitBase);
	}
	public Set<ProtocolInstrumentOptionConfig> getInstrumentOptions() {
		return (Set<ProtocolInstrumentOptionConfig>) super.getOptionsBase();
	}
	public void setInstrumentOptions(Set<ProtocolInstrumentOptionConfig> instrumentOptions) {
		super.setOptionsBase(instrumentOptions);
	}

	
	/*
	 * Method to add an option to an instrument, managing the bi-directional relationship.
	 */	
	public void addOption(ProtocolInstrumentOptionConfig instrumentOption) {
		instrumentOption.setInstrument(this);
		this.getInstrumentOptions().add(instrumentOption);
	}
	
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
