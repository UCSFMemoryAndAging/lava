package edu.ucsf.lava.core.calendar.model;

import java.sql.Time;
import java.util.Date;
import java.util.Set;

import edu.ucsf.lava.core.model.CoreEntity;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.type.DateRange;


public class Appointment extends CoreEntity {

	public static EntityManager MANAGER = new EntityBase.Manager(Appointment.class);
	public static String APPOINTMENT_TYPE = "Appointment";
	public static String STATUS_SCHEDULED = "Scheduled";
	public static String STATUS_CANCELED = "Canceled";
	
	protected Calendar calendar;
	protected Long calendarId;
	protected edu.ucsf.lava.core.auth.model.AuthUser organizer;
	protected Long organizerId;
	
	protected String type;
	protected String description;
	protected String location;
	protected Date startDate;
	protected Time startTime;
	protected Date endDate;
	protected Time endTime;
	protected String status;
	protected String notes;
	
	protected Set attendees;
	
	protected Set changes;
	
	public Appointment() {
		super();
		setType(APPOINTMENT_TYPE);
	}

	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.calendar,this.organizer, this.attendees};
	}
	
	
	public Calendar getCalendar() {
		return calendar;
	}
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		if(this.calendar != null){
			this.calendarId = this.calendar.getId();
		}
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public edu.ucsf.lava.core.auth.model.AuthUser getOrganizer() {
		return organizer;
	}
	public void setOrganizer(edu.ucsf.lava.core.auth.model.AuthUser owner) {
		this.organizer = owner;
		if(this.organizer != null){
			this.organizerId = this.organizer.getId();
		}
	}
	
	
	public Set getAttendees() {
		return attendees;
	}

	public void setAttendees(Set attendees) {
		this.attendees = attendees;
	}
	
	public Long getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Long calendarId) {
		this.calendarId = calendarId;
	}
	public Long getOrganizerId() {
		return organizerId;
	}
	public void setOrganizerId(Long ownerId) {
		this.organizerId = ownerId;
	}
	
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public DateRange getDateRange(){
		return new DateRange(getStartDate(),getStartTime(),getEndDate(),getEndTime());
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set getChanges() {
		return changes;
	}

	public void setChanges(Set changes) {
		this.changes = changes;
	}

	


	
}
