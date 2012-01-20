package edu.ucsf.lava.crms.protocol.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;

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
	public static String PENDING_NOW = "Pending-Now";
	public static String IN_PROGRESS = "In Progress";
	public static String COMPLETED = "Completed";
	public static String NOT_COMPLETED = "Not Completed";
	public static String PARTIAL = "Partial";
	public static String LATE = "Late";
	public static String EARLY = "Early";
	public static String COLLECTED = "Collected";
    public static String N_A = "N/A";
    public static String SCHEDULED = "Scheduled";
    public static String PENDING_LATE = "Pending-Late";
    public static String TBD = "TBD";
	
	private Patient patient;
	// because every type of Protocol tree node subclasses this class, projName is 
	// denormalized (which makes for simpler configuration of project authorization filtering)
	private String projName;
	private Short listOrder;
	private Short strategy;
	private String currStatus;
	private String currReason;
	private String currNote;
	private String compStatus;
	private String compReason;
	private String compNote;
	private String compBy;
	private Date compDate;
	private String schedWinStatus;
	private String schedWinReason;
	private String schedWinNote;
	private String collectWinStatus;
	private String collectWinReason;
	private String collectWinNote;
	private String assignDescrip;
	private String notes;
	
	public ProtocolNode(){
		super();
		this.setProjectAuth(true);
		this.addPropertyToAuditIgnoreList("patient");
		this.setStrategy((short)0);
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

	public Short getListOrder() {
		return listOrder;
	}

	public void setListOrder(Short listOrder) {
		this.listOrder = listOrder;
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

	public String getAssignDescrip() {
		return assignDescrip;
	}

	public void setAssignDescrip(String assignDescrip) {
		this.assignDescrip = assignDescrip;
	}

	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
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
	
	protected String rollupCompStatusHelper(ProtocolNode protocolNode, String updatedCompStatus) {
		if (protocolNode.getCompStatus() != null) {
			if (updatedCompStatus == null) {
				updatedCompStatus = protocolNode.getCompStatus();
			}
			// start with the most severe status and work up from there
			else if (protocolNode.getCompStatus().equals(NOT_COMPLETED)) {
				updatedCompStatus = protocolNode.getCompStatus();
			}
			else if (protocolNode.getCompStatus().equals(PARTIAL)) {
				// only update if this instrument status is more severe
				if (!updatedCompStatus.equals(NOT_COMPLETED)) {
					updatedCompStatus =  protocolNode.getCompStatus();
				}
			}
			else if (protocolNode.getCompStatus().equals(PENDING)) {
				// only update if this instrument status is more severe
				if (updatedCompStatus.equals(IN_PROGRESS) || updatedCompStatus.equals(COMPLETED)) {
					updatedCompStatus = protocolNode.getCompStatus();
				}
			}
			else if (protocolNode.getCompStatus().equals(IN_PROGRESS)) {
				// only update if this instrument status is more severe
				if (updatedCompStatus.equals(COMPLETED)) {
					updatedCompStatus = protocolNode.getCompStatus();
				}
			}
		}
		return updatedCompStatus;
	}
	
	protected String rollupSchedWinStatusHelper(ProtocolNode protocolNode, String updatedSchedWinStatus) {
		if (protocolNode.getCollectWinStatus() != null) {
			if (updatedSchedWinStatus == null) {
				updatedSchedWinStatus = protocolNode.getCollectWinStatus();
			}
			// start with the most severe status and work up from there
			else if (protocolNode.getCollectWinStatus().equals(TBD)) {
				updatedSchedWinStatus = protocolNode.getCollectWinStatus();
			}
			else if (protocolNode.getCollectWinStatus().equals(PENDING_LATE)) {
				if (!updatedSchedWinStatus.equals(TBD)) {
					updatedSchedWinStatus = protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(LATE)) {
				// only update if this instrument status is more severe
				if (!updatedSchedWinStatus.equals(PENDING_LATE)) {
					updatedSchedWinStatus =  protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(PENDING_NOW)) {
				// only update if this instrument status is more severe
				if (!updatedSchedWinStatus.equals(PENDING_LATE) && !updatedSchedWinStatus.equals(LATE)) {
					updatedSchedWinStatus = protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(PENDING)) {
				// only update if this instrument status is more severe
				if (!updatedSchedWinStatus.equals(PENDING_LATE) && !updatedSchedWinStatus.equals(LATE)
						&& !updatedSchedWinStatus.equals(PENDING_NOW)) {
					updatedSchedWinStatus = protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(EARLY) 
					|| protocolNode.getCollectWinStatus().equals(SCHEDULED)) {
				// this will handle the situation where one of the instruments has colletWinStatus of N_A,
				// which should be replaced by any other collectWinStatus
				if (!updatedSchedWinStatus.startsWith(PENDING) && !updatedSchedWinStatus.equals(LATE)) {
					
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
			else if (protocolNode.getCollectWinStatus().equals(TBD)) {
				updatedCollectWinStatus = protocolNode.getCollectWinStatus();
			}
			else if (protocolNode.getCollectWinStatus().equals(PENDING_LATE)) {
				if (!updatedCollectWinStatus.equals(TBD)) {
					updatedCollectWinStatus = protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(LATE)) {
				// only update if this instrument status is more severe
				if (!updatedCollectWinStatus.equals(PENDING_LATE)) {
					updatedCollectWinStatus =  protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(PENDING_NOW)) {
				// only update if this instrument status is more severe
				if (!updatedCollectWinStatus.equals(PENDING_LATE) && !updatedCollectWinStatus.equals(LATE)) {
					updatedCollectWinStatus = protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(PENDING)) {
				// only update if this instrument status is more severe
				if (!updatedCollectWinStatus.equals(PENDING_LATE) && !updatedCollectWinStatus.equals(LATE)
						&& !updatedCollectWinStatus.equals(PENDING_NOW)) {
					updatedCollectWinStatus = protocolNode.getCollectWinStatus();
				}
			}
			else if (protocolNode.getCollectWinStatus().equals(EARLY) 
					|| protocolNode.getCollectWinStatus().equals(COLLECTED)) {
				// this will handle the situation where one of the instruments has colletWinStatus of N_A,
				// which should be replaced by any other collectWinStatus
				if (!updatedCollectWinStatus.startsWith(PENDING) && !updatedCollectWinStatus.equals(LATE)) {
					
				}
				
			}
		}
		return updatedCollectWinStatus;
	}

}
