package edu.ucsf.lava.crms.assessment.model.cbt;

import java.util.Date;

import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;

public abstract class BaseCbt extends Instrument {

	public BaseCbt() {}
	
	/* constructor for adding new instruments. do instrument-specific initialization here. */
	public BaseCbt(Patient p, Visit v, String projName, String instrType, Date dcDate, String dcStatus) {
		super(p,v,projName,instrType,dcDate,dcStatus);
	}

	private String filename;
	// versioning properties to track versions of a given computer-based task over time. some or all of
	// these properties should be written to the task data output so they can be recorded. they can be used
	// for altering uploading and processing logic for a given task when changes have been made to the task
	private String task;
	private String version;
	private String versionDate;
	private String language;
	private String form;
	private String adultChild;
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTask() {
		return task;
	}

	public void setTask(String task) {
		this.task = task;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVersionDate() {
		return versionDate;
	}

	public void setVersionDate(String versionDate) {
		this.versionDate = versionDate;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getAdultChild() {
		return adultChild;
	}

	public void setAdultChild(String adultChild) {
		this.adultChild = adultChild;
	}

	public String[] getRequiredResultFields() {
		return new String[] {
		};
	}
	
	/**
	 * Clear out the summary stats. Note that the summary stats are computed
	 * in the calculate method which is defined in the superclass. 
	 */
	public abstract void clearSummaryStats();
	
}


