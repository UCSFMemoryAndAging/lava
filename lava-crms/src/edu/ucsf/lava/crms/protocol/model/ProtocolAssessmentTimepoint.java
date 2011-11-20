package edu.ucsf.lava.crms.protocol.model;

import java.util.Calendar;
import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ProtocolAssessmentTimepoint extends ProtocolTimepoint {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolAssessmentTimepoint.class);

	public ProtocolAssessmentTimepoint() {
		super();
		this.setAuditEntityType("ProtocolAssessmentTimepoint");
	}

	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{
				/*this.getProtocolTimepointBase(),*/
				this.getPatient(), 
				this.getProtocolVisitsBase()};
	}

	private Date collectAnchorDate;
	private Date collectWinStart;
	private Date collectWinEnd;
	private String collectWinStatus;
	private String collectWinReason;
	private String collectWinNote;
	
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
	public Date getCollectAnchorDate() {
		return collectAnchorDate;
	}
	public void setCollectAnchorDate(Date collectAnchorDate) {
		this.collectAnchorDate = collectAnchorDate;
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
	

	/**
	 * Calculate a default ideal date on which all instruments in this timepoint would be collected. The 
	 * date is persisted in the ProtocolAssessmentInstrument collectAnchorDate property.
	 * 
	 * A ProtocolAssessmentTimepointConfig defines a collection window relative to the 
	 * timepoint's primary visit, which serves as the default collection window for all of the 
	 * timepoint's instruments. Note that each instrument can override this collection window
	 * by defining its own custom collection window in ProtocolInstrumentOptionConfig. That
	 * calculation is done in ProtocolInstrument. 
	 *  
	 * If a Visit has not yet been associated with the primary ProtocolVisit, then the 
	 * collectAnchorDate can not be calculated, and this method returns null. 
	 */
	public Date calcCollectAnchorDate() {
		// the collection window is based on the visitDate of a Visit assigned to the
		// primary ProtocolVisit of this ProtocolAssessmentTimepoint
		if (this.getPrimaryProtocolVisit().getVisit() != null) {
			return this.getPrimaryProtocolVisit().getVisit().getVisitDate();
		}		
		else {
			return null;
		}
	}
	
	
	/**
	 * Given the calculation done by calcCollectAnchorDate, calculate a default data collection window 
	 * defined in this protocol's configuration representing the acceptable window within which data 
	 * collection for all instruments in this timepoint should occur.
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
			// collectWinOffset is typically 0 or negative. it is negative to balance the scheduling
			// window around collectAnchorDate
			cal.add(Calendar.DATE, ((ProtocolAssessmentTimepointConfig)this.getProtocolTimepointConfig()).getCollectWinOffset());
			this.setCollectWinStart(cal.getTime());
			cal.add(Calendar.DATE, ((ProtocolAssessmentTimepointConfig)this.getProtocolTimepointConfig()).getCollectWinSize());
			this.setCollectWinEnd(cal.getTime());
		}
	}
	
	
	
	/** 
	 * Perform any calculations done in ProtocolAssessmentTimepoint. This method should be called 
	 * whenever anything that may affect these calculations occurs, such as changes in the
	 * Protocol configuration, and assigning a Visit to the Protocol.
	 */
	public void calculate() {
		super.calculate();
		this.calcCollectWinDates();
	}
	
	
	
}
