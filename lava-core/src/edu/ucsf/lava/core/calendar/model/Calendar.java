package edu.ucsf.lava.core.calendar.model;


import java.lang.reflect.Array;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.CoreEntity;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.type.DateRange;
import edu.ucsf.lava.core.type.LavaDateUtils;


public class Calendar extends CoreEntity {
	public static EntityManager MANAGER = new Calendar.Manager();
	public static String CALENDAR_TYPE = "Calendar";
	public static Time DEFAULT_WORK_BEGIN_TIME = LavaDateUtils.getTime("09:00:00");
	public static Time DEFAULT_WORK_END_TIME = LavaDateUtils.getTime("17:00:00");
	
	public Calendar() throws Exception{
		super();
		setType(CALENDAR_TYPE);
		this.workBeginTime = DEFAULT_WORK_BEGIN_TIME;
		this.workEndTime = DEFAULT_WORK_END_TIME;
	}
	
	protected String name;
	protected String type;
	protected String description;
	protected String notes;
	protected Time workBeginTime;
	protected Time workEndTime;
	protected String workDays;
	
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
	
	public String getWorkDays() {
		return workDays;
	}
	public void setWorkDays(String workDays) {
		this.workDays = workDays;
	}
	public Time getWorkBeginTime() {
		return workBeginTime;
	}
	
	public void setWorkBeginTime(Time workBeginTime) {
		this.workBeginTime = workBeginTime;
	}
	
	public Time getWorkEndTime() {
		return workEndTime;
	}
	
	public void setWorkEndTime(Time workEndTime) {
		this.workEndTime = workEndTime;
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
			filter.addDaoParam(CalendarDaoUtils.getOrganizerParam(user, filter));
		}
		if(range!=null && range.hasRange()){
			filter.addDaoParam(CalendarDaoUtils.getDateRangeOverlapParam(range,filter));
		}
		
		// exclude canceled appointments by default
		if (!filter.getParams().containsKey("status")){
			filter.addDaoParam(filter.daoNot(filter.daoEqualityParam("status", Appointment.STATUS_CANCELED)));
		}
		
		return Appointment.MANAGER.get(filter);
	}
	
	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(Calendar.class);
		}
		
	}


}
