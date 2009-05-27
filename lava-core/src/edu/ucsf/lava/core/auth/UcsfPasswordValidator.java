package edu.ucsf.lava.core.auth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.auth.model.AuthUser;

/**
 * Implements UCSF standard password complexity checks
 * 
 * 1) Must contain a minimum of 7 characters.
 * 2) Cannot contain:
 *         o The user's account name;
 *         o More than two consecutive characters of the user's full name.
 *
 * 3) Must contain characters from three of the following four categories:
 *         o English uppercase characters (A through Z);
 *         o English lowercase characters (a through z);
 *         o Base 10 digits (0 through 9);
 *         o Symbol characters (!, $, #, %).
 * 
 */
public class UcsfPasswordValidator implements
		PasswordValidator {

	public boolean isPasswordComplex(AuthUser user, String password, Map<String,ArrayList> errors) {
		if(user == null || password == null){return false;}
	
		ArrayList errorArgs = new ArrayList();
		String passwordLower = password.toLowerCase();
		
		//if length is less than 7 then false
		if(password.length()<7){
			errors.put("ucsf.passwordValidator.passwordTooShortError", null);
			return false;
			}
	
		//if password contains login name then false
		if(passwordLower.contains(user.getLogin().toLowerCase())){
			errorArgs.add(user.getLogin());
			errors.put("ucsf.passwordValidator.passwordContainsLoginError",errorArgs);
			return false;
		}
		
		//if password contains three consecutive letters of the users name then false
		//first strip any non alphanumeric from the username
		String cleanUserName = user.getUserName().replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
		//now loop through cleanUserName in groups of 3 characters seeing if we get a match on the password
		for (int start=0, end=3; end < cleanUserName.length() ; start++,end++){
			String check = cleanUserName.substring(start, end);
			if(passwordLower.contains(cleanUserName.substring(start,end))){
				errorArgs.add(user.getUserName());
				errors.put("ucsf.passwordValidator.passwordContainsPartOfUserNameError", errorArgs);
				return false;
			}
		}
		
		//now check categories
		int categories = 0;
		if(password.matches(".*[a-z].*")){categories++;}
		if(password.matches(".*[A-Z].*")){categories++;}
		if(password.matches(".*[0-9].*")){categories++;}
		if(password.matches(".*[~!@#$%^&*()+=].*")){categories++;}
		if(categories < 3){
			errors.put("ucsf.passwordValidator.passwordNotEnoughCategoriesError", null);
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
	
	

}
