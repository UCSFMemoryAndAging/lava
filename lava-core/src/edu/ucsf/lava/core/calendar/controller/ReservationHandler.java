package edu.ucsf.lava.core.calendar.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
import edu.ucsf.lava.core.calendar.model.ResourceCalendar;
import edu.ucsf.lava.core.calendar.rules.AppointmentRule;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;

public class ReservationHandler extends AppointmentHandler {

	public static String APPOINTMENT_TYPE_RESERVATION = "Reservation";
	
	
	public ReservationHandler() {
		super();
		this.setHandledEntity("reservation", Appointment.class);
	}


	/**
	 * Set location of the appointment = to the location of the calendar resource and 
	 * set the appointment type
	 */
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		Appointment a = (Appointment)super.initializeNewCommandInstance(context, command);
		if(a.getCalendar()!=null && a.getCalendar().getType().equals(ResourceCalendar.CALENDAR_TYPE_RESOURCE)){
	
			a.setType(APPOINTMENT_TYPE_RESERVATION);
		}
		return a;
	}


	protected List<AppointmentRule> getRules(Appointment appointment) {
		return super.getRules(appointment);
	}
	
	
	
}
