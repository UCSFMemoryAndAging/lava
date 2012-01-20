package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;

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
public class ProtocolInstrumentConfigOption extends ProtocolInstrumentConfigOptionBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrumentConfigOption.class);
	
	public ProtocolInstrumentConfigOption(){
		super();
		this.setAuditEntityType("ProtocolInstrumentConfigOption");		
	}
	
	private String instrType; 
	
	/**
	 * Convenience methods to convert ProtocolInstrumentConfigOptionBase method types to types 
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
	
	/**
	 * This method returns the effective effDate for this option. This is done by going up the
	 * protocol configuration chain:
	 *   InstrumentConfigOption ==> InstrumentConfig ==> VisitConfig ==> TimepointConfig
	 * and returning the latest effDate found.
	 * 
	 * note: this is named as a getter so view can reference it in an expression
	 * 
	 * @return the effective effDate
	 */
	public Date getEffectiveEffDate() {
		Date effectiveDate = this.getEffDate();
		ProtocolInstrumentConfig instrConfig = this.getProtocolInstrumentConfig();
		if (effectiveDate == null || (instrConfig.getEffDate() != null 
				&& instrConfig.getEffDate().after(effectiveDate))) {
			effectiveDate = instrConfig.getEffDate();
		}
		ProtocolVisitConfig visitConfig = instrConfig.getProtocolVisitConfig();
		if (effectiveDate == null || (visitConfig.getEffDate() != null 
				&& visitConfig.getEffDate().after(effectiveDate))) {
			effectiveDate = visitConfig.getEffDate();
		}
		ProtocolTimepointConfig timepointConfig = visitConfig.getProtocolTimepointConfig();
		if (effectiveDate == null || (timepointConfig.getEffDate() != null 
				&& timepointConfig.getEffDate().after(effectiveDate))) {
			effectiveDate = timepointConfig.getEffDate();
		}
		return effectiveDate;
	}
	
	
	/**
	 * This method returns the effective expDate for this option. This is done by going up the
	 * protocol configuration chain:
	 *   InstrumentConfigOption ==> InstrumentConfig ==> VisitConfig ==> TimepointConfig
	 * and returning the earliest expDate found.
	 * 
	 * note: this is named as a getter so view can reference it in an expression
	 * 
	 * @return the effective expDate
	 */
	public Date getEffectiveExpDate() {
		Date effectiveDate = this.getExpDate();
		ProtocolInstrumentConfig instrConfig = this.getProtocolInstrumentConfig();
		if (effectiveDate == null || (instrConfig.getExpDate() != null 
				&& instrConfig.getExpDate().before(effectiveDate))) {
			effectiveDate = instrConfig.getExpDate();
		}
		ProtocolVisitConfig visitConfig = instrConfig.getProtocolVisitConfig();
		if (effectiveDate == null || (visitConfig.getExpDate() != null 
				&& visitConfig.getExpDate().before(effectiveDate))) {
			effectiveDate = visitConfig.getExpDate();
		}
		ProtocolTimepointConfig timepointConfig = visitConfig.getProtocolTimepointConfig();
		if (effectiveDate == null || (timepointConfig.getExpDate() != null 
				&& timepointConfig.getExpDate().before(effectiveDate))) {
			effectiveDate = timepointConfig.getExpDate();
		}
		return effectiveDate;
	}
	
}
