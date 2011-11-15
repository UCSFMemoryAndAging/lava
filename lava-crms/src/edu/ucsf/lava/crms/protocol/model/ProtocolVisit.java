package edu.ucsf.lava.crms.protocol.model;

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

public class ProtocolVisit extends ProtocolVisitBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolVisit.class);
	
	public ProtocolVisit(){
		super();
		this.setAuditEntityType("ProtocolVisit");		
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				//TODO: at the moment there is an issue with initializing associations. since the ..Base classes map
				//the associations, the properties/getters in the ..Base classes must be used for initialization. So
				//the proxies that are created are proxies for the ..Base classes, but upon initialization, the objects
				//materialized are subclasses of the ..Base classes, and the process of initializing subclass with 
				//a ..Base proxy does not appear to work
				//for now, using lazy="false" to eagerly load associations (but not collections which do not suffer
				//the problem), but that results in loading chains, e.g. each component loads its parent, which in
				//turn loads its parent until get to the root of the hierarchy. this is why the following are
				//commented out.
				//possible solution is to implement our own initialization which gets around the problem that
				//Hibernate initialization suffers
				
				/*this.getTimepointBase(), */
				/*this.getVisitConfigBase(), */
				this.getInstrumentsBase(),
				this.getPatient(),
				this.getVisit(),
				// the rest of these are needed to allow traversal of the object graph in scheduling window calculations
				// so do not have to explicitly retrieve them
				// TODO: look at modifying transaction configuration to leave Hibernate session open during entire request
				// rather than opening/closing on each call to the DAO, so that the object graph can be traversed as 
				// needed without this kind of configuration
				this.getVisitConfigBase().getOptionsBase(),
				/*this.getTimepointBase().getTimepointConfigBase(), */
				this.getTimepointBase().getVisitsBase(), 
				/*this.getTimepointBase().getProtocolBase(),*/
				this.getTimepointBase().getProtocolBase().getTimepointsBase()
				};
	}
 
	private Visit visit;
	// visitId is part of the mechanism required to set the Visit association, allowing the user
	// to designate a visit in the view which binds the visit id to visitId. visitId is then used 
	// when saving a ProtocolVisit to set the Visit association
	private Long visitId; 
	
	/**
	 * Convenience methods to convert ProtocolVisitBase method types to types of this subclass,
	 * since if an object of this class exists we know we can safely downcast 
	 */
	public ProtocolTimepoint getTimepoint() {
		return (ProtocolTimepoint) super.getTimepointBase();
	}
	public void setTimepoint(ProtocolTimepoint timepoint) {
		super.setTimepointBase(timepoint);
	}
	public ProtocolVisitConfig getVisitConfig() {
		return (ProtocolVisitConfig) super.getVisitConfigBase();
	}
	public void setVisitConfig(ProtocolVisitConfig config) {
		super.setVisitConfigBase(config);
	}
	public Set<ProtocolInstrument> getInstruments() {
		return (Set<ProtocolInstrument>) super.getInstrumentsBase();
	}
	public void setInstruments(Set<ProtocolInstrument> patientProtocolInstruments) {
		super.setInstrumentsBase(patientProtocolInstruments);
	}

	/*
	 * Method to add an instrument to a visit, managing the bi-directional relationship.
	 */
	public void addInstrument(ProtocolInstrument patientProtocolInstrument) {
		patientProtocolInstrument.setVisit(this);
		this.getInstruments().add(patientProtocolInstrument);
	}
	
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
