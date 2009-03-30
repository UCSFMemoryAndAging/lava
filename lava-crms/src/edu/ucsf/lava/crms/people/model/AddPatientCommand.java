package edu.ucsf.lava.crms.people.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.model.CrmsEntity;

public class AddPatientCommand extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(AddPatientCommand.class);
	//note: this command class is also used for adding enrollment status
	
	private Boolean ignoreMatches;
	private Boolean deidentified;
	protected Patient patient;
	protected EnrollmentStatus enrollmentStatus;
	
	private String subjectId;
	

	private String Status;
	private Date StatusDate;
	

	
	public AddPatientCommand(){
		this.ignoreMatches = Boolean.FALSE;
		this.deidentified = Boolean.FALSE;
		//this is really just a data transfer object, so turn off auditing and authorization 
		this.setAudited(false); 
		this.setPatientAuth(false);
	}
	
	
	


	public Long getId() {
		return getPatient().getId();
	}
	public void setId(Long id) {
		//do nothing
	}
	
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public Date getStatusDate() {
		return StatusDate;
	}
	public void setStatusDate(Date statusDate) {
		StatusDate = statusDate;
	}
	public Boolean getDeidentified() {
		return deidentified;
	}
	public void setDeidentified(Boolean deidentified) {
		this.deidentified = deidentified;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	
	public EnrollmentStatus getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	public Boolean getIgnoreMatches() {
		return ignoreMatches;
	}
	public void setIgnoreMatches(Boolean ignoreMatches) {
		this.ignoreMatches = ignoreMatches;
	}

}	

	
	