package edu.ucsf.lava.crms.auth.model;

import edu.ucsf.lava.crms.people.model.Patient;

public interface CrmsAuthEntity {

	/**
	 * If a CRMS entity is subject to Patient authorization filtering, this method must be implemented 
	 * and return the associated Patient entity. 
	 * @return
	 */
	public Patient getPatient();
	/**
	 * If a CRMS entity is subject to Project authorization filtering, this method must be implemented 
	 * and return the associated Project name (projUnitDesc) entity. 
	 * @return
	 */
	public String getProjName();

	/**
	 * is this entity subject to Patient Authorization filtering (associated with a patient)
	 * @return
	 */
	public boolean getPatientAuth();

	/**
	 * is this entity subject to Project Authorization filtering (associated with a project)
	 * @return
	 */
	
	public boolean getProjectAuth();
		
}
