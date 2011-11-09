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
 * 
 * @author ctoohey
 *
 */
public abstract class PatientProtocolNode extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientProtocolNode.class);
	// projName is denormalized and stored in the node so every level of the protocol tree stores
	// the projName for efficient project authorization filtering
	private Patient patient;
	private String projName;
	private Short listOrder;
	private String currStatus;
	private String currReason;
	private String currNote;
	
	public PatientProtocolNode(){
		super();
		this.setProjectAuth(true);
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
	
}
