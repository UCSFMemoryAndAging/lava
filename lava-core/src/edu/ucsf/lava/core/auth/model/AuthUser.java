package edu.ucsf.lava.core.auth.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;

import edu.ucsf.lava.core.auth.AuthDaoUtils;
import edu.ucsf.lava.core.auth.AuthUserPermissionCache;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;

public class AuthUser extends EntityBase implements UserDetails {
	
	public static final String AUTH_TYPE_LOCAL = "LOCAL";
	public static AuthUser.Manager MANAGER = new AuthUser.Manager();

	protected String userName;
	protected String login;
	protected String email;
	protected String phone;
	protected Date accessAgreementDate;
	protected Date effectiveDate;
	protected Date expirationDate;
	protected String shortUserName; 
	protected String shortUserNameRev; 
	protected String notes;
	protected String authenticationType;
	protected String password;
	protected Timestamp passwordExpiration;
	protected String passwordResetToken;
	protected Timestamp passwordResetExpiration;
	protected Short failedLoginCount;
	protected Timestamp lastFailedLogin;
	protected Timestamp accountLocked;
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






	public Timestamp getAccountLocked() {
		return accountLocked;
	}






	public void setAccountLocked(Timestamp accountLocked) {
		this.accountLocked = accountLocked;
	}






	public String getAuthenticationType() {
		return authenticationType;
	}






	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}






	public Date getEffectiveDate() {
		return effectiveDate;
	}






	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}






	public List<AuthUserRole> getEffectiveRoles() {
		if(effectiveRoles == null){
			effectiveRoles = this.getUserRoles();
		}
		return effectiveRoles;
	}






	public void setEffectiveRoles(List<AuthUserRole> effectiveRoles) {
		this.effectiveRoles = effectiveRoles;
	}






	public String getEmail() {
		return email;
	}






	public void setEmail(String email) {
		this.email = email;
	}






	public Date getExpirationDate() {
		return expirationDate;
	}






	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}






	public Short getFailedLoginCount() {
		return failedLoginCount;
	}






	public void setFailedLoginCount(Short failedLoginCount) {
		this.failedLoginCount = failedLoginCount;
	}






	public Timestamp getLastFailedLogin() {
		return lastFailedLogin;
	}






	public void setLastFailedLogin(Timestamp lastFailedLogin) {
		this.lastFailedLogin = lastFailedLogin;
	}






	public String getLogin() {
		return login;
	}






	public void setLogin(String login) {
		this.login = login;
	}






	public String getNotes() {
		return notes;
	}






	public void setNotes(String notes) {
		this.notes = notes;
	}






	public String getPassword() {
		return password;
	}




	public void setPassword(String password) {
		this.password = password;
	}




	public Timestamp getPasswordExpiration() {
		return passwordExpiration;
	}






	public void setPasswordExpiration(Timestamp passwordExpiration) {
		this.passwordExpiration = passwordExpiration;
	}






	public Timestamp getPasswordResetExpiration() {
		return passwordResetExpiration;
	}






	public void setPasswordResetExpiration(Timestamp passwordResetExpiration) {
		this.passwordResetExpiration = passwordResetExpiration;
	}






	public String getPasswordResetToken() {
		return passwordResetToken;
	}






	public void setPasswordResetToken(String passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}






	public String getPhone() {
		return phone;
	}






	public void setPhone(String phone) {
		this.phone = phone;
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
		// EMORY change: b/c an effective group parameter was given, any roles assigned directly
		//   to users (i.e. role not given through group) was filtered out.  That authuserrole entry
		//   had GID=NULL, so getEffectiveDaoParam("group",filter) evaluated to false.
		//   Get around this (without changing the getEffectiveDaoParam function for now) by adding
		//   a NULL check.
		//filter.addDaoParam(AuthDaoUtils.getEffectiveDaoParam("group",filter));
		filter.addDaoParam(
				filter.daoOr(
						filter.daoNull("group"),
						AuthDaoUtils.getEffectiveDaoParam("group",filter)));
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
		
		
		/**
		 * Only return granted authorities for accounts with locally defined
		 * authentication.  The granted authroities for external authenticated 
		 * accounts will be returned by the external providers. Return a single
		 * authority "ROLE_USER" to allow access to the application.  Internal 
		 * application roles and access permissions managed by the auth framework
		 * not acegi. 
		 */
		public GrantedAuthority[] getAuthorities() {
			String type = getAuthenticationType();
			if(type!=null && type.equals(AUTH_TYPE_LOCAL)){
				return new GrantedAuthority[]{new GrantedAuthorityImpl("ROLE_USER")};
			}else{
				return new GrantedAuthority[]{new GrantedAuthorityImpl("EXTERNAL_AUTHENTICATION_TYPE")};
			}
		}


	


		public String getUsername() {
			return getLogin();
		}






		public boolean isAccountNonExpired() {
			return (this.getExpirationDate()==null || 
					this.getExpirationDate().after(new Date())); 
		}
		





		/*
		 * If the account has local defined authentication, check to
		 * see if an account locked timestamp is set. Otherwise return false;
		 */
		public boolean isAccountNonLocked() {
			if(this.getAuthenticationType()!=null && this.getAuthenticationType().equals(AUTH_TYPE_LOCAL)){
				return (this.getAccountLocked()==null);
			}
			return true;
		}





		/*
		 * If the account has local defined authentication, check to
		 * see if an account has a password expiration set that has passed.
		 */
		public boolean isCredentialsNonExpired() {
			if(this.getAuthenticationType()!=null && 
					this.getAuthenticationType().equals(AUTH_TYPE_LOCAL) &&
					this.getPasswordExpiration()!=null &&
					this.getPasswordExpiration().before(new Date())){
				return false;
			}
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
