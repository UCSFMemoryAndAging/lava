package edu.ucsf.lava.crms.ui.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.list.model.LabelValueBean;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.controller.CrmsContextChangeComponentHandler;
import edu.ucsf.lava.crms.dao.CrmsDaoFilterUtils;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.model.Project;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientContextHandler extends CrmsContextChangeComponentHandler {
	
	public PatientContextHandler(){
		super();
		Map<String,Class> handledObject = new HashMap<String,Class>();
		handledObject.put("patientContext", PatientContextFilter.class);
		this.setHandledObjects(handledObject);	
		}
		
	
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map backingObject = new HashMap<String,Object>();
		PatientContextFilter filter = new PatientContextFilter();
		filter.setPatient(CrmsSessionUtils.getCurrentPatient(sessionManager,request));
		filter.setSearchBy("NameRev");
		backingObject.put("patientContext",filter);
		return backingObject;
	}

		//Set current patient if there is one...
	public Event postEventHandled(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		PatientContextFilter filter = ((PatientContextFilter)((ComponentCommand)command).getComponents().get("patientContext"));
		Patient currentPatient = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		if(currentPatient!= null && (filter.getPatient()==null || !filter.getPatient().getId().equals(currentPatient.getId()))){
			filter.setPatient(CrmsSessionUtils.getCurrentPatient(sessionManager,request));
		}
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		PatientContextFilter filter = ((PatientContextFilter)((ComponentCommand)command).getComponents().get("patientContext"));
		List<LabelValueBean> results = filter.getSearchResults();
		if(results !=null){
			Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		    // convert search results into standard list data structure
		    Map<String,String> resultsMap = new LinkedHashMap<String,String>();
		    resultsMap.put("","");
		    for (LabelValueBean lvb : results) {
		    	resultsMap.put(lvb.getValue(), lvb.getLabel());
		    }
		    dynamicLists.put("context.patientResults",resultsMap);
		    model.put("dynamicLists", dynamicLists);
		}
		return super.addReferenceData(context, command, errors, model);
	}
	
	protected void doContextChange(RequestContext context, Object command, BindingResult errors) throws Exception {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		List results = null; //the contents of the currentpatient search field
		String view = null; //the view to return to
		Long patientId = null;
		Patient patient = null;
		
		//get current context objects
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager,request);
		Project currProject = CrmsSessionUtils.getCurrentProject(sessionManager,request);
		Patient currPatient = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		PatientContextFilter filter = ((PatientContextFilter)((ComponentCommand)command).getComponents().get("patientContext"));
		//clear the results list
		filter.setSearchResults(null);
		
		//Only filter if a value was supplied and it isn't the current patient's unique id
		if ((StringUtils.isNotEmpty(filter.getPatientSearch()))&&
			(currPatient == null || !filter.getPatientSearch().equalsIgnoreCase(currPatient.getId().toString()))){
			
			//prepare Dao Filter
			LavaDaoFilter daoFilter = Patient.newFilterInstance(getCurrentUser(request));
			if (currProject != null) {
				CrmsDaoFilterUtils.setProjectContext(daoFilter,currProject);
			}

			//If submitted value is a numeric attempt to retrieve the patient by ID or by deidentified subject's id
			try{
				patientId = Long.valueOf(filter.getPatientSearch());
			}
			catch(Exception e){ //just continue
			}
			if (patientId != null)
			{
				daoFilter.addDaoParam(daoFilter.daoOr(
												daoFilter.daoAnd(daoFilter.daoEqualityParam("lastName", Patient.DEIDENTIFIED), 
																daoFilter.daoEqualityParam("firstName", patientId.toString())),
									  daoFilter.daoEqualityParam("id",patientId)));
				
				patient = (Patient) Patient.MANAGER.getOne(daoFilter);
				//if we found a patient then use it.
				if(patient != null){
					CrmsSessionUtils.setCurrentPatient(sessionManager,request,patient);
					filter.setPatient(patient);
					currPatient = patient;
					setDoDefaultActionFlag(context);
				}else{  //numeric submmitted did not return a patient
					filter.setPatientSearch("");
					// add informational message
					errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"info.patientSearch.idNotFound"}, null, ""));
				}
			}else{ //the value submitted was not a numeric id
				daoFilter.clearDaoParams();
				daoFilter.addDaoParam( daoFilter.daoNamedParam("search",filter.getPatientSearch()));
			
				//first look for patient....if we find it, use it, otherwise return a new list of patients to the search box
				results = Patient.doPatientSearch(filter.getSearchBy(),daoFilter);
				
				//if only one patient returned by search then use it
				if(results.size()==1){
					LabelValueBean result = (LabelValueBean)results.get(0);
					CrmsSessionUtils.setCurrentPatient(sessionManager,request,Long.valueOf(result.getValue()));
					currPatient = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
					filter.setPatient(currPatient);
					// set flag in request scope to indicate to flow that patient has been
					// changed (as opposed to displaying a search results list or no match)
					setDoDefaultActionFlag(context);
				}else if (results.size()==0){ 
					// 0 results found
					filter.setPatientSearch("");
					// add informational message
					errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"info.patientSearch.nameNotFound"}, null, ""));
				}else{
					//more than one result found 
					filter.setPatientSearch("");
					filter.setSearchResults(results);
			//		clearDoDefaultActionFlag(context);
				}
			}
		}
		//if there is not a list of search results, but there is a current patient (either
		//pre-existing or there was a single match), set the current patient as the only
		//result in results for the autocomplete control, and setPatient also sets the 
		//patientSearch to the patient id, so the autocomplete shows the patient name as
		//the selected option
		if (filter.getSearchResults() == null && currPatient != null){
			filter.setPatient(currPatient);
		}
		//if there is a result list, do not want to set patient in the filter yet because
		//the search is in process; the user is in the process of selecting 
	}
	
	protected void doContextClear(RequestContext context, Object command, BindingResult errors) throws Exception {
		//get current context objects
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action currentAction = CoreSessionUtils.getCurrentAction(sessionManager,request);
		Project currProject = CrmsSessionUtils.getCurrentProject(sessionManager,request);
		Patient currPatient = CrmsSessionUtils.getCurrentPatient(sessionManager,request);
		PatientContextFilter filter = ((PatientContextFilter)((ComponentCommand)command).getComponents().get("patientContext"));
		if(currPatient != null){
				//clear out the last patient search results
			CrmsSessionUtils.setCurrentPatient(sessionManager,request,(Patient)null);
				filter.setPatient((Patient)null);
				filter.setSearchResults(null);
				setDoDefaultActionFlag(context);
		}
	}

	public void initBinder(RequestContext context, Object command, DataBinder binder) {
	}

}



