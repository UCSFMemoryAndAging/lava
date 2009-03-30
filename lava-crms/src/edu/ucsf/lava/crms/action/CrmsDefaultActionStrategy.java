package edu.ucsf.lava.crms.action;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionRegistry;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.CoreDefaultActionStrategy;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;
import static edu.ucsf.lava.crms.action.CrmsActionDelegate.DEFAULT_PATIENT_ACTION;



/**
 * Implementation of the DefaultActionStrategy for the crms scope.  The crms scope 
 * has a primary action context of "current patient" (i.e. the presense or absense of 
 * a current patient determines what action is default for any given module or section. 
 * This subclasses the core scope strategy implementation so that is can defer 
 * to the core implementation logic when there is no current patient.   
 * @author jhesse
 *
 */
public class CrmsDefaultActionStrategy extends CoreDefaultActionStrategy {
	
	SessionManager sessionManager;
	
	public Action getDefaultAction(ActionManager actionManager, HttpServletRequest request, Action actionIn) {
		Patient patient = CrmsSessionUtils.getCurrentPatient(getSessionManager(), request);
		if(null != patient){
			Action defaultAction = super.getDefaultAction(actionManager, request, actionIn, DEFAULT_PATIENT_ACTION);
			defaultAction.setParam("id", patient.getId().toString());
			return(defaultAction);
		}
		return super.getDefaultAction(actionManager, request, actionIn);
	}

	
	
	protected SessionManager getSessionManager(){
		if(sessionManager!=null){return sessionManager;}
		sessionManager = CoreManagerUtils.getSessionManager();
		return sessionManager;
	}
	
}
