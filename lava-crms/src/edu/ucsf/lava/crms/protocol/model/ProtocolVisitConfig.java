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
		return new Object[]{this.getInstrumentsBase(), this.getOptionsBase()};
	}
	
	private Boolean optional;
	
	/**
	 * Convenience methods to convert ProtocolVisitConfigBase method types to types of 
	 * this subclass, since if an object of this class exists we know we can safely downcast. 
	 */
	public ProtocolTimepointConfig getTimepointConfig() {
		return (ProtocolTimepointConfig) super.getTimepointConfigBase();
	}
	public void setTimepointConfig(ProtocolTimepointConfig config) {
		super.setTimepointConfigBase(config);
	}
	public Set<ProtocolInstrumentConfig> getInstruments() {
		return (Set<ProtocolInstrumentConfig>) super.getInstrumentsBase();
	}
	public void setInstruments(Set<ProtocolInstrumentConfig> protocolInstruments) {
		super.setInstrumentsBase(protocolInstruments);
	}
	public Set<ProtocolVisitOptionConfig> getVisitOptions() {
		return (Set<ProtocolVisitOptionConfig>) super.getOptionsBase();
	}
	public void setVisitOptions(Set<ProtocolVisitOptionConfig> visitOptions) {
		super.setOptionsBase(visitOptions);
	}
	
	/*
	 * Method to add a ProtocolInstrumentConfig to a ProtocolVisitConfig, managing the bi-directional relationship.
	 */	
	public void addInstrument(ProtocolInstrumentConfig instrumentConfig) {
		instrumentConfig.setVisitConfig(this);
		this.getInstruments().add(instrumentConfig);
	}
	
	/*
	 * Method to add an option to a visit, managing the bi-directional relationship.
	 */	
	public void addOption(ProtocolVisitOptionConfig visitOption) {
		visitOption.setVisit(this);
		this.getVisitOptions().add(visitOption);
	}
	
	public Boolean getOptional() {
		return optional;
	}
	public void setOptional(Boolean optional) {
		this.optional = optional;
	}

}
