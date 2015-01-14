package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class FamilyPatientHandler extends CrmsEntityComponentHandler {
	protected static final String CHANGE_PROBAND_WITH_FAMILY_MEMBERS_ERROR_CODE="patient.changeProbandWithFamilyMembers.command";

	public FamilyPatientHandler() {
		this.setHandledEntity("familyPatient", Patient.class);
		CrmsSessionUtils.setIsPatientContext(this);
	}

	protected String[] defineRequiredFields(RequestContext context, Object command) {
		if (ActionUtils.getEventName(context).equalsIgnoreCase("saveAdd") || ActionUtils.getEventName(context).equalsIgnoreCase("save")) {
			Map components = ((ComponentCommand)command).getComponents();
			Patient p = (Patient) components.get(getDefaultObjectName());
			
			setRequiredFields(new String[]{"familyStatus"});
			return getRequiredFields();
		}
		return new String[0];
	}
	
	// note that this handler is not used to add Patient so no doSaveAdd. This handler is for the family
	// properties of existing Patients
	
	// override save to validate
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient patient = (Patient) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());

		//TODO: conditional required field validation of relationToProband and twin 
		// if familyStatus is "family" or "familyStudy"	
		
		
		
		
		
		//TODO: validate that if Proband chosen that there is not already a Proband
		//try this in MAC LAVA to see what happens
		
		
		// if the patient is the Proband and has other family members, the FamilyStatus can not be set to "Individual" and the
		// RelationToProBand should be set to "Proband"
		if (patient.patientIsProbandWithFamilyMembers() && 
				((patient.getFamilyStatus() != null && patient.getFamilyStatus().equals("Individual")) 
					|| (patient.getRelationToProband() != null && !patient.getRelationToProband().equals("Proband")))){
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{CHANGE_PROBAND_WITH_FAMILY_MEMBERS_ERROR_CODE}, null);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		
		
		String modifiedBy = CrmsSessionUtils.getCrmsCurrentUser(sessionManager,((ServletExternalContext)context.getExternalContext()).getRequest()).getUserName();
		if (modifiedBy != null && modifiedBy.length() > 25) {
			// have to truncate due to database mismatch in column lengths
			modifiedBy = modifiedBy.substring(0,24);
		}
		patient.setModifiedBy(modifiedBy);
		
		return super.doSave(context,command,errors);
	}
	

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		// because the component is 'familyPatient' but the entity is Patient so get lists 
		this.addListsToModel(model, listManager.getStaticListsForEntity("patient"));

		//load up dynamic lists
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		
		dynamicLists.put("patient.familyIds", listManager.getDynamicList("patient.familyIds"));
	
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}




}
