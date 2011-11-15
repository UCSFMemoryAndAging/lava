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
public class ProtocolTimepoint extends ProtocolTimepointBase {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolTimepoint.class);
	
	public ProtocolTimepoint(){
		super();
		this.setAuditEntityType("ProtocolTimepoint");	
	}

	private String schedWinStatus;
	private String schedWinReason;
	private String schedWinNote;
//NOTE: schedWinPivot current not persisted. should it be?	
	private Date schedWinPivot;
	private Date schedWinStart;
	private Date schedWinEnd;
	
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
	public ProtocolTimepointConfig getTimepointConfig() {
		return (ProtocolTimepointConfig) super.getTimepointConfigBase();
	}
	public void setTimepointConfig(ProtocolTimepointConfig config) {
		super.setTimepointConfigBase(config);
	}
	public Set<ProtocolVisit> getVisits() {
		return (Set<ProtocolVisit>) super.getVisitsBase();
	}
	public void setVisits(Set<ProtocolVisit> visits) {
		super.setVisitsBase(visits);
	}

	/*
	 * Method to add a ProtocolVisit to a ProtocolTimepoint, managing the bi-directional relationship.
	 */
	public void addVisit(ProtocolVisit protocolVisit) {
		protocolVisit.setTimepoint(this);
		this.getVisits().add(protocolVisit);
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

	public Date getSchedWinPivot() {
		return schedWinPivot;
	}

	public void setSchedWinPivot(Date schedWinPivot) {
		this.schedWinPivot = schedWinPivot;
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
	
	
	private Date calcSchedPivotDate() {
		// if this is the first timepoint, then the date is the date of the designated anchor visit (since
		// timepoints can have more than one visit. note that because there could be multiple visits, a
		// scheduling window for the first timepoint still makes sense, as visits other than the anchor
		// visit must be scheduled within the window anchored by the anchor visit)
		// if there are no visits, then return the patientProtocol enrolledDate
		
		// if this is a subsequent timepoint, then the date is the date of the timepoint to which this 
		// timepoint's scheduling window is relative, plus the schedWinDaysFrom
		
		if (this.getTimepointConfig().getFirstTimepoint()) {
			// iterate thru the visits of this timepoint and return the earliest visitDate
			Date earliestVisitDate = null;
			for (ProtocolVisit protocolVisit : this.getVisits()) {
				if (protocolVisit.getVisit() != null && protocolVisit.getVisit().getVisitDate() != null) {
					if (earliestVisitDate == null || earliestVisitDate.compareTo(protocolVisit.getVisit().getVisitDate()) > 0) {
						earliestVisitDate = protocolVisit.getVisit().getVisitDate();
					}
				}
			}
			// if there are no visits scheduled, return the Protocol enrolledDate
			if (earliestVisitDate == null) {
				earliestVisitDate = this.getProtocol().getEnrolledDate();
			}
			return earliestVisitDate;
		} else {
			// call recursively with the timepoint to which this is relative
			ProtocolTimepointConfig protocolAnchorTimepoint = this.getTimepointConfig().getSchedWinAnchorTimepoint();
			if (protocolAnchorTimepoint != null) {
				// to get from the ProtocolAssessmentTimepointConfig to its corresponding ProtocolAssessmentTimepoint, iterate thru
				// the latter until find it
				Long patientProtocolAnchorTimepointId = null;
				for (ProtocolTimepoint patientProtocolTimepoint : this.getProtocol().getTimepoints()) {
					if (protocolAnchorTimepoint.getId().equals(patientProtocolTimepoint.getTimepointConfig().getId())) {
						patientProtocolAnchorTimepointId = patientProtocolTimepoint.getId();
						break;
					}
				}
				// even though this entity is already loaded due to the current object graph in memory, need to 
				// retrieve the entity to initialize its object graph which will be used in this recursive call 
				ProtocolTimepoint patientProtocolAnchorTimepoint = (ProtocolTimepoint) MANAGER.getById(patientProtocolAnchorTimepointId);
				Date anchorSchedWinStart = patientProtocolAnchorTimepoint.calcSchedPivotDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(anchorSchedWinStart);
				// if this timepoint was relative to the first timepoint 
				cal.add(Calendar.DATE, this.getTimepointConfig().getSchedWinDaysFromAnchor());
				return cal.getTime();
			}
			else {
				return null;
			}
		}
	}
	
	
	public void calcSchedWinDates() {
		// there is a single Date representing the actual start of the Protocol (whether that 
		// is the enrolledDate or a real Visit date). subsequent timepoints that are relative to it have
		// a scheduling window, defined in the configuration, that can be computed from this date. 
		// subsequent timepoints that are relative to an earlier timepoint that is not the first timepoint
		// are relative to the mid-point of the scheduling window for the timepoint to which the timepoint
		// is relative. this mid-point is the schedPivotDate
		
		this.setSchedWinPivot(this.calcSchedPivotDate());
		
		if (this.getSchedWinPivot() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.getSchedWinPivot());
			ProtocolTimepointConfig protocolTimepoint = (ProtocolTimepointConfig)this.getTimepointConfig();
			if (protocolTimepoint.getSchedWinSize() != null) { // firstTimepoint has no scheduling window
				if (protocolTimepoint.getSchedWinOffset() == null) {
					this.setSchedWinStart(cal.getTime());
					cal.add(Calendar.DATE, protocolTimepoint.getSchedWinSize());
					this.setSchedWinEnd(cal.getTime());
				}
				else {
					cal.add(Calendar.DATE, -protocolTimepoint.getSchedWinOffset());
					this.setSchedWinStart(cal.getTime());
					cal.add(Calendar.DATE, protocolTimepoint.getSchedWinSize());
					this.setSchedWinEnd(cal.getTime());
				}
			}
		}
	}

}
