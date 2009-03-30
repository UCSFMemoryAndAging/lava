package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.ContactInfo;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ContactInfoHandler extends CrmsEntityComponentHandler {
	public ContactInfoHandler() {
		super();
		setHandledEntity("contactInfo", edu.ucsf.lava.crms.people.model.ContactInfo.class);
		CrmsSessionUtils.setIsPatientContext(this);
		// required fields set below, as they depend on the data
	}

	public Map getBackingObjects(RequestContext context, Map components) {
		Map backingObjects = super.getBackingObjects(context, components);

		ContactInfo contactInfo = (ContactInfo)backingObjects.get(getDefaultObjectName());
		if (contactInfo.getCaregiverId() == null) {
			contactInfo.setIsCaregiver(false);
			setRequiredFields(new String[]{"active","contactPatient"});
		}
		else {
			contactInfo.setIsCaregiver(true);
			setRequiredFields(new String[]{"active","caregiverId"});
		}
		
		return backingObjects;
	}
	
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		if (p != null){
			((ContactInfo)command).setPatient(p);
		}
		return command;
	}

	protected Long getContextIdFromRequest(RequestContext context){		
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ContactInfo c = (ContactInfo) ContactInfo.MANAGER.getOne(getFilterWithId(request,Long.valueOf(context.getFlowScope().getString("id"))));
		return c.getPatient().getId();
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);


		
		dynamicLists.put("patient.caregivers", 
				listManager.getDynamicList("patient.caregivers", "patientId", 
				((ContactInfo)((ComponentCommand)command).getComponents().get("contactInfo")).getPatient().getId(), Long.class));
		dynamicLists.put("contactInfo.city", 
				listManager.getDynamicList("contactInfo.city"));
		model.put("dynamicLists", dynamicLists);
	
		return super.addReferenceData(context, command, errors, model);
	}
	
	
	//override 
	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		ContactInfo contactInfo = ((ContactInfo)((ComponentCommand)command).getComponents().get("contactInfo"));
		// isCaregiver is not persisted, so it must be initialized from some other persistent field(s). this is
		// done in getBackingObjects above, based on the caregiverId field
		// once the object is loaded as the form command object, it is persisted to memory as long as the edit 
		// flow is active (or the view/edit flow if edit is a subflow of view), so its submitted value is 
		// accurate
		
		// however, if user checks isCaregiver but does not set caregiverId and saves, then the next time 
		// user edits the record, isCaregiver will erroneously be set back to false, since caregiverId is null. 
		// to prevent this from happening, caregiverId should be set to a required field whenever isCaregiver is 
		// checked (and removed from the required fields whenever isCaregiver is unchecked). also, if isCaregiver
		// is unchecked, caregiverId field is not used so it is hidden from the user and the caregiver field should 
		// be set to null here
		if (contactInfo.getIsCaregiver()) { 
			setRequiredFields(new String[]{"active","caregiverId"});
		}
		else {
			contactInfo.setCaregiverId(null);
			setRequiredFields(new String[]{"active", "contactPatient"});
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

}
