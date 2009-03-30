package edu.ucsf.lava.crms.people.model;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;



import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.scheduling.model.Visit;

public class Patient extends CrmsEntity {
	
public static final String DEIDENTIFIED = "DE-IDENTIFIED";
	
	public static EntityManager MANAGER = new EntityBase.Manager(Patient.class);
	
	private String lastName;  //set to DE-IDENTIFIED when subject record is de-identified
	private Character middleInitial;
	private Boolean firstTime;  
	private Boolean deidentified;
	private String firstName;  //subjectId is an alias for firstName when deidentified = true
	private String suffix;
	private String degree;
	private Date birthDate; 
	private Integer age; 
	private Byte gender;
	private String hand;
	private Boolean deceased; //db default=0
	private Date deathDate;
	private String primaryLanguage;
	private String testingLanguage;
	private Boolean transNeeded;
	private String transLanguage;
	private String enterBy;
	private Boolean dupNameFlag; //db default=0
	private String fullNameRev; 
	private String fullName; 
	private String fullNameRevNoSuffix; 
	private String fullNameNoSuffix; 

	private Set doctors;
	private Set enrollmentStatus;
	private Set caregivers;
	private Set contactInfo;
	private Set contactLog;
	private Set consent;
	private Set visits;
	private Set instruments;
	private Set tasks;

	
	public Patient() {
		super();
		this.setAuditEntityType("Patient");
		
		// flag for correctly initializing deidentified via lastName
		this.firstTime = Boolean.TRUE;

		// FOR NOW: until business logic is added to determine this, give it a value since
		// underlying column is not null
		dupNameFlag = Boolean.FALSE;
		
		// FOR NOW: until this is input by the user, give it a value since underlying col null
		deceased = Boolean.FALSE;
		
		this.addPropertyToAuditIgnoreList("doctors");
		this.addPropertyToAuditIgnoreList("enrollmentStatus");
		this.addPropertyToAuditIgnoreList("caregivers");
		this.addPropertyToAuditIgnoreList("contactInfo");
		this.addPropertyToAuditIgnoreList("contactLog");
		this.addPropertyToAuditIgnoreList("visits");
		this.addPropertyToAuditIgnoreList("instruments");
		this.addPropertyToAuditIgnoreList("tasks");

		
	}
	
	
	/* Convenience constructor for obtaining query results. */
	public Patient(Long id, byte[] concurrencyTimestamp) {
		this.id = id;
		/*
		this.concurrencyTimestamp = concurrencyTimestamp;
		*/
	}
	
	
	
	


		
	public String getLastName() {return lastName;}
	public void setLastName(String lastName) 
	{
		if(this.firstTime) {
			this.lastName = lastName;
			if (this.lastName.equals(this.DEIDENTIFIED)){
				this.deidentified = Boolean.TRUE;
			}
			else {
				this.deidentified = Boolean.FALSE;
			}
			this.firstTime = Boolean.FALSE;
		}
		else {
			// once initialized, lastName should only be set when not deidentified 
			if (!this.deidentified) {
				this.lastName = lastName;
			}
		}
	}
	
	public Character getMiddleInitial() {return middleInitial;}
	public void setMiddleInitial(Character middleInitial) {this.middleInitial = middleInitial;}
	
	public String getFirstName() {return firstName;}
	public void setFirstName(String firstName) {this.firstName = firstName;}
	
	public String getSuffix() {return suffix;}
	public void setSuffix(String suffix) {this.suffix = suffix;}
	
	public String getDegree() {return degree;}
	public void setDegree(String degree) {this.degree = degree;}
	

	
	public Date getBirthDate() {return birthDate;}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
		trackDirty("birthDate",(birthDate==null) ? null : new Timestamp(birthDate.getTime()));
		}
	
	public Integer getAge() {return age;}
	public void setAge(Integer age) {this.age = age;}
	
	public Byte getGender() {return gender;}
	public void setGender(Byte gender) {this.gender = gender;}
	
	public String getHand() {return hand;}
	public void setHand(String hand) {this.hand = hand;}
	
	public Boolean getDeceased() {return deceased;}
	public void setDeceased(Boolean deceased) {this.deceased = deceased;}
	
	public Date getDeathDate() {return deathDate;}
	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
		trackDirty("deathDate",(deathDate==null) ? null : new Timestamp(deathDate.getTime()));
	}
	
	
	
	public String getEnterBy() {return enterBy;}
	public void setEnterBy(String enterBy) {this.enterBy = enterBy;}
	
	public Boolean getDupNameFlag() {return dupNameFlag;}
	public void setDupNameFlag(Boolean dupNameFlag) {this.dupNameFlag = dupNameFlag;}
	
	public String getFullNameRev() {return fullNameRev;}
	public void setFullNameRev(String fullNameRev) {
		this.fullNameRev = fullNameRev;
		this.trackDirty("fullNameRev", fullNameRev);
		}
	
	public String getFullName() {return fullName;}
	public void setFullName(String fullName) {this.fullName = fullName;}
	
	public String getFullNameWithId() {return fullName + " (" + id.toString()+")";}
	public void setFullNameWithId(String fullName){};
	
	public String getFullNameRevWithId() {return fullNameRev + " (" + id.toString()+")";}
	public void setFullNameRevWithId(String fullName){};
	
	public String getFullNameRevNoSuffix() {return fullNameRevNoSuffix;}
	public void setFullNameRevNoSuffix(String fullNameRevNoSuffix) {this.fullNameRevNoSuffix = fullNameRevNoSuffix;}
	
	public String getFullNameNoSuffix() {return fullNameNoSuffix;}
	public void setFullNameNoSuffix(String fullNameNoSuffix) {this.fullNameNoSuffix = fullNameNoSuffix;}

	
	

	public String getPrimaryLanguage() {
		return primaryLanguage;
	}

	public void setPrimaryLanguage(String primaryLanguage) {
		this.primaryLanguage = primaryLanguage;
	}

	public String getTestingLanguage() {
		return testingLanguage;
	}

	public void setTestingLanguage(String testingLanguage) {
		this.testingLanguage = testingLanguage;
	}

	public String getTransLanguage() {
		return transLanguage;
	}

	public void setTransLanguage(String transLanguage) {
		this.transLanguage = transLanguage;
	}

	public Boolean getTransNeeded() {
		return transNeeded;
	}

	public void setTransNeeded(Boolean transNeeded) {
		this.transNeeded = transNeeded;
	}

	public Boolean getDeidentified() {
		return deidentified;
	}
	
	public void setDeidentified(Boolean deidentified) {
		this.deidentified = deidentified;
		if(this.deidentified){
			this.lastName = this.DEIDENTIFIED;
			this.suffix = null;
			this.degree = null;
			this.middleInitial = null;
		}
	}

	public String getSubjectId() {
		return firstName;
	}

	public void setSubjectId(String subjectId) {
		this.firstName = subjectId;
	}
	
	/**
	 * call save on all visits and instruments to ensure that age at visit and age at DC are 
	 * correctly calculated
	 */
	public boolean afterUpdate(){
		boolean resave = super.afterUpdate();
		if(isDirty("deathDate") || isDirty("birthDate")){
			List<Visit> visits = getVisits(newFilterInstance());
			for (Visit v:visits){
				v.save();
			}
			List<InstrumentTracking> instruments = getInstruments(newFilterInstance());
			for (InstrumentTracking i:instruments){
				i.save();
			}
		}
		if(isDirty("fullNameRev")){
			List<ContactInfo> contactInfo = getContactInfo(newFilterInstance());
			for (ContactInfo ci : contactInfo){
				ci.save(); //updates contact description
			}
		}
		return resave;
	}
	

	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateFullName();
		updateFullNameNoSuffix();
		updateFullNameRev();
		updateFullNameRevNoSuffix();
		updateAge();
	}

	protected void updateAge(){
		setAge(calcAge(getBirthDate(),(getDeathDate()==null) ? new Date(): getDeathDate()));
	}
	
	protected void updateFullName(){
		StringBuffer buffer = new StringBuffer();
		if(getLastName().equals(this.DEIDENTIFIED)){
			buffer.append(getFirstName());
		}else{
			buffer.append(getFirstName()).append(" ");
			if(getMiddleInitial()!=null){
				buffer.append(getMiddleInitial()).append(". ");
			}

			buffer.append(getLastName()).append(" ");
			if(getSuffix()!=null){
				buffer.append(getSuffix()).append(", ");
			}
			if(getDegree()!=null){
				buffer.append(getDegree()).append(" ");
			}
			if(true==getDeceased()){ 
				buffer.append("(Deceased)");
			}
		}
		setFullName(buffer.toString());
	}
	protected void updateFullNameNoSuffix(){
		StringBuffer buffer = new StringBuffer();
		if(getLastName().equals(this.DEIDENTIFIED)){
			buffer.append(getFirstName());
		}else{
			buffer.append(getFirstName()).append(" ");
			if(getMiddleInitial()!=null){
				buffer.append(getMiddleInitial()).append(". ");
			}
			buffer.append(getLastName());
			if(true==getDeceased()){ 
				buffer.append(" (Deceased)");
			}
		}
		setFullNameNoSuffix(buffer.toString());
	}
	protected void updateFullNameRev(){
		StringBuffer buffer = new StringBuffer();
		if(getLastName().equals(this.DEIDENTIFIED)){
			buffer.append(getFirstName());
		}else{
			buffer.append(getLastName()).append(", ");
			if(getSuffix()!=null){
				buffer.append(getSuffix()).append(", ");
			}
			if(getDegree()!=null){
				buffer.append(getDegree()).append(", ");
			}
			buffer.append(getFirstName()).append(" ");
			if(getMiddleInitial()!=null){
				buffer.append(getMiddleInitial()).append(".");
			}
			if(true==getDeceased()){ 
				buffer.append(" (Deceased)");
			}
		}
		setFullNameRev(buffer.toString());
	}
	protected void updateFullNameRevNoSuffix(){
		StringBuffer buffer = new StringBuffer();
		if(getLastName().equals(this.DEIDENTIFIED)){
			buffer.append(getFirstName());
		}else{
			buffer.append(getLastName()).append(", ");
			buffer.append(getFirstName());
			if(getMiddleInitial()!=null){
				buffer.append(" ").append(getMiddleInitial()).append(".");
			}
			if(true==getDeceased()){ 
				buffer.append(" (Deceased)");
			}
		}
		setFullNameRevNoSuffix(buffer.toString());
	}
	
	/*
	 * These accessor and mutator functions for the association sets are 
	 * declard private because these associations are solely to support
	 * the cascade delete funtionality in hibernate when the patient 
	 * object is deleted.  If you need to access a collection of objects associated with 
	 * the patient, make an explicit call to the appropriate service method to
	 * query the objects.  These persistent sets are mapped as lazy initialitzed and should
	 * never be accessed under a hibenate session scope (this would cause them to be initialized 
	 * and added to the session cache which can easily create NonUniqueObjectExceptions
	 * 
	 */
	
	
	
	private Set getCaregivers() {
		return caregivers;
	}


	private void setCaregivers(Set caregivers) {
		this.caregivers = caregivers;
	}


	private Set getConsent() {
		return consent;
	}


	private void setConsent(Set consent) {
		this.consent = consent;
	}


	private Set getContactInfo() {
		return contactInfo;
	}


	private void setContactInfo(Set contactInfo) {
		this.contactInfo = contactInfo;
	}


	private Set getDoctors() {
		return doctors;
	}


	private void setDoctors(Set doctors) {
		this.doctors = doctors;
	}


	private Set getEnrollmentStatus() {
		return enrollmentStatus;
	}


	private void setEnrollmentStatus(Set enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}


	private Set getInstruments() {
		return instruments;
	}


	private void setInstruments(Set instruments) {
		this.instruments = instruments;
	}


	private Set getVisits() {
		return visits;
	}


	private void setVisits(Set visits) {
		this.visits = visits;
	}


	public Set getContactLog() {
		return contactLog;
	}


	public void setContactLog(Set contactLog) {
		this.contactLog = contactLog;
	}




	public Set getTasks() {
		return tasks;
	}


	public void setTasks(Set tasks) {
		this.tasks = tasks;
	}

	
	public List getDoctors(LavaDaoFilter filter){
			filter.setAlias("patient", "patient");
			filter.addDaoParam(filter.daoEqualityParam("patient.id", this.getId()));
			return PatientDoctor.MANAGER.get(filter);
		}
	
	public List getVisits(LavaDaoFilter filter){
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", this.getId()));
		return Visit.MANAGER.get(filter);
	}
	
	public List getInstruments(LavaDaoFilter filter){
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", this.getId()));
		return InstrumentTracking.MANAGER.get(filter);
	}
	
	public List getContactInfo(LavaDaoFilter filter){
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", this.getId()));
		return ContactInfo.MANAGER.get(filter);
	}
	
	public static List doPatientSearch(String searchBy, LavaDaoFilter filter){
		if(searchBy.equalsIgnoreCase("NameRev")){
				return findByLastName(filter);
		}else if(searchBy.equalsIgnoreCase("Name")){
				return findByName(filter);
		}else if(searchBy.equalsIgnoreCase("PatientId")){
				return findByPatientId(filter);
		}else{ 
				return findByLastName(filter);
		}
		
	}
	
	
	public List getEnrollmentStatus(LavaDaoFilter filter){
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", this.getId()));
		return EnrollmentStatus.MANAGER.get(filter);
	}

	
	
	public static List findByLastName(LavaDaoFilter filter){
		return MANAGER.findByNamedQuery("patient.searchByNameRev",filter);
	}
	public static List findByName(LavaDaoFilter filter){
		return MANAGER.findByNamedQuery("patient.searchByName", filter);
	}
	
	public static List findByPatientId(LavaDaoFilter filter){
		return MANAGER.findByNamedQuery("patient.searchByPatientId", filter);
	}

	
	public static List addPatientLookup(LavaDaoFilter filter){
		return MANAGER.findByNamedQuery("patient.addPatientLookup", filter);
	}
	
	public boolean hasInstruments(){
		LavaDaoFilter filter = newFilterInstance();
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", getId()));
		
		if(InstrumentTracking.MANAGER.getResultCount(filter) > 0){
			return true;
		}
		return false;
	}
	
	public boolean hasVisits(){
		LavaDaoFilter filter = newFilterInstance();
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", this.getId()));
		
		if(Visit.MANAGER.getResultCount(filter) > 0){
			return true;
		}
		return false;
	}
	

}
