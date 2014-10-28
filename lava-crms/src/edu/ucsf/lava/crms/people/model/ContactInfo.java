package edu.ucsf.lava.crms.people.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

public class ContactInfo extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(ContactInfo.class);
	private edu.ucsf.lava.crms.people.model.Patient patient;
	private Boolean isCaregiver;
	private Caregiver caregiver;
	private Short contactPatient;
	private Short isPatientResidence;
	private Short optOutMac;
	private Short optOutAffiliates;
	private Short active;
	private String address;
	private String address2;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String preferredContactMethod;
	private String phone1;
	private String phoneType1;
	private String bestTimePhone1;
	private String phone2;
	private String phoneType2;
	private String bestTimePhone2;
	private String phone3;
	private String phoneType3;
	private String bestTimePhone3;
	private String email;
	private String notes;
	private String contactNameRev;
	private String contactDesc;
	
	public ContactInfo(){
		super();
		this.active=1;
		this.optOutMac=0;
		this.optOutAffiliates=0;
		
		
	}
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}
	
	
	public String getAddressBlock(){
		StringBuffer block = new StringBuffer();
		block.append(StringUtils.defaultString(this.address,"[...]")).append("\n");
		if(StringUtils.isNotEmpty(this.address2)){
			block.append(this.address2).append("\n");	
		}
		block.append(StringUtils.defaultString(this.city,"[...]")).append(", ");
		block.append(StringUtils.defaultString(this.state,"[...]")).append(" ");
		block.append(StringUtils.defaultString(this.zip,"[...]")).append("\n");
		block.append(StringUtils.defaultString(this.country,""));
		if(StringUtils.equalsIgnoreCase(block.toString(), "[...]\n[...], [...] [...]\n")){
			return new String();
		}else{
			return new String(block);
		}
	}
	
	public void setAddressBlock(String block){
		//do nothing...this function just keeps spring:bind from failing.  
		//I suppose we could try to parse an address block?
		return;
		
	}
	
	public String getPhoneEmailBlock(){
		StringBuffer block = new StringBuffer();
		if(StringUtils.isNotEmpty(this.phone1)){
			block.append(StringUtils.defaultString(this.phoneType1,"Phone 1")).append(": ");
			block.append(StringUtils.defaultString(this.phone1,"")).append("\n");
		}
		if(StringUtils.isNotEmpty(this.phone2)){
			block.append(StringUtils.defaultString(this.phoneType2,"Phone 2")).append(": ");
			block.append(StringUtils.defaultString(this.phone2,"")).append("\n");
		}
		if(StringUtils.isNotEmpty(this.phone3)){
			block.append(StringUtils.defaultString(this.phoneType3,"Phone 3")).append(": ");
			block.append(StringUtils.defaultString(this.phone3,"")).append("\n");
		}
		if(StringUtils.isNotEmpty(this.email)){
		block.append("Email:").append(this.email);
		}
		return new String(block);
	}
	
	public void setPhoneEmailBlock(String block){
		//do nothing...this function just keeps spring:bind from failing.  
		
		return;
		
	}



	public Short getActive() {
		return active;
	}



	public void setActive(Short active) {
		this.active = active;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public String getAddress2() {
		return address2;
	}



	public void setAddress2(String address2) {
		this.address2 = address2;
	}



	public Caregiver getCaregiver() {
		return caregiver;
	}



	public void setCaregiver(Caregiver caregiver) {
		this.caregiver = caregiver;
	}



	public String getCity() {
		return city;
	}



	public void setCity(String city) {
		this.city = city;
	}



	public String getContactDesc() {
		return contactDesc;
	}



	public void setContactDesc(String contactDesc) {
		this.contactDesc = contactDesc;
	}



	public String getContactNameRev() {
		return contactNameRev;
	}



	public void setContactNameRev(String contactNameRev) {
		this.contactNameRev = contactNameRev;
	}



	public Short getContactPatient() {
		return contactPatient;
	}



	public void setContactPatient(Short contactPatient) {
		this.contactPatient = contactPatient;
	}



	public String getCountry() {
		return country;
	}



	public void setCountry(String country) {
		this.country = country;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}





	public Short getIsPatientResidence() {
		return isPatientResidence;
	}



	public void setIsPatientResidence(Short isPatientResidence) {
		this.isPatientResidence = isPatientResidence;
	}



	public String getNotes() {
		return notes;
	}



	public void setNotes(String notes) {
		this.notes = notes;
	}



	public Short getOptOutAffiliates() {
		return optOutAffiliates;
	}



	public void setOptOutAffiliates(Short optOutAffiliates) {
		this.optOutAffiliates = optOutAffiliates;
	}



	public Short getOptOutMac() {
		return optOutMac;
	}



	public void setOptOutMac(Short optOutMac) {
		this.optOutMac = optOutMac;
	}



	public edu.ucsf.lava.crms.people.model.Patient getPatient() {
		return patient;
	}



	public void setPatient(edu.ucsf.lava.crms.people.model.Patient patient) {
		this.patient = patient;
	}



	public String getPhone1() {
		return phone1;
	}



	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}



	public String getPhone2() {
		return phone2;
	}



	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}



	public String getPhone3() {
		return phone3;
	}



	public void setPhone3(String phone3) {
		this.phone3 = phone3;
	}



	public String getPhoneType1() {
		return phoneType1;
	}



	public void setPhoneType1(String phoneType1) {
		this.phoneType1 = phoneType1;
	}



	public String getPhoneType2() {
		return phoneType2;
	}



	public void setPhoneType2(String phoneType2) {
		this.phoneType2 = phoneType2;
	}



	public String getPhoneType3() {
		return phoneType3;
	}



	public void setPhoneType3(String phoneType3) {
		this.phoneType3 = phoneType3;
	}
	

	public String getPreferredContactMethod() {
		return preferredContactMethod;
	}
	public void setPreferredContactMethod(String preferredContactMethod) {
		this.preferredContactMethod = preferredContactMethod;
	}
	public String getBestTimePhone1() {
		return bestTimePhone1;
	}
	public void setBestTimePhone1(String bestTimePhone1) {
		this.bestTimePhone1 = bestTimePhone1;
	}
	public String getBestTimePhone2() {
		return bestTimePhone2;
	}
	public void setBestTimePhone2(String bestTimePhone2) {
		this.bestTimePhone2 = bestTimePhone2;
	}
	public String getBestTimePhone3() {
		return bestTimePhone3;
	}
	public void setBestTimePhone3(String bestTimePhone3) {
		this.bestTimePhone3 = bestTimePhone3;
	}
	public String getState() {
		return state;
	}



	public void setState(String state) {
		this.state = state;
	}



	public String getZip() {
		return zip;
	}



	public void setZip(String zip) {
		this.zip = zip;
	}



	public Boolean getIsCaregiver() {
		return this.isCaregiver;
	}



	public void setIsCaregiver(Boolean isCaregiver) {
		this.isCaregiver = isCaregiver;
	}
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateContactNameRev();
		updateContactDesc();
	}
	

	
	public void updateContactNameRev(){
		if(getPatient()!=null && getCaregiver()==null){
			setContactNameRev(getPatient().getFullNameRev());
		}else if(getCaregiver()!=null){
////			Caregiver c = (Caregiver)Caregiver.MANAGER.getById(getCaregiverId(),newFilterInstance());
////			if(c!=null){
////				setContactNameRev(c.getFullNameRev());
			setContactNameRev(getCaregiver().getFullNameRev());
////			}
		}else{
			setContactNameRev(null);
		}
		
	}
	
	public void updateContactDesc(){
		if(getCaregiver()==null){
			StringBuffer buffer = new StringBuffer();
			if(getContactPatient() != null && getContactPatient()==(short)1){
				buffer.append("Patient is contact");
			}else if(getContactPatient() != null && getContactPatient()==(short)0){
				buffer.append("Patient is not contact");
			}
			else {
				buffer.append("UNKNOWN if Patient is contact");
			}
			setContactDesc(buffer.toString());
		}else{
////			Caregiver c = (Caregiver)Caregiver.MANAGER.getById(getCaregiverId(),newFilterInstance());
			if(c!=null){
				setContactDesc(c.getContactDesc());
			}		
		}
	}
	
	
	
	public static List<ContactInfo> getForCaregiver(Caregiver caregiver){
		if(caregiver!=null && caregiver.getId()!=null){
			LavaDaoFilter filter = MANAGER.newFilterInstance(); 
			filter.addDaoParam(filter.daoEqualityParam("caregiverId",caregiver.getId()));
			return MANAGER.get(filter);
		}
		return new ArrayList<ContactInfo>();
	}

	public static List<ContactInfo> getForPatient(Patient patient){
		if(patient!=null && patient.getId()!=null){
			LavaDaoFilter filter = MANAGER.newFilterInstance(); 
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id",patient.getId()));
			return MANAGER.get(filter);
		}
		return new ArrayList<ContactInfo>();
	}
	
	@Override
	public boolean getLocked() {
		/* lock down this when corresponding patient is locked */
		if (getPatient() != null) return getPatient().getLocked();
		return super.getLocked();
	}

}