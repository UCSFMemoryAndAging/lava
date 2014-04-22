package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportConfig;

public class CrmsImportLog extends ImportConfig {
	
	public CrmsImportLog(){
		super();
	}
	
	Long dupPatients; // existing Patients
	Long newPatients;
	Long dupCaregivers;
	Long newCaregivers;
	Long dupContactInfo;
	Long newContactInfo;
	Long dupEnrollments;
	Long newEnrollments;
	Long dupVisits;
	Long newVisits;
	Long dupInstruments;
	Long newInstruments;
	
	
	
}
