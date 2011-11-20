package edu.ucsf.lava.crms.protocol.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


/**
 *   
 * @author ctoohey
 *
 *
 */
public abstract class ProtocolTimepoint extends ProtocolTimepointBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTimepoint.class);
	
	public ProtocolTimepoint(){
		super();
		this.setAuditEntityType("ProtocolTimepoint");	
	}
	
	// NOTE: this is an abstract class so getAssociationsToInitialize is called in the subclass


	private String schedWinStatus;
	private String schedWinReason;
	private String schedWinNote;
	private Date schedAnchorDate; // calculated
	private Date schedWinStart; // calculated
	private Date schedWinEnd; // calculated
	private ProtocolVisit primaryProtocolVisit;
	
	/**
	 * Convenience methods to convert ProtocolTimepointBase method types to types of this subclass,
	 * since if an object of this class exists we know we can safely downcast. 
	 */
	public Protocol getProtocol() {
		return (Protocol) super.getProtocolBase();
	}
	public void setProtocol(Protocol protocol) {
		super.setProtocolBase(protocol);
	}
	public ProtocolTimepointConfig getProtocolTimepointConfig() {
		return (ProtocolTimepointConfig) super.getProtocolTimepointConfigBase();
	}
	public void setProtocolTimepointConfig(ProtocolTimepointConfig protocolTimepointConfig) {
		super.setProtocolTimepointConfigBase(protocolTimepointConfig);
	}
	public Set<ProtocolVisit> getProtocolVisits() {
		return (Set<ProtocolVisit>) super.getProtocolVisitsBase();
	}
	public void setProtocolVisits(Set<ProtocolVisit> protocolVisits) {
		super.setProtocolVisitsBase(protocolVisits);
	}

	/*
	 * Method to add a ProtocolVisit to a ProtocolTimepoint, managing the bi-directional relationship.
	 */
	public void addProtocolVisit(ProtocolVisit protocolVisit) {
		protocolVisit.setProtocolTimepoint(this);
		this.getProtocolVisits().add(protocolVisit);
	}

	

	public String getSchedWinStatus() {
		return schedWinStatus;
	}

	public void setSchedWinStatus(String schedWinStatus) {
		this.schedWinStatus = schedWinStatus;
	}

	public String getSchedWinReason() {
		return schedWinReason;
	}

	public void setSchedWinReason(String schedWinReason) {
		this.schedWinReason = schedWinReason;
	}

	public String getSchedWinNote() {
		return schedWinNote;
	}

	public void setSchedWinNote(String schedWinNote) {
		this.schedWinNote = schedWinNote;
	}

	public Date getSchedAnchorDate() {
		return schedAnchorDate;
	}
	
	public void setSchedAnchorDate(Date schedAnchorDate) {
		this.schedAnchorDate = schedAnchorDate;
	}
	
	public Date getSchedWinStart() {
		return schedWinStart;
	}

	public void setSchedWinStart(Date schedWinStart) {
		this.schedWinStart = schedWinStart;
	}

	public Date getSchedWinEnd() {
		return schedWinEnd;
	}

	public void setSchedWinEnd(Date schedWinEnd) {
		this.schedWinEnd = schedWinEnd;
	}

	public ProtocolVisit getPrimaryProtocolVisit() {
		return primaryProtocolVisit;
	}
	
	public void setPrimaryProtocolVisit(ProtocolVisit primaryProtocolVisit) {
		this.primaryProtocolVisit = primaryProtocolVisit;
	}
	
	/**
	 * Calculate the ideal date that the timepoint would be anchored on for scheduling visits. This calculation 
	 * should be done whenever configuration is changed. The date is persisted in the ProtocolTimepoint 
	 * schedAnchorDate property.
	 * 
	 * If this is the first ProtocolTimepoint, it is the beginning of the protocol, so its schedAnchorDate
	 * is the visit date of the Visit assigned to this timepoint's primary ProtocolVisit (designated 
	 * by primaryProtocolVisit). If this visit is not scheduled, then the first timepoint, nor any subsequent 
     * timepoints, can have a schedAnchorDate.
     * 
     * If this is not the first timepoint, then its schedAnchorDate is relative to an earlier timepoint
     * In this case, the method is called recursively until the first timepoint returns its
     * visit date (if scheduled).
	 *
	 * @return The calculated value of schedAnchorDate for this timepoint, as a java.util.Date, or null
	 * 			if the primary visit for the first timepoint has not been scheduled, i.e. the protocol
	 * 			has not commenced.
	 */
	public Date calcSchedAnchorDate() {
		
		// if this is a subsequent timepoint, then the date is the date of the timepoint to which this 
		// timepoint's scheduling window is relative, plus the schedWinRelativeAmount (units in schedWinRelativeUnits)
		
		if (this.getProtocolTimepointConfig().isFirstProtocolTimepointConfig()) {
			if (this.getPrimaryProtocolVisit().getVisit() == null) {
				return null;
			}
			else {
				return this.getPrimaryProtocolVisit().getVisit().getVisitDate();
			}
		} else {
			// call recursively with the timepoint to which this is relative
			ProtocolTimepointConfig relativeProtocolTimepointConfig = this.getProtocolTimepointConfig().getSchedWinRelativeTimepoint();

			// other than the first ProtocolTimepointConfig, schedWinRelativeTimepoint should not be null because it
			// is conditionally enforced as a required field. if here, this is not the first timepoint, so should not have
			// to check this for null

			// to get from the ProtocolAssessmentTimepointConfig to its corresponding ProtocolAssessmentTimepoint, iterate thru
			// the latter until find it
			Long protocolRelativeTimepointId = null;
			for (ProtocolTimepoint protocolTimepoint : this.getProtocol().getProtocolTimepoints()) {
				if (relativeProtocolTimepointConfig.getId().equals(protocolTimepoint.getProtocolTimepointConfig().getId())) {
					protocolRelativeTimepointId = protocolTimepoint.getId();
					break;
				}
			}
			// even though this entity is already loaded due to the current object graph in memory, need to 
			// retrieve the entity to initialize its object graph which will be used in this recursive call 
			ProtocolTimepoint protocolRelativeTimepoint = (ProtocolTimepoint) MANAGER.getById(protocolRelativeTimepointId);
			Date relativeTimepointSchedAnchorDate = protocolRelativeTimepoint.calcSchedAnchorDate();
			if (relativeTimepointSchedAnchorDate != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(relativeTimepointSchedAnchorDate);
				//TODO: convert schedWinRelativeAmount to days using schedWinRelativeUnits
				cal.add(Calendar.DATE, this.getProtocolTimepointConfig().getSchedWinRelativeAmount());
				return cal.getTime();
			}
			else {
				return null;
			}
		}
	}
	
	
	/**
	 * Given the calculation done by calcSchedAnchorDate, calculate a window defined in this timepoint's
	 * ProtocolTimepointConfig representing the acceptable window within which the actual visits for this 
	 * timepoint should be scheduled. 
	 * 
	 * The timepoint configuration's schedWinOffset determines the beginning of the scheduling window,
	 * schedWinStart, as follows:
	 *  0 equals the schedAnchorDate
	 *  negative value represents number of days before schedAnchorDate
	 *  positive value represents number of days after schedAnchorDate
	 *  
	 * Once the beginning of the scheduling window is determined, schedWinSize represents the size of
	 * the window and can be used to determine schedWinEnd.
	 * 
	 * Note that because timepoints can have multiple visits, a scheduling window for the first timepoint 
	 * still makes sense, as visits other than the primary visit must be scheduled within the window 
	 * anchored by the primary visit.
	 */
	public void calcSchedWinDates() {
		this.setSchedAnchorDate(this.calcSchedAnchorDate());
		
		if (this.getSchedAnchorDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.getSchedAnchorDate());
			ProtocolTimepointConfig protocolTimepointConfig = this.getProtocolTimepointConfig();
			// schedWinOffset is typically 0 or negative. it is negative to balance the scheduling
			// window around schedAnchorDate
			cal.add(Calendar.DATE, protocolTimepointConfig.getSchedWinOffset());
			this.setSchedWinStart(cal.getTime());
			cal.add(Calendar.DATE, protocolTimepointConfig.getSchedWinSize());
			this.setSchedWinEnd(cal.getTime());
		}
	}

	
	/** 
	 * Perform any calculations done in ProtocolTimepoint. This method should be called 
	 * whenever anything that may affect these calculations occurs, such as changes in the
	 * Protocol configuration, and assigning a Visit to the Protocol.
	 */
	public void calculate() {
		this.calcSchedWinDates();
	}

}
