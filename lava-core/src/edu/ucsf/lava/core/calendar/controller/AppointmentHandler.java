package edu.ucsf.lava.core.calendar.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import edu.ucsf.lava.core.session.CoreSessionUtils;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserGroup;
import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;

public class AppointmentHandler extends BaseEntityComponentHandler {

	
	
	public AppointmentHandler() {
		super();
		this.setHandledEntity("appointment", Appointment.class);
		this.setRequiredFields(new String[]{
					//"organizerId",
					"startDate",
					"startTime",
					"endDate",
					"endTime"});
		}



	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment a = (Appointment)command;
		if(request.getParameterMap().containsKey("calendarId")){
			String calendarId = request.getParameter("calendarId");
			if(StringUtils.isNumeric(calendarId)){
				Calendar calendar = (Calendar)Calendar.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(calendarId)));
				if(calendar != null){
					a.setCalendar(calendar);
				}
			}
		}
		//set the organizer to the current user. 
		a.setOrganizer(CoreSessionUtils.getCurrentUser(sessionManager, request));
		//TODO:  determine whether to auto add organizer to list of attendees as the organizer role.
		
		return command;
	}
	
	
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleOrganizerChange(context,command, errors);
		return super.doSave(context, command, errors);
	}
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleOrganizerChange(context,command, errors);
		return super.doSaveAdd(context, command, errors);
	}

	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleOrganizerChange(context,command, errors);
		return super.doReRender(context, command, errors);
	}
	
	
	protected void handleOrganizerChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment a = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(a.getOrganizerId(),a.getOrganizer())){
			if(a.getOrganizerId()==null){
				a.setOrganizer(null); 	//clear the association
			}else{
				AuthUser au = (AuthUser)AuthUser.MANAGER.getOne(this.getFilterWithId(request,a.getOrganizerId()));
				a.setOrganizer(au);
			}
		}
	}
	
	

	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
	
	
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put("appointment.organizer", listManager.getDynamicList(getCurrentUser(request),"appointment.organizer"));
		model.put("dynamicLists", dynamicLists);
		
	
		return super.addReferenceData(context, command, errors, model);
	}
	

	
	
}
