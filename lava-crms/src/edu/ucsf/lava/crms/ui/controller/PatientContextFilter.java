package edu.ucsf.lava.crms.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.ucsf.lava.core.list.model.LabelValueBean;
import edu.ucsf.lava.crms.people.model.Patient;

public class PatientContextFilter implements Serializable {

	protected String patientSearch;
	protected Patient patient;
	protected String searchBy;
	protected List<LabelValueBean> searchResults;
	
	
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		if(patient!=null){
			this.patient = patient;
			this.setPatientSearch(patient.getId().toString());
			LabelValueBean result = new LabelValueBean(patient.getFullNameRevWithId(),patient.getId());
			List results = new ArrayList<LabelValueBean>();
			results.add(result);
			this.setSearchResults(results);
		}else{
			this.setPatientSearch("");
		}
		
	}
	
	public String getPatientSearch() {
		return patientSearch;
	}
	public void setPatientSearch(String patientSearch) {
		this.patientSearch = patientSearch;
	}
	
	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
	public List<LabelValueBean> getSearchResults() {
		return searchResults;
	}
	public void setSearchResults(List<LabelValueBean> searchResults) {
		this.searchResults = searchResults;
	}
	
}
