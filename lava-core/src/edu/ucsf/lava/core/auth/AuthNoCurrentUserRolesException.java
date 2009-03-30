package edu.ucsf.lava.core.auth;

public class AuthNoCurrentUserRolesException extends AuthException {

	public AuthNoCurrentUserRolesException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public AuthNoCurrentUserRolesException(String message) {
		super(message);
	}

}
