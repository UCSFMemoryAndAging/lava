package edu.ucsf.lava.crms.action.model;

import edu.ucsf.lava.core.action.model.BaseAction;

public class CrmsAction extends BaseAction {

	private boolean patientHomeDefault = false;
	private boolean patientModuleDefault = false;
	private boolean patientSectionDefault = false;
	
	public CrmsAction(){
	super();
	}
	
	public boolean getPatientHomeDefault() {
		return patientHomeDefault;
	}

	public void setPatientHomeDefault(boolean patientHomeDefault) {
		this.patientHomeDefault = patientHomeDefault;
	}

	public boolean getPatientModuleDefault() {
		return patientModuleDefault;
	}

	public void setPatientModuleDefault(boolean patientModuleDefault) {
		this.patientModuleDefault = patientModuleDefault;
	}

	public boolean getPatientSectionDefault() {
		return patientSectionDefault;
	}

	public void setPatientSectionDefault(boolean patientSectionDefault) {
		this.patientSectionDefault = patientSectionDefault;
	}

}
