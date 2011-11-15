package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

/**
 * 
 * @author ctoohey
 *
 *
 */
public class Protocol extends ProtocolBase {
	public static EntityManager MANAGER = new EntityBase.Manager(Protocol.class);
	
	public Protocol(){
		super();
		this.setAuditEntityType("Protocol");
		this.enrolledDate = new Date();
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{/*this.getProtocolBase(),*/this.getPatient(),this.getTimepointsBase()};
	}

	private Date enrolledDate;
	
	/**
	 * Convenience methods to convert ProtocolBase method types to types of this subclass,
	 * since if an object of this class exists we know we can safely downcast 
	 */
	public ProtocolConfig getProtocolConfig() {
		return (ProtocolConfig) super.getProtocolConfigBase();
	}
	public void setProtocolConfig(ProtocolConfig config) {
		super.setProtocolConfigBase(config);
	}
	public Set<ProtocolTimepoint> getTimepoints() {
		return (Set<ProtocolTimepoint>) super.getTimepointsBase();
	}
	public void setTimepoints(Set<ProtocolTimepoint> patientProtocolTimepoints) {
		super.setTimepointsBase(patientProtocolTimepoints);
	}

	/*
	 * Method to add a ProtocolTimepoint to a Protocol, managing the bi-directional relationship.
	 */	
	public void addTimepoint(ProtocolTimepoint protocolTimepoint) {
		protocolTimepoint.setProtocol(this);
		this.getTimepoints().add(protocolTimepoint);
	}	

	public Date getEnrolledDate() {
		return enrolledDate;
	}

	public void setEnrolledDate(Date enrolledDate) {
		this.enrolledDate = enrolledDate;
	}

}
