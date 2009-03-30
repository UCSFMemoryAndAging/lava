package edu.ucsf.lava.core.auth;

public class AuthException extends RuntimeException {

	public AuthException(String message){
	    super(message);
	  }

	  public AuthException(String message, Throwable throwable){
	    super(message, throwable);
	  }

	

}
