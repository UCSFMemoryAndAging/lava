package edu.ucsf.lava.core.auth.model;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;


public class AuthPermission extends EntityBase {

	
	public static EntityManager MANAGER = new EntityBase.Manager(AuthPermission.class);

	public final static String DEFAULT_PERMISSION_ROLE = "DEFAULT PERMISSIONS";
	public final static String ANY_WILDCARD = "*";
	public final static String PERMIT_VALUE = "PERMIT";
	public final static String DENY_VALUE = "DENY";
	
	private AuthRole role;
	private Long roleId;
	private String permitDeny;
	private String scope;
	private String module;
	private String section;
	private String target;
	private String mode;
	private String notes;
	
	public AuthPermission(){
		super();
	}
	

	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.role};
	}

	

	public boolean getPatientAuth() {
		return false;
	}


	public String getScope() {
		return scope;
	}


	public void setScope(String scope) {
		this.scope = scope;
	}


	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getPermitDeny() {
		return permitDeny;
	}
	public void setPermitDeny(String permitDeny) {
		this.permitDeny = permitDeny;
	}
	public AuthRole getRole() {
		return role;
	}
	public void setRole(AuthRole role) {
		this.role = role;
		if(this.role!=null){
			this.roleId = role.getId();
		}
	
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}



	public Long getRoleId() {
		return roleId;
	}



	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	
	public boolean permits(){
		if(PERMIT_VALUE.equals(this.getPermitDeny())){
			return true;
		}
		return false;
	}
	public boolean denies(){
		if(DENY_VALUE.equals(this.getPermitDeny())){
			return true;
		}
		return false;
	}
	public boolean matches(Action action, String mode){
		return matches(action.getScope(),action.getModule(),action.getSection(),action.getTarget(),mode);
	}
	
	public boolean matches(String scope, String module,String section, String target,String mode){
			if((this.scope.equals(ANY_WILDCARD) || this.scope.equals(scope)) &&
			   (this.module.equals(ANY_WILDCARD) || this.module.equals(module)) &&
			   (this.section.equals(ANY_WILDCARD) || this.section.equals(section)) &&	
			   (this.target.equals(ANY_WILDCARD) || this.target.equals(target)) &&	
			   (this.mode.equals(ANY_WILDCARD) || this.mode.equals(mode))){
				return true;
			}
			return false;
	}
	
	
	/**
	 * The permission is overridden by the permission passed in if it has a lower level of specificity as 
	 * determined by values specified without using the ANY_WILDCARD value.
	 * specificity-order:
	 * 		1) scope-module-section-target-mode
	 * 		2) scope-module-section-target
	 * 		3) scope-module-section
	 * 		4) scope-module
	 * 		5) scope
	 * 
	 * If the permission has the same level of specificity of the permission passed in then the
	 * result is false (is not overridden). 
	 * 
	 * @param permissionIn
	 * @return
	 */
	public boolean isOverriddenBy(AuthPermission permissionIn){
		if(!permissionIn.getScope().equals(ANY_WILDCARD)){
			if(this.getScope().equals(ANY_WILDCARD)){
				return true;
			}
			if(!permissionIn.getScope().equals(this.getScope())){
				return false;
			}
		}else if(!this.getScope().equals(ANY_WILDCARD)){
				return false;
		}
		if(!permissionIn.getModule().equals(ANY_WILDCARD)){
			if(this.getModule().equals(ANY_WILDCARD)){
				return true;
			}
			if(!permissionIn.getModule().equals(this.getModule())){
				return false;
			}
		}else if(!this.getModule().equals(ANY_WILDCARD)){
				return false;
		}
		if(!permissionIn.getSection().equals(ANY_WILDCARD)){
			if(this.getSection().equals(ANY_WILDCARD)){
				return true;
			}
			if(!permissionIn.getSection().equals(this.getSection())){
				return false;
			}
		}else if(!this.getSection().equals(ANY_WILDCARD)){
				return false;
		}
		
		if(!permissionIn.getTarget().equals(ANY_WILDCARD)){
			if(this.getTarget().equals(ANY_WILDCARD)){
				return true;
			}
			if(!permissionIn.getTarget().equals(this.getTarget())){
				return false;
			}
		}else if(!this.getTarget().equals(ANY_WILDCARD)){
				return false;
		}
		
		
		if(!permissionIn.getMode().equals(ANY_WILDCARD)){
			if(this.getMode().equals(ANY_WILDCARD)){
				return true;
			}
			if(!permissionIn.getMode().equals(this.getMode())){
				return false;
			}
		}else if(!this.getMode().equals(ANY_WILDCARD)){
				return false;
		}
		//if we get here then the rules are equivalent and thus the permission is not 
		//overridden by the permission passed in 
		return false;
	}
}
