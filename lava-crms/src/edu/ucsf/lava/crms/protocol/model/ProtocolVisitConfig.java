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
		// options are eagerly fetched
		return new Object[]{
				this.getProtocolInstrumentConfigsBase(), 
				this.getProtocolTimepointConfigBase().getProtocolVisitConfigsBase()};
	}
	
	private String category;
	
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
	 * Convenience method to check if this is the primary ProtocolVisitConfig. An association to the primary
	 * VisitConfig is stored in the ProtocolTimepointConfig parent of the ProtocolVisitConfig collection (as 
	 * ooposed to having a flag on each ProtocolVisitConfig to designate itself as the primary, which would 
	 * require synchronization among all sibling ProtocolVisitConfigs)
	 * 
	 * note: using the id property here instead of getPrimaryProtocolVisitConfig.getId() means do not
	 * have to eagerly load the first ProtocolVisitConfig
	 * 
	 * @return true if this is the primary visit config, false if not.
	 */
	public Boolean isPrimaryProtocolVisitConfig() {
		// if adding, no id since it has not been saved yet
		if (this.getId() == null) {
			// if there is not a primary yet, then this will be the first, as the business rule is that
			// if there is only one visit config it must be marked as the primary
			if (this.getProtocolTimepointConfig().getPrimaryProtocolVisitConfigId() == null) {
				return Boolean.TRUE;
			}
			else {
				return Boolean.FALSE;
			}
		}
		else {
			// existing ProtocolVisitConfig, so can check the ProtocolTimepointConfig to see if this is
			// marked as the primary
			return this.getProtocolTimepointConfig().getPrimaryProtocolVisitConfigId().equals(this.getId());
		}
	}
	
	
	/*
	 * Method to add a ProtocolVisitConfigOption to a ProtocolVisit's collection, managing the bi-directional relationship.
	 */	
	public void addOption(ProtocolVisitConfigOption protocolVisitConfigOption) {
		protocolVisitConfigOption.setProtocolVisitConfig(this);
		this.getOptions().add(protocolVisitConfigOption);
	}
	
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	protected void updateSummary() {
		StringBuffer block = new StringBuffer();
		if (isPrimaryProtocolVisitConfig()) {
			block.append("Primary Visit");
		}
		if (this.getOptional()) {
			block.append(" (optional)");
		}
		
		this.setSummary(block.toString());
	}
	
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		
		this.updateSummary();
		
		for (ProtocolVisitConfigOption visitConfigOption : this.getOptions()) {
			visitConfigOption.updateSummary();
		}
	}
	
	
	public void beforeCreate() {
		super.beforeCreate();
		this.setNodeType(VISIT_NODE);
	}


	public boolean afterCreate() {
		// flag as the primary visit config if this is the only visit config
		// note: have to do in afterCreate because need the id of the newly created entity
		if (this.getProtocolTimepointConfig().getPrimaryProtocolVisitConfigId() == null) {
			this.getProtocolTimepointConfig().setPrimaryProtocolVisitConfig(this);
			// this is special case as the only place that primaryProtocolVisitConfig gets set on 
			// ProtocolTimepointConfig other than its own handler. 
			// have to save ProtocolTimepointConfig here
			this.getProtocolTimepointConfig().save();
		}

		// not need to save this entity and parent saved explicitly above
		return false;
	}

	
}
