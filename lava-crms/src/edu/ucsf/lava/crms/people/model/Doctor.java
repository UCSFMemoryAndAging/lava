package edu.ucsf.lava.crms.people.model;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;

public class Doctor extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(Doctor.class);
	private String lastName;
	private Character middleInitial;
	private String firstName;
	private String address;
	private String city;
	private String state;
	private String zip;
	private String phone1;
	private String phoneType1;
	private String phone2;
	private String phoneType2;
	private String phone3;
	private String phoneType3;
	private String email;
	private String docType;
	private String fullNameRev;
	private String fullName;
	private Set patients;
	
	public Doctor(){
		super();
		setPatientAuth(false);
	}
	
	public Set getPatients() {
		return patients;
	}
	public void setPatients(Set patients) {
		this.patients = patients;
	}
	
	public void addPatient(PatientDoctor patientDoctor){
		patientDoctor.setDoctor(this);
		patients.add(patientDoctor);
		
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDocType() {
		return docType;
	}
	public void setDocType(String docType) {
		this.docType = docType;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Character getMiddleInitial() {
		return middleInitial;
	}
	public void setMiddleInitial(Character middleInitial) {
		this.middleInitial = middleInitial;
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
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateFullName();
		updateFullNameRev();
	}
	
	
	protected void updateFullName(){
		StringBuffer buffer = new StringBuffer();
			buffer.append(getFirstName()).append(" ");
			if(getMiddleInitial()!=null){
				buffer.append(getMiddleInitial()).append(". ");
			}

			buffer.append(getLastName());
			
		setFullName(buffer.toString());
	}
	protected void updateFullNameRev(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(getLastName()).append(", ");
		buffer.append(getFirstName()).append(" ");
		if(getMiddleInitial()!=null){
			buffer.append(getMiddleInitial()).append(".");
		}
		setFullNameRev(buffer.toString());
	}

	
	public String getAddressBlock(){
		StringBuffer block = new StringBuffer();
		block.append(StringUtils.defaultString(this.address,"[Street Address]")).append("\n");
		block.append(StringUtils.defaultString(this.city,"[City]")).append(", ");
		block.append(StringUtils.defaultString(this.state,"[State]")).append(" ");
		block.append(StringUtils.defaultString(this.zip,"[Zip]"));
		return new String(block);
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
	

	
	public List findPatients(LavaDaoFilter filter){
		filter.setAlias("patient","patient");
		filter.setAlias("doctor","doctor");
		filter.addDefaultSort("patient.fullNameRev",true);
		return MANAGER.get(PatientDoctor.class,filter);
	}
	
	
	
}
