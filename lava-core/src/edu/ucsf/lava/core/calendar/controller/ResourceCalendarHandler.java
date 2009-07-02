package edu.ucsf.lava.core.calendar.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserGroup;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.ResourceCalendar;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;

public class ResourceCalendarHandler extends BaseEntityComponentHandler {

	public ResourceCalendarHandler() {
		super();
		this.setHandledEntity("resourceCalendar", ResourceCalendar.class);
		this.setRequiredFields(new String[]{"name","description","resourceType"});
		
	}

	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleContactChange(context,command, errors);
		return super.doSave(context, command, errors);
	}
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleContactChange(context,command, errors);
		return super.doSaveAdd(context, command, errors);
	}

	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleContactChange(context,command, errors);
		return super.doReRender(context, command, errors);
	}
	




	
	protected void handleContactChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ResourceCalendar rc = (ResourceCalendar)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(rc.getContactId(),rc.getContact())){
			if(rc.getContactId()==null){
				rc.setContact(null); 	//clear the association
			}else{
				AuthUser u = (AuthUser)AuthUser.MANAGER.getOne(getFilterWithId(request, rc.getContactId()));
				rc.setContact(u);
			}
		}
	}


	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		//add lists from the base calendar entity. 
		addListsToModel(model, listManager.getStaticListsForEntity("calendar"));
		dynamicLists.put("resourceCalendar.contact", listManager.getDynamicList(getCurrentUser(request),"resourceCalendar.contact"));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	
	
}
