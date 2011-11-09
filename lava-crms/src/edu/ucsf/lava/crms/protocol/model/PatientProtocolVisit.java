package edu.ucsf.lava.crms.protocol.model;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.scheduling.model.Visit;

/**
 * 
 * @author ctoohey
 * 
 *
 */

public class PatientProtocolVisit extends PatientProtocolVisitBase {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolVisit.class);
	
	public PatientProtocolVisit(){
		super();
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getPatientProtocolTimepoint(), this.getProtocolVisit(), this.getInstruments(),
				// the rest of these are needed to allow traversal of the object graph in scheduling window calculations
				// so do not have to explicitly retrieve them
				// TODO: look at modifying transaction configuration to leave Hibernate session open during entire request
				// rather than opening/closing on each call to the DAO, so that the object graph can be traversed as 
				// needed without this kind of configuration
				this.getProtocolVisit().getOptions(),
				this.getPatientProtocolTimepoint().getProtocolTimepoint(), 
				this.getPatientProtocolTimepoint().getVisits(), 
				this.getPatientProtocolTimepoint().getPatientProtocol(),
				this.getPatientProtocolTimepoint().getPatientProtocol().getTimepoints()
				};
	}

	private Visit visit;
	// visitId is part of the mechanism required to set the Visit association, allowing the user
	// to designate a visit in the view which binds the visit id to visitId. visitId is then used 
	// when saving a PatientProtocolVisit to set the Visit association
	private Long visitId; 
	
	/**
	 * Convenience method to return PatientProtocolTimepoint instead of PatientProtocolTimepointBase
	 */
//	public PatientProtocolTimepoint getPatientProtocolTimepoint() {
//		return (PatientProtocolTimepoint) super.getPatientProtocolTimepoint();
//	}
	
	/**
	 * Convenience method to return ProtocolVisit instead of ProtocolVisitBase
	 */
//	public ProtocolVisit getProtocolVisit() {
//		return (ProtocolVisit) super.getProtocolVisit();
//	}

	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
		// this is the paradigm used to set associations via the UI
		if (this.visit != null) {
			this.visitId = this.visit.getId();
		}	
	}
	public Long getVisitId() {
		return visitId;
	}
	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
	
	
}
