package edu.ucsf.lava.crms.model;


import java.util.Calendar;
import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.crms.people.model.Patient;

public class CrmsEntity extends EntityBase {
	// Skip Error Codes
	public static final Short DATA_CODES_PATIENT_FACTOR = (short) -1;
	public static final Short DATA_CODES_SITUATIONAL_FACTOR = (short) -2;
	public static final Short DATA_CODES_ALTERNATE_TEST_GIVEN = (short) -3;
	public static final Short DATA_CODES_REFUSED = (short) -4;
	public static final Float DATA_CODES_PATIENT_FACTOR_FLOAT = new Float(-1.0);
	public static final Float DATA_CODES_SITUATIONAL_FACTOR_FLOAT = new Float(-2.0);
	public static final Float DATA_CODES_ALTERNATE_TEST_GIVEN_FLOAT = new Float(-3.0);
	public static final Float DATA_CODES_REFUSED_FLOAT = (float) -4.0;
	
	// Standard Error Codes
	public static final Short DATA_CODES_LOGICAL_SKIP = (short) -6;
	public static final Short DATA_CODES_INCOMPLETE = (short) -7;
	public static final Short DATA_CODES_UNUSED_VARIABLE= (short) -8;
	public static final Short DATA_CODES_MISSING_DATA = (short) -9;
	public static final Float DATA_CODES_LOGICAL_SKIP_FLOAT = (float) -6.0;
	public static final Float DATA_CODES_INCOMPLETE_FLOAT = (float) -7.0;
	public static final Float DATA_CODES_UNUSED_VARIABLE_FLOAT = (float) -8.0;
	public static final Float DATA_CODES_MISSING_DATA_FLOAT = (float) -9.0;
	
	// Total (Calculation) Error Codes
	public static final Short TOTAL_ERROR_CODE = (short) -5;
	public static final Float TOTAL_ERROR_CODE_FLOAT = -5.0f;	
	
	protected boolean patientAuth;
	protected boolean projectAuth;
	
	
	public CrmsEntity(){
		super();
		this.setPatientAuth(true);
		this.setProjectAuth(false);
	}
	
	/**
	 * If a CRMS entity is subject to Patient authorization filtering, this method must be implemented 
	 * and return the associated Patient entity. 
	 * @return
	 */
	public Patient getPatient() {
		return null;
	}

	/**
	 * If a CRMS entity is subject to Project authorization filtering, this method must be implemented 
	 * and return the associated Project name (projUnitDesc) entity. 
	 * @return
	 */
	public String getProjName() {
		return null;
	}

	/**
	 * is this entity subject to Patient Authorization filtering (associated with a patient)
	 * @return
	 */
	public boolean getPatientAuth() {
		return patientAuth;
	}

	/**
	 * Sets the patient Authorization status
	 * @param patientAuth
	 */
	protected void setPatientAuth(boolean patientAuth) {
		this.patientAuth = patientAuth;
	}

	/**
	 * is this entity subject to Project Authorization filtering (associated with a project)
	 * @return
	 */
	
	public boolean getProjectAuth() {
		return projectAuth;
	}
	
	/**
	 * Sets the project authorization status. 
	 * @param projectAuth
	 */
	protected void setProjectAuth(boolean projectAuth) {
		this.projectAuth = projectAuth;
	}

	
	
	/**
	 * Utility method to update calculated age fields
	 */
	public static Integer calcAge(Date birthDate, Date endDate){
		Integer age = null;
		if(birthDate!=null && endDate !=null){
			Calendar birth = Calendar.getInstance();
			birth.setTime(birthDate);
			Calendar end = Calendar.getInstance();
			end.setTime(endDate);
			age = end.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
			if((age < 0)){
				age = null;
			}else if(age==0){
				if(end.before(birth)){
					age = null;
				}else{
					age = 0;
				}
			}else{
				//add the age to the birth date and check to see if the birthday happened in the year of "endDate"					
				birth.add(Calendar.YEAR, age);
				if(end.before(birth)){
					age--;
				}
			}
		}
		return age;
	}
	
	/**
	 * Uses the current date as the end date in calculating age
	 */
	protected Integer calcAge(Date birthDate){
		return calcAge(birthDate,new Date());
	}
}
