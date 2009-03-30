package edu.ucsf.lava.crms.people.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

public class Caregiver extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(Caregiver.class);
	private edu.ucsf.lava.crms.people.model.Patient patient;
	private String lastName;
	private String firstName;
	private Byte gender;
	private String relation;
	private Short livesWithPatient;
	private String primaryLanguage;
	private Short transNeeded;
	private String transLanguage;
	private Short isPrimaryContact;
	private Short isContact;
	private String isContactNotes;
	private Short isCaregiver;
	private Short isInformant;
	private Short isNextOfKin;
	private Short isResearchSurrogate;
	private Short isPowerOfAttorney;
	private Short isOtherRole;
	private String otherRoleDesc;
	private String note;
	private Short active;
	private Date birthDate;
	private Byte education;
	private String race;
	private String maritalStatus;
	private String occupation;
	private Long age;
	private String fullName;
	private String fullNameRev;
	private String contactDesc;
	private String rolesDesc;
	
	private Set contactInfo;

	
	public Caregiver() {
		super();
		this.active = 1;
	}
	

	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}

	public Short getActive() {
		return active;
	}

	public void setActive(Short active) {
		this.active = active;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getContactDesc() {
		return contactDesc;
	}

	public void setContactDesc(String contactDesc) {
		this.contactDesc = contactDesc;
	}

	public Byte getEducation() {
		return education;
	}

	public void setEducation(Byte education) {
		this.education = education;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getFullNameRev() {
		return fullNameRev;
	}

	public void setFullNameRev(String fullNameRev) {
		this.fullNameRev = fullNameRev;
	}

	public Byte getGender() {
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public Short getIsCaregiver() {
		return isCaregiver;
	}

	public void setIsCaregiver(Short isCaregiver) {
		this.isCaregiver = isCaregiver;
	}

	public Short getIsContact() {
		return isContact;
	}

	public void setIsContact(Short isContact) {
		this.isContact = isContact;
	}

	public String getIsContactNotes() {
		return isContactNotes;
	}

	public void setIsContactNotes(String isContactNotes) {
		this.isContactNotes = isContactNotes;
	}

	public Short getIsInformant() {
		return isInformant;
	}

	public void setIsInformant(Short isInformant) {
		this.isInformant = isInformant;
	}

	public Short getIsNextOfKin() {
		return isNextOfKin;
	}

	public void setIsNextOfKin(Short isNextOfKin) {
		this.isNextOfKin = isNextOfKin;
	}

	public Short getIsOtherRole() {
		return isOtherRole;
	}

	public void setIsOtherRole(Short isOtherRole) {
		this.isOtherRole = isOtherRole;
	}

	public Short getIsPowerOfAttorney() {
		return isPowerOfAttorney;
	}

	public void setIsPowerOfAttorney(Short isPowerOfAttorney) {
		this.isPowerOfAttorney = isPowerOfAttorney;
	}

	public Short getIsPrimaryContact() {
		return isPrimaryContact;
	}

	public void setIsPrimaryContact(Short isPrimaryContact) {
		this.isPrimaryContact = isPrimaryContact;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Short getLivesWithPatient() {
		return livesWithPatient;
	}

	public void setLivesWithPatient(Short livesWithPatient) {
		this.livesWithPatient = livesWithPatient;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getOtherRoleDesc() {
		return otherRoleDesc;
	}

	public void setOtherRoleDesc(String otherRoleDesc) {
		this.otherRoleDesc = otherRoleDesc;
	}

	public edu.ucsf.lava.crms.people.model.Patient getPatient() {
		return patient;
	}

	public void setPatient(edu.ucsf.lava.crms.people.model.Patient patient) {
		this.patient = patient;
	}

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getRace() {
		return race;
	}

	public void setRace(String race) {
		this.race = race;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getRolesDesc() {
		return rolesDesc;
	}

	public void setRolesDesc(String rolesDesc) {
		this.rolesDesc = rolesDesc;
	}

	public String getTransLanguage() {
		return transLanguage;
	}

	public void setTransLanguage(String transLanguage) {
		this.transLanguage = transLanguage;
	}

	public Short getTransNeeded() {
		return transNeeded;
	}

	public void setTransNeeded(Short transNeeded) {
		this.transNeeded = transNeeded;
	}

	public Short getIsResearchSurrogate() {
		return isResearchSurrogate;
	}

	public void setIsResearchSurrogate(Short isResearchSurrogate) {
		this.isResearchSurrogate = isResearchSurrogate;
	}
	

	/*
	 * These accessor and mutator functions for the association set are 
	 * declard private because these associations are solely to support
	 * the cascade delete funtionality in hibernate when the caregiver 
	 * object is deleted.  If you need to access a collection of objects associated with 
	 * the caregiver, make an explicit call to the appropriate service method to
	 * query the objects.  These persistent sets are mapped as lazy initialitzed and should
	 * never be accessed under a hibenate session scope (this would cause them to be initialized 
	 * and added to the session cache which can easily create NonUniqueObjectExceptions
	 * 
	 */
	
	
	
	private Set getContactInfo() {
		return contactInfo;
	}


	private void setContactInfo(Set contactInfo) {
		this.contactInfo = contactInfo;
	}

	
	

	public boolean afterUpdate() {
		boolean resave = super.afterUpdate();
		List<ContactInfo> contactInfos = ContactInfo.getForCaregiver(this);
		for(ContactInfo contactInfo:contactInfos){
			contactInfo.save(); //this ensures that the caregiver name is correct on the contact info records
		}
		return resave;
	}


	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateFullName();
		updateFullNameRev();
		updateContactDesc();
		updateRolesDesc();
		updateAge();
	}
	
	
	/**
	 * TODO://add concept of deathdate to caregivers so the age can be calced correctly
	 *
	 */
	protected void updateAge(){
		
		Integer ageCalc = calcAge(getBirthDate());
		
		setAge((ageCalc==null)?null:ageCalc.longValue());
	}
	
	protected void updateFullName(){
		setFullName( new StringBuffer(getFirstName()).
				append(" ").append(getLastName()).toString());
	}
	
	protected void updateFullNameRev(){
		setFullNameRev( new StringBuffer(getLastName()).
				append(", ").append(getFirstName()).toString());
	}
	

	protected void updateContactDesc(){
		StringBuffer buffer = new StringBuffer();
		if(getIsPrimaryContact()==(short)1){
			buffer.append("Primary Contact");
		}else if(getIsContact()==(short)1){
			buffer.append("Contact");
		}
		
		if(getIsContactNotes()!=null){
				buffer.append(": ").append(getIsContactNotes());
		}
		
		this.setContactDesc(buffer.toString());
	}

	protected void updateRolesDesc(){
		StringBuffer buffer = new StringBuffer();
		if(getIsCaregiver()==(short)1){
			buffer.append("Caregiver; ");
		}
		if(getIsInformant()==(short)1){
			buffer.append("Informant; ");
		}
		if(getIsResearchSurrogate()==(short)1){
			buffer.append("Surrogate; ");
		}
		if(getIsNextOfKin()==(short)1){
			buffer.append("Next Of Kin; ");
		}
		if(getIsPowerOfAttorney()==(short)1){
			buffer.append("Power of Attorney; ");
		}
		if(getIsOtherRole()==(short)1){
			buffer.append("Other Role");
		}
		if(getOtherRoleDesc()!=null){
			buffer.append(": ").append(getOtherRoleDesc().toString());
		}
		this.setRolesDesc(buffer.toString());
	}

}
