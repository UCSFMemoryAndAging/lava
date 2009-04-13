package edu.ucsf.lava.core.controller;

import static edu.ucsf.lava.core.controller.ComponentHandler.SUCCESS_FLOW_EVENT_ID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.propertyeditors.CharacterEditor;
import org.springframework.beans.propertyeditors.CustomBooleanEditor;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.engine.ViewSelector;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.util.CustomDatePartEditor;
import edu.ucsf.lava.core.util.CustomDateTimeEditor;

import edu.ucsf.lava.core.util.CustomTimePartEditor;
import edu.ucsf.lava.core.util.DatePart;
import edu.ucsf.lava.core.util.DateTime;
import edu.ucsf.lava.core.util.TimePart;
import edu.ucsf.lava.core.webflow.CustomReportSelector;
import edu.ucsf.lava.core.webflow.CustomViewSelector;


//This class adds in LAVA specific functionality that should not be coupled into the logic
//of the BaseComponentFormController.  
public class LavaComponentFormAction extends BaseComponentFormAction {
	
	// the following ViewSelectors are used by the FlowBuilders to define the flows so that
	// application views, defaultPatientAction views, and defaultProjectAction views are
	// determined at runtime, since they could be project specific
	protected CustomViewSelector customViewSelector; //injected
	protected CustomReportSelector customReportSelector; //injected
	protected ViewSelector defaultActionViewSelector; //injected

	// command-level error keys
    protected static final String COMMAND_REQUIRED_FIELD_ERROR_CODE = "required.command";
    protected static final String COMMAND_TYPE_MISMATCH_ERROR_CODE = "typeMismatch.command";

   
	public LavaComponentFormAction() {
		super();
	}

	public LavaComponentFormAction(ArrayList<ComponentHandler> handlers) {
		super(handlers);
	}
	
	// constructor if only one handler
	public LavaComponentFormAction(ComponentHandler handler) {
		super(handler);
	}
	

	//functionality added to handle action and context setting. 
	//this is called by formAction setupForm ONLY if there is not already a form
	//backing object in scope.
    protected Object createFormObject(RequestContext context) throws Exception {
    	// iterate thru the handlers, creating the backing objects for each.
    	
    	// form action's entities are configured to store the form backing object in 
    	// flow scope under the name "command". must use flow scope and not conversation scope
    	// because within a given conversation there could be more than one entity flow
    	// and since they are all stored under "command" they must all be stored in their
    	// own flow scopes
    	
    	// form action's for lists are configured to store the form backing object in flow
    	// scope under the name "command".  
    	
		// use the first handler to set context...the first handler is primary.
    	
    	// because the POST-REDIRECT-GET pattern is in effect automatically (because web flow is
    	// configured with alwaysRedirectOnPause true by default) render actions are executed after the
    	// second request, so request parameters are not available. instead input mappers in the flow
    	// definitions of flows that depend on request parameters transfer needed values to flow
    	// scope. so here, look in flow scope for the id, not in request parameters.
    	if(StringUtils.isNotEmpty(context.getFlowScope().getString("id"))){
			componentHandlers.iterator().next().setContextFromScope(context);   
		}
		return super.createFormObject(context);
	}
    
    
	//set standard property editors for binding on LAVA forms before registering binders on components. 
	// note: registerPropertyEditors is called by createBinder which is called by setupForm (when initializing
	// the form errors object) and by bind
	protected void registerPropertyEditors(PropertyEditorRegistry registry) {
		// this will set empty String fields to null, rather than '' (empty string)
		// NOTE: the rationale is that not-null properties mean that a value has been
		//       supplied, and the empty string should not be considered a value, because
		//       in terms of user input, it means they did not enter anything
		
		//strings
		registry.registerCustomEditor(String.class, new StringTrimmerEditor(true));

		//make date only fields java.util.Date (Hibernate type=timestamp)
		// note: list filter date fields do not use this registered custom editor because the Spring
		// binding process does not determine the type of the fields, presumably because LavaDaoFilter 
		// params is a Map and the filter field values are of type Object. therefore, since type is
		// unknown, no property editor is used. However, the filter params of type java.util.Date are
		// converted from string input to Date property, as java.util.Date can parse many syntaxes.
		// this begs the question of whether registering this custom editor for "MM/dd/yyyy" is
		// even necessary --- possibly not
		
		// *** should always register custom editors for properties requiring a time in addition to date 
		// (i.e. datetime format) as property-specific in a handler subclass, so as not to interfere with this
		// java.util.Date class-specific custom editor which is used by all properties that just require a date portion.
		// i.e. since most Date properties only require a date portion, the custom editor for java.util.Date is dedicated
		// to them and nothing need be done for these properties in handler subclasses
		// e.g Visit class has visitDate which required datetime format, and so VisitHandler registers a custom editor
		// specific to that property which includes time in the format. Visit also had waitListDate which only requires the
		// date portion, so it just uses the default java.util.Date custom editor registered here 
		// vital: for registering property-specific custom editors, Spring binding requires our syntax to not include the 
		// single quotes around the component entity name,
		// e.g. given SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mma");  
		// must use:
		//  registry.registerCustomEditor(Date.class, "components[visit].visitDate", new CustomDateEditor(dateFormat, true));
		// not
		//  registry.registerCustomEditor(Date.class, "components['visit'].visitDate", new CustomDateEditor(dateFormat, true));
		SimpleDateFormat datePartFormat = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yyyy h:mma");
		SimpleDateFormat timePartFormat = new SimpleDateFormat("h:mma");
		registry.registerCustomEditor(Date.class, new LavaCustomDateEditor(datePartFormat, true));
		registry.registerCustomEditor(DatePart.class, new CustomDatePartEditor(datePartFormat, true));
		registry.registerCustomEditor(DateTime.class, new CustomDateTimeEditor(dateTimeFormat, true));
		registry.registerCustomEditor(TimePart.class, new CustomTimePartEditor(timePartFormat, true));
			
		registry.registerCustomEditor(Long.class, new CustomNumberEditor(Long.class,true));
		
		//	this will set all Short fields to all null when empty string ("") is submitted
		registry.registerCustomEditor(Short.class, new CustomNumberEditor(Short.class, true));
		
		//This will map true to 1 and false to 0.  (the default binder maps to true and false)  If this is not what is needed in the subclass, 
		//a specific boolean editor can be configured for specific propoerites
		registry.registerCustomEditor(Boolean.class, 	new CustomBooleanEditor(
					"1","0",true));
		//chars
		registry.registerCustomEditor(Character.class, new CharacterEditor(true));
		registry.registerCustomEditor(Byte.class, new CustomNumberEditor(Byte.class,true));
		registry.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class,true));
		// this will set all Float fields to null when empty string ("") is submitted
		registry.registerCustomEditor(Float.class, new CustomNumberEditor(Float.class, true));

		// for multiple select box, this will bind selection(s) to a List (this works for a List<String>
		// where select option values are String (equivalent to option text)
		registry.registerCustomEditor(List.class, new CustomCollectionEditor(List.class, true));
		
		super.registerPropertyEditors(registry);
	}

	
	// this is a Spring Web Flow Action method so must conform to that signature
	public Event customBind(RequestContext context) throws Exception {
		
		// call the FormAction bind to do standard binding to the command object, as well as required field error
		// and field format checking. 
		Event bindReturnEvent = bind(context);

		// if there were any required field errors, create a required field ObjectError for the command object, 
		//  so that the view will display a header level error message. Do the same for invalid format, i.e.
		//  typeMismatch field errors.
		// note: Spring does not create an command object error if there are field errors, so <spring:bind path="entity"/>
		//       will not produce any error messages for display in the message box at the top of each page unless they
		//       are explicitly created
		
		// note: separating the call to FormAction.bind and createCommandErrors in the flow definition does not 
		// work, i.e. in the transition actions, calling bind, and then calling a method to create the 
		// command-level errors won't work because in transition actions, if an action signals the error event, 
		// no further actions are executed, he transition is aborted and the flow remains in the current view-state. 
		// so if there were bind errors, bind would signal the error event and the flow would not execute the next
		// transition action which creates the command-level error and consequently, only the field-level
		// errors created in bind display, not any command-level errors
		// thus, the functionality is combined into a single method, customBind, which must be sure
		// to signal the error event if the call to bind signalled the error event, so that the flow 
		// will not transition and instead will refresh the current view-state with the field-level and
		// command-level errors displayed
		if (bindReturnEvent.toString().equals(error().toString())) {
			createCommandErrorsForFieldErrors((BindingResult)getFormErrors(context));
		}
		
		return bindReturnEvent;
	}
	
	protected Map coreReferenceData(RequestContext context) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("flowId", context.getActiveFlow().getId()); // e.g. lava.crms.assessment.instrument.cdr.enter
		model.put("flowMode", ActionUtils.getFlowMode(context.getActiveFlow().getId())); // e.g. enter
		model.put("flowState", context.getCurrentState().getId()); // e.g. enter
		model.put("flowEvent", ActionUtils.getEventId(context)); // e.g. continue
		model.put("flowIsRoot", new Boolean(context.getFlowExecutionContext().getActiveSession().isRoot()));
		model.put("webappInstance", actionManager.webappInstanceName); // for reports
		model.put("actions",actionManager.getActionRegistry().getActions());
		model.putAll(sessionManager.getContextFromSession(request));
		return model;
	}

	
	// this is called by the prepareToRender form action. after each handlers prepareToRender method
	// has been called, this method is called which adds core data structures to the reference data
	// model, and then it calls each handler's addReferenceData method
	//override to put common lava reference data in model
	protected Map referenceData(RequestContext context, Object command, BindingResult errors) {
		Map<String,Object> model = coreReferenceData(context);
		//note: the view mode is placed in the model in the component reference data calls. 
		return super.getComponentReferenceData(context, command, errors,model);
	}


	// used by the error page which needs the core reference data to correctly create links
	// for tabs. error page should not use prepareToRender (which calls referenceData above) because 
	// if there was an exception in setupForm (createFormObject), prepareToRender would call 
	// createFormObject again to get the form object, which would generate the same exception again
	// and the error page would never be displayed
	public Event errorReferenceData(RequestContext context) throws Exception {
		MutableAttributeMap requestScope = context.getRequestScope();
		Map<String,Object> coreRefData = coreReferenceData(context);
		for (Map.Entry<String,Object> entry : coreRefData.entrySet()) {
			requestScope.put(entry.getKey(), entry.getValue());
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	

	
// utility methods	
	//TODO: put these into a controllers utility class so that handlers are not referencing FormAction methods
	
	/**
	 * If there are required field errors or field format (aka type mismatch) errors, then 
	 * create a command error to display at the top of the view.
	 * 
	 * @param	errors
	 */
	// this was separated out into a separate method so the code could be shared by subclasses, such
	// as InstrumentComponentFormAction, and handlers (which is why it is a static method)
	public static void createCommandErrorsForFieldErrors(BindingResult errors) throws Exception {
		boolean requiredFieldError = false;
		boolean typeMismatchError = false;
		// iterate over field errors to determine what kind of field errors occurred
		for (Object error : errors.getAllErrors()) {
			if (error instanceof FieldError) {
				if (((FieldError)error).getCode().startsWith(DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE)) {
					requiredFieldError = true;
				}
				else if (((FieldError)error).getCode().startsWith(TypeMismatchException.ERROR_CODE)) {
					typeMismatchError = true;
				}
				
				// note: when using command objects that have map syntax, the Spring binder associates errors
				//  	with properties without the single quotes surrounding the map key, i.e. the component name
				// 		e.g. <spring:bind> to field components['visit'].visitDate, but if creating a fieldError
				//      for this field, must specify the property name as components[visit].visitDate
			}
			if (requiredFieldError & typeMismatchError) {
				break;
			}
		}

		// add a command object-level error to the errors object if there were any required field errors
		if (requiredFieldError) {
			// messages.properties code will be "required.command"
			errors.addError(new ObjectError(errors.getObjectName(), 
					new String[]{COMMAND_REQUIRED_FIELD_ERROR_CODE}, null, ""));
		}
		// add a command object-level error to the errors object if there were any type mismatch (i.e. formatting) field errors
		if (typeMismatchError) {
			// messages.properties code will be "typeMismatch.command"
			errors.addError(new ObjectError(errors.getObjectName(), 
					new String[]{COMMAND_TYPE_MISMATCH_ERROR_CODE}, null, ""));
		}
	}
	
	public static void createCommandError(BindingResult errors, String msgKey, Object[] msgArgs) throws Exception {
		errors.addError(new ObjectError(errors.getObjectName(),	new String[]{msgKey}, msgArgs, ""));
	}

	// this is modeled after required field error creation done in Spring MVC code
	public static void createRequiredFieldError(BindingResult errors, String propName, String objName) throws Exception {
		StringBuffer fullPropNameNoQuotes = new StringBuffer("components[").append(objName).append("].").append(propName);
		String[] codes = errors.resolveMessageCodes(DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE, propName);
		// get arguments for error that could optionally be used in the field error message, though would never put 
		// field names in error message because they are not user friendly (e.g. command.think, think)
		String[] arguments = new String[] {objName + Errors.NESTED_PATH_SEPARATOR + propName, propName};
		errors.addError(new FieldError(
				objName, fullPropNameNoQuotes.toString(), "", true,
				codes, arguments, "Field '" + propName + "' is required"));
	}
	
	// create a field error where the error msg is passed in as the message key
	public static void createFieldError(BindingResult errors, String propName, String objName, Object rejectedValue, String msgKey) throws Exception {
		StringBuffer fullPropNameNoQuotes = new StringBuffer("components[").append(objName).append("].").append(propName);
		String[] codes = errors.resolveMessageCodes(msgKey, propName);
		// get arguments for error that could optionally be used in the field error message, though would never put 
		// field names in error message because they are not user friendly (e.g. command.think, think)
		String[] arguments = new String[] {objName + Errors.NESTED_PATH_SEPARATOR + propName, propName};
		errors.addError(new FieldError(objName, fullPropNameNoQuotes.toString(), rejectedValue, true, codes, arguments, null));
	}

	



	public CustomViewSelector getCustomViewSelector() {
		return this.customViewSelector;
	}
	
	public void setCustomViewSelector(CustomViewSelector customViewSelector) {
		this.customViewSelector = customViewSelector;
	}

	public CustomReportSelector getCustomReportSelector() {
		return this.customReportSelector;
	}
	
	public void setCustomReportSelector(CustomReportSelector customReportSelector) {
		this.customReportSelector = customReportSelector;
	}


	public ViewSelector getDefaultActionViewSelector() {
		return this.defaultActionViewSelector;
	}
	
	public void setDefaultActionViewSelector(ViewSelector defaultActionViewSelector) {
		this.defaultActionViewSelector = defaultActionViewSelector;
	}
}
	


