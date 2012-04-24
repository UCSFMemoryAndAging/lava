package edu.ucsf.lava.core.auth.model;


import edu.ucsf.lava.core.auth.AuthorizationContext;
import edu.ucsf.lava.core.auth.CoreAuthorizationContext;
import edu.ucsf.lava.core.model.CoreEntity;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.scope.ScopeAuthorizationContext;

public class AuthUserRole extends CoreEntity {

	
	public static EntityManager MANAGER = new EntityBase.Manager(AuthUserRole.class);

	private AuthRole role;
	private Long roleId;
	private AuthUser user;
	private Long userId;
	private AuthGroup group;
	private Long groupId;
	private String notes;

	
	
	
	public AuthUserRole(){
		super();
		}
	

	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.group,this.user,this.role};
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
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public AuthRole getRole() {
		return role;
	}
	public void setRole(AuthRole role) {
		this.role = role;
		if(this.role!=null){
			this.roleId = this.role.getId();
		}
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	public AuthUser getUser() {
		return user;
	}
	public void setUser(AuthUser user) {
		this.user = user;
		if(this.user!=null){
			this.userId = this.user.getId();
		}
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * Override this method in subclasses to provide a short 
	 * summary description of any extended context or customization
	 * related to the assignments of roles to user/groups  E.g. the 
	 * project that the role is associated with in a crms application. 
	 * @return
	 */
	public String getSummaryInfo(){
		return new String();
	}


	

	/**
	 * Default implementation returns a CoreAuthorizationContext.  
	 * Every role must be defined in a particular scope and the default
	 * scope for a base AuthUserRole is 'core'.  All other scope will 
	 * need to subclass this AuthUserRole to support specific authorization
	 * context requirements. 
	 * @return
	 */
	public AuthorizationContext getAuthorizationContext(){
		return new CoreAuthorizationContext();
	}
	
	

	public boolean isContextAuthorized(AuthorizationContext authorizationContext) {
		if (((ScopeAuthorizationContext)authorizationContext).getScope().equals(CoreAuthorizationContext.CORE_SCOPE)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	
	
		
}
