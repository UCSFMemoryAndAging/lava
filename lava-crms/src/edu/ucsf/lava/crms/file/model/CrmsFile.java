package edu.ucsf.lava.crms.file.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.auth.model.CrmsAuthEntity;
import edu.ucsf.lava.crms.enrollment.model.Consent;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;


public class CrmsFile extends LavaFile implements CrmsAuthEntity{
	
	public static LavaFile.Manager MANAGER = new LavaFile.Manager(CrmsFile.class);
	
	private Patient patient;
	private String projName;
	// this is a fixed value populated during Add Attachment as:
	// for Consent attachment consentType
	// for Visit attachment visitType
	// for instrument attachment instrType
	private String entityType;
	private Visit visit;
	private EnrollmentStatus enrollmentStatus;
	private Consent consent;
	private InstrumentTracking instrumentTracking;
	private Long visitId;
	private Long pidn;
	private Long instrId;
	private Long enrollStatId;
	private Long consentId;
	
	protected boolean patientAuth;
	protected boolean projectAuth;
	
	public CrmsFile() {
		super();
		this.setPatientAuth(true);
		// can have files attached to Patient outside the scope of any Project
		this.setProjectAuth(false);
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

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
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
	public Consent getConsent() {
		return consent;
	}
	public void setConsent(Consent consent) {
		this.consent = consent;
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
	public Long getConsentId() {
		return consentId;
	}
	public void setConsentId(Long consentId) {
		this.consentId = consentId;
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

	public Object[] getAssociationsToInitialize(String method) {
		if (this.getInstrumentTracking() != null) {
			return new Object[]{this.getInstrumentTracking().getVisit()};
		}
		return new Object[]{};
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
