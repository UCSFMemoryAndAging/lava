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
	private Boolean matchVisitType;
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
	private Boolean instrCaregiver;
	// allow up to 10 instruments to be imported from a single data file
	private String instrType2;
	private String instrVer2;
	private Boolean instrCaregiver2;
	private String instrType3;
	private String instrVer3;
	private Boolean instrCaregiver3;
	private String instrType4;
	private String instrVer4;
	private Boolean instrCaregiver4;
	private String instrType5;
	private String instrVer5;
	private Boolean instrCaregiver5;
	private String instrType6;
	private String instrVer6;
	private Boolean instrCaregiver6;
	private String instrType7;
	private String instrVer7;
	private Boolean instrCaregiver7;
	private String instrType8;
	private String instrVer8;
	private Boolean instrCaregiver8;
	private String instrType9;
	private String instrVer9;
	private Boolean instrCaregiver9;
	private String instrType10;
	private String instrVer10;
	private Boolean instrCaregiver10;
	
	private Short instrCaregiverExistRule; // support MAY_OR_MAY_NOT_EXIST and MUST_EXIST (single rule suffices if multiple instruments)	
	private String instrDcStatus;
	
	public CrmsImportDefinition(){
		super();
		this.setAuditEntityType("CrmsImportDefinition");
		// set defaults for typical usage (base defaults set in superclass)
		this.patientExistRule = MUST_EXIST;
		this.esExistRule = MAY_OR_MAY_NOT_EXIST;
		this.esStatus = "ENROLLED";
		this.visitExistRule = MAY_OR_MAY_NOT_EXIST;
		this.visitWindow = 0;
		this.matchVisitType = Boolean.TRUE;
		this.visitStatus = "COMPLETE";
		this.instrExistRule = MAY_OR_MAY_NOT_EXIST;
		this.allowInstrUpdate = Boolean.FALSE;
		this.instrDcStatus = "Complete";
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
	
	public Boolean getMatchVisitType() {
		return matchVisitType;
	}

	public void setMatchVisitType(Boolean matchVisitType) {
		this.matchVisitType = matchVisitType;
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

	public Boolean getInstrCaregiver() {
		return instrCaregiver;
	}

	public void setInstrCaregiver(Boolean instrCaregiver) {
		this.instrCaregiver = instrCaregiver;
	}

	public String getInstrType10() {
		return instrType10;
	}

	public void setInstrType10(String instrType10) {
		this.instrType10 = instrType10;
	}

	public String getInstrVer10() {
		return instrVer10;
	}

	public void setInstrVer10(String instrVer10) {
		this.instrVer10 = instrVer10;
	}

	public Boolean getInstrCaregiver10() {
		return instrCaregiver10;
	}

	public void setInstrCaregiver10(Boolean instrCaregiver10) {
		this.instrCaregiver10 = instrCaregiver10;
	}

	public String getInstrType2() {
		return instrType2;
	}

	public void setInstrType2(String instrType2) {
		this.instrType2 = instrType2;
	}

	public String getInstrVer2() {
		return instrVer2;
	}

	public void setInstrVer2(String instrVer2) {
		this.instrVer2 = instrVer2;
	}

	public Boolean getInstrCaregiver2() {
		return instrCaregiver2;
	}

	public void setInstrCaregiver2(Boolean instrCaregiver2) {
		this.instrCaregiver2 = instrCaregiver2;
	}

	public String getInstrType3() {
		return instrType3;
	}

	public void setInstrType3(String instrType3) {
		this.instrType3 = instrType3;
	}

	public String getInstrVer3() {
		return instrVer3;
	}

	public void setInstrVer3(String instrVer3) {
		this.instrVer3 = instrVer3;
	}

	public Boolean getInstrCaregiver3() {
		return instrCaregiver3;
	}

	public void setInstrCaregiver3(Boolean instrCaregiver3) {
		this.instrCaregiver3 = instrCaregiver3;
	}

	public String getInstrType4() {
		return instrType4;
	}

	public void setInstrType4(String instrType4) {
		this.instrType4 = instrType4;
	}

	public String getInstrVer4() {
		return instrVer4;
	}

	public void setInstrVer4(String instrVer4) {
		this.instrVer4 = instrVer4;
	}

	public Boolean getInstrCaregiver4() {
		return instrCaregiver4;
	}

	public void setInstrCaregiver4(Boolean instrCaregiver4) {
		this.instrCaregiver4 = instrCaregiver4;
	}

	public String getInstrType5() {
		return instrType5;
	}

	public void setInstrType5(String instrType5) {
		this.instrType5 = instrType5;
	}

	public String getInstrVer5() {
		return instrVer5;
	}

	public void setInstrVer5(String instrVer5) {
		this.instrVer5 = instrVer5;
	}

	public Boolean getInstrCaregiver5() {
		return instrCaregiver5;
	}

	public void setInstrCaregiver5(Boolean instrCaregiver5) {
		this.instrCaregiver5 = instrCaregiver5;
	}

	public String getInstrType6() {
		return instrType6;
	}

	public void setInstrType6(String instrType6) {
		this.instrType6 = instrType6;
	}

	public String getInstrVer6() {
		return instrVer6;
	}

	public void setInstrVer6(String instrVer6) {
		this.instrVer6 = instrVer6;
	}

	public Boolean getInstrCaregiver6() {
		return instrCaregiver6;
	}

	public void setInstrCaregiver6(Boolean instrCaregiver6) {
		this.instrCaregiver6 = instrCaregiver6;
	}

	public String getInstrType7() {
		return instrType7;
	}

	public void setInstrType7(String instrType7) {
		this.instrType7 = instrType7;
	}

	public String getInstrVer7() {
		return instrVer7;
	}

	public void setInstrVer7(String instrVer7) {
		this.instrVer7 = instrVer7;
	}

	public Boolean getInstrCaregiver7() {
		return instrCaregiver7;
	}

	public void setInstrCaregiver7(Boolean instrCaregiver7) {
		this.instrCaregiver7 = instrCaregiver7;
	}

	public String getInstrType8() {
		return instrType8;
	}

	public void setInstrType8(String instrType8) {
		this.instrType8 = instrType8;
	}

	public String getInstrVer8() {
		return instrVer8;
	}

	public void setInstrVer8(String instrVer8) {
		this.instrVer8 = instrVer8;
	}

	public Boolean getInstrCaregiver8() {
		return instrCaregiver8;
	}

	public void setInstrCaregiver8(Boolean instrCaregiver8) {
		this.instrCaregiver8 = instrCaregiver8;
	}

	public String getInstrType9() {
		return instrType9;
	}

	public void setInstrType9(String instrType9) {
		this.instrType9 = instrType9;
	}

	public String getInstrVer9() {
		return instrVer9;
	}

	public void setInstrVer9(String instrVer9) {
		this.instrVer9 = instrVer9;
	}

	public Boolean getInstrCaregiver9() {
		return instrCaregiver9;
	}

	public void setInstrCaregiver9(Boolean instrCaregiver9) {
		this.instrCaregiver9 = instrCaregiver9;
	}

	public Short getInstrCaregiverExistRule() {
		return instrCaregiverExistRule;
	}

	public void setInstrCaregiverExistRule(Short instrCaregiverExistRule) {
		this.instrCaregiverExistRule = instrCaregiverExistRule;
	}

	public String getInstrDcStatus() {
		return instrDcStatus;
	}

	public void setInstrDcStatus(String instrDcStatus) {
		this.instrDcStatus = instrDcStatus;
	}

}
