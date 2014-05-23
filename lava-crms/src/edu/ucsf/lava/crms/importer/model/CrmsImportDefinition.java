package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportDefinition;

public class CrmsImportDefinition extends ImportDefinition {
// for visit and time format fields (birthDateFormat, visitDateFormat, visitTimeFormat) have dropdown list with
// common formats but make it a suggest field. Info text regarding default values
// same with dcStatus but not sure about the suggest bc do not have suggest on regular dcStatus ??
	
	//TODO: decide if need separate CrmsInsertImportDefinition and CrmsUpdateImportDefinition, e.g.
	//with Insert Patient Exists could be true or false depending upon whether inserting Patient records,
	//whereas with Update Patient Exists should be a constant = true
	//really depends on how separate Insert and Update will be so will not really know until Update is
	//implemented (jhesse re: security of exporting PHI CSV files for update --- is REDCap update done
	//within the context of REDCap?? pretty sure Songster does updates in Excel) at which time can
	//refactor names
	
	//more:
	// CrmsInsertImportDefinition
	
	// if Patients def will need to indicate which field or fields to use to look for already existing Patient
	//   also for Patient need initial Project for EnrollmentStatus
	// radio buttons / dropdown
	//   if Patient does not exist, add Patient (for new patients, e.g. new patient history form)
	//   if Patient does not exist, skip import for this record (for assessment imports)
	// also  
	//   if Patient exists, log warning (new patients but patient might exist for some reason. Patient is
	//      obviously not added since they exist, but other records in row are imported)
	// UPDATE: import code will hard-code which fields will be used to match Patient, e.g.
	// first look for PIDN in the definition mapping, if no PIDN use first name,last name and DOB if present
	
	// if Visits, def will need to know name of date field to use for VisitDate and Project / Visit Type / Visit With
	//   and VisiDate and optionally Visit Type to looking for already existing Visit
	// need to check if Patient is Enrolled in the Visit's Project
	// UPDATE: see above update, i.e. def mapping file will identify visitDate, visitType, visitWith properties
	
	// if Assessment need instrument name and dcDate and check for already existing instrument (and what if instrument
	// exists but has no data entered --- that would be considered a 1.0 insert rather than a 2.0 update)
	// UPDATE: see above update, i.e. def mapping file will identify instrType, dcDate
	
	// CrmsUpdateImportDefinition
	// not sure if there will be a definition mapping file, in which case maybe definition file should 
	// not exist at core level and put into CrmsInsertImportDefinition, but feeling is that it 
	// should be left in core and just not used here (for this envisioning an export which uses 
	// reflection to essentially export the definition mapping, but with data, and then import it
	// using that same export file so there is no definition mapping file needed, i.e. the data 
	// file is also the definition mapping file
	
	//UPDATE: can also consider CoreInsertImportDefinition with def mapping file and 
	//CoreUpdateImportDefinition without def mapping file
	

	
	// createPatient (if not exists)
	// createCaregiver (if not exists)
	// createContactInfo (if not exists)
	// project
	// enrollPatient (if not enrolled)
	// createVisit (if not exists)
	// createInstrument (if not exists)
	
	private Boolean patientMustExist;
	private String birthDateFormat; // default: MM/dd/yyyy
	
//?? need flags for Caregiver, Contact Info, etc.. or just handle that with property names in mapping file?
// note that pediLAVA new patient history could have multiple caregivers, also caregivers may have contact info	

	// this is not a persistent property. it is available for user to specify a project if they need to
	// choose other property values that are dependent on projName (visitType, visitWith, visitLocation)
	private String projNameForContext;

	private Boolean visitMustExist;
	String visitDateFormat; // default MM/dd/yyyy
	String visitTimeFormat; // default hh:mm
	// if import will create new Visits, account for all Visit required fields
	// visitDate (and optionally visitTime) must be present in the data file (mapped to either visitDate or
	// dcDate). the rest of the required fields may not be in the data file so need to be supplied as part of
	// the definition
	private Boolean visitTypeSupplied;
	private String visitType;
	private Boolean visitWithSupplied;
	private String visitWith;
	private Boolean visitLocSupplied;
	private String visitLoc;
	private Boolean visitStatusSupplied;
	private String visitStatus;
	
// need to figure out which variables needed for;
// instrument must not exist in which case it will be created
// instrument must exist in which case it will be updated
// instrument must not exist and it it does error log record
// instrument must exist and it it does not error log record
	
// if instrument does not exist or instrument exists but not data entered proceed, but error log record
// if instrument exists and is already data entered
// alternatively, an update mode would allow update even if instrment was data entered
// easy way out would be to always create new instrument without regard for whether instrument
// already exists, in which case could have duplicate instruments
	
// goal of re-runnable scripts should be a consideration 
// initially just tink about typical use case	
	private Boolean instrMustExist; 
	private String instrType;
	private String instrDcDateFormat;
	private Boolean instrDcStatusSupplied;
	private String instrDcStatus;
	
	public CrmsImportDefinition(){
		super();
	}

	public Boolean getPatientMustExist() {
		return patientMustExist;
	}

	public void setPatientMustExist(Boolean patientMustExist) {
		this.patientMustExist = patientMustExist;
	}
	
	public String getBirthDateFormat() {
		return birthDateFormat;
	}

	public void setBirthDateFormat(String birthDateFormat) {
		this.birthDateFormat = birthDateFormat;
	}

	public String getVisitDateFormat() {
		return visitDateFormat;
	}

	public void setVisitDateFormat(String visitDateFormat) {
		this.visitDateFormat = visitDateFormat;
	}

	public String getVisitTimeFormat() {
		return visitTimeFormat;
	}

	public void setVisitTimeFormat(String visitTimeFormat) {
		this.visitTimeFormat = visitTimeFormat;
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

	public Boolean getInstrMustExist() {
		return instrMustExist;
	}

	public void setInstrMustExist(Boolean instrMustExist) {
		this.instrMustExist = instrMustExist;
	}

	public String getInstrType() {
		return instrType;
	}

	public void setInstrType(String instrType) {
		this.instrType = instrType;
	}

	public String getInstrDcDateFormat() {
		return instrDcDateFormat;
	}

	public void setInstrDcDateFormat(String instrDcDateFormat) {
		this.instrDcDateFormat = instrDcDateFormat;
	}

	public Boolean getInstrDcStatusSupplied() {
		return instrDcStatusSupplied;
	}

	public void setInstrDcStatusSupplied(Boolean instrDcStatusSupplied) {
		this.instrDcStatusSupplied = instrDcStatusSupplied;
	}

	public String getInstrDcStatus() {
		return instrDcStatus;
	}

	public void setInstrDcStatus(String instrDcStatus) {
		this.instrDcStatus = instrDcStatus;
	}

}
