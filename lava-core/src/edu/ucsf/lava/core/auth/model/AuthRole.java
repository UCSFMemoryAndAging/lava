package edu.ucsf.lava.core.auth.model;

import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;



public class AuthRole extends EntityBase {
	
	
	public static EntityManager MANAGER = new EntityBase.Manager(AuthRole.class);

	private String roleName;
	private String notes;

	private Set<AuthUserRole> users;
	private Set<AuthPermission> permissions;
	
	public AuthRole(){
		super();
		this.addPropertyToAuditIgnoreList("users");
		this.addPropertyToAuditIgnoreList("permissions");
		
	}
	
	public boolean getPatientAuth() {
		return false;
	}

	public void addPermission(AuthPermission permission){
		if(permission==null){return;}
		permission.setRole(this);
		this.permissions.add(permission);
	}
	
	public Set<AuthPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<AuthPermission> permissions) {
		this.permissions = permissions;
	}

	public Set<AuthUserRole> getUsers() {
		return users;
	}

	public void setUsers(Set<AuthUserRole> users) {
		this.users = users;
	}

	public void addUser(AuthUserRole userRole){
		if(userRole==null){return;}
		userRole.setRole(this);
		this.users.add(userRole);
	}
	

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	
	
	/**
	 * Override this method in subclasses to provide a short 
	 * summary description of any extended access or customization
	 * related to roles  E.g. the patient access flag in crms applications. 
	 * @return
	 */
	public String getSummaryInfo(){
		return new String();
	}
}
