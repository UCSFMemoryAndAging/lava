package edu.ucsf.lava.crms.assessment.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static edu.ucsf.lava.crms.assessment.controller.InstrumentComponentFormAction.INSTRUMENT;
import static edu.ucsf.lava.core.controller.BaseEntityComponentHandler.CONFIRM_LOGIC;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.assessment.controller.InstrumentComponentFormAction;
import edu.ucsf.lava.crms.assessment.controller.InstrumentGroupHandler;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentConfig;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class SwitchInstrumentGroupHandler extends InstrumentGroupHandler {
	
	static public String VISIT_NAVIGATION = "visitNavigation";
	static protected Boolean handleVisitLists = false;
	
	public SwitchInstrumentGroupHandler() {
		super();
		Map handledObjects = getHandledObjects();
		handledObjects.put(VISIT_NAVIGATION, Visit.class);
		if (handleVisitLists)
		  defaultEvents.add("contextChange");
	}

	@Override
	protected List getDefaultEvents() {
		// append the action events specific to InstrumentGroup to the base
		// action events.
		List defaultEvents = super.getDefaultEvents();

		defaultEvents.addAll(new ArrayList(Arrays.asList(new String[] {
				"switch", "reRender" })));
		return defaultEvents;
	}

	@Override
	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		// see super's comments for why we get event/target this way
		String event = ActionUtils.getEventName(context);
		String target = ActionUtils.getTarget(ActionUtils.getActionIdFromFlowId(context.getActiveFlow().getId(), new StringBuffer()));

		if (event.equals("switch")) {
			return this.handleSwitchEvent(context, command, errors);
		} else if (event.equals("reRender")) {
			return this.handleReRenderEvent(context, command, errors);
		} else if (handleVisitLists && event.equals("contextChange")) {
			this.doContextChange(context,command,errors);
			return new Event(this,SUCCESS_FLOW_EVENT_ID);
		} else {
			return super.handleFlowEvent(context, command, errors);
		}
	}
	
	@Override
	public Event initMostRecentViewState(RequestContext context) throws Exception {
		// this is called when a switching flow got denied because of unauthorized
		// we do not want to iterate (with BaseGroupComponentHandler) in this scenario, 
		// but rather stop the instrumentGroup navigation all together, returning back to parent flow
		context.getFlowScope().put("mostRecentViewState", "finish");
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	public Event handleSwitchEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doSwitch(context, command, errors);
	}

	protected Event doSwitch(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Event retEvent;

		// the lava.assessment.instrument.instrumentGroup action is a child of the parent instrument 
		// list flow, and specific events are appended per InstrumentGroupFlowTypeBuilder to create
		// flow ids, e.g. 
		// lava.assessment.instrument.instrumentGroup.enter
		// lava.assessment.instrument.instrumentGroup.bulkDelete
		// the bulkDelete event is a special case handled in an initial decisionState in the GroupFlowBuilder
		// where all instruments are deleted at once
		// all other events indicate an action that is to be performed on each instrument in the group
		// individually, and this method handles each one in succession
		
		// get the action to be performed on each instrument in the group, e.g. 'enter', 'delete'
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		
		List<LavaEntity> instrumentGroup = (List) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

		Long request_id;
		if (request.getParameter("id") != null)
			request_id = Long.valueOf(request.getParameter("id"));
		else
			request_id = null;

		// instead of going to next group index, traverse through list and find
		// the instrument id supplied
		Instrument nextInstrument = null;
		int groupIndex = 0;
		for (LavaEntity instrumentIterator : instrumentGroup) {
			if (instrumentIterator.getId() != null) {
				if (instrumentIterator.getId().equals(request_id)) {
					nextInstrument = (Instrument)instrumentIterator;
					// increment the index in preparation for the next
					// instrument
					context.getFlowScope().put(GROUP_INDEX, groupIndex + 1);
					break;
				}
			}
			groupIndex++; // track this for pure Next iterations
		}
		
		// if instrument was not found (or no id parameter given), handle this
		// like a "next"
		if (nextInstrument == null) { // default to group index method (a "pure" Next)
			return this.doNext(context, command, errors);
		}

		// SWITCHABLE: support SwitchGroupFlow switchMode changing
		// If the switchEvent parameter in the request contains a different mode than
		//   the one used by SwitchInstrumentGroup, then finish this flow, returning
		//   to the parent flow with "switchMode" event and the parent should
		//   create a new InstrumentGroup flow of the new mode (given by "switchEvent" requestParameter)
		String paramValue = request.getParameter("switchEvent");
		String requestedMode = paramValue.substring(paramValue.indexOf("__")+2);
		if (!requestedMode.equals(flowMode)) {
			return new Event(this, "switchMode");
		}
		
		InstrumentConfig instrumentConfig = instrumentManager.getInstrumentConfig().get(nextInstrument.getInstrTypeEncoded());

		// verify that the instrument supports the flow if it is an optional
		// instrument
		// flow. the "edit" event encompasses the "enter", "enterReview" and
		// "upload" actions
		// since different instruments in the flow could have different
		// "edit"

		if (flowMode.equals("edit")
				&& !instrumentConfig.supportsFlow("enter")
				&& !instrumentConfig.supportsFlow("enterReview")
				&& !instrumentConfig.supportsFlow("upload")) {
			retEvent = new Event(this, "groupError");

			// create the error message and put in flow scope
			context.getFlowScope().put(
					"groupErrorMsg",
					getMessage("instrumentGroup.actionNotSupported",
							new Object[] { flowMode,
									nextInstrument.getInstrType() }));
		} else {
			// if the mode for the instrumentGroup action is "edit" then the
			// mode for the subflow could
			// either be "enter", "enterReview" or "upload" based on the
			// instrument. the key is that these
			// are assumed to be mutually exclusive.
			if (flowMode.equals("edit")) {
				if (instrumentConfig.getEnterFlow()) {
					flowMode = "enter";
				} else if (instrumentConfig.getEnterReviewFlow()) {
					flowMode = "enterReview";
				} else if (instrumentConfig.getUploadFlow()) {
					flowMode = "upload";
				}
			}

			// put instrTypeEncoded into flow scope so that when the subflow
			// starts and needs to
			// set this (as part of setting the current action), it can
			// access the parent instrumentGroup
			// flow and get is from its flow scope. normally,
			// instrTypeEncoded comes from the
			// request URL when an instrument CRUD flow session is starting,
			// but with the instrumentGroup
			// flow, instrument CRUD subflows are launched without an HTTP
			// Request, so have to
			// do it a different way.
			// mapping instrTypeEncoded into the flow scope of the
			// instrument CRUD subflows is not an
			// option because when the flow session is starting, flow scope
			// does not exist yet
			String instrTypeEncoded = nextInstrument.getInstrTypeEncoded();
			context.getFlowScope().put("instrTypeEncoded", instrTypeEncoded);

			// put id into flow scope so that it will be mapped into the
			// instrument CRUD subflow
			// mapped as a String, so convert
			context.getFlowScope().put("id", nextInstrument.getId().toString());

			// the event prefix is determined by whether the instrument has
			// its own flows or uses
			// the shared instrument flows
			StringBuffer transitionEvent = new StringBuffer();
			transitionEvent.append(instrTypeEncoded);

			// the event suffix can be obtained from the mode (event)
			// portion of the flow id, as the
			// instrumentGroup mode will be the same as the mode of the
			// instrument subflow, e.g.
			// lava.assessment.instrument.instrumentGroup.enter will iterate
			// over instrumentGroup
			// transitioning in and out of subflows like
			// lava.assessment.instrument.instrument.enter

			transitionEvent.append("__").append(flowMode);

			// increment the index in preparation for the next instrument
			context.getFlowScope().put(GROUP_INDEX, groupIndex + 1);

			retEvent = new Event(this, transitionEvent.toString());
		}
			
		return retEvent;
	}
	
	public Event handleReRenderEvent(RequestContext context, Object command,
			BindingResult errors) throws Exception {
		return doReRender(context, command, errors);
	}

	protected Event doReRender(RequestContext context, Object command,
			BindingResult errors) throws Exception {
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}

	@Override
	public void subFlowReturnHook(RequestContext context, Object command, BindingResult errors) throws Exception {
		// this method is called whenever a subflow completes and a parent flow
		// resumes so that any data in the parent flow that may have been modified by the
		// child flows is reflected.
		//
		// This thus happens for each switch event, even when coming from a non-editing view.

		// Since the instrumentGroup list could have been affected (e.g. completed
		// deStatus change, or instrument fields changed), we wish for our instrumentGroup in
		// backingobjects to be refreshed, else the list would remain as the older one used
		// (e.g. not showing deStatus changes for our navigation bar view).  Both GROUP_MAPPING
		// and GROUP_INDEX could change in the flow (e.g. subflows that delete).
		
		// We used to do this here in subFlowReturnHook, but this gets called when a primary
		// AND a secondary handler.  Instead, we now do it only when it's a secondary handler in
		// postEventHandled(), thus when still in middle of the subflow and have knowledge of
		// which particular instrument has changed.  In this way, we do not have to recreate the
		// entire list but make use of the knowledge of the one instrument that changed.
		
		// EMORY change: according to emory protocol, udsmilestone saving will alter visit's status and readyForSubmit
		// So certain instrument subflows (e.g. UdsMilestone enterSave) may change the
		//   visit entity, so when refreshing, we need to refresh the visitNavigation just in case
		Map backingObjects = ((ComponentCommand) command).getComponents();
		
		// we cannot count on a current instrument, but an instrumentGroup should be defined
		List<Instrument> instrumentGroup = (List<Instrument>)backingObjects.get(this.getDefaultObjectName());
		if (instrumentGroup == null) return; // no visit to refresh
		if (instrumentGroup.get(0) == null) return;  // shouldn't happen, but just in case a instrumentGroup had no instruments
		Long visitID = instrumentGroup.get(0).getVisit().getId();
		Visit visit = (Visit) Visit.MANAGER.getById(visitID, Visit.newFilterInstance());
		backingObjects.put(SwitchInstrumentGroupHandler.VISIT_NAVIGATION, visit);
	}
	
	@Override
	public Event preEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception { 
		HttpServletRequest request = ((ServletExternalContext) context.getExternalContext()).getRequest();
		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
	
	@Override
	public Event postEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext) context.getExternalContext()).getRequest();
		Map backingObjects = ((ComponentCommand) command).getComponents();
		String primaryhandler_event = ActionUtils.getEventName(context);
		
		// the CONFIRM_LOGIC checkbox could have been changed, so pass that on to flow scope
		if (backingObjects.containsKey(CONFIRM_LOGIC)) {
			Byte confirm_logic = (Byte)backingObjects.get(CONFIRM_LOGIC);
			if (confirm_logic == null) confirm_logic = (byte)0;
			context.getFlowScope().put(CONFIRM_LOGIC, confirm_logic);
		}
		
		List<Instrument> instrumentGroup = (List<Instrument>)backingObjects.get(this.getDefaultObjectName());
		Instrument instrument_of_primaryhandler = (Instrument)backingObjects.get(INSTRUMENT);
		
		// postEventHandled is only called when a secondary handler, so the primary could have
		// changed an instrument and we'd want to update our instrumentGroup with the new values
		// This is necessary as instrumentGroup is passed through flow scope.  This prevents us
		// from having to update all instruments in instrumentGroup during refreshBackingObject(),
		// which gets called after the subflow ends and when there is no longer an "instrument" (changed)
		// backingobject.  This also prevents having to do queries to find
		// current instrument values since already in backingobjects now (having just been saved).
		//
		// But only need to do all of this during "changing" events of InstrumentHandler

		// TODO: support "applyAdd", "saveAdd" (EMORY doesn't allow users to add individual instruments with these events)
		if (primaryhandler_event.equals("enterSave")
			|| primaryhandler_event.equals("doubleEnterSave")
			|| primaryhandler_event.equals("confirmChangeVersion")
			|| primaryhandler_event.equals("collectSave")
			|| primaryhandler_event.equals("enterVerify")
			|| primaryhandler_event.equals("statusSave")) {
			
			Instrument instrument;
			for (int i=0; i < instrumentGroup.size(); i++) {
				instrument = instrumentGroup.get(i);
				if (instrument.getId().equals(instrument_of_primaryhandler.getId())) {
					// Note that we are replacing an InstrumentTracking with a fully-qualified subclass of Instrument
					// This likely works until InstrumentTracking contains other members outside of Instrument.
					// This has the side benefit though of filling in our instrumentGroup as we go from our 
					// light-weight way of initially getting the group (not having needed all the
					// instrument-specific data then)
					instrumentGroup.set(i, instrument_of_primaryhandler);
				}
			}
			// flow scope thus updated as well
			backingObjects.put(this.getDefaultObjectName(), instrumentGroup);
		}
		
		if (primaryhandler_event.equals("confirmDelete")) {
			// then "instrument" is the one being deleted, so remove from instrumentGroup
			for (Instrument instrument : instrumentGroup) {
				if (instrument.getId().equals(instrument_of_primaryhandler.getId())) {
					instrumentGroup.remove(instrument);
					// now that instrumentGroup has a difference size, the GROUP_INDEX mapping may
					// be incorrect.  Fortunately, we can get away with just decrementing GROUP_INDEX.
					// Why? GROUP_INDEX always refers to next instrument (i.e. one after current), and
					// deletes always occurs to current instrument, and all deletes will go through here if
					// part of an instrumentGroup.
					Integer groupIndex = context.getFlowScope().getInteger(GROUP_INDEX);
					// should always be greater than 0 since points to next instrument's index
					if (groupIndex != null) context.getFlowScope().put(GROUP_INDEX, groupIndex - 1);
					break; // since we changed instrumentGroup, for-each is now broken
				}
			}
		}

		return new Event(this, SUCCESS_FLOW_EVENT_ID);
	}
	
	protected Visit getLastVisit(RequestContext context) {
		HttpServletRequest request = ((ServletExternalContext) context.getExternalContext()).getRequest();
		
		String currentProjectName = CrmsSessionUtils.getCurrentProjectName(sessionManager, request);
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		AuthUser currentUser = getCurrentUser(request);
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager, request);
		
		LavaDaoFilter filter = Visit.newFilterInstance(currentUser);
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", p.getId()));
		if (currentProjectName != null && !currentProjectName.equals(""))
			filter.addDaoParam(filter.daoEqualityParam("projName", currentProjectName));
		List<Visit> visits = Visit.MANAGER.get(filter);
		
		// filter out visits to which user lacks permission
		visits = CrmsAuthUtils.filterVisitListByPermission(currentUser, currentAction, visits, false);
		
		Visit lastvisit = null;
		for (Visit v : visits) {
			// must be after or same date as current value to replace
			if ((lastvisit == null)
					|| (!v.getVisitDate().before(lastvisit.getVisitDate()))) { 
				lastvisit = v;
			}
		}
		return lastvisit;
	}

	@Override
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request = ((ServletExternalContext) context.getExternalContext()).getRequest();

		// if this is the primary component, the componentView and componentMode
		// will be set correctly in
		// BaseListEntityHandler, but if this list is used as a secondary
		// component, it's prepareDefaultModelAndView
		// in BaseListEntityHandler will not be called, since the primary
		// component's default will be used, so
		// it will not get a chance to set its component mode and view within
		// prepareDefaultModelAndView, so
		// set them here to have them added to the reference data
		
		// add mode to model
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager, request);
		String mode = getComponentMode(request, currentAction.getTarget());
		if (mode == null) {
			mode = getDefaultMode();
			setComponentMode(request, currentAction.getTarget(), mode);
		}
		model.put(getDefaultObjectName().concat(COMPONENT_MODE_SUFFIX), mode);

		// add view to model
		String view = getComponentView(request, currentAction.getTarget());
		if (view == null) {
			mode = getDefaultView();
			setComponentView(request, currentAction.getTarget(), view);
		}
		model.put(currentAction.getTarget().concat(COMPONENT_VIEW_SUFFIX),view);

		model.put("instrumentConfig", instrumentManager.getInstrumentConfig());


/* TODO: add visit list
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		if (p != null) {
			Map<String, Map<String, String>> dynamicLists = getDynamicLists(model);
			Map<String, String> visitList = listManager.getDynamicList(
					getCurrentUser(request), "visit.patientVisitDates",
					"patientId", p.getId(), Long.class);
			visitList = CrmsAuthUtils.filterVisitListByPermission(getCurrentUser(request),
					CoreSessionUtils.getCurrentAction(sessionManager, request),
					visitList, false);
 also need to filter out only uds
			dynamicLists.put("visit.patientVisitDates", visitList);
			model.put("dynamicLists", dynamicLists);
		}
*/

		return super.addReferenceData(context, command, errors, model);
	}

	protected void doContextChange(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		if (handleVisitLists) {
			sessionManager.setSessionAttribute(request, VISIT_NAVIGATION, null /*visitNavigation*/);
		}
	}
	
	protected List<Instrument> getInstrumentListFromVisit(RequestContext context, Visit v) {
		HttpServletRequest request = ((ServletExternalContext) context.getExternalContext()).getRequest();
		LavaDaoFilter filter = InstrumentTracking.newFilterInstance(getCurrentUser(request));

		// sort so that it appears in right order on the action bar
		filter.addDefaultSort("instrType", true);
		// needed when using filter for UdsInstrumentTracking.MANAGER.get()
		filter.setAlias("visit", "visit");

		long visit_id;
		if (v == null) {
			// then we wish to return nothing for our backingObjects, so filter
			// out everything
			// visit id should never be 0, so this returns no values
			visit_id = 0;
		} else {
			visit_id = v.getId();
		}
		filter.addDaoParam(filter.daoEqualityParam("visit.id", new Long(visit_id)));

		List<Instrument> instruments = InstrumentTracking.MANAGER.get(filter);
		return instruments;
	}

	@Override
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request = ((ServletExternalContext) context.getExternalContext()).getRequest();

		// Map backingObjects = new HashMap<String, Object>();
		Map backingObjects = (Map<String,?>)super.getBackingObjects(context, components);

		// The super-class just added an "instrumentGroup" if the instrumentGroup was found in
		//   flow scope as GROUP_MAPPING (e.g. from visitInstruments selections, or a prior calculation
		//   based on visitNavigation)
		// If so, then we'll use this backing object instead of recreating a default instrument list
		List<Instrument> instrumentGroup = (List<Instrument>) backingObjects.get(this.getDefaultObjectName());
		
		// The super-class just added an "instrumentGroup" if the instrumentGroup was found in
		//   flow scope xor if GROUP_MAPPING was defined in flow scope (e.g. the visitInstruments
		//   flow adds this mapping when creating the instrumentGroup, after having selected items
		//   and chosen an action). 
		// If so, then use this backing object instead of recreating a default instrument list
		//List<Instrument> instrumentGroup = (List<Instrument>) backingObjects.get(this.getDefaultObjectName());
		
		// find any instrument that may determine the visit instrument list; the current instrument would be one
		Instrument instrOfNav = (Instrument)components.get(InstrumentComponentFormAction.INSTRUMENT);
		
		if (instrOfNav == null) {
			// there is still a chance an instrument should be considered, that when in middle of transitioning
			//   to it, so it isn't in the backing objects yet.  This occurs during the switch event
			// check if this flow was caused by a switchEvent, which may be elicit a change in instrumentGroup below
			String switchEventParam = context.getRequestParameters().get("switchEvent");
			if (switchEventParam != null) {
				String idParam = context.getRequestParameters().get("id");
				if (idParam != null && StringUtils.isNumeric(idParam)) {
					// then this flow was initiated by a switchEvent and so we can trust the id in the
					//   request parameters to be an instrument id representing the new instrument
					// use this instrument to figure out if instrumentGroup needs to change (compare visits)
					LavaDaoFilter filter = Instrument.newFilterInstance(getCurrentUser(request));
					filter.addDaoParam(filter.daoEqualityParam("id", new Long(idParam)));
					instrOfNav = (Instrument) Instrument.MANAGER.getOne(InstrumentTracking.class, filter);
				} else {
					// e.g. coming from a list view
					instrOfNav = null;
				}
			}
		}
		
		// check flow scope to see if visitNavigation has already been assigned, else
		//   figure out which visitNavigation to use and add to flow scope
		Visit visitNavigation = null;  // TODO: assign visitNavigation to flowScope
		//Visit visitNavigation = (Visit)context.getFlowScope().get(VISIT_NAVIGATION);
		if (visitNavigation == null) {
			if (instrumentGroup == null) {
				if (instrOfNav == null) {
					// since no instrument is set as current, assign default visit to be last visit
					visitNavigation = getLastVisit(context);
				} else {
					visitNavigation = instrOfNav.getVisit();
				}
			} else {
				// note: it is a given that all instruments in an instrumentGroup belong to the same visit
				
				// if instrOfNav is defined, then ensure that it's visit is the same as the instrumentGroup in flow
				// if not, then assign the visitNavigation to instrOfNav's visit instead, and recalculate instrumentGroup
				if (instrOfNav != null && !instrOfNav.getVisit().getId().equals(instrumentGroup.get(0).getVisit().getId())) {
					visitNavigation = instrOfNav.getVisit();
					instrumentGroup = null; // later this gets assigned based on visit, when null
				} else if ((instrumentGroup.size() > 0)
						&& (instrumentGroup.get(0) != null)
						&& (instrumentGroup.get(0).getVisit() != null)) {
					// determine visit from instrumentGroup
					visitNavigation = (Visit)Visit.MANAGER.getById(instrumentGroup.get(0).getVisit().getId(), Visit.newFilterInstance());
				}
				// instrumentGroup should never been non-null yet empty 
			}
			//context.getFlowScope().put(VISIT_NAVIGATION, visitNavigation);
			backingObjects.put(VISIT_NAVIGATION, visitNavigation);
		}
		
		
		// if instrumentGroup was never assigned, then recreate it using visitNavigation
		if (instrumentGroup == null) {
			instrumentGroup = getInstrumentListFromVisit(context, visitNavigation);
			context.getFlowScope().put(GROUP_MAPPING, instrumentGroup);  // now that we've determined it, use it from now on in this flow
			backingObjects.put(this.getDefaultObjectName(), instrumentGroup); // replaces any current key
		}
		
		/*
		// include the visit list of instrument lists
		boolean alsoFilterLocked = true; // since the list actions are all editing instruments, don't show locked visits
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		if (p != null) {
			List<Visit> visitList = p.getVisits(Patient.newFilterInstance());
			visitList = CrmsAuthUtils.filterVisitListByPermission(getCurrentUser(request),
					CoreSessionUtils.getCurrentAction(sessionManager, request),
					visitList,
					alsoFilterLocked);
			Map<Long, List<Visit>> groupSets = new LinkedHashMap<Long, List<Visit>>();
			for (Visit visit : visitList) {
				groupSets.put(visit.getId(), (List)getInstrumentListFromVisit(context, visit));
			}
			backingObjects.put("patientVisitInstrumentGroups", groupSets);
		}
		*/

		//EMORY change: support SwitchGroupFlow switchMode changing
		TreeMap authorizedFlowModeList = new TreeMap();
		authorizedFlowModeList.put("view","VIEW");
		if (visitNavigation != null
		    && visitNavigation.getLocked() == false)
		  authorizedFlowModeList.put("edit","EDIT");
		backingObjects.put("authorizedFlowModeList", authorizedFlowModeList);
		

		return backingObjects;
	}
	
	
	
/*
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request = ((ServletExternalContext) context.getExternalContext()).getRequest();
		LavaDaoFilter filter = InstrumentTracking.newFilterInstance(getCurrentUser(request));

		Visit v = this.getLastVisit(context);

		// sort by formId so that it appears in right order on the action bar
		filter.addDefaultSort("formId", true);
		// needed when using filter for UdsInstrumentTracking.MANAGER.get()
		filter.setAlias("visit", "visit");

		long visit_id;
		if (v == null) {
			// then we wish to return nothing for our backingObjects, so filter
			// out everything
			// visit id should never be 0, so this returns no values
			visit_id = 0;
		} else {
			visit_id = v.getId();
		}
		filter.setParam("visit.id", new Long(visit_id));
		filter.addParamHandler(new LavaEqualityParamHandler("visit.id"));
		return filter;
	}
*/
}
