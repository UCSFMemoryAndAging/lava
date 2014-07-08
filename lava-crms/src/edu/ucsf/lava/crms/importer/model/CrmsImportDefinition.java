package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportDefinition;

public class CrmsImportDefinition extends ImportDefinition {
	// if does not exist, will be created. support re-runnable imports. note that for instruments,
	// additional requirement is that if it does exist it must not have data entered, as do not 
	// want to overwrite data
	public static Short MAY_OR_MAY_NOT_EXIST = 1;  
	// ensure that a duplicate record is not created. supports import updates.
	public static Short MUST_EXIST = 2;
	// entity will be created. does not support re-runnable in that entity will exist for all
	// subsequent imports so just create a warning instead of an error for this (in other words,
	// this flag means entity must not exist prior to the first import which creates the entity)
	public static Short MUST_NOT_EXIST = 3; 

	//TODO:	
// for visit and time format fields (birthDateFormat, visitDateFormat, visitTimeFormat) have dropdown list with
// common formats but make it a suggest field. Info text regarding default values
// same with dcStatus but not sure about the suggest bc do not have suggest on regular dcStatus ??
	
//TODO: jhesse re: security of exporting PHI CSV files for update --- is REDCap update done
// within the context of REDCap?? pretty sure Songster does updates in Excel
	
	//more:
	// CrmsInsertImportDefinition
	
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
	
	private Short patientExistRule;
	// currently this flag is not used unless it is determined that import files should be able to overwrite
	// existing Patient data
	private Boolean allowPatientUpdate; 
	
//?? need flags for Caregiver, Contact Info, etc.. or just handle that with property names in mapping file?
// note that pediLAVA new patient history could have multiple caregivers, also caregivers may have contact info	

	// this is not a persistent property. it is available for user to specify a project if they need to
	// choose other property values that are dependent on projName (visitType, visitWith, visitLocation)
	private String projNameForContext;

	// Enrollment Status
	private Short esExistRule;
	// currently this flag is not used unless it is determined that import files should be able to overwrite
	// existing EnrollmentStatus data
	private Boolean allowEsUpdate; 
	private String esStatus;

	// VIsit
	private Short visitExistRule;
	// currently this flag is not used unless it is determined that import files should be able to overwrite
	// existing Visit data
	private Boolean allowVisitUpdate; 
	// if import will create new Visits, account for all Visit required fields
	// visitDate (and optionally visitTime) must be present in the data file (mapped to either visitDate or
	// dcDate). the rest of the required fields may not be in the data file so need to be supplied as part of
	// the definition
	private String visitType;
	private String visitWith;
	private String visitLoc;
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
	
	// Instrument
	private Short instrExistRule; 
	// note that this flag applies to instruments that have already been data entered. if an instrument exists
	// but has not been data entered then this flag need not be considered as no data will be overwritten by import
	private Boolean allowInstrUpdate; 
	private String instrType;
	private String instrVer;
	private String instrDcStatus;
	
	public CrmsImportDefinition(){
		super();
	}

	public Short getPatientExistRule() {
		return patientExistRule;
	}

	public void setPatientExistRule(Short patientExistRule) {
		this.patientExistRule = patientExistRule;
	}

	public Boolean getAllowPatientUpdate() {
		return allowPatientUpdate;
	}

	public void setAllowPatientUpdate(Boolean allowPatientUpdate) {
		this.allowPatientUpdate = allowPatientUpdate;
	}

	public String getProjNameForContext() {
		return projNameForContext;
	}

	public void setProjNameForContext(String projNameForContext) {
		this.projNameForContext = projNameForContext;
	}

	public Short getEsExistRule() {
		return esExistRule;
	}

	public void setEsExistRule(Short esExistRule) {
		this.esExistRule = esExistRule;
	}

	public Boolean getAllowEsUpdate() {
		return allowEsUpdate;
	}

	public void setAllowEsUpdate(Boolean allowEsUpdate) {
		this.allowEsUpdate = allowEsUpdate;
	}

	public String getEsStatus() {
		return esStatus;
	}

	public void setEsStatus(String esStatus) {
		this.esStatus = esStatus;
	}

	public Short getVisitExistRule() {
		return visitExistRule;
	}

	public void setVisitExistRule(Short visitExistRule) {
		this.visitExistRule = visitExistRule;
	}

	public Boolean getAllowVisitUpdate() {
		return allowVisitUpdate;
	}

	public void setAllowVisitUpdate(Boolean allowVisitUpdate) {
		this.allowVisitUpdate = allowVisitUpdate;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getVisitWith() {
		return visitWith;
	}

	public void setVisitWith(String visitWith) {
		this.visitWith = visitWith;
	}

	public String getVisitLoc() {
		return visitLoc;
	}

	public void setVisitLoc(String visitLoc) {
		this.visitLoc = visitLoc;
	}

	public String getVisitStatus() {
		return visitStatus;
	}

	public void setVisitStatus(String visitStatus) {
		this.visitStatus = visitStatus;
	}

	public Short getInstrExistRule() {
		return instrExistRule;
	}

	public void setInstrExistRule(Short instrExistRule) {
		this.instrExistRule = instrExistRule;
	}

	public Boolean getAllowInstrUpdate() {
		return allowInstrUpdate;
	}

	public void setAllowInstrUpdate(Boolean allowInstrUpdate) {
		this.allowInstrUpdate = allowInstrUpdate;
	}

	public String getInstrType() {
		return instrType;
	}

	public void setInstrType(String instrType) {
		this.instrType = instrType;
	}

	public String getInstrVer() {
		return instrVer;
	}

	public void setInstrVer(String instrVer) {
		this.instrVer = instrVer;
	}

	public String getInstrDcStatus() {
		return instrDcStatus;
	}

	public void setInstrDcStatus(String instrDcStatus) {
		this.instrDcStatus = instrDcStatus;
	}

}
