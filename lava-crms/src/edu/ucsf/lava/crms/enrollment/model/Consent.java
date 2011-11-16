package edu.ucsf.lava.crms.enrollment.model;

import java.util.Date;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Caregiver;
import edu.ucsf.lava.crms.people.model.Patient;

public class Consent extends CrmsEntity {
	
	public static EntityManager MANAGER = new EntityBase.Manager(Consent.class);
	
	private Long id;
	private Patient patient;
	private Caregiver caregiver;
	private Long caregiverId; 
	private String projName;
	private String consentType;
	private Byte hipaa;
	private String consentRevision;
	private String consentDeclined;
	private Date consentDate;
	private Date expirationDate;
	private Date withdrawlDate;
	private String capacityReviewBy;
	private String note;
	
	public Consent(){
		super();
		this.setProjectAuth(true);
		this.setConsentDate(dateWithoutTime(new Date()));
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}
	
	public String getCapacityReviewBy() {
		return capacityReviewBy;
	}
	public void setCapacityReviewBy(String capacityReviewBy) {
		this.capacityReviewBy = capacityReviewBy;
	}
	public Caregiver getCaregiver() {
		return caregiver;
	}
	public void setCaregiver(Caregiver caregiver) {
		this.caregiver = caregiver;
	}
	public Date getConsentDate() {
		return consentDate;
	}
	public void setConsentDate(Date consentDate) {
		this.consentDate = consentDate;
	}
	public String getConsentDeclined() {
		return consentDeclined;
	}
	public void setConsentDeclined(String consentDeclined) {
		this.consentDeclined = consentDeclined;
	}
	public String getConsentRevision() {
		return consentRevision;
	}
	public void setConsentRevision(String consentRevision) {
		this.consentRevision = consentRevision;
	}
	public String getConsentType() {
		return consentType;
	}
	public void setConsentType(String consentType) {
		this.consentType = consentType;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public Date getWithdrawlDate() {
		return withdrawlDate;
	}
	public void setWithdrawlDate(Date withdrawlDate) {
		this.withdrawlDate = withdrawlDate;
	}
	public Long getCaregiverId() {
		return caregiverId;
	}
	public void setCaregiverId(Long caregiverId) {
		this.caregiverId = caregiverId;
	}

	public Byte getHipaa() {
		return hipaa;
	}

	public void setHipaa(Byte hipaa) {
		this.hipaa = hipaa;
	}

	public String getConsentTypeBlock() {
		StringBuffer sb = new StringBuffer(this.consentType);
		sb.append(this.consentRevision != null ? ", Revision #" + this.consentRevision : "");
		return sb.toString();
	}
}
