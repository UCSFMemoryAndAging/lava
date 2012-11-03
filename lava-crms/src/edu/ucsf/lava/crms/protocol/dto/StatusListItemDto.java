package edu.ucsf.lava.crms.protocol.dto;

import java.util.Date;

import edu.ucsf.lava.core.dto.PagedListItemDto;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;

public class StatusListItemDto implements PagedListItemDto {
	// used to note the parent class used while forming this DTO, for auditing purposes 
	protected static Class auditParentEntity = EnrollmentStatus.class;
	
	private Long id; // timepoint ID
	private Long configId; // timepointConfig ID
	private String fullNameRevNoSuffix;
	private String projName;
	private String protocolLabel;
	private String currStatus;
	private String currReason;
	private String currNote;
	private String timepointLabel;
	private String timepointOptional;
	private Long visitId;
	private Long visitConfigId;
	private String visitLabel;
	private String visitOptional;
	private String visitSummary;
	private Long instrId;
	private Long instrConfigId;
	private String instrLabel;
	private String instrOptional;
	private String instrSummary;

	// see comments in ProtocolNode about which status related properties are used at which level of the protocol tree
	// this is why the "tp", "visit", and "instr" prefixes must be used in this DTO to differentiate properties that
	// are used in multiple levels of the protocol tree
	private Date tpSchedWinStart;
	private Date tpSchedWinEnd;
	private Date tpSchedWinAnchorDate;
	private Date tpIdealSchedWinStart;
	private Date tpIdealSchedWinEnd;
	private String tpSchedWinStatus;
	private String visitSchedWinStatus;
	private String visitSchedWinReason;
	private String visitSchedWinNote;
	private Date tpCollectWinStart;
	private Date tpCollectWinEnd;
	private Date tpCollectWinAnchorDate;
	private Date tpIdealCollectWinStart;
	private Date tpIdealCollectWinEnd;
	private String tpCollectWinStatus;
	private Date instrCollectWinStart;
	private Date instrCollectWinEnd;
	private Date instrCollectWinAnchorDate;
	private Date instrIdealInstrCollectWinStart;
	private Date instrIdealInstrCollectWinEnd;
	private String visitCollectWinStatus;
	private String instrCollectWinStatus;
	private String instrCollectWinReason;
	private String instrCollectWinNote;
	private String tpCompStatus;
	private String visitCompStatus;
	private String instrCompStatus;
	private String instrCompReason;
	private String instrCompNote;
	
	private Long assignedVisitId;
	private Long assignedInstrId;
	private String assignedInstrTypeEncoded;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuditParentEntityName() {
		return auditParentEntity.getSimpleName();
	}
	public String getAuditParentEntityType() {
		return auditParentEntity.getSimpleName();
	}
	public Long getConfigId() {
		return configId;
	}
	public void setConfigId(Long configId) {
		this.configId = configId;
	}
	public String getFullNameRevNoSuffix() {
		return fullNameRevNoSuffix;
	}
	public void setFullNameRevNoSuffix(String fullNameRevNoSuffix) {
		this.fullNameRevNoSuffix = fullNameRevNoSuffix;
	}
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public String getProtocolLabel() {
		return protocolLabel;
	}
	public void setProtocolLabel(String protocolLabel) {
		this.protocolLabel = protocolLabel;
	}
	public String getCurrStatus() {
		return currStatus;
	}
	public void setCurrStatus(String currStatus) {
		this.currStatus = currStatus;
	}
	public String getCurrReason() {
		return currReason;
	}
	public void setCurrReason(String currReason) {
		this.currReason = currReason;
	}
	public String getCurrNote() {
		return currNote;
	}
	public void setCurrNote(String currNote) {
		this.currNote = currNote;
	}
	public String getTimepointLabel() {
		return timepointLabel;
	}
	public void setTimepointLabel(String timepointLabel) {
		this.timepointLabel = timepointLabel;
	}
	public Long getVisitId() {
		return visitId;
	}
	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
	public Long getVisitConfigId() {
		return visitConfigId;
	}
	public void setVisitConfigId(Long visitConfigId) {
		this.visitConfigId = visitConfigId;
	}
	public String getVisitLabel() {
		return visitLabel;
	}
	public void setVisitLabel(String visitLabel) {
		this.visitLabel = visitLabel;
	}
	public Long getInstrId() {
		return instrId;
	}
	public void setInstrId(Long instrId) {
		this.instrId = instrId;
	}
	public Long getInstrConfigId() {
		return instrConfigId;
	}
	public void setInstrConfigId(Long instrConfigId) {
		this.instrConfigId = instrConfigId;
	}
	public String getInstrLabel() {
		return instrLabel;
	}
	public void setInstrLabel(String instrLabel) {
		this.instrLabel = instrLabel;
	}
	public String getTpSchedWinStatus() {
		return tpSchedWinStatus;
	}
	public void setTpSchedWinStatus(String tpSchedWinStatus) {
		this.tpSchedWinStatus = tpSchedWinStatus;
	}
	public Date getTpSchedWinStart() {
		return tpSchedWinStart;
	}
	public void setTpSchedWinStart(Date tpSchedWinStart) {
		this.tpSchedWinStart = tpSchedWinStart;
	}
	public Date getTpSchedWinEnd() {
		return tpSchedWinEnd;
	}
	public void setTpSchedWinEnd(Date tpSchedWinEnd) {
		this.tpSchedWinEnd = tpSchedWinEnd;
	}
	public Date getTpSchedWinAnchorDate() {
		return tpSchedWinAnchorDate;
	}
	public void setTpSchedWinAnchorDate(Date tpSchedWinAnchorDate) {
		this.tpSchedWinAnchorDate = tpSchedWinAnchorDate;
	}
	public String getVisitSummary() {
		return visitSummary;
	}
	public void setVisitSummary(String visitSummary) {
		this.visitSummary = visitSummary;
	}
	public String getInstrSummary() {
		return instrSummary;
	}
	public void setInstrSummary(String instrSummary) {
		this.instrSummary = instrSummary;
	}
	public String getTimepointOptional() {
		return timepointOptional;
	}
	public void setTimepointOptional(String timepointOptional) {
		this.timepointOptional = timepointOptional;
	}
	public String getVisitOptional() {
		return visitOptional;
	}
	public void setVisitOptional(String visitOptional) {
		this.visitOptional = visitOptional;
	}
	public String getInstrOptional() {
		return instrOptional;
	}
	public void setInstrOptional(String instrOptional) {
		this.instrOptional = instrOptional;
	}
	public String getVisitSchedWinStatus() {
		return visitSchedWinStatus;
	}
	public void setVisitSchedWinStatus(String visitSchedWinStatus) {
		this.visitSchedWinStatus = visitSchedWinStatus;
	}
	public String getVisitSchedWinReason() {
		return visitSchedWinReason;
	}
	public void setVisitSchedWinReason(String visitSchedWinReason) {
		this.visitSchedWinReason = visitSchedWinReason;
	}
	public String getVisitSchedWinNote() {
		return visitSchedWinNote;
	}
	public void setVisitSchedWinNote(String visitSchedWinNote) {
		this.visitSchedWinNote = visitSchedWinNote;
	}
	public Date getTpCollectWinStart() {
		return tpCollectWinStart;
	}
	public void setTpCollectWinStart(Date tpCollectWinStart) {
		this.tpCollectWinStart = tpCollectWinStart;
	}
	public Date getTpCollectWinEnd() {
		return tpCollectWinEnd;
	}
	public void setTpCollectWinEnd(Date tpCollectWinEnd) {
		this.tpCollectWinEnd = tpCollectWinEnd;
	}
	public Date getTpCollectWinAnchorDate() {
		return tpCollectWinAnchorDate;
	}
	public void setTpCollectWinAnchorDate(Date tpCollectWinAnchorDate) {
		this.tpCollectWinAnchorDate = tpCollectWinAnchorDate;
	}
	public String getTpCollectWinStatus() {
		return tpCollectWinStatus;
	}
	public void setTpCollectWinStatus(String tpCollectWinStatus) {
		this.tpCollectWinStatus = tpCollectWinStatus;
	}
	public Date getInstrCollectWinStart() {
		return instrCollectWinStart;
	}
	public void setInstrCollectWinStart(Date instrCollectWinStart) {
		this.instrCollectWinStart = instrCollectWinStart;
	}
	public Date getInstrCollectWinEnd() {
		return instrCollectWinEnd;
	}
	public void setInstrCollectWinEnd(Date instrCollectWinEnd) {
		this.instrCollectWinEnd = instrCollectWinEnd;
	}
	public Date getInstrCollectWinAnchorDate() {
		return instrCollectWinAnchorDate;
	}
	public void setInstrCollectWinAnchorDate(Date instrCollectWinAnchorDate) {
		this.instrCollectWinAnchorDate = instrCollectWinAnchorDate;
	}
	public String getVisitCollectWinStatus() {
		return visitCollectWinStatus;
	}
	public void setVisitCollectWinStatus(String visitCollectWinStatus) {
		this.visitCollectWinStatus = visitCollectWinStatus;
	}
	public String getInstrCollectWinStatus() {
		return instrCollectWinStatus;
	}
	public void setInstrCollectWinStatus(String instrCollectWinStatus) {
		this.instrCollectWinStatus = instrCollectWinStatus;
	}
	public String getInstrCollectWinReason() {
		return instrCollectWinReason;
	}
	public void setInstrCollectWinReason(String instrCollectWinReason) {
		this.instrCollectWinReason = instrCollectWinReason;
	}
	public String getInstrCollectWinNote() {
		return instrCollectWinNote;
	}
	public void setInstrCollectWinNote(String instrCollectWinNote) {
		this.instrCollectWinNote = instrCollectWinNote;
	}
	public String getTpCompStatus() {
		return tpCompStatus;
	}
	public void setTpCompStatus(String tpCompStatus) {
		this.tpCompStatus = tpCompStatus;
	}
	public String getVisitCompStatus() {
		return visitCompStatus;
	}
	public void setVisitCompStatus(String visitCompStatus) {
		this.visitCompStatus = visitCompStatus;
	}
	public String getInstrCompStatus() {
		return instrCompStatus;
	}
	public void setInstrCompStatus(String instrCompStatus) {
		this.instrCompStatus = instrCompStatus;
	}
	public String getInstrCompReason() {
		return instrCompReason;
	}
	public void setInstrCompReason(String instrCompReason) {
		this.instrCompReason = instrCompReason;
	}
	public String getInstrCompNote() {
		return instrCompNote;
	}
	public void setInstrCompNote(String instrCompNote) {
		this.instrCompNote = instrCompNote;
	}
	public Long getAssignedVisitId() {
		return assignedVisitId;
	}
	public void setAssignedVisitId(Long assignedVisitId) {
		this.assignedVisitId = assignedVisitId;
	}
	public Long getAssignedInstrId() {
		return assignedInstrId;
	}
	public void setAssignedInstrId(Long assignedInstrId) {
		this.assignedInstrId = assignedInstrId;
	}
	public String getAssignedInstrTypeEncoded() {
		return assignedInstrTypeEncoded;
	}
	public void setAssignedInstrTypeEncoded(String assignedInstrTypeEncoded) {
		this.assignedInstrTypeEncoded = assignedInstrTypeEncoded;
	}
	public Date getTpIdealSchedWinStart() {
		return tpIdealSchedWinStart;
	}
	public void setTpIdealSchedWinStart(Date tpIdealSchedWinStart) {
		this.tpIdealSchedWinStart = tpIdealSchedWinStart;
	}
	public Date getTpIdealSchedWinEnd() {
		return tpIdealSchedWinEnd;
	}
	public void setTpIdealSchedWinEnd(Date tpIdealSchedWinEnd) {
		this.tpIdealSchedWinEnd = tpIdealSchedWinEnd;
	}
	public Date getTpIdealCollectWinStart() {
		return tpIdealCollectWinStart;
	}
	public void setTpIdealCollectWinStart(Date tpIdealCollectWinStart) {
		this.tpIdealCollectWinStart = tpIdealCollectWinStart;
	}
	public Date getTpIdealCollectWinEnd() {
		return tpIdealCollectWinEnd;
	}
	public void setTpIdealCollectWinEnd(Date tpIdealCollectWinEnd) {
		this.tpIdealCollectWinEnd = tpIdealCollectWinEnd;
	}
	public Date getInstrIdealInstrCollectWinStart() {
		return instrIdealInstrCollectWinStart;
	}
	public void setInstrIdealInstrCollectWinStart(
			Date instrIdealInstrCollectWinStart) {
		this.instrIdealInstrCollectWinStart = instrIdealInstrCollectWinStart;
	}
	public Date getInstrIdealInstrCollectWinEnd() {
		return instrIdealInstrCollectWinEnd;
	}
	public void setInstrIdealInstrCollectWinEnd(Date instrIdealInstrCollectWinEnd) {
		this.instrIdealInstrCollectWinEnd = instrIdealInstrCollectWinEnd;
	}

}
