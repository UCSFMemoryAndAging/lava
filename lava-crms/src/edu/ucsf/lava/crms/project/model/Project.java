package edu.ucsf.lava.crms.project.model;

import java.util.Date;
import java.sql.Timestamp;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.dao.CrmsDaoFilterUtils;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;

public class Project extends CrmsEntity {

	public static EntityManager MANAGER = new EntityBase.Manager(Project.class);
	
	private String project;
	private String unit;
	private String status;
	private Timestamp effectiveDate;
	private Timestamp expirationDate;
	private String projUnitDesc; // readonly field which is the concatenation of project and unit via a db trigger

	
	public Project() {
		super();
		this.setPatientAuth(false);
		this.setEffectiveDate(new Timestamp(dateWithoutTime(new Date()).getTime()));
	}
	
	

	public String getProject() {
		return project;
	}
	
	public void setProject(String project) {
		this.project = project;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Timestamp getEffectiveDate() {
		return effectiveDate;
	}
	
	public void setEffectiveDate(Timestamp effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	
	public Timestamp getExpirationDate() {
		return expirationDate;
	}
	
	public void setExpirationDate(Timestamp expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getProjUnitDesc() {
		return this.projUnitDesc;
	}

	public void setProjUnitDesc(String projUnitDesc) {
		this.projUnitDesc = projUnitDesc;
	}
	// this method maintains backwards compatibility 
	public String getName() {
		return this.projUnitDesc;
	}
	
	public String getProjectUrlEncoded() {
		return project.replaceAll("[^a-zA-Z]","").toLowerCase();
	}

	public String getUnitUrlEncoded() {
		if (unit == null) {
			return null;
		}
		else {
			return unit.replaceAll("[^a-zA-Z]","").toLowerCase();
		}
	}

	
	public void updateCalculatedFields(){
		super.updateCalculatedFields();
		updateProjUnitDesc();
	}
	
	public void updateProjUnitDesc(){
		StringBuffer buffer = new StringBuffer(getProject());
		if(getUnit()!=null){
			buffer.append(" [").append(getUnit()).append("]");
		}
		setProjUnitDesc(buffer.toString());
	}
	
	public boolean isPatientAssociatedWithProject(Patient patient){
		LavaDaoFilter filter = MANAGER.newFilterInstance();
		CrmsDaoFilterUtils.setProjectContext(filter,this);
		CrmsDaoFilterUtils.setPatientContext(filter,patient);
		if (0 < EnrollmentStatus.MANAGER.getResultCount(filter)){
			return true;
		}else{
			return false;
		}
		
	}

}
