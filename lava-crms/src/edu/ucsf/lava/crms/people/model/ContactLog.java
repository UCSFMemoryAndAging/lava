package edu.ucsf.lava.crms.people.model;

import java.sql.Time;
import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

public class ContactLog extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(ContactLog.class);
	private Patient patient;
	private String projName;
	private Date logDate;
	private Time logTime;
	private String method;
	private Short staffInit;
	private String staff;
	private String contact;
	private String note;

	
	public ContactLog() {
		super();
		
		this.setProjectAuth(true);
		staffInit = 1;
	}
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}
	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}



	
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	public Time getLogTime() {
		return logTime;
	}
	public void setLogTime(Time logTime) {
		this.logTime = logTime;
	}
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getStaff() {
		return staff;
	}

	public void setStaff(String staff) {
		this.staff = staff;
	}

	public Short getStaffInit() {
		return staffInit;
	}

	public void setStaffInit(Short staffInit) {
		this.staffInit = staffInit;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	
	
}
