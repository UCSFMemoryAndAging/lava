package edu.ucsf.lava.crms.importer.model;

import edu.ucsf.lava.core.importer.model.ImportLog;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class CrmsImportLog extends ImportLog {
	public static EntityManager MANAGER = new EntityBase.Manager(CrmsImportLog.class);

	private String projName;
	
	// Record the following counts, which are for specific entities within an import record. Note
	// that these do not represent errors and are just informational. 
	// The superclass maintains properties for counts for import records as a whole, i.e. total
	// imported, number not imported due to errors, number with warnings, etc. 
	private Integer newPatients;
	private Integer existingPatients;
	private Integer newContactInfo;
	private Integer existingContactInfo;
	// both caregiver and caregiver2 are counted together in the next 3 counts
	private Integer newCaregivers; 
	private Integer existingCaregivers;
	private Integer newCaregiversContactInfo;
	// currently not maintaining this count since not checking for existing ContactInfo for a Caregiver and
	// instead just counting existing Caregivers. since caregiver and caregiverContactInfo are tightly
	// bound that is enough
	private Integer existingCaregiversContactInfo;
	private Integer newEnrollmentStatuses;
	private Integer existingEnrollmentStatuses;
	private Integer newVisits;
	private Integer existingVisits;
	private Integer newInstruments;
	private Integer existingInstruments; // deDate == null
	private Integer existingInstrumentsWithData; // deDate != null 
	
	public CrmsImportLog(){
		super();
		this.newPatients = 0;
		this.existingPatients = 0;
		this.newContactInfo = 0;
		this.existingContactInfo = 0;
		this.newCaregivers = 0;
		this.existingCaregivers = 0;
		this.newCaregiversContactInfo = 0;
		this.newEnrollmentStatuses = 0;
		this.existingEnrollmentStatuses = 0;
		this.newVisits = 0;
		this.existingVisits = 0;
		this.newInstruments = 0;
		this.existingInstruments = 0;
		this.existingInstrumentsWithData = 0;
		this.setAuditEntityType("CrmsImportLog");
	}
	
	public CrmsImportLog(ImportLog importLog){
		this();
		this.setImportedBy(importLog.getImportedBy());
		this.setImportTimestamp(importLog.getImportTimestamp());
		this.setTotalRecords(importLog.getTotalRecords());
		this.setImported(importLog.getImported());
		this.setUpdated(importLog.getUpdated());
		this.setAlreadyExist(importLog.getAlreadyExist());
		this.setErrors(importLog.getErrors());
		this.setWarnings(importLog.getWarnings());
		this.setAuditEntityType("CrmsImportLog");
	}
	

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public Integer getNewPatients() {
		return newPatients;
	}

	public void setNewPatients(Integer newPatients) {
		this.newPatients = newPatients;
	}

	public Integer getExistingPatients() {
		return existingPatients;
	}

	public void setExistingPatients(Integer existingPatients) {
		this.existingPatients = existingPatients;
	}

	public Integer getNewCaregivers() {
		return newCaregivers;
	}

	public void setNewCaregivers(Integer newCaregivers) {
		this.newCaregivers = newCaregivers;
	}

	public Integer getExistingCaregivers() {
		return existingCaregivers;
	}

	public void setExistingCaregivers(Integer existingCaregivers) {
		this.existingCaregivers = existingCaregivers;
	}

	public Integer getExistingContactInfo() {
		return existingContactInfo;
	}

	public void setExistingContactInfo(Integer existingContactInfo) {
		this.existingContactInfo = existingContactInfo;
	}
	
	public Integer getNewContactInfo() {
		return newContactInfo;
	}

	public void setNewContactInfo(Integer newContactInfo) {
		this.newContactInfo = newContactInfo;
	}

	public Integer getNewCaregiversContactInfo() {
		return newCaregiversContactInfo;
	}

	public void setNewCaregiversContactInfo(Integer newCaregiversContactInfo) {
		this.newCaregiversContactInfo = newCaregiversContactInfo;
	}
	
	public Integer getExistingCaregiversContactInfo() {
		return existingCaregiversContactInfo;
	}

	public void setExistingCaregiversContactInfo(
			Integer existingCaregiversContactInfo) {
		this.existingCaregiversContactInfo = existingCaregiversContactInfo;
	}

	public Integer getNewEnrollmentStatuses() {
		return newEnrollmentStatuses;
	}

	public void setNewEnrollmentStatuses(Integer newEnrollmentStatuses) {
		this.newEnrollmentStatuses = newEnrollmentStatuses;
	}

	public Integer getExistingEnrollmentStatuses() {
		return existingEnrollmentStatuses;
	}

	public void setExistingEnrollmentStatuses(Integer existingEnrollmentStatuses) {
		this.existingEnrollmentStatuses = existingEnrollmentStatuses;
	}

	public Integer getNewVisits() {
		return newVisits;
	}

	public void setNewVisits(Integer newVisits) {
		this.newVisits = newVisits;
	}

	public Integer getExistingVisits() {
		return existingVisits;
	}

	public void setExistingVisits(Integer existingVisits) {
		this.existingVisits = existingVisits;
	}

	public Integer getNewInstruments() {
		return newInstruments;
	}

	public void setNewInstruments(Integer newInstruments) {
		this.newInstruments = newInstruments;
	}

	public Integer getExistingInstruments() {
		return existingInstruments;
	}

	public void setExistingInstruments(Integer existingInstruments) {
		this.existingInstruments = existingInstruments;
	}
	
	public Integer getExistingInstrumentsWithData() {
		return existingInstrumentsWithData;
	}

	public void setExistingInstrumentsWithData(Integer existingInstrumentsWithData) {
		this.existingInstrumentsWithData = existingInstrumentsWithData;
	}

	public void incNewPatients() {
		this.newPatients++;
	}
	
	public void incExistingPatients() {
		this.existingPatients++;
	}

	public void incNewContactInfo() {
		this.newContactInfo++;
	}
	
	public void incExistingContactInfo() {
		this.existingContactInfo++;
	}

	public void incNewCaregivers() {
		this.newCaregivers++;
	}
	
	public void incExistingCaregivers() {
		this.existingCaregivers++;
	}

	public void incNewCaregiverContactInfo() {
		this.newCaregiversContactInfo++;
	}
	
	public void incNewEnrollmentStatuses() {
		this.newEnrollmentStatuses++;
	}
	
	public void incExistingEnrollmentStatuses() {
		this.existingEnrollmentStatuses++;
	}

	public void incNewVisits() {
		this.newVisits++;
	}
	
	public void incExistingVisits() {
		this.existingVisits++;
	}

	public void incNewInstruments() {
		this.newInstruments++;
	}
	
	public void incExistingInstruments() {
		this.existingInstruments++;
	}

	public void incExistingInstrumentsWithData() {
		this.existingInstrumentsWithData++;
	}
}
