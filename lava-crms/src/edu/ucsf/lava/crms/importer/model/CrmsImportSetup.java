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
 * is not a persisted class; it just faciliates the import process. Aside from the data that
 * is imported and persisted in crms entities, the CrmsImportLog persists information about
 * the results of an import, in addition to a link to the imported data file, which is written
 * to the import file respository.
 *  * 
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
	
	// ContactInfo is created automatically if and only if a new Patient is created, and only if any 
	// ContactInfo data exists in the data file to be imported
	// keep in mind that these are not the only ContactInfo properties that will be imported; rather at least
	// one of these ContactInfo properties must exist for a ContactInfo record to be created for the Patient
	private int indexContactInfoAddress;
	private int indexContactInfoCity;
	private int indexContactInfoState;
	private int indexContactInfoZip;
	private int indexContactInfoPhone1;
	private int indexContactInfoEmail;
	private ContactInfo contactInfo;
	private boolean contactInfoCreated;
	private boolean contactInfoExisted;

	private int indexCaregiverFirstName;
	private int indexCaregiverLastName;
	private Caregiver caregiver;
	private boolean caregiverCreated;
	private boolean caregiverExisted;
	// caregiver ContactInfo is created automatically when caregiver is created, regardless of whether
	// there is data or not, because at a minimum the ContactInfo will be created as for the Caregiver
	private ContactInfo caregiverContactInfo;
	private boolean caregiverContactInfoCreated;
	
	//TODO: general purpose import of Caregiver ContactInfo is not done in CrmsImportHandler
	//i.e. need new contactInfoExistsHandling method ala caregiverExistsHandlng, new createContactInfo method
	// and add to setPropertyHandling, saveImportRecord
	//can just move the contactInfoExistsHandling code from SpdcHistoryFormImportHandler to CrmsImportHandler 
	// getting rid of using Event attributes and instead put values in CrmsImportSetup and move from
	// setOtherPropertyHandling and saveImportRecord, and move createContactInfo to PediImportHandler (except
	// see about the isCaregiver flag, may need to be SpdcHistoryForm specific)

	private int indexCaregiver2FirstName;
	private int indexCaregiver2LastName;
	private Caregiver caregiver2;
	private boolean caregiver2Created;
	private boolean caregiver2Existed;
	// caregiver2 ContactInfo is created automatically when caregiver2 is created, regardless of whether
	// there is data or not, because at a minimum the ContactInfo will be created as for the Caregiver2
	private ContactInfo caregiver2ContactInfo;
	private boolean caregiver2ContactInfoCreated;
	
	
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
	private Instrument instrument;
	private boolean instrCreated;
	private boolean instrExisted; // instrument exists with no data entered (deDate == null)
	private boolean instrExistedWithData; // instrument exists with data entered (deDate != null)
	
	public CrmsImportSetup() {
		super();
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

	public boolean isCaregiverContactInfoCreated() {
		return caregiverContactInfoCreated;
	}

	public void setCaregiverContactInfoCreated(boolean caregiverContactInfoCreated) {
		this.caregiverContactInfoCreated = caregiverContactInfoCreated;
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

	public boolean isCaregiver2ContactInfoCreated() {
		return caregiver2ContactInfoCreated;
	}

	public void setCaregiver2ContactInfoCreated(boolean caregiver2ContactInfoCreated) {
		this.caregiver2ContactInfoCreated = caregiver2ContactInfoCreated;
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

}