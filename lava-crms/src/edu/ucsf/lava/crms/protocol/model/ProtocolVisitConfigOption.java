package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 * 
 * ProtocolVisitOptionConfig is a fulfillment option for the ProtocolVisitConfig, which 
 * has a collection of options. One of the options is the default, and any others are 
 * alternate ways in which the visit config can be fulfilled.   
 */

public class ProtocolVisitConfigOption extends ProtocolVisitConfigOptionBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolVisitConfigOption.class);
	
	public ProtocolVisitConfigOption(){
		super();
		this.setAuditEntityType("ProtocolVisitConfigOption");			
	}
	
	// facilitates allowing user to specify visitType from project other than the project for
	// which the protocol config is defined, so that patients that are co-enrolled in two or more 
	// projects will be accommodated
	// note: not using projName property as there is a projName in the base class
	private String visitTypeProjName;
	private String visitType; 
	
	/**
	 * Convenience methods to convert ProtocolVisitConfigOptionBase method types to types 
	 * of this subclass, since if an object of this class exists we know we can safely 
	 * downcast. 
	 */
	public ProtocolVisitConfig getProtocolVisitConfig() {
		return (ProtocolVisitConfig) super.getProtocolVisitConfigBase();
	}
	public void setProtocolVisitConfig(ProtocolVisitConfig protocolVisit) {
		super.setProtocolVisitConfigBase(protocolVisit);
	}

	public String getVisitTypeProjName() {
		return visitTypeProjName;
	}
	public void setVisitTypeProjName(String visitTypeProjName) {
		this.visitTypeProjName = visitTypeProjName;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
	

	protected void updateSummary() {
		StringBuffer block = new StringBuffer(this.getVisitTypeProjName()).append("~").append(this.getVisitType());
		if (this.getDefaultOption()) {
			block.append(" (default)");
		}
		this.setSummary(block.toString());
	}
	
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		this.updateSummary();
		// label is not currently used but is required so set it
		if (this.getLabel() == null) {
			String theLabel = new StringBuffer(this.getVisitTypeProjName()).append(" - ").append(this.getVisitType()).toString();
			if (theLabel.length() > 25) {
				theLabel = this.getVisitType();
			}
			this.setLabel(theLabel);
		}
	}


	
	
	/**
	 * This method returns the effective effDate for this option. This is done by going up the
	 * protocol configuration chain:
	 *   VisitConfigOption ==> VisitConfig ==> TimepointConfig
	 * and returning the latest effDate found.
	 * 
	 * note: this is named as a getter so view can reference it in an expression
	 * 
	 * @return the effective effDate
	 */
	public Date getEffectiveEffDate() {
		Date effectiveDate = this.getEffDate();
		ProtocolVisitConfig visitConfig = this.getProtocolVisitConfig();
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
	 *   VisitConfigOption ==> VisitConfig ==> TimepointConfig
	 * and returning the earliest expDate found.
	 * 
	 * note: this is named as a getter so view can reference it in an expression
	 * 
	 * @return the effective expDate
	 */
	public Date getEffectiveExpDate() {
		Date effectiveDate = this.getExpDate();
		ProtocolVisitConfig visitConfig = this.getProtocolVisitConfig();
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
