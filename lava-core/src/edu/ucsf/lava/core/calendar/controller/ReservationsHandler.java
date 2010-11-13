package edu.ucsf.lava.core.calendar.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.calendar.model.Calendar;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;

import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaCustomDateEditor;

public class ReservationsHandler extends AppointmentsHandler {

	public ReservationsHandler() {
		super();
		this.setHandledList("reservations","appointment");
		this.addListedEntityName("reservation");
	}
	
}
