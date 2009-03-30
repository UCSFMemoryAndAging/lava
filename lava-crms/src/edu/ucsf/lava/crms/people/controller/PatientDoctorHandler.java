package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Doctor;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.people.model.PatientDoctor;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

// CRUD handler for the Patient-Doctor relationship

public class PatientDoctorHandler extends CrmsEntityComponentHandler {

	public PatientDoctorHandler() {
		super();
		setHandledEntity("patientDoctor", PatientDoctor.class);
	}
	
	protected Object initializeNewCommandInstance(RequestContext context,Object command){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		// init currentPatient. currentPatient should not be null, as user is not given option of
		// adding doctor to a patient if no patient is selected 
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		((PatientDoctor)command).setPatient(p);
		
		// create a blank doctor so there is something to bind to
		((PatientDoctor)command).setDoctor((Doctor)Doctor.MANAGER.create());
		return command;
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put("doctor.allDoctors", 
				listManager.getDynamicList("doctor.allDoctors"));
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}

	
	//explicitly get and associated the doctor object from the database that has been selected by the user. 
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		PatientDoctor pd = (PatientDoctor)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		Doctor d = (Doctor)Doctor.MANAGER.getOne(Doctor.newFilterInstance().addIdDaoEqualityParam(pd.getDoctor().getId()));
		if(d != null){
			pd.setDoctor(d);
		}
		return super.doSaveAdd(context, command, errors);
	}

}
