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
 *   ProtocolTimepoint (subclass: ProtocolAssessmentTimepoint)
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

	private Patient patient;
	// because every type of Protocol tree node subclasses this class, projName is 
	// denormalized (which makes for simpler configuration of project authorization filtering)
	private String projName;
	private Short listOrder;
	private String currStatus;
	private String currReason;
	private String currNote;
	private String notes;
	
	public ProtocolNode(){
		super();
		this.setProjectAuth(true);
		this.addPropertyToAuditIgnoreList("patient");
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
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	
}
