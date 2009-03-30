package edu.ucsf.lava.crms.auth.model;

import edu.ucsf.lava.core.auth.AuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityManager;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.auth.CrmsAuthorizationContext;
import edu.ucsf.lava.crms.enrollment.ProjectUnitUtils;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class CrmsAuthUserRole extends AuthUserRole {

	public static EntityManager MANAGER = new EntityBase.Manager(CrmsAuthUserRole.class);
	
	private String project;
	private String unit;

	
	public CrmsAuthUserRole(){
		super();
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
	
	public AuthorizationContext getAuthorizationContext() {
		return new CrmsAuthorizationContext(this.project,this.unit);
	}
	
	public String getProjUnitDesc(){
		return ProjectUnitUtils.getDescription(this.project, this.unit);
	}

	
	public String getSummaryInfo() {
		
		return new StringBuffer(super.getSummaryInfo()).append("Project: ").append(ProjectUnitUtils.getDescription(this.project, this.unit)).append("\n").toString();
	}


	
}
