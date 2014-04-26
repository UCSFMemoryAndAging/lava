package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportLog;

public class CrmsImportLog extends ImportLog {
	private String projName;

	private Long dupPatients; // existing Patients
	private Long newPatients;
	private Long dupCaregivers;
	private Long newCaregivers;
	private Long dupContactInfo;
	private Long newContactInfo;
	private Long dupEnrollments;
	private Long newEnrollments;
	private Long dupVisits;
	private Long newVisits;
	private Long dupInstruments;
	private Long newInstruments;
	
	//TODO logMsgs collection object
	
	public CrmsImportLog(){
		super();
	}
	
	
	
	
	
}
