package edu.ucsf.lava.core.calendar.model;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class ResourceCalendar extends Calendar {
	public static EntityManager MANAGER = new ResourceCalendar.Manager();
	
	
	public static String CALENDAR_TYPE_RESOURCE = "Resource";

	
	
	public ResourceCalendar() {
		super();
		this.setType(CALENDAR_TYPE_RESOURCE);
		
	}
	protected String resourceType;
	protected String location;
	protected AuthUser contact;
	protected Long contactId;

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
