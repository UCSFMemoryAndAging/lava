package edu.ucsf.lava.core.calendar.model;


import java.util.Date;
import java.util.List;


import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.CoreEntity;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.type.DateRange;

public class Calendar extends CoreEntity {
	public static EntityManager MANAGER = new Calendar.Manager();
	public static String CALENDAR_TYPE = "Calendar";
	
	
	public Calendar(){
		super();
		setType(CALENDAR_TYPE);
	}
	
	protected String name;
	protected String type;
	protected String description;
	protected String notes;
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * Get all appointments for this calendar
	 * @return
	 */
	public List<Appointment> getAppointments() {
		return getAppointments(null,null,Appointment.newFilterInstance());
	}

	/**
	 * Get all the appointments that overlap the supplied appointment
	 * @return
	 */
	public List<Appointment> getConcurrentAppointments(Appointment appointment) {
		LavaDaoFilter filter = Appointment.newFilterInstance();
		//exclude records with the same id as this reservation
		if(appointment.getId()!=null && appointment.getId() > 0 ){
			filter.addDaoParam(filter.daoNot(filter.daoEqualityParam("id",appointment.getId())));
		}
		return getAppointments(null,appointment.getDateRange(),filter);
	}

	
	
	
	/**
	 * Get all the appointments for this calendar that
	 * overlap the given date range
	 * @param range
	 * @return
	 */
	public List<Appointment> getAppointments(DateRange range) {
		return getAppointments(null,range,Appointment.newFilterInstance());
	}

	
	/**
	 * Get all the appointments for the calendar for a specific user 
	 * 
	 * @param organizer
	 * @return
	 */
	public List<Appointment> getAppointments(AuthUser user) {
		return getAppointments(user,null,Appointment.newFilterInstance());
	}

	/**
	 * Get all the appointments for the  calendar for the given 
	 * user that overlap the given date range
	 * @param organizer
	 * @param range
	 * @return
	 */
	public List<Appointment> getAppointments(AuthUser user, DateRange range, LavaDaoFilter filter) {
			
		
		filter.setAlias("calendar","calendar");
		filter.addDaoParam(filter.daoEqualityParam("calendar.id", this.getId()));
		
			
		if(user!=null){
			filter.addDaoParam(CalendarDaoUtils.getAttendeesParam(user, filter));
		}
		if(range!=null && range.hasRange()){
			filter.addDaoParam(CalendarDaoUtils.getDateRangeOverlapParam(range,filter));
		}
		return Appointment.MANAGER.get(filter);
	}
	
	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(Calendar.class);
		}
		
		

	}	
	
}
