package edu.ucsf.lava.crms.assessment.controller;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultBindingErrorProcessor;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ComponentHandler;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.controller.CrmsComponentFormAction;


public class InstrumentComponentFormAction extends CrmsComponentFormAction {
    // comboRadioSelect control needs a value for the radio button which represents 
    // the fact that a missing data code was selected. chose -9999 since that value
    // is not a valid result value. since comboRadioSelect can be used for String,
    // Short, Integer, Long and Float result properties, need String representions
    // of -9999 for all of those: "-9999" covers everything except for Float, which
    // is "-9999.0"
    // note: if these constants are changed, comboRadioSelect.tag needs to change
    //   and FormGuideTag.java needs to change
    private static final String COMBO_RADIO_SELECT_USE_SELECT = "-9999";
    private static final String COMBO_RADIO_SELECT_USE_SELECT_FLOAT = "-9999.0";

    // public constants, shared by handlers
	// note: if these change, jsps must change too
    public static final String INSTRUMENT = "instrument";
    public static final String COMPARE_INSTRUMENT = "compareInstrument";
    
	public InstrumentComponentFormAction() {
		super();
	}
	
	public InstrumentComponentFormAction(ArrayList<ComponentHandler> handlers) {
		super(handlers);
	}
	
    // the instrument type is used for two purposes:
    // 1) to determine the jsp page name for the instrument flow to display (since there is only one of
    //    each instrument CRDU flow shared across all instruments, the flows are effectively parameterized by
    //    the instrument type which appears in the request URL, except where an instrument has its own
	//    custom flow definition)
    // 2) to obtain the commmand class, i.e. the instrument-specific class, used for retrieving the instrument
    //    from the DAO, or if adding, for instantiating a new instrument (as such, this controller and the
    //    InstrumentHandler are effectively parameterized by the instrument type)
    // the instrument type is obtained from the current action (which parsed it from the requested URL) and
	// used by the flow to generate the jsp to display (in CustomViewSelector), and by the handler,
    // to look up the instrument-specific class (which it is able to do via a service from configuration data
    // in lava-dao.xml which maps instrument types to instrument model classes. this technique facilitates
    // using one FormAction class and one handler class for all instruments, unless an instrument requires
	// a custom flow definition which uses a custom FormAction injected with the custom instrument specific
	// handler)

    // regarding instruments with versions, each version has its own jsp page. however, all versions use the
    // same model class/Hibernate mapping, so all versions are mapped to that class in lava-dao.xml
    // if there is no version, instrTypeEncoded reduces to the encoded instrType 
    // if the instrument does have versions, instrTypeEncoded is simply the concatenation of encoded
    // instrType and the encoded instrVer

	
	/* 
	 * Custom binding for instrument views with results fields handles:
	 * 	1) user has chosen to set all missing result fields to a missing data code (request param "missingDataCode")
	 * 	2) handling the result field comboRadioSelect input widget used in the data collection flow, where 
	 *     value could come from radiobutton group or a select box
	 *  3) result field required field validation. done here rather than as part of core binding process,
	 *     because the core binding process could create field errors which both 1) and 2) above can 
	 *     clear, and since it is not possible to remove field errors from the errors object, just do all
	 *     validation here   
	 * 
	 *  note: this includes setting field errors that are a result of the binding process, i.e. 
	 *        specifically, required field validation errors. details:  
	 *        in addition to doing required field validation on all result fields, there is custom 
	 *        required field validation that takes place for comboRadioSelect input widgets that could 
	 *        not be handled by the Spring MVC superclass BaseCommandController because the radiobutton, 
	 *        which is bound to a command field, always has a value, even when the field is null (i.e. 
	 *        when the hidden "Missing Code:" radiobutton is checked and the selectbox is set to the 
	 *        blank entry). the browser enforces that one radiobutton in a group is checked at all times, 
	 *        so the value for the radiobuttons group will never be missing
	 */
	public Event customBindResultFields(RequestContext context) throws Exception {
		customBindResultFieldsHelper(context, false);
		BindingResult errors = (BindingResult) getFormErrors(context);
		// return Event result code to flow for transition
		if (errors.hasFieldErrors()) {
			// create command-level errors based on the existence of any field-level errors
			createCommandErrorsForFieldErrors(errors);
			return error();
		}
		else {
			return success();
		}
	}
	
	public Event customBindResultFieldsIgnoreErrors(RequestContext context) throws Exception {
		customBindResultFieldsHelper(context, true);
		return success();
	}
	
	protected void customBindResultFieldsHelper(RequestContext context, boolean ignoreFieldErrors) throws Exception {
		ComponentCommand componentCommand = (ComponentCommand)getFormObject(context);
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		String event = ActionUtils.getEventName(context);
		
		// this method handles the following:
		//  - custom comboRadioSelect binding
		//  - instrument required field validation,
		//  - user choosing to mark missing fields with a missing data code
		
		// first, bind properties to the command object
		// note: since result fields are not marked as required fields, and only result fields appear
		//  on a result field enter/collect page, bind will not generate any required field errors
		bind(context);
		BindingResult errors = (BindingResult) getFormErrors(context);
		
		// set the instrument entity to process, based on the action event
		Instrument instrument = null;
		String component = null;
		// for double entry verification, when user has just double entered the data, then
		// process the "compareInstrument" instrument object
		// note: on "compare" event, user input is submitted to both the "instrument" and
		//       "compareInstrument" map entries. however, it is enough to just perform this processing
		//       on the "instrument" instrument object, because if required field validation
		//       is performed on the "instrument" (guaranteeing nothing is null), and the "compareInstrument"
		//       does have null for a required field, an error will still be flagged because 
		//       verification will fail since the field values are not equal. so no need
		//       to also process the "compareInstrument" instrument object here for the saveDoubleEnter
		//       action event (also, in enter flow, the fields are not comboRadioSelects, and
		//       on "compare" event, there is no action to mark missing fields with a missing data code, 
		//       so do not need to process the "compareInstrument" instrument object for those purposes either)
		if (event.equals("doubleEnterSave") || event.equals("hideCodesDoubleEnter") || event.equals("showCodesDoubleEnter")) {
			component = COMPARE_INSTRUMENT;
		}
		// in all other cases, process the "instrument" instrument object 
		else {
			component = INSTRUMENT;
		}
		instrument = (Instrument)((ComponentCommand)componentCommand).getComponents().get(component);
		
		
		// iterate thru the properties of the instrument to do any custom binding for comboRadioSelectFields
		PropertyDescriptor[] propDescriptors = PropertyUtils.getPropertyDescriptors(instrument);
		for (PropertyDescriptor propDescrip : propDescriptors) {

			// BeanUtils getProperty returns property value as a String
			String propValue = BeanUtils.getProperty(instrument, propDescrip.getName());
			if (propValue != null && 
					(propValue.equals(COMBO_RADIO_SELECT_USE_SELECT) || propValue.equals(COMBO_RADIO_SELECT_USE_SELECT_FLOAT))) {
				// Handle comboRadioSelect input widgets.
				
				// For comboRadioSelect processing, determine when the value should come from the select rather 
				// than from the radiobuttons. This is indicated when the hidden "Missing Code:" radiobutton is 
				// selected, in which case the property will have the value of COMBO_RADIO_SELECT_USE_SELECT 
				// depending upon the type of the result field (the "Missing Code:" radiobutton is autoselected via 
				// javascript when a value is selected from the missing data codes select box)
					
				// In this case, the actual value for this property should come from the associated select. The 
				// associated select has the same property name as the radio but with "_CODE" appended, so use 
				// this as the property value.
					
				// If the hidden "Missing Code:" radiobutton is checked and the selectbox value is the empty string
				// (not null, because the selectbox is not a command field, so the property editor binding that
				// sets empty string to null is not done), set the actual value of the property to null, so that
				// it will be flagged as a required field error in the next loop. 

				// since BeanUtils.getProperty above converts the property value to a String from whatever its
				// native type is, just have to compare against String values here; "-9999" for String and 
				// Integer/Short/Long types, and "-9999.0" for Float types

				// logger.error("propValue is -9999, so getting value from select box");
				StringBuffer fullPropName = new StringBuffer("components['");
				fullPropName.append(component).append("'].").append(propDescrip.getName());
				String codeValue = request.getParameter(fullPropName.toString() + "_CODE");
				if (codeValue.equals("")) {
					//logger.error("select box (codeValue) is empty string, so set prop to null");
					// set the property value to null
					// note: using BeanUtils.setProperty here does not properly set numeric types to null, but
					//       rather sets them to 0, so switched to PropertyUtils and it does set them to null
					PropertyUtils.setProperty(instrument, propDescrip.getName(), null);
				}
				else {
					// set the property value to the comboRadioSelect select box value 
					BeanUtils.setProperty(instrument, propDescrip.getName(), codeValue);
					//logger.error("select box (codeValue) is NOT empty string, so set prop to codeValue");
				}
			}
		}
		
		
		// iterate thru all required fields, creating field errors for any that are missing, or, if user has specified
		// a missing data code to use for all missing required field values, assign the code
		for (String propName : instrument.getRequiredResultFields()) {
			// create version of fullPropName without the quotes around the component name because
			// when adding fieldError, Spring matches without the single quotes (which seems like a bug)
			StringBuffer fullPropNameNoQuotes = new StringBuffer("components[").append(component).append("].").append(propName);
			
			// BeanUtils getProperty returns property value as a String
			String propValue = BeanUtils.getProperty(instrument, propName);
			//String propValueToPrint = propValue == null ? "NuLL" : propValue;
			//logger.error("propName="+propName+" propValue<" + propValueToPrint  + ">");
				
			// Handle regular result input widgets. 
			if (propValue == null) {
				// For marking missing fields with a code, if user has chosen to do this (as indicated
				//  by the value of the "missingDataCode" request parameter not being empty) then for every 
				//  field that has a null value, set its value to the chosen code.
				String missingDataCode = context.getRequestParameters().get("missingDataCode");
				if (StringUtils.isNotEmpty(missingDataCode)) {
					// set the property value to the selected missing data code (Date properties can not be set
					// to missing data code because format validation will fail, so they are just left null and
					// the user will receive a required field error until they enter a date)
					if (PropertyUtils.getPropertyType(instrument, propName) != Date.class) {
						BeanUtils.setProperty(instrument, propName, missingDataCode);
					}
				}
				else {
					if (!ignoreFieldErrors) {
						// generate a required field error for this property
		
						// the following code to add required field error is modeled after DefaultBindingErrorProcessor 
						// processMissingFieldError method, which is the same thing that DataBinder uses to add required
						// field errors (unfortunately, we do not have access to the binder object here, or we could just 
						// call the binder methods to do this)
		
						// create field error keys with code MISSING_FIELD_ERROR_CODE ("required")
						//  (e.g. required.command.think, required.think, required.java.lang.String, required) 
						// note: use simple propName here to match error keys
						String[] codes = errors.resolveMessageCodes(DefaultBindingErrorProcessor.MISSING_FIELD_ERROR_CODE, propName);
						// get arguments for error that could optionally be used in the field error message, though would never put 
						// field names in error message because they are not user friendly (e.g. command.think, think)
						String[] arguments = new String[] {errors.getObjectName() + Errors.NESTED_PATH_SEPARATOR + propName, propName};
						errors.addError(new FieldError(
								errors.getObjectName(), fullPropNameNoQuotes.toString(), "", true,
								codes, arguments, "Field '" + propName + "' is required"));
					}
					
				}
			}
		}
	}
}
	


