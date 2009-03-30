package edu.ucsf.lava.crms.session;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.LavaComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.auth.model.CrmsAuthUser;
import edu.ucsf.lava.crms.dao.CrmsDaoFilterUtils;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.model.Project;
import edu.ucsf.lava.crms.scheduling.model.Visit;

/**
 * Utility class that provides strongly types methods for setting crms specific scope session attributes
 * @author jhesse
 *
 */
public class CrmsSessionUtils {
	public static final String CRMS_SCOPE = "crms";
	public static final String CURRENT_PATIENT = "currentPatient";
	public static final String CURRENT_VISIT = "currentVisit";
	public static final String CURRENT_PROJECT = "currentProject";
	public static final String CURRENT_INSTRUMENT = "currentInstrument";
	public static final String PATIENT_ACTIONS = "patientActions";  //actions that resolve to patient context data
	public static final String PROJECT_ACTIONS = "projectActions";  //actions that resolve to project context data
	public static final String INSTRUMENT_CODES_DISPLAY_PREF = "codes";
    public static final String PATIENT_ID_REQUEST_PARAMETER = "pidn";
    public static final String VISIT_ID_REQUEST_PARAMETER = "vid";
    public static final String INSTRUMENT_ID_REQUEST_PARAMETER = "instrid";
    public static final String PATIENT_CONTEXT_SEARCH_RESULTS = "patientContextSearchResults";
    
    
    /**
     * Sets the filter with the current project context. 
     * @param sessionManager
     * @param request
     * @param filter
     * @return
     */
    public static LavaDaoFilter setFilterProjectContext(SessionManager sessionManager, HttpServletRequest request, LavaDaoFilter filter){
    	Project currentProject = getCurrentProject(sessionManager, request);
    	if (currentProject == null) { 
    		CrmsDaoFilterUtils.clearProjectContext(filter);
    	} else {
    		CrmsDaoFilterUtils.setProjectContext(filter, currentProject);
    	}
    	return filter;
    }

    /**
     * Sets the filter with the current patient context.
     * @param sessionManager
     * @param request
     * @param filter
     * @return
     */
    public static LavaDaoFilter setFilterPatientContext(SessionManager sessionManager, HttpServletRequest request, LavaDaoFilter filter){
    	Patient currentPatient = getCurrentPatient(sessionManager,request);
    	if (currentPatient == null) { 
    		CrmsDaoFilterUtils.clearPatientContext(filter);
    	} else {
    		CrmsDaoFilterUtils.setPatientContext(filter, currentPatient);
    	}
    	return filter;
    }
    
    /**
     * Gets the current patient from the session
     * @param sessionManager
     * @param request
     * @return
     */
    public  static Patient getCurrentPatient(SessionManager sessionManager, HttpServletRequest request){
    		return (Patient) sessionManager.getSessionAttribute(request,CURRENT_PATIENT);
    }

    /**
     * Sets the current patient in the session
     * @param sessionManager
     * @param request
     * @param p
     */
    public  static void setCurrentPatient(SessionManager sessionManager, HttpServletRequest request, Patient p){
    		sessionManager.setSessionAttribute(request,CURRENT_PATIENT,p);
    	}

    /**
     * Clears the current patient in the session (equivalent to calling setCurrentPatient with a null parameter)
     * @param sessionManager
     * @param request
     */
    public  static void clearCurrentPatient(SessionManager sessionManager, HttpServletRequest request){
		sessionManager.setSessionAttribute(request,CURRENT_PATIENT,null);
    }

    /**
     * Sets the current patient in the session, given a patient id
     * @param sessionManager
     * @param request
     * @param id
     */
    public  static void setCurrentPatient(SessionManager sessionManager, HttpServletRequest request, Long id){
    	sessionManager.setSessionAttribute(request,CURRENT_PATIENT,id);
    }
    
    /**
     * Gets the current visit from the session
     * @param sessionManager
     * @param request
     * @return
     */
    public  static Visit getCurrentVisit(SessionManager sessionManager, HttpServletRequest request){
    	return (Visit) sessionManager.getSessionAttribute(request,CURRENT_VISIT);
    }
    
    /**
     * Sets the current visit in the session, given a visit id
     * @param sessionManager
     * @param request
     * @param id
     */
    public static void setCurrentVisit(SessionManager sessionManager, HttpServletRequest request, Long id){
  		sessionManager.setSessionAttribute(request,CURRENT_VISIT,id);
  	}
    
    /**
     * Sets the current visit in the session
     * @param sessionManager
     * @param request
     * @param v
     */
    public  static void setCurrentVisit(SessionManager sessionManager, HttpServletRequest request, Visit v){
    		sessionManager.setSessionAttribute(request,CURRENT_VISIT,v);
    }

    /**
     * Clears the current visit in the session (equivalent to calling setCurrentVisit with a null parameter)
     * @param sessionManager
     * @param request
     */
    public  static void clearCurrentVisit(SessionManager sessionManager, HttpServletRequest request){
		sessionManager.setSessionAttribute(request,CURRENT_VISIT,null);
    }

    
    /**
     * Gets the current project from the session.
     * @param sessionManager
     * @param request
     * @return
     */
    public  static Project getCurrentProject(SessionManager sessionManager, HttpServletRequest request){
    	return (Project) sessionManager.getSessionAttribute(request,CURRENT_PROJECT);
    }

    /**
     * Sets the current project in the session
     * @param sessionManager
     * @param request
     * @param project
     */
    public  static void setCurrentProject(SessionManager sessionManager, HttpServletRequest request, Project project){
    	sessionManager.setSessionAttribute(request,CURRENT_PROJECT,project);
    }

    /**
     * Sets the current project in the session, given the projectName
     * @param sessionManager
     * @param request
     * @param projectName
     */
    public  static void setCurrentProject(SessionManager sessionManager, HttpServletRequest request, String projectName){
    	sessionManager.setSessionAttribute(request,CURRENT_PROJECT,projectName);
    }


    /**
     * Clears the current project in the session (equivalent to calling setCurrentProject with a null parameter)
     * @param sessionManager
     * @param request
     */
    public  static void clearCurrentProject(SessionManager sessionManager, HttpServletRequest request){
		sessionManager.setSessionAttribute(request,CURRENT_PROJECT,null);
    }

    
    /**
     * Gets the current project name from the session
     * @param sessionManager
     * @param request
     * @return
     */
    public  static String getCurrentProjectName(SessionManager sessionManager, HttpServletRequest request){
    	Project project = getCurrentProject(sessionManager,request);
    	if (project != null) {
    		return project.getName();
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * Gets the current instrument from the session. 
     * @param sessionManager
     * @param request
     * @return
     */
    public  static Instrument getCurrentInstrument(SessionManager sessionManager, HttpServletRequest request){
    	return (Instrument) sessionManager.getSessionAttribute(request,CURRENT_INSTRUMENT);
    }

    /**
     * Sets the current instrument in the session
     * @param sessionManager
     * @param request
     * @param i
     */
    public  static void setCurrentInstrument(SessionManager sessionManager, HttpServletRequest request, Instrument i){
    	sessionManager.setSessionAttribute(request,CURRENT_INSTRUMENT,i);
    	
    }

    /**
     * Sets the current instrument in the session given the instrument id
     * @param sessionManager
     * @param request
     * @param id
     */
    public  static void setCurrentInstrument(SessionManager sessionManager, HttpServletRequest request, Long id){
    	sessionManager.setSessionAttribute(request,CURRENT_INSTRUMENT,id);
    }

    /**
     * Clears the current instrument in the session (equivalent to calling setCurrentInstrument with a null parameter)
     * @param sessionManager
     * @param request
     */
    public  static void clearCurrentInstrument(SessionManager sessionManager, HttpServletRequest request){
		sessionManager.setSessionAttribute(request,CURRENT_INSTRUMENT,null);
    }

    
    /**
     * Crms specific method that returns the current user cast to CrmsAuthUser
     * @param request
     * @return null if the current user is not castable to CrmsAuthUser
     */
    public static CrmsAuthUser getCrmsCurrentUser(SessionManager sessionManager, HttpServletRequest request){
    		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
    		if(CrmsAuthUser.class.isAssignableFrom(user.getClass())){
    			return (CrmsAuthUser)user;
    		}
    		return null;
    		
    }
    
    /**
     * Sets the handler as a patient context handler (i.e. the handler will set the current patient context based
     * on the id that it is loaded with
     */
    public static void setIsPatientContext(LavaComponentHandler handler){
    	handler.setPrimaryComponentContext(CURRENT_PATIENT);
    }
    
    /**
     * Sets the handler as a visit context handler (i.e. the handler will set the current visit context based
     * on the id that it is loaded with
     */
    public static void setIsVisitContext(LavaComponentHandler handler){
    	handler.setPrimaryComponentContext(CURRENT_VISIT);
    }
    
    /**
     * Sets the handler as a instrument context handler (i.e. the handler will set the current instrument context based
     * on the id that it is loaded with
     */
    public static void setIsInstrumentContext(LavaComponentHandler handler){
    	handler.setPrimaryComponentContext(CURRENT_INSTRUMENT);
    }
}   
