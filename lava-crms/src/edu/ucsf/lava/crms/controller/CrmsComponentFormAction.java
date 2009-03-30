package edu.ucsf.lava.crms.controller;

import java.util.ArrayList;

import edu.ucsf.lava.core.controller.ComponentHandler;
import edu.ucsf.lava.core.controller.LavaComponentFormAction;
import edu.ucsf.lava.crms.ui.controller.PatientContextHandler;
import edu.ucsf.lava.crms.ui.controller.ProjectContextHandler;


public class CrmsComponentFormAction extends LavaComponentFormAction {

	protected PatientContextHandler patientContextHandler;
	protected ProjectContextHandler projectContextHandler;
		
	
	

	public CrmsComponentFormAction() {
		super();
		}

	public CrmsComponentFormAction(ArrayList<ComponentHandler> handlers) {
		super(handlers);
	}

	public CrmsComponentFormAction(ComponentHandler handler) {
		super(handler);
	}

	public PatientContextHandler getPatientContextHandler() {
		return patientContextHandler;
	}

	public void setPatientContextHandler(PatientContextHandler patientContextHandler) {
		this.patientContextHandler = patientContextHandler;
		componentHandlers.add(patientContextHandler);
	}

	public ProjectContextHandler getProjectContextHandler() {
		return projectContextHandler;
	}

	public void setProjectContextHandler(ProjectContextHandler projectContextHandler) {
		this.projectContextHandler = projectContextHandler;
		
		componentHandlers.add(projectContextHandler);
	}


}
