package edu.ucsf.lava.core.calendar.model;

import java.sql.Timestamp;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.CoreEntity;

public class AppointmentChange extends CoreEntity {
	
	
	protected Appointment appointment;
	protected String type;
	protected String description;
	protected AuthUser changeBy;
	protected Timestamp changeTimestamp;
	
	
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	public AuthUser getChangeBy() {
		return changeBy;
	}
	public void setChangeBy(AuthUser changeBy) {
		this.changeBy = changeBy;
	}
	public Timestamp getChangeTimestamp() {
		return changeTimestamp;
	}
	public void setChangeTimestamp(Timestamp changedTimestamp) {
		this.changeTimestamp = changedTimestamp;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	
	
	
}
