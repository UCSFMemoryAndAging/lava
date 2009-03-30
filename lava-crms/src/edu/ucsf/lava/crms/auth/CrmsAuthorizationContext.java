package edu.ucsf.lava.crms.auth;

import edu.ucsf.lava.core.auth.AuthorizationContext;
import edu.ucsf.lava.core.scope.AbstractScopeAuthorizationContext;
import edu.ucsf.lava.crms.enrollment.ProjectUnitUtils;
import edu.ucsf.lava.crms.project.model.Project;



/**
 * The authorization context for the crms scope is based on 
 * enrollment status records that associate a specific patient entity
 * witha a project.  The project is defined as a "project [unit]" where project is 
 * the project identifier, and the [unit] can be used to segment
 * data into distinct project sites or coordination teams. 
 *  
 * @author jhesse
 *
 */
public class CrmsAuthorizationContext extends AbstractScopeAuthorizationContext {
	
	public static final String CRMS_SCOPE = "crms";
	public static final String CRMS_PROJECT_CONTEXT = "project";
	public static final String CRMS_UNIT_CONTEXT = "unit";
	
	
	/**
	 * Default constructor, sets up contexts with the default
	 * wildcards. 
	 *
	 */
	public CrmsAuthorizationContext(){
		super(CRMS_SCOPE);
		this.addContextKey(CRMS_PROJECT_CONTEXT);
		this.addContextKey(CRMS_UNIT_CONTEXT);
	}
	
	/**
	 * Constructor that sets the project/unit context
	 * @param project
	 * @param unit
	 */
	public CrmsAuthorizationContext(String project, String unit){
		this();
		this.setProject(project);
		this.setUnit(unit);
	}
	/**
	 * Convenience constructor that takes a Project entity
	 * @param project
	 */
	public CrmsAuthorizationContext(Project project){
			this(project==null? null : project.getProject(),
				 project==null? null : project.getUnit());
		}
	
	/**
	 * Convenience constructor that takes a project unit description
	 * @param projUnitDesc
	 */
	public CrmsAuthorizationContext(String projUnitDesc){
		this(ProjectUnitUtils.getProject(projUnitDesc),
			 ProjectUnitUtils.getUnit(projUnitDesc));
	}
	
	
	//CRMS Action Authorization Contexts
	
	/**
	 * Get the value of the Project action authorization context. 
	 */
	public String getProject() {
		return (String)getContextValue(CRMS_PROJECT_CONTEXT);
	}

	/**
	 * Set the value of the Project action authorization context. 
	 */
	public void setProject(String project) {
		setContextValue(CRMS_PROJECT_CONTEXT,project);
	}

	/**
	 * Get the value of the Unit action authorization context. 
	 */
	public String getUnit() {
		return (String)getContextValue(CRMS_UNIT_CONTEXT);
	}

	/**
	 * Set the value of the Unit action authorization context. 
	 */
	public void setUnit(String unit) {
		setContextValue(CRMS_UNIT_CONTEXT,unit);
	}

	/*
	 * Get the project and unit values in ProjUnitDesc format (i.e. 'Project [Unit]')
	 */
	public String getProjUnitDesc(){
		return ProjectUnitUtils.getDescription(getProject(), getUnit());
	}
	
	/**
	 * Override matches to take advantage of the utility matches function
	 * on the ProjectUnitUtils class. 
	 */
	public boolean matches(AuthorizationContext matchContext) {
		if(CrmsAuthorizationContext.class.isAssignableFrom(matchContext.getClass())){
			return ProjectUnitUtils.matches(getProjUnitDesc(),
					((CrmsAuthorizationContext)matchContext).getProjUnitDesc());
		}
		return false;
	}

	

	

	
	
}
