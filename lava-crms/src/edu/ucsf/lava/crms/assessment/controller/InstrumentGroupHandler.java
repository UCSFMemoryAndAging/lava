package edu.ucsf.lava.crms.assessment.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder.ListItem;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentConfig;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.controller.CrmsGroupComponentHandler;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class InstrumentGroupHandler extends CrmsGroupComponentHandler {
	// primary list handlers can set a prototype to be used when the instrumentGroup is created
	public static String PROTOTYPE_PROJ_NAME = "prototypeProject";
	public static String PROTOTYPE_VISIT_TYPE = "prototypeVisitType";
	public static final String ANY_PROJECT_KEY="ANY";
	public static final String ANY_VISIT_TYPE_KEY="ANY";
	protected Map<String,List<String>> groupPrototypes;
	// target part of the action id for the group flow, lava.assessment.instrument.instrumentGroup
	// used by the base class to determine when this handler is being used as the secondary handler
	// of a group flow's parent flow (to create the group), or as the primary handler for the group flow
	public static String TARGET = "instrumentGroup";
	
	
	public InstrumentGroupHandler() {
		super();
		
		// define the default object name here instead of in superclass because it is
		// entity type specific. it must correlate with the name of the action target 
		// for the group actions in the parent flow, in this case "instrumentGroup"
		// note that the flow scope attribute that was used to map the group into the group
		// flow is defined in the base class ("group") as it can be the same for all uses
		// of the group flow
		Map<String,Class> objectMap = new HashMap<String,Class>();
		objectMap.put("instrumentGroup", List.class);
		this.setHandledObjects(objectMap);
	}
	
	protected List getDefaultEvents() {
		// append the action events specific to InstrumentGroup to the base action events.
		List defaultEvents = super.getDefaultEvents();
		// these action events occur when this handler is used as a secondary handler where
		// the primary handler is an instrument list flow, which has action buttons for these events

		// (the event could also be appended with "_prototype" which indicates that the group is to
		// be created from a project-visit instrument group prototype, not from user selected 
		// list items)
		
		defaultEvents.addAll(new ArrayList(Arrays.asList(new String[]{"view", "view_prototype", 
				"edit", "edit_prototype", "status", "status_prototype", "delete", "delete_prototype"})));
		return defaultEvents;
	}
	
	protected List getAuthEvents() {
		// isAuthEvent maps "deleteAll" event to "delete" for auth purposes		
		return new ArrayList(Arrays.asList(new String[]{"enter", "delete"}));
	}

	public String getActionTarget() {
		return InstrumentGroupHandler.TARGET;
	}
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		// if there is a matching projName-visitType instrument group prototype,
		// construct a string to put in the model which will both serve as a flag to
		// the view to display group functionality and provide the string needed
		String prototypeProject = context.getFlowScope().getString(PROTOTYPE_PROJ_NAME);
		String prototypeVisitType = context.getFlowScope().getString(PROTOTYPE_VISIT_TYPE);
		List<String> prototypeList = this.getGroupPrototype(prototypeProject, prototypeVisitType);
		if (prototypeList != null && prototypeList.size() > 0) {
			model.put(GROUP_PROTOTYPE, getMessage("instrumentGroup.groupTitle", new String[]{prototypeProject, prototypeVisitType}));
		}
		return super.addReferenceData(context, command, errors, model);
	}
	
		
	// this method creates an instrumentGroup prior to transitioning to the instrumentGroup subflow
	// (which in turn iterates thru the instruments in the instrumentGroup, transitioning to a 
	// specific instrument subflow for each instrument)
	// this method is called when this handler is serving as a secondary handler to a primary
	// instrument list handler. it creates an instrumentGroup based on either the instruments in the 
	// list selected by the user or based on an instrument group prototype 
	protected Event doCreateGroup(RequestContext context, Object command, BindingResult errors) throws Exception {
		// get the instrument list from which a group will be created
		String sourceComponent = context.getFlowScope().getString(SOURCE_COMPONENT);
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get(sourceComponent);
		List<ListItem> instrumentList = plh.getSource();
		
		List<LavaEntity> instrumentGroup = new ArrayList<LavaEntity>();
		
		// determine whether the group should be created based up on an instrument group
		// prototype, or based on user selected list items
		String event = ActionUtils.getEventName(context);
		
		if (event.endsWith("_prototype")) {
			// create the group based on a prototype
			// NOTE: the assumption is a project-visitType instrument group prototype. if there
			// may be other prototypes used this code will need to change to accomodate
			
			// could get the currentVisit in context and get the project and visitType from it, which
			// would work fine when this handler is used in conjunction with VisitInstrumentsHandler, but
			// to facilitate having other instrument list handlers work with this handler, allow the
			// list handler to designate the project and visitType, so pass via flow scope
			String prototypeProject = context.getFlowScope().getString(PROTOTYPE_PROJ_NAME);
			String prototypeVisitType = context.getFlowScope().getString(PROTOTYPE_VISIT_TYPE);
			List<String> prototypeList = this.getGroupPrototype(prototypeProject, prototypeVisitType);

			// do not need to check if the prototype exists as that was already done in 
			// addReferenceData and the "_prototype" events could not be generated if the prototype
			// had not existed
			
			// iterate thru the prototype. for each instrument in the prototype, iterate thru the 
			// instrument list and if the instrument is found, put it in the group. if not found,
			// instantiate an instrument for the instrType with no id as a placeholder
			for (String instrType : prototypeList) {
				// iterate thru the instrumentGroup and find the instrTypeEncoded
				boolean found = false;
				// iterate thru the entire list until the entity whose instrType matches the prototype
				// item instrType is found, and add it to the group 
				for (ListItem listItem : instrumentList) {
					if (listItem.getEntity() != null) {
						Instrument instrument = (Instrument) listItem.getEntity();
						if (instrType.equals(instrument.getInstrType())) {
							instrumentGroup.add(instrument);
							found = true;
							break;
						}
					}
					else { // listItem.entity is null, so the list item has not been retrieved
						//TODO: the list item has not been retrieved yet, so must retrieve it
						//using listItem.id
						
						//for now ok because project-visit prototypes only used with lists that are 
						//small such that proxies do not need to be used and all list items are loaded
					}
				}					
				if (!found) {
					// all that is needed is an Instrument instance with the instrType populated to be used
					// for creating an instrument of that type, and the id null to signal that an instrument
					// should be created (if the user chooses to do so)
					Instrument missingInstrument = new Instrument();
					missingInstrument.setInstrType(instrType);
					instrumentGroup.add(missingInstrument);
				}
			}
		}
		else {
			// create the instrumentGroup from user selected instruments 

			for (ListItem listItem : instrumentList) {
				if (listItem.getSelected()) {
					if (listItem.getEntity() != null) {
						instrumentGroup.add((Instrument) listItem.getEntity());
					}
					else { // instrument has not been loaded yet
						// note that the entire list may not have entities loaded; items that have a 
						// null entity means that the entity is not loaded
						
						// at the moment, only list elements for which the entity has been loaded can 
						// be part of the selected list, which is ok, since currently only list elements 
						// which are viewed can be selected, and any list element that has been viewed 
						// has clearly been loaded. however, this will change
						
						// TODO: load the element, or create an id list of unloaded elements to load in 
						// bulk. this only needs to be implemented when it is possible to select items 
						// that have not been loaded, i.e. elements that have not been viewed
					}
				}
			}
		}
		
		if (instrumentGroup.size() == 0) {
			LavaComponentFormAction.createCommandError(errors, "instrumentGroup.nothingSelected", null);
			return new Event(this, ERROR_FLOW_EVENT_ID);
		}
		
		context.getFlowScope().put(GROUP_MAPPING, instrumentGroup);
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	protected Event doNext(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Event retEvent;

		// the lava.assessment.instrument.instrumentGroup action is a child of the parent instrument 
		// list flow, and specific events are appended per InstrumentGroupFlowTypeBuilder to create
		// flow ids, e.g. 
		// lava.assessment.instrument.instrumentGroup.enter
		// lava.assessment.instrument.instrumentGroup.deleteAll
		// the deleteAll event is a special case handled in an initial decisionState in the GroupFlowBuilder
		// where all instruments are deleted at once
		// all other events indicate an action that is to be performed on each instrument in the group
		// individually, and this method handles each one in succession
		
		// get the action to be performed on each instrument in the group, e.g. 'enter', 'delete'
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		
		List<LavaEntity> instrumentGroup = (List) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		// get the current group index (initialized to 0 in the GroupFlowBuilder)
		int groupIndex = context.getFlowScope().getInteger(GROUP_INDEX);
		if (groupIndex + 1 > instrumentGroup.size()) {
			retEvent = new Event(this, "finish");
//TODO: translate flowMode into a word/phrase for info msg, currently: Completed 'enter' on all selected instruments
// change to: Completed 'Data Entry' on all selected instruments			
			CoreSessionUtils.addFormError(sessionManager, request, new String[]{"info.instrumentGroup.complete"}, new Object[]{flowMode});
		}
		else {
			Instrument nextInstrument = (Instrument) instrumentGroup.get(groupIndex);
			
			if (nextInstrument.getId() == null) {
				retEvent = new Event(this, "missing");
				
				// create the error message and put in flow scope
				context.getFlowScope().put("missingMsg", getMessage("instrumentGroup.missing", new Object[]{nextInstrument.getInstrType()}));
				
				// put the missing instrType into flow scope so that the doAddMissing handler
				context.getFlowScope().put("missingInstrType", nextInstrument.getInstrType());
			}
			else {
				InstrumentConfig instrumentConfig = instrumentManager.getInstrumentConfig().get(nextInstrument.getInstrTypeEncoded());

				// verify that the instrument supports the flow if it is an optional instrument 
				// flow. the "edit" event encompasses the "enter", "enterReview" and "upload" actions
				// since different instruments in the flow could have different "edit"
				
				if (flowMode.equals("edit") && !instrumentConfig.supportsFlow("enter") && !instrumentConfig.supportsFlow("enterReview")
						&& !instrumentConfig.supportsFlow("upload")) {
					retEvent = new Event(this, "groupError");
						
					// create the error message and put in flow scope
					context.getFlowScope().put("groupErrorMsg", getMessage("instrumentGroup.actionNotSupported", new Object[]{flowMode, nextInstrument.getInstrType()}));
					
					// increment the index in preparation for the next instrument
					context.getFlowScope().put(GROUP_INDEX, groupIndex + 1);
				}
				else {
					// if the mode for the instrumentGroup action is "edit" then the mode for the subflow could 
					// either be "enter", "enterReview" or "upload" based on the instrument. the key is that these 
					// are assumed to be mutually exclusive.
					if (flowMode.equals("edit")) {
						if (instrumentConfig.getEnterFlow()) {
							flowMode = "enter";
						}
						else if (instrumentConfig.getEnterReviewFlow()) {
							flowMode = "enterReview";
						}
						else if (instrumentConfig.getUploadFlow()) {
							flowMode = "upload";
						}
					}

					// put instrTypeEncoded into flow scope so that when the subflow starts and needs to
					// set this (as part of setting the current action), it can access the parent instrumentGroup
					// flow and get is from its flow scope. normally, instrTypeEncoded comes from the
					// request URL when an instrument CRUD flow session is starting, but with the instrumentGroup
					// flow, instrument CRUD subflows are launched without an HTTP Request, so have to
					// do it a different way.
					// mapping instrTypeEncoded into the flow scope of the instrument CRUD subflows is not an 
					// option because when the flow session is starting, flow scope does not exist yet
					String instrTypeEncoded = nextInstrument.getInstrTypeEncoded();
					context.getFlowScope().put("instrTypeEncoded", instrTypeEncoded);
					
					// put id into flow scope so that it will be mapped into the instrument CRUD subflow
					// mapped as a String, so convert
					context.getFlowScope().put("id", nextInstrument.getId().toString());
	
					// the event prefix is determined by whether the instrument has its own flows or uses 
					// the shared instrument flows
					StringBuffer transitionEvent = new StringBuffer();
					transitionEvent.append(instrTypeEncoded);
					
					// the event suffix can be obtained from the mode (event) portion of the flow id, as the 
					// instrumentGroup mode will be the same as the mode of the instrument subflow, e.g.
					// lava.assessment.instrument.instrumentGroup.enter will iterate over instrumentGroup
					// transitioning in and out of subflows like lava.assessment.instrument.instrument.enter
					
					transitionEvent.append("__").append(flowMode);
	
					// increment the index in preparation for the next instrument
					context.getFlowScope().put(GROUP_INDEX, groupIndex + 1);
	
					retEvent = new Event(this, transitionEvent.toString());
				}
			}
		}
				
		return retEvent;
	}
	
	
	protected Event doAddMissing(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		// missing instrument currently can only happen in the case where there is an instrumentGroup
		// prototype for the project/visit type for the currentVisit which defines which instruments should 
		// be in the group. therefore, it is guaranteed that there is a currentVisit which can be
		// used when creating this new instrument to populate the required Patient, Visit, ProjName and 
		// DcDate properties
		// note: if decide that prototypes can be defined at the project or projectUnit level regardless
		// of visitType, then addMissing view would need to prompt the user to select a Visit from a 
		// visit list to use in creating the missing instrument (and would need changes commented out
		// in getGroupPrototype)
		
		// note that only with the VisitInstrumentsHandler is a project/visit type available which
		// may match an instrumentGroup prototype; all other instrument list handlers, e.g. 
		// ProjectInstrumentsHandler, do not, as the instruments in these lists could span projects and
		// visits, so for these lists there is not an instrumentGroup prototype to enforce missing
		// instruments and therefore this handler method will never be called
		Visit v = CrmsSessionUtils.getCurrentVisit(sessionManager, request);
		
		// an instrument must be created and save. the flow will then return to doNext, from which
		// the flow will transition into an instrument action subflow which will retrieve this
		// newly added instrument
		
		// use instrType to get instrTypeEncoded, which can then be used the instrument class
		// to be created from instrumentConfig
		// for versioned instruments, getting instrTypeEncoded without a version will match
		// currentVersionAlias in instrumentConfig so that the class for the latest version
		// of an instrument will be used
		// note: this is why group prototype is not configured with instrTypeEncoded instead of 
		// instrType; can not get instrType from instrTypeEncoded, and need instrType to create 
		// a new instrument for the instrType property (this is akin to the Add Instrument page 
		// where the user chooses from a list which supplies instrType)
		String instrType = context.getFlowScope().getString("missingInstrType");
		// use the exact same code that Instrument does
		String instrTypeEncoded = instrType.replaceAll("[^a-zA-Z0-9]","").toLowerCase();
		
		Class instrClass = instrumentManager.getInstrumentClass(instrTypeEncoded);
		
		Instrument instrument = null;
		if (instrClass == null) {
			// if instrument is not yet implemented, just add a record to InstrumentTracking
			instrument = Instrument.create(InstrumentTracking.class,
					v.getPatient(),
					v, 
					v.getProjName(), 
					context.getFlowScope().getString("missingInstrType"),
					v.dateWithoutTime(v.getVisitDate()),
					"Scheduled");
		}
		else {
			// create instrument-specific object for those instruments which have been implemented thus far
			instrument = Instrument.create(instrClass,
					v.getPatient(),
					v, 
					v.getProjName(), 
					context.getFlowScope().getString("missingInstrType"),
					v.dateWithoutTime(v.getVisitDate()),
					"Scheduled");
		}

		// if instrument is implemented, do post-creation stuff
		if (instrClass != null) {
			// for instruments with multiple versions, set the result fields that are not used for a specific
			// version to unused. 
			instrument.markUnusedFields(); 
		}

		instrument.save();

		// replace the missing instrument with the new instrument
		List<LavaEntity> instrumentGroup = (List) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		int groupIndex = context.getFlowScope().getInteger(GROUP_INDEX);
		instrumentGroup.set(groupIndex, instrument);
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	protected Event doConfirmDeleteAll(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		// iterate thru the entities in the group, deleting each
		// the group may be contain some or all InstrumentTracking objects (e.g. if created from an instrument list), which if 
		// deleted, will not delete the data from the instrument specific table(s). therefore, need to retrieve the actual
		// instrument specific instance and then delete it (just instantiating an instrument specific instrument and giving it
		// the correct id will not work -- Hibernate considers it a transient object and therefore will not delete it)
		List<LavaEntity> instrumentGroup = (List) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		for (LavaEntity instrument : instrumentGroup) {
			Class instrClass = instrumentManager.getInstrumentClass(((Instrument)instrument).getInstrTypeEncoded());
			Instrument specificInstrument = (Instrument) Instrument.MANAGER.getOne(instrClass, getFilterWithId(request,Long.valueOf(instrument.getId())));
			specificInstrument.delete();
		}
							
		CoreSessionUtils.addFormError(sessionManager, request, new String[]{"info.instrumentGroup.deleteAllComplete"}, new Object[]{});
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}				
	

	private List<String> getGroupPrototype(String projName, String visitType) {
		/* 
		 * gets the group prototype for a given project and visitType
		 * Use the following order (most specific to least specific):
		 * 	projName~visitType
		 * 	projName(without unit)~visitType
		 * 	ANY~visitType
		 * 
		 * could add the following, but not sure if it would ever be reasonable to define 
		 * an instrument group based on a project alone, regardless of visitType, but 
		 * allow it for now (part of problem is that if an instrument is missing and user 
		 * chooses to create it as part of the group flow, need to know the visit to use 
		 * and if there is not a currentVisit in context, would need to expand the 
		 * addMissing view to allow user to select a visit from the list of all visits (or 
		 * visits filtered by project) 
		 * 	projName-ANY
		 * 	projName(without Unit)-ANY
		 * 
		 */
		List prototypeList = new ArrayList();
		Boolean found = false;
		String lookupKey = null;
		String projectNoUnit = null;
		
		if(this.groupPrototypes==null || this.groupPrototypes.size()==0 ||
			(projName == null && visitType == null)){
			return prototypeList;
		}
		//set projName and visitType to default keys if not provided
		if(projName==null){ projName=ANY_PROJECT_KEY;}
		if(visitType==null){ visitType=ANY_VISIT_TYPE_KEY;}
		
		//try projName~visitType
		lookupKey = new StringBuffer(projName).append("~").append(visitType).toString();
		if(this.groupPrototypes.containsKey(lookupKey)){
			prototypeList = this.groupPrototypes.get(lookupKey);
			found = true;
		}
			
		//try projName(without Unit)~visitType
		if(!found && projName.contains("[")){
			projectNoUnit = projName.substring(0, projName.indexOf("[")).trim();
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(visitType).toString();
			if(this.groupPrototypes.containsKey(lookupKey)){
				prototypeList = this.groupPrototypes.get(lookupKey);
				found = true;
			}
		}
		//try ANY~visitType
		lookupKey = new StringBuffer(ANY_PROJECT_KEY).append("~").append(visitType).toString();
		if(!found && this.groupPrototypes.containsKey(lookupKey)){
			prototypeList = this.groupPrototypes.get(lookupKey);
			found = true;
		}
		
		/** not supporting prototypes which are not defined for a specific visitType for now.
		 *  see comments above.
		 
		//try projName~ANY
		lookupKey = new StringBuffer(projName).append("~").append(ANY_VISIT_TYPE_KEY).toString();
		if(!found && this.groupPrototypes.containsKey(lookupKey)){
			prototypeList = this.groupPrototypes.get(lookupKey);
			found = true;
		}
		//try (projname without unit)~ANY
		if(!found && projName.contains("[")){
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(ANY_VISIT_TYPE_KEY).toString();
			if(this.groupPrototypes.containsKey(lookupKey)){
				prototypeList = this.groupPrototypes.get(lookupKey);
				found = true;
			}
		}
		 */
		
		if (found) {
			return prototypeList;
		}
		else {
			return null;
		}
	}
	
	
	public void setGroupPrototypes(Map<String, List<String>> groupPrototypes) {
		this.groupPrototypes = groupPrototypes;
	}

}
