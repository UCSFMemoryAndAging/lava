package edu.ucsf.lava.crms.protocol.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;

/**
 * This class serves as the base class with all common properties within the Protocol tree
 * hierarchy. Specific protocol components subclass this. 
 * 
 * A Protocol is a hierarchical structure so this class is named ProtocolNode rather than 
 * ProtocolBase to emphasize that point.
 * 
 * The tree hierarchy is:
 * Protocol
 *   ProtocolTimepoint
 *     ProtocolVisit 
 *       ProtocolInstrument 
 *
 * Note that between each of the above classes and this class, there is a ..Base class for each
 * config component. This is necessitated by the OR mapping layer. At that layer, the protocol
 * config component subclasses do not subclass a common base class because if they did it would 
 * result in polymorphic joins to every protocol config subclass when retrieving an object or 
 * objects of just one of the subclasses (because the base mapping is self-referencing via
 * a parent reference, which must generically be mapped to the base class -- and any query involving
 * a mapping with a base class joins to all of its subclasses)). Instead, a different 
 * base class exists for each protocol config component type (where each base class plus the 
 * ProtocolTracking class map the same base table) and the common properties are included rather 
 * than inherited.  
 * 
 * @author ctoohey
 *
 */

public abstract class ProtocolNode extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(ProtocolNode.class);
	
	// statuses for scheduling, collection, completion
	public static String PENDING = "Pending";
	public static String IN_PROGRESS = "In Progress";
	public static String COMPLETED = "Completed";
	public static String NOT_COMPLETED = "Not Completed";
	public static String PARTIAL = "Completed (Partial)";
	public static String LATE = "Late";
	public static String EARLY = "Early";
	public static String COLLECTED = "Collected";
    public static String N_A = "N/A";
    public static String SCHEDULED = "Scheduled";
    public static String PROTOCOL_NOT_STARTED = "Not Started";
	
    private String nodeType;
	private Patient patient;
	// because every type of Protocol tree node subclasses this class, projName is 
	// denormalized (which makes for simpler configuration of project authorization filtering)
	private String projName;
	// if this is a repeating timepoint, keep track of which timepoint is it in the repetition
	private Short repeatNum; // 1-based
	private Short strategy;
	
	// note on status properties
	// a given status consists of three properties: status, reason and note
	// because all of these properties exist in this base class (for reasons of db efficiency), they
	// are available to all node levels of the protocol tree: protocol, timepoint, visit, instrument
	// however, they are not used by all node levels
	// in particular:

	// a status property is used at the level of the protocol tree where is is calculated, and all
	// levels above that (except the protocol level) in which case the status property value is determined 
	// by rolling up the status properties of the level below it, i.e.
	// - compStatus is computed in ProtocolInstrument
	// - compStatus is rolled up in ProtocolTimepoint and ProtocolVisit
	// - compStatusOverride is used in ProtocolInstrument, ProtocolVisit, ProtocolTimepoint to flag that user is overrding computed compStatus
	// - compStatusOvrDiff is used in ProtocolInstrument, ProtocolVisit, ProtocolTimepoint to store computed compStatus when different from user overridden compStatus
	// - schedWinStatus is computed in ProtocolVisit
	// - schedWinStatus is rolled up in ProtocolTimepoint
	// - collectWinStatus is computed in ProtocolInstrument
	// - collectWinStatus is rolled up in ProtocolTimepoint and ProtocolVisit
	
	// the reason and note status properties are only used in the lowest level of the protocol tree 
	// where they are calculated, as it does not make sense to roll these up to a higher level (how
	// would you), i.e. 
	// - compReason and compNote are used in ProtocolInstrument
	// - schedWinReason and schedWinNote are used in ProtocolVisit
	// - collectWinReason and collectWinNote are used in ProtocolInstrument
	
	private String currStatus;
	private String currReason;
	private String currNote;
	private String compStatus;
	private String compReason;
	private String compNote;
	// flag set when user overrides compStatus
	private Boolean compStatusOverride;
	// if user overrides compStatus and computed compStatus differs, it is stored here
	private String compStatusComputed;
	// not sure if these are needed. if they reflect instrument dcBy, dcDate, then only pertain to
	// protocolInstrument, not protocolTimepoint or protocolVisit
	private String compBy;
	private Date compDate; 
	
	private String schedWinStatus;
	private String schedWinReason;
	private String schedWinNote;
	private String collectWinStatus;
	private String collectWinReason;
	private String collectWinNote;
	private Short confSchedWinDaysFromStart; // from ProtocolTimepointConfig
	// timepoint level windows
	private Date schedWinAnchorDate; // calculated, used in ordering in ProtocolTimepoint, ProtocolTracking
	private Date schedWinStart; // calculated
	private Date schedWinEnd; // calculated
	private Date idealSchedWinAnchorDate; // calculated, used in ordering in ProtocolTimepoint, ProtocolTracking
	private Date idealSchedWinStart; // calculated
	private Date idealSchedWinEnd; // calculated
	private Date collectWinAnchorDate; // calculated
	private Date collectWinStart; // calculated
	private Date collectWinEnd; // calculated
	private Date idealCollectWinAnchorDate; // calculated
	private Date idealCollectWinStart; // calculated
	private Date idealCollectWinEnd; // calculated
	
	private Visit visit;
	// visitId is part of the mechanism required to set the Visit association, allowing the user
	// to designate a visit in the view which binds the visit id to visitId. visitId is then used 
	// when saving a ProtocolVisit to set the Visit association
	private Long visitId; 
	
	
	// instrument level collection window 
	// this is calculated either from custom configuration in its ProtocolInstrumentConfig, or the 
	// default collection window defined in ProtocolTimepointConfig. if neither collection window
	// is defined, defaults to the scheduling window
	private Date instrCollectWinStart;
	private Date instrCollectWinEnd;
	private Date instrCollectWinAnchorDate; 
	private Date idealInstrCollectWinStart;
	private Date idealInstrCollectWinEnd;
	private Date idealInstrCollectWinAnchorDate; 
	
	private Instrument instrument;
	// instrId is part of the mechanism required to set the Instrument association, allowing the user
	// to designate an instrument which binds the instrument id to instrId. instrId is then used 
	// when saving a ProtocolInstrument to set the Instrument association
	private Long instrId;
	// the following should not be in the base class with the other custom collection window properties 
	// because it references ProtocolVisit and so ProtocolTracking queries would then always query 
	// ProtocolVisit, which sets off a chain of other queries
	

	private String summary;
	private String notes;
	
	public ProtocolNode(){
		super();
		this.setProjectAuth(true);
		this.addPropertyToAuditIgnoreList("patient");
		this.setStrategy((short)0);
	}
	
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public Short getRepeatNum() {
		return repeatNum;
	}

	public void setRepeatNum(Short repeatNum) {
		this.repeatNum = repeatNum;
	}

	public Short getStrategy() {
		return strategy;
	}

	public void setStrategy(Short strategy) {
		this.strategy = strategy;
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

	public String getCompStatus() {
		return compStatus;
	}

	public void setCompStatus(String currStatus) {
		this.compStatus = currStatus;
	}

	public String getCompReason() {
		return compReason;
	}

	public void setCompReason(String currReason) {
		this.compReason = currReason;
	}

	public String getCompNote() {
		return compNote;
	}

	public void setCompNote(String currNote) {
		this.compNote = currNote;
	}
	
	public Boolean getCompStatusOverride() {
		return compStatusOverride;
	}

	public void setCompStatusOverride(Boolean compStatusOverride) {
		this.compStatusOverride = compStatusOverride;
	}

	public String getCompStatusComputed() {
		return compStatusComputed;
	}

	public void setCompStatusComputed(String compStatusComputed) {
		this.compStatusComputed = compStatusComputed;
	}

	public String getCompBy() {
		return compBy;
	}

	public void setCompBy(String compBy) {
		this.compBy = compBy;
	}

	public Date getCompDate() {
		return compDate;
	}

	public void setCompDate(Date compDate) {
		this.compDate = compDate;
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
	
	public Short getConfSchedWinDaysFromStart() {
		return confSchedWinDaysFromStart;
	}

	public void setConfSchedWinDaysFromStart(Short confSchedWinDaysFromStart) {
		this.confSchedWinDaysFromStart = confSchedWinDaysFromStart;
	}

	public Date getSchedWinAnchorDate() {
		return schedWinAnchorDate;
	}
	
	public void setSchedWinAnchorDate(Date schedWinAnchorDate) {
		this.schedWinAnchorDate = schedWinAnchorDate;
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
	
	public Date getIdealSchedWinAnchorDate() {
		return idealSchedWinAnchorDate;
	}

	public void setIdealSchedWinAnchorDate(Date idealSchedWinAnchorDate) {
		this.idealSchedWinAnchorDate = idealSchedWinAnchorDate;
	}

	public Date getIdealSchedWinStart() {
		return idealSchedWinStart;
	}

	public void setIdealSchedWinStart(Date idealSchedWinStart) {
		this.idealSchedWinStart = idealSchedWinStart;
	}

	public Date getIdealSchedWinEnd() {
		return idealSchedWinEnd;
	}

	public void setIdealSchedWinEnd(Date idealSchedWinEnd) {
		this.idealSchedWinEnd = idealSchedWinEnd;
	}

	public Date getCollectWinAnchorDate() {
		return collectWinAnchorDate;
	}

	public void setCollectWinAnchorDate(Date collectWinAnchorDate) {
		this.collectWinAnchorDate = collectWinAnchorDate;
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
	
	public Date getIdealCollectWinAnchorDate() {
		return idealCollectWinAnchorDate;
	}

	public void setIdealCollectWinAnchorDate(Date idealCollectWinAnchorDate) {
		this.idealCollectWinAnchorDate = idealCollectWinAnchorDate;
	}

	public Date getIdealCollectWinStart() {
		return idealCollectWinStart;
	}

	public void setIdealCollectWinStart(Date idealCollectWinStart) {
		this.idealCollectWinStart = idealCollectWinStart;
	}

	public Date getIdealCollectWinEnd() {
		return idealCollectWinEnd;
	}

	public void setIdealCollectWinEnd(Date idealCollectWinEnd) {
		this.idealCollectWinEnd = idealCollectWinEnd;
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
	
	public Date getIdealInstrCollectWinStart() {
		return idealInstrCollectWinStart;
	}

	public void setIdealInstrCollectWinStart(Date idealInstrCollectWinStart) {
		this.idealInstrCollectWinStart = idealInstrCollectWinStart;
	}

	public Date getIdealInstrCollectWinEnd() {
		return idealInstrCollectWinEnd;
	}

	public void setIdealInstrCollectWinEnd(Date idealInstrCollectWinEnd) {
		this.idealInstrCollectWinEnd = idealInstrCollectWinEnd;
	}

	public Date getIdealInstrCollectWinAnchorDate() {
		return idealInstrCollectWinAnchorDate;
	}

	public void setIdealInstrCollectWinAnchorDate(
			Date idealInstrCollectWinAnchorDate) {
		this.idealInstrCollectWinAnchorDate = idealInstrCollectWinAnchorDate;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
		// this is the paradigm used to set associations via the UI
		if (this.visit != null) {
			this.visitId = this.visit.getId();
		}	
	}
	public Long getVisitId() {
		return visitId;
	}
	public void setVisitId(Long visitId) {
		this.visitId = visitId;
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

	
	
	public String getCompStatusBlock() {
		StringBuffer block = new StringBuffer("Status: ").append(this.getCompStatus() == null ? "" : this.getCompStatus()).append("\n");
		block.append("Reason: ").append(this.getCompReason() == null ? "" : this.getCompReason()).append("\n");
		block.append("Reason: ").append(this.getCompNote() == null ? "" : this.getCompNote());
		return block.toString();
	}

	/**
	 * The components at each level of the Protocol tree must implement a calculate method.
	 */
	public abstract void calculate();
	
	/**
	 * The components at each level of the Protocol tree must implement an updateStatus method.
	 */
	public abstract void updateStatus();

	
	/**
	 * The components at each level of the Protocol tree can implement a postUpdateStatus method
	 * to do things that depend on the latest status.
	 */
	public void postUpdateStatus() {
	}
	
	
	protected String rollupCompStatusHelper(ProtocolNode protocolNode, String updatedCompStatus) {
		if (protocolNode.getCompStatus() != null) {
			if (updatedCompStatus == null) {
				updatedCompStatus = protocolNode.getCompStatus();
			}
			// start with the most severe status and work up from there
			else if (protocolNode.getCompStatus().equals(PROTOCOL_NOT_STARTED)) {
				updatedCompStatus = protocolNode.getCompStatus();
			}
			else if (protocolNode.getCompStatus().equals(NOT_COMPLETED)) {
				// only update if this instrument status is more severe
				if (!updatedCompStatus.equals(PROTOCOL_NOT_STARTED)) {
					updatedCompStatus = protocolNode.getCompStatus();
				}
			}
			else if (protocolNode.getCompStatus().equals(PARTIAL)) {
				// only update if this instrument status is more severe
				if (!updatedCompStatus.equals(PROTOCOL_NOT_STARTED) && !updatedCompStatus.equals(NOT_COMPLETED)) {
					updatedCompStatus =  protocolNode.getCompStatus();
				}
			}
			else if (protocolNode.getCompStatus().equals(PENDING)) {
				// only update if this instrument status is more severe (aka current status is less severe)
				if (updatedCompStatus.equals(IN_PROGRESS) || updatedCompStatus.equals(COMPLETED)) {
					updatedCompStatus = protocolNode.getCompStatus();
				}
			}
			else if (protocolNode.getCompStatus().equals(IN_PROGRESS)) {
				// only update if this instrument status is more severe (aka current status is less severe)
				if (updatedCompStatus.equals(COMPLETED)) {
					updatedCompStatus = protocolNode.getCompStatus();
				}
			}
		}
		return updatedCompStatus;
	}
	
	protected String rollupSchedWinStatusHelper(ProtocolNode protocolNode, String updatedSchedWinStatus) {
		if (protocolNode.getSchedWinStatus() != null) {
			if (updatedSchedWinStatus == null) {
				updatedSchedWinStatus = protocolNode.getSchedWinStatus();
			}
			// start with the most severe status and work up from there
			else if (protocolNode.getSchedWinStatus().equals(PROTOCOL_NOT_STARTED)) {
				updatedSchedWinStatus = protocolNode.getSchedWinStatus();
			}
			else if (protocolNode.getSchedWinStatus().equals(PENDING)) {
				// only update if this instrument status is more severe
				if (!updatedSchedWinStatus.equals(PROTOCOL_NOT_STARTED)) {
					updatedSchedWinStatus =  protocolNode.getSchedWinStatus();
				}
			}
			else if (protocolNode.getSchedWinStatus().equals(LATE)) {
				// only update if this instrument status is more severe
				if (!updatedSchedWinStatus.equals(PROTOCOL_NOT_STARTED) && !updatedSchedWinStatus.equals(PENDING)) {
					updatedSchedWinStatus = protocolNode.getSchedWinStatus();
				}
			}
			else if (protocolNode.getSchedWinStatus().equals(EARLY)) {
				// only update if this instrument status is more severe
				if (!updatedSchedWinStatus.equals(PROTOCOL_NOT_STARTED) && !updatedSchedWinStatus.equals(PENDING)
						&& !updatedSchedWinStatus.equals(LATE)) {
					updatedSchedWinStatus = protocolNode.getSchedWinStatus();
				}
			}
		}
		return updatedSchedWinStatus;
	}
	
	protected String rollupCollectWinStatusHelper(ProtocolNode protocolNode, String updatedCollectWinStatus) {
		if (protocolNode.getCollectWinStatus() != null) {
			if (updatedCollectWinStatus == null) {
				updatedCollectWinStatus = protocolNode.getCollectWinStatus();
			}
			// start with the most severe status and work up from there
			else if (protocolNode.getCollectWinStatus().equals(PROTOCOL_NOT_STARTED)) {
				updatedCollectWinStatus = protocolNode.getCollectWinStatus();
			}
			else if (protocolNode.getCollectWinStatus().equals(PENDING)) {
				// only update if this instrument status is more severe
				if (!updatedCollectWinStatus.equals(PROTOCOL_NOT_STARTED)) {
					updatedCollectWinStatus =  protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(LATE)) {
				// only update if this instrument status is more severe
				if (!updatedCollectWinStatus.equals(PROTOCOL_NOT_STARTED) && !updatedCollectWinStatus.equals(PENDING)) {
					updatedCollectWinStatus = protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(EARLY)) {
				// only update if this instrument status is more severe
				if (!updatedCollectWinStatus.equals(PROTOCOL_NOT_STARTED) && !updatedCollectWinStatus.equals(PENDING)
						&& !updatedCollectWinStatus.equals(LATE)) {
					updatedCollectWinStatus = protocolNode.getCollectWinStatus();
				}
			}
		}
		return updatedCollectWinStatus;
	}

}
