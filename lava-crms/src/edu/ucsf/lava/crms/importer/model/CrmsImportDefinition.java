package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportDefinition;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class CrmsImportDefinition extends ImportDefinition {
	public static EntityManager MANAGER = new EntityBase.Manager(CrmsImportDefinition.class);
	
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

	
	private Short patientExistRule;
	// for data files that only have Patient data, no visit or assessment data. the import will only create or match 
	// Patients, ContactInfo, Caregivers, Caregivers ContactInfo, and if a Patient is created an EnrollmentStatus must 
	// be created, which will use the projName project
	private Boolean patientOnlyImport;
	// currently this flag is not used unless it is determined that import files should be able to overwrite
	// existing Patient data
	private Boolean allowPatientUpdate; 
	
	// note: for now, any imports that import Caregivers and/or ContactInfo are assumed to be new Patient imports
	// such that the Caregiver and ContactInfo records will be created if a new Patient is created and without 
	// checking whether existing records already exist 	

	// this is used both for importing data in the context of a specific project, and to provide context for
	// lists for other property values that are dependent on projName (visitType, visitWith, visitLocation)
	private String projName;

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
	private Short visitWindow;
	// if import will create new Visits, account for all Visit required fields
	// visitDate (and optionally visitTime) must be present in the data file (mapped to either visitDate or
	// dcDate). the rest of the required fields may not be in the data file so need to be supplied as part of
	// the definition
	private String visitType;
	private String visitWith;
	private String visitLoc;
	private String visitStatus;
	
	// Instrument
	private Short instrExistRule; 
	// note that this flag applies to instruments that have already been data entered. if an instrument exists
	// but has not been data entered then this flag need not be considered as no data will be overwritten by import
	private Boolean allowInstrUpdate; 
	private String instrType;
	private String instrVer;
	// is this a caregiver instrument, such that an instance of a Caregiver should be set on the instrument
	private Short instrCaregiver;
	private String instrDcStatus;
	
	public CrmsImportDefinition(){
		super();
		this.setAuditEntityType("CrmsImportDefinition");
		this.visitWindow = 0;
//TODO: remove when this functionality is supported
		this.allowInstrUpdate = Boolean.FALSE;
	}

	public Short getPatientExistRule() {
		return patientExistRule;
	}

	public void setPatientExistRule(Short patientExistRule) {
		this.patientExistRule = patientExistRule;
	}

	public Boolean getPatientOnlyImport() {
		return patientOnlyImport;
	}

	public void setPatientOnlyImport(Boolean patientOnlyImport) {
		this.patientOnlyImport = patientOnlyImport;
	}

	public Boolean getAllowPatientUpdate() {
		return allowPatientUpdate;
	}

	public void setAllowPatientUpdate(Boolean allowPatientUpdate) {
		this.allowPatientUpdate = allowPatientUpdate;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
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
	
	public Short getVisitWindow() {
		return visitWindow;
	}

	public void setVisitWindow(Short visitWindow) {
		this.visitWindow = visitWindow;
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

	public Short getInstrCaregiver() {
		return instrCaregiver;
	}

	public void setInstrCaregiver(Short instrCaregiver) {
		this.instrCaregiver = instrCaregiver;
	}

	public String getInstrDcStatus() {
		return instrDcStatus;
	}

	public void setInstrDcStatus(String instrDcStatus) {
		this.instrDcStatus = instrDcStatus;
	}

}
