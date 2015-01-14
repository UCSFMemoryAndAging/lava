package edu.ucsf.lava.crms.enrollment.model;

import java.util.Date;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;

public class EnrollmentStatus extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(EnrollmentStatus.class);
	
	public static final String REFERRED = "REFERRED";
	public static final String DEFERRED = "DEFERRED";
	public static final String ELIGIBLE = "ELIGIBLE";
	public static final String INELIGIBLE = "INELIGIBLE";
	public static final String DECLINED = "DECLINED";
	public static final String ENROLLED = "ENROLLED";
	public static final String EXCLUDED = "EXCLUDED";
	public static final String WITHDREW = "WITHDREW";
	public static final String INACTIVE = "INACTIVE";
	public static final String DECEASED = "DECEASED";
	public static final String AUTOPSY = "AUTOPSY";
	public static final String CLOSED = "CLOSED";
	
	
	private Patient patient;
	private String projName;
	private String subjectStudyId;
	private String referralSource;
	private String latestDesc;
	private Date latestDate;
	private String latestNote;
	private String referredDesc;
	private Date referredDate;
	private String referredNote;
	private String deferredDesc;
	private Date deferredDate;
	private String deferredNote;
	private String eligibleDesc;
	private Date eligibleDate;
	private String eligibleNote;
	private String ineligibleDesc;
	private Date ineligibleDate;
	private String ineligibleNote;
	private String declinedDesc;
	private Date declinedDate;
	private String declinedNote;
	private String enrolledDesc;
	private Date enrolledDate;
	private String enrolledNote;
	private String excludedDesc;
	private Date excludedDate;
	private String excludedNote;
	private String withdrewDesc;
	private Date withdrewDate;
	private String withdrewNote;
	private String inactiveDesc;
	private Date inactiveDate;
	private String inactiveNote;
	private String deceasedDesc;
	private Date deceasedDate;
	private String deceasedNote;
	private String autopsyDesc;
	private Date autopsyDate;
	private String autopsyNote;
	private String closedDesc;
	private Date closedDate;
	private String closedNote;
	private String enrollmentNotes;

	
	public EnrollmentStatus(){
		super();
		this.setProjectAuth(true);
		//EnrollmentStatus is frequently subclassed and it makes sense to set
		//the auditEntityType here in the default constructor...
		this.setAuditEntityType("EnrollmentStatus");
		
		this.setupDescriptions(REFERRED, DEFERRED, ELIGIBLE, INELIGIBLE, DECLINED,
				ENROLLED, EXCLUDED, WITHDREW, INACTIVE,DECEASED, AUTOPSY,CLOSED);
		
	}

	public void setupDescriptions(String referredDesc,String deferredDesc, String eligibleDesc,
			String ineligibleDesc, String declinedDesc, String enrolledDesc, String excludedDesc,
			String withdrewDesc,String inactiveDesc,String deceasedDesc, String autopsyDesc, String closedDesc) {
		this.referredDesc = referredDesc;
		this.deferredDesc = deferredDesc;
		this.eligibleDesc = eligibleDesc;
		this.ineligibleDesc = ineligibleDesc;
		this.declinedDesc = declinedDesc;
		this.enrolledDesc = enrolledDesc;
		this.excludedDesc = excludedDesc;
		this.withdrewDesc = withdrewDesc;
		this.inactiveDesc = inactiveDesc;
		this.deceasedDesc = deceasedDesc;
		this.autopsyDesc = autopsyDesc;
		this.closedDesc = closedDesc;
	}

	
	
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}
	
	
	

	public Date getAutopsyDate() {
		return autopsyDate;
	}
	public void setAutopsyDate(Date autopsyDate) {
		this.autopsyDate = autopsyDate;
	}
	public String getAutopsyDesc() {
		return autopsyDesc;
	}
	public void setAutopsyDesc(String autopsyDesc) {
		this.autopsyDesc = autopsyDesc;
	}
	public String getAutopsyNote() {
		return autopsyNote;
	}
	public void setAutopsyNote(String autopsyNote) {
		this.autopsyNote = autopsyNote;
	}
	public Date getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(Date closedDate) {
		this.closedDate = closedDate;
	}
	public String getClosedDesc() {
		return closedDesc;
	}
	public void setClosedDesc(String closedDesc) {
		this.closedDesc = closedDesc;
	}
	public String getClosedNote() {
		return closedNote;
	}
	public void setClosedNote(String closedNote) {
		this.closedNote = closedNote;
	}
	public Date getDeceasedDate() {
		return deceasedDate;
	}
	public void setDeceasedDate(Date deceasedDate) {
		this.deceasedDate = deceasedDate;
	}
	public String getDeceasedDesc() {
		return deceasedDesc;
	}
	public void setDeceasedDesc(String deceasedDesc) {
		this.deceasedDesc = deceasedDesc;
	}
	public String getDeceasedNote() {
		return deceasedNote;
	}
	public void setDeceasedNote(String deceasedNote) {
		this.deceasedNote = deceasedNote;
	}
	public Date getDeclinedDate() {
		return declinedDate;
	}
	public void setDeclinedDate(Date declinedDate) {
		this.declinedDate = declinedDate;
	}
	public String getDeclinedDesc() {
		return declinedDesc;
	}
	public void setDeclinedDesc(String declinedDesc) {
		this.declinedDesc = declinedDesc;
	}
	public String getDeclinedNote() {
		return declinedNote;
	}
	public void setDeclinedNote(String declinedNote) {
		this.declinedNote = declinedNote;
	}
	public Date getDeferredDate() {
		return deferredDate;
	}
	public void setDeferredDate(Date deferredDate) {
		this.deferredDate = deferredDate;
	}
	public String getDeferredDesc() {
		return deferredDesc;
	}
	public void setDeferredDesc(String deferredDesc) {
		this.deferredDesc = deferredDesc;
	}
	public String getDeferredNote() {
		return deferredNote;
	}
	public void setDeferredNote(String deferredNote) {
		this.deferredNote = deferredNote;
	}
	public Date getEligibleDate() {
		return eligibleDate;
	}
	public void setEligibleDate(Date eligibleDate) {
		this.eligibleDate = eligibleDate;
	}
	public String getEligibleDesc() {
		return eligibleDesc;
	}
	public void setEligibleDesc(String eligibleDesc) {
		this.eligibleDesc = eligibleDesc;
	}
	public String getEligibleNote() {
		return eligibleNote;
	}
	public void setEligibleNote(String eligibleNote) {
		this.eligibleNote = eligibleNote;
	}
	public Date getEnrolledDate() {
		return enrolledDate;
	}
	public void setEnrolledDate(Date enrolledDate) {
		this.enrolledDate = enrolledDate;
	}
	public String getEnrolledDesc() {
		return enrolledDesc;
	}
	public void setEnrolledDesc(String enrolledDesc) {
		this.enrolledDesc = enrolledDesc;
	}
	public String getEnrolledNote() {
		return enrolledNote;
	}
	public void setEnrolledNote(String enrolledNote) {
		this.enrolledNote = enrolledNote;
	}
	public String getEnrollmentNotes() {
		return enrollmentNotes;
	}
	public void setEnrollmentNotes(String enrollmentNotes) {
		this.enrollmentNotes = enrollmentNotes;
	}

	public Date getExcludedDate() {
		return excludedDate;
	}
	public void setExcludedDate(Date excludedDate) {
		this.excludedDate = excludedDate;
	}
	public String getExcludedDesc() {
		return excludedDesc;
	}
	public void setExcludedDesc(String excludedDesc) {
		this.excludedDesc = excludedDesc;
	}
	public String getExcludedNote() {
		return excludedNote;
	}
	public void setExcludedNote(String excludedNote) {
		this.excludedNote = excludedNote;
	}
	
	public Date getInactiveDate() {
		return inactiveDate;
	}
	public void setInactiveDate(Date inactiveDate) {
		this.inactiveDate = inactiveDate;
	}
	public String getInactiveDesc() {
		return inactiveDesc;
	}
	public void setInactiveDesc(String inactiveDesc) {
		this.inactiveDesc = inactiveDesc;
	}
	public String getInactiveNote() {
		return inactiveNote;
	}
	public void setInactiveNote(String inactiveNote) {
		this.inactiveNote = inactiveNote;
	}
	public Date getIneligibleDate() {
		return ineligibleDate;
	}
	public void setIneligibleDate(Date ineligibleDate) {
		this.ineligibleDate = ineligibleDate;
	}
	public String getIneligibleDesc() {
		return ineligibleDesc;
	}
	public void setIneligibleDesc(String ineligibleDesc) {
		this.ineligibleDesc = ineligibleDesc;
	}
	public String getIneligibleNote() {
		return ineligibleNote;
	}
	public void setIneligibleNote(String ineligibleNote) {
		this.ineligibleNote = ineligibleNote;
	}
	public Date getLatestDate() {
		return latestDate;
	}
	public void setLatestDate(Date latestDate) {
		this.latestDate = latestDate;
	}
	public String getLatestDesc() {
		return latestDesc;
	}
	public void setLatestDesc(String latestDesc) {
		this.latestDesc = latestDesc;
	}
	public String getLatestNote() {
		return latestNote;
	}
	public void setLatestNote(String latestNote) {
		this.latestNote = latestNote;
	}
	public edu.ucsf.lava.crms.people.model.Patient getPatient() {
		return patient;
	}
	public void setPatient(edu.ucsf.lava.crms.people.model.Patient patient) {
		this.patient = patient;
	}
	public String getProjName() {
		return projName;
	}
	public void setProjName(String projName) {
		this.projName = projName;
	}
	public Date getReferredDate() {
		return referredDate;
	}
	public void setReferredDate(Date referredDate) {
		this.referredDate = referredDate;
	}
	public String getReferredDesc() {
		return referredDesc;
	}
	public void setReferredDesc(String referredDesc) {
		this.referredDesc = referredDesc;
	}
	public String getReferredNote() {
		return referredNote;
	}
	public void setReferredNote(String referredNote) {
		this.referredNote = referredNote;
	}
	public Date getWithdrewDate() {
		return withdrewDate;
	}
	public void setWithdrewDate(Date withdrewDate) {
		this.withdrewDate = withdrewDate;
	}
	public String getWithdrewDesc() {
		return withdrewDesc;
	}
	public void setWithdrewDesc(String withdrewDesc) {
		this.withdrewDesc = withdrewDesc;
	}
	public String getWithdrewNote() {
		return withdrewNote;
	}
	public void setWithdrewNote(String withdrewNote) {
		this.withdrewNote = withdrewNote;
	}
	public String getSubjectStudyId() {
		return subjectStudyId;
	}
	public void setSubjectStudyId(String subjectStudyId) {
		this.subjectStudyId = subjectStudyId;
	}

	public String getReferralSource() {
		return referralSource;
	}
	public void setReferralSource(String referralSource) {
		this.referralSource = referralSource;
	}
	
	public boolean setStatus(String statusDesc, Date statusDate){
		return setStatus(statusDesc,statusDate,null);
	}
	
	public boolean setStatus(String statusDesc, Date statusDate, String statusNote){
		if(statusDesc == null){return false;}
		
		if(statusDesc.equalsIgnoreCase(getReferredDesc())){
			setReferredDate(statusDate);
			setReferredNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getDeferredDesc())){
			setDeferredDate(statusDate);
			setDeferredNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getEligibleDesc())){
			setEligibleDate(statusDate);
			setEligibleNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getIneligibleDesc())){
			setIneligibleDate(statusDate);
			setIneligibleNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getDeclinedDesc())){
			setDeclinedDate(statusDate);
			setDeclinedNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getEnrolledDesc())){
			setEnrolledDate(statusDate);
			setEnrolledNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getExcludedDesc())){
			setExcludedDate(statusDate);
			setExcludedNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getWithdrewDesc())){
			setWithdrewDate(statusDate);
			setWithdrewNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getInactiveDesc())){
			setInactiveDate(statusDate);
			setInactiveNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getDeceasedDesc())){
			setDeceasedDate(statusDate);
			setDeceasedNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getAutopsyDesc())){
			setAutopsyDate(statusDate);
			setAutopsyNote(statusNote);
			return true;
		}else if(statusDesc.equalsIgnoreCase(getClosedDesc())){
			setClosedDate(statusDate);
			setClosedNote(statusNote);
			return true;
		}
		return false;
			
	}
	
	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateLatestStatusValues();
	}
	
	
	/**
	 * This routine updates the latest status values based on the latest status date.
	 */
	public void updateLatestStatusValues(){
		String status = null;
		Date statusDate = null;
		String statusNote = null;
		
		if(getClosedDesc() != null && getClosedDate() != null){
			status = getClosedDesc();
			statusDate = getClosedDate();
			statusNote = getClosedNote();
		}
		
		if(getAutopsyDesc() != null && getAutopsyDate() != null){
			if(statusDate==null || statusDate.before(getAutopsyDate())){
				status = getAutopsyDesc();
				statusDate = getAutopsyDate();
				statusNote = getAutopsyNote();
			}
		}
		
		if(getDeceasedDesc() != null && getDeceasedDate() != null){
			if(statusDate==null || statusDate.before(getDeceasedDate())){
					status = getDeceasedDesc();
					statusDate = getDeceasedDate();
					statusNote = getDeceasedNote();
			}
		}
			

		if(getInactiveDesc() != null && getInactiveDate() != null){
			if(statusDate==null || statusDate.before(getInactiveDate())){
					status = getInactiveDesc();
					statusDate = getInactiveDate();
					statusNote = getInactiveNote();
			}
		}
			
		if(getWithdrewDesc() != null && getWithdrewDate() != null){
			if(statusDate==null || statusDate.before(getWithdrewDate())){
					status = getWithdrewDesc();
					statusDate = getWithdrewDate();
					statusNote = getWithdrewNote();
			}
		}
			
		if(getExcludedDesc() != null && getExcludedDate() != null){
			if(statusDate==null || statusDate.before(getExcludedDate())){
					status = getExcludedDesc();
					statusDate = getExcludedDate();
					statusNote = getExcludedNote();
			}
		}
			
		if(getEnrolledDesc() != null && getEnrolledDate() != null){
			if(statusDate==null || statusDate.before(getEnrolledDate())){
					status = getEnrolledDesc();
					statusDate = getEnrolledDate();
					statusNote = getEnrolledNote();
			}
		}
			
		if(getDeclinedDesc() != null && getDeclinedDate() != null){
			if(statusDate==null || statusDate.before(getDeclinedDate())){
					status = getDeclinedDesc();
					statusDate = getDeclinedDate();
					statusNote = getDeclinedNote();
			}
		}
			
		if(getIneligibleDesc() != null && getIneligibleDate() != null){
			if(statusDate==null || statusDate.before(getIneligibleDate())){
					status = getIneligibleDesc();
					statusDate = getIneligibleDate();
					statusNote = getIneligibleNote();
			}
		}
			
		if(getEligibleDesc() != null && getEligibleDate() != null){
			if(statusDate==null || statusDate.before(getEligibleDate())){
					status = getEligibleDesc();
					statusDate = getEligibleDate();
					statusNote = getEligibleNote();
			}
		}
			
		if(getDeferredDesc() != null && getDeferredDate() != null){
			if(statusDate==null || statusDate.before(getDeferredDate())){
					status = getDeferredDesc();
					statusDate = getDeferredDate();
					statusNote = getDeferredNote();
			}
		}
			
		if(getReferredDesc() != null && getReferredDate() != null){
			if(statusDate==null || statusDate.before(getReferredDate())){
					status = getReferredDesc();
					statusDate = getReferredDate();
					statusNote = getReferredNote();
			}
		}
			
		this.setLatestDate(statusDate);
		this.setLatestDesc(status);
		this.setLatestNote(statusNote);
	}

	
	public boolean isLastEnrollmentStatus(){
		Patient p = this.getPatient();
		if(patient==null) {
			return false;
			//TODO: should we throw something to let the programmer know that this is called in error?
			// or does it really not matter..this would only happen if this was called on a newly created enrollmentstatus...
		}
		LavaDaoFilter filter = MANAGER.newFilterInstance();
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", p.getId()));
		
		if (1 == MANAGER.getResultCount(filter)){
			return true;
		}
		return false;
	}
	

	

}
