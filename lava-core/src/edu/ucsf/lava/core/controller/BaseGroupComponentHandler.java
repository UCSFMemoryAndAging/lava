package edu.ucsf.lava.core.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.session.CoreSessionUtils;

/*
 * This class serves as the base handler class for group handler classes. A group handler
 * specific to the type of entities in the group should override this class and define the 
 * abstract methods.
 * 
 * The handler is the primary handler for group flow FormAction's, handling all events related
 * to iterating over a group of entities and initiating a specified action on each entity
 * in the group.
 * 
 * The handler is also used as the secondary handler for parent flows of group flows, e.g.
 * list flow FormActions. In this capacity, it creates the group of entities based on items
 * selected in the list.  
 */

public abstract class BaseGroupComponentHandler extends LavaComponentHandler {
	// keeps track of where we are in the group. initialized to 0 in GroupFlowBuilder
	public static String GROUP_INDEX = "groupIndex";
	// reference data model attribute used by view's to display prototype group
	// functionality
	protected static String GROUP_PROTOTYPE = "groupPrototype";
	// flow scope attribute for storing the name of the command component from which
	// the group can be created, e.g. the list from which the selected items compose 
	// the group
	public static String SOURCE_COMPONENT = "sourceComponent";
	// flow scope attribute for mapping the group data structure into the group flow
	public static String GROUP_MAPPING = "group";
	// used for both returning an Event for errors when handling an entity in the group
	// and the flow scope attribute to store the error message 
	protected static String GROUP_ERROR = "groupError";
	// used for returning the Event to indicate that there are no more entities in
	// the group to be 
	protected static String FINISH = "finish";
	// used for displaying the group entities in a navigable list by the view when
	// needed, e.g. to confirm deletion of all entities at once
	protected static String GROUP_LIST = "groupList";
	
	public BaseGroupComponentHandler() {
		super();
		defaultEvents = this.getDefaultEvents();
		authEvents = this.getAuthEvents();
	}

	// subclasses can override to append each event which the group flow will execute on the
	// entities in the group, e.g. "enter" or "edit", i.e. they should call this base class 
	// method and then append events to the result
	protected List getDefaultEvents() {
		// "bulkDelete" is handled when this handler is serving as the secondary handler to a
		// primary handler when a group should be created prior to transitioning to a group
		// subflow, where this handler serves as the primary handler, handling the rest of 
		// these events
		return new ArrayList(Arrays.asList(new String[]{"bulkDelete", "next", "addMissing","close", "confirmBulkDelete", "cancelBulkDelete"}));
	}
	
	// subclasses must override to define which authorization events should be checked
	// when the group flow is performing a specified action on each entity in the group
	protected abstract List getAuthEvents();
	
	public Event authorizationCheck(RequestContext context) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action action = actionManager.getCurrentAction(request);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		// the authorization event for reports is "view"

		
//TODO: with group flow, authorization falls to the individual entity actions. however, bulkDelete
//does not have individual entity actions. perhaps the way to handle this is to iterate thru the
//group, checking the authorization for each entity
/**		
		if (!authManager.isAuthorized(user,action,ProjectUnitUtils.ANY_WILDCARD)) {
			CoreSessionUtils.addFormError(sessionManager, request, new String[]{COMMAND_AUTHORIZATION_ERROR_CODE}, null);
			return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
		}
**/		
		
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	public Event initMostRecentViewState(RequestContext context) throws Exception {
		// probably not useful since report flow will not have subflows
		context.getFlowScope().put("mostRecentViewState", "iterate");
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	
	// override to map group action events into authorized events
	public boolean isAuthEvent(RequestContext context) {
		// in this case, event refers to the action or flow mode, where for actions, the
		// mode is stored within the Action, and for flow's the mode is the final part of the flow id 
		String event = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		
		if (event.equals("bulkDelete")) {
			event = "delete";
		}
		
		for (String authEvent: authEvents){
			if (event.equals(authEvent)){
				return true;
			}
		}
		return false;
	}
	
	public Map getBackingObjects(RequestContext context, Map components) {
		// the backing object is a group, that is created during a parent flow which uses this 
		// handler as a secondary handler to create it (the parent is typically a list flow, 
		// where the user has selected list items to create the group). the parent flow starts
		// the group flow as a subflow and maps the group into its flow scope. 
		// this group structure is not a persistent object, and therefore there is no retrieval 
		// of a persistent entity here. rather, the entity is obtained from flow scope and set as
		// the command object
		
		// the default data structure for the group is java.util.List<LavaEntity>
		// if another data structure is used, then a subclass may have to override some
		// methods in this class which assume that structure
		
		Map backingObjects = new HashMap<String,Object>();
		backingObjects.put(this.getDefaultObjectName(), context.getFlowScope().get(GROUP_MAPPING));

		// also put the group in the command object as a PagedListHolder so they can be displayed 
		// in a navigable list for confirmation purposes (e.g. for "bulkDelete")

		// put the group items into a ScrollablePagedListHolder and add that to the command components
		// so that the view can display the entities to be deleted as a paged list on confirmDelete
		// note: even though just need basic navigation for the group entities, and no filtering or
		//  sorting, still need a ScrollablePagedListHolder vs. PagedListHolder to support the record set
		//  navigation, e.g. "1-10 of 121"
		List<LavaEntity> group = (List<LavaEntity>) context.getFlowScope().get(GROUP_MAPPING);
		if (group != null) {
			ScrollablePagedListHolder groupListHolder = new ScrollablePagedListHolder();
			groupListHolder.setSourceFromEntityList(group);
			backingObjects.put(GROUP_LIST, groupListHolder);
		}
		
		return backingObjects;
	}


	public void initBinder(RequestContext context, Object command, DataBinder binder) {}

	public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 

		if (flowMode.equals("bulkDelete")) {
			// setting componentView to "bulkDelete" will trigger the "Are you sure you want to.."
			// warning via the component content decorator
			setComponentMode(request, getDefaultObjectName(), "vw");
			setComponentView(request, getDefaultObjectName(), "bulkDelete");
		}
		else {
			// other than bulkDelete, the group flow itself only has view states for errors and 
			// missing entities. there is no CRUD or persistent model object, so just set view mode
			setComponentMode(request, getDefaultObjectName(), "vw");
			setComponentView(request, getDefaultObjectName(), "view");
		}
	}
	
	// subclasses must override to define what the target part of the group action is, 
	// e.g. "instrumentGroup", to allow handleFlowEvent to determine whether the handler
	// is being called as part of the group flow or the parent flow of the group flow
	protected abstract String getActionTarget();

	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		// note: do not use "event = ActionUtils.getEvent(request)" because it is possible for a flow
		//  to set an overrideEvent which will be different from the event request parameter
		String event = ActionUtils.getEventName(context);

		// this handler will serve as both the primary handler in the group flow and a secondary
		// handler in a parent flow of the group flow. so events for both flows must be handled here.
		
		// when this is the secondary handler, it will be called upon to create the group List 
		// data structure. the event will be the action that the group flow is to perform on 
		// each item in the group, e.g. for instrument groups the event could be "enter", "delete", etc..
		// each of these events requires the same handling: creation of the group

		// so here, if the handler is being used as the secondary handler, create the group. this
		// is determined by comparing the current action id's (or flow id's) target part to the 
		// group action's target part (e.g. if currently in "visitInstruments" and user clicks
		// on "instrumentGroup" action, this is when a group should be created. once in the group
		// flow, the current action and group actions will all have the "instrumentGroup" target
		// so handleFlowEvent can proceed in the standard way)
		String target = ActionUtils.getTarget(ActionUtils.getActionIdFromFlowId(context.getActiveFlow().getId(), new StringBuffer()));
		if (!target.equals(this.getActionTarget())) {
			// this event is handled as part of a parent flow of the group subflow, e.g. a list flow, where 
			// this handler is the secondary handler
			return this.handleCreateGroupEvent(context,command,errors);
		}
		else if(event.equals("next")){
			return this.handleNextEvent(context,command,errors);
		}
		else if(event.equals("addMissing")){
			return this.handleAddMissingEvent(context,command,errors);
		}
		else if(event.equals("close")){
			return this.handleCloseEvent(context,command,errors);
		}	
		else if(event.equals("confirmBulkDelete")){
			return this.handleConfirmBulkDeleteEvent(context,command,errors);
		}	
		else if(event.equals("cancelBulkDelete")){
			return this.handleCancelBulkDeleteEvent(context,command,errors);
		}	
		else {
			return this.handleCustomEvent(context,command,errors);
		}
	}

	public Event handleCreateGroupEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doCreateGroup(context, command,errors);
	}
		
	// subclasses must override to create a group of entities of type java.util.List and set
	// it in flow scope as the GROUP_MAPPING attribute
	protected abstract Event doCreateGroup(RequestContext context, Object command, BindingResult errors) throws Exception;
	
	public Event handleNextEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doNext(context, command,errors);
	}

	// subclasses must override this to handle the next entity in the group, i.e. 
	// preparation for the group flow to start an entity specific subflow to perform the
	// specified action on the entity.
	
	// the id of the entity should be set in the flow scope "id" attribute
	
	// the Event that is returned is used by the "iterate" action state in the group flow
	// to determine which subflow should be started, e.g. "instrument__enter"
	// if there are no more entities to process, return the FINISH Event
	
	// optionally, if there is an error condition, return the GROUP_ERROR event
	// and store an error message in the flow scope GROUP_ERROR attribute
	protected abstract Event doNext(RequestContext context, Object command, BindingResult errors) throws Exception;
	

	public Event handleAddMissingEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doAddMissing(context, command,errors);
	}
		
	// subclasses can override to create a missing entity and add it to the group
	protected Event doAddMissing(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	

	public Event handleCloseEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doClose(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doClose(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	
	public Event handleCancelEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doCancel(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doCancel(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	
	public Event handleConfirmBulkDeleteEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doConfirmBulkDelete(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doConfirmBulkDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();

		// iterate thru the entities in the group, deleting each
		List<LavaEntity> group = (List) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		for (LavaEntity entity : group) {
			entity.delete();
		}
							
		CoreSessionUtils.addFormError(sessionManager, request, new String[]{"info.group.bulkDeleteComplete"}, new Object[]{});

		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}				

	
	public Event handleCancelBulkDeleteEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doCancelBulkDelete(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doCancelBulkDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
	
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
}
