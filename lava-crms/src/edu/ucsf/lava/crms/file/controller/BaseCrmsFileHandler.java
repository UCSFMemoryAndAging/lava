package edu.ucsf.lava.crms.file.controller;

import static edu.ucsf.lava.crms.file.CrmsRepositoryStrategy.CRMS_REPOSITORY_ID;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.file.controller.BaseLavaFileComponentHandler;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.enrollment.model.Consent;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

/**
 * Base class for files that are linked to patients, projects, consents, visits or instruments, i.e.
 * PatientAttachmentHandler
 * EnrollmentAttachmentHandler
 * ConsentAttachmentHandler
 * VisitAttachmentHandler
 * AssessmentAttachmentHandler
 * 
 * This is for attaching a file to a specific entity. When this is done, only the id for Patient and
 * the specific entity are populated in CrmsFile. If the attachment is at the "level of Patient" then 
 * only the Patient id is populated and none of the other ids.
 * 
 * The UI for this is a jsp that is included at the end of a specific type of entity which lists all
 * attachments for that entity (an entity can have multiple attachments, i.e. a list). This secondary
 * entity list is supported by CrmsAttachmentsListHandler. 
 * 
 * This differs from the PatientAttachmentsHandler and ProjectPatientAttachmentsHandler which are 
 * standard primary list handlers and display all the attachments associated with a Patient across all
 * types of entities (i.e. enrollments, consents, visits and specific instruments) as opposed to 
 * CrmsAttachmentsListHandler which for a specific Patient only lists the attachments at the level
 * of Patient, i.e. not an enrollment, consent, visit or instrument attachments.
 */
public class BaseCrmsFileHandler extends BaseLavaFileComponentHandler {

	public BaseCrmsFileHandler() {
		super();
		this.setHandledEntity("crmsFile", CrmsFile.class);
		CrmsSessionUtils.setIsPatientContext(this);
	}

	/**
	 * Examine request parameters and configure the links of the new file object to patient, project, visit, and/or instrument.
	 */
	@Override
	protected Object initializeNewCommandInstance(RequestContext context, Object command) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsFile crmsFile = (CrmsFile)command;
		// critical to set the repo id 
		crmsFile.setRepositoryId(CRMS_REPOSITORY_ID);
		//Setup default values
		crmsFile.setFileStatusBy(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request).getShortUserNameRev());
		crmsFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));
		
		if (request.getParameterMap().containsKey("enrollStatId")){
			EnrollmentStatus es = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(request.getParameter("enrollStatId"))));
			setEnrollmentStatus(crmsFile,es,request);
			this.setPatient(crmsFile, es.getPatient(), request);
			crmsFile.setProjName(es.getProjName());
		}else if (request.getParameterMap().containsKey("consentId")){
			Consent consent = (Consent) Consent.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(request.getParameter("consentId"))));
			crmsFile.setConsentId(consent.getId());
			crmsFile.setConsent(consent);
			this.setPatient(crmsFile, consent.getPatient(), request);
			crmsFile.setProjName(consent.getProjName());
		}else if (request.getParameterMap().containsKey("visitId")){
			Visit v = (Visit)Visit.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("visitId"))));
			setVisit(crmsFile,v,request);
			this.setPatient(crmsFile, v.getPatient(), request);
			crmsFile.setProjName(v.getProjName());
		}else if (request.getParameterMap().containsKey("instrId")){
			InstrumentTracking instr = (InstrumentTracking)InstrumentTracking.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("instrId"))));
			setInstrument(crmsFile,instr,request);
			this.setPatient(crmsFile, instr.getPatient(), request);
			crmsFile.setProjName(instr.getProjName());
		}else if (request.getParameterMap().containsKey("pidn")){
			// note that this is an attachment at the level of Patient, meaning that all other entity id's are
			// null
			Patient p = (Patient)Patient.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("patientId"))));
			setPatient(crmsFile,p,request);
		}else{
			// can assume there is a current patient in context. otherwise there will be no Add attachement button
			// so would never get here
			Patient p = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
			setPatient(crmsFile,p,request);
			
			// do not set crmsFile.projName. user can optionally set a Project or not
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
		}
// NOT SURE HOW THESE ARE CALLED SINCE THIS METHOD IS ONLY CALLED WHEN THERE IS AN "id" REQUEST PARAM
		else if (request.getParameterMap().containsKey("enrollStatId")){
			EnrollmentStatus es = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(request.getParameter("enrollStatId"))));
			if(es!=null){
				return es.getPatient().getId();
			}
		}else if (request.getParameterMap().containsKey("consentId")){
			Consent consent = (Consent) Consent.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(request.getParameter("consentId"))));
			if(consent!=null){
				return consent.getPatient().getId();
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
		}else if (request.getParameterMap().containsKey("pidn")){
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
			crmsFile.setEnrollStatId(es.getId());
			crmsFile.setEnrollmentStatus(es);
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
		}
	}

	/**
	 * Set the linked instrument based on the instrument id, cacading and updating the visit, project, and patient links. 
	 * @param crmsFile
	 * @param instrId
	 * @param request
	 */
	protected void setInstrument(CrmsFile crmsFile, InstrumentTracking instr, HttpServletRequest request ){
		if(crmsFile!=null && instr!=null){
			crmsFile.setInstrId(instr.getId());
			crmsFile.setInstrumentTracking(instr);
		}
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

	 	//add 'lavaFile' and 'crmsFile' static lists. the component (aka handled entity) in the subclasses of this class
		//use different names so they can all share this common base class but present separate UI views. so 'crmsFile'
		//is not used and the lists must be loaded here explicitly
	 	this.addListsToModel(model, listManager.getStaticListsForEntity("lavaFile"));
	 	this.addListsToModel(model, listManager.getStaticListsForEntity("crmsFile"));

		Map<String,String> projList = listManager.getDynamicList(getCurrentUser(request),
				"enrollmentStatus.patientProjects",	"patientId", crmsFile.getPatient().getId(),  Long.class);
		projList = CrmsAuthUtils.filterProjectListByPermission(getCurrentUser(request),
				CoreSessionUtils.getCurrentAction(sessionManager,request), projList);
		dynamicLists.put("enrollmentStatus.patientProjects", projList);

		if (crmsFile.getProjName() != null) {
			dynamicLists.put("crmsFile.contentType", listManager.getDynamicList(getCurrentUser(request), 
					"crmsFile.projectContentType", "projectName",crmsFile.getProjName(),String.class));
		}
		else {
			dynamicLists.put("crmsFile.contentType", listManager.getDynamicList(getCurrentUser(request), "crmsFile.contentType"));	
		}

		
		model.put("dynamicLists", dynamicLists);

		return super.addReferenceData(context, command, errors, model);
	}
}
