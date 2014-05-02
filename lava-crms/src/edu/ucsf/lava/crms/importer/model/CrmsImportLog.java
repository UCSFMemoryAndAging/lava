package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportLog;

public class CrmsImportLog extends ImportLog {
	private String projName;

	private Short totalPatientRecords;
	private Short numExistingPatients;
	private Short newPatientsCreated;
	
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

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}
	
	
	
	
	
}
