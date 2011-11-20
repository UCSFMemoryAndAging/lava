package edu.ucsf.lava.crms.protocol.model;

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
		return new Object[]{/*this.getProtocolInstrumentBase(),*/
				this.getProtocolInstrumentConfigBase().getProtocolInstrumentOptionConfigsBase(),
				this.getPatient()};
	}

	private Instrument instrument;
	// instrId is part of the mechanism required to set the Instrument association, allowing the user
	// to designate an instrument which binds the instrument id to instrId. instrId is then used 
	// when saving a ProtocolInstrument to set the Instrument association
	private Long instrId;

	// collection window calculated either from custom configuration in its ProtocolInstrumentConfig, or the default collection
	// window defined in ProtocolTimepointConfig
	private ProtocolVisit collectWinProtocolVisit;
	private Date collectWinStart;
	private Date collectWinEnd;
	private Date collectAnchorDate; 
	
	private String collectWinStatus;
	private String collectWinReason;
	private String collectWinNote;
	
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
	
	public String getCollectWinStatus() {
		return collectWinStatus;
	}
	public void setCollectWinStatus(String collectWinStatus) {
		this.collectWinStatus = collectWinStatus;
	}
	public String getCollectWinReason() {
		return collectWinReason;
	}
	public void setCollectWinReason(String collectWinReason) {
		this.collectWinReason = collectWinReason;
	}
	public String getCollectWinNote() {
		return collectWinNote;
	}
	public void setCollectWinNote(String collectWinNote) {
		this.collectWinNote = collectWinNote;
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
	 * A ProtocolAssessmentTimepointConfig defines a collection window relative to the 
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
		if (this.getCollectWinProtocolVisit() != null && this.getCollectWinProtocolVisit().getVisit() != null) {
			return this.getCollectWinProtocolVisit().getVisit().getVisitDate();
		}
		// if there is no custom collection window visit defined for this instrument, then
		// use the Visit associated with the primary ProtocolVisit as defined by the timepoint, 
		// ProtocolTimepointConfig
		else if (this.getProtocolVisit().getProtocolTimepoint().getPrimaryProtocolVisit() != null &&
				this.getProtocolVisit().getProtocolTimepoint().getPrimaryProtocolVisit().getVisit() != null) {
			return this.getProtocolVisit().getProtocolTimepoint().getPrimaryProtocolVisit().getVisit().getVisitDate();
		}		
		else {
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
			if (this.getProtocolInstrumentConfig().getCustomCollectWinSize() != null 
					&& this.getProtocolInstrumentConfig().getCustomCollectWinOffset() != null) {
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectAnchorDate
				cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinOffset());
				this.setCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, this.getProtocolInstrumentConfig().getCustomCollectWinSize());
				this.setCollectWinEnd(cal.getTime());
			}
			// otherwise, use the default collection window configuration for the timepoint to which
			// this instrument belongs
			else {
				// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
				// window around collectAnchorDate
				cal.add(Calendar.DATE, ((ProtocolAssessmentTimepointConfig)this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig()).getCollectWinOffset());
				this.setCollectWinStart(cal.getTime());
				cal.add(Calendar.DATE, ((ProtocolAssessmentTimepointConfig)this.getProtocolVisit().getProtocolTimepoint().getProtocolTimepointConfig()).getCollectWinSize());
				this.setCollectWinEnd(cal.getTime());
			}
		}
	}


	//TODO: under construction
	public void updateStatus() {
		//TODO: set collectStatus
		//if there is an instrument associated 
		  // if it has been completed
		  // else
     	    // compare its dcDate vs. the collection window
            // if not in collection window yet, do not set status 
		    // if within collection window: PENDING
		    // if past collection windoe: PENDING - LATE
		//else
		//  could be same as else above, but since no instrument associated, do not know
		//  which collection window to use, but could just use default window in this case
		
		
		// calculate the collection window
		// if an instrument has been associated, can check if there is a custom collection window
		// defined for that instrType. otherwise, use the timepoint collection window.
		// if no instrument has been associated, use the timepoint collection window.
		// if the timepoint collection window has not been defined, then:
		//   1) give some kind of warning msg on page or in some kind of validation report
		//   2) just set compStatus PENDING; PENDING - LATE not an option 
		//   3) could set collectWinStatus to N/A
		
		
		
		
		// if an instrument has been associated per the protocol definition, set the collection
		// and completion status
		// golden rule1: do not set a status if it already has a value as do not want to override
		// the value a user has set
		// golden rule2: allow user to override any computed status
		if (this.instrument != null) {

			if (this.instrument.getDcStatus() != null) {
				if (instrument.getDcStatus().toLowerCase().startsWith("complete")) {
					this.setCurrStatus("COMPLETED");
				}
				else if (this.instrument.getDcStatus().toLowerCase().startsWith("cancelled")) {
				// note that the user may choose to override this and/or set compReason and compNote 
					this.setCurrStatus("CANCELLED");
				}
				else if (this.instrument.getDcStatus().toLowerCase().startsWith("incomplete")) {
					// note that the user may choose to override this and/or set compReason and compNote 
					this.setCollectWinStatus("NOT COMPLETED");
				}
				else if (this.instrument.getDcStatus().toLowerCase().startsWith("scheduled")) {
					this.setCollectWinStatus("PENDING");
					
//TODO:checking whether before/within/after collection window, and set to "", "PENDING", "PENDING - LATE", respectively
// i.e. thought is that if not within collection window, there should be no status yet, i.e. coordinator can ignore,
//  and that coordinator should definitely know if collection is late --- could use collection status for that, i.e.
//  compStatus="PENDING" collectWinStatus="LATE" but seems clearer to just relegate collectWinStatus to the
//  status of a completed instrument
// if no collection window defined, then just set to PENDING					
				}
				else {
					//TODO: log unhandled dcStatus
				}
			}	
		
			// collection status
			if (this.getCurrStatus() != null && this.getCurrStatus().equals("COMPLETE")) {
					
					//TODO: compare dcDate vs. collect window: set collectStatus EARLY,COLLECTED or LATE
				    // the collection window may be custom so need to check the ProtocolInstrumentOptions, find which option this
				    //  instrType corresponds to, and find if there is a custom window
					
					//jhesse: think that ON TIME would be more accurate than "COLLECTED" given "EARLY" and "LATE"					
					this.setCollectWinStatus("EARLY | ON TIME | LATE");
			}
				
		}
		else {
			// if within or past the collection window and no instrument has been associated yet, seems
			// like should set compStatus to something like "NO INSTRUMENT" to alert coordinator
			
			
			
			
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
