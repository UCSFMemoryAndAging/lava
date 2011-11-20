package edu.ucsf.lava.crms.protocol.model;

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

public class ProtocolVisitOptionConfig extends ProtocolVisitOptionConfigBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolVisitOptionConfig.class);
	
	public ProtocolVisitOptionConfig(){
		super();
		this.setAuditEntityType("ProtocolVisitOptionConfig");			
	}
	
	// facilitates allowing user to specify visitType from project other than the project for
	// which the protocol config is defined, so that patients that are co-enrolled in two or more 
	// projects will be accommodated
	private String visitTypeProjName;
	private String visitType; 
	
	/**
	 * Convenience methods to convert ProtocolVisitOptionConfigBase method types to types 
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
	
}
