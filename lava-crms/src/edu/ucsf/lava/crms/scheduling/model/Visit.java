package edu.ucsf.lava.crms.scheduling.model;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;

import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;

public class Visit extends CrmsEntity {
	public static EntityManager MANAGER = new EntityBase.Manager(Visit.class);
	
	private Patient patient;
	private String projName;
	private String visitLocation;
	private String visitType;
	private String visitWith;
	private Date visitDate;
	private Time visitTime;
	private String visitStatus;
	private String visitNote;
	private String followUpMonth; // 3 char 
	private String followUpYear; // 4 digit
	private String followUpNote;
	private String waitList;
	private String waitListNote;
	private Date waitListDate;
	private String visitDescrip; // computed
	private Short ageAtVisit; //set by trigger


    public static final String CLINIC_CALENDAR_DATE = "clinicCalendarDate"; 
	public static final String CLINIC_CALENDAR_RANGE = "clinicCalendarRange";
	
	public Visit() {
		super();

		this.setProjectAuth(true);
	}
	public Object[] getAssociationsToInitialize(String method) {
		return new Object[]{this.patient};
	}
	
	public Patient getPatient() {return this.patient;}
	public void setPatient(Patient patient) {this.patient = patient;}
	
	public String getProjName() {return this.projName;}
	public void setProjName(String projName) {this.projName = projName;}
	
	public String getVisitLocation() {return this.visitLocation;}
	public void setVisitLocation(String visitLocation) {this.visitLocation = visitLocation;}
	
	public String getVisitType() {return this.visitType;}
	public void setVisitType(String visitType) {this.visitType = visitType;}
	
	public String getVisitWith() {return this.visitWith;}
	public void setVisitWith(String visitWith) {this.visitWith = visitWith;}

	
	

	public Date getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}
	public Time getVisitTime() {
		return visitTime;
	}
	public void setVisitTime(Time visitTime) {
		this.visitTime = visitTime;
	}
	public int getWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.visitDate);
		//c.computeFields(); // not sure if this is necessary or not here
		return c.get(Calendar.WEEK_OF_YEAR); 
	}
	
	public int getWeekDay() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.visitDate);
		//c.computeFields(); // not sure if this is necessary or not here
		return c.get(Calendar.DAY_OF_WEEK); 
	}
	
	public int getDayOfMonth() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.visitDate);
		//c.computeFields(); // not sure if this is necessary or not here
		return c.get(Calendar.DAY_OF_MONTH); 
	}
	
	public int getMonthOfYear() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.visitDate);
		//c.computeFields(); // not sure if this is necessary or not here
		return c.get(Calendar.MONTH) + 1; // add 1, as 0 based 
	}
	
	public int getYear() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.visitDate);
		//c.computeFields(); // not sure if this is necessary or not here
		return c.get(Calendar.YEAR); 
	}
	
	public int getHour() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.visitTime);
		//c.computeFields(); // not sure if this is necessary or not here
		return c.get(Calendar.HOUR_OF_DAY); 
	}
	
	public int getMinute() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.visitTime);
		//c.computeFields(); // not sure if this is necessary or not here
		return c.get(Calendar.MINUTE); 
	}
	
	
	public String getVisitStatus() {return this.visitStatus;}
	public void setVisitStatus(String visitStatus) {this.visitStatus = visitStatus;}

	public String getVisitNote() {return this.visitNote;}
	public void setVisitNote(String visitNote) {this.visitNote = visitNote;}

	public String getFollowUpMonth() {return this.followUpMonth;}
	public void setFollowUpMonth(String followUpMonth) {this.followUpMonth = followUpMonth;}

	public String getFollowUpYear() {return this.followUpYear;}
	public void setFollowUpYear(String followUpYear) {this.followUpYear = followUpYear;}

	public String getFollowUpNote() {return this.followUpNote;}
	public void setFollowUpNote(String followUpNote) {this.followUpNote = followUpNote;}
	
	public String getWaitList() {return this.waitList;}
	public void setWaitList(String waitList) {this.waitList = waitList;}

	public String getWaitListNote() {return this.waitListNote;}
	public void setWaitListNote(String waitListNote) {this.waitListNote = waitListNote;}

	public Date getWaitListDate() {return this.waitListDate;}
	public void setWaitListDate(Date waitListDate) {this.waitListDate = waitListDate;}
	
	public String getVisitDescrip() {return this.visitDescrip;}
	public void setVisitDescrip(String visitDescrip) {this.visitDescrip = visitDescrip;}

	public Short getAgeAtVisit() {return this.ageAtVisit;}
	public void setAgeAtVisit(Short ageAtVisit) {this.ageAtVisit = ageAtVisit;}
	

	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateAgeAtVisit();
		updateVisitDescrip();
		
	}
	
	
	public void updateAgeAtVisit(){
		
		if(patient==null){ setAgeAtVisit(null);}
			
		Integer calcAge = calcAge(patient.getBirthDate(),getVisitDate());
		setAgeAtVisit((calcAge==null)?null:calcAge.shortValue());
	}
	
	public void updateVisitDescrip(){
		StringBuffer buffer = new StringBuffer();
		Date vDate = getVisitDate();
		if(vDate!=null){
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			buffer.append(dateFormat.format(vDate));
		}
		buffer.append(" (");
		String project = getProjName();
		if(project != null){
			buffer.append(project).append("-");
		}
		String type = getVisitType();
		if(type!=null){
			buffer.append(type);
		}
		buffer.append(")");
		setVisitDescrip(buffer.toString());
	}
	
	
	/** clear
	 * 
	 *  Clear every field. Typically used to clear fields in a command object before the
	 *  model is sent to the view, e.g. when adding Visits repeatedly on same view.
	 */
	public void clear() {
		id = null;
		patient = null;
		projName = null;
		visitLocation = null;
		visitType = null;
		visitWith = null;
		visitDate = null;
		visitTime = null;
		visitStatus = null;
		visitNote = null;
		followUpMonth = null;
		followUpYear = null;
		followUpNote = null;
		waitList = null;
		waitListNote = null;
		waitListDate = null;
		visitDescrip = null;
		ageAtVisit = null;
	}
	
	public static List<Visit> findByDateRange(LavaDaoFilter filter) {
		filter.setAlias("patient","patient");
		filter.addDefaultSort("visitDate",false);
		filter.addDefaultSort("visitTime",false);
		return MANAGER.get(filter);
		}
	
	public static List<Visit> findByPatient(LavaDaoFilter filter) {
		filter.setAlias("patient","patient");
		filter.addDefaultSort("visitDate",false);
		filter.addDefaultSort("visitTime",false);
		return MANAGER.get(filter);
		}
	

	
	
	public boolean hasInstruments(){
		LavaDaoFilter filter = this.newFilterInstance();
		filter.setAlias("visit", "visit");
		filter.addDaoParam(filter.daoEqualityParam("visit.id",getId()));
		
		if(InstrumentTracking.MANAGER.getResultCount(filter) > 0){
				return true;
		}
		return false;
	}
	
	
}
