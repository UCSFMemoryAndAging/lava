package edu.ucsf.lava.crms.importer.model;

import java.util.Date;

import edu.ucsf.lava.core.importer.model.ImportLogMessage;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

public class CrmsImportLogMessage extends ImportLogMessage {
	public static EntityManager MANAGER = new EntityBase.Manager(CrmsImportLogMessage.class);

	private String entityType;
	private Long patientId;
	private String lastName;
	private String firstName;
	private Date birthDate;
	private Long enrollStatId;
	private String projName;
	private Long visitId;
	private Date visitDate;
	private String visitType;
	private Long instrId;
	private String instrType;
	private String instrVer;
	
	public CrmsImportLogMessage(){
		super();
	}

	public CrmsImportLogMessage(String type, Integer lineNum, String message) {
		super(type, lineNum, message);
	}

	public CrmsImportLogMessage(String entityType, Long patientId, String lastName, String firstName, Date birthDate, Long enrollStatId, String projName,
			Long visitId, Date visitDate, String visitType, Long instrId, String instrType, String instrVer){
		this.entityType = entityType;
		this.patientId = patientId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.birthDate = birthDate;
		this.enrollStatId = enrollStatId;
		this.projName = projName;
		this.visitId = visitId;
		this.visitDate = visitDate;
		this.visitType = visitType;
		this.instrId = instrId;
		this.instrType = instrType;
		this.instrVer = instrVer;
	}

	public CrmsImportLogMessage(String type, Integer lineNum, String message, CrmsImportLogMessage crmsInfo){
		super(type, lineNum, message);
		this.entityType = crmsInfo.getEntityType();
		this.patientId = crmsInfo.getPatientId();
		this.lastName = crmsInfo.getLastName();
		this.firstName = crmsInfo.getFirstName();
		this.birthDate = crmsInfo.getBirthDate();
		this.enrollStatId = crmsInfo.getEnrollStatId();
		this.projName = crmsInfo.getProjName();
		this.visitId = crmsInfo.getVisitId();
		this.visitDate = crmsInfo.getVisitDate();
		this.visitType = crmsInfo.getVisitType();
		this.instrId = crmsInfo.getInstrId();
		this.instrType = crmsInfo.getInstrType();
		this.instrVer = crmsInfo.getInstrVer();
	}
	
	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Long getEnrollStatId() {
		return enrollStatId;
	}

	public void setEnrollStatId(Long enrollStatId) {
		this.enrollStatId = enrollStatId;
	}

	public String getProjName() {
		return projName;
	}

	public void setProjName(String projName) {
		this.projName = projName;
	}

	public Long getVisitId() {
		return visitId;
	}

	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Long getInstrId() {
		return instrId;
	}

	public void setInstrId(Long instrId) {
		this.instrId = instrId;
	}

	public String getInstrType() {
		return instrType;
	}

	public void setInstrType(String instrType) {
		this.instrType = instrType;
	}

	public String getInstrVer() {
		return instrVer;
	}

	public void setInstrVer(String instrVer) {
		this.instrVer = instrVer;
	}

}
