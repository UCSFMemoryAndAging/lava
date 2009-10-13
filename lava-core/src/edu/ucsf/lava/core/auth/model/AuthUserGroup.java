package edu.ucsf.lava.core.auth.model;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;




// note: the mapping for this class is within the mapping for the AuthGroup class
public class AuthUserGroup extends EntityBase {

	
	public static EntityManager MANAGER = new EntityBase.Manager(AuthUserGroup.class);

	private AuthUser user;
	private Long userId;
	private AuthGroup group;
	private Long groupId;
	private String notes;

	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.group,this.user};
	}
	
	
	public AuthGroup getGroup() {
		return group;
	}
	public void setGroup(AuthGroup group) {
		this.group = group;
		if(this.group!=null){
			this.groupId = this.group.getId();
		}
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
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

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	

	
}
