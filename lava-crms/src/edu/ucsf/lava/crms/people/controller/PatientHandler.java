package edu.ucsf.lava.crms.people.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class PatientHandler extends CrmsEntityComponentHandler {
	protected static final String DELETE_PATIENT_WITH_VISITS_ERROR_CODE = "patient.deleteHasVisits.command";
	protected static final String DELETE_PROBAND_WITH_FAMILY_MEMBERS_ERROR_CODE="patient.deleteWithFamilyMembers.command";

	public PatientHandler() {
		this.setHandledEntity("patient", Patient.class);
		CrmsSessionUtils.setIsPatientContext(this);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
		if (ActionUtils.getEventName(context).equalsIgnoreCase("saveAdd") || ActionUtils.getEventName(context).equalsIgnoreCase("save")) {
			Map components = ((ComponentCommand)command).getComponents();
			Patient p = (Patient) components.get("patient");
			
			if(p.getDeidentified()){
				setRequiredFields(new String[]{
		 				"subjectId",
		 				"birthDate",
		 				"gender",
		 				"deceased",
 						"dupNameFlag"});
			}else{		
				setRequiredFields(new String[]{
	 				"firstName",
	 				"lastName",
	 				"birthDate",
	 				"gender",
	 				"deceased",
	 				"dupNameFlag"});
			}
			return getRequiredFields();
		}
		return new String[0];
	}
	
	// override save to validate 
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		Patient patient = (Patient) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if (patient.getBirthDate() != null && patient.getBirthDate().after(new Date())) {
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"patient.futureBirthDate"}, new String[]{new SimpleDateFormat("MM/dd/yyyy").format(patient.getBirthDate())}, ""));
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		// check to ensure death date was a valid date, yet accepting all dates having "unknown" parts
		// beware input leniency, where 2/31/1999 gets saved as 3/3/1999 (a valid date); check equal date components
		Calendar calculatedDOD = Calendar.getInstance();
		Date deathDate = patient.getDeathDate();
		if (deathDate!=null) {
			calculatedDOD.setTime(deathDate);
		
			if (deathDate!=null && deathDate.after(new Date())) {
				errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"patient.futureDeathDate"}, new String[]{patient.getDeathMonth().toString()+'/'+patient.getDeathDay().toString()+'/'+patient.getDeathYear().toString()}, ""));
				return new Event(this,ERROR_FLOW_EVENT_ID);
			}
			if (patient.getDeathMonth()!=null && !patient.getDeathMonth().equals((short)99)
				&& patient.getDeathDay()!=null && !patient.getDeathDay().equals((short)99)
				&& patient.getDeathYear()!=null && !patient.getDeathYear().equals((short)9999)) {
				// the date components should match exactly, else leniency was used
				if (!patient.getDeathMonth().equals((short)(calculatedDOD.get(Calendar.MONTH)+1))
					|| !patient.getDeathDay().equals((short)calculatedDOD.get(Calendar.DAY_OF_MONTH))
					|| !patient.getDeathYear().equals((short)calculatedDOD.get(Calendar.YEAR))) {
					errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"patient.invalidDeathDate"}, new String[]{patient.getDeathMonth().toString()+'/'+patient.getDeathDay().toString()+'/'+patient.getDeathYear().toString()}, ""));
					return new Event(this,ERROR_FLOW_EVENT_ID);
				}
			}
		}

		
		String modifiedBy = CrmsSessionUtils.getCrmsCurrentUser(sessionManager,((ServletExternalContext)context.getExternalContext()).getRequest()).getUserName();
		if (modifiedBy != null && modifiedBy.length() > 25) {
			// have to truncate due to database mismatch in column lengths
			modifiedBy = modifiedBy.substring(0,24);
		}
		patient.setModifiedBy(modifiedBy);
		
		return super.doSave(context,command,errors);
	}

	@Override
	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient patient = (Patient) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		
		if(patient.hasVisits()){
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{DELETE_PATIENT_WITH_VISITS_ERROR_CODE}, null);
			 return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		if(patient.patientIsProbandWithFamilyMembers()){
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{DELETE_PROBAND_WITH_FAMILY_MEMBERS_ERROR_CODE}, null);
			 return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		Event event = super.doConfirmDelete(context, command, errors);
		// if returning error event, flow definition will return to delete page
		// if successful deletion, set the current patient null, and the flow will transition to the 
		// confirmDelete event target
		if (event.getId().equals(SUCCESS_FLOW_EVENT_ID)) {
			CrmsSessionUtils.clearCurrentPatient(sessionManager, request);
		}
		return event;
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
	
		
		Patient p = (Patient)((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		
		dynamicLists.put("patient.staffList", listManager.getDynamicList("patient.patientStaffList", 
					"patientId", p.getId(),Long.class));
	
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}




}
