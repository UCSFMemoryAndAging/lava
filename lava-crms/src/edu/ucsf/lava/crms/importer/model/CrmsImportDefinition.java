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
	// currently this flag is only visible to the SYSADMIN and is used to support rare cases where there is not
	// a date in the data for birthDate and/or esDate/visitDate/dcDate, such as when importing de-identified data
	// where those dates are not present. if the flag is set then the code does not check that the dates are 
	// out of range
	private Boolean allowExtremeDates; 
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
	private Boolean matchVisitTypeFlag;
	// allow user to specify up to 3 additional visit types to attempt to match already existing visits, along
	// with visitType (unless the visit type is specified in the data file for each import record in which case
	// these visit types are ignored for matching)
	// use case: over time data has been collected under different visit types, such that within a given data
	// file some records could match an earlier used visitType and later records could match a later used visitType
	private String matchVisitType;
	private String matchVisitType2;
	private String matchVisitType3;
	// the following are used when a new visit is created, unless values are supplied in the data file
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
	private String instrMappingAlias;
	// is this a caregiver instrument, such that an instance of a Caregiver should be set on the instrument
	private Boolean instrCaregiver;
	private Boolean instrCalculate;
	// allow up to 15 instruments to be imported from a single data file
	private String instrType2;
	private String instrVer2;
	private String instrMappingAlias2;
	private Boolean instrCaregiver2;
	private Boolean instrCalculate2;
	private String instrType3;
	private String instrVer3;
	private String instrMappingAlias3;
	private Boolean instrCaregiver3;
	private Boolean instrCalculate3;
	private String instrType4;
	private String instrVer4;
	private String instrMappingAlias4;
	private Boolean instrCaregiver4;
	private Boolean instrCalculate4;
	private String instrType5;
	private String instrVer5;
	private String instrMappingAlias5;
	private Boolean instrCaregiver5;
	private Boolean instrCalculate5;
	private String instrType6;
	private String instrVer6;
	private String instrMappingAlias6;
	private Boolean instrCaregiver6;
	private Boolean instrCalculate6;
	private String instrType7;
	private String instrVer7;
	private String instrMappingAlias7;
	private Boolean instrCaregiver7;
	private Boolean instrCalculate7;
	private String instrType8;
	private String instrVer8;
	private String instrMappingAlias8;
	private Boolean instrCaregiver8;
	private Boolean instrCalculate8;
	private String instrType9;
	private String instrVer9;
	private String instrMappingAlias9;
	private Boolean instrCaregiver9;
	private Boolean instrCalculate9;
	private String instrType10;
	private String instrVer10;
	private String instrMappingAlias10;
	private Boolean instrCaregiver10;
	private Boolean instrCalculate10;
	private String instrType11;
	private String instrVer11;
	private String instrMappingAlias11;
	private Boolean instrCaregiver11;
	private Boolean instrCalculate11;
	private String instrType12;
	private String instrVer12;
	private String instrMappingAlias12;
	private Boolean instrCaregiver12;
	private Boolean instrCalculate12;
	private String instrType13;
	private String instrVer13;
	private String instrMappingAlias13;
	private Boolean instrCaregiver13;
	private Boolean instrCalculate13;
	private String instrType14;
	private String instrVer14;
	private String instrMappingAlias14;
	private Boolean instrCaregiver14;
	private Boolean instrCalculate14;
	private String instrType15;
	private String instrVer15;
	private String instrMappingAlias15;
	private Boolean instrCaregiver15;
	private Boolean instrCalculate15;
	
	private Short instrCaregiverExistRule; // support MAY_OR_MAY_NOT_EXIST and MUST_EXIST (single rule suffices if multiple instruments)	
	private String instrDcStatus;
	
	// a LAVA standard error code to use if an imported variable has no value (i.e. is blank in the data file)
	// this is a string because all import data is read in as text and then converted via BeanUtils setProperty. also, this default 
	// will be used on both numeric and string fields (but not dates, obviously)
	// note that the 4th line of the data file will be dedicated to defaults on a per variable basis, but if nothing specified then this
	// global default value will apply
	private String instrDefaultCode; 
	
	public CrmsImportDefinition(){
		super();
		this.setAuditEntityType("CrmsImportDefinition");

		// set defaults for typical usage (base defaults set in superclass)

		// any Boolean properties that only appear in the UI conditionally need to be set to a default in case they are not submitted 
		// because on submission they bind to 0 or 1, so if not submitted they will still be null and create the potential for NullPointerException
		// these include: matchVisitTypeFlag, allowInstrUpdate, allowExtremeDates

		this.patientExistRule = MUST_EXIST;
		this.esExistRule = MAY_OR_MAY_NOT_EXIST;
		this.esStatus = "ENROLLED";
		this.visitExistRule = MAY_OR_MAY_NOT_EXIST;
		this.visitWindow = 0;
		this.matchVisitTypeFlag = true;
		this.visitStatus = "COMPLETE";
		this.instrExistRule = MAY_OR_MAY_NOT_EXIST;
		this.allowPatientUpdate = false; // currently not used
		this.allowEsUpdate = false; // currently not used
		this.allowVisitUpdate = false; // currently not used
		this.allowInstrUpdate = false; // only settable by SYSTEM ADMIN role until "update" mode is implemented
		this.instrDcStatus = "Complete";
		this.instrCalculate = true;
		this.instrCalculate2 = true;
		this.instrCalculate3 = true;
		this.instrCalculate4 = true;
		this.instrCalculate5 = true;
		this.instrCalculate6 = true;
		this.instrCalculate7 = true;
		this.instrCalculate8 = true;
		this.instrCalculate9 = true;
		this.instrCalculate10 = true;
		this.instrCalculate11 = true;
		this.instrCalculate12 = true;
		this.instrCalculate13 = true;
		this.instrCalculate14 = true;
		this.instrCalculate15 = true;
		this.allowExtremeDates = false;
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
	
	public Boolean getMatchVisitTypeFlag() {
		return matchVisitTypeFlag;
	}

	public void setMatchVisitTypeFlag(Boolean matchVisitTypeFlag) {
		this.matchVisitTypeFlag = matchVisitTypeFlag;
	}

	public String getMatchVisitType() {
		return matchVisitType;
	}

	public void setMatchVisitType(String matchVisitType) {
		this.matchVisitType = matchVisitType;
	}

	public String getMatchVisitType2() {
		return matchVisitType2;
	}

	public void setMatchVisitType2(String matchVisitType2) {
		this.matchVisitType2 = matchVisitType2;
	}

	public String getMatchVisitType3() {
		return matchVisitType3;
	}

	public void setMatchVisitType3(String matchVisitType3) {
		this.matchVisitType3 = matchVisitType3;
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

	public String getInstrMappingAlias() {
		return instrMappingAlias;
	}

	public void setInstrMappingAlias(String instrMappingAlias) {
		this.instrMappingAlias = instrMappingAlias;
	}

	public String getInstrMappingAlias2() {
		return instrMappingAlias2;
	}

	public void setInstrMappingAlias2(String instrMappingAlias2) {
		this.instrMappingAlias2 = instrMappingAlias2;
	}

	public String getInstrMappingAlias3() {
		return instrMappingAlias3;
	}

	public void setInstrMappingAlias3(String instrMappingAlias3) {
		this.instrMappingAlias3 = instrMappingAlias3;
	}

	public String getInstrMappingAlias4() {
		return instrMappingAlias4;
	}

	public void setInstrMappingAlias4(String instrMappingAlias4) {
		this.instrMappingAlias4 = instrMappingAlias4;
	}

	public String getInstrMappingAlias5() {
		return instrMappingAlias5;
	}

	public void setInstrMappingAlias5(String instrMappingAlias5) {
		this.instrMappingAlias5 = instrMappingAlias5;
	}

	public String getInstrMappingAlias6() {
		return instrMappingAlias6;
	}

	public void setInstrMappingAlias6(String instrMappingAlias6) {
		this.instrMappingAlias6 = instrMappingAlias6;
	}

	public String getInstrMappingAlias7() {
		return instrMappingAlias7;
	}

	public void setInstrMappingAlias7(String instrMappingAlias7) {
		this.instrMappingAlias7 = instrMappingAlias7;
	}

	public String getInstrMappingAlias8() {
		return instrMappingAlias8;
	}

	public void setInstrMappingAlias8(String instrMappingAlias8) {
		this.instrMappingAlias8 = instrMappingAlias8;
	}

	public String getInstrMappingAlias9() {
		return instrMappingAlias9;
	}

	public void setInstrMappingAlias9(String instrMappingAlias9) {
		this.instrMappingAlias9 = instrMappingAlias9;
	}

	public String getInstrMappingAlias10() {
		return instrMappingAlias10;
	}

	public void setInstrMappingAlias10(String instrMappingAlias10) {
		this.instrMappingAlias10 = instrMappingAlias10;
	}
	public Boolean getInstrCalculate() {
		return instrCalculate;
	}

	public void setInstrCalculate(Boolean instrCalculate) {
		this.instrCalculate = instrCalculate;
	}

	public Boolean getInstrCalculate2() {
		return instrCalculate2;
	}

	public void setInstrCalculate2(Boolean instrCalculate2) {
		this.instrCalculate2 = instrCalculate2;
	}

	public Boolean getInstrCalculate3() {
		return instrCalculate3;
	}

	public void setInstrCalculate3(Boolean instrCalculate3) {
		this.instrCalculate3 = instrCalculate3;
	}

	public Boolean getInstrCalculate4() {
		return instrCalculate4;
	}

	public void setInstrCalculate4(Boolean instrCalculate4) {
		this.instrCalculate4 = instrCalculate4;
	}

	public Boolean getInstrCalculate5() {
		return instrCalculate5;
	}

	public void setInstrCalculate5(Boolean instrCalculate5) {
		this.instrCalculate5 = instrCalculate5;
	}

	public Boolean getInstrCalculate6() {
		return instrCalculate6;
	}

	public void setInstrCalculate6(Boolean instrCalculate6) {
		this.instrCalculate6 = instrCalculate6;
	}

	public Boolean getInstrCalculate7() {
		return instrCalculate7;
	}

	public void setInstrCalculate7(Boolean instrCalculate7) {
		this.instrCalculate7 = instrCalculate7;
	}

	public Boolean getInstrCalculate8() {
		return instrCalculate8;
	}

	public void setInstrCalculate8(Boolean instrCalculate8) {
		this.instrCalculate8 = instrCalculate8;
	}

	public Boolean getInstrCalculate9() {
		return instrCalculate9;
	}

	public void setInstrCalculate9(Boolean instrCalculate9) {
		this.instrCalculate9 = instrCalculate9;
	}

	public Boolean getInstrCalculate10() {
		return instrCalculate10;
	}

	public void setInstrCalculate10(Boolean instrCalculate10) {
		this.instrCalculate10 = instrCalculate10;
	}

	public String getInstrType11() {
		return instrType11;
	}

	public void setInstrType11(String instrType11) {
		this.instrType11 = instrType11;
	}

	public String getInstrVer11() {
		return instrVer11;
	}

	public void setInstrVer11(String instrVer11) {
		this.instrVer11 = instrVer11;
	}

	public String getInstrMappingAlias11() {
		return instrMappingAlias11;
	}

	public void setInstrMappingAlias11(String instrMappingAlias11) {
		this.instrMappingAlias11 = instrMappingAlias11;
	}

	public Boolean getInstrCaregiver11() {
		return instrCaregiver11;
	}

	public void setInstrCaregiver11(Boolean instrCaregiver11) {
		this.instrCaregiver11 = instrCaregiver11;
	}

	public Boolean getInstrCalculate11() {
		return instrCalculate11;
	}

	public void setInstrCalculate11(Boolean instrCalculate11) {
		this.instrCalculate11 = instrCalculate11;
	}

	public String getInstrType12() {
		return instrType12;
	}

	public void setInstrType12(String instrType12) {
		this.instrType12 = instrType12;
	}

	public String getInstrVer12() {
		return instrVer12;
	}

	public void setInstrVer12(String instrVer12) {
		this.instrVer12 = instrVer12;
	}

	public String getInstrMappingAlias12() {
		return instrMappingAlias12;
	}

	public void setInstrMappingAlias12(String instrMappingAlias12) {
		this.instrMappingAlias12 = instrMappingAlias12;
	}

	public Boolean getInstrCaregiver12() {
		return instrCaregiver12;
	}

	public void setInstrCaregiver12(Boolean instrCaregiver12) {
		this.instrCaregiver12 = instrCaregiver12;
	}

	public Boolean getInstrCalculate12() {
		return instrCalculate12;
	}

	public void setInstrCalculate12(Boolean instrCalculate12) {
		this.instrCalculate12 = instrCalculate12;
	}

	public String getInstrType13() {
		return instrType13;
	}

	public void setInstrType13(String instrType13) {
		this.instrType13 = instrType13;
	}

	public String getInstrVer13() {
		return instrVer13;
	}

	public void setInstrVer13(String instrVer13) {
		this.instrVer13 = instrVer13;
	}

	public String getInstrMappingAlias13() {
		return instrMappingAlias13;
	}

	public void setInstrMappingAlias13(String instrMappingAlias13) {
		this.instrMappingAlias13 = instrMappingAlias13;
	}

	public Boolean getInstrCaregiver13() {
		return instrCaregiver13;
	}

	public void setInstrCaregiver13(Boolean instrCaregiver13) {
		this.instrCaregiver13 = instrCaregiver13;
	}

	public Boolean getInstrCalculate13() {
		return instrCalculate13;
	}

	public void setInstrCalculate13(Boolean instrCalculate13) {
		this.instrCalculate13 = instrCalculate13;
	}

	public String getInstrType14() {
		return instrType14;
	}

	public void setInstrType14(String instrType14) {
		this.instrType14 = instrType14;
	}

	public String getInstrVer14() {
		return instrVer14;
	}

	public void setInstrVer14(String instrVer14) {
		this.instrVer14 = instrVer14;
	}

	public String getInstrMappingAlias14() {
		return instrMappingAlias14;
	}

	public void setInstrMappingAlias14(String instrMappingAlias14) {
		this.instrMappingAlias14 = instrMappingAlias14;
	}

	public Boolean getInstrCaregiver14() {
		return instrCaregiver14;
	}

	public void setInstrCaregiver14(Boolean instrCaregiver14) {
		this.instrCaregiver14 = instrCaregiver14;
	}

	public Boolean getInstrCalculate14() {
		return instrCalculate14;
	}

	public void setInstrCalculate14(Boolean instrCalculate14) {
		this.instrCalculate14 = instrCalculate14;
	}

	public String getInstrType15() {
		return instrType15;
	}

	public void setInstrType15(String instrType15) {
		this.instrType15 = instrType15;
	}

	public String getInstrVer15() {
		return instrVer15;
	}

	public void setInstrVer15(String instrVer15) {
		this.instrVer15 = instrVer15;
	}

	public String getInstrMappingAlias15() {
		return instrMappingAlias15;
	}

	public void setInstrMappingAlias15(String instrMappingAlias15) {
		this.instrMappingAlias15 = instrMappingAlias15;
	}

	public Boolean getInstrCaregiver15() {
		return instrCaregiver15;
	}

	public void setInstrCaregiver15(Boolean instrCaregiver15) {
		this.instrCaregiver15 = instrCaregiver15;
	}

	public Boolean getInstrCalculate15() {
		return instrCalculate15;
	}

	public void setInstrCalculate15(Boolean instrCalculate15) {
		this.instrCalculate15 = instrCalculate15;
	}

	public String getInstrDefaultCode() {
		return instrDefaultCode;
	}

	public void setInstrDefaultCode(String instrDefaultCode) {
		this.instrDefaultCode = instrDefaultCode;
	}

	public Boolean getAllowExtremeDates() {
		return allowExtremeDates;
	}

	public void setAllowExtremeDates(Boolean allowExtremeDates) {
		this.allowExtremeDates = allowExtremeDates;
	}
	
}
