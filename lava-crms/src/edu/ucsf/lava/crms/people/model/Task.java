package edu.ucsf.lava.crms.people.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

public class Task extends CrmsEntity {
	
	public static EntityManager MANAGER = new EntityBase.Manager(Task.class);
	
	private Patient patient;
	private String projName;
	private Date openedDate;
	private String openedBy;
	private String taskType;
	private String taskDesc;
	private Date dueDate;
	private String taskStatus;
	private String assignedTo;
	private String workingNotes;
	private Date closedDate;

	
	
	public Task(){
		super();
		this.setProjectAuth(true);
	}
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}
	
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public Date getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	public String getOpenedBy() {
		return openedBy;
	}
	public void setOpenedBy(String openedBy) {
		this.openedBy = openedBy;
	}
	public Date getOpenedDate() {
		return openedDate;
	}
	public void setOpenedDate(Date openedDate) {
		this.openedDate = openedDate;
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
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getWorkingNotes() {
		return workingNotes;
	}
	public void setWorkingNotes(String workingNotes) {
		this.workingNotes = workingNotes;
	}
	


	
}
