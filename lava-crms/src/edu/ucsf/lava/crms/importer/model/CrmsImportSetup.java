package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportSetup;

public class CrmsImportSetup extends ImportSetup {
	
	// projName is required for creating Patients (initial EnrollmentStatus), creating Visits and
	// creating Instruments
	
	// decided to put projName in setup rather than in template because a given template could be
	// used with differnt projects over time

	// this is not a required field as the projName could be supplied in the imported data file itself
	private String projName;

	public CrmsImportSetup() {
		super();
	}

}
