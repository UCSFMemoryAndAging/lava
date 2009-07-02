package edu.ucsf.lava.core.calendar.model;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.CoreEntity;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class Attendee extends CoreEntity {

	public static String STATUS_ACCEPTED = "Accepted";
	public static String STATUS_DECLINED = "Declined";
	public static String STATUS_TENTATIVE = "Tentative";
	public static String STATUS_UNKNOWN = "Unknown";
	
	public static String ROLE_REQUIRED = "Required";
	public static String ROLE_OPTIONAL = "Optional";
	public static String ROLE_ORGANIZER = "Organizer";
	
	
	
	public static EntityManager MANAGER = new EntityBase.Manager(Attendee.class);

	protected Appointment appointment;
	protected AuthUser user;
	protected Long userId;
	protected String role;
	protected String status;
	protected String notes;
	
	
	
	public Appointment getAppointment() {
		return appointment;
	}
	public void setAppointment(Appointment appointment) {
		this.appointment = appointment;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
		if(this.user != null){
			this.userId = this.user.getId();
		}
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	
	
	
	
}
