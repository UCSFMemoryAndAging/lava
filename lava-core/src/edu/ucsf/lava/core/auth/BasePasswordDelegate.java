package edu.ucsf.lava.core.auth;

import java.util.ArrayList;
import java.util.Map;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.springframework.webflow.execution.Event;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;

public class BasePasswordDelegate implements PasswordDelegate {
	
	public static final Long DEFAULT_ORDER = (long)100; 
	
	protected SaltSource passwordSaltSource;
	protected PasswordEncoder passwordEncoder;
	protected PasswordValidator passwordValidator;
	protected String authenticationType;
	protected String passwordChangeMessageCode;
	protected String passwordResetMessageCode;
	protected String passwordChangeMessageArgs;
	protected String passwordResetMessageArgs;
	
	protected boolean supportsPasswordChange;
	protected boolean supportsPasswordReset;
	protected Long order;
	
	
	
	public BasePasswordDelegate(){
		supportsPasswordChange = supportsPasswordReset = true;
		order = DEFAULT_ORDER;
	}
	
	
	
	public boolean changePassword(AuthUser user, String oldPassword, String newPassword, String newPasswordConfirm, Map<String, ArrayList> errors) {
		//make sure this delegate supports password change
		if(!getSupportsPasswordChange()){
			errors.put("passwordDelegate.changePassword.notSupportedError",null);
			return false;
		}
		
		//make sure there are no other errors related to the delegate configuration...
		if(!isConfigured() || user == null || !getAuthenticationType().equals(user.getAuthenticationType())){
			errors.put("passwordDelegate.changePassword.unexpectedError", null);
			return false;
		}

		//make sure all the password change fields were completed
		if(oldPassword==null || newPassword==null || newPasswordConfirm==null){
			errors.put("passwordDelegate.changePassword.incompleteSubmissionError",null);
			return false;
		}
	
		//now confirm the old password matches the current password for the user
		if(!passwordEncoder.isPasswordValid(user.getPassword(), oldPassword, getPasswordSaltSource().getSalt(user))){
			errors.put("passwordDelegate.changePassword.oldPasswordIncorrectError",null);
			return false;
		}
			
		//make sure new password and new password confirm match
		if(!newPassword.equals(newPasswordConfirm)){
			errors.put("passwordDelegate.changePassword.confirmationDoesNotMatchError",null);
			return false;
		}
		
		//make sure the password supplied is different from the old password
		if(newPassword.equals(oldPassword)){
			errors.put("passwordDelegate.changePassword.newPasswordMatchesOldError",null);
			return false;
		}
		
		//make sure the new password matches the password validity requirements
		if(!getPasswordValidator().isPasswordValid(newPassword, errors)){
			return false;
		}
		
		//make sure the new password matches the password complexity requirements
		if(!getPasswordValidator().isPasswordComplex(user, newPassword, errors)){
			return false;
		}
			
		//if we get here then everything is ok, change the password and persist the user object 
		user.setPassword(getPasswordEncoder().encodePassword(newPassword,getPasswordSaltSource().getSalt(user)));
		user.save();
		return true;
	}
	

	



	public boolean resetPassword(AuthUser user, String newPassword, String newPasswordConfirm, Map<String, ArrayList> errors) {
		//make sure this delegate supports password reset
		if(!getSupportsPasswordReset()){
			errors.put("passwordDelegate.resetPassword.notSupportedError",null);
			return false;
		}
		
		//make sure there are no other errors related to the delegate configuration...
		if(!isConfigured() || user == null || !getAuthenticationType().equals(user.getAuthenticationType())){
			errors.put("passwordDelegate.resetPassword.unexpectedError", null);
			return false;
		}

		//make sure all the password change fields were completed
		if(newPassword==null || newPasswordConfirm==null){
			errors.put("passwordDelegate.resetPassword.incompleteSubmissionError",null);
			return false;
		}
	
				
		//make sure new password and new password confirm match
		if(!newPassword.equals(newPasswordConfirm)){
			errors.put("passwordDelegate.resetPassword.confirmationDoesNotMatchError",null);
			return false;
		}
		
			
		//make sure the new password matches the password validity requirements
		if(!getPasswordValidator().isPasswordValid(newPassword, errors)){
			return false;
		}
		
		//make sure the new password matches the password complexity requirements
		if(!getPasswordValidator().isPasswordComplex(user, newPassword, errors)){
			return false;
		}
			
		//if we get here then everything is ok, change the password and persist the user object 
		user.setPassword(getPasswordEncoder().encodePassword(newPassword,getPasswordSaltSource().getSalt(user)));
		user.save();
		return true;
	}

	
	protected boolean isConfigured(){
		return (getPasswordSaltSource()!=null && 
				getPasswordEncoder()!=null && 
				getPasswordValidator()!=null &&
				getAuthenticationType()!=null);
		}
	
	/**
	 * Default implementation clears all authentication details fields
	 */
	public void initializeAuthenticationDetails(AuthUser authUser) {
		authUser.setPassword(null);
		authUser.setPasswordExpiration(null);
		authUser.setPasswordResetToken(null);
		authUser.setPasswordResetExpiration(null);
		authUser.setFailedLoginCount(null);
		authUser.setLastFailedLogin(null);
		authUser.setAccountLocked(null);
	}



	public String getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(String authenticationType) {
		this.authenticationType = authenticationType;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public PasswordValidator getPasswordValidator() {
		return passwordValidator;
	}

	public void setPasswordValidator(PasswordValidator passwordValidator) {
		this.passwordValidator = passwordValidator;
	}

	public SaltSource getPasswordSaltSource() {
		return passwordSaltSource;
	}

	public void setPasswordSaltSource(SaltSource passwordSaltSource) {
		this.passwordSaltSource = passwordSaltSource;
	}


	public String getPasswordChangeMessageCode() {
		return passwordChangeMessageCode;
	}


	public void setPasswordChangeMessageCode(String passwordChangeMessageCode) {
		this.passwordChangeMessageCode = passwordChangeMessageCode;
	}


	public String getPasswordResetMessageCode() {
		return passwordResetMessageCode;
	}


	public void setPasswordResetMessageCode(String passwordResetMessageCode) {
		this.passwordResetMessageCode = passwordResetMessageCode;
	}


	public boolean getSupportsPasswordChange() {
		return supportsPasswordChange;
	}


	public void setSupportsPasswordChange(boolean supportsPasswordChange) {
		this.supportsPasswordChange = supportsPasswordChange;
	}


	public boolean getSupportsPasswordReset() {
		return supportsPasswordReset;
	}


	public void setSupportsPasswordReset(boolean supportsPasswordReset) {
		this.supportsPasswordReset = supportsPasswordReset;
	}



	public Long getOrder() {
		return order;
	}



	public void setOrder(Long order) {
		this.order = order;
	}

	public void setPasswordDelegates(PasswordDelegates passwordDelegates){
		if(passwordDelegates!=null){
			passwordDelegates.addDelegate(this);
		}
	}



	public String getPasswordChangeMessageArgs() {
		return passwordChangeMessageArgs;
	}



	public void setPasswordChangeMessageArgs(String passwordChangeMessageArgs) {
		this.passwordChangeMessageArgs = passwordChangeMessageArgs;
	}



	public String getPasswordResetMessageArgs() {
		return passwordResetMessageArgs;
	}



	public void setPasswordResetMessageArgs(String passwordResetMessageArgs) {
		this.passwordResetMessageArgs = passwordResetMessageArgs;
	}
	


	
}
