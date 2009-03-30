package edu.ucsf.lava.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.audit.AuditManager;
import edu.ucsf.lava.core.auth.AuthManager;
import edu.ucsf.lava.core.list.ListManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.webflow.InterFlowFormErrorParams;




public class BaseComponentFormAction extends FormAction implements ManagersAware{
	
	//component handlers will be iterated in the order listed
	protected ArrayList<ComponentHandler> componentHandlers;
	
	protected static final String VIEW_MODE = "viewMode"; 
    protected static final String COMMAND_AUTHORIZATION_ERROR_CODE = "authorization.command"; //????
	
    protected ActionManager actionManager;  
	protected AuthManager authManager;  
	protected SessionManager sessionManager; 
	protected ListManager listManager;
	protected AuditManager auditManager;
    protected MetadataManager metadataManager;
		
	public BaseComponentFormAction(ArrayList<ComponentHandler> handlers) {
		super();
		this.componentHandlers = handlers;
	}
	
	public BaseComponentFormAction(ComponentHandler handler) {
		super();
		this.componentHandlers = new ArrayList<ComponentHandler>();
		this.componentHandlers.add(handler);
	}

	public BaseComponentFormAction() {
		super();
	}
	
	
	
	
	// called upon starting a flow. if authorization fails, the flow terminates after setting
	// an error message to be displayed by the parent flow or if no parent, the error page
	public Event authorizationCheck(RequestContext context) throws Exception {
		// only the primary handler makes the decision on authorization, so no need to iterate thru all handlers
		return componentHandlers.iterator().next().authorizationCheck(context, getFormObject(context));
	}

	public Event initMostRecentViewState(RequestContext context) throws Exception {
		// the most recent view state is used when a view state transitions to a subflow which is 
		// unauthorized such that the subflow is terminated and control returns to the parent flow. the
		// parent flow then uses the "mostRecentViewState" attribute in flow scope to determine which
		// view state to return to. initially, "mostRecentViewState" should be set to the initial
		// view state of the flow, 
		
		// the primary handler can initialize the view state, so no need to iterate thru all handlers
		return componentHandlers.iterator().next().initMostRecentViewState(context);
	}
	

	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		// only the primary handler makes the decision on flow direction, so no need to iterate thru all handlers
		return componentHandlers.iterator().next().preSetupFlowDirector(context);
	}

	public Event postSetupFlowDirector(RequestContext context) throws Exception {
		// only the primary handler makes the decision on flow direction, so no need to iterate thru all handlers
		ComponentCommand componentCommand = (ComponentCommand)getFormObject(context);
		return componentHandlers.iterator().next().postSetupFlowDirector(context, componentCommand);
	}
	
	protected Object createFormObject(RequestContext context) throws Exception {
		ComponentCommand componentCommand = new ComponentCommand();
		for (ComponentHandler handler: componentHandlers){
			componentCommand.getComponents().putAll(handler.getBackingObjects(context, componentCommand.getComponents()));
			}
		
		return componentCommand;
    }

	
	public Event refreshFormObject(RequestContext context) throws Exception {
		ComponentCommand componentCommand = (ComponentCommand)getFormObject(context);
		BindingResult errors = (BindingResult) getFormErrors(context);
		// iterate thru the command components and refresh each from the database
		// this method is called whenever a subflow completes and a parent flow resumes so
		// that any data in the parent flow that may have been modified by the child flows is
		// reflected
		
		// for list flows, call a refresh which remembers the current page of the list so it
		// can be repositioned to the same page after refreshing
		for (ComponentHandler handler: componentHandlers){
			handler.refreshBackingObjects(context, componentCommand, errors);
		}
		return success();
	}
	
	
	// call initBinder on all handlers to set required fields
	// note: initBinder is called by createBinder which is called by setupForm (when initializing
	// the form errors object) and by bind
	protected void initBinder(RequestContext context, DataBinder binder) {
		// want to pass the command object into handler's initBinder in case logic for setting
		// required fields (or doing something else) requires command property values. since
		// initBinder is an override and does not throw Exception but getFormObject does throw
		// Exception, get around compilation by enclosing in try/catch
		// note: could get command object from context itself instead of doing this, but if
		// command object name or scope were to change, that code would break, so prefer this
		ComponentCommand command = null;		
		try {
			command = (ComponentCommand)getFormObject(context);
		}
		catch (Exception e) {
		}
		for (ComponentHandler handler: componentHandlers){
			handler.initBinder(context,command,binder); 
		}
	}
	
	// call registerPropertyEditors on all handlers to register any handler-specific property editors
	// note: this does what initBinder used to do. 
	protected void registerPropertyEditors(PropertyEditorRegistry registry) {
		for (ComponentHandler handler: componentHandlers){
			handler.registerPropertyEditors(registry); 
		}
	}

	
	// akin to what was showForm
	public Event prepareToRender(RequestContext context) throws Exception {
		// get commonly used values
		initializeRenderAuditing(context);
		ComponentCommand componentCommand = (ComponentCommand)getFormObject(context);
		BindingResult errors = (BindingResult) getFormErrors(context);
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		HttpServletResponse response =  ((ServletExternalContext)context.getExternalContext()).getResponse();
		FlowDefinition flow = context.getActiveFlow();
		StateDefinition state = context.getCurrentState();
		String eventId = ActionUtils.getEventId(context);
		
		// this is where inter-flow error/info messages are carried over.
		// if there are an object errors in the session that should carry over from a previous flow,
		// add them to the form object and clear them in the session
		BindingResult formErrors = (BindingResult)getFormErrors(context);
		List<InterFlowFormErrorParams> formErrorParamsList = CoreSessionUtils.getFormErrorParamsList(sessionManager, request);
		for (InterFlowFormErrorParams formErrorParams : formErrorParamsList) {
			formErrors.addError(new ObjectError(formErrors.getObjectName(),	formErrorParams.getCodes(), formErrorParams.getArguments(), ""));
		}
		CoreSessionUtils.clearFormErrors(sessionManager, request);
		
		for (ComponentHandler handler: componentHandlers){
			// do any setup required for the view, e.g. setting componentMode and componentView (which
			// are then added to the model in addReferenceData)
			handler.prepareToRender(context, componentCommand, errors);
		}
		
		// get the reference data
		MutableAttributeMap requestScope = context.getRequestScope();
		Map<String,Object> refData = referenceData(context, componentCommand, errors);
		for (Map.Entry<String,Object> entry : refData.entrySet()) {
			requestScope.put(entry.getKey(), entry.getValue());
		}
		finalizeRenderAuditing(context);
		return success();
	}
	
	
	/* Base method to handle a download flow that will have no "rendering" of a page
	 * but rather will involve returning a data stream with a mimetype header. 
	 * when this is called setupForm will already have been called, including get backing object
	 *
	 */
	
	public Event prepareDownload(RequestContext context) throws Exception {
		ComponentHandler downloadHandler = null;
		ComponentCommand componentCommand = (ComponentCommand)getFormObject(context);
		BindingResult errors = (BindingResult) getFormErrors(context);
		
		for (ComponentHandler handler: componentHandlers){
			if((downloadHandler==null) && handler.handlesDownload(context)){
				downloadHandler = handler;
			}
		}
		
		if(downloadHandler == null){
			//is this the right thing to do? return what
			return error();
		}
		
		return downloadHandler.handleDownload(context,componentCommand, errors);
		
	}


	
	
	//written so it could be easily overridden by a subclass.  
	protected Map referenceData(RequestContext context, Object command, BindingResult errors){
		Map<String,Object> model = new HashMap<String,Object>();
		return getComponentReferenceData(context,command,errors,model);
	}
	
	protected Map getComponentReferenceData(RequestContext context, Object command, BindingResult errors,Map model){
		ComponentCommand componentCommand = (ComponentCommand)command;
		Map handlerReferenceData;
		for (ComponentHandler handler: componentHandlers){
			handlerReferenceData = handler.addReferenceData(context,componentCommand,errors,model);
			if (handlerReferenceData != null){
				model.putAll(handlerReferenceData);
			}
		}
		return model;
	}

	
	// processFormSubmission
	public Event handleFlowEvent(RequestContext context) throws Exception {
		initializeEventHandlerAuditing(context);
		ComponentHandler eventHandler = null;
		// get commonly used values
		ComponentCommand componentCommand = (ComponentCommand)getFormObject(context);
		BindingResult errors = (BindingResult) getFormErrors(context);
		FlowDefinition flow = context.getActiveFlow();
		StateDefinition state = context.getCurrentState();
		String eventId = ActionUtils.getEventId(context);

		// event, or eventId, is in the format: eventObject__eventName, e.g. patient__save
		
		//determine which handler will handle event
		for (ComponentHandler handler: componentHandlers){
			if((eventHandler == null) && handler.handlesEvent(context)){
				eventHandler = handler;
			}
		}
		if (eventHandler == null) {
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"flowEventNotHandled.command"}, new Object[] {eventId}, ""));
			finalizeEventHandlerAuditing(context);
			return error();
		}
		
		//allow non-handling components a change to do pre-event handling work
		//note: preEventHandled returns Event, but currently ignoring
		for (ComponentHandler handler: componentHandlers){
			if(handler != eventHandler){
				handler.preEventHandled(context,componentCommand,errors);
			}
		}
		
		//call the primary event handling routine
		Event handlerReturnEvent = eventHandler.handleFlowEvent(context,componentCommand,errors); 
		
		//allow other handlers a chance to modify model objects or view based on what the priamry event handler did.
		//note: not sure whether to prevent this if returnEvent from eventHandler.handleFlowEvent is "error". for now,
		// execute post handlers regardless of whether event handler succeeds or fails
		//note: postEventHandled returns Event, but currently ignoring
		for (ComponentHandler handler: componentHandlers){
			if(handler != eventHandler){
				handler.postEventHandled(context,componentCommand,errors); 
			}
		}
		finalizeEventHandlerAuditing(context);
		return handlerReturnEvent;
	}

	
	public Event initializeRenderAuditing(RequestContext context) throws Exception{
		if (auditManager.isCurrentEventAudited()){return success();} //do not reinitialize
		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		FlowDefinition flow = context.getActiveFlow();
		StateDefinition state = context.getCurrentState();
		StringBuffer event = new StringBuffer(state.getId());
	
		auditManager.initializeAuditing(flow.getId(), event.toString(), context.getFlowScope().getString("id"), request);
	    return success();
	}
	
	public Event finalizeRenderAuditing(RequestContext context) throws Exception{
		auditManager.finalizeAuditing();
	    return success();
	}
	
	public Event initializeEventHandlerAuditing(RequestContext context) throws Exception{
		if (auditManager.isCurrentEventAudited()){return success();} //do not reinitialize
		
		ComponentCommand componentCommand = (ComponentCommand)getFormObject(context);
		BindingResult errors = (BindingResult) getFormErrors(context);
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		HttpServletResponse response =  ((ServletExternalContext)context.getExternalContext()).getResponse();
		FlowDefinition flow = context.getActiveFlow();
		StateDefinition state = context.getCurrentState();
		String event = ActionUtils.getEventId(context);
		if(event==null) {event = state.getId();}
		
		//The getRemoteUser() method on the request was returning null, so I am getting the user from the session instead
		
		auditManager.initializeAuditing(flow.getId(), event, context.getFlowScope().getString("id"),CoreSessionUtils.getCurrentUser(sessionManager, request).getLogin(),request.getRemoteAddr());
	    return success();
	}
	
	public Event finalizeEventHandlerAuditing(RequestContext context) throws Exception{
		auditManager.finalizeAuditing();
	    return success();
	}
	
	
	public void updateManagers(Managers managers) {
		this.actionManager = CoreManagerUtils.getActionManager(managers);
		this.sessionManager = CoreManagerUtils.getSessionManager(managers);
		this.listManager = CoreManagerUtils.getListManager(managers);
		this.authManager = CoreManagerUtils.getAuthManager(managers);
		this.auditManager = CoreManagerUtils.getAuditManager(managers);
		this.metadataManager = CoreManagerUtils.getMetadataManager(managers);
	}


	
	
}
