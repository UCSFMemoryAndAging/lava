package edu.ucsf.lava.crms.auth;

import java.util.List;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.AuthRolePermissionCache;
import edu.ucsf.lava.core.auth.AuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.scope.AbstractScopeAuthorizationDelegate;
import edu.ucsf.lava.crms.auth.model.CrmsAuthEntity;
import edu.ucsf.lava.crms.enrollment.ProjectUnitUtils;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.model.CrmsEntity;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class CrmsAuthorizationDelegate extends
		AbstractScopeAuthorizationDelegate {

	public CrmsAuthorizationDelegate(){
		super();
		this.handledScope = CrmsSessionUtils.CRMS_SCOPE;
		
	}
	
	
	public AuthorizationContext newMatchesAllAuthorizationContext() {
		CrmsAuthorizationContext authContext = (CrmsAuthorizationContext)newAuthorizationContext();
		authContext.setProject((String)authContext.getContextWildcard(authContext.CRMS_PROJECT_CONTEXT));
		authContext.setUnit((String)authContext.getContextWildcard(authContext.CRMS_UNIT_CONTEXT));
		return authContext;
		}
	
	

	public AuthorizationContext newAuthorizationContext() {
		return new CrmsAuthorizationContext();
	}


	/**
	 * Return null.  The process of getting Authorization context from a CrmsEntity is too
	 * complex to be supported by this methods call signature that returns a single AuthorizationContext. 
	 * 
	 * The default implementation of isAuthorized(AuthRolePermissionCache roleCache, AuthUser user, Action action, LavaEntity entity)
	 * was overridden instead. 
	 */
	public AuthorizationContext newAuthorizationContext(LavaEntity entity) {
		return null;
	}

	protected AuthorizationContext newAuthorizationContext(String projectName) {
		CrmsAuthorizationContext authContext = (CrmsAuthorizationContext)newAuthorizationContext();
		authContext.setProject(ProjectUnitUtils.getProject(projectName));
		authContext.setUnit(ProjectUnitUtils.getUnit(projectName));
		return authContext;
	}



	/**
	 * Handle custom CrmsEntity authorization check.  Checks whether the user has permission for the action in 
	 * the specific project/unit context associated with the entity. 
	 */
	public boolean isAuthorized(AuthRolePermissionCache roleCache, AuthUser user, Action action, LavaEntity entity) {
		//if entity is missing, return false;  This method should not be called without an entity.   
		if(entity==null){return false;}
		
		//if entity is not a subclass of CrmsEntity return false, only CrmsEntity objects should be used with crms scope actions authorization checks
		if(!CrmsAuthEntity.class.isAssignableFrom(entity.getClass())){return false;}
		
		CrmsAuthEntity crmsAuthEntity = (CrmsAuthEntity)entity;
		
		//Check project or patient-->projects of the entity for authorization
		
		if(crmsAuthEntity.getProjectAuth() && crmsAuthEntity.getProjName()!=null){
			//if project authorized and projName is set, use this for auth check.
			return this.isAuthorized(roleCache, user, action, newAuthorizationContext(crmsAuthEntity.getProjName()));
			
		}else if(crmsAuthEntity.getPatientAuth()){
			Patient p = (crmsAuthEntity instanceof Patient) ? (Patient)crmsAuthEntity : crmsAuthEntity.getPatient();
			if(p==null){return false;} //if no patient set, then return false.
		
			//if patient auth get enrollment status records and check for authorization on each project
			List enrollmentStatusList = p.getEnrollmentStatus(Patient.newFilterInstance());
			for(Object o: enrollmentStatusList){
				if(this.isAuthorized(roleCache,user,action,newAuthorizationContext(((EnrollmentStatus)o).getProjName()))){
					return true;
				}
			}
			
		}else if(!crmsAuthEntity.getPatientAuth() && !crmsAuthEntity.getProjectAuth()){
			//entity is neither patient or project authorized...see if user has permission for the action on any project
			return this.isAuthorized(roleCache, user, action, newMatchesAllAuthorizationContext());
		}
		
		//if we get here without finding authorization, then it failed.
		return false;
	}
		
		

	

	
	
}
