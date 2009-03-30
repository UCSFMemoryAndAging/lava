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
	private Date consentDate;
	private Date expirationDate;
	private Date withdrawlDate;
	private String note;
	private String capacityReviewBy;
	private String consentRevision;
	private String consentDeclined;
	private String research;
	private String neuro;
	private String dna;
	private String genetic;
	private String geneticShare;
	private String lumbar;
	private String video;
	private String audio;
	private String mediaEdu;
	private String t1_5mri;
	private String t4mri;
	private String otherStudy;
	private String followup;
	private String music;
	private String part;
	private String carepart;
	
	
	
	public Consent(){
		super();
		this.setProjectAuth(true);
		this.setConsentDate(dateWithoutTime(new Date()));
	}
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}
	
	public String getAudio() {
		return audio;
	}
	public void setAudio(String audio) {
		this.audio = audio;
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
	public String getCarepart() {
		return carepart;
	}
	public void setCarepart(String carepart) {
		this.carepart = carepart;
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
	public String getDna() {
		return dna;
	}
	public void setDna(String dna) {
		this.dna = dna;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public String getFollowup() {
		return followup;
	}
	public void setFollowup(String followup) {
		this.followup = followup;
	}
	public String getGenetic() {
		return genetic;
	}
	public void setGenetic(String genetic) {
		this.genetic = genetic;
	}
	public String getGeneticShare() {
		return geneticShare;
	}
	public void setGeneticShare(String geneticShare) {
		this.geneticShare = geneticShare;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLumbar() {
		return lumbar;
	}
	public void setLumbar(String lumbar) {
		this.lumbar = lumbar;
	}
	public String getMediaEdu() {
		return mediaEdu;
	}
	public void setMediaEdu(String mediaEdu) {
		this.mediaEdu = mediaEdu;
	}
	public String getMusic() {
		return music;
	}
	public void setMusic(String music) {
		this.music = music;
	}
	public String getNeuro() {
		return neuro;
	}
	public void setNeuro(String neuro) {
		this.neuro = neuro;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getOtherStudy() {
		return otherStudy;
	}
	public void setOtherStudy(String otherStudy) {
		this.otherStudy = otherStudy;
	}
	public String getPart() {
		return part;
	}
	public void setPart(String part) {
		this.part = part;
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
	public String getResearch() {
		return research;
	}
	public void setResearch(String research) {
		this.research = research;
	}
	public String getT1_5mri() {
		return t1_5mri;
	}
	public void setT1_5mri(String t1_5mri) {
		this.t1_5mri = t1_5mri;
	}
	public String getT4mri() {
		return t4mri;
	}
	public void setT4mri(String t4mri) {
		this.t4mri = t4mri;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
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
	





}
