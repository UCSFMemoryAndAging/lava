package edu.ucsf.lava.crms.protocol.model;

import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

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
				// there is an issue with initializing associations. since the ..Base classes map
				// the associations, the properties/getters in the ..Base classes must be used for initialization. So
				// the proxies that are created are proxies for the ..Base classes, but upon initialization, the objects
				// materialized are subclasses of the ..Base classes, and the process of initializing subclass with 
				// a ..Base proxy does not appear to work
				// for now, using lazy="false" to eagerly load associations (but not collections, which do not suffer
				// the problem), but that results in loading chains, e.g. each component loads its parent, which in
				// turn loads its parent until get to the root of the hierarchy. the hierarchy is not deep here so
				// not a major issue
				// possible solution is to implement our own initialization which gets around the problem that
				// Hibernate initialization suffers
				
				// note that its parent (ProtocolTimepoint) and configuration (ProtocolVisitConfig) are eagerly fetched
				// so no need to initialize this.getProtocolTimepointBase() and this.getProtocolVisitConfigBase()
				
				// also, options are eagerly fetched, so no init of: this.getProtocolVisitConfigBase().getProtocolVisitConfigOptionsBase()
				this.getProtocolInstrumentsBase(),
				this.getPatient(),
				this.getVisit(),
				// the rest of these are needed to allow traversal of the object graph in scheduling window calculations;
				// initialize here so do not have to explicitly retrieve them
				this.getProtocolTimepointBase().getProtocolVisitsBase(), 
				this.getProtocolTimepointBase().getProtocolBase().getProtocolTimepointsBase()
				// again, the parent of this instance, ProtocolTimepoint, has its parent and config loaded, so no need
				// to initialized these here:
				/*this.getProtocolTimepointBase().getProtocolTimepointConfigBase(), */
				/*this.getProtocolTimepointBase().getProtocolBase(),*/
				};
	}
 
	/**
	 * Convenience methods to convert ProtocolVisitBase method types to types of this subclass,
	 * since if an object of this class exists we know we can safely downcast 
	 */
	public ProtocolTimepoint getProtocolTimepoint() {
		return (ProtocolTimepoint) super.getProtocolTimepointBase();
	}
	public void setProtocolTimepoint(ProtocolTimepoint protocolTimepoint) {
		super.setProtocolTimepointBase(protocolTimepoint);
	}
	public ProtocolVisitConfig getProtocolVisitConfig() {
		return (ProtocolVisitConfig) super.getProtocolVisitConfigBase();
	}
	public void setProtocolVisitConfig(ProtocolVisitConfig protocolVisitConfig) {
		super.setProtocolVisitConfigBase(protocolVisitConfig);
	}
	public Set<ProtocolInstrument> getProtocolInstruments() {
		return (Set<ProtocolInstrument>) super.getProtocolInstrumentsBase();
	}
	public void setProtocolInstruments(Set<ProtocolInstrument> protocolInstruments) {
		super.setProtocolInstrumentsBase(protocolInstruments);
	}

	/*
	 * Method to add a ProtocolInstrument to a ProtocolVisit's collection, managing the bi-directional relationship.
	 */
	public void addProtocolInstrument(ProtocolInstrument protocolInstrument) {
		protocolInstrument.setProtocolVisit(this);
		this.getProtocolInstruments().add(protocolInstrument);
	}
	
	public void updateStatus() {

		// Completion Status
		
		// determine the compStatus by iterating thru the instruments of this visit and
		// using the most severe instrument compStatus as the overall compStatus for the visit
		String updatedCompStatus = null;
		for (ProtocolInstrument protocolInstrument : this.getProtocolInstruments()) {
			// instruments configured as optional should not be considered
			if (protocolInstrument.getProtocolInstrumentConfig().getOptional() == null 
					|| !protocolInstrument.getProtocolInstrumentConfig().getOptional()) {
				updatedCompStatus = this.rollupCompStatusHelper(protocolInstrument, updatedCompStatus);
			}
		}
		if (updatedCompStatus == null) {
			// there are no (non-optional) instruments from which to roll up a status
			this.setCompStatus(N_A);
		}
		if (this.getCompStatusOverride() == null || !this.getCompStatusOverride()) {
			this.setCompStatus(updatedCompStatus);
			// store the computed status so if user decides to override they know computed status
			this.setCompStatusComputed(updatedCompStatus);
		}
		else {
			// do not update the compStatus if there has been a user override. but stored the computed
			// value in a separate property
			this.setCompStatusComputed(updatedCompStatus);
		}
			

		// Collection Status
			
		// determine the collectWinStatus by iterating thru the instruments of this visit and
		// using the most severe instrument collectWinStatus as the overall collectWinStatus for the visit
		String updatedCollectWinStatus = null;
		for (ProtocolInstrument protocolInstrument : this.getProtocolInstruments()) {
			// instruments configured as optional should not be considered
			if (protocolInstrument.getProtocolInstrumentConfig().getOptional() == null
					|| !protocolInstrument.getProtocolInstrumentConfig().getOptional()) {
				updatedCollectWinStatus = this.rollupCollectWinStatusHelper(protocolInstrument, updatedCollectWinStatus);
			}
		}
		if (updatedCollectWinStatus == null) {
			// there are no (non-optional) instruments from which to roll up a status
			this.setCollectWinStatus(N_A);
		}
		else {
			this.setCollectWinStatus(updatedCollectWinStatus);
		}
		
			
		// Scheduling Status
		
		// compute the status of the scheduling window (schedWinStatus) as one of the following:
		// "Not Started", "Pending", "Early", "Scheduled", "Late", "N/A"

		// the status is based on the status of the assigned visit (visitStatus) in conjunction with the visit time vs.
		// the scheduling window, or, if the visit has not been scheduled yet, the current time vs. the scheduling
		// window
		
		// VisitStatus values (list table: listname='VisitStatus')
		// CAME IN
		// COMPLETE
		// NO SHOW
		// PATIENT CANCELLED
		// SCHEDULED
		// % CANCELLED (where % is app specific representing the research clinic that cancelled the visit)

		// if a visit is scheduled before a visit which it references is scheduled, then can not compute the
		// scheduling window and therefore can not determine the status
		if (this.getProtocolTimepoint().getSchedWinAnchorDate() == null) {
			this.setSchedWinStatus(PROTOCOL_NOT_STARTED);
		}
		else if (this.getVisit() == null || this.getVisit().getVisitStatus() == null ||
				(this.getVisit().getVisitStatus().toLowerCase().equals("no show") || this.getVisit().getVisitStatus().toLowerCase().endsWith("cancelled"))) {
			// Visit not assigned, or VStatus IN (NO SHOW, *CANCELLED)
			setSchedWinStatus(PENDING);
		}
		else if (this.getVisit().getVisitStatus().toLowerCase().equals("scheduled") 
				|| this.getVisit().getVisitStatus().toLowerCase().equals("came in") 
				|| this.getVisit().getVisitStatus().toLowerCase().equals("complete")) {
			// VStatus = SCHEDULED || CAME IN || COMPLETE
			// VDate before scheduling window = EARLY (SCHEDULED - EARLY)
			// VDate during scheduling window = SCHEDULED (SCHEDULED - ON TIME)
			// VDate after scheduling window = LATE (SCHEDULED - LATE)
			if (this.getVisit().getVisitDate().before(this.getProtocolTimepoint().getSchedWinStart())) {
				this.setSchedWinStatus(EARLY);
			}
			else if (this.getVisit().getVisitDate().after(this.getProtocolTimepoint().getSchedWinEnd())) {
				this.setSchedWinStatus(LATE);
			}
			else { // visit date is within the scheduling window
				this.setSchedWinStatus(SCHEDULED);
			}
		}
		else {
			// log unhandled situation
			logger.error("ProtocolVisit:" + this.getId() + " unhandled situation determining schedWinStatus");
		}
	}
	
	/** 
	 * Perform any calculations done in ProtocolVisit. This method should be called 
	 * whenever anything that may affect these calculations occurs, such as changes in the
	 * Protocol configuration, and assigning a Visit to the Protocol.
	 */
	public void calculate() {
	}
	
	public boolean afterUpdate() {
		if (this.getVisit() != null) {
			this.setSummary(new StringBuffer(this.getVisit().getVisitDescrip()).toString());
		}
		else {
			this.setSummary(null);
		}
		// note: in case the assigned Visit is modified in any way, window and status calculations are 
		// updated in ProtocolVisitHandler doSave (can not do here because this method is called within
		// an active persistence layer session and that results in "object confusion" because the entire
		// protocol tree needs to be saved (since status changes at one level affect statuses at other
		// levels) in addition to the individual save that is in process on this object)
		return true;		
	}
	
}
