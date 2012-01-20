package edu.ucsf.lava.crms.protocol.model;

import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 * ProtocolVisitConfig is a protocol node which defines the requirements for a patient visit
 * at a defined protocol timepoint. The eligible visit types which could fulfill the requirements 
 * are instances of the ProtocolVisitOptionConfig class, where any one ProtocolVisitOptionConfig
 * could fulfill a ProtocolVisitConfig definition.
 * 
 * A ProtocolVisitConfig should be something that will be scheduled. A ProtocolTimepointConfig
 * defines a scheduling window within which all of its visits should be scheduled. Its counterpart 
 * class, ProtocolVisit, contains a method can be called to determine whether it satisfies the 
 * ProtocolTimepoint the scheduling window.
 */

public class ProtocolVisitConfig extends ProtocolVisitConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolVisitConfig.class);
	
	public ProtocolVisitConfig(){
		super();
		this.setAuditEntityType("ProtocolVisitConfig");	
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				this.getProtocolInstrumentConfigsBase(), 
				this.getProtocolVisitConfigOptionsBase(), 
				this.getProtocolTimepointConfigBase().getProtocolVisitConfigsBase()};
	}
	
	private Boolean optional;
	private String category;
	private ProtocolVisitConfigOption defaultOption;
	// defaultOptionId facilitates modifying the defaultOption association. the user selects a 
	// defaultOption (ProtocolVisitOptionConfig) and that defaultOptionId is bound to this property. 
	// if it differs from this entity's existing defaultOption id, the defaultOptionId is used to 
	// retrieve its defaultOption which is then set on this entity via setDefaultOption to change 
	// the associated defaultOption
	private Long defaultOptionId;
	
	/**
	 * Convenience methods to convert ProtocolVisitConfigBase method types to types of 
	 * this subclass, since if an object of this class exists we know we can safely downcast. 
	 */
	public ProtocolTimepointConfig getProtocolTimepointConfig() {
		return (ProtocolTimepointConfig) super.getProtocolTimepointConfigBase();
	}
	public void setProtocolTimepointConfig(ProtocolTimepointConfig protocolTimepointConfig) {
		super.setProtocolTimepointConfigBase(protocolTimepointConfig);
	}
	public Set<ProtocolInstrumentConfig> getProtocolInstrumentConfigs() {
		return (Set<ProtocolInstrumentConfig>) super.getProtocolInstrumentConfigsBase();
	}
	public void setProtocolInstrumentConfigs(Set<ProtocolInstrumentConfig> protocolInstrumentConfigs) {
		super.setProtocolInstrumentConfigsBase(protocolInstrumentConfigs);
	}
	public Set<ProtocolVisitConfigOption> getOptions() {
		return (Set<ProtocolVisitConfigOption>) super.getProtocolVisitConfigOptionsBase();
	}
	public void setOptions(Set<ProtocolVisitConfigOption> options) {
		super.setProtocolVisitConfigOptionsBase(options);
	}
	
	/*
	 * Method to add a ProtocolInstrumentConfig to a ProtocolVisitConfig, managing the bi-directional relationship.
	 */	
	public void addProtocolInstrumentConfig(ProtocolInstrumentConfig instrumentConfig) {
		instrumentConfig.setProtocolVisitConfig(this);
		this.getProtocolInstrumentConfigs().add(instrumentConfig);
	}
	
	/**
	 * Convenience method to check if this is the primary ProtocolVisitConfig of the ProtocolTimepointConfig, given
	 * that there can be multiple visit configs. An association to the primary visit config is stored in the 
	 * ProtocolTimepointConfig parent of the ProtocolVisitConfig collection (as opposed to having a flag on each 
	 * ProtocolVisitConfig to designate itself as the primary, which would require synchronization among all 
	 * sibling ProtocolVisitConfigs)
	 * 
	 * @return true if this is the primary visit config, false if not.
	 */
	public Boolean isPrimaryProtocolVisitConfig() {
		return this.getProtocolTimepointConfig().getPrimaryProtocolVisitConfigId().equals(this.getId());
	}

	
	/*
	 * Method to add a ProtocolVisitConfigOption to a ProtocolVisit's collection, managing the bi-directional relationship.
	 */	
	public void addOption(ProtocolVisitConfigOption protocolVisitConfigOption) {
		protocolVisitConfigOption.setProtocolVisitConfig(this);
		this.getOptions().add(protocolVisitConfigOption);
	}
	
	public Boolean getOptional() {
		return optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ProtocolVisitConfigOption getDefaultOption() {
		return defaultOption;
	}

	public void setDefaultOption(ProtocolVisitConfigOption defaultOption) {
		this.defaultOption = defaultOption;
		if (this.defaultOption != null) {
			this.defaultOptionId = this.defaultOption.getId();
		}
	}

	public Long getDefaultOptionId() {
		return defaultOptionId;
	}

	public void setDefaultOptionId(Long defaultOptionId) {
		this.defaultOptionId = defaultOptionId;
	}

	
	public boolean afterCreate() {
		// flag as the primary visit config if this is the only visit config
		if (this.getProtocolTimepointConfig().getProtocolVisitConfigs().size() == 1) {
			this.getProtocolTimepointConfig().setPrimaryProtocolVisitConfig(this);
			// this is special case as the only place that primaryProtocolVisitConfig gets set on 
			// ProtocolTimepointConfig other than its own handler. 
			// have to save ProtocolTimepointConfig here
			this.getProtocolTimepointConfig().save();
		}
		
		// always save again, as node value has been set
		return true;
	}

	
}
