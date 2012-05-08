package edu.ucsf.lava.crms.protocol.model;

import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_CANCELLED;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_COMPLETE;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_INCOMPLETE;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_PARTIALLY_COMPLETE;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_SCHEDULED;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentHandler.STATUS_UNKNOWN;

import java.util.Calendar;
import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

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
				// so no need to initialize those. also, the options collection is eagerly fetched.
				this.getPatient()};
	}

	private ProtocolVisit instrCollectWinProtocolVisit; // this is set in ProtocolHandler doSaveAdd when Protocol assigned to a Patient

	
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

	public ProtocolVisit getInstrCollectWinProtocolVisit() {
		return instrCollectWinProtocolVisit;
	}

	public void setInstrCollectWinProtocolVisit(ProtocolVisit instrCollectWinProtocolVisit) {
		this.instrCollectWinProtocolVisit = instrCollectWinProtocolVisit;
	}

	/**
	 * Calculate the date that this instrument should be collected on. This calculation 
	 * should be done whenever configuration is changed. The date is persisted in the  
	 * ProtocolInstrument collectWinAnchorDate property.
	 * 
	 * A ProtocolTimepointConfig defines a collection window relative to the 
	 * timepoint's primary visit, which serves as the default collection window for all of the 
	 * timepoint's instruments. However each instrument can override this collection window
	 * by defining its own custom collection window in ProtocolInstrumentConfigOption.
	 * 
	 * If a collection window is not defined, then use the scheduling window anchor date.
	 *  
	 * If a Visit has not yet been associated with the defined ProtocolVisit, then the 
	 * collectWinAnchorDate can not be calculated, and this method returns null.
	 * 
     * @param   ideal calculate the ideal scheduling window anchor date instead of the actual 
	 * @return        the calculated value of collectWinAnchorDate for this instrument
	 */
	public Date calcCollectWinAnchorDate(boolean ideal) {
		// the collection window is based on the visitDate of a visit assigned to this Protocol
		
		if (ideal) {
			// in all cases the ideal collect window anchor date would be the scheduling window anchor
			// date for the timepoint; whether there is a custom collection window defined by the instrument
			// or a default collection window defined, the anchor is the visit date, and the ideal
			// visit date for a timepoint is idealSchedWinAnchorDate (there is not a configuration 
			// for scheduling window at the visit level, just at the timepoint level, so the timepoint
			// anchor date applies to all visits configured for that timepoint
			
			return this.getProtocolVisit().getProtocolTimepoint().getIdealSchedWinAnchorDate();
		}
		// first determine if this instrument defines a custom collection window visit (a
		// ProtocolVisitConfig) within the same timepoint as this instrument in its
		// ProtocolInstrumentConfig. if so, use the Visit associated with the ProtocolVisit
		// defined in the configuration
		else if (this.getProtocolInstrumentConfig().getCustomCollectWinDefined()) {
			if (this.getInstrCollectWinProtocolVisit() != null && this.getInstrCollectWinProtocolVisit().getVisit() != null) {
				return this.getInstrCollectWinProtocolVisit().getVisit().getVisitDate();
			}
			else {
				return null;
			}
		}
		// if there is no custom collection window visit defined for this instrument, then
		// use the Visit associated with the primary ProtocolVisit as defined by the timepoint, 
		// ProtocolTimepointConfig
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
			return this.getProtocolVisit().getProtocolTimepoint().getSchedWinAnchorDate();
		}
	}

	/**
	 * calcCollectWinAnchorDate convenience method.
	 * 
	 * invokes calcCollectWinAnchorDate(false)
	 * 
	 * @return the collection window anchor date for this instrument
	 */
	public Date calcCollectWinAnchorDate() {
		return this.calcCollectWinAnchorDate(false);
	}

		
	/**
	 * calcCollectWinAnchorDate convenience method.
	 * 
	 * Invokes calcCollectWinAnchorDate(true)
	 * 
	 * @return the ideal collection window anchor date for this timepoint
	 */
	public Date calcIdealCollectWinAnchorDate() {
		return this.calcCollectWinAnchorDate(true);
	}
		
	
	
	/**
	 * Each timepoint has an optionally defined collection window which is the default for all
	 * instruments that are data collected as part of the timepoint. Additionally, a given instrument
	 * can be configured to have its own custom collection window. This method computes the start
	 * and end dates for the collection window for this instrument.
	 * 
	 * The configuration of a collection window is optional. Collection windows are typically defined
	 * in the following scenarios:
	 * 1) a timepoint default collection window specifies that all data for the timepoint is collected within
	 *    the configured window, relative to the primary visit of the timepoint, i.e. at one point should
	 *    data be excluded from analysis because it is to far from the primary visit
	 * 2) a custom collection window defined for the instrument specifies that the data for the instrument
	 *    is collected within a configured window relative to some visit of the timepoint, to ensure 
	 *    that data for two or more instruments within the same timepoint is collected within a 
	 *    certain timeframe relative to each other, i.e. one or more instruments have a level of time
	 *    sensitivity among them 
	 * 
	 * Given the calculation done by calcCollectWinAnchorDate, calculate a window defined in this protocol's
	 * configuration representing the acceptable window within which data collection for the Instrument
	 * assigned to this ProtocolInstrument should occur.
	 * 
	 * The configuration's collectWinOffset determines the beginning of the collection window,
	 * collectWinStart, as follows:
	 *  0 equals the collectWinAnchorDate
	 *  negative value represents number of days before collectWinAnchorDate
	 *  positive value represents number of days after collectWinAnchorDate
	 *  
	 * Once the beginning of the collection window is determined, collectWinSize represents the size of
	 * the window and can be used to determine collectWinEnd.
	 */
	public void calcCollectWinDates() {
		// if there are custom collection window attributes configured in the ProtocolInstrumentConfig,
		// use those
		if (this.getProtocolInstrumentConfig().getCustomCollectWinDefined()) {
			this.setInstrCollectWinAnchorDate(this.calcCollectWinAnchorDate());
			if (this.getInstrCollectWinAnchorDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getInstrCollectWinAnchorDate());
					
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectWinAnchorDate
				// note: size and offset are conditionally required when a custom collection window is defined
				cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinOffset());
				this.setInstrCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinSize());
				this.setInstrCollectWinEnd(cal.getTime());
			}
			this.setIdealInstrCollectWinAnchorDate(this.calcIdealCollectWinAnchorDate());
			if (this.getIdealInstrCollectWinAnchorDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getIdealInstrCollectWinAnchorDate());
					
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectWinAnchorDate
				// note: size and offset are conditionally required when a custom collection window is defined
				cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinOffset());
				this.setIdealInstrCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinSize());
				this.setIdealInstrCollectWinEnd(cal.getTime());
			}
		}
		// otherwise, use the default collection window configuration for the timepoint to which
		// this instrument belongs
		else if (this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWindowDefined()) {
			this.setInstrCollectWinAnchorDate(this.calcCollectWinAnchorDate());
			if (this.getInstrCollectWinAnchorDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getInstrCollectWinAnchorDate());
			
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectWinAnchorDate
				cal.add(Calendar.DATE, this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWinOffset());
				this.setInstrCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWinSize());
				this.setInstrCollectWinEnd(cal.getTime());
			}
			this.setIdealInstrCollectWinAnchorDate(this.calcIdealCollectWinAnchorDate());
			if (this.getIdealInstrCollectWinAnchorDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(this.getIdealInstrCollectWinAnchorDate());
			
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectWinAnchorDate
				cal.add(Calendar.DATE, this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWinOffset());
				this.setIdealInstrCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig().getCollectWinSize());
				this.setIdealInstrCollectWinEnd(cal.getTime());
			}
		}
		else {
			// default to the scheduling window
			this.setInstrCollectWinAnchorDate(this.getProtocolVisit().getProtocolTimepoint().getSchedWinAnchorDate());
			this.setInstrCollectWinStart(this.getProtocolVisit().getProtocolTimepoint().getSchedWinStart());
			this.setInstrCollectWinEnd(this.getProtocolVisit().getProtocolTimepoint().getSchedWinEnd()); 
			this.setIdealInstrCollectWinAnchorDate(this.getProtocolVisit().getProtocolTimepoint().getIdealSchedWinAnchorDate());
			this.setIdealInstrCollectWinStart(this.getProtocolVisit().getProtocolTimepoint().getIdealSchedWinStart());
			this.setIdealInstrCollectWinEnd(this.getProtocolVisit().getProtocolTimepoint().getIdealSchedWinEnd()); 
		}
		
		
	}


	public void updateStatus() {
		
		// Completion Status
		
		// Compute the Completion Status as one of the following:
		// "Not Started", "Pending", "In Progress", "Completed", "Partial", "Not Completed"
		
		// the compStatus property is used for the completion status of the instrument, without regard for
		// whether or not the instrument was completed within the configured window or not (that will be
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
		// "Complete - Partial", which indicates that the collection is complete and data is incomplete 

		String updatedCompStatus = null;
		if (this.getProtocolVisit().getProtocolTimepoint().getSchedWinAnchorDate() == null) {
			updatedCompStatus =  PROTOCOL_NOT_STARTED;
		}
		else if (this.getInstrument() == null || this.getInstrument().getDcStatus() == null 
				|| this.getInstrument().getDcStatus().startsWith(STATUS_CANCELLED)
				|| this.getInstrument().getDcStatus().equals(STATUS_UNKNOWN)
				|| this.getInstrument().getDcStatus().equals(STATUS_SCHEDULED)) {
			updatedCompStatus = PENDING;
		}
		else if (this.getInstrument().getDcStatus().startsWith(STATUS_INCOMPLETE) || this.getInstrument().getDcStatus().equals(STATUS_PARTIALLY_COMPLETE)) {
			// the remaining possibilities are the instrument is incomplete or partially complete.
			// since both of these statuses leave open the possibility that the instrument could be 
			// completed, if still within the collection window, consider the compStatus as "In Progress".
			// otherwise, these statuses become the completion status

			// if data collection not complete yet, compare the collection window vs. current time 
			// want current date but without a time component, since windows do not have a time component
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date currDate = cal.getTime();
			if (currDate.before(this.getInstrCollectWinEnd())) {
				updatedCompStatus = IN_PROGRESS;
			}
			else if (this.getInstrument().getDcStatus().startsWith(STATUS_INCOMPLETE)) {
				updatedCompStatus = NOT_COMPLETED;
			}
			else if (this.getInstrument().getDcStatus().equals(STATUS_PARTIALLY_COMPLETE)) {
				updatedCompStatus = PARTIAL;
			}
		}
		else if (this.getInstrument().getDcStatus().startsWith(STATUS_COMPLETE)) {
			updatedCompStatus = COMPLETED;
		}
		else {
			// log unhandled situation
			logger.error("ProtocolInstrument:" + this.getId() + " unhandled situation determining compStatus");
		}
		
		if (updatedCompStatus == null) {
			// there are no (non-optional) visits/instruments from which to roll up a status
			updatedCompStatus = N_A;
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
		
		// Compute the Collection Status as one of the following:
		// "Not Started", "Pending", "Early", "Collected", "Late", "N/A"
		
		// the collectWinStatus property is used for the status of when the instrument data was collected
		// relative to the collection window
		
		// collectWinStatus is a calculated property and as such the user can not modify it. collectWinReason is
		// available for the user to augment and/or override collectWinStatus, a kind of secondary status,
		// and collectWinNotes is available for the user to further clarify the collection status

		if (this.getProtocolVisit().getProtocolTimepoint().getSchedWinAnchorDate() == null) {
			// if the scheduling window for this instrument's timepoint can not be calculated it means
			// the patient has not yet started the protocol, i.e. no visit has been assigned to the
			// first protocol timepoint
			this.setCollectWinStatus(PROTOCOL_NOT_STARTED);
		}
		else if (this.getInstrument() == null || this.getInstrument().getDcStatus() == null 
				|| this.getInstrument().getDcStatus().startsWith(STATUS_CANCELLED)
				|| this.getInstrument().getDcStatus().equals(STATUS_UNKNOWN)
				|| this.getInstrument().getDcStatus().equals(STATUS_SCHEDULED)) {
			this.setCollectWinStatus(PENDING);
		}
		// note that the compStatus PARTIAL is completed status, i.e. collection is complete even
		// though it was only partial
		else if (this.getCompStatus().equals(PARTIAL) || this.getCompStatus().equals(COMPLETED)) {
			if (this.getInstrument().getDcDate().before(this.getInstrCollectWinStart())) {
				this.setCollectWinStatus(EARLY);
			}
			else if (this.getInstrument().getDcDate().after(this.getInstrCollectWinEnd())) {
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
			this.setSummary(new StringBuffer(this.getInstrument().getInstrType()).toString());
		}
		else {
			this.setSummary(null);
		}
		// note: in case the assigned Instrument is modified in any way, window and status calculations are 
		// updated in ProtocolInstrumentHandler doSave (can not do here because this method is called within
		// an active persistence layer session and that results in "object confusion" because the entire
		// protocol tree needs to be saved (since status changes at one level affect statuses at other
		// levels) in addition to the individual save that is in process on this object)
		return true;
	}
	
}
