package edu.ucsf.lava.core.auth;

import java.util.ArrayList;
import java.util.Map;

import edu.ucsf.lava.core.auth.model.AuthUser;

public interface PasswordValidator {

	/**
	 * Does the password meet the complexity rules defined for the system. 
	 * @param user
	 * @param password
	 * @return
	 */
	public boolean isPasswordComplex(AuthUser user, String password, Map<String,ArrayList> errors);
	
	/**
	 * Is the password a valid string (e.g. is not zero length and does not contain invalid characters)
	 * @return
	 */
	public boolean isPasswordValid(String password,  Map<String,ArrayList> errors);
}
