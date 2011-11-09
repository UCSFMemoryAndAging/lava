package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;

public class PatientProtocolInstrument extends PatientProtocolInstrumentBase {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolInstrument.class);
	
	public PatientProtocolInstrument(){
		super();
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.getProtocolInstrument()};
	}

	private Instrument instrument;
	// instrId is part of the mechanism required to set the Instrument association, allowing the user
	// to designate an instrument which binds the instrument id to instrId. instrId is then used 
	// when saving a PatientProtocolInstrument to set the Instrument association
	private Long instrId;
	private String collectWinStatus;
	private String collectWinReason;
	private String collectWinNote;
	private Date customCollectWinStart;
	private Date customCollectWinEnd;
	
	/**
	 * Convenience method to return PatientProtocolVisit instead of PatientProtocolVisitBase
	 */
//	public PatientProtocolVisit getPatientProtocolVisit() {
//		return (PatientProtocolVisit) super.getPatientProtocolVisit();
//	}

	/**
	 * Convenience method to return ProtocolInstrument instead of ProtocolInstrumentBase
	 */
//	public ProtocolInstrument getProtocolInstrument() {
//		return (ProtocolInstrument) super.getProtocolInstrument();
//	}
	
	public Instrument getInstrument() {
		return instrument;
	}
	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
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
	public Date getCustomCollectWinStart() {
		return customCollectWinStart;
	}
	public void setCustomCollectWinStart(Date customCollectWinStart) {
		this.customCollectWinStart = customCollectWinStart;
	}
	public Date getCustomCollectWinEnd() {
		return customCollectWinEnd;
	}
	public void setCustomCollectWinEnd(Date customCollectWinEnd) {
		this.customCollectWinEnd = customCollectWinEnd;
	}
	
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
	
	
}
