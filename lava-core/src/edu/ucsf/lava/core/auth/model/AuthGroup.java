package edu.ucsf.lava.core.auth.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;



public class AuthGroup extends EntityBase {
	
	public static EntityManager MANAGER = new EntityBase.Manager(AuthGroup.class);
	
	private String groupName;
	private Date effectiveDate;
	private Date expirationDate;
	private String notes;
	
	
	private Set<AuthUserGroup> users;
	public AuthGroup() {}
	
	public boolean getPatientAuth() {
		return false;
	}


	
	public Set<AuthUserGroup> getUsers() {
		return users;
	}

	public void setUsers(Set<AuthUserGroup> users) {
		this.users = users;
	}
	
	public void addUser(AuthUserGroup userGroup){
		if(userGroup==null){return;}
		userGroup.setGroup(this);
		this.users.add(userGroup);
	}
	

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}



	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public Boolean getDisabled() {
		if(this.getExpirationDate()==null || this.getExpirationDate().after(new Date())){
			return false;
		}
		return true;
	}



	public void setDisabled(Boolean disabled) {
		if(disabled && (this.getExpirationDate()==null || this.getExpirationDate().after(new Date()))){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DAY_OF_MONTH,-1);
			this.setExpirationDate(calendar.getTime());
		}else if(!disabled && this.getExpirationDate()!=null && this.getExpirationDate().before(new Date())){
			this.setExpirationDate(null);
		}
	}


	public String getGroupNameWithStatus(){
		if(this.getDisabled()){
			return new StringBuffer(this.getGroupName()).append(" (Disabled)").toString();
		}
		return this.getGroupName();
	}

}
