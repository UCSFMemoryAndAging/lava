package edu.ucsf.lava.crms.protocol.model;

import java.util.Calendar;
import java.util.Date;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


/**
 *   
 * @author ctoohey
 *
 *
 */
public class PatientProtocolTimepoint extends PatientProtocolTimepointBase {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolTimepoint.class);
	
	public PatientProtocolTimepoint(){
		super();
	}

	private String schedWinStatus;
	private String schedWinReason;
	private String schedWinNote;
//NOTE: schedWinPivot current not persisted. should it be?	
	private Date schedWinPivot;
	private Date schedWinStart;
	private Date schedWinEnd;
	
	/**
	 * Convenience method to return PatientProtocol instead of PatientProtocolBase
	 */
//	public PatientProtocol getPatientProtocol() {
//		return (PatientProtocol) super.getPatientProtocol();
//	}

	/**
	 * Convenience method to return ProtocolTimepoint instead of ProtocolTimepointBase
	 */
//	public ProtocolTimepoint getProtocolTimepoint() {
//		return (ProtocolTimepoint) super.getProtocolTimepoint();
//	}

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
		
		if (((ProtocolTimepoint)this.getProtocolTimepoint()).getFirstTimepoint()) {
			// iterate thru the visits of this timepoint and return the earliest visitDate
			Date earliestVisitDate = null;
			for (PatientProtocolVisitBase patientProtocolVisitBase : this.getVisits()) {
				PatientProtocolVisit patientProtocolVisit = (PatientProtocolVisit) patientProtocolVisitBase;
				if (patientProtocolVisit.getVisit() != null && patientProtocolVisit.getVisit().getVisitDate() != null) {
					if (earliestVisitDate == null || earliestVisitDate.compareTo(patientProtocolVisit.getVisit().getVisitDate()) > 0) {
						earliestVisitDate = patientProtocolVisit.getVisit().getVisitDate();
					}
				}
			}
			// if there are no visits scheduled, return the PatientProtocol enrolledDate
			if (earliestVisitDate == null) {
				earliestVisitDate = ((PatientProtocol)this.getPatientProtocol()).getEnrolledDate();
			}
			return earliestVisitDate;
		} else {
			// call recursively with the timepoint to which this is relative
			ProtocolTimepoint protocolAnchorTimepoint = ((ProtocolTimepoint)this.getProtocolTimepoint()).getSchedWinAnchorTimepoint();
			if (protocolAnchorTimepoint != null) {
				// to get from the ProtocolAssessmentTimepoint to its corresponding PatientProtocolAssessmentTimepoint, iterate thru
				// the latter until find it
				Long patientProtocolAnchorTimepointId = null;
				for (PatientProtocolTimepointBase patientProtocolTimepointBase : this.getPatientProtocol().getTimepoints()) {
					if (protocolAnchorTimepoint.getId().equals(patientProtocolTimepointBase.getId())) {
						patientProtocolAnchorTimepointId = patientProtocolTimepointBase.getId();
						break;
					}
				}
				// need to retrieve the entity to initialize the object graph required by recursive call to calcSchedPivotDate
				PatientProtocolTimepoint patientProtocolAnchorTimepoint = (PatientProtocolTimepoint) MANAGER.getById(patientProtocolAnchorTimepointId);
				Date anchorSchedWinStart = patientProtocolAnchorTimepoint.calcSchedPivotDate();
				Calendar cal = Calendar.getInstance();
				cal.setTime(anchorSchedWinStart);
				cal.add(Calendar.DATE, protocolAnchorTimepoint.getSchedWinDaysFromAnchor());
				return cal.getTime();
			}
			else {
				return null;
			}
		}
	}
	
	
	public void calcSchedWinDates() {
		// the way to think about this is that there is a single Date representing the actual start of the
		// PatientProtocol (whether that is the enrolledDate or a real Visit date). there is a window
		// around that as defined by the first Timepoint in the Protocol definition. but subsequent 
		// timepoints that are relative to it need to be relative to a single Date, not window start
		// and end dates. call this the pivotDate. calculate the pivotDate of a timepoint and then
		// calculate the window start and end from the pivotDate
		
		this.setSchedWinPivot(this.calcSchedPivotDate());
		
		if (this.getSchedWinPivot() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.getSchedWinPivot());
			ProtocolTimepoint protocolTimepoint = (ProtocolTimepoint)this.getProtocolTimepoint();
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
