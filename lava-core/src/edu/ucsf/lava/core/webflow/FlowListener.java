package edu.ucsf.lava.core.webflow;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;


public class FlowListener extends FlowExecutionListenerAdapter implements ManagersAware {
    public static final String CURRENT_INSTR_TYPE_ENCODED = "instrTypeEncoded";
	

	protected ActionManager actionManager;
	protected SessionManager sessionManager;
	protected MetadataManager metadataManager;
	
	public void eventSignaled(RequestContext context, Event event) {
		// set the event in scope because the context.getLastEvent() returns null in render
		// actions because the event request parameter is lost after POST-REDIRECT-GET.
		// putting the event in flashScope would solve the problem within a given flow,
		// but each flow across a conversation has its own flashScope (and flowScope), so
		// the only place where it will persist when transitioning into and out of
		// subflows is in conversation scope
		context.getConversationScope().put("lastEventId", event.getId());
		
		// if the event is a project context event, i.e. the project context has changed
		// or has been cleared, the current action must be updated because actions can
		// be customized in that they are project specific, and because views are 
		// determined based upon the current action (in CustomViewSelector), so e.g.
		// if the current project is changed to "clinic" and project specific action 
		// clinic.people.patient.patient exists, current action must be updated to 
		// reflect this, so that the view that is selected is:
		// local/clinic/people/patient/patient
		// UPDATE: project and project/unit actions and views are not currently used, but
		// this code was left in, in addition to code in ActionService, to support this functionality 
		// note: if the project context change results in a patient clear (because patient is not associated
		//  with project) then a redirect to an entirely new flow will take place and this update will
		//  be meaningless but that is ok. the current action for the new flow will then be set in 
		//  sessionStarting below
		// note: handle project clear too, so that if the current action currently reflects
		//  a project specific scope, then the scope will be reset
		// note: if the event results in a transition to a different flow which corresponds to 
		//  a different action, this update will be replaced by another update done subsequently
		//  in either sessionStarting (if a new subflow is created) or resumed (if a subflow 
		//  terminates and a parent flow resumes)
		if (event.getId().equals("projectContext__filter") || event.getId().equals("projectContext__clearProject")) {
			this.updateCurrentAction(context, context.getActiveFlow().getId());
		}
	}
	
//TODO: when upgrade to Spring Web Flow 2.x, change this to sessionCreating. this should solve the
//problem where currently the flow scope already exists in sessionStarting (per the javadocs it should not)
//and furthermore, an even bigger potential problem exists, which is that if this is called when starting a 
//child subflow, this flow scope which should not exist yet contains the attributes from the parent flow. 
//since this situation does not exist in our system yet, do not need to upgrade immediately	
	public void sessionStarting(RequestContext context, FlowDefinition definition, MutableAttributeMap input) {
		// the current action is guaranteed to be identical for all states of a flow, since all flows are
		// defined with action mode granularity, i.e. edit flows, view flows, etc.
		// e.g. the current action for flow lava.assessment.instrument.cdr.enter is always
		//   lava.assessment.instrument.cdr for all states of the enter flow: enter,doubleEnter,compare,status,etc.
		
		// therefore, the current action only needs to be set when a flow is starting (or resuming),
		// and when a project context change takes place (and remaining in the same flow), and it will 
		// accurately apply for the lifetime of the flow. this method handles flow starting, the "resumed" 
		// method handles flow resuming, and the "eventSignaled" method handles project context changes 

		// note: formerly, the current action was based upon the request URL. however, with web flows,
		//   it is possible to transition to a different flow (within a flow conversation) that has
		//   a different module/section without changing the request URL (since flow conversations
		//   just rely on the _flowExecutionKey, not the request URL). so the current action would
		//   not be set correctly. instead, base the action id on the active flow id which will always 
		//   correctly reflect the module/section (since a given flow will always operate within a single 
		//   given module/section throughout its lifetime, as opposed to a flow conversation which
		//   assumes the module/section of whichever flow is active)

		this.updateCurrentAction(context, definition.getId());
	}
	
	
	public void resumed(RequestContext context) {
		// update the current action when a flow resumes, as there could be changes from
		// the current action of the subflow, e.g. the module.section portion of the current
		// action may have changed
		this.updateCurrentAction(context, context.getActiveFlow().getId());
	}
	
	// internal helper
	private void updateCurrentAction(RequestContext context, String flowId) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		// set the current action based on the active flow's id.
		
		// the reason why the current action must be derived from the flow id instead of the request URL
		// is as follows: imagine going from scheduling/visit/visitInstruments to the subflow for
		// a specific instrument, e.g. assessment/instrument/cdr, and upon closing the instrument, the 
		// POST request is made to assessment/instrument/cdr, the instrument subflow terminates, and
		// control returns to scheduling/visit/visitInstruments. if the request URL were used to
		// determine the current action at this point, it would incorrectly determine 
		// assessment.instrument.cdr. if the flow id is used to determine the current action at this
		// point, it correctly determines scheduling.visit.visitInstruments.
		
		// one reason this is critical is that the current action is used to visually highlight the 
		// correct module tab and section link
		
		
		// convert the flow id to an action id, possibly with customized scope part
		// e.g. flowId=lava.scheduling.patientCalendar.patientVisits
		//      actionId=SCOPE.scheduling.patientCalendar.patientVisits
		// also, get the flowId mode part to set as an Action parameter
		StringBuffer flowIdModePart = new StringBuffer();
		// get the default scope flow id because later it will be passed to ActionService getAction
		// which will convert it to its project or instance scope if it should
		String flowActionId = ActionUtils.getDefaultActionIdFromFlowId(flowId, flowIdModePart);
		
		// call getAction to determine whether there is an instance specific customization of the action
		Action currentAction = actionManager.getEffectiveAction(request,  flowActionId);
		
		// at moment, only the action id parts are used from the currentAction, 
		// i.e. scope.module.section.target
		// however, easy to add instance specific parameters to currentAction, in case they 
		// are needed
		currentAction.setParam(ActionUtils.MODE_PARAMETER_NAME, flowIdModePart.toString());
		// get the "id" parameter. all flows (lists and entity CRUD), have an input mapper that stores 
		// the "id" in flow scope. however, for new flows, the "id" is a request parameter, so
		// check for that first (also, flow scope does not exist yet for new flows)
		String idParam = context.getRequestParameters().get("id");
		if (idParam == null) {
			if (context.getFlowExecutionContext().isActive()) {
				idParam = context.getFlowScope().getString("id");
			}
		}
		currentAction.setParam(currentAction.getIdParamName(), idParam);
		// default for getMessage would be a description property configured for the Action bean
		currentAction.setDescription(metadataManager.getMessage("action."+currentAction.getId(),null,
				currentAction.getDescription(),Locale.US));
		CoreSessionUtils.setCurrentAction(sessionManager, request,currentAction);
	}
	


	

	public void updateManagers(Managers managers) {
		this.actionManager = CoreManagerUtils.getActionManager(managers);
		this.sessionManager = CoreManagerUtils.getSessionManager(managers);
		this.metadataManager = CoreManagerUtils.getMetadataManager(managers);
	
	}	
	
}
