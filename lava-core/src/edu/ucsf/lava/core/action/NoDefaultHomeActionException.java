package edu.ucsf.lava.core.action;

public class NoDefaultHomeActionException extends RuntimeException {

	public NoDefaultHomeActionException() {
		super("There is no action defined as the default home action, aborting action configuration");
		
	}

	
}
