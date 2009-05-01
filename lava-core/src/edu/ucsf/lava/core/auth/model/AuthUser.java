package edu.ucsf.lava.core.auth.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;

import edu.ucsf.lava.core.auth.AuthDaoUtils;
import edu.ucsf.lava.core.auth.AuthUserPermissionCache;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;

public class AuthUser extends EntityBase implements UserDetails {
	
	
	public static AuthUser.Manager MANAGER = new AuthUser.Manager();

	protected String userName;
	protected String login;
	protected String hashedPassword;
	protected String email;
	protected String phone;
	protected Date accessAgreementDate;
	protected Date effectiveDate;
	protected Date expirationDate;
	protected String shortUserName; 
	protected String shortUserNameRev; 
	protected String notes;

	protected boolean authUserContextInit = false; //flag used by authUserContext filters to determine when to perform initialization actions.
	protected Set<AuthUserGroup> groups;
	protected Set<AuthUserRole> roles;
	
	protected List<AuthUserRole> effectiveRoles;
	

	
	protected AuthUserPermissionCache permissionCache = new AuthUserPermissionCache();
	
	public AuthUser(){
		super();
		this.addPropertyToAuditIgnoreList("groups");
		this.addPropertyToAuditIgnoreList("roles");
		
	}
	
	
	
	


	public Set<AuthUserGroup> getGroups() {
		return groups;
	}



	public void setGroups(Set<AuthUserGroup> groups) {
		this.groups = groups;
	}

	public void addGroup(AuthUserGroup userGroup){
		if(userGroup==null){return;}
		userGroup.setUser(this);
		this.groups.add(userGroup);
	}

	public Set<AuthUserRole> getRoles() {
		return roles;
	}



	public void setRoles(Set<AuthUserRole> roles) {
		this.roles = roles;
	}

	public void addRole(AuthUserRole userRole){
		if(userRole==null){return;}
		userRole.setUser(this);
		this.roles.add(userRole);
	}

	public boolean getPatientAuth() {
		return false;
	}

	public Date getAccessAgreementDate() {
		return accessAgreementDate;
	}

	public void setAccessAgreementDate(Date accessAgreementDate) {
		this.accessAgreementDate = accessAgreementDate;
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



	public String getHashedPassword() {
		return hashedPassword;
	}






	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}






	public String getEmail() {
		return email;
	}






	public void setEmail(String email) {
		this.email = email;
	}






	public String getPhone() {
		return phone;
	}






	public void setPhone(String phone) {
		this.phone = phone;
	}






	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getShortUserName() {
		return shortUserName;
	}

	public void setShortUserName(String shortUserName) {
		this.shortUserName = shortUserName;
	}

	public String getShortUserNameRev() {
		return shortUserNameRev;
	}

	public void setShortUserNameRev(String shortUserNameRev) {
		this.shortUserNameRev = shortUserNameRev;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}



	public List<AuthUserRole> getEffectiveRoles() {
		return effectiveRoles;
	}



	public void setEffectiveRoles(List<AuthUserRole> effectiveRoles) {
		this.effectiveRoles = effectiveRoles;
	}

	/**
	 * Return a map of Authorization Dao Filters to be used by
	 * the dao for filter all data access based on user authorization
	 * There is no data authorization filtering in the core scope, so 
	 * simply return an empty map.  Subclasses for other scopes should 
	 * override this method. 
	 * 
	 * The data structure to return is a map where the key = the dao filter
	 * name and the value is another map of filter paramaters with the key =
	 * the param name and the value is an appropriately typed value or a list 
	 * of values for use in an IN clause
	 * @return
	 */
	public Map<String,Map<String,Object>> getAuthDaoFilters(){
		return new HashMap<String,Map<String,Object>>();
	}
	


	



	public AuthUserPermissionCache getPermissionCache() {
		return permissionCache;
	}




	public Boolean getDisabled() {
		if(this.getExpirationDate()==null || this.getExpirationDate().after(new Date())){
			return false;
		}
		return true;
	}



	public void setDisabled(Boolean disabled) {
		if(disabled && (this.getExpirationDate()==null || this.getExpirationDate().after(new Date()))){
			this.setExpirationDate(dateWithoutTime(new Date()));
		}else if(!disabled && this.getExpirationDate()!=null && this.getExpirationDate().before(new Date())){
			this.setExpirationDate(null);
		}
	}




	public String getUserNameWithStatus(){
		if(this.getDisabled()){
			return new StringBuffer(this.getUserName()).append(" (Disabled)").toString();
		}
		return this.getUserName();
	}

	
	
	
	public void updateCalculatedFields() {
		super.updateCalculatedFields();
		updateShortUserName();
		updateShortUserNameRev();
	}
	
	protected void updateShortUserName(){
		String userName = getUserName();
		if(userName==null){
			setShortUserName(null);
			return;
		}
		if(!userName.contains(" ")){
				setShortUserName(userName);
		}else{
			setShortUserName(new StringBuffer(userName.substring(0,1))
				.append(". ").append(userName.substring(userName.indexOf(" "), userName.length())).toString());
		}
	
		return;
}

	protected void updateShortUserNameRev(){
		String userName = getUserName();
		if(userName==null){
			setShortUserNameRev(null);
			return;
		}
		if(!userName.contains(" ")){
				setShortUserNameRev(userName);
		}else{
			setShortUserNameRev(new StringBuffer(userName.substring(userName.indexOf(" "), userName.length()))
					.append(", ").append(userName.substring(0,1)).toString());
		}
		return;
}
		





	//TODO: may need to filter this by an authUser
	public List<AuthUserRole> getUserRoles() {
		LavaDaoFilter filter = AuthUserRole.MANAGER.newFilterInstance();
		Date now = new Date();
		filter.setOuterAlias("user", "user");
		filter.setOuterAlias("group", "group");
		filter.setOuterAlias("group.users", "groupUsers");
		filter.setOuterAlias("groupUsers.user", "groupUser");
		filter.addDaoParam(AuthDaoUtils.getEffectiveDaoParam("group",filter));
		filter.addDaoParam(
				filter.daoOr(
					filter.daoEqualityParam("user.id", this.getId()),
					filter.daoEqualityParam("groupUser.id", this.getId())));
		return AuthUserRole.MANAGER.get(filter);
		}

	

	public boolean isAuthUserContextInit() {
		return authUserContextInit;
	}



	public void setAuthUserContextInit(boolean authUserContextInit) {
		this.authUserContextInit = authUserContextInit;
	}

	
	
	


	   /*
	    *  Acegi User Details Interface Implementation
	    *  
	    *   This allows the AuthUser model object to serve as 
	    *   a User token for Acegi Authentication.
	    *   
	    *
	    */
		
		
		
		public GrantedAuthority[] getAuthorities() {
			return null;
		}


		public String getPassword() {
			return getHashedPassword();
		}


		public String getUsername() {
			return getLogin();
		}






		public boolean isAccountNonExpired() {
			return (this.getExpirationDate()==null || 
					this.getExpirationDate().after(new Date())); 
		}
		





		/*
		 * Lava User Accounts do not have a locked property
		 * (non-Javadoc)
		 * @see org.acegisecurity.userdetails.UserDetails#isAccountNonLocked()
		 */
		public boolean isAccountNonLocked() {
			return true;
		}





		/*
		 * Lava User Accounts do not have a password expiration feature
		 */
		public boolean isCredentialsNonExpired() {
			return true;
		}


		public boolean isEnabled() {
			return !getDisabled();
		}





		



	
	
	
	static public class Manager extends EntityBase.Manager{
		
		public Manager(){
			super(AuthUser.class);
		}
		
		

		public AuthUser getByLogin(String username) {
			LavaDaoFilter filter = newFilterInstance();
			filter.addDaoParam(filter.daoEqualityParam("login", username));
			filter.addDaoParam(AuthDaoUtils.getEffectiveDaoParam(filter));
			filter.addDaoParam(filter.daoLessThanOrEqualParam("accessAgreementDate", new Date()));
			return (AuthUser)getOne(AuthUser.class,filter);
		}
		
	


		
		
	}

}
