package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportTemplate;

public class CrmsImportTemplate extends ImportTemplate {
	//TODO: decide if need separate CrmsInsertImportTemplate and CrmsUpdateImportTemplate, e.g.
	//with Insert Patient Exists could be true or false depending upon whether inserting Patient records,
	//whereas with Update Patient Exists should be a constant = true
	//really depends on how separate Insert and Update will be so will not really know until Update is
	//implemented (jhesse re: security of exporting PHI CSV files for update --- is REDCap update done
	//within the context of REDCap?? pretty sure Songster does updates in Excel) at which time can
	//refactor names
	
	//more:
	// CrmsInsertImportTemplate
	
	// if Patients template will need to indicate which field or fields to use to look for already existing Patient
	//   also for Patient need initial Project for EnrollmentStatus
	// radio buttons / dropdown
	//   if Patient does not exist, add Patient (for new patients, e.g. new patient history form)
	//   if Patient does not exist, skip import for this record (for assessment imports)
	// also  
	//   if Patient exists, log warning (new patients but patient might exist for some reason. Patient is
	//      obviously not added since they exist, but other records in row are imported)
	// UPDATE: import code will hard-code which fields will be used to match Patient, e.g.
	// first look for PIDN in the template def, if no PIDN use first name,last name and DOB if present
	
	// if Visits template will need to know name of date field to use for VisitDate and Project / Visit Type / Visit With
	//   and VisiDate and optionally Visit Type to looking for already existing Visit
	// need to check if Patient is Enrolled in the Visit's Project
	// UPDATE: see above update, i.e. template mapping file will identify visitDate, visitType, visitWith properties
	
	// if Assessment need instrument name and dcDate and check for already existing instrument (and what if instrument
	// exists but has no data entered --- that would be considered a 1.0 insert rather than a 2.0 update)
	// UPDATE: see above update, i.e. template mapping file will identify instrType, dcDate
	
	// CrmsUpdateImportTemplate
	// not sure if there will be a template mapping file, in which case maybe template file should not exist at core
	// level and put into CrmsInsertImportTemplate, but feeling is that it should be left in core and just
	// not used here (for this envisioning an export which uses reflection to essentially export the template,
	// but with data, and then import is using that same export file so there is no template mapping file
	// needed, i.e. the data file is also the template mapping file
	
	//UPDATE: can also consider CoreInsertImportTemplate with template mapping file and CoreUpdateImportTemplate
	//without
	

	
	// createPatient (if not exists)
	// createCaregiver (if not exists)
	// createContactInfo (if not exists)
	// project
	// enrollPatient (if not enrolled)
	// createVisit (if not exists)
	// createInstrument (if not exists)
	
	private Boolean patientMustExist;

	// this is not a persistent property. it is available for user to specify a project if they need to
	// choose other property values that are dependent on projName (visitType, visitWith, visitLocation)
	private String projNameForContext;

	private Boolean visitMustExist;
	// if import will create new Visits, account for all Visit required fields
	// visitDate (and optionally visitTime) must be present in the data file (mapped to either visitDate or
	// dcDate). the rest of the required fields may not be in the data file so need to be supplied as part of
	// the template
// INSERT specific fields	
	private Boolean visitTypeSupplied;
	private String visitType;
	private Boolean visitWithSupplied;
	private String visitWith;
	private Boolean visitLocSupplied;
	private String visitLoc;
	private Boolean visitStatusSupplied;
	private String visitStatus;
	
	public CrmsImportTemplate(){
		super();
	}

	public Boolean getPatientMustExist() {
		return patientMustExist;
	}

	public void setPatientMustExist(Boolean patientMustExist) {
		this.patientMustExist = patientMustExist;
	}

	public String getProjNameForContext() {
		return projNameForContext;
	}

	public void setProjNameForContext(String projNameForContext) {
		this.projNameForContext = projNameForContext;
	}

	public Boolean getVisitMustExist() {
		return visitMustExist;
	}

	public void setVisitMustExist(Boolean visitMustExist) {
		this.visitMustExist = visitMustExist;
	}

	public Boolean getVisitTypeSupplied() {
		return visitTypeSupplied;
	}

	public void setVisitTypeSupplied(Boolean visitTypeSupplied) {
		this.visitTypeSupplied = visitTypeSupplied;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Boolean getVisitWithSupplied() {
		return visitWithSupplied;
	}

	public void setVisitWithSupplied(Boolean visitWithSupplied) {
		this.visitWithSupplied = visitWithSupplied;
	}

	public String getVisitWith() {
		return visitWith;
	}

	public void setVisitWith(String visitWith) {
		this.visitWith = visitWith;
	}

	public Boolean getVisitLocSupplied() {
		return visitLocSupplied;
	}

	public void setVisitLocSupplied(Boolean visitLocSupplied) {
		this.visitLocSupplied = visitLocSupplied;
	}

	public String getVisitLoc() {
		return visitLoc;
	}

	public void setVisitLoc(String visitLoc) {
		this.visitLoc = visitLoc;
	}

	public Boolean getVisitStatusSupplied() {
		return visitStatusSupplied;
	}

	public void setVisitStatusSupplied(Boolean visitStatusSupplied) {
		this.visitStatusSupplied = visitStatusSupplied;
	}

	public String getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}

}
