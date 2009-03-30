package edu.ucsf.lava.crms.enrollment;

import java.util.Map;

import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;

/**
 * @author jhesse
 *
 */
public class EnrollmentManager extends LavaManager  {
	public static String ENROLLMENT_MANAGER_NAME="enrollmentManager";
	
	protected Map<String,EnrollmentStatus> enrollmentStatusPrototypes;
	protected EnrollmentStatus defaultEnrollmentStatusPrototype;
	
	public EnrollmentManager(){
		super(ENROLLMENT_MANAGER_NAME);
	}
	
	/* 
	 * gets an EnrollmentStatus prototype based on the project name specified
	 */
	public EnrollmentStatus getEnrollmentStatusPrototype(String projName) {
		EnrollmentStatus prototype = getDefaultEnrollmentStatusPrototype(); 
		
		if(projName == null || enrollmentStatusPrototypes==null){
			return (EnrollmentStatus)prototype.deepClone();
		}
		
		//check for exact match in projName
		if(enrollmentStatusPrototypes.containsKey(projName)){
			return (EnrollmentStatus)enrollmentStatusPrototypes.get(projName).deepClone();
		}
		//check for a project only match if the projName is in "project [Unit]" format
		if (projName.contains("[")){
			String projectNoUnit = projName.substring(0, projName.indexOf("[")).trim();
			if(enrollmentStatusPrototypes.containsKey(projectNoUnit)){
				return (EnrollmentStatus)enrollmentStatusPrototypes.get(projectNoUnit).deepClone();
				
			}
		}
		return (EnrollmentStatus) prototype.deepClone();
		
	}
		

	public void setEnrollmentStatusPrototypes(
			Map<String, EnrollmentStatus> projectEnrollmentStatuses) {
		this.enrollmentStatusPrototypes = projectEnrollmentStatuses;
	}

	public EnrollmentStatus getDefaultEnrollmentStatusPrototype() {
		if(defaultEnrollmentStatusPrototype==null){
			this.defaultEnrollmentStatusPrototype=(EnrollmentStatus)EnrollmentStatus.MANAGER.create();
		}
		return defaultEnrollmentStatusPrototype;
	}

	public void setDefaultEnrollmentStatusPrototype(
			EnrollmentStatus enrollmentStatusPrototype) {
		this.defaultEnrollmentStatusPrototype = enrollmentStatusPrototype;
	}

	
	
	
	
}
