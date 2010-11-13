package edu.ucsf.lava.core.calendar.model;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.type.DateRange;
import edu.ucsf.lava.core.type.LavaDateUtils;

public class ResourceCalendar extends Calendar {
	public static EntityManager MANAGER = new ResourceCalendar.Manager();
	
	
	public static String CALENDAR_TYPE_RESOURCE = "Resource";

	
	
	public ResourceCalendar() throws Exception{
		super();
		this.setType(CALENDAR_TYPE_RESOURCE);
		this.peakUsageBeginTime = workBeginTime;
		this.peakUsageEndTime = workEndTime;
	}
	protected String resourceType;
	protected String location;
	protected AuthUser contact;
	protected Long contactId;
	protected String peakUsageDays;
	protected Time peakUsageBeginTime;
	protected Time peakUsageEndTime;

	public String getPeakUsageDays() {
		return peakUsageDays;
	}

	public void setPeakUsageDays(String peakUsageDays) {
		this.peakUsageDays = peakUsageDays;
	}

	public Time getPeakUsageBeginTime() {
		return peakUsageBeginTime;
	}

	public void setPeakUsageBeginTime(Time peakUsageBeginTime) {
		this.peakUsageBeginTime = peakUsageBeginTime;
	}

	public Time getPeakUsageEndTime() {
		return peakUsageEndTime;
	}

	public void setPeakUsageEndTime(Time peakUsageEndTime) {
		this.peakUsageEndTime = peakUsageEndTime;
	}

	public List<DateRange> getPeakHoursOverlaps(DateRange testRange){
		List<DateRange> peakHoursOverlaps = new ArrayList<DateRange>();
			if (testRange!=null && this.peakUsageDays!=null && this.peakUsageBeginTime!=null && this.peakUsageEndTime!=null){
				List<DateRange> dailyRanges = testRange.getDailyRanges();
				for (DateRange dailyRange : dailyRanges){
					Integer dailyRangeDayNum = LavaDateUtils.toCalendar(dailyRange.getStart()).get(java.util.Calendar.DAY_OF_WEEK);
					if (this.peakUsageDays.contains(dailyRangeDayNum.toString())){
						DateRange peakHours = new DateRange (LavaDateUtils.setTimePart(dailyRange.getStart(), this.peakUsageBeginTime),
								LavaDateUtils.setTimePart(dailyRange.getStart(), this.peakUsageEndTime));
						DateRange peakHoursOverlap = dailyRange.getOverlap(peakHours);
						if (peakHoursOverlap.hasRange()) {
							peakHoursOverlaps.add(peakHoursOverlap);
						}
					}
				}
			}
		
		return peakHoursOverlaps;
	}
	
	public AuthUser getContact() {
		return contact;
	}
	
	public void setContact(AuthUser contact) {
		this.contact = contact;
		if(this.contact!=null){
			this.contactId = this.contact.getId();
		}
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}


	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(ResourceCalendar.class);
		}
		
		

	}	
	
	
	
	
	
	
	
}
