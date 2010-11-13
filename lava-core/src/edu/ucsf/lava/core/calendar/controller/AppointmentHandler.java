package edu.ucsf.lava.core.calendar.controller;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.auth.model.AuthGroup;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.auth.model.AuthUserGroup;
import edu.ucsf.lava.core.auth.model.AuthUserRole;
import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.calendar.rules.AppointmentRule;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;

public class AppointmentHandler extends BaseEntityComponentHandler {

	public static String ROLE_CALENDAR_ADMIN = "CALENDAR ADMIN";
	public static String ROLE_SYSTEM_ADMIN = "SYSTEM ADMIN";
	
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
		if(request.getParameterMap().containsKey("selectedDate")){
			Date startDate = LavaDateUtils.getDate(request.getParameter("selectedDate"));
			a.setStartDate(startDate);
			if(startDate != null && request.getParameterMap().containsKey("selectedTime")){
				Time startTime = LavaDateUtils.getTime(request.getParameter("selectedTime"));
				if (startTime != null){
					a.setStartTime(startTime);
					Date endDateTime = LavaDateUtils.addTime(LavaDateUtils.getDateTime(startDate, startTime),a.getDefaultDuration());
					a.setEndDate(LavaDateUtils.getDatePart(endDateTime));
					a.setEndTime(LavaDateUtils.getTimePart(endDateTime));
				} else {
					a.setEndDate(startDate);
				}
			} else {
				a.setEndDate(startDate);
			}
		}
		//set the organizer to the current user. 
		a.setOrganizer(CoreSessionUtils.getCurrentUser(sessionManager, request));
		//TODO:  determine whether to auto add organizer to list of attendees as the organizer role.
		
		return command;
	}
	
	
	
	protected Event doSave(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment a = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		handleOrganizerChange(context,command, errors);
		if(this.hasRuleViolationErrors(context, command, errors)){
			a.setStatus(Appointment.STATUS_ERROR);
			return new Event(this,ERROR_FLOW_EVENT_ID);
			}
		a.setStatus(Appointment.STATUS_SCHEDULED);
		return super.doSave(context, command, errors);
	}
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment a = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		handleOrganizerChange(context,command, errors);
		if(this.hasRuleViolationErrors(context, command, errors)){
			a.setStatus(Appointment.STATUS_ERROR);
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}
		a.setStatus(Appointment.STATUS_SCHEDULED);
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
	
	/**
	 * Check all appointment rules and convert any errors into command errors. 
	 * @param context
	 * @param command
	 * @param errors
	 * @return 
	 * @throws Exception
	 */
	protected boolean hasRuleViolationErrors(RequestContext context, Object command, BindingResult errors)throws Exception{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Appointment a = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Map<String,Object[]> violations = new LinkedHashMap<String,Object[]>();
		List<AppointmentRule> rules = getRules(a);
		for(AppointmentRule rule : rules){
			rule.isViolatedBy(a, violations);
		}
		//If any rules returned violation errors, then convert errors into command errors and return error. 
		if(!violations.isEmpty()){
			
			for(Entry<String,Object[]> violation : violations.entrySet()){
				LavaComponentFormAction.createCommandError(errors,violation.getKey(),violation.getValue());
			}
			return true;
		}
		return false;
	}

	/**
	 * Override this in subclasses to provide appointment rules.
	 * @return
	 */
	protected List<AppointmentRule> getRules(Appointment appointment){
		return new ArrayList<AppointmentRule>();
	}
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		//load up dynamic lists
	
	
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put("appointment.organizer", listManager.getDynamicList(getCurrentUser(request),"appointment.organizer"));
		model.put("dynamicLists", dynamicLists);
		
	
		return super.addReferenceData(context, command, errors, model);
	}

	@Override
	protected Event handleCustomEvent(RequestContext context, Object command,
			BindingResult errors) throws Exception {
	
		String event = ActionUtils.getEventName(context);
		if (event.equalsIgnoreCase("custom")){
			// cancel appointment
			Appointment a = (Appointment)((ComponentCommand)command).getComponents().get(getDefaultObjectName());

			// determine whether user has admin privileges
			AuthUser user = a.getOrganizer();
			boolean isCalendarAdmin = false;
			if(user != null){
				for (AuthUserRole role : user.getEffectiveRoles()){
					String roleName = role.getRole().getRoleName();
					if(roleName.equals(ROLE_CALENDAR_ADMIN) || roleName.equals(ROLE_SYSTEM_ADMIN)){
						isCalendarAdmin = true;
					}
				}
			}
			
			// check to make sure appointment does not occur in the past (OK for admin roles)
			if (!isCalendarAdmin){
				Date currentDate = new Date();
				if (a.getDateRange().getStart().before(currentDate)){
					errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"error.appointmentCancel.occursInPast"}, null, ""));
					return new Event(this,this.ERROR_FLOW_EVENT_ID);
				}
			}
		
			a.cancelAppointment();
			a.save();
			errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"info.appointmentCancel.success"}, null, ""));
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
		
	
}
