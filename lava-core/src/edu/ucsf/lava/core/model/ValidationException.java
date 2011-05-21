package edu.ucsf.lava.core.model;

/**
 * 
 * @author ctoohey
 *
 * This is a marker class to be used for throwing exceptions from the LavaEntity 
 * validate method. This will allow the infrastructure to distinguish a validation
 * failure from other exceptions that may occur. 
 * 
 */
public class ValidationException extends RuntimeException {

	public ValidationException(String detailMsg) {
		super(detailMsg);
	}
	
}
