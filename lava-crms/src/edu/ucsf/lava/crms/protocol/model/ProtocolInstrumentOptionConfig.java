package edu.ucsf.lava.crms.protocol.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 * ProtocolInstrumentOptionConfig is a fulfillment option for the ProtocolInstrumentConfig, which 
 * has a collection of options. One of the options is the default, and any others are alternate
 * ways in which the instrument config can be fulfilled.   
 */
public class ProtocolInstrumentOptionConfig extends ProtocolInstrumentOptionConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentOptionConfig.class);
	
	public ProtocolInstrumentOptionConfig(){
		super();
		this.setAuditEntityType("ProtocolInstrumentOptionConfig");		
	}
	
	private String instrType; 
	
	/**
	 * Convenience methods to convert ProtocolInstrumentOptionConfigBase method types to types 
	 * of this subclass, since if an object of this class exists we know we can safely downcast. 
	 */
	public ProtocolInstrumentConfig getProtocolInstrumentConfig() {
		return (ProtocolInstrumentConfig) super.getProtocolInstrumentConfigBase();
	}
	public void setProtocolInstrumentConfig(ProtocolInstrumentConfig protocolInstrumentConfig) {
		super.setProtocolInstrumentConfigBase(protocolInstrumentConfig);
	}
	
	public String getInstrType() {
		return instrType;
	}
	public void setInstrType(String instrType) {
		this.instrType = instrType;
	}
}
