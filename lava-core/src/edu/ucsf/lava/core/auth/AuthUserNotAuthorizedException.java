package edu.ucsf.lava.core.auth;

public class AuthUserNotAuthorizedException extends AuthException {

	public AuthUserNotAuthorizedException(String message, Throwable throwable) {
		super(message, throwable);
	
	}

	public AuthUserNotAuthorizedException(String message) {
		super(message);
	}

}
