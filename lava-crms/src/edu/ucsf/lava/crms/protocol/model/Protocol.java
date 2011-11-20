package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;

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
		this.assignedDate = new Date();
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				/*this.getProtocolBase(),*/
				this.getPatient(),
				this.getEnrollmentStatus(),
				this.getProtocolTimepointsBase()};
	}

	private Date assignedDate;
	private EnrollmentStatus enrollmentStatus;
	
	/**
	 * Convenience methods to convert ProtocolBase method types to types of this subclass,
	 * since if an object of this class exists we know we can safely downcast 
	 */
	public ProtocolConfig getProtocolConfig() {
		return (ProtocolConfig) super.getProtocolConfigBase();
	}
	public void setProtocolConfig(ProtocolConfig protocolConfig) {
		super.setProtocolConfigBase(protocolConfig);
	}
	public Set<ProtocolTimepoint> getProtocolTimepoints() {
		return (Set<ProtocolTimepoint>) super.getProtocolTimepointsBase();
	}
	public void setProtocolTimepoints(Set<ProtocolTimepoint> protocolTimepoints) {
		super.setProtocolTimepointsBase(protocolTimepoints);
	}

	/*
	 * Method to add a ProtocolTimepoint to a Protocol, managing the bi-directional relationship.
	 */	
	public void addProtocolTimepoint(ProtocolTimepoint protocolTimepoint) {
		protocolTimepoint.setProtocol(this);
		this.getProtocolTimepoints().add(protocolTimepoint);
	}	

	public Date getAssignedDate() {
		return assignedDate;
	}

	public void setAssignedDate(Date assignedDate) {
		this.assignedDate = assignedDate;
	}

	public EnrollmentStatus getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	
	/**
	 * This method iterates thru the entire Protocol tree structure, calling calculate on every
	 * node. This method requires that the complete Protocol tree is loaded.
	 */
	public void calculate() {
		// if there is anything to calculate on the Protocol node, do it here
		
		for (ProtocolTimepoint protocolTimepoint: this.getProtocolTimepoints()) {
			protocolTimepoint.calculate();
			for (ProtocolVisit protocolVisit: protocolTimepoint.getProtocolVisits()) {
				protocolVisit.calculate();
				for (ProtocolInstrument protocolInstrument: protocolVisit.getProtocolInstruments()) {
					protocolInstrument.calculate();
				}
			}			
		}

	}
	
	public boolean afterCreate() {
		// as part of creation, the full protocol tree has been created so safe to call calculate
		//TODO: not much point in doing this unless the process of creating Protocol, i.e. assigning patient
		//to protocol, includes assigning a Visit to the first timepoint
		this.calculate();
		// return true to save again
		return true;
	}
	
	

}
