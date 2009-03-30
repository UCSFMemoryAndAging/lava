package edu.ucsf.lava.crms.assessment.controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import edu.ucsf.lava.crms.assessment.model.Instrument;


// this class contains validation common across all instruments. it is intended to
// be subclassed by instrument specific Validators containing instrument specific
// validation, and delegating common validation to this Validator class

// note that the validate method required by the Validator interface is not
// used, because this Validator is used in conjunction with 
// AbstractWizardFormController style controllers, which do not call validate
// (rather, they call the validatePage method of the controller).
// however, we have a separate validation class which services the validatePage
// method, and furthermore, we have the class still implement the Validator 
// interface so that it can be used in the normal context of a Validator, i.e. 
// it can be configured on the controller, and a controller can retrieve it via
// getValidator

public class InstrumentValidator implements Validator {
	
	// this is required to implement Validator, but this should never be called, as the
	//  supports method of subclasses should be called. the InstrumentValidator is never
	//  configured as a validator for any instrument Controller. rather, this is just a
	//  convenience class.
	public boolean supports(Class supportedClass) {
		return supportedClass.equals(Instrument.class);
	}
	

	// this method is required by the Validator interface, but since validating
	// for AbstractWizardFormController controllers, validate is never called
	// (instead, validatePage is called), so do nothing
	public void validate(Object command, Errors errors) {
		
	}
	
	// this returns a boolean because it is part of wholesale validation which has
	//  stores validation flags for each section
	public boolean validateGeneral(Object command, Errors errors) {
		boolean validationFailure = false;

		//ValidationUtils.rejectIfEmpty(errors, "careId", "required.field");
		//if (errors.getFieldError("careId") != null) {validationFailure = true;}
		
		return validationFailure;
	}

	
	// common validation among all instruments is done here, which equates to the
	// "second stage" instrument validation done for the "status" page, the page containing
	// the data collected and data entered status for the instrument
	public void validateStatus(Object command, Errors errors) {
		// if key does not exist in message resource, the default message as last parameter is used 
		//ValidationUtils.rejectIfEmpty(errors, "patient", "required.instrument.patient", "Patient is required");
		//ValidationUtils.rejectIfEmpty(errors, "projName", "required.instrument.projName", "Project Name is required");
		//ValidationUtils.rejectIfEmpty(errors, "visit", "required.instrument.visit", "Visit is required");
		//ValidationUtils.rejectIfEmpty(errors, "instrType", "required.instrument.instrType", "Instrument Type is required");
		ValidationUtils.rejectIfEmpty(errors, "dcDate", "required.field");
		ValidationUtils.rejectIfEmpty(errors, "dcBy", "required.field");
		ValidationUtils.rejectIfEmpty(errors, "dcStatus", "required.field");
		ValidationUtils.rejectIfEmpty(errors, "deDate", "required.field");
		ValidationUtils.rejectIfEmpty(errors, "deBy", "required.field");
		ValidationUtils.rejectIfEmpty(errors, "deStatus", "required.field");
		if (errors.hasErrors()) {
			errors.reject("validationFailure.page");
		}

	}

}
