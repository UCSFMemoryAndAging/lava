package edu.ucsf.lava.crms.protocol.model;

import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_CANCELLED;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_COMPLETE;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_INCOMPLETE;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_PARTIALLY_COMPLETE;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_SCHEDULED;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_UNKNOWN;

import java.util.Calendar;
import java.util.Date;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;

public class ProtocolInstrument extends ProtocolInstrumentBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolInstrument.class);
	
	public ProtocolInstrument(){
		super();
		this.setAuditEntityType("ProtocolInstrument");
		this.addPropertyToAuditIgnoreList("visit");		
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				// note that its parent (ProtocolVisit) and configuration (ProtocolInstrumentConfig) are eagerly fetched
				// so no need to initialize those
				this.getProtocolInstrumentConfigBase().getProtocolInstrumentConfigOptionsBase(),
				this.getPatient()};
	}

	private Instrument instrument;
	// instrId is part of the mechanism required to set the Instrument association, allowing the user
	// to designate an instrument which binds the instrument id to instrId. instrId is then used 
	// when saving a ProtocolInstrument to set the Instrument association
	private Long instrId;

	// collection window calculated either from custom configuration in its ProtocolInstrumentConfig, or the default collection
	// window defined in ProtocolTimepointConfig
	private ProtocolVisit collectWinProtocolVisit; // this is set in ProtocolHandler doSaveAdd when Protocol assigned to a Patient
	private Date collectWinStart;
	private Date collectWinEnd;
	private Date collectAnchorDate; 
/** moved to ProtocolNode	
	private String collectWinStatus;
	private String collectWinReason;
	private String collectWinNote;
**/	
	
	/**
	 * Convenience methods to convert ProtocolInstrumentBase method types to types of this subclass,
	 * since if an object of this class exists we know we can safely downcast 
	 */
	public ProtocolVisit getProtocolVisit() {
		return (ProtocolVisit) super.getProtocolVisitBase();
	}
	public void setProtocolVisit(ProtocolVisit protocolVisit) {
		super.setProtocolVisitBase(protocolVisit);
	}
	public ProtocolInstrumentConfig getProtocolInstrumentConfig() {
		return (ProtocolInstrumentConfig) super.getProtocolInstrumentConfigBase();
	}
	public void setProtocolInstrumentConfig(ProtocolInstrumentConfig protocolInstrumentConfig) {
		super.setProtocolInstrumentConfigBase(protocolInstrumentConfig);
	}

	
	public Instrument getInstrument() {
		return instrument;
	}
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
		// this is the paradigm used to set associations via the UI
		if (this.instrument != null) {
			this.instrId = this.instrument.getId();
		}	
	}
	public Long getInstrId() {
		return instrId;
	}
	public void setInstrId(Long instrId) {
		this.instrId = instrId;
	}
	
	public ProtocolVisit getCollectWinProtocolVisit() {
		return collectWinProtocolVisit;
	}
	public void setCollectWinProtocolVisit(ProtocolVisit collectWinProtocolVisit) {
		this.collectWinProtocolVisit = collectWinProtocolVisit;
	}
	public Date getCollectWinStart() {
		return collectWinStart;
	}
	public void setCollectWinStart(Date collectWinStart) {
		this.collectWinStart = collectWinStart;
	}
	public Date getCollectWinEnd() {
		return collectWinEnd;
	}
	public void setCollectWinEnd(Date collectWinEnd) {
		this.collectWinEnd = collectWinEnd;
	}
	public Date getCollectAnchorDate() {
		return collectAnchorDate;
	}
	public void setCollectAnchorDate(Date collectAnchorDate) {
		this.collectAnchorDate = collectAnchorDate;
	}

	/**
	 * Calculate the ideal date that this instrument would be collected on. This calculation 
	 * should be done whenever configuration is changed. The date is persisted in the  
	 * ProtocolInstrument collectAnchorDate property.
	 * 
	 * A ProtocolTimepointConfig defines a collection window relative to the 
	 * timepoint's primary visit, which serves as the default collection window for all of the 
	 * timepoint's instruments. However each instrument can override this collection window
	 * by defining its own custom collection window in ProtocolInstrumentOptionConfig. 
	 *  
	 * If a Visit has not yet been associated with the defined ProtocolVisit, then the 
	 * collectAnchorDate can not be calculated, and this method returns null. 
	 * note: if it is a business rule (TBD) that an Instrument can not be assigned to a 
	 * ProtocolInstrument until a Visit has been assigned to the ProtocolInstrument's ProtocolVisit, 
	 * this should not occur.   
	 */
	public Date calcCollectAnchorDate() {
		// the collection window is based on the visitDate of a visit assigned to this Protocol
		
		// first determine if this instrument defines a custom collection window visit (a
		// ProtocolVisitConfig) within the same timepoint as this instrument in its
		// ProtocolInstrumentConfig. if so, use the Visit associated with the ProtocolVisit
		// defined in the configuration
		if (this.getProtocolInstrumentConfig().getCustomCollectWinDefined()) {
			if (this.getCollectWinProtocolVisit() != null && this.getCollectWinProtocolVisit().getVisit() != null) {
				return this.getCollectWinProtocolVisit().getVisit().getVisitDate();
			}
			else {
				return null;
			}
		}
		// if there is no custom collection window visit defined for this instrument, then
		// use the Visit associated with the primary ProtocolVisit as defined by the timepoint, 
		// ProtocolTimepointConfig
//TODO: believe this works without explicitly retrieving ProtocolTimepointConfig in getAssociationsToInitialize 
//because on retrieval of ProtocolInstrument eager fetching fetches ProtocolVisit (and ProtocolInstrumentConfig) 
//which in turn has eager fetching that fetches ProtocolTimepoint (and ProtocolVisitConfig) which in turn eagerly
//fetches ProtocolTimepointConfig (and Protocol)		
		else if (this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWindowDefined()) { 
			if (this.getProtocolVisit().getProtocolTimepoint().getPrimaryProtocolVisit() != null &&
				this.getProtocolVisit().getProtocolTimepoint().getPrimaryProtocolVisit().getVisit() != null) {
				return this.getProtocolVisit().getProtocolTimepoint().getPrimaryProtocolVisit().getVisit().getVisitDate();
			}		
			else {
				return null;
			}
		}
		else {
			// no collection window defined
			return null;
		}
	}
	
	
	/**
	 * Given the calculation done by calcCollectAnchorDate, calculate a window defined in this protocol's
	 * configuration representing the acceptable window within which data collection for the Instrument
	 * assigned to this ProtocolInstrument should occur.
	 * 
	 * The configuration's collectWinOffset determines the beginning of the collection window,
	 * collectWinStart, as follows:
	 *  0 equals the collectAnchorDate
	 *  negative value represents number of days before collectAnchorDate
	 *  positive value represents number of days after collectAnchorDate
	 *  
	 * Once the beginning of the collection window is determined, collectWinSize represents the size of
	 * the window and can be used to determine collectWinEnd.
	 */
	public void calcCollectWinDates() {
		this.setCollectAnchorDate(this.calcCollectAnchorDate());
		
		if (this.getCollectAnchorDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.getCollectAnchorDate());
			
			// if there are custom collection window attributes configured in the ProtocolInstrumentConfig,
			// use those
			if (this.getProtocolInstrumentConfig().getCustomCollectWinDefined()) {
				if (this.getProtocolInstrumentConfig().getCustomCollectWinSize() != null 
						&& this.getProtocolInstrumentConfig().getCustomCollectWinOffset() != null) {
					// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
					// window around collectAnchorDate
					cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinOffset());
					this.setCollectWinStart(cal.getTime());
					cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinSize());
					this.setCollectWinEnd(cal.getTime());
				}
			}
			// otherwise, use the default collection window configuration for the timepoint to which
			// this instrument belongs
			else if (this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWindowDefined()) { 
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectAnchorDate
				cal.add(Calendar.DATE, this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWinOffset());
				this.setCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWinSize());
				this.setCollectWinEnd(cal.getTime());
			}
		}
	}


	public void updateStatus() {
		// Completion Status
		
		// Compute the Completion Status as one of the following:
		// "Pending", "In Progress", "Completed", "Partial", "Not Completed"
		
		// the compStatus property is used for the completion status of the instrument, without regard for
		// whether or not the instrument was completed within the collection window or not (that will be
		// reflected in the collectWinStatus)
		
		// compStatus is a calculated property, but may be modified by the user under certain circumstances. 
		// compReason is available for the user to augment and/or override compStatus, a kind of secondary 
		// status, and compNotes is available for the user to further clarify the completion status
		
		// Instrument DCStatus values (list table: listname='DataCollectionStatus')
		// Cancelled
		// Cancelled - Alt Test Given
		// Cancelled - Patient Factor
		// Cancelled - Situational
		// Scheduled
		// Incomplete
		// Incomplete - Not Returned
		// Incomplete - Scoring
		// Complete - Partially 
		// Complete
		// Unknown
		// note: the above "Incomplete" statuses indicate that collection is in progress, i.e. the
		// data is incomplete but collection is not complete yet, as opposed to the status
		// "Complete - Partial", which indicates that the data is incomplete and collection is
		// complete

		
		Date winStart, winEnd;
		Date currDate = new Date();
		// the status determination uses the window during which the instrument should have been completed. use the collection
		// window if it has been defined. otherwise use the scheduling window (there can be a default collection window defined
		// at the timepoint config level and/or a custom collection window defined at the instrument config level)
		// configured. the custom collection window visit will be null)
		if (this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWindowDefined() ||
				this.getProtocolInstrumentConfig().getCustomCollectWinDefined()) {
			winStart = this.getCollectWinStart();
			winEnd = this.getCollectWinEnd();
		}
		else {
			// collection window not configured, so use the the scheduling window
			winStart = this.getProtocolVisit().getProtocolTimepoint().getSchedWinStart();
			winEnd= this.getProtocolVisit().getProtocolTimepoint().getSchedWinEnd(); 
		}
		
		if (winStart == null || winEnd == null) {
			// if a collection window references a visit that has not been assigned to the protocol, then can not compute the
			// collection window and therefore can not determine which status to assign in this situation
			
			// collection window not defined
//TODO: determine what this should be				
			this.setCompStatus("TBD");
		}
		else if (this.instrument == null || this.instrument.getDcStatus() == null 
				|| this.instrument.getDcStatus().startsWith(STATUS_CANCELLED)
				|| this.instrument.getDcStatus().equals(STATUS_UNKNOWN)
				|| this.instrument.getDcStatus().equals(STATUS_SCHEDULED)) {
			if (currDate.before(winStart)) {
				// before collection window
				this.setCompStatus(PENDING);
			}
			else if (currDate.after(winEnd)) {
				// after collection window
				this.setCompStatus(NOT_COMPLETED);
			}
			else {
				// during collection window
				this.setCompStatus(PENDING_NOW);
			}
		}
		else if (this.instrument.getDcStatus().startsWith(STATUS_INCOMPLETE)) {
			if (currDate.before(winStart)) {
				// before collection window
				this.setCompStatus(PENDING);
			}
			else if (currDate.before(winEnd)) {
				// during collection window
				this.setCompStatus(IN_PROGRESS);
			}
			else {
//TODO: this does not distinguish from above, i.e. without investigating, user will not know whether
//the instrument was never assigned/never collected above, vs. collection has begun but incomplete here				
				// after collection window
				this.setCompStatus(NOT_COMPLETED);
			}
		}
		else if (instrument.getDcStatus().equals(STATUS_PARTIALLY_COMPLETE)) {
			this.setCompStatus(PARTIAL);
		}
		else if (instrument.getDcStatus().startsWith(STATUS_COMPLETE)) {
			this.setCompStatus(COMPLETED);
		}
		else {
			// log unhandled situation
			logger.error("ProtocolInstrument:" + this.getId() + " unhandled situation determining compStatus");
		}
		

		// Collection Status
		
		// Compute the Collection Status as one of the following:
		// "Pending", "Early", "Collected", "Late", "N/A"
		
		// the collectWinStatus property is used for the status of when the instrument data was collected
		// relative to the collection window
		
		// collectWinStatus is a calculated property and as such the user can not modify it. collectWinReason is
		// available for the user to augment and/or override collectWinStatus, a kind of secondary status,
		// and collectWinNotes is available for the user to further clarify the collection status
		
		// if no collection window has been defined (it is optional), return the not applicable status
		if (!this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWindowDefined() &&
				!this.getProtocolInstrumentConfig().getCustomCollectWinDefined()) {
			this.setCollectWinStatus(N_A);
		}
		else if (this.getCollectAnchorDate() == null) {
			// if a collection window references a visit that has not been assigned to the protocol, then can not compute the
			// collection window and therefore can not determine the collectWinStatus
//TODO: determine what this should be				
			this.setCollectWinStatus("TBD");
//??			this.setCollectWinReason/Note("Collection window can not be calculated");
		}
		else if (this.getCompStatus().equals(PENDING)) {
			// nothing collected and current date is prior to collection window
			this.setCollectWinStatus(PENDING);
		}
		else if (this.getCompStatus().equals(PENDING_NOW) || this.getCompStatus().equals(IN_PROGRESS)) {
			// nothing collected and current date is within collection window
			this.setCollectWinStatus(PENDING_NOW);
		}
		else if (this.getCompStatus().equals(NOT_COMPLETED)) {
			// nothing collected and current data is past collection window
			this.setCollectWinStatus(PENDING_LATE);
		}
//TODO: decide if compStatus PARTIAL should be considered as final, such that collectWinStatus of
// EARLY/COLLECTED/LATE can be used, or should it be paired with collectWinStatus of Pending
		else if (this.getCompStatus().equals(PARTIAL) || this.getCompStatus().equals(COMPLETED)) {
			if (this.getInstrument().getDcDate().before(this.getCollectWinStart())) {
				this.setCollectWinStatus(EARLY);
			}
			else if (this.getInstrument().getDcDate().after(this.getCollectWinEnd())) {
//TODO: distinguishing between PENDING - LATE (Alert), and LATE (Deviation), so should LATE be changed 
//to COLLECTED - LATE to make things more clear?
				this.setCollectWinStatus(LATE);
			}
			else { 
				this.setCollectWinStatus(COLLECTED);
			}
		}
		else {
			// log unhandled situation
			logger.error("ProtocolInstrument:" + this.getId() + " unhandled situation determining collectWinStatus");
		}
	}
	

	/** 
	 * Perform any calculations done in ProtocolInstrument. This method should be called 
	 * whenever anything that may affect these calculations occurs, such as changes in the
	 * Protocol configuration, and assigning a Visit to the Protocol.
	 */
	public void calculate() {
		this.calcCollectWinDates();
	}
	
	public boolean afterUpdate() {
		if (this.getInstrument() != null) {
			this.setAssignDescrip(new StringBuffer("Instrument: ").append(this.getInstrument().getVisit().getVisitDescrip()).append(" - ").append(this.getInstrument().getInstrType()).toString());
		}
		// in case visit has been modified, need to recalculate windows
		LavaDaoFilter filter = newFilterInstance();
		filter.addDaoParam(filter.daoNamedParam("protocolId", this.getProtocolVisit().getProtocolTimepoint().getProtocol().getId()));
		Protocol protocolTree = (Protocol) EntityBase.MANAGER.findOneByNamedQuery("protocol.completeProtocolTree", filter);
		protocolTree.calculate();
		// return true to save any changes
		return true;
	}
	
}
