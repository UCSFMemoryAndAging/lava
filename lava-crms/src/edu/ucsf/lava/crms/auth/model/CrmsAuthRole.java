package edu.ucsf.lava.crms.auth.model;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.crms.enrollment.ProjectUnitUtils;

public class CrmsAuthRole extends AuthRole {
	
	public static EntityManager MANAGER = new EntityBase.Manager(CrmsAuthRole.class);
	public static final Short ACCESS = new Short((short)1);
	public static final Short NOACCESS = new Short((short)0);
	public static final String PATIENT_ACCESS_DENIED_ROLE = "PATIENT ACCESS DENIED";
	public static final String PROJECT_ACCESS_DENIED_ROLE = "PROJECT ACCESS DENIED";
	
	private Short patientAccess;
	private Short phiAccess;
	private Short ghiAccess;

	
	public CrmsAuthRole(){
		super();
		
	}
	
	public Short getGhiAccess() {
		return ghiAccess;
	}
	public void setGhiAccess(Short ghiAccess) {
		this.ghiAccess = ghiAccess;
	}
	
	public Short getPatientAccess() {
		return patientAccess;
	}
	public void setPatientAccess(Short patientAccess) {
		this.patientAccess = patientAccess;
	}
	public Short getPhiAccess() {
		return phiAccess;
	}
	public void setPhiAccess(Short phiAccess) {
		this.phiAccess = phiAccess;
	}

	public String getSummaryInfo() {
		
		return new StringBuffer(super.getSummaryInfo())
			.append("Patient Access: ").append((ACCESS.equals(getPatientAccess()) ? "Yes":"No"))
			.append("\nPHI access: ").append((ACCESS.equals(getPhiAccess()) ? "Yes":"No"))
			.append("\nGHI access: ").append((ACCESS.equals(getGhiAccess()) ? "Yes":"No")).append("\n").toString();
	}
}
