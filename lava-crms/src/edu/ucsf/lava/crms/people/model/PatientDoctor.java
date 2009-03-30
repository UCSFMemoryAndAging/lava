package edu.ucsf.lava.crms.people.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class PatientDoctor extends EntityBase {
	public static EntityManager MANAGER = new EntityBase.Manager(PatientDoctor.class);
	
	
	private Doctor doctor;
	private Patient patient;
	private String docStat;
	private String docNote;

	
	
	public PatientDoctor(){
		super();
	}
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient,this.doctor};
	}
	public String getDocNote() {
		return docNote;
	}
	public void setDocNote(String docNote) {
		this.docNote = docNote;
	}
	public String getDocStat() {
		return docStat;
	}
	public void setDocStat(String docStat) {
		this.docStat = docStat;
	}
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	
	}
	
	public Patient getPatient() {
		return patient;
	}

	
	public void setPatient(Patient patient) {
		this.patient = patient;
	
	}

	
	
}
