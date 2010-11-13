package edu.ucsf.lava.core.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.auth.model.AuthUser;

/**
 * Implements simple password complexity check with configurable options for 
 * 1) Must contain a minimum of x characters.
 * 
 * 2) Cannot contain the user's account name;
 *         
 * 3) Must contain characters from x of the following four categories:
 *         o English uppercase characters (A through Z);
 *         o English lowercase characters (a through z);
 *         o Base 10 digits (0 through 9);
 *         o Symbol characters (!, $, #, %).
 * 
 */
public class SimplePasswordValidator implements
		PasswordValidator {

	public static final Long DEFAULT_MIN_LENGTH = (long)6;
	public static final Long DEFAULT_MIN_CATEGORIES = (long)2;
	
	protected Long minLength = DEFAULT_MIN_LENGTH;
	protected Boolean disallowAccountName = true;
	protected Long minCharacterCategories = DEFAULT_MIN_CATEGORIES;
	
	
	

	public boolean isPasswordComplex(AuthUser user, String password, Map<String,ArrayList> errors) {
		if(user == null || password == null){return false;}
	
		ArrayList errorArgs = new ArrayList();
		String passwordLower = password.toLowerCase();
		
		//if length is less than 7 then false
		if(password.length()< this.getMinLength()){
			errorArgs.add(this.getMinLength());
			errors.put("simple.passwordValidator.passwordTooShortError",errorArgs);
			return false;
			}
	
		//if password contains login name then false
		if(disallowAccountName && passwordLower.contains(user.getLogin().toLowerCase())){
			errorArgs.add(user.getLogin());
			errors.put("simple.passwordValidator.passwordContainsLoginError",errorArgs);
			return false;
		}
		
		
		//now check categories
		int categories = 0;
		if(password.matches(".*[a-z].*")){categories++;}
		if(password.matches(".*[A-Z].*")){categories++;}
		if(password.matches(".*[0-9].*")){categories++;}
		if(password.matches(".*[~!@#$%^&*()+=].*")){categories++;}
		if(categories < this.getMinCharacterCategories()){
			errorArgs.add(getMinCharacterCategories());
			errors.put("simple.passwordValidator.passwordNotEnoughCategoriesError", errorArgs);
			return false;
		}
		
		
		return true;
	}

	public boolean isPasswordValid(String password, Map<String,ArrayList> errors ) {
		//do not allow empty passwords
		if(StringUtils.isEmpty(password)){
			errors.put("ucsf.passwordValidator.passwordCannotBeBlankError", null);
			return false;
			}
		
		//no nonalphanumeric or allowed symbols
		if(password.matches(".*[^a-zA-Z0-9~!@#$%^&*()+=].*")){
			errors.put("ucsf.passwordValidator.passwordContainsInvalidCharactersError", null);
			return false;
		}
		return true;
		
	}

	public Boolean getDisallowAccountName() {
		return disallowAccountName;
	}

	public void setDisallowAccountName(Boolean disallowAccountName) {
		this.disallowAccountName = disallowAccountName;
	}

	public Long getMinCharacterCategories() {
		return minCharacterCategories;
	}

	public void setMinCharacterCategories(Long minCharacterCategories) {
		this.minCharacterCategories = minCharacterCategories;
	}

	public Long getMinLength() {
		return minLength;
	}

	public void setMinLength(Long minLength) {
		this.minLength = minLength;
	}

	
	

}
