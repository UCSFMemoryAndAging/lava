package edu.ucsf.lava.core.reporting.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.CoreAuthorizationContext;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.calendar.controller.CalendarHandlerUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.reporting.model.ReportSetup;

/*
 * This class serves as the base handler class for handler classes that generate reports.
 * 
 * Behavior is different from entity/list handlers. Those handlers follow a pattern of retrieving
 * the data on the first (GET) request and displaying it, and then saving/printing the data on 
 * the second (POST) request. 
 * Report handlers just initialize the filter properties on the first (GET) request and display
 * them for user input (report setup). On the second (POST) request, the report data is both retrieved
 * and displayed (retrieval as well as display is done by the report engine).
 * 
 * Report queries acquire their data via SQL queries embedded in the report design files. A JDBC data
 * source is passed to the report generation framework to execute these queries. Even though the query
 * is done outside of the DAO, a LavaDaoFilter object is still used to handle the criteria report
 * parameters specified by the user. This allows leveraging the tags in the view which work with
 * LavaDaoFilter and leveraging the CalendarHandlerUtils which work with a LavaDaoFilter to support 
 * quick date selection.
 */

public class ReportComponentHandler extends LavaComponentHandler {
	protected Boolean dateCriteria;
	// subclasses can override the defaultDisplayRange in their configuration
	protected String defaultDisplayRange=CalendarDaoUtils.DISPLAY_RANGE_MONTH;
	protected String selectedProjListPropName = "projectList";
	// the dateProperyName dictates naming for the java.util.Date filter date range fields, startDateParam
	// and endDateParam
	// to clarify, in the filter, the parameters used by the view for input are named CalendarHandlerUtils
	// CUSTOM_DATE_FILTER_START_PARAM and CUSTOM_DATE_FILTER_END_PARAM and have String values, while
	// the parameters in the filter that are used for the report query are named datePropertyName + "Start" 
	// and datePropertyName + "End" and are java.util.Date values. after user input, the former are converted to 
	// the latter.
	// in the report, the parameter names for date start and end should match the java.util.Date date range fields,
	// i.e. startDateParam and endDateParam 
	// query date range parameters
	protected String datePropName; 
	protected String startDateParam;
	protected String endDateParam;
	
	public ReportComponentHandler() {
		super();
		// the refresh event does nothing in this case, but must be handled because project context changes
		// execute a refresh (and if "refresh" is not a handled event the event will not return
		// success and project context changes will be stuck in a decision state forever, until
		// stack overflow)
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"generate", "close", "refresh"}));
		defaultEvents.addAll(CalendarHandlerUtils.getDefaultEvents());
		authEvents = new ArrayList(); // none of the report events require explicit permission

		// date range and project name are criteria that are often used for reports. the handler configuration
		// defines a base handler with each combination of date range and project being used or not. default
		// each to false so the configuration file only need specify those to use and set them true.
		this.dateCriteria = false;
		
		this.setDatePropName("date");

		Map<String,Class> objectMap = new HashMap<String,Class>();
		objectMap.put("reportSetup", ReportSetup.class);
		this.setHandledObjects(objectMap);
	}

	public Event authorizationCheck(RequestContext context) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action action = CoreSessionUtils.getCurrentAction(sessionManager, request);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		// the authorization event for reports is "view"

		
		if (!authManager.isAuthorized(user,action,new CoreAuthorizationContext())) {
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{COMMAND_AUTHORIZATION_ERROR_CODE}, null);
			return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
		}
		
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	public Event initMostRecentViewState(RequestContext context) throws Exception {
		// probably not useful since report flow will not have subflows
		context.getFlowScope().put("mostRecentViewState", "reportSetup");
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}

	
	// subclasses can override/enhance this to add additional criteria filter fields 
	protected LavaDaoFilter setupReportFilter(RequestContext context, LavaDaoFilter filter) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();

		
		if (this.dateCriteria) {
			// add DateRange filter param handler
			filter.addParamHandler(new LavaDateRangeParamHandler("customDate"));
			
			// for reports since the customDateStart and customDateEnd will always reflect the quick date
			// selection (unlike for lists, where they are mutually exclusive), want to initialize them here so 
			// they default to the defaultDisplayRange
			
			// initializes the query date range to defaults based on the defaultDisplayRange 
			CalendarHandlerUtils.setDateFilterParams(context, filter, this.defaultDisplayRange, this.startDateParam, this.endDateParam);
			// transfer the default date range to the custom date params used as filter fields in the view. this
			// must be done separately from the above call because the Calendar handler does not want this to happen
			// (see updateCustomParamsFromDateParams comments)
			CalendarDaoUtils.updateCustomParamsFromDateParams(filter, this.startDateParam, this.endDateParam, Boolean.FALSE);	
			
		}
		
	
		
		return filter;
	}
	
	

	/**
	 * Create the filter used for the report query.   
	 */
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map backingObjects = new HashMap<String,Object>();
		ReportSetup reportSetup = new ReportSetup();
		// initialize report format
		// for now, the list is restricted to PDF
		reportSetup.setFormat("pdf"); // Jasper Reports supported formats: "pdf", "xls", "csv", "html"
		// filters have nothing to do with queries in report files which execute them, do not need to 
		// pass the user id to the filter, which will result in the authorization filters not being applied
		// instead, the equivalent of the authorization filter is added to the SQL in the report file
		LavaDaoFilter filter = EntityBase.newFilterInstance();
		filter = setupReportFilter(context, filter);
		// set the filter on the reportSetup object since that is stored in the command object in form scope
		reportSetup.setFilter(filter);
		backingObjects.put(this.getDefaultObjectName(), reportSetup);
		return backingObjects;
	}


	// override this subclass to set required fields if additional criteria parameters 
	// are defined which are required. the format of the required fields is a String which is
	// assumed to be a param in the reportSetup LavaDaoFilter
	// this class should be invoked from the subclass if date range and/or project criteria are used
	protected String[] defineRequiredFields(RequestContext context, Object command) {
		ReportSetup reportSetup = (ReportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		String[] required = new String[0];

		if (this.dateCriteria) {
			if (! ((String) reportSetup.getFilter().getParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM)).equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_ALL)) {
				required = StringUtils.concatenateStringArrays(required, new String[]{CalendarDaoUtils.CUSTOM_DATE_FILTER_START_PARAM, CalendarDaoUtils.CUSTOM_DATE_FILTER_END_PARAM});
			}
		}
		
	
		
		setRequiredFields(required);
		return getRequiredFields();
	}

	
	// initBinder is called when the flow calls setupForm and when the flow calls bind
	public void initBinder(RequestContext context, Object command, DataBinder binder) {
		 // only set validation for required fields on the generate event 
		String event = ActionUtils.getEventName(context);
		if (event.equals("generate")) {
			String[] requiredFields = defineRequiredFields(context, command);
			if(requiredFields != null && requiredFields.length != 0){
				String[] existingRequiredFields = binder.getRequiredFields();
				// convert the required field names into their equivalent bind names within a 
				// ComponentCommand command object where the command object is a LavaDaoFilter, e.g.
				// projectList ==> components['reportSetup'].filter.params['projectList']
				String[] componentRequiredFields = new String[requiredFields.length];
				for (int i=0; i<requiredFields.length;i++) {
					componentRequiredFields[i] = 
						new StringBuffer("components['").append(getDefaultObjectName()).
						append("'].filter.params['").append(requiredFields[i]).append("']").toString();
				}
				if (existingRequiredFields!=null){
					List existing = Arrays.asList(existingRequiredFields);
					HashSet fields = new HashSet(existing);
					fields.addAll(Arrays.asList(componentRequiredFields));
					binder.setRequiredFields((String[])fields.toArray());
				}else{
					binder.setRequiredFields(componentRequiredFields);
				}
			}
		 }
	}
	

	public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		setComponentMode(request, getDefaultObjectName(), "dc");
		setComponentView(request, getDefaultObjectName(), "edit");
	 	StateDefinition state = context.getCurrentState();
	}


	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
	 	String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
	 	StateDefinition state = context.getCurrentState();
		ReportSetup reportSetup = (ReportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());

	 	model = super.addReferenceData(context, command, errors, model);

	 	if (state.getId().equals("reportSetup")) {
		    // put flags in the model so view decorator knows which filter fields to create
		    model.put("dateCriteria", this.dateCriteria);

		}
	 	
	 	if (state.getId().equals("reportGen")) {
	 		// add anything to model that is needed for filling the report, i.e. report parameters
	 		// note: this must be done here on the prepareToRender call for the ReportFlowBuilder
	 		// "reportGen" state" because anything added to the model before this, i.e. in a
	 		// "reportSetup" state "reportSetup__generate" event transition action handler method,
	 		// would get wiped out of the model (request scope) due to webflow POST-REDIRECT-GET

	 		// a SQL report query is embedded in the report design file and executed during
	 		// report fill outside the scope the service/dao layer
	 		
 			// the jdbcDataSource used for these queries is configured on a given report view in 
	 		// lava-reports.xml so need not be configured here
	 		
		    model.put("format", reportSetup.getFormat());
		    // pass the handler itself as a report parameter                                                                                                                                               
		    model.put("handler", this);
		    // pass the user id for patient/project authorization filtering. since querying outside the
		    // scope of the DAO, entities and authorization filters, authorization clauses are added
		    // to the SQL statement embedded in the query as necessary
		    AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
			if (user != null){
				model.put("userId", user.getId());

			}
		   
			if (this.dateCriteria) {
				// if user uses quick date selection, the query date range and view date range will
				// already be in sync, but if they input custom dates, need to update the query date range.
				// handleCustomDateFilter converts the view date range Strings to query date range Dates
				
				CalendarHandlerUtils.handleCustomDateFilter(reportSetup.getFilter(), this.startDateParam, this.endDateParam);
				// since defaultDisplayRange is initialized, the view date range should never be missing
				// unless the user has cleared one or both date ranges. if they happen to do that, set default
				// date ranges for the report query
				// update: this should never happen if startDate and endDate are required fields, but
				// leave this code in for now
				if (CalendarHandlerUtils.missingDateFilterParams(reportSetup.getFilter(), this.startDateParam, this.endDateParam)){
					CalendarHandlerUtils.setDefaultFilterParams(reportSetup.getFilter(), this.defaultDisplayRange, this.startDateParam, this.endDateParam);
				}
				
				if (! ((String) reportSetup.getFilter().getParam(CalendarDaoUtils.DISPLAY_RANGE_PARAM)).equalsIgnoreCase(CalendarDaoUtils.DISPLAY_RANGE_ALL)) {
				    // within the report design, parameters must be defined and named this.startDateParam, this.endDateParam 
				    model.put(this.startDateParam, reportSetup.getFilter().getParam(this.startDateParam));
				    model.put(this.endDateParam, reportSetup.getFilter().getParam(this.endDateParam));
				}
				else {
					// if the All date range selected, set date params to extreme dates (use SQL Server smalldaterange
					// bounds for now)
					try {
					    model.put(this.startDateParam, new SimpleDateFormat("yyyy-MM-dd").parse("1900-01-01"));
					    model.put(this.endDateParam,  new SimpleDateFormat("yyyy-MM-dd").parse("2079-06-06"));
					}
					catch (ParseException pe ){
						// swallow since hard-coding valid dates
					}
				}
			}
 			
		
 			
		}
 
		return model;
	}
		

	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		String event = ActionUtils.getEventName(context);
		// note: do not use "event = ActionUtils.getEvent(request)" because it is possible for a flow
		//  to set an overrideEvent which will be different from the event request parameter
		
		if(event.startsWith("generate")){
			return this.handleGenerateEvent(context,command,errors);
		}	
		else if(event.startsWith("close")){
			return this.handleCloseEvent(context,command,errors);
		}	
		else {
			return this.handleCustomEvent(context,command,errors);
		}
	}
	
	
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		//	handle filter events for quick date range selection
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ReportSetup reportSetup = (ReportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());		
		CalendarHandlerUtils.setDateFilterParams(context,(LavaDaoFilter)reportSetup.getFilter(), this.defaultDisplayRange, this.startDateParam, this.endDateParam);
		// reflect the new date param values in the view by updating the custom date params
		CalendarDaoUtils.updateCustomParamsFromDateParams(reportSetup.getFilter(), this.startDateParam, this.endDateParam, Boolean.FALSE);	
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	

	// the "generate" event is handled when the user submits parameters to generate the report. after
	// this handler is called, the view state for the report output is entered, prepareToRender/addReferenceData
	// are called above, and then the report engine is called to generate the report
	public Event handleGenerateEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doGenerate(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doGenerate(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	

	public Event handleCloseEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doClose(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doClose(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
	
	public String getSelectedProjListPropName() {
		return this.selectedProjListPropName;
	}
	
	public void setSelectedProjListPropName(String selectedProjListPropName) {
		this.selectedProjListPropName = selectedProjListPropName;
	}

	public String getDatePropName() {
		return this.datePropName;
	}

	public void setDatePropName(String datePropName) {
		this.datePropName = datePropName;
		this.startDateParam = datePropName.concat("Start");
		this.endDateParam = datePropName.concat("End");
	}

	public String getDefaultDisplayRange() {
		return this.defaultDisplayRange;
	}

	public void setDefaultDisplayRange(String defaultDisplayRange) {
		this.defaultDisplayRange = defaultDisplayRange;
	}

	public Boolean getDateCriteria() {
		return dateCriteria;
	}

	public void setDateCriteria(Boolean dateCriteria) {
		this.dateCriteria = dateCriteria;
	}

	
}
