package edu.ucsf.lava.core.auth;

import java.util.ArrayList;
import java.util.Map;

import org.acegisecurity.providers.dao.SaltSource;
import org.acegisecurity.providers.encoding.PasswordEncoder;

import edu.ucsf.lava.core.auth.model.AuthUser;

/**
 * Encapsulates the functionality for password management.
 * @author jhesse
 *
 */
public interface PasswordDelegate {

	/**
	 * returns the auth user authentication type handled by this delegate
	 * @return
	 */
	public String getAuthenticationType();
	
	/**
	 * returns true if the delegate can change passwords
	 * @return
	 */
	public boolean getSupportsPasswordChange();
	/**
	 * returns true if the delegate can reset passwords
	 * @return
	 */
	public boolean getSupportsPasswordReset();
	
	/**
	 * returns a message code that describes the password change procedures for the authentication type. 
	 * @return
	 */
	public String getPasswordChangeMessageCode();
	
	
	/**
	 * returns a comma separated string of arguments for the password change message. 
	 * @return
	 */
	public String getPasswordChangeMessageArgs();
	
	/**
	 * returns a message code that describes the password reset procedure for the authentication type. 
	 * @return
	 */
	public String getPasswordResetMessageCode();
	
	/**
	 * returns a comma separated string of arguments for the password reset message. 
	 *  @return
	 */
	public String getPasswordResetMessageArgs();
	
	
	
	/**
	 * returns the order specified for the delegate.  This allows delegates to be
	 * overrided by "lower" ordered delegates registered 
	 * @return
	 */
	public Long getOrder();
	
	/**
	 * Change the password of the user.   
	 * @param user the user
	 * @param oldPassword the old password (plain text)
	 * @param newPassword the new password (plain text)
	 * @param newPasswordConfirm the new password confirmation value (plain text)
	 * @param errors a map with error codes on failure
	 * @return
	 */
	public boolean changePassword(AuthUser user, String oldPassword, String newPassword, String newPasswordConfirm, Map <String, ArrayList> errors);

	/**
	 * Reset the password of the user.   
	 * @param user the user
	 * @param newPassword the new password (plain text)
	 * @param newPasswordConfirm the new password confirmation value (plain text)
	 * @param errors a map with error codes on failure
	 * @return
	 */
	public boolean resetPassword(AuthUser user, String newPassword, String newPasswordConfirm, Map <String, ArrayList> errors);
	
	
	/**
	 * Initialize the authentication details of the authUser.  Called when a new user is created or 
	 * when the authentication type of a user is changed. 
	 * @param authUser
	 */
	public void initializeAuthenticationDetails(AuthUser authUser);
	
	
	public PasswordEncoder getPasswordEncoder();
	public void setPasswordEncoder(PasswordEncoder passwordEncoder);
	
	public SaltSource getPasswordSaltSource();	
	public void setPasswordSaltSource(SaltSource passwordSaltSource);
		
	public PasswordValidator getPasswordValidator();
	public void setPasswordValidator(PasswordValidator passwordValidator);



	
}
