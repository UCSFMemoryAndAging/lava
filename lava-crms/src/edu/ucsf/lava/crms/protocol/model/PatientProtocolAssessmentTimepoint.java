package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;
import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class PatientProtocolAssessmentTimepoint extends PatientProtocolTimepoint {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolAssessmentTimepoint.class);

	public PatientProtocolAssessmentTimepoint() {
		super();
		this.setAuditEntityType("PatientProtocolAssessmentTimepoint");
	}

	public Object[] getAssociationsToInitialize(String method) {
		// initialize parent to the Protocol level as need the projName of the protocol
		return new Object[]{this.getProtocolTimepoint(), this.getVisits()};
	}
	
	private String collectWinStatus;
	private String collectWinReason;
	private String collectWinNote;
	private Date collectWinStart;
	private Date collectWinEnd;
	
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
	
	
	
}
