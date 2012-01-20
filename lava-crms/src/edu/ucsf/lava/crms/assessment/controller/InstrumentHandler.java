package edu.ucsf.lava.crms.assessment.controller;

import static edu.ucsf.lava.crms.assessment.controller.InstrumentComponentFormAction.COMPARE_INSTRUMENT;
import static edu.ucsf.lava.crms.assessment.controller.InstrumentComponentFormAction.INSTRUMENT;
import static edu.ucsf.lava.crms.session.CrmsSessionUtils.INSTRUMENT_CODES_DISPLAY_PREF;
import static edu.ucsf.lava.crms.webflow.builder.InstrumentFlowTypeBuilder.INSTRUMENT_EVENTS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.assessment.controller.cbt.FileLoader;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentConfig;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class InstrumentHandler extends CrmsEntityComponentHandler {

	private Map<String,FileLoader> fileLoaders;
	public static final String STATUS_SCHEDULED = "Scheduled";
	public static final String STATUS_COMPLETE = "Complete";
	public static final String STATUS_CANCELLED = "Cancelled";
	public static final String STATUS_PARTIALLY_COMPLETE = "Complete - Partially";
	public static final String STATUS_INCOMPLETE = "Incomplete";
	public static final String STATUS_UNKNOWN = "Unknown";
	public static final String STATUS_VERIFIED_DOUBLE_ENTRY = "Verified - Double Entry";
	public static final String STATUS_VERIFIED_REVIEW = "Verified - Review";
	public static final String STATUS_VERIFY_DEFER = "Defer";
	private static final String VISIT_LIST = "addInstrVisitList";	
    protected static final String DOUBLE_ENTER_MISMATCH_ERROR_CODE = "doubleEnterMismatch";
    protected static final String DOUBLE_ENTER_MISMATCH_AT_POS_ERROR_CODE = "doubleEnterMismatchAtPosition";
    protected static final String COMMAND_DOUBLE_ENTER_MISMATCH_ERROR_CODE = "doubleEnterMismatch.command";
    protected static final String COMMAND_DOUBLE_ENTER_MATCH_INFO_CODE = "info.doubleEnterMatch.command";
    protected static final String INCOMPLETE_MISSING_DATA_CODE = "-7"; 
    protected static final String MISSING_DATA_CODE = "-9";     
    public static final String INSTRUMENT_DETAILS = "instrumentDetails";
	
	public static final String ANY_PROJECT_KEY="ANY";
	public static final String ANY_INSTRUMENT_KEY="ANY";
	protected Map<String,Short> projectInstrumentVerifyRates;

	// accomodate subclasses that have no-args constructure, like DiagnosisHandler
	public InstrumentHandler() {
		super();
		// just replace events from superclass, because some do not apply to instrument handling
		// saveAdd,cancelAdd are specific to Mac Diagnosis
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"applyAdd","saveAdd","cancelAdd","close","confirmDelete","cancelDelete",
				"enterSave", "enterVerify", "doubleEnterSave", "doubleEnterDefer", "doubleEnterCompare",
				"enterReviewSave", "upload", "collectSave", "collectReviewRevise", "collectReviewSave", "collectReviewDefer",
				"statusSave", "cancel","refresh","reRender","save","upload","confirmChangeVersion","cancelChangeVersion",
				"custom","custom2","custom3","customEnd","customEnd2","customEnd3"}));
		// set events for which required field validation should be done
		requiredFieldEvents = new ArrayList(Arrays.asList(new String[]{"applyAdd", "saveAdd", "confirmChangeVersion",
				"collectSave", "enterSave", "enterVerify", "doubleEnterSave", "statusSave"}));

		authEvents = new ArrayList(Arrays.asList(INSTRUMENT_EVENTS));
		
		this.setPrimaryComponentContext(CrmsSessionUtils.CURRENT_INSTRUMENT);
	}
	
	public InstrumentHandler(Map<String,FileLoader> fileLoaders) {
		this();
		// beans which implement instrument specific file loading
		this.fileLoaders = fileLoaders;
	}
	
	protected String[] defineRequiredFields(RequestContext context, Object command) {
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		String event = ActionUtils.getEventName(context);

		// set the required fields based on the current action/event. these required
		// fields are enforced during the core binding process of BaseCommandController
		//
		// however, instrument result fields should NOT be set here, because required field
		// validation is handled in the InstrumentComponentFormAction instead of in onBind.
		// the reason for this is fields that are blank/null/missing fields going into onBind
		// may not necessarily be null, because of special result field binding which 
		// takes place after onBind:
		// 1) setting all null result fields to a missing data code, if the user chooses to do so
		// 2) comboRadioSelect controls used in the "collect" flow required special binding
		//    because the control is made up of two input controls
		// therefore, if result fields were designated as required fields, the core binding
		// processing could generate required field errors that no longer hold after specialized
		// result field binding in the customBindResultFields method. and since the errors
		// object which holds result field errors does not allow field errors to be removed,
		// these errors would be erroneously displayed.
		//
		// therefore, the required fields set in this method only pertain to non-result instrument 
		// fields
		
		// required fields must ultimately be specified in terms of the command object, which is a 
		// ComponentCommand, meaning that required fields are fields of the instrument object that is 
		// a map entry value of the components field. however, the initBinder method takes care of converting
		// a simple property name into the ComponentCommand syntax that is required, so only simple
		// property names need be specified herein
		// since only result fields ever use the "compareInstrument" map entry instrument object, and 
		// no result fields are specified here (they are processed in customBindResultFields), initBinder
		// need not be concerned with the "compareInstrument" component
		
		// set required fields for all events which occur in editable views, unless there are no required
		// fields in the view
		if (event.equals("applyAdd") || event.equals("saveAdd")) {
			setRequiredFields(new String[]{
					"instrType",
					"visit.id",
					"dcDate",
					"dcStatus"});
		}else if (event.equals("confirmChangeVersion")) {
	    		setRequiredFields(new String[]{"instrVer"});
	       	
	    }else if (event.equals("collectSave")||event.equals("enterSave")||event.equals("enterVerify")||event.equals("doubleEnterSave")) {
	    	if(instrument.getEntityNameEncoded().startsWith("uds")){
	    		setRequiredFields(new String[]{
	    				"packet",
	    				"formId",
	    				"visitNum",
	    				"initials"});
	    	}
	    	else {
		    	setRequiredFields(new String[0]);
	    	}
	    }
	    else if (event.equals("statusSave")) {
	    	// the dv* fields are readonly and therefore not required. they are imputed when double
	    	// enter verify is done ("enter" and "enterReview" flows, if configured to support). the
	    	// dvNotes field is not readonly, but is not a required field.
    		setRequiredFields(new String[]{
    				"dcDate",
    				"dcBy",
    				"dcStatus",
    				"deDate",
    				"deBy",
    				"deStatus"//,
    				//"researchStatus",
	    			//"qualityIssue"
    				});
	    }
	    else {
	    	setRequiredFields(new String[0]);
	    }
		
		return getRequiredFields();
	}
	
	
	// override to check if visit list for add is empty after permission filtering
	public Event authorizationCheck(RequestContext context, Object command) throws Exception {
		// first do standard permissions check
		Event returnEvent = super.authorizationCheck(context, command);
		
		if (returnEvent.getId().equals(this.SUCCESS_FLOW_EVENT_ID)) {
			String authEvent = ActionUtils.getFlowMode(context.getActiveFlow().getId());
			if (authEvent.equals("add")) {
				// next, check that the user has permission to add instrument to at least one visit
				HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
				Map<String,String> visitList = listManager.getDynamicList(getCurrentUser(request),
						"visit.patientVisits", "patientId",
						((Instrument)((ComponentCommand)command).getComponents().get(INSTRUMENT)).getPatient().getId(),
						Long.class);
				visitList = filterVisitListByPermission(getCurrentUser(request),
						CoreSessionUtils.getCurrentAction(sessionManager,request), visitList, true);
				if (visitList.size() == 1) { // there will always be the blank entry, so list size 1 means empty list
					CoreSessionUtils.addFormError(sessionManager,request, new String[]{"authorization.noInstrumentVisits.command"}, null);
					return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
				}
			}
			return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
		}
		else {
			return returnEvent;
		}
	}
	

	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Class instrClass = null;
		Instrument instrument = null;
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		
		// not calling super.getBackingObjects because this handler is shared among all instruments and so the
		// class of entity being created varies, so can not set the class via setHandledEntity as that is an
		// instance variable and this is a multi-threaded singleton, and therefore can not use the superclass
		// (CrmsEntityComponentHandler) getBackingObjects because it uses that instance variable
		// but, do use the entity name (via getDefaultObjectName) and since it is always the same across
		// instruments (currently it is "instrument"), that is thread safe. so have to call setHandledEntity
		// just for setting getDefaultObjectName
		setHandledEntity("instrument", null); 

		Map backingObjects = new HashMap<String,Object>();
		if (flowMode.equals("add")) {
			// must use InstrumentTracking.class not Instrument.class, because the latter is not mapped for other reasons (see
			// InstrumentTracking class)
			backingObjects.put(getDefaultObjectName(), initializeNewCommandInstance(context,InstrumentTracking.MANAGER.create()));
		}else{
			Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager,request);
			String instrTypeEncoded = currentAction.getTarget();
			//get backing object based on id
			// the "id" parameter is stored in flow scope by the flow definition
			String id = context.getFlowScope().getString("id");
			if(StringUtils.isNotEmpty(id)) {
				// special case for delete instrument while supporting delete for unimplemented instruments.
				// since there is no model class/mapping for unimplemented instruments, use the general
				// InstrumentTracking class
				if (instrTypeEncoded.equals("instrument")) {
					// do not use Instrument.class because Hibernate would do a polymorphic query, which is
					// not necessary (which is why InstrumentTracking exists and why Instrument is not mapped)
					instrClass = InstrumentTracking.class;
				}
				else {
					instrClass = instrumentManager.getInstrumentClass(instrTypeEncoded);
				}
				
				instrument = (Instrument) Instrument.MANAGER.getOne(instrClass, getFilterWithId(request,Long.valueOf(id)));
				backingObjects.put(getDefaultObjectName(), instrument);
			}else{
				throw new RuntimeException(metadataManager.getMessage("idMissing.command", new Object[]{instrTypeEncoded}, Locale.getDefault()));
			}

			
			// setup another backing object for double enter verify (which may or may not be used in a given
			// flow, depending upon the project and instrument, but it is simpler just to create it here if needed
			// rather having logic to conditionally create it in multiple places) 
			
			// note: the "compareInstrument" entity is not put into the handledObjects map for this handler, 
			// because do not want this entity to be saved. it is purely to support the double entry functionality.
			// if user has cancelled to go back to view and then goes to enter again, component will already
			// exist, so check first
			Instrument compareInstrObj = (Instrument) Instrument.MANAGER.create(instrClass);
			compareInstrObj.setInstrType(instrument.getInstrType());
			compareInstrObj.setInstrVer(instrument.getInstrVer());
			// do the same setup for result fields as is done when adding a new instrument (see doSaveAdd)
			compareInstrObj.markUnusedFields(); // marks all result fields not used by this specific version
			backingObjects.put(COMPARE_INSTRUMENT, compareInstrObj);
			
	
			// special handling for CBT instruments, which have may have one or more detail components
			InstrumentConfig instrumentConfig = instrumentManager.getInstrumentConfig().get(instrument.getInstrTypeEncoded());
			if (instrumentConfig.getUploadFlow()) {
				// obtain the bean to perform instrument specific file loading
				FileLoader fileLoader = fileLoaders.get(instrTypeEncoded + "FileLoader");
				// note that it is possible for CBT instruments to not have a details table. the base 
				// BaseCbt class still instantiates a details collection, but even though it is added to 
				// the backingObjects here it is ignored and does no harm
				fileLoader.setDetailComponents(context, backingObjects);
			}
		}

		return backingObjects;
	}
		
	protected Object initializeNewCommandInstance(RequestContext context,Object command){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		// note: since this is called on applyAdd as well as on initial creation of a command object,
		//       the command could be either an InstrumentTracking object, or an instrument specific
		//       object of the type just added, so cast to Instrument to handle both possibilities
		
		
		// init currentPatient. currentPatient should not be null, as user is not given option of
		// adding instrument if no patient is selected 
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		((Instrument)command).setPatient(p);
		
	
		// determine whether visit can be initiailized to currentVisit here, because user may not have 
		// permissions to add instrument within the project of the current visit
		// this is expensive, because it repeats the code that is in addReferenceData, but obtain the
		// list of visits (which filters it for project access authorization) and filter it for permissions,
		// and if the current visit is in the resulting list, init the new instrument's visit 
		Map<String,String> visitList = listManager.getDynamicList(getCurrentUser(request),
				"visit.patientVisits", "patientId",  
				((Instrument)command).getPatient().getId(), 
				Long.class);
		visitList = filterVisitListByPermission(getCurrentUser(request),
				CoreSessionUtils.getCurrentAction(sessionManager,request), visitList, true);
		// save visit list for reference data so do not have to query and filter each time 
		// an add is done (since the visit list is static for the life of the page)
		request.getSession().setAttribute(VISIT_LIST, visitList);
		
		// if the current Visit is in the list, use it to initialize the new instrument visit property
		Visit v = CrmsSessionUtils.getCurrentVisit(sessionManager, request);
		if (v != null) {
			boolean currentVisitInList = false;
			for (Entry<String,String> entry : visitList.entrySet()) {
				if (entry.getKey().length() > 0 && v.getId().equals(Long.valueOf(entry.getKey()))) {
					currentVisitInList = true;
					break;
				}
			}
			if (currentVisitInList) {
				((Instrument)command).setVisit(v);
				// as a convenience to user, pre-populate dcDate and dcStatus which are required on the
				// add instrument view (because they are required columns in the database)
				((Instrument)command).setDcDate(v.getVisitDate());
				((Instrument)command).setDcStatus(STATUS_SCHEDULED);
				
				// init project to visit's project
				((Instrument)command).setProjName(v.getProjName());
			}
		}
		
		// if the instrument visit is still null, create a blank visit because instrument must have a 
		// visit object to bind to
		if (((Instrument)command).getVisit() == null) {
			((Instrument)command).setVisit((Visit)Visit.MANAGER.create());
		}

		// set instrument notes collection field to null b/c after using stored procs to add and reattaching
		// instrument object to Hiberante session, get exception:
		//  org.hibernate.HibernateException: reassociated object has dirty collection reference (or an array)
		// this is ok since notes fields are not editable on instrument add
		// update: not sure if this is applicable anymore, since not reattaching instr object to Hibernate session
		((Instrument)command).setNotes(null);
		
		return command;
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		String stateId = context.getCurrentState().getId();
		String event = ActionUtils.getEventName(context);
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		
		// add map of which instruments have been implemented and which flows they support, used to 
		// determine which action buttons the decorator displays
		model.put("instrumentConfig", instrumentManager.getInstrumentConfig());
		if (flowMode.equals("add")) {
			// adding a new instrument requires the list of visits for the patient in context so user can 
			// select which visit they are adding the instrument to
			// get the visit list that was created in initializeNewCommandInstance
			Map<String,String> visitList = (Map<String,String>) request.getSession().getAttribute(VISIT_LIST);
			dynamicLists.put("visit.patientVisits", visitList);
		}
		
		if (flowMode.equals("view")) {
			model.put("showCodes", Boolean.TRUE); // indicate to view that codes are being shown
			addInstrumentListsToModel(model,instrument);
		
		}
		
		if (flowMode.equals("enter") || flowMode.equals("enterReview") || flowMode.equals("collect")) {
			//			 note: ideally, want codes to display as follows:
			//       - when editing a new instrument, do not want codes to display to clutter
			//         screen (but how to know it is a "new" instrument ? check statuses ?)
			//       - when editing an existing instrument, check all fields to see if any
			//         of them have a code for a value, in which case the codes should be
			//         displayed (for all fields)
			//         note: displaying codes on a per property basis adds much complexity in
			//               terms of javascript. also, a potential issue is should their
			//               be state for these per property settings, so if user moves to
			//               another page can comes back their per field setting would be
			//               retained. solving this would add to the complexity
		    //   
			//       - however, user should be able to override when codes are not shown and
			//         choose to display codes, in which case they should obviously display
			//       - if user is allowed to hide codes when displayed, this must be subject
			//         to whether any of the properties have a code as its value, in which case
			//         codes must be displayed
			//       
			//       until decisions are made on the desired behavior, do the following:
			//   
			//       - if the user chooses to show/hide codes, honor the user
			//         (NOTE: if the user hides codes but the value of the property is a code, the code
			//			is added to the input control for display purposes
			//       - if there are validation errors, show codes
			//       - if new instrument, hide codes
			//       - if existing instrument, show codes
			
			// see if the user has chosen to show or hide codes in the current request
			String codes = null;
			if (event.startsWith("hideCodes")) {
				codes = "hideCodes";
				// save user preference in their session
				request.getSession().setAttribute(INSTRUMENT_CODES_DISPLAY_PREF, codes);
			}
			else if (event.startsWith("showCodes")) {
				codes = "showCodes";
				// save user preference in their session
				request.getSession().setAttribute(INSTRUMENT_CODES_DISPLAY_PREF, codes);
			}
			// see if user has chosen to show or hide codes in the current session
			else if ((codes = (String) request.getSession().getAttribute(INSTRUMENT_CODES_DISPLAY_PREF)) != null) {
				// user chose on an earlier request whether to show or hide codes, so use that
			}
			else if (errors.getErrorCount() > 0) {
				// if there are validation errors, display the lists with all codes, so that the
				//  user has the option of setting property values to the codes in order to 
				//  pass validation
				codes = "showCodes";
			}
			//TODO: check statuses to determine if a newly added instrument. if so, hideCodes
			else { // default
				codes = "showCodes";
			}
		
			// put the lists with or without codes in the model. however, for the review state of the 
			// collect flow, where everything is readonly, do as for view flow above, i.e. set showCodes
			// true and put listsWithCodes in model, so that code values are translated into code text
			if (flowMode.equals("collect") && stateId.equals("review")) {
				model.put("showCodes", Boolean.TRUE); // indicate to view that codes are being shown
				addInstrumentListsToModel(model,instrument);
				
			}
			else {
				if (codes.equals("hideCodes")) {
					model.put("showCodes", Boolean.FALSE); // indicate to view that codes are not being shown
					addInstrumentListsToModel(model,instrument);
					
				}
				else {
					model.put("showCodes", Boolean.TRUE); // indicate to view that codes are being shown
					addInstrumentListsToModel(model,instrument);
					
				}
			}
			
			if(flowMode.equals("enter") && (event.equals("enterSave") || event.equals("statusSave"))) {
				model.put("hasMissingOrIncompleteFields", instrument.hasMissingOrIncompleteFields());
			}
			
	
		}
		
		// all flows which display result fields
		if (flowMode.equals("view") || flowMode.equals("enter") || flowMode.equals("enterReview") 
				|| flowMode.equals("collect") || flowMode.equals("upload") || flowMode.equals("delete")) {
			
			// map of list of all codes to text, so view can convert numeric codes to text. 
			// for a given instrument, the codes used across the board should be the same.
			// each instrument may have a different codes list, e.g. FAQ has both std error and skip error
			// codes, and BRDRS requires std error codes in decimal format since its result values have
			// decimal domain
			Map map = listManager.getDefaultStaticList(instrument.getInstrTypeEncoded()+".codes");
			model.put("missingCodesMap", map);

			// this is only need for informant instruments 
			dynamicLists.put("instrument.informants", 
					listManager.getDynamicList("instrument.informants", 
					"patientId", instrument.getPatient().getId(),Long.class));
		}

	
		
	 	if (flowMode.equals("view") && stateId.equals("print")) {
	 		// this is the data source provided for the instrument report status, header, footer subreports.
	 		// they are configured as subreports because this is essentially how to include files in Jasper
	 		// reports, so that there is only one copy of the status, header and footer sections for all 
	 		// instrument reports. 
	 		// a data source is a collection which contains a single element: instrument object. however, 
	 		// each time a data source is accessed by a subreport the report engine iterates to the next
	 		// element of the collection. this means that only the header on the first page would be output
	 		// and then nothing would be output for subsequent headers, the footers, and instrument status.
	 		// the kludge to get around this is a combination of things:
	 		// 1) do not pass the data source properties to the subreports as parameters, because the first
	 		//    instance of a subreport (the first page header) would iterate the data source to the next
	 		//    row, and so the instrument would not be available for the instrument status subreport
	 		//    (instead, the subreports get their values from the "instrTrackingDataSource" data source)
	 		// 2) have the subreports specify whenNoDataType="AllSectionsNoDetail" so they will still be output
	 		//    even when the report engine finds there are no additional records in the data source
	 		// 3) put the band within a <group> instead of a <detail> because AllSectionsNoDetail from 2)
	 		//    means that anything inside <detail> is not output
 			model.put("instrTrackingDataSource", new Object[] {((ComponentCommand)command).getComponents().get(getDefaultObjectName())});
	 	}
		
	 	
		if(flowMode.equals("changeVersion")){
			dynamicLists.put("instrument.versions", 
					listManager.getDynamicList("instrument.versions", 
							"instrument", instrument.getInstrTypeEncoded(false),String.class));
		}
		
		if(instrument.getInstrTypeEncoded()!= null && instrument.getInstrTypeEncoded().startsWith("uds")){
            dynamicLists.put("udsinstrument.formId",
            		listManager.getDynamicList("udsinstrument.formId",
							"instrument", instrument.getInstrTypeEncoded(false),String.class));
		}
		
		// project.staffList is used by the instrument status page and also a number of instruments, so
		// just query it for all flows
		if (instrument.getProjName()!= null){
			dynamicLists.put("project.staffList", listManager.getDynamicList("project.projectStaffList", 
						"projectName", instrument.getProjName(),String.class));
			}
		else {
			dynamicLists.put("project.staffList", new HashMap<String, String>());
		}
		
		model.put("dynamicLists", dynamicLists);
		
		return super.addReferenceData(context, command, errors, model);
	}

	protected void addInstrumentListsToModel(Map model,Instrument instrument){
			addListsToModel(model, listManager.getStaticListsForEntity(instrument.getInstrTypeEncoded()));

			//TODO: the use of currentVersionAlias for this is temporary. that is for instrument
			//versioning, so need something like instrumentAlias
			// determine if the instrument is an alias for a target instrument, in which case the
			// lists for the target instrument should be added
			InstrumentConfig instrumentConfig = instrumentManager.getInstrumentConfig().get(instrument.getInstrTypeEncoded());
			if (!instrumentConfig.getInstrTypeEncoded().equals(instrument.getInstrTypeEncoded())) {
				addListsToModel(model, listManager.getStaticListsForEntity(instrumentConfig.getInstrTypeEncoded()));
			}
			
			if(instrument.getInstrTypeEncoded().startsWith("uds")){
				addListsToModel(model,listManager.getStaticListsForEntity("udsinstrument"));
			}
	}
			
		
		
	public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		// check the flow id, and if necessary, the event id, to determine how to set componentMode
		// and componentView for this component

		// componentMode is used by the jsp (in particular by the createField tag) to determine (along
		// with property metadata) how a given property should be displayed
		
		// componentView is used for 2 purposes
		// 1) determines what buttons (and their events) are displayed, where this determination may be made
		// in conjunction with the flow, as multiple flows may use the same componentView but have different
		// buttons/events
		// 2) it is used to categorize the nature of a view such that the decorators/jsps/tags can use
		// it their logic to do different things
		//
		// it is important to understand that a componentView be more general than a specific flow/state
		// combination because each componentView is like a view category, where the view code (jsp) can 
		// make logical decisions based on the category, regardless of which flow/state is involved. thus,
		// a given componentView can be used by more than one flow
		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		String stateId = context.getCurrentState().getId();
		String event = ActionUtils.getEventName(context);
				
		if (flowMode.equals("add")) {
			setComponentMode(request, getDefaultObjectName(), "dc");
			setComponentView(request, getDefaultObjectName(), "add");
		}
		else if (flowMode.equals("delete")) {
			setComponentMode(request, getDefaultObjectName(), "vw");
			setComponentView(request, getDefaultObjectName(), "delete");
		}
		else if (flowMode.equals("view")) {
			setComponentMode(request, getDefaultObjectName(), "vw");
			setComponentView(request, getDefaultObjectName(), "view");
		}
	
		else if (flowMode.equals("enter")) {
			if (stateId.equals("enter")) {
				setComponentMode(request, getDefaultObjectName(), "de");
				setComponentView(request, getDefaultObjectName(), "enter");
			}
			else if (stateId.equals("doubleEnter")) {
				// define redundant component mode and view attibutes, because the jsp's and decorator use
				// "instrument_mode" and "instrument_view" to obtain the mode and view (since the mode and view
				// is always the same regardless of whether the component is instrument or compareInstrument)
				// but createField uses the component, which will be set to "compareInstrument" for doubleEnter, 
				// so also set "compareInstrument_mode" and "compareInstrument_view"
				setComponentMode(request, getDefaultObjectName(), "de");
				setComponentView(request, getDefaultObjectName(), "doubleEnter");
				setComponentMode(request, COMPARE_INSTRUMENT, "de");
				setComponentView(request, COMPARE_INSTRUMENT, "doubleEnter");
			}
			else if (stateId.equals("doubleEnterCompare")) {
				setComponentMode(request, getDefaultObjectName(), "de");
				setComponentView(request, getDefaultObjectName(), "compare");
			}
			else if (stateId.equals("editStatus")) {
				setComponentMode(request, getDefaultObjectName(), "dc");
				setComponentView(request, getDefaultObjectName(), "editStatus");
			}
		}
		else if (flowMode.equals("enterReview")) {
			if (stateId.equals("enterReview")) {
				setComponentMode(request, getDefaultObjectName(), "de");
				setComponentView(request, getDefaultObjectName(), "enter");
			}
			else if (stateId.equals("review")) {
				setComponentMode(request, getDefaultObjectName(), "vw");
				setComponentView(request, getDefaultObjectName(), "review");
			}
			else if (stateId.equals("doubleEnter")) {
				// define redundant component mode and view attibutes, because the jsp's and decorator use
				// "instrument_mode" and "instrument_view" to obtain the mode and view (since the mode and view
				// is always the same regardless of whether the component is instrument or compareInstrument)
				// but createField uses the component, which will be set to "compareInstrument" for doubleEnter, 
				// so also set "compareInstrument_mode" and "compareInstrument_view"
				setComponentMode(request, getDefaultObjectName(), "de");
				setComponentView(request, getDefaultObjectName(), "doubleEnter");
				setComponentMode(request, COMPARE_INSTRUMENT, "de");
				setComponentView(request, COMPARE_INSTRUMENT, "doubleEnter");
			}
			else if (stateId.equals("doubleEnterCompare")) {
				setComponentMode(request, getDefaultObjectName(), "de");
				setComponentView(request, getDefaultObjectName(), "compare");
			}
			else if (stateId.equals("editStatus")) {
				setComponentMode(request, getDefaultObjectName(), "dc");
				setComponentView(request, getDefaultObjectName(), "editStatus");
			}
		}
		else if (flowMode.equals("upload")) {
			if (stateId.equals("upload")) {
				setComponentMode(request, getDefaultObjectName(), "de");
				setComponentView(request, getDefaultObjectName(), "upload");
			}
			else if (stateId.equals("review")) {
				setComponentMode(request, getDefaultObjectName(), "vw");
				setComponentView(request, getDefaultObjectName(), "review");
			}
			else if (stateId.equals("editStatus")) {
				setComponentMode(request, getDefaultObjectName(), "dc");
				setComponentView(request, getDefaultObjectName(), "editStatus");
			}
		}
		else if (flowMode.equals("collect")) {
			if (stateId.equals("collect")) {
				setComponentMode(request, getDefaultObjectName(), "dc");
				setComponentView(request, getDefaultObjectName(), "collect");
			}
			else if (stateId.equals("review")) {
				setComponentMode(request, getDefaultObjectName(), "vw");
				setComponentView(request, getDefaultObjectName(), "review");
			}
			else if (stateId.equals("editStatus")) {
				setComponentMode(request, getDefaultObjectName(), "dc");
				setComponentView(request, getDefaultObjectName(), "editStatus");
			}
		}
		else if (flowMode.equals("status")){
			if (stateId.equals("status")) {
				setComponentMode(request, getDefaultObjectName(), "vw");
				setComponentView(request, getDefaultObjectName(), "status");
			}
		}
		else if (flowMode.equals("editStatus")) {
			setComponentMode(request, getDefaultObjectName(), "dc");
			setComponentView(request, getDefaultObjectName(), "editStatus");
		}
		else if (flowMode.equals("changeVersion")) {
			setComponentMode(request, getDefaultObjectName(), "vw");
			setComponentView(request, getDefaultObjectName(), "changeVersion");
		}
	 }
	
	
	
	// EVENT HANDLING METHODS

	// override to use regular expression matching on event so that instruments with details
	// can submit list navigation events using whatever the detail component name is as the prefix
	public boolean handlesEvent(RequestContext context) {
		// note: do not use "String event = ActionUtils.getEventParameter(request)" because it is possible for a flow
		//  to set an overrideEvent which will be different from the event request parameter
		String eventId = ActionUtils.getEventId(context);
		
		for (String handledEvent: handledEvents){
			if (eventId.matches(handledEvent)) {
				return true;
			}
		}
		return false;
	}

	// instruments with details have special handling in that they handle basic list navigation events for 
	// their detail lists, so that their detail lists do not require a handler themselves (just for these events)
	protected void setDefaultHandledEvents(String defaultObjectName){
		super.setDefaultHandledEvents(defaultObjectName);
		
		// add special handling 
		this.handledEvents.add(".*__prevPage");
		this.handledEvents.add(".*__nextPage");
		this.handledEvents.add(".*__recordNav");
		this.handledEvents.add(".*__pageSize");
	}
	
	// override base class doSaveAdd (called on "applyAdd", "saveAdd" event)
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception{
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		// note: re: the ability to repeatedly add instruments, the form backing object, i.e. command object
		//  is used for binding user input from the form, and then an instrument-specific object is created
		//  for the actual database add. so two instances are required, one instance is of type InstrumentTracking,
		//  used for the formBackingObject, and a second instance which is of the specific type of instrument
		//  being added, is used for the DAO add
		
		// UPDATE: this is essentially non-standard use of a DTO, i.e. was done prior to formal usage of DTO
		// with "dto" package and mappers

		// note on context: 
		// if there is a current visit in the context, that is the visit that is used to pre-populate the Add
		// Instrument visit field (if the user has authorization to add instruments in the project of the
		// visit), but the visit can be changed by the user. currently, adding an instrument does
		// not result in setting the currentInstrument in the context, and thus in turn does not result in
		// setting the currentVisit in context to the visit to which the instrument was added
	
		// use the populated fields from the command object component, to create an object of the specific instrument 
		// type as input by the user in the instrType field. this must be done before saving, i.e. before calling the
		// superclass doSaveAdd method 
		
		// as mentioned, two instances are used for repeat add (applyAdd event). therefore, must save the
		// form backing object for use in repeat add (applyAdd), because property value(s) need to be cleared,
		// and clearing the instr-specific object fields after Hibernate adds the entity results in 
		// exceptions: a) identifier altered to null, if id is set null and b) not-null property is null or transient,
		// if any not-null fields are set null
		// this is because DAO is configured to use JTA transactions with the PROPAGATION_REQUIRED propagation
		// rules, such that the transaction spans the entire request, and the data is not flushed until 
		// the request has been processed, so dirty checking during the flush detects these null field values.
		// this could be resolved by changing the propagation rule to PROPAGATION_REQUIRES_NEW to set the transaction 
		// granularity to be per-service method, i.e. the same object can be used for saving to the db
		// and for form binding and fields can be set null after the save. however, this transaction granularity
		// is not desired, so that is not a solution, and two instances are used instead.
		// note: see VisitHandler for same situation, different comments
		Instrument instrObjFormBacking = ((Instrument)((ComponentCommand)command).getComponents().get(INSTRUMENT));
		
		// if the selected visit is not the currentVisit, retrieve it, as the Visit object is needed as a property
		// on the new instrument, and also the projName for new instrument comes from Visit
		Long vid = instrObjFormBacking.getVisit().getId();
		Visit v = CrmsSessionUtils.getCurrentVisit(sessionManager, request);
		if (v == null || !vid.equals(v.getId())) {
			v = (Visit)Visit.MANAGER.getOne(getFilterWithId(request,vid));
		}
		
		Instrument instrObjDao = null;
		Class instrClazz =instrumentManager.getInstrumentClass(instrObjFormBacking.getInstrTypeEncoded());
		// only implemented instrument classes are known, so all others will return null for their Class and 
		// they use stored procs for their creation so it does not matter if the new instrument data is in 
		// an InstrumentTracking object or an instrument-specific object, but have to use InstrumentTracking since 
		// unimplemented instruments do not have a model class (which is why they are unimplemented !)
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		try {
			if (instrClazz == null) {
				instrObjDao = (Instrument) Instrument.create(InstrumentTracking.class,
						instrObjFormBacking.getPatient(),
						v, 
						v.getProjName(), 
						instrObjFormBacking.getInstrType(),
						instrObjFormBacking.getDcDate(),
						instrObjFormBacking.getDcStatus());
			}
			else {
				// create instrument-specific object for those instruments which have been implemented thus far
				instrObjDao = Instrument.create(instrClazz,
						instrObjFormBacking.getPatient(),
						v, 
						v.getProjName(), 
						instrObjFormBacking.getInstrType(),
						instrObjFormBacking.getDcDate(),
						instrObjFormBacking.getDcStatus());
			}
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}

		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			// no need to do the following for unimplemented instruments
			if (instrClazz != null) {
				// for instruments with multiple versions, set the result fields that are not used for a specific
				// version to unused. 
				instrObjDao.markUnusedFields(); 
	
			}

			// put the new instrument-specific object into the command object, replacing the InstrumentTracking object 
			// created in getBackingObjects
			((ComponentCommand)command).getComponents().put(INSTRUMENT, instrObjDao);

			returnEvent = super.doSaveAdd(context, command, errors);
			
			if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"info.instrument.added"}, new Object[]{instrObjFormBacking.getInstrType()}, ""));
		
				// if repeatedly adding new instruments, the form backing object must be cleared out so the fields on the 
				// form are cleared after they "Add" 
				// note: currently, user can only "applyAdd", i.e. repeatedly add instruments until they "close", but test
				//  the event in case "saveAdd" is offered to the user, where they add a single instrument by clicking "save"
				//  to effectively add and close
				String event = ActionUtils.getEventName(context);
				if (event.equals("applyAdd")) {
					// above, an instrument-specific object (or InstrumentTracking object for unimplemented instruments) was 
					// created and put into command so it would be added to the database, but now put the form backing object 
					// back in command for the next add
					// note: the instrObjFormBacking object has a null id, which is important so that the instrument DAO 
					//  knows to do an add rather than an update
					((ComponentCommand)command).getComponents().put(INSTRUMENT, instrObjFormBacking);
		
					// reset instrType to null for next add. can leave visit and related properties as is for next 
					// add, since user will likely add to the same visit. id is irrelevant because when add actually takes
					// place, an instrument-specific object is created with a null id (though while there are still
					// unimplemented instrments, id must be explicity set null for those (done above), since they use form 
					// backing object for binding AND the add)
					instrObjFormBacking.setInstrType(null);
				}
			}
		}

		return returnEvent;
	}


	// override base class doReRender
	// for add instrument, update the pre-populated dcDate and dcStatus based on the visit the user has 
	// selected (selecting visit submits with refresh event)
	
	// for edit instrument ("enter", "enterReview", "collect" flows), the default is to do nothing as this is 
	// typically just used to refresh the page in response to user input via uitags, where binding takes place
	// and addReferenceData is called and there is no special handling needed. of course, instrument specific handlers can 
	// override this to do custom handling, or they can submit the __custom events and handle those 
	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 

		if (flowMode.equals("add")) {
			// retrieve the visit 
			Long vid = instrument.getVisit().getId();
			Visit v = (Visit)Visit.MANAGER.getOne(getFilterWithId(request,vid));
			if (v != null) {
				instrument.setDcDate(v.getVisitDate());
				// pre-populate dcStatus
				if (v.getVisitStatus().equalsIgnoreCase("Came In")) {
					instrument.setDcStatus(STATUS_COMPLETE);
				}
				else if (v.getVisitStatus().equalsIgnoreCase(STATUS_SCHEDULED)) {
					instrument.setDcStatus(STATUS_SCHEDULED);
				}
			}
		}
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		String stateId = context.getCurrentState().getId();
		String event = ActionUtils.getEventName(context);
		String eventComponent = ActionUtils.getEventObject(context);
		Boolean handlerFound = true;

		// for multi-step flows, handle events by flow, which allows these flows to use the same event names yet
		// handle them differently, i.e. these flows have their own event namespace. this does not mean that the
		// handler method is different -- if they require the same handling as another flow, the handle..Event method
		// is shared
		if (flowMode.equals("enter")) {
			// enter flow events
			// "enterSave" - enter state - transitions to doubleEnter state if configured, else to editStatus state
			// "enterVerify" - enter state - transitions to doubleEnter state
			// "doubleEnterSave" - doubleEnter state - transitions to compare state
			// "doubleEnterDefer" - doubleEnter state - transitions to editStatus state
			// "doubleEnterCompare" - compare" state - transitions to editStatus state
			// "upload" - enter state - transitions back to enter state after uploading file and populating properties

			if(event.equals("enterSave")){
				return this.handleEnterSaveEvent(context,command,errors);
			}
			else if(event.equals("enterVerify")){
				return this.handleEnterVerifyEvent(context,command,errors);
			}
			else if(event.equals("doubleEnterSave")){
				return this.handleDoubleEnterSaveEvent(context,command,errors);
			}	
			else if(event.equals("doubleEnterDefer")){
				return this.handleDoubleEnterDeferEvent(context,command,errors);
			}	
			else if(event.equals("doubleEnterCompare")){
				return this.handleDoubleEnterCompareEvent(context,command,errors);
			}
			if(event.equals("upload")){
				return this.handleUploadEvent(context,command,errors);
			}
			else {
				handlerFound = false;
			}
		}
		else if (flowMode.equals("enterReview")) {
			// enterReview flow events
			// "enterSave" - enterReview state - transitions to review state
			// "enterReviewSave" - review state - transitions to doubleEnter state if configured, else to editStatus state
			// "enterReviewRevise" - review state - transitions to enterReview state (does not need a handler)
			// "enterReviewVerify" - review state - transitions to doubleEnter (does not need a handler) 
			// "doubleEnterSave" - doubleEnter state - transitions to compare state
			// "doubleEnterDefer" - doubleEnter state - transitions to editStatus state
			// "doubleEnterCompare" - compare" state - transitions to editStatus state
			
			if(event.equals("enterSave")){
				return this.handleEnterReviewSaveEvent(context,command,errors);
			}
			else if(event.equals("enterReviewSave")){
				return this.handleEnterReviewReviewSaveEvent(context,command,errors);
			}
			else if(event.equals("doubleEnterSave")){
				return this.handleDoubleEnterSaveEvent(context,command,errors);
			}	
			else if(event.equals("doubleEnterDefer")){
				return this.handleDoubleEnterDeferEvent(context,command,errors);
			}	
			else if(event.equals("doubleEnterCompare")){
				return this.handleDoubleEnterCompareEvent(context,command,errors);
			}	
			else {
				handlerFound = false;
			}
		}
		else if (flowMode.equals("upload")) {
			// upload flow events
			// "upload" - upload state - transitions to review state
			// "uploadReviewSave" - review state - transitions to editStatus (no handler needed) 		
			// "uploadReviewRevise" - review state - transitions to upload state (no handler needed)
			if(event.equals("upload")){
				return this.handleUploadEvent(context,command,errors);
			}
			else {
				handlerFound = false;
			}
		} else if (flowMode.equals("collect")) {
			// collect flow events
			// "collectSave" - collect state - transitions to review state 
			// "collectReviewRevise" - review state - transitions to collect state (no handler needed)
			// "collectReviewSave" - review state - transitions to editStatus state
			// "collectReviewDefer" - review state - transitions to editStatus state
			if(event.equals("collectSave")){
				return this.handleCollectSaveEvent(context,command,errors);
			}
			else if(event.equals("collectReviewSave")){
				return this.handleCollectReviewSaveEvent(context,command,errors);
			}	
			else if(event.equals("collectReviewDefer")){
				return this.handleCollectReviewDeferEvent(context,command,errors);
			}	
			else {
				handlerFound = false;
			}
		}
			
		// the rest of these events are common across all flows that use them 
	    // "statusSave" in both enter, enterReview, upload and collect flows is signaled in the status view-state
		if(event.equals("statusSave")){
			return this.handleStatusSaveEvent(context,command,errors);
		}
		// pass the event component to page navigation event handlers. these handle the list navigation
		// for detail components, and need to know the name of which detail component to navigate 
		else if(event.equals("prevPage")){
			return this.handlePrevPageEvent(context,command,errors,eventComponent);
		}
		else if(event.equals("nextPage")){
			return this.handleNextPageEvent(context,command,errors,eventComponent);
		}
		else if(event.equals("recordNav")){
			return this.handleRecordNavEvent(context,command,errors,eventComponent);
		}
		else if(event.equals("pageSize")){
			return this.handlePageSizeEvent(context,command,errors,eventComponent);
		}
		else if(event.equals("confirmChangeVersion")){
			return this.handleConfirmChangeVersionEvent(context,command,errors);
		}
		else if(event.equals("cancelChangeVersion")){
			return this.handleCancelChangeVersionEvent(context,command,errors);
		}
		else {
			handlerFound = false;
		}
		
		if (handlerFound) {
			return new Event(this,SUCCESS_FLOW_EVENT_ID);
		}
		else {
			// this should never happen because the FormAction calls handlesEvent on each handler and
			// returns an error without calling handleFlowEvent, but if there is an event in the defaultEvents
			// array which is not handled, could get here
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"flowEventNotHandled.command"}, new Object[] {event}, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
	}

	
// "enter" FLOW EVENT HANDLERS
	/**
	 * "enter" flow, "enter" state. save the data entry and if there will be an automatic verify, set flag to transition
	 * to "doubleEnter" state, otherwise transition to "editStatus" state
	 */
	public Event handleEnterSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doEnterSaveEvent(context,command,errors);
	}

	protected Event doEnterSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		InstrumentConfig instrumentConfig = instrumentManager.getInstrumentConfig().get(instrument.getInstrTypeEncoded());
		
		// determine whether verify (double enter) should be done or skipped 
		
		// examine the instrument config to see if the verify flag is set for this instrument. if not, 
		// skip double enter. 
		if (instrumentConfig.getVerify()) {
			// if the instrument has already been successfully double entered then double enter should not be made
			// mandatory again (but the user will still have the option of doing double enter via the Double Enter button)
			if (instrument.getDvStatus() != null && instrument.getDvStatus().equals(STATUS_VERIFIED_DOUBLE_ENTRY)) {
				context.getFlowScope().put("mandatoryDoubleEnter", Boolean.FALSE);
			}
			else {
				// examine the project/instrument configuration data to determine  whether verify should 
				// be mandatory as the next step in the flow, based on the verify rate 
				Short rate = this.getProjectInstrumentVerifyRate(instrument.getProjName(), instrument.getInstrTypeEncoded());
	
				if (rate > 0) {
	
					// TODO: based upon the rate, determine whether double enter should be done this time. 
					// for now, rate is either 0 or 100, i.e. either double enter is never mandatory or double
					// enter is always mandatory
					
					// set a flag in flow scope so that the flow decision state knows to proceed to the 
					// doubleEnter state (and if not the flow decision state skips the doubleEnter and compare 
					// states and goes to the editStatus state)
					// note that unless the instrument is configured such that it does not support verify, that
					// the user has the option of manually entering the doubleEnter state via an action button
					
					context.getFlowScope().put("mandatoryDoubleEnter", Boolean.TRUE);
					LavaComponentFormAction.createCommandError(errors, "info.instrument.mandatoryDoubleEnter", null);
				}
				else {
					context.getFlowScope().put("mandatoryDoubleEnter", Boolean.FALSE);
				}
				
				// note: upon transitioning to the double entry page, the componentView will have been set to
				// "doubleEnter" in prepareToRender, which is how the view knows to use the COMPARE_INSTRUMENT component
			}
		}
		else {
			context.getFlowScope().put("mandatoryDoubleEnter", Boolean.FALSE);
		}

		// since dcStatus is initialized to STATUS_SCHEDULED for new instruments, consider it the
		// same as null in terms of whether dcStatus can be overwritten		
		if (instrument.getDcStatus() == null || instrument.getDcStatus().equals(STATUS_SCHEDULED)) {
			if (instrument.hasMissingOrIncompleteFields()) {
				instrument.setDcStatus(STATUS_PARTIALLY_COMPLETE);
			}
		}
		// infer status values, specific to the "enter" flow 
		imputeEnterStatusValues(context, instrument);
		
		// save takes place at every state in the flow
		return this.doSave(context,command,errors);
	}


	// when data entry is saved (via Save or Verify (Double Enter), impute any status values that
	// can be imputed
	private void imputeEnterStatusValues(RequestContext context, Instrument instrument) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		// infer status values, specific to the "enter" flow 
		
		// ** never ** overwrite status properties that already have a value
		
		String userName = CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request).getShortUserNameRev();
		// default "collection by" to the staff member for the visit
		if (instrument.getDcBy()==null) instrument.setDcBy(instrument.getVisit().getVisitWith());
		// default collection date to the visit date
		if (instrument.getDcDate()==null) instrument.setDcDate(instrument.getVisit().getVisitDate());
		// since dcStatus is initialized to STATUS_SCHEDULED for new instruments, consider it the
		// same as null in terms of whether dcStatus can be overwritten
		if (instrument.getDcStatus() == null || instrument.getDcStatus().equals(STATUS_SCHEDULED)) {
			instrument.setDcStatus(STATUS_COMPLETE);
		}
		// the data entry fields are whoever is logged in and now			
		if (instrument.getDeBy()==null) instrument.setDeBy(userName);
		if (instrument.getDeDate()==null) instrument.setDeDate(new Date());
		// data entry status is considered complete at this point, because all result fields
		// are required, and even if STATUS_INCOMPLETE entered for an individual field, the overall
		// data entry status for the instrument is STATUS_COMPLETE
		if (instrument.getDeStatus()==null) instrument.setDeStatus(STATUS_COMPLETE);
	}
	
	
	/**
	 * "enter" flow, "enter" state. user has chosen to verify. save the data entry transition to the 
	 * "doubleEnter" state (this event is only possible if the instrument is configured to support verify (the default)) 
	 */
	public Event handleEnterVerifyEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doEnterVerifyEvent(context,command,errors);
	}

	protected Event doEnterVerifyEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		// note: upon transitioning to the double entry page, the componentView will have been set to
		// "doubleEnter" in prepareToRender, which is how the view knows to use the COMPARE_INSTRUMENT component

		// infer status values, specific to the "enter" flow 
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		imputeEnterStatusValues(context, instrument);
		
		// save data entry
		return this.doSave(context,command,errors);
	}
	
	/**
	 *  "enter", "enterReview" flows, "doubleEnter" state, transitioning to "editStatus" state.
	 */
	public Event handleDoubleEnterDeferEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doDoubleEnterDeferEvent(context,command,errors);
	}

	protected Event doDoubleEnterDeferEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		// set verification status and proceed to status
		//TODO: add verification to task list of user or some kind of reminder
		instrument.setDvStatus(STATUS_VERIFY_DEFER);
		return this.doSave(context,command,errors);
	}	

	
	// helper method
	protected void compareDoubleEntry(Instrument instr1, Instrument instr2, BindingResult errors) throws Exception {
		// iterate thru the required result fields of the command object, and for each field, compare the
		// value against the corresponding field in the double entered command object. if the values
		// are different, create a fieldError
		boolean compareError = false;
		for (String propName : instr1.getRequiredResultFields()) {
			// BeanUtils getProperty returns property value as a String
			String propValuePrimary = BeanUtils.getProperty(instr1, propName);
			String propValueSecondary = BeanUtils.getProperty(instr2, propName);
			
			if (!propValuePrimary.equals(propValueSecondary)) {
				compareError = true;
				
				// special-case for execpilotbattery random numbers and random letters compare, in lieu of
				// creating a handler subclass which would also require a FormAction subclass and a custom
				// flow
				if (instr1.getInstrTypeEncoded().equals("execpilotbattery") && 
						(propName.equals("rndNumRaw") || propName.equals("rndLetRaw"))) {
					// determine the character position at which the different occurs
					Integer position = 0;
					while (position < propValuePrimary.length() && position < propValueSecondary.length() &&
							propValuePrimary.charAt(position) == propValueSecondary.charAt(position)) {
						position++;
					}
					position++; // convert to 1-based
					String[] codes = errors.resolveMessageCodes(DOUBLE_ENTER_MISMATCH_AT_POS_ERROR_CODE, propName);
					String[] arguments = new String[] {position.toString()};
					errors.addError(new FieldError(
							errors.getObjectName(), "components[compareInstrument]." + propName, propValueSecondary, false,
							codes, arguments, ""));
				}
				else { // standard error reporting
					
					// create field error keys with code DOUBLE_ENTER_MISMATCH_ERROR_CODE ("doubleEnter")
					//  (e.g. doubleEnter.cdr.think, doubleEnter.think, doubleEnter.java.lang.String, doubleEnter)
					// note: use simple propName here to match error keys
					String[] codes = errors.resolveMessageCodes(DOUBLE_ENTER_MISMATCH_ERROR_CODE, propName);
					// get arguments for error that could optionally be used in the field error message (e.g. cdr.think, think) 
					String[] arguments = new String[] {errors.getObjectName() + Errors.NESTED_PATH_SEPARATOR + propName, propName};
					// use the full property name with respect to the command object so the error will bind to the property,
					// and bind to the compareInstrument so the field error shows after that field
					errors.addError(new FieldError(
							errors.getObjectName(), "components[compareInstrument]." + propName, propValueSecondary, false,
							codes, arguments, ""));
				}
			}
		}
		
		// create an object-level error if any field-level errors exist
		if (compareError) {
			errors.addError(new ObjectError(errors.getObjectName(), 
					new String[]{COMMAND_DOUBLE_ENTER_MISMATCH_ERROR_CODE}, new Object[] {errors.getObjectName()}, ""));
		}
		else {
			// create object-level message that compare succeeded (using the errors structure)
			errors.addError(new ObjectError(errors.getObjectName(), 
					new String[]{COMMAND_DOUBLE_ENTER_MATCH_INFO_CODE}, new Object[] {errors.getObjectName()}, ""));
		}
	}
	
	/**
	 * "enter", "enterReview" flows, "doubleEnter" state, transitioning to "compare" state 
	 * 
	 * This event executes after the user has double entered the data while in the "doubleEnter" flow state. It does a compare 
	 * prior to entering the "compare" flow state, so that the "compare" flow state view can display any double enter
	 * errors. Note that if even if there are no errors on the first double enter, still go to the "compare" flow state,
	 * and the handleDoubleEnterCompare event handler still does a compare, redundantly, before proceeding to the "editStatus" 
	 * flow state. 
	 */
	
	public Event handleDoubleEnterSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doDoubleEnterSaveEvent(context,command,errors);
	}

	protected Event doDoubleEnterSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		// compare the result fields of the first data entry (in the INSTRUMENT component of ComponentCommand) and
		// the second data entry (in the COMPARE_INSTRUMENT component) to verify the data. this is done here, prior 
		// to showing the compare form, so that differences will be flagged when the user sees the form
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		Instrument compareInstrument = (Instrument) ((ComponentCommand)command).getComponents().get(COMPARE_INSTRUMENT);
		compareDoubleEntry(instrument, compareInstrument, errors);

		// note: upon transitioning to the compare, the componentView will have been set to
		//   "compare" in prepareToRender, which is how the view (createField) knows to present
		//   two sets of properties for the comparison; the first of each set binds to the INSTRUMENT 
		//   component and the second binds to the COMPARE_INSTRUMENT component of ComponentCommand
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID); 
	}
	
	/**
	 * "enter" and "enterReview" flows, "compare" state, transitioning to "editStatus" state
	 * 
	 * This event executes after the user has been presented with the "compare" flow state view, which displays
	 * both the initial and double entry data, and the user chooses to Verify. It does a compare and proceeds
	 * to the "editStatus" state if there are no errors, or remains in the "compare" state if there are double
	 * enter errors.
	 */
	public Event handleDoubleEnterCompareEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doDoubleEnterCompareEvent(context,command,errors);
	}
	
	protected Event doDoubleEnterCompareEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		// compare the result fields of the INSTRUMENT and COMPARE_INSTRUMENT components from the compare
		// page, where the user sees two sets of result field properties and can modify the property value
		// from the first data entry, the second data entry, or both, until they match
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		Instrument compareInstrument = (Instrument) ((ComponentCommand)command).getComponents().get(COMPARE_INSTRUMENT);
		compareDoubleEntry(instrument, compareInstrument, errors);
		
		// do not transition to status page if there are compare errors. remain on the compare page. 
		if (errors.hasFieldErrors()){
			return new Event(this,ERROR_FLOW_EVENT_ID); 
		}
		else {
			String userName = CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request).getShortUserNameRev();
			if (instrument.getDvBy() == null) instrument.setDvBy(userName);
			if (instrument.getDvDate() == null) instrument.setDvDate(new Date());
			if (instrument.getDvStatus() == null || instrument.getDvStatus().equals(STATUS_VERIFY_DEFER)) {
				instrument.setDvStatus(STATUS_VERIFIED_DOUBLE_ENTRY);
			}
			
			return this.doSave(context,command,errors);
		}
	}

	
// "enterReview" FLOW EVENT HANDLERS (this flow also uses the handleDoubleEnter* methods above)
	/**
	 * "enterReview" flow, "enterReview" state. Save the data entry and go to the review state. 
	 */
	public Event handleEnterReviewSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doEnterReviewSaveEvent(context,command,errors);
	}

	protected Event doEnterReviewSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		// infer status values, specific to the "enterReview" flow 
		
		// ** never ** overwrite status properties that already have a value
		
		String userName = CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request).getShortUserNameRev();
		// default "collection by" to the staff member for the visit
		if (instrument.getDcBy()==null) instrument.setDcBy(instrument.getVisit().getVisitWith());
		// default collection date to the visit date
		if (instrument.getDcDate()==null) instrument.setDcDate(instrument.getVisit().getVisitDate());
		// since dcStatus is initialized to STATUS_SCHEDULED for new instruments, consider it the
		// same as null in terms of whether dcStatus can be overwritten
		if (instrument.getDcStatus() == null || instrument.getDcStatus().equals(STATUS_SCHEDULED)) {
			instrument.setDcStatus(STATUS_COMPLETE);
		}

		// the data entry fields are whoever is logged in and now			
		if (instrument.getDeBy()==null) instrument.setDeBy(userName);
		if (instrument.getDeDate()==null) instrument.setDeDate(new Date());
		// data entry status is considered complete at this point, because all result fields
		// are required, and even if STATUS_INCOMPLETE entered for an individual field, the overall
		// data entry status for the instrument is STATUS_COMPLETE
		if (instrument.getDeStatus()==null) instrument.setDeStatus(STATUS_COMPLETE);
		
		
		// save takes place at every state in the flow
		return this.doSave(context,command, errors);
	}
	

	/**
	 * "enterReview" flow, "review" state, transitioning to either "doubleEnter" state if configured to do so,
	 * or the "editStatus" state. 
	 */
	public Event handleEnterReviewReviewSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doEnterReviewReviewSaveEvent(context,command,errors);
	}

	protected Event doEnterReviewReviewSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		InstrumentConfig instrumentConfig = instrumentManager.getInstrumentConfig().get(instrument.getInstrTypeEncoded());
		
		// instrument that use the "enterReview" flow can still be subject to double entry, if applicable (if it is applicable,
		// the instrument configuration should set verify to "true"). note that in this case, the double enter is done
		// after the review
		// if double entry is applicable, check whether verify (double enter) should be done or skipped 
		
		// examine the instrument config to see if the verify flag is set for this instrument. if not, 
		// skip double enter.  
		if (instrumentConfig.getVerify()) {
			// if the instrument has already been successfully double entered then double enter should not be made
			// mandatory again (but the user will still have the option of doing double enter via the Double Enter button)
			if (instrument.getDvStatus() != null && instrument.getDvStatus().equals(STATUS_VERIFIED_DOUBLE_ENTRY)) {
				context.getFlowScope().put("mandatoryDoubleEnter", Boolean.FALSE);
			}
			else {
				// examine the project/instrument configuration data to determine  whether verify should 
				// be mandatory as the next step in the flow, based on the verify rate 
				Short rate = this.getProjectInstrumentVerifyRate(instrument.getProjName(), instrument.getInstrTypeEncoded());
	
				if (rate > 0) {
					
					// TODO: based upon the rate, determine whether double enter should be done this time. 
					// for now, rate is either 0 or 100, i.e. either double enter is never mandatory or double
					// enter is always mandatory
					
					// set a flag in flow scope so that the flow decision state knows to proceed to the 
					// doubleEnter state (and if not the flow decision state skips the doubleEnter and compare 
					// states and goes to the editStatus state
					// note that unless the instrument is configured such that it does not support verify, that
					// the user has the option of manually entering the doubleEnter state via an action button
					
					context.getFlowScope().put("mandatoryDoubleEnter", Boolean.TRUE);
					if (rate.equals((short)100)) {
						LavaComponentFormAction.createCommandError(errors, "info.instrument.mandatoryDoubleEnter", null);
					}
					else {
						LavaComponentFormAction.createCommandError(errors, "info.instrument.randomDoubleEnter", null);
					}
				}
				else {
					context.getFlowScope().put("mandatoryDoubleEnter", Boolean.FALSE);
				}
				
				// note: upon transitioning to the double entry page, the componentView will have been set to
				// "doubleEnter" in prepareToRender, which is how the view knows to use the COMPARE_INSTRUMENT component
			}
		}
		else {
			// double entry is not applicable, i.e. the instrument verification is a visual review only
			
			context.getFlowScope().put("mandatoryDoubleEnter", Boolean.FALSE);
			
			// ** never ** overwrite status properties that already have a value
			String userName = getCurrentUser(request).getShortUserNameRev();
			if (instrument.getDvBy() == null) instrument.setDvBy(userName); 
			if (instrument.getDvDate() == null) instrument.setDvDate(new Date());
			if (instrument.getDvStatus() == null) instrument.setDvStatus(STATUS_VERIFIED_REVIEW);
		}

		// no need to save, since nothing edited in review state
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	
// "upload" FLOW EVENT HANDLERS	
	/**
	 * "upload" flow, "upload" state transitioning to "review" state 
	 */
	public Event handleUploadEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doUploadEvent(context,command,errors); 
	}

	protected Event doUploadEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager,request);
		String instrTypeEncoded = ActionUtils.getTarget(currentAction.getId());
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);

		// obtain the bean to perform instrument specific file loading
		FileLoader fileLoader = fileLoaders.get(instrTypeEncoded + "FileLoader");
		
		// load the data from selected file(s) into the command object
		Event returnEvent = fileLoader.loadData(context, command, errors);
		
		if (returnEvent.getId().equals(SUCCESS_FLOW_EVENT_ID)) {

			// infer status values, specific to the "upload" flow

			// ** never ** overwrite status properties that already have a value
			
			String userName = CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request).getShortUserNameRev();
			// default collection by to the staff member for the visit
			if (instrument.getDcBy()==null) instrument.setDcBy(instrument.getVisit().getVisitWith());
			// default collection date to the the visit date
			if (instrument.getDcDate()==null) instrument.setDcDate(instrument.getVisit().getVisitDate());
			// since dcStatus is initialized to STATUS_SCHEDULED for new instruments, consider it the
			// same as null in terms of whether dcStatus can be overwritten
			if (instrument.getDcStatus() == null || instrument.getDcStatus().equals(STATUS_SCHEDULED)) {
				instrument.setDcStatus(STATUS_COMPLETE);
			}
			// the data entry fields are for the upload, so whoever is logged in and now			
			if (instrument.getDeBy()==null) instrument.setDeBy(userName);
			if (instrument.getDeDate()==null) instrument.setDeDate(new Date());
			if (instrument.getDeStatus()==null) instrument.setDeStatus(STATUS_COMPLETE);

			returnEvent = this.doSave(context,command,errors);
		}
		return returnEvent;
	}	

	
// "collect" FLOW EVENT HANDLERS	
	/**
	 * "collect" flow, "collect" state transitioning to "review" state. save collected results and infer any status 
	 *  values that can be inferred, prior to transitioning to the "review" state.
	 */
	public Event handleCollectSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doCollectSaveEvent(context,command,errors);
	}

	protected Event doCollectSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		
		// infer status values 
		// note: this is done in the "collect" state of the "collect" flow rather than in the "review" state
		//       (which may make more sense since it immediately precedes the "editStatus" state where status
		//       values are displayed/edited), because this event handler is specific to the "collect"
		//       flow, while the "review" state's doSaveReview event handler is used for other flows 
		//       (e.g. "upload"). each flow should infer their statuses uniquely since they may differ.
		
		// ** never ** overwrite status properties that already have a value
		
		String userName = getCurrentUser(request).getShortUserNameRev();
		// since data collection mode involves collecting AND inputting
		//  the data, both dc and de fields can be inferred			
		if (instrument.getDcBy()==null) instrument.setDcBy(userName);
		if (instrument.getDcDate()==null) instrument.setDcDate(new Date());
		if (instrument.getDeBy()==null) instrument.setDeBy(userName);
		if (instrument.getDeDate()==null) instrument.setDeDate(new Date());
		
		// determine the data collection status by iterating thru all required result fields and making
		//  sure there are no STATUS_INCOMPLETE fields (all fields will have some value because they
		//  are required fields on the collect page)
		boolean complete = true;
		for (String propName : instrument.getRequiredResultFields()) {
			// BeanUtils getProperty returns property value as a String
			String propValue = BeanUtils.getProperty(instrument, propName);
			if (propValue.startsWith("INCOMPLETE_MISSING_DATA_CODE")) { 
				complete = false;
			}
		}
		// since dcStatus is initialized to STATUS_SCHEDULED for new instruments, consider it the
		// same as null in terms of whether dcStatus can be overwritten
		if (complete) {
			if (instrument.getDcStatus()==null || instrument.getDcStatus().equals(STATUS_SCHEDULED)) instrument.setDcStatus(STATUS_COMPLETE);
			if (instrument.getDeStatus()==null) instrument.setDeStatus("Complete (Collect)");
		}
		else {
			if (instrument.getDcStatus()==null || instrument.getDcStatus().equals(STATUS_SCHEDULED)) instrument.setDcStatus(STATUS_INCOMPLETE);
			if (instrument.getDeStatus()==null) instrument.setDeStatus("Incomplete (Collect)");
		}

		//NOTE: "collect" flow is no longer used, but code left in here. under the new design, only
		// double enter sets the verify fields, dv*, so if "collect" brought back, these would no longer
		// be set, or, may reconsider and have collect flow review set the dv* fields
		// pre-populate the verify fields that can be inferred (since they are editable on the 
		// review form) 
		// note: the dvStatus field is inferred when the review is saved, not here, since 
		//       it depends on what action the user performs on the review page
		if (instrument.getDvBy() == null) instrument.setDvBy(userName); 
		if (instrument.getDvDate() == null) instrument.setDvDate(new Date());
		if (instrument.getDvStatus() == null) instrument.setDvStatus(STATUS_VERIFIED_REVIEW);
		
		return this.doSave(context,command,errors);
	}

	/**
	 * "collect" flow, "review" state, transitioning to "editStatus" state. currently do not need this handler as it does
	 * nothing, but have it as a placeholder for future use  
	 */
	public Event handleCollectReviewSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doCollectReviewSaveEvent(context,command,errors);
	}

	protected Event doCollectReviewSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		// nothing to save here, since review page is not editable, and since dvStatus was set and saved in handleCollectSaveEvent
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
	/**
	 *  "collect" flow, "review" state, transitioning to "editStatus" state 
	 * 
	 */
	public Event handleCollectReviewDeferEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doCollectReviewDeferEvent(context,command,errors);
	}

	protected Event doCollectReviewDeferEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		// set verification status and proceed to status
		//TODO: add verification to task list of user or some kind of reminder
		instrument.setDvStatus(STATUS_VERIFY_DEFER);
		return this.doSave(context,command,errors);
	}	


// "editStatus" FLOW EVENT HANDLERS
	/**
	 * "editStatus" flow, "editStatus" state, transtioning to the end state, thereby terminating the "editStatus" subflow
	 * and returning to the parent flow ("enter", "enterReview", "upload", "collect", "view")
	 */
	public Event handleStatusSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doStatusSaveEvent(context,command,errors);
	}
	
	protected Event doStatusSaveEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return this.doSave(context,command,errors);
	}	
	

// "changeVersion" FLOW EVENT HANDLERS
	public Event handleConfirmChangeVersionEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doConfirmChangeVersion(context,command,errors);
	}
	
	protected Event doConfirmChangeVersion(RequestContext context, Object command, BindingResult errors) throws Exception{
		
		Map components = ((ComponentCommand)command).getComponents();
		Instrument instrument = (Instrument) ((ComponentCommand)command).getComponents().get(INSTRUMENT);
		if (!instrument.isVersioned()){
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		instrument.changeVersion(instrument.getInstrVer());
		//was refreshing object here, but that breaks when different versions have different model objects;
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	public Event handleCancelChangeVersionEvent(RequestContext context, Object command, BindingResult errors){
		return doCancelChangeVersion(context,command,errors);
	}
	
	//Override this in subclass to provide custom cancel delete  handler 
	protected Event doCancelChangeVersion(RequestContext context, Object command, BindingResult errors){
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
// detail record EVENT HANDLING METHODS
	// these support page lists of instrument detail records, where the listing of detail records only requires
	// basic navigation, i.e. no filtering and sorting such that a separate list handler would be needed for the
	// detail component
	// note: the detail records are added as the "instrumentDetails" component of the ComponentCommand structure
	// in the instrument specific FileLoaded bean's loadFile method
	
	public Event handlePrevPageEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		return doPrevPage(context,command,errors,component);
	}
						
	//The default prev page action
	protected Event doPrevPage(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);

		// the plh does not have a sourceProvider; instead, the source has been set to a List. so there
		// is no database interaction to retrieved items in the list, and thus no need to initPageList here
		// or catch exceptions
		plh.previousPage();
		plh.setPageHolder(plh.getPage()); // keep the holder in sync				
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	public Event handleNextPageEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		return doNextPage(context,command,errors,component);
	}
							
	//The default next page action
	protected Event doNextPage(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);
		plh.nextPage();
		plh.setPageHolder(plh.getPage()); // keep the holder in sync		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	public Event handleRecordNavEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		return doRecordNav(context,command,errors,component);
	}
							
	//The default record navigationnext page action
	protected Event doRecordNav(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);

        // the pageHolder must be assigned to the page now. this is so that the page value is                                        
        // not changed until after binding, because if binding editable list items that are in                                       
        // pageList, binding could fail if the size of pageList changes                
		plh.setPage(plh.getPageHolder());
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	public Event handlePageSizeEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		return doPageSize(context,command,errors,component);
	}
							
	//The default page size action
	protected Event doPageSize(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);

        // the pageSizeHolder must be assigned to the pageSize now. this is so that the pageSize                                     
        // value is not changed until after binding, because if binding editable list items that                                     
		// are in pageList, binding could fail if the size of pageList changes                     
		plh.setPageSize(plh.getPageSizeHolder());
		// this is a bit tricky. getPage() actually sets the correct page number based on the pageSize,
		// so it must be called so set the page. then set pageHolder so the view will display the correct page
		plh.setPageHolder(plh.getPage());
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
	
	public Short getProjectInstrumentVerifyRate(String projName, String instrTypeEncoded) {
		/* 
		 * gets the verify rate for a given project and instrument
		 * Use the following order (most specific to least specific):
		 * 	projName~instrTypeEncoded
		 * 	projName(without unit)~instrTypeEncoded
		 * 	ANY~instrTypeEncoded
		 * 	projName-ANY
		 * 	projName(without Unit)-ANY
		 *  ANY~ANY
		 */
		Short rate = (short) 0;
		Boolean found = false;
		String lookupKey = null;
		String projectNoUnit = null;
		
		if(projectInstrumentVerifyRates==null || projectInstrumentVerifyRates.size()==0 ||
			(projName == null && instrTypeEncoded == null)){
			return (short) 0;
		}
		//set projName and instrTypeEncoded to default keys if not provided
		if(projName==null){ projName=ANY_PROJECT_KEY;}
		if(instrTypeEncoded==null){ instrTypeEncoded=ANY_INSTRUMENT_KEY;}
		
		//try projName~instrTypeEncoded
		lookupKey = new StringBuffer(projName).append("~").append(instrTypeEncoded).toString();
		if(projectInstrumentVerifyRates.containsKey(lookupKey)){
			rate = projectInstrumentVerifyRates.get(lookupKey);
			found = true;
		}
			
		//try projName(without Unit)~instrTypeEncoded
		if(!found && projName.contains("[")){
			projectNoUnit = projName.substring(0, projName.indexOf("[")).trim();
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(instrTypeEncoded).toString();
			if(projectInstrumentVerifyRates.containsKey(lookupKey)){
				rate = projectInstrumentVerifyRates.get(lookupKey);
				found = true;
			}
		}
		//try ANY~instrTypeEncoded
		lookupKey = new StringBuffer(ANY_PROJECT_KEY).append("~").append(instrTypeEncoded).toString();
		if(!found && projectInstrumentVerifyRates.containsKey(lookupKey)){
			rate = projectInstrumentVerifyRates.get(lookupKey);
			found = true;
		}
		//try projName~ANY
		lookupKey = new StringBuffer(projName).append("~").append(ANY_INSTRUMENT_KEY).toString();
		if(!found && projectInstrumentVerifyRates.containsKey(lookupKey)){
			rate = projectInstrumentVerifyRates.get(lookupKey);
			found = true;
		}
		//try (projname without unit)~ANY
		if(!found && projName.contains("[")){
			lookupKey = new StringBuffer(projectNoUnit).append("~").append(ANY_INSTRUMENT_KEY).toString();
			if(projectInstrumentVerifyRates.containsKey(lookupKey)){
				rate = projectInstrumentVerifyRates.get(lookupKey);
				found = true;
			}
		}
		//try ANY~ANY
		lookupKey = new StringBuffer(ANY_PROJECT_KEY).append("~").append(ANY_INSTRUMENT_KEY).toString();
		if(!found && projectInstrumentVerifyRates.containsKey(lookupKey)){
			rate = projectInstrumentVerifyRates.get(lookupKey);
			found = true;
		}
		
		return rate;
	}
	
	public void setProjectInstrumentVerifyRates(Map<String, Short> projectInstrumentVerifyRates) {
		this.projectInstrumentVerifyRates = projectInstrumentVerifyRates;
	}

}