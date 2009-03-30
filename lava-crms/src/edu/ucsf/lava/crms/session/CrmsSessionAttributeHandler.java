package edu.ucsf.lava.crms.session;

import static edu.ucsf.lava.core.session.CoreSessionUtils.CURRENT_USER;
import static edu.ucsf.lava.crms.session.CrmsSessionUtils.CRMS_SCOPE;
import static edu.ucsf.lava.crms.session.CrmsSessionUtils.CURRENT_INSTRUMENT;
import static edu.ucsf.lava.crms.session.CrmsSessionUtils.CURRENT_PATIENT;
import static edu.ucsf.lava.crms.session.CrmsSessionUtils.CURRENT_PROJECT;
import static edu.ucsf.lava.crms.session.CrmsSessionUtils.CURRENT_VISIT;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.scope.AbstractScopeSessionAttributeHandler;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.project.model.Project;
import edu.ucsf.lava.crms.scheduling.model.Visit;
public class CrmsSessionAttributeHandler extends AbstractScopeSessionAttributeHandler {

	protected ProjectManager projectManager;
	
	public CrmsSessionAttributeHandler(){
		super();
		this.setOrder(new Long(250));
		this.setHandledScope(CRMS_SCOPE);
		this.addHandledAttribute(CURRENT_PATIENT);
		this.addHandledAttribute(CURRENT_VISIT);
		this.addHandledAttribute(CURRENT_PROJECT);
		this.addHandledAttribute(CURRENT_INSTRUMENT);
		
	}

	/**
	 * handle setting the session attributes.  Call appropriate handler method based on the 
	 * attribute value (or call the abstract superclass handler if attribute is not one of
	 * the custom handled attributes).
	 */
	public Object handleSetAttribute(HttpServletRequest request, String attribute, Object value) {
		if(attribute==null){return value;}
		
		if(attribute.equals(CURRENT_PATIENT)){
			return handleSetCurrentPatient(request,value);
		}else if(attribute.equals(CURRENT_VISIT)){
			return handleSetCurrentVisit(request,value);
		}else if(attribute.equals(CURRENT_PROJECT)){
			return handleSetCurrentProject(request,value);
		}else if(attribute.equals(CURRENT_INSTRUMENT)){
			return handleSetCurrentInstrument(request,value);
		}else{
			return super.handleSetAttribute(request, attribute, value);
		}
	}
	
	/**
	 * Handle setting the current patient.  If we need to clear the current patient,
	 * or the new current patient is a mismatch for the current visit, then clear the current
	 * visit as well.  
	 * 
	 * @param request
	 * @param value
	 * @return
	 */
	public Object handleSetCurrentPatient(HttpServletRequest request, Object value) {
		
		//get the value that we are going to set in the session for current patient
		Patient patient = null;
		if(value!=null){
			//if the value is an id, the get the patient
			if(Long.class.isAssignableFrom(value.getClass())){
				try{
					patient = (Patient)Patient.MANAGER.getById((Long)value, 
							Patient.MANAGER.newFilterInstance((AuthUser)this.attributeHandlers.getAttribute(request, CURRENT_USER)));
				}catch(Exception e){
					logger.error("Unable to find Patient using id " + value.toString() + " when setting current patient.", e);
				}
			}
			//if the value is a patient, then just use it
			else if(Patient.class.isAssignableFrom(value.getClass())){
				patient = (Patient)value;
			}
		}
			
		this.setSessionAttribute(request, CURRENT_PATIENT, patient);
		
		/* if we get here, check to see if there is a current visit, 
		 * if so and it does not match our patient (or if patient is null) then clear the visit
		 */
		Visit visit = (Visit) attributeHandlers.getAttribute(request, CURRENT_VISIT);
		if (visit != null && ((patient==null) || !visit.getPatient().getId().equals(patient.getId()))) {
				clearSessionAttribute(request,CURRENT_VISIT);
		}
		return patient;
		
	}
	

	/**
	 * Handle setting the current visit.  If we need to clear the current visit,
	 * or the new current visit is a mismatch for the current instrument, then clear the current
	 * instrument as well.  
	 * 
	 * @param request
	 * @param value
	 * @return
	 */
	
	public Object handleSetCurrentVisit(HttpServletRequest request, Object value) {
		//get the value that we are going to set in the session for current visit
		Visit visit = null;
		if(value!=null){
			//if the value is an id, the get the visit
			if(Long.class.isAssignableFrom(value.getClass())){
				try{
					visit = (Visit)visit.MANAGER.getById((Long)value, 
							Patient.MANAGER.newFilterInstance((AuthUser)this.attributeHandlers.getAttribute(request, CURRENT_USER)));
				}catch(Exception e){
					logger.error("Unable to find Visit using id " + value.toString() + " when setting current visit.", e);
				}
			}
			//if the value is a visit, then just use it
			else if(Visit.class.isAssignableFrom(value.getClass())){
				visit = (Visit)value;
			}
		}
		
		//set the attribute whether visit == null or an actual visit entity
		this.setSessionAttribute(request, CURRENT_VISIT, visit);
		
		//if visit is not null, make sure we set the current patient
		if(visit!=null){
			//call back out to the attributeHandlers collection, so other handlers have a shot at reacting to the curren patient change 
			attributeHandlers.setAttribute(request, CURRENT_PATIENT, visit.getPatient());
		}
		
		/* if we get here, check to see if there is a current instrument, 
		 * if so and it does not match our visit (or if visit is null) then clear the instrument
		 */
		Instrument instrument  = (Instrument) attributeHandlers.getAttribute(request, CURRENT_INSTRUMENT);
		if (instrument != null && ((visit==null) || !instrument.getVisit().getId().equals(visit.getId()))) {
				clearSessionAttribute(request,CURRENT_INSTRUMENT);
		}
		return visit;
	}
		
		
	
	public Object handleSetCurrentProject(HttpServletRequest request, Object value) {
		Project project = null;
		if(value!=null){
			//if value is a string, then get the project using the string as the project name
			if(String.class.isAssignableFrom(value.getClass())){
					project = getProjectManager().getProject((String)value);
			}
			//if the value is a Project then just use it
			else if(Project.class.isAssignableFrom(value.getClass())){
				project = (Project)value;
			}
		}
		
		setSessionAttribute(request,CURRENT_PROJECT,project);
	    return project;
	}
	
	
	public Object handleSetCurrentInstrument(HttpServletRequest request, Object value) {
		Instrument instrument = null;
		if(value != null){
			//is value is an id, use it to get the instrument
			if(Long.class.isAssignableFrom(value.getClass())){
				instrument = (InstrumentTracking)InstrumentTracking.MANAGER.getById((Long)value, 
						InstrumentTracking.MANAGER.newFilterInstance((AuthUser)this.attributeHandlers.getAttribute(request, CURRENT_USER)));
			}
			//if value is an instrument then just use it
			else if(Instrument.class.isAssignableFrom(value.getClass())){
				instrument = (Instrument)value;
			}
		}
		//set whether it is an instrument instance or null
		this.setSessionAttribute(request, CURRENT_INSTRUMENT, instrument);
		
		//if we have an instrument, make sure the current visit and patient are set appropriately
		if(instrument!=null){
			attributeHandlers.setAttribute(request, CURRENT_VISIT, instrument.getVisit());
			attributeHandlers.setAttribute(request, CURRENT_PATIENT, instrument.getPatient());
		}
		return instrument;
	}
			
	protected ProjectManager getProjectManager(){
		if(projectManager!=null){return projectManager;}
		projectManager = CrmsManagerUtils.getProjectManager();
		return projectManager;
	}
			
	
	
	
}
