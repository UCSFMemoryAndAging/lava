package edu.ucsf.lava.core.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.context.NoSuchMessageException;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.auth.AuthManager;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.file.LavaFile;
import edu.ucsf.lava.core.dao.file.LavaFileDao;
import edu.ucsf.lava.core.environment.EnvironmentManager;
import edu.ucsf.lava.core.list.ListManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.metadata.MetadataManager;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.LavaSessionHttpRequestWrapper;
import edu.ucsf.lava.core.session.SessionManager;



//common functionality and core internal methods required by all Lava Component Handlers

/**
 * @author jhesse
 *
 */
abstract public class LavaComponentHandler implements ComponentHandler, ManagersAware {
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	protected AuthManager authManager;  
	protected ListManager listManager;
	protected MetadataManager metadataManager;
	protected ActionManager actionManager; 
	protected SessionManager sessionManager; 
	protected EnvironmentManager environmentManager;
	
	protected String primaryComponentContext;
	protected List<String> handledEvents; //a list of events handled by the handler, set by default, may be specified
	protected List<String> defaultEvents = new ArrayList(); // a list of default events handled for each object supported...defined in subclasses
	protected List<String> requiredFieldEvents = new ArrayList(); // a list of events for which required field validation should be done
	protected List<String> authEvents = new ArrayList(); // a list of the events for which a user must have permission
	protected Map<String,Class>  handledObjects; //a name, class map of "command" objects handled by this handler, typically just the one object for entity handlers
	protected String defaultObjectName; //the handled object that is the primary object for default methods, set automatically if just one handled object
	protected Class defaultObjectBaseClass; //the default objects base class (this will be the default object class unless explicitly set.  This enables subclassed flows to load base model entities as necessary. 
	String[] requiredFields = new String[]{}; //property names that will be "validated" as required upon submission of the form
	protected boolean supportsAttachedFiles = false; 
	protected LavaFileDao fileDao;
	protected static final String COMPONENT_MODE_SUFFIX = "_mode"; 
	protected static final String COMPONENT_VIEW_SUFFIX = "_view"; 
	protected static final String COMPONENT_COMMAND_PREFIX = "command.components[";
	protected static final String COMPONENT_COMMAND_SUFFIX = "]";
	
    protected static final String COMMAND_AUTHORIZATION_ERROR_CODE = "authorization.command";
	
	protected String defaultMode = "vw";
	protected String defaultView = "view";
	
	
	public LavaComponentHandler() {
		super();
		
	}

	
	
	// this is subclassed for entity and list handlers
	public Event authorizationCheck(RequestContext context, Object command) throws Exception {
		//TODO:  This should probably return unauthorized if we are getting down here to the base class method...
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	// this is subclassed for entity and list handlers
	public Event initMostRecentViewState(RequestContext context) throws Exception {
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	
	// modify in base classes and default handlers to return global transition events
	// override in custom flow handlers to return:
	//   SUCCESS_FLOW_EVENT_ID if custom flow might handle it, pending postSetupFlowDirector return value
	//   UNHANDLED_FLOW_EVENT_ID if custom flow should not handle
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	// modify in base classes and default handlers to return global transition events
	// override in custom flow handlers to return:
	//  CONTINUE_FLOW_EVENT_ID if custom flow handler should handle 
	//	UNHANDLED_FLOW_EVENT_ID if custom flow handler should not handle
	public Event postSetupFlowDirector(RequestContext context, Object command) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}


    public void refreshBackingObjects(RequestContext context, Object command, BindingResult errors) throws Exception {
    }
    
//  Set the current application context based on the id and the type of context that the entity defines.
	public void setContextFromScope(RequestContext context){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Long id = getContextIdFromRequest(context);
		setContextFromScope(context, id);
	}

	public void setContextFromScope(RequestContext context, Long id){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		if(this.primaryComponentContext!=null){
			sessionManager.setSessionAttribute(request,primaryComponentContext,id);
		}
	}

	/*
	 * Override this function in subclasses if the id for setting the current context is not in the "id" request parameter,
	 * e.g. contactInfo sets the patient context, but "id" is the contactInfo id not the patient id, so its
	 * handler overrides this method to get the patient id from that ContactInfo object
	 */ 
	
	protected Long getContextIdFromRequest(RequestContext context){
    	// because the POST-REDIRECT-GET pattern is in effect automatically (because web flow is
    	// configured with alwaysRedirectOnPause true by default) render actions are executed after the
    	// second request, so request parameters are not available. instead start-actions in the flow
    	// definitions of flows that depend on request parameters transfer needed values to flow
    	// scope. so here, look in flow scope for the id, not in request parameters.
		return(Long.valueOf(context.getFlowScope().getString("id")));
	}
	
	/*
	 * Put component mode and view in the model.  Override in subclasses to add additional reference 
	 * data...call superclass before returning. 
	 */
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		
		//put session management messages in model
		if(request.getParameter(LavaSessionHttpRequestWrapper.LAVASESSION_MONITORING_MESSAGE_PARAMETER)!=null){
			model.put(LavaSessionHttpRequestWrapper.LAVASESSION_MONITORING_MESSAGE_PARAMETER, request.getParameter(LavaSessionHttpRequestWrapper.LAVASESSION_MONITORING_MESSAGE_PARAMETER));
		}
		
		// add list navigation list
	 	this.addListToModel(model, "navigation.listPageSize", listManager.getDefaultStaticList("navigation.listPageSize"));
	 	
	 	// add time interval lists
	 	this.addListToModel(model, "datetime.timeInterval30Minute", listManager.getDefaultStaticList("datetime.timeInterval30Minute"));
	 	
		
		//put a lists object in the model
		initializeListsInModel(model);
		
		//Add Mode to model
		String mode = getComponentMode(request,getDefaultObjectName());
		if (mode == null){
			mode = getDefaultMode();
			setComponentMode(request,getDefaultObjectName(),mode);
		}
		model.put(getDefaultObjectName().concat(COMPONENT_MODE_SUFFIX),mode);
		
		//add view to model
		String view = getComponentView(request,getDefaultObjectName());
		if (view == null){
			mode = getDefaultView();
			setComponentView(request,getDefaultObjectName(),view);
		}
		model.put(getDefaultObjectName().concat(COMPONENT_VIEW_SUFFIX),view);
		return  model;
	}

	/**
	 * getDynamicLists
	 * 
	 * Helper method which obtains an existing dynamicLists structure from the model, or creates
	 * a new one. Used by any handler that gets dynamic lists.
	 */
	protected Map<String,Map<String,String>> getDynamicLists(Map model) {
		Map<String,Map<String,String>> dynamicLists = (Map<String,Map<String,String>>) model.get("dynamicLists");
		if (dynamicLists == null) {
			dynamicLists = new HashMap<String,Map<String,String>>();
		}
		return dynamicLists;
	}
	
	// subclasses should override this to add/override the standard property editors registered
	// by the controller
	public void registerPropertyEditors(PropertyEditorRegistry registry) {
		return;
	}

	protected void setComponentMode(HttpServletRequest request, String component, String mode){
			request.setAttribute(component.concat(COMPONENT_MODE_SUFFIX),mode);
	}
	
	protected String getComponentMode(HttpServletRequest request, String component){
		return (String)request.getAttribute(component.concat(COMPONENT_MODE_SUFFIX));
		
	}
	
	protected void setComponentView(HttpServletRequest request, String component, String view){
		request.setAttribute(component.concat(COMPONENT_VIEW_SUFFIX),view);
	}

	protected String getComponentView(HttpServletRequest request, String component){
		return (String)request.getAttribute(component.concat(COMPONENT_VIEW_SUFFIX));
	}
	
	/*
	 * Called by FormAction for every event from the view.  Event is ObjectName__EventName
	 */
	public boolean handlesEvent(RequestContext context) {
		// note: do not use "String event = ActionUtils.getEventParameter(request)" because it is possible for a flow
		//  to set an overrideEvent which will be different from the event request parameter
		String eventId = ActionUtils.getEventId(context);
		
		for (String handledEvent: handledEvents){
			if (eventId.equals(handledEvent)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Called to determine if the event is one for which required field validation should be performed.
	 */
	public boolean validateRequiredFieldsEvent(RequestContext context) {
		// note: do not use "String event = ActionUtils.getEventParameter(request)" because it is possible for a flow
		//  to set an overrideEvent which will be different from the event request parameter
		String eventId = ActionUtils.getEventId(context);
		
		for (String requiredFieldEvent: this.requiredFieldEvents){
			if (eventId.equals(requiredFieldEvent)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Called by the controller, determines whether this handler handles the object.
	 */
	public boolean handlesObject(String objectName) {
		for (String handledObject: handledObjects.keySet()){
			if (StringUtils.equalsIgnoreCase(handledObject,objectName)){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Determines whether this handler handles a download request
	 */
	public boolean handlesDownload(RequestContext context){
		return supportsAttachedFiles();
	}
	/*
	 * Handles the basic function of transfering a file down to the browser by
	 * setting the headers in the response and copying the file content
	 */
	public Event handleDownload(RequestContext context, Object command, BindingResult errors) throws Exception {
		Map components = ((ComponentCommand)command).getComponents();
		LavaFile file = this.getDownloadFile(context,components,errors);
		if(file == null){
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		HttpServletResponse response = ((ServletExternalContext)context.getExternalContext()).getResponse();
		response.setHeader("Content-Disposition","inline;filename=\""+ file.getName()+"\"");
		response.setContentType(file.getContentType());
		response.setContentLength(file.getContentLength());
		
		
		FileCopyUtils.copy(file.getContent(),response.getOutputStream());
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
		
	}
	
	/*
	 * Gets the download file, override in subclass if you need to do something special
	 */
	public LavaFile getDownloadFile(RequestContext context, Map components, BindingResult errors) throws Exception {
		if(this.getFileDao()==null) {return null;}
		return this.getFileDao().getFile(getDownloadFileId(components.get(getDefaultObjectName())));
		}
		
	
		
	/*
	 * Override to get the download file id from the primary model object
	 * 
	 */	
	public String getDownloadFileId(Object command){
		return new String();
	}
	
	
	
	/*
	 * Determine whether the event is an authorization event, i.e. an event for which an 
	 * authorization check should be performed. 
	 */
	public boolean isAuthEvent(RequestContext context) {
		// in this case, event refers to the action or flow mode, where for actions, the
		// mode is stored within the Action, and for flow's the mode is the final part of the flow id 
		String event = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		
		for (String authEvent: authEvents){
			if (event.equals(authEvent)){
				return true;
			}
		}
		return false;
	}
	
	
	/*
	 * Override this function in subclasses to take action when another component handler was the 
	 * primary event handler for the event but this handler may need to take some action.
	 * 
	 * Good programming practice dictates that the handler treat all objects that are handled by
	 * other handlers as read-only.     
	 */
	public Event postEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	/*
	 * Override this function in subclasses to take action when another component handler will be the 
	 * primary event handler for the event but this handler may need to take some action before that action is handled
	 * 
	 * Good programming practice dictates that the handler treat all objects that are handled by
	 * other handlers as read-only.     
	 */
	public Event preEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}


	private Throwable findRootCause(Throwable e) {
		Throwable cause = e.getCause();
		if (cause == null) {
			return e;
		}
		else {
			return findRootCause(cause);
		}
	}

	// in general, all exceptions from calls into the service layer should be handled in the handlers, 
	// and this method should be called to create an object error so that the exception message is
	// presented to the user. the handler method should return the failure event so that the web
	// flow does not transition to another state and the user remains on the same page
	protected void addObjectErrorForException(BindingResult errors, Exception e) {
		// display the root cause to the user. typically there will be an exception chain, since Spring 
		// throws its own unchecked exceptions which wrap the original database checked exceptions
		errors.addError(new ObjectError(errors.getObjectName(), null, null, findRootCause(e).toString()));
		// this is **critical**
		// if there is a transaction in progress, the transaction status must be set to rollbackOnly
		// so that there is not an attempt to commit the transaction. committing the transaction results
		// in an UnexpectedRollbackException thrown in a servlet filter, after the response has been
		// generated, so the response is not preserved. note that the transaction itself is already
		// marked for rollback by the transaction manager AOP code in the service layer and that the
		// transaction will be rolled back in any case, whether we mark the transaction status as
		// rollbackOnly or not 
		try{
			org.springframework.transaction.TransactionStatus status = org.springframework.transaction.interceptor.TransactionInterceptor.currentTransactionStatus();
			if (status != null) {
				status.setRollbackOnly();
			}
		}catch(Exception transException){}
		
	}
	

	
	
	/**
	 * Creates a LavaDaoFilter for the id passed in.  
	 * 
	 */
	protected LavaDaoFilter getFilterWithId(HttpServletRequest request, Long id){
		//Assume that the get mechanism will use a criteria query.  If using a named query then do not use this convenience function
		LavaDaoFilter filter = 	EntityBase.newFilterInstance(CoreSessionUtils.getCurrentUser(sessionManager, request)).addIdDaoEqualityParam(id);
		        
		return filter;
	}

	public List getHandledEvents() {
		return handledEvents;
	}

	//Sets up the handled events for the default object name
	protected void setDefaultHandledEvents(String defaultObjectName){
		
		List<String> events = new ArrayList<String>();

		StringBuffer event;
		for(String defaultEvent : this.defaultEvents){
			event = new StringBuffer().append(defaultObjectName).append(ActionUtils.OBJECT_EVENT_SEPARATOR).append(defaultEvent);
			events.add(event.toString());
		}
		setHandledEvents(events);
		
	}
	public void setHandledEvents(List handledEvents) {
		this.handledEvents = handledEvents;
	}
	public Map getHandledObjects() {
		return handledObjects;
	}

	public void setHandledObjects(Map <String,Class> handledObjects) {
		this.handledObjects = handledObjects;
		//if just one object then we can assume it is the default
		if (handledObjects.size() == 1) {
			setDefaultObjectName(handledObjects.keySet().iterator().next());
		}
	}

	public String getDefaultObjectName() {
		return defaultObjectName;
	}
	public String getDefaultObjectErrorName() {
		return new String(new StringBuffer(this.COMPONENT_COMMAND_PREFIX).append(getDefaultObjectName()).append(this.COMPONENT_COMMAND_SUFFIX));
		
	}

	public Class getDefaultObjectClass() {
		if(handledObjects.containsKey(getDefaultObjectName())){
			return handledObjects.get(getDefaultObjectName());
		}
		return null;
	}

	public Class getDefaultObjectBaseClass() {
		if(defaultObjectBaseClass == null){
			return getDefaultObjectClass();
		} 
		return defaultObjectBaseClass;
	}
	
	public void setDefaultObjectBaseClass(Class defaultObjectBaseClass){
		this.defaultObjectBaseClass = defaultObjectBaseClass;
	}
	public void setDefaultObjectName(String defaultObjectName) {
		this.defaultObjectName = defaultObjectName;
		//this needed to come out do that we could reset the object name in subclasses and redefine the handled events. 
		//if (handledEvents == null){
			setDefaultHandledEvents(defaultObjectName);
			setDefaultRequiredFieldEvents(defaultObjectName);
		//}
	}

	public List getRequiredFieldEvents() {
		return requiredFieldEvents;
	}

	// sets up the events for the default object name for which required fields should be validated
	protected void setDefaultRequiredFieldEvents(String defaultObjectName){
		List<String> events = new ArrayList<String>();

		StringBuffer event;
		for(String requiredFieldEvent : this.requiredFieldEvents){
			event = new StringBuffer().append(defaultObjectName).append(ActionUtils.OBJECT_EVENT_SEPARATOR).append(requiredFieldEvent);
			events.add(event.toString());
		}
		setRequiredFieldEvents(events);
	}
	
	public void setRequiredFieldEvents(List requiredFieldEvents) {
		this.requiredFieldEvents = requiredFieldEvents;
	}
	
	
	
	//Override in subclass for custom required fields functionality
	public String[] getRequiredFields() {
		return requiredFields;
	}

	public void setRequiredFields(String[] requiredFields) {
		this.requiredFields = requiredFields;
	}

	

	public String getDefaultMode() {
		return defaultMode;
	}

	public void setDefaultMode(String defaultMode) {
		this.defaultMode = defaultMode;
	}

	public String getDefaultView() {
		return defaultView;
	}

	public void setDefaultView(String defaultView) {
		this.defaultView = defaultView;
	}


	// this method is used by reporting for properties that have an associated list, where the
	// stored value should be translated into display text, using the list 
	// propName = "entity.property" (i.e. qualified by entity) or whatever should be used for metadata lookup
	// propValue = property value from the database
	public String translatePropValueToText(String propName, Object propValue, Map<String,Map<String,String>> dynamicLists) {
		// propValue could be String, Short, Long, Date, etc.., as could the return value. for
		// reporting via Jasper Reports, the <textFieldExpression> class attribute should agree
		// with the return value type; if this method translates a non-String type to a String,
		// then the <textFieldExpression> class should be String.
		// note: the <textField> pattern attribute can be used to format Date and numeric types
		
		// if propValue is null, return empty string. this is so reports will print nothing and in
		// Jasper Reports textField element need not declare isBlankWhenNull="true" though this was
		// done historically. it is preferable to return the empty string than return null while
		// setting isBlankWhenNull="true" because there are some situations where additional linebreaks
		// are added to the result, e.g. to make all values in a list have the same number of lines
		// so that the enclosing <frame> stretch behavior is the same for each column value and 
		// therefore borders will be treated the same for each cell
		if (propValue == null) {
			return new String();
		}
		
		if (propValue instanceof Date) {
			String style = metadataManager.getMessage(propName + ".style",null,Locale.getDefault());
			if (style.equals("date")) {
				return new SimpleDateFormat("MM/dd/yyyy").format(propValue).toString();
			}
			else if (style.equals("datetime")) {
				return new SimpleDateFormat("MM/dd/yyyy h:mma").format(propValue).toString();
			}
		}
		
		if (propValue instanceof Boolean) {
			if ((Boolean) propValue) {
				return this.getMessage("text.yes",null);
			}
			else {
				return this.getMessage("text.no",null);
			}
		}
		
		
		String listRequestId = null;
		try {
			listRequestId = metadataManager.getMessage(propName + ".listRequestId",null,Locale.getDefault());
		}
		catch (NoSuchMessageException ex) {
			// no list specified in metadata just means no translation to be done
		}
		if (listRequestId == null) {
			return propValue.toString();
		}
		else {
	
			Map<String,String> list = listManager.getStaticListByRequestId(listRequestId);
			if (list == null) {
				// see if the list is a dynamic list, if the dynamicLists argument was supplied
				// (all static lists are available from the listService, but dynamic lists
				// are created as needed by handlers, so are passed into the report designs via the model, which
				// in turn pass them to this method if necessary to translate the data)
				if (dynamicLists != null) {
					list = dynamicLists.get(listRequestId);
				}
			}

			if (list != null) { 
				// list was found in either the static or dynamic lists map, so use it for translation
				if (list.get(propValue.toString()) != null) {
					// return the translated value
					return list.get(propValue.toString());
				}
				else {
					// if the value is not in the list, just output the value (e.g. numeric totals will have
					// associated list to convert "-5" to "Cannot Total" but otherwise should output the total)
					return propValue.toString();
				}
			}
			else {
				// list in metadata is not in the static or dynamic list maps, so just return the property value untranslated
				return propValue.toString();
			}
		}
	}
	
	// convenience method for translation that does not required dynamic list
	public String translatePropValueToText(String propName, Object propValue) {
		return translatePropValueToText(propName, propValue, null);
	}
	
	
	/**
	 * Convenience method for obtaining the label for a property, typically from the metadata. 
	 *  
	 * @param property in the form "entity.property"
	 * @return the label, or null if not found
	 */
	public String getPropLabel(String property) {
		return this.getMessage(property + ".label", null, true);
	}
	

	/**
	 * Convenience method for obtaining the name for section. Sections are identified by a key
	 * name, i.e. a short string used for section quicklinks, to identify the section to which a 
	 * property belongs in the metadata, and as a key to obtain a full text section name. This
	 * method translates this key to the name, typically stored in messages.properties. 
	 *  
	 * @param section the section key, including the entity, e.g. "bedsidescreen2_6_0.visualMemory"
	 * @return the name, or null if not found
	 */
	public String getSectionName(String sectionKey) {
		return this.getMessage(sectionKey + ".section", null);
	}
	
	
	/**
	 * Convenience method for obtaining section instructions. Note that the default is that
	 * the reportPdfStyling is done, since these methods are used by reports.   
	 *  
	 * @param section the message key for the section, e.g. "nam53screening.incGeneral"
	 * @return the instructions, or null if not found
	 */
	public String getSectionInstructions(String sectionKey) {
		return this.getSectionInstructions(sectionKey, (Object[]) null, Boolean.TRUE);
	}
	
	public String getSectionInstructions(String sectionKey, Object[] args) {
		return this.getSectionInstructions(sectionKey, args, Boolean.TRUE);
	}
	
	public String getSectionInstructions(String sectionKey, Object[] args, Boolean reportPdfStyling) {
		if (reportPdfStyling) {
			return this.getMessage(sectionKey + ".instructions", args, Boolean.TRUE);
		}
		else {
			return this.getMessage(sectionKey + ".instructions", args);
		}
	}
	
	
	/**
	 * General purpose convenience method for a message from the resource bundle or metadata,
	 * where the message does not take any arguments. 
	 *  
	 * @param key message key
	 * @param args the message arguments
	 * @return the message, or null if not found
	 */
	public String getMessage(String key) {
		return metadataManager.getMessage(key, new Object[] {}, Locale.getDefault());
	}
	
	/**
	 * General purpose convenience method for a message from the resource bundle or metadata,
	 * where the message can take arguments. 
	 *  
	 * @param key message key
	 * @param args the message arguments
	 * @return the message, or null if not found
	 */
	public String getMessage(String key, Object[] args) {
		return metadataManager.getMessage(key, args, Locale.getDefault());
	}

	/**
	 * Report specific convenience method for a message from the resource bundle or metadata,
	 * where the HTML is converted to the styling required for Jasper PDF reports (this facilitates
	 * sharing the same messages between web pages and reports). 
	 * note: Jasper reportElements must contain <textElement isStyledText="true"/> for the styling
	 *       to display. This can be used in conjunction with the reportElement style attribute where
	 *       the inline style put in by this method has precedence for pdfFontName, but other style
	 *       attributes such as fontSize come from the stylesheet
	 *  
	 * @param key message key
	 * @param args the message arguments
	 * @return the message, or null if not found
	 */
	public String getMessage(String key, Object[] args, Boolean reportPdfStyling) {
		// currently only supports bold & italic together if <b><i> is used, so e.g. if already
		// using <i>, then need to end that with </i>, and then begin bold italic with <b><i> and
		// end with </i></b>
		
		String msg = this.getMessage(key, args);
	    // breaks
		msg = msg.replaceAll("<br[/]?>","\n");
		// bold italic
		msg = msg.replaceAll("<b><i>","<style pdfFontName=\"Times-BoldItalic\">");
		msg = msg.replaceAll("</i></b>","</style>");
		// bold
		msg = msg.replaceAll("<b>","<style pdfFontName=\"Helvetica-Bold\">");
		msg = msg.replaceAll("</b>","</style>");
		// italic
		msg = msg.replaceAll("<i>","<style pdfFontName=\"Times-Italic\">");
		msg = msg.replaceAll("</i>","</style>");
		// underline
		msg = msg.replaceAll("<u>","<style isUnderline=\"true\">");
		msg = msg.replaceAll("</u>","</style>");
		return msg;
	}


	public boolean supportsAttachedFiles() {
		return getSupportsAttachedFiles();
	}
	public boolean getSupportsAttachedFiles() {
		return supportsAttachedFiles;
	}

	public void setSupportsAttachedFiles(boolean supportsAttachedFiles) {
		this.supportsAttachedFiles = supportsAttachedFiles;
	}

	public LavaFileDao getFileDao() {
		return fileDao;
	}

	public void setFileDao(LavaFileDao fileDao) {
		this.fileDao = fileDao;
	}
	
	
	/**
	 * Utility method to check if id property does not match entity.getId()
	 * returns false if both id and entity are null or if they match
	 * returns true otherwise.
	 * 
	 * This is used to quickly check whether an id property as used to 
	 * change an entity association in the UI differs from the current entity 
	 * in the association in the backing object. 
	 */
	protected boolean doesIdDifferFromEntityId(Long id,LavaEntity entity){
		//if both id and entity.id are null
		if(id==null && (entity==null || entity.getId()==null)){
			return false;
		}
		//if id equals entity.id
		if(id!=null && entity !=null && id.equals(entity.getId())){
			return false;
		}
		//otherwise they differ
		return true;
	}



	//utility function
	public AuthUser getCurrentUser(HttpServletRequest request){
		return CoreSessionUtils.getCurrentUser(sessionManager, request);
	}
	
	
	
	protected void initializeListsInModel(Map model){
		if(!model.containsKey("lists")){
			model.put("lists",new HashMap<String,Map<String,String>>());
		}
	}
	protected void addListToModel(Map model,String listRequestId, Map<String,String> list){
		initializeListsInModel(model);
		getListsFromModel(model).put(listRequestId, list);
	}
	
	protected void addDynamicListToModel(Map model,String listName, Map<String,String> list){
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put(listName, list);
		model.put("dynamicLists", dynamicLists);
		}
	
	
	
	protected void addListsToModel(Map model,Map<String,Map<String,String>> lists){
		initializeListsInModel(model);
		getListsFromModel(model).putAll(lists);
	}
	protected Map<String,Map<String,String>> getListsFromModel (Map model){
		initializeListsInModel(model);
		return (Map<String,Map<String,String>>)model.get("lists");
	}

	
	public String getPrimaryComponentContext() {
		return primaryComponentContext;
	}



	public void setPrimaryComponentContext(String primaryComponentContext) {
		this.primaryComponentContext = primaryComponentContext;
	}



	public void updateManagers(Managers managers) {
		this.actionManager = CoreManagerUtils.getActionManager(managers);
		this.authManager = CoreManagerUtils.getAuthManager(managers);
		this.listManager = CoreManagerUtils.getListManager(managers);
		this.metadataManager = CoreManagerUtils.getMetadataManager(managers);
		this.sessionManager = CoreManagerUtils.getSessionManager(managers);
		this.environmentManager = CoreManagerUtils.getEnvironmentManager(managers);
	}		
	
	
}
