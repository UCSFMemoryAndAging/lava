package edu.ucsf.lava.crms.file.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.auth.model.CrmsAuthEntity;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;


public class CrmsFile extends LavaFile implements CrmsAuthEntity{
	
	public static LavaFile.Manager MANAGER = new LavaFile.Manager(CrmsFile.class);
	
	private Patient patient;
	private Visit visit;
	private EnrollmentStatus enrollmentStatus;
	private InstrumentTracking instrumentTracking;
	private Long visitId;
	private Long pidn;
	private Long instrId;
	private Long enrollStatId;
	
	protected boolean patientAuth;
	protected boolean projectAuth;
	
	public CrmsFile() {
		super();
		this.setPatientAuth(true);
		this.setProjectAuth(true);
	}
	
	public String getProjName(){
		if(this.enrollmentStatus!=null){
			return this.enrollmentStatus.getProjName();
		}else{
			return new String();
		}
		
	}
	
	public Patient getPatient() {
		return patient;
	}
	
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public Visit getVisit() {
		return visit;
	}
	public void setVisit(Visit visit) {
		this.visit = visit;
	}
	public EnrollmentStatus getEnrollmentStatus() {
		return enrollmentStatus;
	}
	public void setEnrollmentStatus(EnrollmentStatus enrollmentStatus) {
		this.enrollmentStatus = enrollmentStatus;
	}
	public InstrumentTracking getInstrumentTracking() {
		return instrumentTracking;
	}
	public void setInstrumentTracking(InstrumentTracking instrumentTracking) {
		this.instrumentTracking = instrumentTracking;
	}
	public Long getVisitId() {
		return visitId;
	}
	public void setVisitId(Long visitId) {
		this.visitId = visitId;
	}
	public Long getPidn() {
		return pidn;
	}
	public void setPidn(Long pidn) {
		this.pidn = pidn;
	}
	public Long getInstrId() {
		return instrId;
	}
	public void setInstrId(Long instrId) {
		this.instrId = instrId;
	}
	public Long getEnrollStatId() {
		return enrollStatId;
	}
	public void setEnrollStatId(Long enrollStatId) {
		this.enrollStatId = enrollStatId;
	}
	


	public boolean getPatientAuth() {
		return patientAuth;
	}


	public void setPatientAuth(boolean patientAuth) {
		this.patientAuth = patientAuth;
	}


	public boolean getProjectAuth() {
		return projectAuth;
	}


	public void setProjectAuth(boolean projectAuth) {
		this.projectAuth = projectAuth;
	}


	public String getAssociationBlock(){
		StringBuffer buffer = new StringBuffer();
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
		
		if(this.instrumentTracking!=null){
			buffer.append("Instr:  ").append(this.instrumentTracking.getInstrType())
				.append(" - ").append(dateFormat.format(this.instrumentTracking.getDcDate())).append("\n");
		}
		if(this.visit!=null){
			buffer.append("Visit: ").append(getVisitDesc()).append("\n");
		}
		if(this.enrollmentStatus!=null){
			buffer.append("Project: ").append(this.enrollmentStatus.getProjName()).append("\n");
		}
		return buffer.toString();
	}
	
	public String getVisitDesc() {
		DateFormat visitDateFormat = new SimpleDateFormat("MM/dd/yy");
		if (this.visit != null){
			StringBuffer buffer = new StringBuffer(visit.getVisitType()).append(" - ")
				.append(visitDateFormat.format(visit.getVisitDate()));
			return buffer.toString();
		}else{
			return new String();
		}
	}
	
}
