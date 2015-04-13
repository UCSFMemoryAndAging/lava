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
	private int indexInstrCaregiverId;
	private Instrument instrument;
	private boolean instrCreated;
	private boolean instrExisted; // instrument exists with no data entered (deDate == null)
	private boolean instrExistedWithData; // instrument exists with data entered (deDate != null)
	
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
	
	public int getIndexInstrCaregiverId() {
		return indexInstrCaregiverId;
	}

	public void setIndexInstrCaregiverId(int indexInstrCaregiverId) {
		this.indexInstrCaregiverId = indexInstrCaregiverId;
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
	
}