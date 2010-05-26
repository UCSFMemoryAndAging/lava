package edu.ucsf.lava.crms.people.dto;

import java.util.Date;

import edu.ucsf.lava.core.dto.PagedListItemDto;


public class FindPatientListItemDto implements PagedListItemDto {

	protected Long id;
	protected String fullNameRevNoSuffix;
	protected Date birthDate;
	protected Boolean deceased;
	protected Date deathDate;
	protected String phone1;
	protected String phone2;
	protected String phone3;
	protected String phoneType1;
	protected String phoneType2;
	protected String phoneType3;
	protected String email;
	protected String caregiverFullNameRev;
	
	
	
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getFullNameRevNoSuffix() {
		return fullNameRevNoSuffix;
	}



	public void setFullNameRevNoSuffix(String fullNameRevNoSuffix) {
		
		this.fullNameRevNoSuffix = fullNameRevNoSuffix;
		//hack to deal with bit columns not being groupable in MS SQL Server 7...
		if(this.fullNameRevNoSuffix.contains("(Deceased)")){
			this.setDeceased(true);
		}else{
			this.setDeceased(false);
		}
	}



	public Date getBirthDate() {
		return birthDate;
	}



	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}



	public Boolean getDeceased() {
		return deceased;
	}



	public void setDeceased(Boolean deceased) {
		this.deceased = deceased;
	}



	public Date getDeathDate() {
		return deathDate;
	}



	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
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



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getCaregiverFullNameRev() {
		return caregiverFullNameRev;
	}



	public void setCaregiverFullNameRev(String caregiverFullNameRev) {
		this.caregiverFullNameRev = caregiverFullNameRev;
	}

	


	
}
