package edu.ucsf.lava.crms.dao;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.model.Project;


/**
 * Utility class that encapsulates setting the CRMS runtime contexts of
 * Patient or Project context into the LavaDaoFilter.  This filters the 
 * data returned by a data access query to what is associated with a specific 
 * patient and/or project. 
 * 
 * @author jhesse
 *
 */
public class CrmsDaoFilterUtils  {

	public static final String PATIENT_AUTH_FILTER="patientAuth";
	public static final String PATIENT_AUTH_FILTER_PARAM="projectList";
	public static final String PROJECT_AUTH_FILTER="projectAuth";
	public static final String PROJECT_AUTH_FILTER_PARAM="projectList";
	
	public static final String VALID_PATIENT_FILTER="validPatient";
	public static final String PATIENT_CONTEXT_FILTER="patient";
	public static final String PATIENT_CONTEXT_FILTER_PARAM="patientId";
	public static final String PROJECT_CONTEXT_FILTER="projectContext";
	public static final String PROJECT_CONTEXT_FILTER_PARAM="projectContext";

	
	/**
	 * Sets the patient context filter in the provided filter with the provided patient. 
	 * @param filter
	 * @param patient
	 * @return 
	 */
	public static LavaDaoFilter setPatientContext(LavaDaoFilter filter, Patient patient){
		if(filter==null || patient==null || patient.getId()==null){return filter;}
		return filter.setContextFilter(PATIENT_CONTEXT_FILTER,PATIENT_CONTEXT_FILTER_PARAM,patient.getId());
	}
	
	/**
	 * Clears the patient context filter
	 * @param filter
	 * @return
	 */
	public static LavaDaoFilter clearPatientContext(LavaDaoFilter filter){
		return filter.clearContextFilter(PATIENT_CONTEXT_FILTER);
	}
	
	/**
	 * Sets the project context filter in the provided filter with the provided project. 
	 * @param filter
	 * @param project
	 * @return 
	 */
	
	public static LavaDaoFilter setProjectContext(LavaDaoFilter filter, Project project){
		if(filter==null || project==null || project.getProjUnitDesc()==null){return filter;}
		return filter.setContextFilter(PROJECT_CONTEXT_FILTER,PROJECT_CONTEXT_FILTER_PARAM,project.getProjUnitDesc());
		
	}
	
	/**
	 * Clears the project context filter
	 * @param filter
	 * @return
	 */
	public static LavaDaoFilter clearProjectContext(LavaDaoFilter filter){
		return filter.clearContextFilter(PROJECT_CONTEXT_FILTER);
	}
	
}
