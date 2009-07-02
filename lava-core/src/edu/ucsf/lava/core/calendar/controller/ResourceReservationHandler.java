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

public class ResourceReservationHandler extends BaseEntityComponentHandler {

	
	
	public ResourceReservationHandler() {
		super();
		this.setHandledEntity("resourceReservation", Appointment.class);
		this.setRequiredFields(new String[]{
					"calendarId",
					"organizerId",
					"reservedForId",
					"startDate",
					"startTime",
					"endDate",
					"endTime"});
		}



	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment rr = (Appointment)command;
		if(request.getParameterMap().containsKey("resourceCalendarId")){
			String calendarId = request.getParameter("resourceCalendarId");
			if(StringUtils.isNumeric(calendarId)){
				Calendar rc = (Calendar)Calendar.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(calendarId)));
				if(rc != null){
					rr.setCalendar(rc);
				}
			}
		}
		//set the organizer to the current user. 
		rr.setOrganizer(CoreSessionUtils.getCurrentUser(sessionManager, request));
		//TODO:  determine whether to auto add organizer to list of attendees as the organizer role.
		
		return command;
	}
	
	
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleOwnerChange(context,command, errors);
		handleCalendarChange(context,command, errors);
		return super.doSave(context, command, errors);
	}
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleOwnerChange(context,command, errors);
		handleCalendarChange(context,command, errors);
		return super.doSaveAdd(context, command, errors);
	}

	protected Event doReRender(RequestContext context, Object command, BindingResult errors) throws Exception {
		handleOwnerChange(context,command, errors);
			handleCalendarChange(context,command, errors);
		return super.doReRender(context, command, errors);
	}
	
	
	protected void handleOwnerChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment rr = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(rr.getOrganizerId(),rr.getOrganizer())){
			if(rr.getOrganizerId()==null){
				rr.setOrganizer(null); 	//clear the association
			}else{
				AuthUser au = (AuthUser)AuthUser.MANAGER.getOne(this.getFilterWithId(request,rr.getOrganizerId()));
				rr.setOrganizer(au);
			}
		}
	}
	
	

	protected void handleCalendarChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment rr = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(rr.getCalendarId(),rr.getCalendar())){
			if(rr.getCalendarId()==null){
				rr.setCalendar(null); 	//clear the association
			}else{
				Calendar rc = (Calendar)Calendar.MANAGER.getOne(this.getFilterWithId(request,rr.getCalendarId()));
				rr.setCalendar(rc);
			}
		}
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
	
	
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		Appointment rr = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		dynamicLists.put("resourceReservation.resourceCalendars", listManager.getDynamicList(getCurrentUser(request),"resourceReservation.resourceCalendars"));
		dynamicLists.put("resourceReservation.resourceUsers", listManager.getDynamicList(getCurrentUser(request),"resourceReservation.resourceUsers"));
		model.put("dynamicLists", dynamicLists);
		
	
		return super.addReferenceData(context, command, errors, model);
	}
	

	
	
}
