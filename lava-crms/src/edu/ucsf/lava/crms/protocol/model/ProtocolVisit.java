package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;
import java.util.Set;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
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
				// there is an issue with initializing associations. since the ..Base classes map
				// the associations, the properties/getters in the ..Base classes must be used for initialization. So
				// the proxies that are created are proxies for the ..Base classes, but upon initialization, the objects
				// materialized are subclasses of the ..Base classes, and the process of initializing subclass with 
				// a ..Base proxy does not appear to work
				// for now, using lazy="false" to eagerly load associations (but not collections which do not suffer
				// the problem), but that results in loading chains, e.g. each component loads its parent, which in
				// turn loads its parent until get to the root of the hierarchy. the hierarchy is not deep here so
				// not a major issue
				// possible solution is to implement our own initialization which gets around the problem that
				// Hibernate initialization suffers
				
				// note that its parent (Protocol) and configuration (ProtocolTimepointConfig) are eagerly fetched
				// so no need to initialize this.getProtocolTimepointBase() and this.getProtocolVisitConfigBase()
				this.getProtocolInstrumentsBase(),
				this.getPatient(),
				this.getVisit(),
				// the rest of these are needed to allow traversal of the object graph in scheduling window calculations;
				// initialize here so do not have to explicitly retrieve them
				this.getProtocolVisitConfigBase().getProtocolVisitConfigOptionsBase(),
				this.getProtocolTimepointBase().getProtocolVisitsBase(), 
				this.getProtocolTimepointBase().getProtocolBase().getProtocolTimepointsBase()
				// again, the parent of this instance, ProtocolTimepoint, has its parent and config loaded, so no need
				// to initialized these here:
				/*this.getProtocolTimepointBase().getProtocolTimepointConfigBase(), */
				/*this.getProtocolTimepointBase().getProtocolBase(),*/
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
	
	
	public void updateStatus() {

		// Completion Status
		
		// determine the compStatus by iterating thru the instruments of this visit and
		// using the most severe instrument compStatus as the overall compStatus for the visit
		String updatedCompStatus = null;
		for (ProtocolInstrument protocolInstrument : this.getProtocolInstruments()) {
			// instruments configured as optional should not be considered
			if (!protocolInstrument.getProtocolInstrumentConfig().getOptional()) {
				updatedCompStatus = this.rollupCompStatusHelper(protocolInstrument, updatedCompStatus);
			}
		}
		this.setCompStatus(updatedCompStatus);
			

		// Collection Status
			
		// determine the collectWinStatus by iterating thru the instruments of this visit and
		// using the most severe instrument collectWinStatus as the overall collectWinStatus for the visit
		String updatedCollectWinStatus = null;
		for (ProtocolInstrument protocolInstrument : this.getProtocolInstruments()) {
			// instruments configured as optional should not be considered
			if (!protocolInstrument.getProtocolInstrumentConfig().getOptional()) {
				updatedCollectWinStatus = this.rollupCollectWinStatusHelper(protocolInstrument, updatedCollectWinStatus);
			}
		}
		this.setCollectWinStatus(updatedCollectWinStatus);
		
			
		// Scheduling Status
		
		// compute the status of the scheduling window (schedWinStatus) as one of the following:
//TODO: if in fact this status represents whether a visit was scheduled and completed vs. just scheduled,
//then the status "Scheduled" is confusing, because in the Visit statuses, Complete and Came In indicated
//completion, whereas Scheduled just indicates the visit has been scheduled	withour regard for whether it
//completed			
		// "Pending", "Pending - Late", "Early", "Scheduled", "Late", "N/A"
// and possibly "TBD", "Late"==>"Scheduled Late" (which would prob. mean "Early"==>"Scheduled - Early"		
		
		// the status is based on the status of the assigned visit (visitStatus) in conjunction with the visit time vs.
		// the scheduling window, or, if the visit has not completed yet, the current time vs. the scheduling
		// window
		
//TODO: decide whether better to use VisitStatus or dcStatus(compStatus), i.e. will/should VisitStatus be reliably updated on top of dcStatus		
		// VisitStatus values (list table: listname='VisitStatus')
		// CAME IN
		// COMPLETE
		// NO SHOW
		// PATIENT CANCELLED
		// SCHEDULED
		// % CANCELLED (where % is app specific representing the research clinic that cancelled the visit)

		// if a visit is scheduled before a visit which it references is scheduled, then can not compute the
		// scheduling window and therefore can not determine the status
		if (this.getProtocolTimepoint().getSchedAnchorDate() == null) {
			this.setSchedWinStatus(TBD);
//??			this.setSchedWinReason/Note("References unscheduled visit");
		}
		else if (this.getVisit() == null || this.getVisit().getVisitStatus() == null ||
				(this.getVisit().getVisitStatus().toLowerCase().equals("no show") || this.getVisit().getVisitStatus().toLowerCase().endsWith("cancelled"))) {
			// Visit not assigned, or VStatus IN (NO SHOW, *CANCELLED) 
			
			Date currDate = new Date();
			if (currDate.before(this.getProtocolTimepoint().getSchedWinStart())) {
				// current date before scheduling window = PENDING
				this.setSchedWinStatus(PENDING);
			}
			else if (currDate.after(this.getProtocolTimepoint().getSchedWinEnd())) {
				// current date past scheduling window = PENDING - LATE
				this.setSchedWinStatus(PENDING_LATE);
			}
			else { // current date is within the scheduling window
				this.setSchedWinStatus(PENDING);
			}
		}
		else if (this.getVisit().getVisitStatus().toLowerCase().equals("scheduled")) {
			// VStatus = SCHEDULED
			// note: compare current date instead of VDate because VDate represents completed Visit, e.g. just
			//       because VDate is within scheduling window where VStatus=SCHEDULED, if current date is past
			//       scheduling window, visit is late
//TODO: questions about before/within scheduling window. 
//a) when Visit scheduled before/within scheduling window, since Visit scheduled and everything on time, temptation is to 
//   set schedWinStatus to SCHEDULED, but since Visit is not complete yet, PENDING is more accurate			
//b) given assigning status of PENDING (or PENDING - LATE), does not distinguish between visit assigned and above where visit 
//   not assigned. but the fact that the visit is not assigned should be highlighted along with schedWinStatus so that 
//   should clarify			
			// current date before scheduling window = PENDING
			// current date within scheduling window = PENDING
			// current date after scheduling window = PENDING - LATE
			Date currDate = new Date();
			if (currDate.before(this.getProtocolTimepoint().getSchedWinStart())) {
				// current date before scheduling window = PENDING
				this.setSchedWinStatus(PENDING);
			}
			else if (currDate.after(this.getProtocolTimepoint().getSchedWinEnd())) {
				// current date past scheduling window = PENDING - LATE
				// note: does not matter if Visit with VStatus=SCHEDULED is later than the current date so is still
				// on time in terms of the VDate. what matters is the protocol scheduling window dates. taking this a
				// step further, when VDate arrives if VStatus changes to COMPLETE, then schedWinStatus becomes
				// SCHEDULED_LATE
				this.setSchedWinStatus(PENDING_LATE);
			}
			else { // current date is within the scheduling window
				this.setSchedWinStatus(PENDING_NOW);
			}
		}
		else if (this.getVisit().getVisitStatus().toLowerCase().equals("came in") || this.getVisit().getVisitStatus().toLowerCase().equals("complete")) {
			// VStatus = CAME IN || COMPLETE
			// VDate before scheduling window = EARLY (SCHEDULED - EARLY)
			// VDate during scheduling window = SCHEDULED (SCHEDULED - ON TIME)
			// VDate after scheduling window = LATE (SCHEDULED - LATE)
			if (this.getVisit().getVisitDate().before(this.getProtocolTimepoint().getSchedWinStart())) {
				// current date before scheduling window = PENDING
				this.setSchedWinStatus(EARLY);
			}
			else if (this.getVisit().getVisitDate().after(this.getProtocolTimepoint().getSchedWinEnd())) {
//TODO: distinguishing between PENDING - LATE (Alert), and LATE (Deviation), so should LATE be changed 
//to SCHEDULED - LATE to make things more clear?
				// current date past scheduling window = PENDING - LATE
				this.setSchedWinStatus(LATE);
			}
			else { // current date is within the scheduling window
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
			this.setAssignDescrip(new StringBuffer("Visit: ").append(this.getVisit().getVisitDescrip()).toString());
		}
		// in case visit has been modified, need to recalculate windows
		LavaDaoFilter filter = newFilterInstance();
		filter.addDaoParam(filter.daoNamedParam("protocolId", this.getProtocolTimepoint().getProtocol().getId()));
		Protocol protocolTree = (Protocol) EntityBase.MANAGER.findOneByNamedQuery("protocol.completeProtocolTree", filter);
		protocolTree.calculate();
		// return true to save any changes
		return true;
	}
	
}
