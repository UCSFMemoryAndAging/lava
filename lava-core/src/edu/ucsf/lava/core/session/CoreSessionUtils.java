package edu.ucsf.lava.core.session;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.webflow.InterFlowFormErrorParams;

public class CoreSessionUtils {

	public static final String CORE_SCOPE = "core";
	public static final String CURRENT_USER = "currentUser";
	public static final String CURRENT_ACTION = "currentAction";  //the current action
	public static final String INTER_FLOW_FORM_ERRORS = "interFlowFormErrors";
	public static final String ACTIONS = "actions";
 
	public static void setCurrentUser(SessionManager sessionManager,HttpServletRequest request,AuthUser user){
		sessionManager.setSessionAttribute(request, CURRENT_USER, user);
	}
	
	public static AuthUser getCurrentUser(SessionManager sessionManager,HttpServletRequest request){
		return (AuthUser)sessionManager.getSessionAttribute(request, CURRENT_USER);
	}
	

    public  static void clearCurrentUser(SessionManager sessionManager, HttpServletRequest request){
		sessionManager.setSessionAttribute(request,CURRENT_USER,null);
    }

	public static void setCurrentAction(SessionManager sessionManager,HttpServletRequest request,Action action){
		sessionManager.setSessionAttribute(request, CURRENT_ACTION, action);
	}
	
	public static Action getCurrentAction(SessionManager sessionManager,HttpServletRequest request){
		return (Action)sessionManager.getSessionAttribute(request, CURRENT_ACTION);
	}
	

    public  static void clearCurrentAction(SessionManager sessionManager, HttpServletRequest request){
		sessionManager.setSessionAttribute(request,CURRENT_ACTION,null);
    }


	// for object error/info msgs that span flows, such that they must be stored in session scope in one 
	// flow so that they are available to be reported in another flow. e.g. if project context
	// change results in a redirect to a new flow, but there was an error/info message to be
	// reported to the user, the project context handler would call addObjectError to store the
	// data necessary to create the object error. then, in the createFormObject for the new flow,
	// object errors are obtained via getObjectErrors and added to the form errors object, and 
	// then cleared via clearObjectErrors
	
	public static void addFormError(SessionManager sessionManager,HttpServletRequest request, String[] codes, Object[] arguments) {
		List formErrorParamsList = (List) sessionManager.getSessionAttribute(request, INTER_FLOW_FORM_ERRORS);
		if (formErrorParamsList == null) {
			formErrorParamsList = new ArrayList();
			sessionManager.setSessionAttribute(request, INTER_FLOW_FORM_ERRORS, formErrorParamsList);
		}
		formErrorParamsList.add(new InterFlowFormErrorParams(codes, arguments));
	}
	
	public static List getFormErrorParamsList(SessionManager sessionManager,HttpServletRequest request) {
		List formErrorParamsList = (List) sessionManager.getSessionAttribute(request, INTER_FLOW_FORM_ERRORS);
		if (formErrorParamsList == null) {
			formErrorParamsList = new ArrayList();
		}
		return formErrorParamsList;
	}
	
	public static void clearFormErrors(SessionManager sessionManager,HttpServletRequest request) {
		List formErrorParamsList = (List) sessionManager.getSessionAttribute(request, INTER_FLOW_FORM_ERRORS);
		if (formErrorParamsList != null) {
			formErrorParamsList.clear();
		}
	}
}
