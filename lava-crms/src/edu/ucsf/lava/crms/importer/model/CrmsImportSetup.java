package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportSetup;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.people.model.Caregiver;
import edu.ucsf.lava.crms.people.model.ContactInfo;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;

/**
 * This class contains properties used during the import of a crms data file. Note that this
 * is not a persisted class; it just facilitates the import process. Aside from the data that
 * is imported and persisted in crms entities, the CrmsImportLog persists information about
 * the results of an import, in addition to a link to the imported data file, which is written
 * to the import file repository.
 *  
 * @author ctoohey
 *
 */
public class CrmsImportSetup extends ImportSetup {
	
	// projName is part of CrmsImportDefinition and is required for creating Patients (initial 
	// EnrollmentStatus), creating Visits and creating Instruments
	// specific to the import is to allow custom generation of projName in case import data file 
	// only contains unit (aka site) and need to append it to projName to create a revisedProjName
	// which includes project and unit (see Project class)
	private String revisedProjName;
	
	// these properties facilitate the import of Patient, EnrollmentStatus, Visit and instrument
	// data.
	
	// marker indices for properties that are needed to check for the prior existence of an entity,
	// and/or are required fields for creating a new entity
	
	private int indexPatientPIDN;
	private int indexPatientFirstName;
	private int indexPatientLastName;
	private int indexPatientBirthDate;
	private int indexPatientGender;
	private Patient patient;
	private boolean patientCreated;
	private boolean patientExisted;
	
	// ContactInfo is created if and only if a new Patient is created, and only if any ContactInfo data exists
	// in the data file to be imported
	// keep in mind that these are not the only ContactInfo properties that will be imported; rather at least
	// one of these ContactInfo properties must exist for a ContactInfo record to be created for the Patient, but
	// other properties can be imported, e.g. address2
	private int indexContactInfoAddress;
	private int indexContactInfoCity;
	private int indexContactInfoState;
	private int indexContactInfoZip;
	private int indexContactInfoPhone1;
	private int indexContactInfoPhone2; // ContactInfo could just be a phone2 cell phone without phone1 data
	private int indexContactInfoEmail;
	private ContactInfo contactInfo;
	private boolean contactInfoCreated;
	private boolean contactInfoExisted;

	private int indexCaregiverFirstName;
	private int indexCaregiverLastName;
	private int indexCaregiverRelation;
	private Caregiver caregiver;
	private boolean caregiverCreated;
	private boolean caregiverExisted;

	// see comments for ContactInfo index fields above
	private int indexCaregiverContactInfoAddress;
	private int indexCaregiverContactInfoCity;
	private int indexCaregiverContactInfoState;
	private int indexCaregiverContactInfoZip;
	private int indexCaregiverContactInfoPhone1;
	private int indexCaregiverContactInfoPhone2;
	private int indexCaregiverContactInfoEmail;
	private ContactInfo caregiverContactInfo;
	private boolean caregiverContactInfoCreated;
	private boolean caregiverContactInfoExisted;
	
	private int indexCaregiver2FirstName;
	private int indexCaregiver2LastName;
	private int indexCaregiver2Relation;
	private Caregiver caregiver2;
	private boolean caregiver2Created;
	private boolean caregiver2Existed;

	// caregiver2 ContactInfo - see comments for caregiver contactInfo above
	private int indexCaregiver2ContactInfoAddress;
	private int indexCaregiver2ContactInfoCity;
	private int indexCaregiver2ContactInfoState;
	private int indexCaregiver2ContactInfoZip;
	private int indexCaregiver2ContactInfoPhone1;
	private int indexCaregiver2ContactInfoPhone2;
	private int indexCaregiver2ContactInfoEmail;
	private ContactInfo caregiver2ContactInfo;
	private boolean caregiver2ContactInfoCreated;
	private boolean caregiver2ContactInfoExisted;
	
	private int indexEsProjName;	
	private int indexEsStatusDate;
	private int indexEsStatus;
	private EnrollmentStatus enrollmentStatus;
	private boolean enrollmentStatusCreated;
	private boolean enrollmentStatusExisted;
	
	private int indexVisitDate;
	private int indexVisitTime;
	private int indexVisitType;
	private int indexVisitWith;
	private int indexVisitLoc;
	private int indexVisitStatus;
	private Visit visit;
	private boolean visitCreated;
	private boolean visitExisted;
	
	private int indexInstrDcDate;
	private int indexInstrDcStatus;
//	private int indexInstrCaregiverId;  believe this was never used. took different approach to caregiver instruments
	private Instrument instrument;
	private boolean instrCreated;
	private boolean instrExisted; // instrument exists with no data entered (deDate == null)
	private boolean instrExistedWithData; // instrument exists with data entered (deDate != null)
	private boolean instrExistedWithDataNoUpdate; // instrument exists with data entered (deDate != null) and not in update mode (which may not be currently available to the users yet)

	private int indexInstr2DcDate;
	private int indexInstr2DcStatus;
	private Instrument instrument2;
	// the first instrument uses these flags for updating instrument counts for successful import records where
	// each count represents all the instruments if the data file has multiple import records -- so the
	// first instrument is considered representative of all the instruments
	
	// for the first instrument and all subsequent instruments these flags are used for creating importLog
	// detail messages whenever a new instrument is created (and possibly if an existing instrument is updated
	// if that is considered valuable)
	private boolean instr2Created;
	private boolean instr2Existed;
	private boolean instr2ExistedWithData;
	private boolean instr2ExistedWithDataNoUpdate;

	private int indexInstr3DcDate;
	private int indexInstr3DcStatus;
	private Instrument instrument3;
	private boolean instr3Created;
	private boolean instr3Existed;
	private boolean instr3ExistedWithData;
	private boolean instr3ExistedWithDataNoUpdate;
	
	private int indexInstr4DcDate;
	private int indexInstr4DcStatus;
	private Instrument instrument4;
	private boolean instr4Created;
	private boolean instr4Existed;
	private boolean instr4ExistedWithData;
	private boolean instr4ExistedWithDataNoUpdate;
	
	private int indexInstr5DcDate;
	private int indexInstr5DcStatus;
	private Instrument instrument5;
	private boolean instr5Created;
	private boolean instr5Existed;
	private boolean instr5ExistedWithData;
	private boolean instr5ExistedWithDataNoUpdate;
	
	private int indexInstr6DcDate;
	private int indexInstr6DcStatus;
	private Instrument instrument6;
	private boolean instr6Created;
	private boolean instr6Existed;
	private boolean instr6ExistedWithData;
	private boolean instr6ExistedWithDataNoUpdate;
	
	private int indexInstr7DcDate;
	private int indexInstr7DcStatus;
	private Instrument instrument7;
	private boolean instr7Created;
	private boolean instr7Existed;
	private boolean instr7ExistedWithData;
	private boolean instr7ExistedWithDataNoUpdate;
	
	private int indexInstr8DcDate;
	private int indexInstr8DcStatus;
	private Instrument instrument8;
	private boolean instr8Created;
	private boolean instr8Existed;
	private boolean instr8ExistedWithData;
	private boolean instr8ExistedWithDataNoUpdate;
	
	private int indexInstr9DcDate;
	private int indexInstr9DcStatus;
	private Instrument instrument9;
	private boolean instr9Created;
	private boolean instr9Existed;
	private boolean instr9ExistedWithData;
	private boolean instr9ExistedWithDataNoUpdate;
	
	private int indexInstr10DcDate;
	private int indexInstr10DcStatus;
	private Instrument instrument10;
	private boolean instr10Created;
	private boolean instr10Existed;
	private boolean instr10ExistedWithData;
	private boolean instr10ExistedWithDataNoUpdate;

	private int indexInstr11DcDate;
	private int indexInstr11DcStatus;
	private Instrument instrument11;
	private boolean instr11Created;
	private boolean instr11Existed;
	private boolean instr11ExistedWithData;
	private boolean instr11ExistedWithDataNoUpdate;
	
	private int indexInstr12DcDate;
	private int indexInstr12DcStatus;
	private Instrument instrument12;
	private boolean instr12Created;
	private boolean instr12Existed;
	private boolean instr12ExistedWithData;
	private boolean instr12ExistedWithDataNoUpdate;
	
	private int indexInstr13DcDate;
	private int indexInstr13DcStatus;
	private Instrument instrument13;
	private boolean instr13Created;
	private boolean instr13Existed;
	private boolean instr13ExistedWithData;
	private boolean instr13ExistedWithDataNoUpdate;
	
	private int indexInstr14DcDate;
	private int indexInstr14DcStatus;
	private Instrument instrument14;
	private boolean instr14Created;
	private boolean instr14Existed;
	private boolean instr14ExistedWithData;
	private boolean instr14ExistedWithDataNoUpdate;
	
	private int indexInstr15DcDate;
	private int indexInstr15DcStatus;
	private Instrument instrument15;
	private boolean instr15Created;
	private boolean instr15Existed;
	private boolean instr15ExistedWithData;
	private boolean instr15ExistedWithDataNoUpdate;
	
	public CrmsImportSetup() {
		super();
	}
	
	/**
	 * While the index properties apply to the import as a whole, the entities and flags are used for
	 * each row in the import data file, so they should be initialized at the beginning of processing a 
	 * row of data.
	 */
	public void reset() {
		super.reset();
		this.patient = null;
		this.patientCreated = this.patientExisted = false;
		this.contactInfo = null;
		this.contactInfoCreated = this.contactInfoExisted = false;
		this.caregiver = null;
		this.caregiverCreated = this.caregiverExisted = false;
		this.caregiverContactInfo = null;
		this.caregiverContactInfoCreated = this.caregiverContactInfoExisted = false;
		this.caregiver2 = null;
		this.caregiver2Created = this.caregiver2Existed = false;
		this.caregiver2ContactInfo = null;
		this.caregiver2ContactInfoCreated = this.caregiver2ContactInfoExisted = false;
		this.enrollmentStatus = null;
		this.enrollmentStatusCreated = this.enrollmentStatusExisted = false;
		this.visit = null;
		this.visitCreated = this.visitExisted = false;
		this.instrument = null;
		this.instrCreated = this.instrExisted = this.instrExistedWithData = false;
		this.instrExistedWithDataNoUpdate = false;
		this.instr2ExistedWithDataNoUpdate = false;
		this.instr3ExistedWithDataNoUpdate = false;
		this.instr4ExistedWithDataNoUpdate = false;
		this.instr5ExistedWithDataNoUpdate = false;
		this.instr6ExistedWithDataNoUpdate = false;
		this.instr7ExistedWithDataNoUpdate = false;
		this.instr8ExistedWithDataNoUpdate = false;
		this.instr9ExistedWithDataNoUpdate = false;
		this.instr10ExistedWithDataNoUpdate = false;
		this.instr11ExistedWithDataNoUpdate = false;
		this.instr12ExistedWithDataNoUpdate = false;
		this.instr13ExistedWithDataNoUpdate = false;
		this.instr14ExistedWithDataNoUpdate = false;
		this.instr15ExistedWithDataNoUpdate = false;
	}

	public String getRevisedProjName() {
		return revisedProjName;
	}

	public void setRevisedProjName(String revisedProjName) {
		this.revisedProjName = revisedProjName;
	}

	public int getIndexPatientPIDN() {
		return indexPatientPIDN;
	}

	public void setIndexPatientPIDN(int indexPatientPIDN) {
		this.indexPatientPIDN = indexPatientPIDN;
	}

	public int getIndexPatientFirstName() {
		return indexPatientFirstName;
	}

	public void setIndexPatientFirstName(int indexPatientFirstName) {
		this.indexPatientFirstName = indexPatientFirstName;
	}

	public int getIndexPatientLastName() {
		return indexPatientLastName;
	}

	public void setIndexPatientLastName(int indexPatientLastName) {
		this.indexPatientLastName = indexPatientLastName;
	}

	public int getIndexPatientBirthDate() {
		return indexPatientBirthDate;
	}

	public void setIndexPatientBirthDate(int indexPatientBirthDate) {
		this.indexPatientBirthDate = indexPatientBirthDate;
	}

	public int getIndexPatientGender() {
		return indexPatientGender;
	}

	public void setIndexPatientGender(int indexPatientGender) {
		this.indexPatientGender = indexPatientGender;
	}
	
	public boolean isPatientCreated() {
		return patientCreated;
	}

	public void setPatientCreated(boolean patientCreated) {
		this.patientCreated = patientCreated;
	}

	public boolean isPatientExisted() {
		return patientExisted;
	}

	public void setPatientExisted(boolean patientExisted) {
		this.patientExisted = patientExisted;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public int getIndexContactInfoAddress() {
		return indexContactInfoAddress;
	}

	public void setIndexContactInfoAddress(int indexContactInfoAddress) {
		this.indexContactInfoAddress = indexContactInfoAddress;
	}

	public int getIndexContactInfoCity() {
		return indexContactInfoCity;
	}

	public void setIndexContactInfoCity(int indexContactInfoCity) {
		this.indexContactInfoCity = indexContactInfoCity;
	}

	public int getIndexContactInfoState() {
		return indexContactInfoState;
	}

	public void setIndexContactInfoState(int indexContactInfoState) {
		this.indexContactInfoState = indexContactInfoState;
	}

	public int getIndexContactInfoZip() {
		return indexContactInfoZip;
	}

	public void setIndexContactInfoZip(int indexContactInfoZip) {
		this.indexContactInfoZip = indexContactInfoZip;
	}

	public int getIndexContactInfoPhone1() {
		return indexContactInfoPhone1;
	}
	
	public int getIndexContactInfoPhone2() {
		return indexContactInfoPhone2;
	}

	public void setIndexContactInfoPhone2(int indexContactInfoPhone2) {
		this.indexContactInfoPhone2 = indexContactInfoPhone2;
	}

	public void setIndexContactInfoPhone1(int indexContactInfoPhone1) {
		this.indexContactInfoPhone1 = indexContactInfoPhone1;
	}

	public int getIndexContactInfoEmail() {
		return indexContactInfoEmail;
	}

	public void setIndexContactInfoEmail(int indexContactInfoEmail) {
		this.indexContactInfoEmail = indexContactInfoEmail;
	}

	public ContactInfo getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(ContactInfo contactInfo) {
		this.contactInfo = contactInfo;
	}

	public boolean isContactInfoCreated() {
		return contactInfoCreated;
	}

	public void setContactInfoCreated(boolean contactInfoCreated) {
		this.contactInfoCreated = contactInfoCreated;
	}

	public boolean isContactInfoExisted() {
		return contactInfoExisted;
	}

	public void setContactInfoExisted(boolean contactInfoExisted) {
		this.contactInfoExisted = contactInfoExisted;
	}

	public int getIndexCaregiverFirstName() {
		return indexCaregiverFirstName;
	}

	public void setIndexCaregiverFirstName(int indexCaregiverFirstName) {
		this.indexCaregiverFirstName = indexCaregiverFirstName;
	}

	public int getIndexCaregiverLastName() {
		return indexCaregiverLastName;
	}

	public void setIndexCaregiverLastName(int indexCaregiverLastName) {
		this.indexCaregiverLastName = indexCaregiverLastName;
	}
	
	public int getIndexCaregiverRelation() {
		return indexCaregiverRelation;
	}

	public void setIndexCaregiverRelation(int indexCaregiverRelation) {
		this.indexCaregiverRelation = indexCaregiverRelation;
	}

	public Caregiver getCaregiver() {
		return caregiver;
	}

	public void setCaregiver(Caregiver caregiver) {
		this.caregiver = caregiver;
	}

	public boolean isCaregiverCreated() {
		return caregiverCreated;
	}

	public void setCaregiverCreated(boolean caregiverCreated) {
		this.caregiverCreated = caregiverCreated;
	}

	public boolean isCaregiverExisted() {
		return caregiverExisted;
	}

	public void setCaregiverExisted(boolean caregiverExisted) {
		this.caregiverExisted = caregiverExisted;
	}
	
	public ContactInfo getCaregiverContactInfo() {
		return caregiverContactInfo;
	}

	public void setCaregiverContactInfo(ContactInfo caregiverContactInfo) {
		this.caregiverContactInfo = caregiverContactInfo;
	}

	public int getIndexCaregiver2FirstName() {
		return indexCaregiver2FirstName;
	}

	public void setIndexCaregiver2FirstName(int indexCaregiver2FirstName) {
		this.indexCaregiver2FirstName = indexCaregiver2FirstName;
	}

	public int getIndexCaregiver2LastName() {
		return indexCaregiver2LastName;
	}

	public void setIndexCaregiver2LastName(int indexCaregiver2LastName) {
		this.indexCaregiver2LastName = indexCaregiver2LastName;
	}

	public int getIndexCaregiver2Relation() {
		return indexCaregiver2Relation;
	}

	public void setIndexCaregiver2Relation(int indexCaregiver2Relation) {
		this.indexCaregiver2Relation = indexCaregiver2Relation;
	}

	public Caregiver getCaregiver2() {
		return caregiver2;
	}

	public void setCaregiver2(Caregiver caregiver2) {
		this.caregiver2 = caregiver2;
	}

	public boolean isCaregiver2Created() {
		return caregiver2Created;
	}

	public void setCaregiver2Created(boolean caregiver2Created) {
		this.caregiver2Created = caregiver2Created;
	}

	public boolean isCaregiver2Existed() {
		return caregiver2Existed;
	}

	public void setCaregiver2Existed(boolean caregiver2Existed) {
		this.caregiver2Existed = caregiver2Existed;
	}

	public ContactInfo getCaregiver2ContactInfo() {
		return caregiver2ContactInfo;
	}

	public void setCaregiver2ContactInfo(ContactInfo caregiver2ContactInfo) {
		this.caregiver2ContactInfo = caregiver2ContactInfo;
	}

	public int getIndexEsProjName() {
		return indexEsProjName;
	}

	public void setIndexEsProjName(int indexEsProjName) {
		this.indexEsProjName = indexEsProjName;
	}

	public int getIndexEsStatusDate() {
		return indexEsStatusDate;
	}

	public void setIndexEsStatusDate(int indexEsStatusDate) {
		this.indexEsStatusDate = indexEsStatusDate;
	}

	public int getIndexEsStatus() {
		return indexEsStatus;
	}

	public void setIndexEsStatus(int indexEsStatus) {
		this.indexEsStatus = indexEsStatus;
	}

	public boolean isEnrollmentStatusCreated() {
		return enrollmentStatusCreated;
	}

	public void setEnrollmentStatusCreated(boolean enrollmentStatusCreated) {
		this.enrollmentStatusCreated = enrollmentStatusCreated;
	}

	public boolean isEnrollmentStatusExisted() {
		return enrollmentStatusExisted;
	}

	public void setEnrollmentStatusExisted(boolean enrollmentStatusExisted) {
		this.enrollmentStatusExisted = enrollmentStatusExisted;
	}

	public EnrollmentStatus getEnrollmentStatus() {
		return enrollmentStatus;
	}

	public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}

	public int getIndexVisitDate() {
		return indexVisitDate;
	}

	public void setIndexVisitDate(int indexVisitDate) {
		this.indexVisitDate = indexVisitDate;
	}

	public int getIndexVisitTime() {
		return indexVisitTime;
	}

	public void setIndexVisitTime(int indexVisitTime) {
		this.indexVisitTime = indexVisitTime;
	}

	public int getIndexVisitType() {
		return indexVisitType;
	}

	public void setIndexVisitType(int indexVisitType) {
		this.indexVisitType = indexVisitType;
	}

	public int getIndexVisitWith() {
		return indexVisitWith;
	}

	public void setIndexVisitWith(int indexVisitWith) {
		this.indexVisitWith = indexVisitWith;
	}

	public int getIndexVisitLoc() {
		return indexVisitLoc;
	}

	public void setIndexVisitLoc(int indexVisitLoc) {
		this.indexVisitLoc = indexVisitLoc;
	}

	public int getIndexVisitStatus() {
		return indexVisitStatus;
	}

	public void setIndexVisitStatus(int indexVisitStatus) {
		this.indexVisitStatus = indexVisitStatus;
	}

	public boolean isVisitCreated() {
		return visitCreated;
	}

	public void setVisitCreated(boolean visitCreated) {
		this.visitCreated = visitCreated;
	}

	public boolean isVisitExisted() {
		return visitExisted;
	}

	public void setVisitExisted(boolean visitExisted) {
		this.visitExisted = visitExisted;
	}

	public Visit getVisit() {
		return visit;
	}

	public void setVisit(Visit visit) {
		this.visit = visit;
	}

	public int getIndexInstrDcDate() {
		return indexInstrDcDate;
	}

	public void setIndexInstrDcDate(int indexInstrDcDate) {
		this.indexInstrDcDate = indexInstrDcDate;
	}

	public int getIndexInstrDcStatus() {
		return indexInstrDcStatus;
	}

	public void setIndexInstrDcStatus(int indexInstrDcStatus) {
		this.indexInstrDcStatus = indexInstrDcStatus;
	}

	public boolean isInstrCreated() {
		return instrCreated;
	}

	public void setInstrCreated(boolean instrCreated) {
		this.instrCreated = instrCreated;
	}

	public boolean isInstrExisted() {
		return instrExisted;
	}

	public void setInstrExisted(boolean instrExisted) {
		this.instrExisted = instrExisted;
	}

	public boolean isInstrExistedWithData() {
		return instrExistedWithData;
	}

	public void setInstrExistedWithData(boolean instrExistedWithData) {
		this.instrExistedWithData = instrExistedWithData;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}
	
	public boolean isCaregiverContactInfoCreated() {
		return caregiverContactInfoCreated;
	}

	public void setCaregiverContactInfoCreated(boolean caregiverContactInfoCreated) {
		this.caregiverContactInfoCreated = caregiverContactInfoCreated;
	}
	
	public boolean isCaregiverContactInfoExisted() {
		return caregiverContactInfoExisted;
	}

	public void setCaregiverContactInfoExisted(boolean caregiverContactInfoExisted) {
		this.caregiverContactInfoExisted = caregiverContactInfoExisted;
	}

	public boolean isCaregiver2ContactInfoCreated() {
		return caregiver2ContactInfoCreated;
	}

	public void setCaregiver2ContactInfoCreated(boolean caregiver2ContactInfoCreated) {
		this.caregiver2ContactInfoCreated = caregiver2ContactInfoCreated;
	}
	
	public boolean isCaregiver2ContactInfoExisted() {
		return caregiver2ContactInfoExisted;
	}

	public void setCaregiver2ContactInfoExisted(boolean caregiver2ContactInfoExisted) {
		this.caregiver2ContactInfoExisted = caregiver2ContactInfoExisted;
	}

	public int getIndexCaregiverContactInfoAddress() {
		return indexCaregiverContactInfoAddress;
	}

	public void setIndexCaregiverContactInfoAddress(
			int indexCaregiverContactInfoAddress) {
		this.indexCaregiverContactInfoAddress = indexCaregiverContactInfoAddress;
	}

	public int getIndexCaregiverContactInfoCity() {
		return indexCaregiverContactInfoCity;
	}

	public void setIndexCaregiverContactInfoCity(int indexCaregiverContactInfoCity) {
		this.indexCaregiverContactInfoCity = indexCaregiverContactInfoCity;
	}

	public int getIndexCaregiverContactInfoState() {
		return indexCaregiverContactInfoState;
	}

	public void setIndexCaregiverContactInfoState(int indexCaregiverContactInfoState) {
		this.indexCaregiverContactInfoState = indexCaregiverContactInfoState;
	}

	public int getIndexCaregiverContactInfoZip() {
		return indexCaregiverContactInfoZip;
	}

	public void setIndexCaregiverContactInfoZip(int indexCaregiverContactInfoZip) {
		this.indexCaregiverContactInfoZip = indexCaregiverContactInfoZip;
	}

	public int getIndexCaregiverContactInfoPhone1() {
		return indexCaregiverContactInfoPhone1;
	}

	public void setIndexCaregiverContactInfoPhone1(
			int indexCaregiverContactInfoPhone1) {
		this.indexCaregiverContactInfoPhone1 = indexCaregiverContactInfoPhone1;
	}
	
	public int getIndexCaregiverContactInfoPhone2() {
		return indexCaregiverContactInfoPhone2;
	}

	public void setIndexCaregiverContactInfoPhone2(
			int indexCaregiverContactInfoPhone2) {
		this.indexCaregiverContactInfoPhone2 = indexCaregiverContactInfoPhone2;
	}

	public int getIndexCaregiverContactInfoEmail() {
		return indexCaregiverContactInfoEmail;
	}

	public void setIndexCaregiverContactInfoEmail(int indexCaregiverContactInfoEmail) {
		this.indexCaregiverContactInfoEmail = indexCaregiverContactInfoEmail;
	}

	public int getIndexCaregiver2ContactInfoAddress() {
		return indexCaregiver2ContactInfoAddress;
	}

	public void setIndexCaregiver2ContactInfoAddress(
			int indexCaregiver2ContactInfoAddress) {
		this.indexCaregiver2ContactInfoAddress = indexCaregiver2ContactInfoAddress;
	}

	public int getIndexCaregiver2ContactInfoCity() {
		return indexCaregiver2ContactInfoCity;
	}

	public void setIndexCaregiver2ContactInfoCity(int indexCaregiver2ContactInfoCity) {
		this.indexCaregiver2ContactInfoCity = indexCaregiver2ContactInfoCity;
	}

	public int getIndexCaregiver2ContactInfoState() {
		return indexCaregiver2ContactInfoState;
	}

	public void setIndexCaregiver2ContactInfoState(
			int indexCaregiver2ContactInfoState) {
		this.indexCaregiver2ContactInfoState = indexCaregiver2ContactInfoState;
	}

	public int getIndexCaregiver2ContactInfoZip() {
		return indexCaregiver2ContactInfoZip;
	}

	public void setIndexCaregiver2ContactInfoZip(int indexCaregiver2ContactInfoZip) {
		this.indexCaregiver2ContactInfoZip = indexCaregiver2ContactInfoZip;
	}

	public int getIndexCaregiver2ContactInfoPhone1() {
		return indexCaregiver2ContactInfoPhone1;
	}

	public void setIndexCaregiver2ContactInfoPhone1(
			int indexCaregiver2ContactInfoPhone1) {
		this.indexCaregiver2ContactInfoPhone1 = indexCaregiver2ContactInfoPhone1;
	}
	
	public int getIndexCaregiver2ContactInfoPhone2() {
		return indexCaregiver2ContactInfoPhone2;
	}

	public void setIndexCaregiver2ContactInfoPhone2(
			int indexCaregiver2ContactInfoPhone2) {
		this.indexCaregiver2ContactInfoPhone2 = indexCaregiver2ContactInfoPhone2;
	}

	public int getIndexCaregiver2ContactInfoEmail() {
		return indexCaregiver2ContactInfoEmail;
	}

	public void setIndexCaregiver2ContactInfoEmail(
			int indexCaregiver2ContactInfoEmail) {
		this.indexCaregiver2ContactInfoEmail = indexCaregiver2ContactInfoEmail;
	}

	public int getIndexInstr2DcDate() {
		return indexInstr2DcDate;
	}

	public void setIndexInstr2DcDate(int indexInstr2DcDate) {
		this.indexInstr2DcDate = indexInstr2DcDate;
	}

	public int getIndexInstr2DcStatus() {
		return indexInstr2DcStatus;
	}

	public void setIndexInstr2DcStatus(int indexInstr2DcStatus) {
		this.indexInstr2DcStatus = indexInstr2DcStatus;
	}

	public Instrument getInstrument2() {
		return instrument2;
	}

	public void setInstrument2(Instrument instrument2) {
		this.instrument2 = instrument2;
	}

	public boolean isInstr2Created() {
		return instr2Created;
	}

	public void setInstr2Created(boolean instr2Created) {
		this.instr2Created = instr2Created;
	}

	public boolean isInstr2Existed() {
		return instr2Existed;
	}

	public void setInstr2Existed(boolean instr2Existed) {
		this.instr2Existed = instr2Existed;
	}

	public boolean isInstr2ExistedWithData() {
		return instr2ExistedWithData;
	}

	public void setInstr2ExistedWithData(boolean instr2ExistedWithData) {
		this.instr2ExistedWithData = instr2ExistedWithData;
	}

	public int getIndexInstr3DcDate() {
		return indexInstr3DcDate;
	}

	public void setIndexInstr3DcDate(int indexInstr3DcDate) {
		this.indexInstr3DcDate = indexInstr3DcDate;
	}

	public int getIndexInstr3DcStatus() {
		return indexInstr3DcStatus;
	}

	public void setIndexInstr3DcStatus(int indexInstr3DcStatus) {
		this.indexInstr3DcStatus = indexInstr3DcStatus;
	}

	public Instrument getInstrument3() {
		return instrument3;
	}

	public void setInstrument3(Instrument instrument3) {
		this.instrument3 = instrument3;
	}

	public boolean isInstr3Created() {
		return instr3Created;
	}

	public void setInstr3Created(boolean instr3Created) {
		this.instr3Created = instr3Created;
	}

	public boolean isInstr3Existed() {
		return instr3Existed;
	}

	public void setInstr3Existed(boolean instr3Existed) {
		this.instr3Existed = instr3Existed;
	}

	public boolean isInstr3ExistedWithData() {
		return instr3ExistedWithData;
	}

	public void setInstr3ExistedWithData(boolean instr3ExistedWithData) {
		this.instr3ExistedWithData = instr3ExistedWithData;
	}

	public int getIndexInstr4DcDate() {
		return indexInstr4DcDate;
	}

	public void setIndexInstr4DcDate(int indexInstr4DcDate) {
		this.indexInstr4DcDate = indexInstr4DcDate;
	}

	public int getIndexInstr4DcStatus() {
		return indexInstr4DcStatus;
	}

	public void setIndexInstr4DcStatus(int indexInstr4DcStatus) {
		this.indexInstr4DcStatus = indexInstr4DcStatus;
	}

	public Instrument getInstrument4() {
		return instrument4;
	}

	public void setInstrument4(Instrument instrument4) {
		this.instrument4 = instrument4;
	}

	public boolean isInstr4Created() {
		return instr4Created;
	}

	public void setInstr4Created(boolean instr4Created) {
		this.instr4Created = instr4Created;
	}

	public boolean isInstr4Existed() {
		return instr4Existed;
	}

	public void setInstr4Existed(boolean instr4Existed) {
		this.instr4Existed = instr4Existed;
	}

	public boolean isInstr4ExistedWithData() {
		return instr4ExistedWithData;
	}

	public void setInstr4ExistedWithData(boolean instr4ExistedWithData) {
		this.instr4ExistedWithData = instr4ExistedWithData;
	}

	public int getIndexInstr5DcDate() {
		return indexInstr5DcDate;
	}

	public void setIndexInstr5DcDate(int indexInstr5DcDate) {
		this.indexInstr5DcDate = indexInstr5DcDate;
	}

	public int getIndexInstr5DcStatus() {
		return indexInstr5DcStatus;
	}

	public void setIndexInstr5DcStatus(int indexInstr5DcStatus) {
		this.indexInstr5DcStatus = indexInstr5DcStatus;
	}

	public Instrument getInstrument5() {
		return instrument5;
	}

	public void setInstrument5(Instrument instrument5) {
		this.instrument5 = instrument5;
	}

	public boolean isInstr5Created() {
		return instr5Created;
	}

	public void setInstr5Created(boolean instr5Created) {
		this.instr5Created = instr5Created;
	}

	public boolean isInstr5Existed() {
		return instr5Existed;
	}

	public void setInstr5Existed(boolean instr5Existed) {
		this.instr5Existed = instr5Existed;
	}

	public boolean isInstr5ExistedWithData() {
		return instr5ExistedWithData;
	}

	public void setInstr5ExistedWithData(boolean instr5ExistedWithData) {
		this.instr5ExistedWithData = instr5ExistedWithData;
	}

	public int getIndexInstr6DcDate() {
		return indexInstr6DcDate;
	}

	public void setIndexInstr6DcDate(int indexInstr6DcDate) {
		this.indexInstr6DcDate = indexInstr6DcDate;
	}

	public int getIndexInstr6DcStatus() {
		return indexInstr6DcStatus;
	}

	public void setIndexInstr6DcStatus(int indexInstr6DcStatus) {
		this.indexInstr6DcStatus = indexInstr6DcStatus;
	}

	public Instrument getInstrument6() {
		return instrument6;
	}

	public void setInstrument6(Instrument instrument6) {
		this.instrument6 = instrument6;
	}

	public boolean isInstr6Created() {
		return instr6Created;
	}

	public void setInstr6Created(boolean instr6Created) {
		this.instr6Created = instr6Created;
	}

	public boolean isInstr6Existed() {
		return instr6Existed;
	}

	public void setInstr6Existed(boolean instr6Existed) {
		this.instr6Existed = instr6Existed;
	}

	public boolean isInstr6ExistedWithData() {
		return instr6ExistedWithData;
	}

	public void setInstr6ExistedWithData(boolean instr6ExistedWithData) {
		this.instr6ExistedWithData = instr6ExistedWithData;
	}

	public int getIndexInstr7DcDate() {
		return indexInstr7DcDate;
	}

	public void setIndexInstr7DcDate(int indexInstr7DcDate) {
		this.indexInstr7DcDate = indexInstr7DcDate;
	}

	public int getIndexInstr7DcStatus() {
		return indexInstr7DcStatus;
	}

	public void setIndexInstr7DcStatus(int indexInstr7DcStatus) {
		this.indexInstr7DcStatus = indexInstr7DcStatus;
	}

	public Instrument getInstrument7() {
		return instrument7;
	}

	public void setInstrument7(Instrument instrument7) {
		this.instrument7 = instrument7;
	}

	public boolean isInstr7Created() {
		return instr7Created;
	}

	public void setInstr7Created(boolean instr7Created) {
		this.instr7Created = instr7Created;
	}

	public boolean isInstr7Existed() {
		return instr7Existed;
	}

	public void setInstr7Existed(boolean instr7Existed) {
		this.instr7Existed = instr7Existed;
	}

	public boolean isInstr7ExistedWithData() {
		return instr7ExistedWithData;
	}

	public void setInstr7ExistedWithData(boolean instr7ExistedWithData) {
		this.instr7ExistedWithData = instr7ExistedWithData;
	}

	public int getIndexInstr8DcDate() {
		return indexInstr8DcDate;
	}

	public void setIndexInstr8DcDate(int indexInstr8DcDate) {
		this.indexInstr8DcDate = indexInstr8DcDate;
	}

	public int getIndexInstr8DcStatus() {
		return indexInstr8DcStatus;
	}

	public void setIndexInstr8DcStatus(int indexInstr8DcStatus) {
		this.indexInstr8DcStatus = indexInstr8DcStatus;
	}

	public Instrument getInstrument8() {
		return instrument8;
	}

	public void setInstrument8(Instrument instrument8) {
		this.instrument8 = instrument8;
	}

	public boolean isInstr8Created() {
		return instr8Created;
	}

	public void setInstr8Created(boolean instr8Created) {
		this.instr8Created = instr8Created;
	}

	public boolean isInstr8Existed() {
		return instr8Existed;
	}

	public void setInstr8Existed(boolean instr8Existed) {
		this.instr8Existed = instr8Existed;
	}

	public boolean isInstr8ExistedWithData() {
		return instr8ExistedWithData;
	}

	public void setInstr8ExistedWithData(boolean instr8ExistedWithData) {
		this.instr8ExistedWithData = instr8ExistedWithData;
	}

	public int getIndexInstr9DcDate() {
		return indexInstr9DcDate;
	}

	public void setIndexInstr9DcDate(int indexInstr9DcDate) {
		this.indexInstr9DcDate = indexInstr9DcDate;
	}

	public int getIndexInstr9DcStatus() {
		return indexInstr9DcStatus;
	}

	public void setIndexInstr9DcStatus(int indexInstr9DcStatus) {
		this.indexInstr9DcStatus = indexInstr9DcStatus;
	}

	public Instrument getInstrument9() {
		return instrument9;
	}

	public void setInstrument9(Instrument instrument9) {
		this.instrument9 = instrument9;
	}

	public boolean isInstr9Created() {
		return instr9Created;
	}

	public void setInstr9Created(boolean instr9Created) {
		this.instr9Created = instr9Created;
	}

	public boolean isInstr9Existed() {
		return instr9Existed;
	}

	public void setInstr9Existed(boolean instr9Existed) {
		this.instr9Existed = instr9Existed;
	}

	public boolean isInstr9ExistedWithData() {
		return instr9ExistedWithData;
	}

	public void setInstr9ExistedWithData(boolean instr9ExistedWithData) {
		this.instr9ExistedWithData = instr9ExistedWithData;
	}

	public int getIndexInstr10DcDate() {
		return indexInstr10DcDate;
	}

	public void setIndexInstr10DcDate(int indexInstr10DcDate) {
		this.indexInstr10DcDate = indexInstr10DcDate;
	}

	public int getIndexInstr10DcStatus() {
		return indexInstr10DcStatus;
	}

	public void setIndexInstr10DcStatus(int indexInstr10DcStatus) {
		this.indexInstr10DcStatus = indexInstr10DcStatus;
	}

	public Instrument getInstrument10() {
		return instrument10;
	}

	public void setInstrument10(Instrument instrument10) {
		this.instrument10 = instrument10;
	}

	public boolean isInstr10Created() {
		return instr10Created;
	}

	public void setInstr10Created(boolean instr10Created) {
		this.instr10Created = instr10Created;
	}

	public boolean isInstr10Existed() {
		return instr10Existed;
	}

	public void setInstr10Existed(boolean instr10Existed) {
		this.instr10Existed = instr10Existed;
	}

	public boolean isInstr10ExistedWithData() {
		return instr10ExistedWithData;
	}

	public void setInstr10ExistedWithData(boolean instr10ExistedWithData) {
		this.instr10ExistedWithData = instr10ExistedWithData;
	}

	public int getIndexInstr11DcDate() {
		return indexInstr11DcDate;
	}

	public void setIndexInstr11DcDate(int indexInstr11DcDate) {
		this.indexInstr11DcDate = indexInstr11DcDate;
	}

	public int getIndexInstr11DcStatus() {
		return indexInstr11DcStatus;
	}

	public void setIndexInstr11DcStatus(int indexInstr11DcStatus) {
		this.indexInstr11DcStatus = indexInstr11DcStatus;
	}

	public Instrument getInstrument11() {
		return instrument11;
	}

	public void setInstrument11(Instrument instrument11) {
		this.instrument11 = instrument11;
	}

	public boolean isInstr11Created() {
		return this.instr11Created;
	}

	public void setInstr11Created(boolean instr11Created) {
		this.instr11Created = instr11Created;
	}

	public boolean isInstr11Existed() {
		return instr11Existed;
	}

	public void setInstr11Existed(boolean instr11Existed) {
		this.instr11Existed = instr11Existed;
	}

	public boolean isInstr11ExistedWithData() {
		return instr11ExistedWithData;
	}

	public void setInstr11ExistedWithData(boolean instr11ExistedWithData) {
		this.instr11ExistedWithData = instr11ExistedWithData;
	}

	public int getIndexInstr12DcDate() {
		return indexInstr12DcDate;
	}

	public void setIndexInstr12DcDate(int indexInstr12DcDate) {
		this.indexInstr12DcDate = indexInstr12DcDate;
	}

	public int getIndexInstr12DcStatus() {
		return indexInstr12DcStatus;
	}

	public void setIndexInstr12DcStatus(int indexInstr12DcStatus) {
		this.indexInstr12DcStatus = indexInstr12DcStatus;
	}

	public Instrument getInstrument12() {
		return instrument12;
	}

	public void setInstrument12(Instrument instrument12) {
		this.instrument12 = instrument12;
	}

	public boolean isInstr12Created() {
		return instr12Created;
	}

	public void setInstr12Created(boolean instr12Created) {
		this.instr12Created = instr12Created;
	}

	public boolean isInstr12Existed() {
		return instr12Existed;
	}

	public void setInstr12Existed(boolean instr12Existed) {
		this.instr12Existed = instr12Existed;
	}

	public boolean isInstr12ExistedWithData() {
		return instr12ExistedWithData;
	}

	public void setInstr12ExistedWithData(boolean instr12ExistedWithData) {
		this.instr12ExistedWithData = instr12ExistedWithData;
	}

	public int getIndexInstr13DcDate() {
		return indexInstr13DcDate;
	}

	public void setIndexInstr13DcDate(int indexInstr13DcDate) {
		this.indexInstr13DcDate = indexInstr13DcDate;
	}

	public int getIndexInstr13DcStatus() {
		return indexInstr13DcStatus;
	}

	public void setIndexInstr13DcStatus(int indexInstr13DcStatus) {
		this.indexInstr13DcStatus = indexInstr13DcStatus;
	}

	public Instrument getInstrument13() {
		return instrument13;
	}

	public void setInstrument13(Instrument instrument13) {
		this.instrument13 = instrument13;
	}

	public boolean isInstr13Created() {
		return instr13Created;
	}

	public void setInstr13Created(boolean instr13Created) {
		this.instr13Created = instr13Created;
	}

	public boolean isInstr13Existed() {
		return instr13Existed;
	}

	public void setInstr13Existed(boolean instr13Existed) {
		this.instr13Existed = instr13Existed;
	}

	public boolean isInstr13ExistedWithData() {
		return instr13ExistedWithData;
	}

	public void setInstr13ExistedWithData(boolean instr13ExistedWithData) {
		this.instr13ExistedWithData = instr13ExistedWithData;
	}

	public int getIndexInstr14DcDate() {
		return indexInstr14DcDate;
	}

	public void setIndexInstr14DcDate(int indexInstr14DcDate) {
		this.indexInstr14DcDate = indexInstr14DcDate;
	}

	public int getIndexInstr14DcStatus() {
		return indexInstr14DcStatus;
	}

	public void setIndexInstr14DcStatus(int indexInstr14DcStatus) {
		this.indexInstr14DcStatus = indexInstr14DcStatus;
	}

	public Instrument getInstrument14() {
		return instrument14;
	}

	public void setInstrument14(Instrument instrument14) {
		this.instrument14 = instrument14;
	}

	public boolean isInstr14Created() {
		return instr14Created;
	}

	public void setInstr14Created(boolean instr14Created) {
		this.instr14Created = instr14Created;
	}

	public boolean isInstr14Existed() {
		return instr14Existed;
	}

	public void setInstr14Existed(boolean instr14Existed) {
		this.instr14Existed = instr14Existed;
	}

	public boolean isInstr14ExistedWithData() {
		return instr14ExistedWithData;
	}

	public void setInstr14ExistedWithData(boolean instr14ExistedWithData) {
		this.instr14ExistedWithData = instr14ExistedWithData;
	}

	public int getIndexInstr15DcDate() {
		return indexInstr15DcDate;
	}

	public void setIndexInstr15DcDate(int indexInstr15DcDate) {
		this.indexInstr15DcDate = indexInstr15DcDate;
	}

	public int getIndexInstr15DcStatus() {
		return indexInstr15DcStatus;
	}

	public void setIndexInstr15DcStatus(int indexInstr15DcStatus) {
		this.indexInstr15DcStatus = indexInstr15DcStatus;
	}

	public Instrument getInstrument15() {
		return instrument15;
	}

	public void setInstrument15(Instrument instrument15) {
		this.instrument15 = instrument15;
	}

	public boolean isInstr15Created() {
		return instr15Created;
	}

	public void setInstr15Created(boolean instr15Created) {
		this.instr15Created = instr15Created;
	}

	public boolean isInstr15Existed() {
		return instr15Existed;
	}

	public void setInstr15Existed(boolean instr15Existed) {
		this.instr15Existed = instr15Existed;
	}

	public boolean isInstr15ExistedWithData() {
		return instr15ExistedWithData;
	}

	public void setInstr15ExistedWithData(boolean instr15ExistedWithData) {
		this.instr15ExistedWithData = instr15ExistedWithData;
	}

	public boolean isInstrExistedWithDataNoUpdate() {
		return instrExistedWithDataNoUpdate;
	}

	public void setInstrExistedWithDataNoUpdate(boolean instrExistedWithDataNoUpdate) {
		this.instrExistedWithDataNoUpdate = instrExistedWithDataNoUpdate;
	}

	public boolean isInstr2ExistedWithDataNoUpdate() {
		return instr2ExistedWithDataNoUpdate;
	}

	public void setInstr2ExistedWithDataNoUpdate(boolean instr2ExistedWithDataNoUpdate) {
		this.instr2ExistedWithDataNoUpdate = instr2ExistedWithDataNoUpdate;
	}

	public boolean isInstr3ExistedWithDataNoUpdate() {
		return instr3ExistedWithDataNoUpdate;
	}

	public void setInstr3ExistedWithDataNoUpdate(boolean instr3ExistedWithDataNoUpdate) {
		this.instr3ExistedWithDataNoUpdate = instr3ExistedWithDataNoUpdate;
	}

	public boolean isInstr4ExistedWithDataNoUpdate() {
		return instr4ExistedWithDataNoUpdate;
	}

	public void setInstr4ExistedWithDataNoUpdate(boolean instr4ExistedWithDataNoUpdate) {
		this.instr4ExistedWithDataNoUpdate = instr4ExistedWithDataNoUpdate;
	}

	public boolean isInstr5ExistedWithDataNoUpdate() {
		return instr5ExistedWithDataNoUpdate;
	}

	public void setInstr5ExistedWithDataNoUpdate(boolean instr5ExistedWithDataNoUpdate) {
		this.instr5ExistedWithDataNoUpdate = instr5ExistedWithDataNoUpdate;
	}

	public boolean isInstr6ExistedWithDataNoUpdate() {
		return instr6ExistedWithDataNoUpdate;
	}

	public void setInstr6ExistedWithDataNoUpdate(boolean instr6ExistedWithDataNoUpdate) {
		this.instr6ExistedWithDataNoUpdate = instr6ExistedWithDataNoUpdate;
	}

	public boolean isInstr7ExistedWithDataNoUpdate() {
		return instr7ExistedWithDataNoUpdate;
	}

	public void setInstr7ExistedWithDataNoUpdate(boolean instr7ExistedWithDataNoUpdate) {
		this.instr7ExistedWithDataNoUpdate = instr7ExistedWithDataNoUpdate;
	}

	public boolean isInstr8ExistedWithDataNoUpdate() {
		return instr8ExistedWithDataNoUpdate;
	}

	public void setInstr8ExistedWithDataNoUpdate(boolean instr8ExistedWithDataNoUpdate) {
		this.instr8ExistedWithDataNoUpdate = instr8ExistedWithDataNoUpdate;
	}

	public boolean isInstr9ExistedWithDataNoUpdate() {
		return instr9ExistedWithDataNoUpdate;
	}

	public void setInstr9ExistedWithDataNoUpdate(boolean instr9ExistedWithDataNoUpdate) {
		this.instr9ExistedWithDataNoUpdate = instr9ExistedWithDataNoUpdate;
	}

	public boolean isInstr10ExistedWithDataNoUpdate() {
		return instr10ExistedWithDataNoUpdate;
	}

	public void setInstr10ExistedWithDataNoUpdate(boolean instr10ExistedWithDataNoUpdate) {
		this.instr10ExistedWithDataNoUpdate = instr10ExistedWithDataNoUpdate;
	}

	public boolean isInstr11ExistedWithDataNoUpdate() {
		return instr11ExistedWithDataNoUpdate;
	}

	public void setInstr11ExistedWithDataNoUpdate(boolean instr11ExistedWithDataNoUpdate) {
		this.instr11ExistedWithDataNoUpdate = instr11ExistedWithDataNoUpdate;
	}

	public boolean isInstr12ExistedWithDataNoUpdate() {
		return instr12ExistedWithDataNoUpdate;
	}

	public void setInstr12ExistedWithDataNoUpdate(boolean instr12ExistedWithDataNoUpdate) {
		this.instr12ExistedWithDataNoUpdate = instr12ExistedWithDataNoUpdate;
	}

	public boolean isInstr13ExistedWithDataNoUpdate() {
		return instr13ExistedWithDataNoUpdate;
	}

	public void setInstr13ExistedWithDataNoUpdate(boolean instr13ExistedWithDataNoUpdate) {
		this.instr13ExistedWithDataNoUpdate = instr13ExistedWithDataNoUpdate;
	}

	public boolean isInstr14ExistedWithDataNoUpdate() {
		return instr14ExistedWithDataNoUpdate;
	}

	public void setInstr14ExistedWithDataNoUpdate(boolean instr14ExistedWithDataNoUpdate) {
		this.instr14ExistedWithDataNoUpdate = instr14ExistedWithDataNoUpdate;
	}

	public boolean isInstr15ExistedWithDataNoUpdate() {
		return instr15ExistedWithDataNoUpdate;
	}

	public void setInstr15ExistedWithDataNoUpdate(boolean instr15ExistedWithDataNoUpdate) {
		this.instr15ExistedWithDataNoUpdate = instr15ExistedWithDataNoUpdate;
	}

}