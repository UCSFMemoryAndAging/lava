package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.crms.controller.CrmsEntityComponentHandler;
import edu.ucsf.lava.crms.people.model.Doctor;

public class DoctorHandler extends CrmsEntityComponentHandler {

	public DoctorHandler() {
		super();
		setHandledEntity("doctor", Doctor.class);
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {

		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		dynamicLists.put("doctor.city", 
				listManager.getDynamicList("doctor.city"));
		model.put("dynamicLists", dynamicLists);
		
		return super.addReferenceData(context, command, errors, model);
	}

}
