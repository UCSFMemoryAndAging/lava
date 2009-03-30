package edu.ucsf.lava.crms.auth.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Hibernate;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.crms.dao.CrmsDaoFilterUtils;
import edu.ucsf.lava.crms.enrollment.ProjectUnitUtils;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
/**
 * This class extends the AuthUser to provide additional functionality for 
 * data access filtering based on projects that the user has access to via
 * the user's role assignment.   
 * @author jhesse
 *
 */
public class CrmsAuthUser extends AuthUser{

	public static final String PROJECT_LIST_PLACEHOLDER = "NO_PROJECTS_IN_LIST"; //if no project access, this value will keep SQL statements valid --this term gets added to an in clause and will return no projects
	
	/**
	 * List of projects that the user can access data from.  This list only provides access to 
	 * data that is associated with patients that the user is authorized to access.  That patient
	 * access is rpresented by the patientProjectAccessList.
	 */
	private List<String> projectAccessList;
	/**
	 * Indicates that the user has access to all projects in the system (same access rules as for
	 * the projectAccessList property).
	 */
	private Boolean allProjectAccess;
	/**
	 * List of projects for which the user is granted patient access. As distinct from simple data 
	 * access granted by the projectAccessList. 
	 */
	private List<String> patientProjectAccessList;
	/**
	 * Indeicates that the user has access to patients associated with all projects in the system.
	 */
	private Boolean allPatientProjectAccess;

	
	
	public CrmsAuthUser(){
		super();
	}
	
	/**
	 * Update the project access lists when the effective roles are set.
	 */
	public void setEffectiveRoles(List<AuthUserRole> effectiveRoles) {
		super.setEffectiveRoles(effectiveRoles);
		
		Set<String> projects = CrmsManagerUtils.getProjectManager().getProjects().keySet();
		this.setAllProjectAccess(effectiveRoles);
		this.setProjectAccessLists(effectiveRoles,projects);
		}




public Boolean getAllPatientProjectAccess() {
	return allPatientProjectAccess;
}






public Boolean getAllProjectAccess() {
	return allProjectAccess;
}


/**
 * Determine is any of the effective roles give the user access to all projects and/or
 * access to all patients on all projects
 * @param effectiveRoles
 */

protected void setAllProjectAccess(List<AuthUserRole> effectiveRoles) {
	this.allProjectAccess = false;
	this.allPatientProjectAccess = false;
	
	for (AuthUserRole authUserRole:this.effectiveRoles){
	
		//If the AuthUserRole is a CrmsAuthUserRole
		if(CrmsAuthUserRole.class.isAssignableFrom(authUserRole.getClass())){
			CrmsAuthUserRole crmsAuthUserRole = (CrmsAuthUserRole)authUserRole;
			AuthRole authRole = crmsAuthUserRole.getRole();
			//If the AuthRole is a CrmsAuthRole (should always be the case but worth checking)
			if(CrmsAuthRole.class.isAssignableFrom(authRole.getClass())){
				CrmsAuthRole crmsAuthRole = (CrmsAuthRole)authRole;
				String projUnitDesc = crmsAuthUserRole.getProjUnitDesc();
		
				// if the role is assigned for all projects
				if(ProjectUnitUtils.isProjectUnitWildcard(projUnitDesc)){ 
					this.allProjectAccess = true; //all project access is granted
						
					//if the role grants patient access then all patient project access is also granted
					if(crmsAuthRole.getPatientAccess().equals(CrmsAuthRole.ACCESS)){ 
							this.allPatientProjectAccess = true;
							//if we get here we don't need to check any other roles
							return;
					}
				}
			}
		}
	}
}




public List<String> getProjectAccessList() {
	return projectAccessList;
}




protected void setProjectAccessLists(List<AuthUserRole> effectiveRoles, Set<String> projects) {
	//if all patient project access granted then simply assign the full list of projects to the user
	if(true==allPatientProjectAccess){
		patientProjectAccessList = new ArrayList(projects);
		projectAccessList = patientProjectAccessList;
		return;
	}else if(true==allProjectAccess){
		projectAccessList = new ArrayList(projects);
	}
	
	/* if we get here, then we need to iterate through all roles to determine which provide patient access,
	 * but bases on the allProjectAccess flag we may be able to ignore building the projectAccessList
	 */
		
	HashSet<String> projectAccessSet = new HashSet<String>();
	HashSet<String> patientAccessSet = new HashSet<String>();
	for (AuthUserRole authUserRole:this.effectiveRoles){
		//If the AuthUserRole is a CrmsAuthUserRole
		if(CrmsAuthUserRole.class.isAssignableFrom(authUserRole.getClass())){
			CrmsAuthUserRole crmsAuthUserRole = (CrmsAuthUserRole)authUserRole;
			AuthRole authRole = crmsAuthUserRole.getRole();
			
			//If the AuthRole is a CrmsAuthRole (should always be the case but worth checking)
			if(CrmsAuthRole.class.isAssignableFrom(authRole.getClass())){
				CrmsAuthRole crmsAuthRole = (CrmsAuthRole)authRole;
				String projUnitDesc = crmsAuthUserRole.getProjUnitDesc();
		
				// check each project against the projUnitDesc from the AuthUserRole
				for(String project : projects){
					//if the project matches then add it to the lists as appropriate
					if(ProjectUnitUtils.matches(projUnitDesc, project)){
						if(false==allProjectAccess){
							projectAccessSet.add(project);
						}
						if(false==allPatientProjectAccess && crmsAuthRole.getPatientAccess().equals(CrmsAuthRole.ACCESS)){
							patientAccessSet.add(project);
						}
					}
				}
			}
		}
	}
	
	//set the lists as appropriate based on the "all[x]Access" flags
	if(false==allProjectAccess){
		if(projectAccessSet.size()==0){
			projectAccessSet.add(PROJECT_LIST_PLACEHOLDER);
		}
		this.projectAccessList = new ArrayList(projectAccessSet);
	}
	if(false==allPatientProjectAccess){
		if(patientAccessSet.size()==0){
			patientAccessSet.add(PROJECT_LIST_PLACEHOLDER);
		}
		this.patientProjectAccessList = new ArrayList(patientAccessSet);
	}
}



public List<String> getPatientProjectAccessList() {
	return patientProjectAccessList;
}

public Map<String, Map<String, Object>> getAuthDaoFilters() {
	Map<String,Map<String,Object>> authFilters = super.getAuthDaoFilters();
	
	//always turn on the valid patient filter.  This ensures that the dao only returns records associated with PIDN>0. 
	authFilters.put(CrmsDaoFilterUtils.VALID_PATIENT_FILTER,new HashMap<String,Object>());
	
	//if the user does not have patient access on all projects, add the list of projects that the user has patient access through into the Patient Auth Filter
	if(false==allPatientProjectAccess){
		Map<String,Object> patientAuthParams = new HashMap<String,Object>();
		patientAuthParams.put(CrmsDaoFilterUtils.PATIENT_AUTH_FILTER_PARAM, patientProjectAccessList);
		authFilters.put(CrmsDaoFilterUtils.PATIENT_AUTH_FILTER, patientAuthParams);
	}
	//if the user does not have project access to all projects, add the list of projects that the user has access to into the Project Auth Filter
	if(false==allProjectAccess){
		Map<String,Object> projectAuthParams = new HashMap<String,Object>();
		projectAuthParams.put(CrmsDaoFilterUtils.PROJECT_AUTH_FILTER_PARAM, projectAccessList);
		authFilters.put(CrmsDaoFilterUtils.PROJECT_AUTH_FILTER, projectAuthParams);
	}
	
	return authFilters;
}









}
