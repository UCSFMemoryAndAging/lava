package edu.ucsf.lava.crms.file.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.file.controller.BaseLavaFileComponentHandler;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.people.model.ContactInfo;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

/**
 * Base class for files that are linked to patients, projects, visits or instruments. 
 * @author jhesse
 *
 */
public class BaseCrmsFileHandler extends BaseLavaFileComponentHandler {

	public BaseCrmsFileHandler() {
		super();
		this.setHandledEntity("crmsFile", CrmsFile.class);
		CrmsSessionUtils.setIsPatientContext(this);
		//Every file must be linked to a patient and project. Enforcing that the enrollStatId
		//has a value accomplishes this. 
		this.extendRequiredFields(new String[]{"enrollStatId"});
		
	}

	/**
	 * Examine request parameters and configure the links of the new file object to patient, project, visit, and/or instrument.
	 */
	@Override
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsFile crmsFile = (CrmsFile)command;
		//Setup default values
		crmsFile.setFileStatusBy(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request).getShortUserNameRev());
		crmsFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));
		
		//setup the linking data depending on the parameter supplied. 
		if (request.getParameterMap().containsKey("enrollStatId")){
			EnrollmentStatus es = getEnrollmentStatus(Long.valueOf(request.getParameter("enrollStatId")), request);
			setEnrollmentStatus(crmsFile,es,request);
		}else if (request.getParameterMap().containsKey("visitId")){
			Visit v = (Visit)Visit.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("visitId"))));
			setVisit(crmsFile,v,request);
		}else if (request.getParameterMap().containsKey("instrId")){
			setInstrument(crmsFile,Long.valueOf(request.getParameter("instrId")),request);
		}else if (request.getParameterMap().containsKey("patientId")){
			Patient p = (Patient)Patient.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("patientId"))));
			setPatient(crmsFile,p,request);
		}else{
			//no parameter supplied, so make assumptions based on current context
			Instrument i = CrmsSessionUtils.getCurrentInstrument(sessionManager, request);
			Visit v = CrmsSessionUtils.getCurrentVisit(sessionManager, request);
			Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
			
			if(i!=null){
				setInstrument(crmsFile,i.getId(),request);
			}else if(v!=null){
				setVisit(crmsFile,v,request);
			}else if(p!=null){
				setPatient(crmsFile,p,request);
			}

		}
			
		// check for property values in parameters list of action
		return command;
	}
	
	
	
	
	@Override
	protected Long getContextIdFromRequest(RequestContext context) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		if (request.getParameterMap().containsKey("id")){
			CrmsFile crmsFile = (CrmsFile)CrmsFile.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("id"))));
			if(crmsFile != null){
				return crmsFile.getPidn();
			}
		}else if (request.getParameterMap().containsKey("enrollStatId")){
			EnrollmentStatus es = getEnrollmentStatus(Long.valueOf(request.getParameter("enrollStatId")), request);
			if(es!=null){
				return es.getPatient().getId();
			}
		}else if (request.getParameterMap().containsKey("visitId")){
			Visit v = (Visit)Visit.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("visitId"))));
			if(v!=null){
				return v.getPatient().getId();
			}
		}else if (request.getParameterMap().containsKey("instrId")){
			InstrumentTracking it = (InstrumentTracking)InstrumentTracking.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("instrId"))));
			if(it!=null){
				return it.getPatient().getId();
			}
		}else if (request.getParameterMap().containsKey("patientId")){
			Patient p = (Patient)Patient.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("patientId"))));
			if(p!=null){
				return p.getId();
			}
		}
		
		//if we get here we have no context so assume the current patient
		Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		if(p!=null){
			return p.getId();
		}else{
			return null;
		}
		
	}

	/**
	 * Set the linked patient.
	 * @param crmsFile
	 * @param p
	 * @param request
	 */
	protected void setPatient(CrmsFile crmsFile, Patient p,HttpServletRequest request ){
		if(crmsFile!=null && p!=null){
			crmsFile.setPidn(p.getId());
			crmsFile.setPatient(p);
		}
	}

	/**
	 * Set the linked project cascading and updating the patient link.
	 * @param crmsFile
	 * @param es
	 * @param request
	 */
	protected void setEnrollmentStatus(CrmsFile crmsFile, EnrollmentStatus es, HttpServletRequest request ){
		if(crmsFile!=null && es!=null){
			crmsFile.setEnrollmentStatus(es);
			crmsFile.setEnrollStatId(es.getId());
			this.setPatient(crmsFile, es.getPatient(), request);
		}

	}

	/**
	 * Set the linked visit, cascading and updating the project and patient links.
	 * @param crmsFile
	 * @param v
	 * @param request
	 */
	protected void setVisit(CrmsFile crmsFile, Visit v, HttpServletRequest request ){
		if(crmsFile!=null && v!=null){
			crmsFile.setVisitId(v.getId());
			crmsFile.setVisit(v);
			EnrollmentStatus es = this.getEnrollmentStatus(crmsFile.getPatient(), crmsFile.getVisit().getProjName(), request);
			if(es!=null){
				this.setEnrollmentStatus(crmsFile, es, request);
			}else{
				//this shouldn't happen but it is easy enough to trap
				this.setPatient(crmsFile, v.getPatient(), request);
			}
		}
	}

	/**
	 * Set the linked instrument based on the instrument id, cacading and updating the visit, project, and patient links. 
	 * @param crmsFile
	 * @param instrId
	 * @param request
	 */
	protected void setInstrument(CrmsFile crmsFile, Long instrId, HttpServletRequest request ){
		if(crmsFile!=null && instrId!=null){

			crmsFile.setInstrId(instrId);
			InstrumentTracking it = (InstrumentTracking)InstrumentTracking.MANAGER
			.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("instrId"))));
			if(it!=null){
				crmsFile.setInstrumentTracking(it);
				this.setVisit(crmsFile, it.getVisit(), request);
			}else{
				//this shouldn't happen, but we should at least associated with the current patient
				Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
				setPatient(crmsFile,p,request);
			}
		}
	}

	/**
	 * Utility method to get the enrollment status when we know the patient and project.
	 * @param p
	 * @param projName
	 * @param request
	 * @return
	 */
	protected EnrollmentStatus getEnrollmentStatus(Patient p, String projName,HttpServletRequest request){
		LavaDaoFilter filter = EnrollmentStatus.MANAGER.newFilterInstance(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request));
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", p.getId()));
		//TODO: refactor this to make sure it works with Project [unit] combinations
		filter.addDaoParam(filter.daoEqualityParam("projName",projName));
		EnrollmentStatus es = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(filter);
		return es;
	}
	/**
	 * Utility method to get the enrollment status when we have the id.
	 * @param enrollStatId
	 * @param request
	 * @return
	 */
	protected EnrollmentStatus getEnrollmentStatus(Long enrollStatId,HttpServletRequest request){
		EnrollmentStatus es = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(request.getParameter("enrollStatId"))));
		return es;
	}


	/**
	 * Configure the dynamic lists of linked / linkable objects.
	 */
	@Override
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		//	load up dynamic lists
		CrmsFile crmsFile = (CrmsFile) ((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();


		if(crmsFile.getPatient()==null){
			StateDefinition state = context.getCurrentState();
			if ((flowMode.equals("edit") && state.getId().equals("edit"))) {
				dynamicLists.put("crmsFile.patients", listManager.getDynamicList(getCurrentUser(request),"patient.allPatients"));
			}

		}else {
			//always setup project list
			dynamicLists.put("crmsFile.projects", listManager.getDynamicList(getCurrentUser(request), 
					"crmsFile.projects","patientId",crmsFile.getPidn(),Long.class));

			//setup visit list if project is defined
			if(crmsFile.getEnrollmentStatus()!=null){
				dynamicLists.put("crmsFile.visits", listManager.getDynamicList( getCurrentUser(request), 
						"crmsFile.visits",new String[]{"patientId","projectName"},
						new Object[]{crmsFile.getPidn(),crmsFile.getEnrollmentStatus().getProjName()},
						new Class[]{Long.class,String.class}));
				
				dynamicLists.put("crmsFile.contentType", listManager.getDynamicList(getCurrentUser(request), 
						"crmsFile.projectContentType", "projectName",crmsFile.getEnrollmentStatus().getProjName(),String.class));
			}else{
				dynamicLists.put("crmsFile.visits",  new HashMap<String, String>());
				dynamicLists.put("crmsFile.contentType", listManager.getDynamicList(getCurrentUser(request), "crmsFile.contentType"));	
			}

			//setup instrument list based on visit and or project definition
			if(crmsFile.getVisit()!=null){
				dynamicLists.put("crmsFile.instruments", listManager.getDynamicList(getCurrentUser(request), 
						"crmsFile.instruments","visitId",crmsFile.getVisitId(),Long.class));

			}else {
				dynamicLists.put("crmsFile.instruments",  new HashMap<String, String>());
			}
		}
		model.put("dynamicLists", dynamicLists);

		return super.addReferenceData(context, command, errors, model);
	}

	/**
	 * Override to handle linked object changes
	 */
	@Override
	public Event handleReRenderEvent(RequestContext context, Object command,
			BindingResult errors) throws Exception {
		this.handleEntityLinkChanges(context,command,errors);
		return super.handleReRenderEvent(context, command, errors);
	}

	/**
	 * Override to handle linked object changes
	 */
	@Override
	public Event handleSaveAddEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		this.handleEntityLinkChanges(context,command,errors);
		return super.handleSaveAddEvent(context, command, errors);
	}

	/**
	 * Override to handle linked object changes
	 */
	@Override
	protected Event doSave(RequestContext context, Object command,
			BindingResult errors) throws Exception {
		this.handleEntityLinkChanges(context,command,errors);
		return super.doSave(context, command, errors);
	}


	/**
	 * Utility method to gather methods that check for changes to linked objects from the UI
	 * 
	 * @param context
	 * @param command
	 * @param errors
	 * @throws Exception
	 */
	protected void handleEntityLinkChanges(RequestContext context, Object command, BindingResult errors) throws Exception{
		this.handlePatientChange(context, command, errors);
		this.handleEnrollmentStatusChange(context, command, errors);
		this.handleVisitChange(context, command, errors);
		this.handleInstrumentChange(context, command, errors);
	}

	/**
	 * Handle UI changes to the project linked to the file.
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handleEnrollmentStatusChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsFile c = (CrmsFile)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(c.getEnrollStatId(),c.getEnrollmentStatus())){
			if(c.getEnrollStatId()==null){
				c.setEnrollmentStatus(null); 	//clear the association
			}else{
				EnrollmentStatus es = (EnrollmentStatus)EnrollmentStatus.MANAGER.getOne(this.getFilterWithId(request, c.getEnrollStatId()));
				c.setEnrollmentStatus(es);
			}
		}
	}

	/**
	 * Handle UI changes to the visit linked to the file
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handleVisitChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsFile c = (CrmsFile)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(c.getVisitId(),c.getVisit())){
			if(c.getVisitId()==null){
				c.setVisit(null); 	//clear the association
			}else{
				Visit v = (Visit)Visit.MANAGER.getOne(this.getFilterWithId(request, c.getVisitId()));
				c.setVisit(v);
			}
		}
	}
	/**
	 * Handle UI changes to the patient linked to the file.
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handlePatientChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsFile c = (CrmsFile)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(c.getPidn(),c.getPatient())){
			if(c.getPidn()==null){
				c.setPatient(null); 	//clear the association
			}else{
				Patient pat = (Patient)Patient.MANAGER.getOne(this.getFilterWithId(request, c.getPidn()));
				c.setPatient(pat);
			}
		}
	}

	/**
	 * Handle UI changes to the instrument linked to the file
	 * @param context
	 * @param command
	 * @param errors
	 */
	protected void handleInstrumentChange(RequestContext context, Object command, BindingResult errors){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsFile c = (CrmsFile)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if(doesIdDifferFromEntityId(c.getInstrId(),c.getInstrumentTracking())){
			if(c.getInstrId()==null){
				c.setInstrumentTracking(null); 	//clear the association
			}else{
				InstrumentTracking it = (InstrumentTracking) InstrumentTracking.MANAGER.getOne(this.getFilterWithId(request, c.getInstrId()));
				c.setInstrumentTracking(it);
			}
		}
	}
}
