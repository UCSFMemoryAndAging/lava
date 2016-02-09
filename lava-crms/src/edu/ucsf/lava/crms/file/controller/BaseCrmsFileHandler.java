package edu.ucsf.lava.crms.file.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.file.controller.BaseLavaFileComponentHandler;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.type.LavaDateUtils;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.enrollment.model.Consent;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.file.model.CrmsFile;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.scheduling.model.Visit;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;
import static edu.ucsf.lava.crms.file.CrmsRepositoryStrategy.CRMS_REPOSITORY_ID;

/**
 * Base class for files that are linked to patients, projects, consents, visits or instruments, i.e.
 * PatientAttachmentHandler
 * EnrollmentAttachmentHandler
 * ConsentAttachmentHandler
 * VisitAttachmentHandler
 * AssessmentAttachmentHandler
 * 
 * Note there are two UIs to attach a file.
 * One UI presents a list of all attachments for a given type of entity, e.g. all EnrollmentStatus
 * attachments (enrollmentAttachments action for patient list, projectEnrollmentAttachments action
 * for project list). 
 * The Add view is currently generic for all entities, e.g. an Assessment attachment 
 * could be added under the EnrollmentStatus attachment list, and vice versa.
 * e.g. enrollmentAttachments action, which shows enrollmentAttachments.jsp (which includes
 * enrollmentAttachmentsContent.jsp which is a separate include as it is also included by 
 * enrollmentAttachmentsListContent.jsp as described below).
 * 
 * The second UI is where a specific entity includes a list of attachments at the end (as a secondary
 * component supported by CrmsAttachmentsListHandler). In that case, it is already known which entity
 * to which the file will be attached, so the Add view does not need the user to select the
 * entity.
 * e.g. enrollmentStatus.jsp includes enrollmentAttachmentListContent.jsp which includes
 * enrollmentAttachmentsContent.jsp to display the list (this is a separate include as it is also
 * included by enrollmentAttachments.jsp as described above).  
 * 
 * Both of these UIs use the enrollmentAttachment action for attachment CRUD, but they pass a 
 * request parameter to each CRUD action indicating whether the action was invoked from the list
 * of all attachments for a given type of entity or invoked from the list of attachments for a
 * specific entity. This parameter is put into the model by the handler so the jsp can display
 * accordingly.
 * e.g. enrollmentAttachment.jsp has logic that either displays the attachments/attachmentContent.jsp
 * which can be used across patients, enrollmentStatus, visit and assessment, or it displays
 * content specific to an enrollmentStatus attachment.
 *  * 
 * @author jhesse
 *
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
		// critical to set the 
		crmsFile.setRepositoryId(CRMS_REPOSITORY_ID);
		//Setup default values
		crmsFile.setFileStatusBy(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request).getShortUserNameRev());
		crmsFile.setFileStatusDate(LavaDateUtils.getDatePart(new Date()));
		
		//setup the linking data depending on the parameter supplied. 
		
		// when this handler is used by EnrollmentAttachmentsHandler/ProjectEnrollmentAttachmentsHandler, the "patientId" request param
		// is set, and user must input the EnrollmentStatus (i.e. Project) at which time reRender event handling will set the EnrollmentStatus
		// on CrmsFile
		// when this handler is used by the secondary CrmsAttachmentsListHandler, the "enrollStatId" request param is set, so the
		// EnrollmentStatus is set on CrmsFile. user need not input an EnrollmentStatus and rerender.
//NOW WILL ONLY BE DOING THE LATTER OF 2 UI METHODS OF ATTACHING ENTITY SPECIFIC ATTACHMENTS	
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
		}else if (request.getParameterMap().containsKey("patientId")){
//NOTE: this is a patient specific attachment vs. an general attachment, which is attached to the patient
//in the patient specific attachment list will have to filter on Content Types that should be shown there. the general
//attachemnt list will show all attachments for the Patient and have Filter on Consent Type(s)
			Patient p = (Patient)Patient.MANAGER.getOne(this.getFilterWithId(request, Long.valueOf(request.getParameter("patientId"))));
			setPatient(crmsFile,p,request);
		}else{
//TODO: attaching via the Patient Attachments list so attach to the Patient
			
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
	 * Utility method to get the enrollment status when we know the patient and project.
	 * @param p
	 * @param projName
	 * @param request
	 * @return
	 */
/**	NUKE WHEN READY
	protected EnrollmentStatus getEnrollmentStatus(Patient p, String projName,HttpServletRequest request){
		LavaDaoFilter filter = EnrollmentStatus.MANAGER.newFilterInstance(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request));
		filter.setAlias("patient", "patient");
		filter.addDaoParam(filter.daoEqualityParam("patient.id", p.getId()));
		//TODO: refactor this to make sure it works with Project [unit] combinations
		filter.addDaoParam(filter.daoEqualityParam("projName",projName));
		EnrollmentStatus es = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(filter);
		return es;
	}
**/
	
	/**
	 * Utility method to get the enrollment status when we have the id.
	 * @param enrollStatId
	 * @param request
	 * @return
	 */
/**	NUKE WHEN READY
	protected EnrollmentStatus getEnrollmentStatus(Long enrollStatId,HttpServletRequest request){
		EnrollmentStatus es = (EnrollmentStatus) EnrollmentStatus.MANAGER.getOne(this.getFilterWithId(request,Long.valueOf(request.getParameter("enrollStatId"))));
		return es;
	}
**/

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

// DO NOT HAVE Add button IF NO PATIENT IN CONTEXT
/*		
		if(crmsFile.getPatient()==null){
			StateDefinition state = context.getCurrentState();
			if ((flowMode.equals("edit") && state.getId().equals("edit"))) {
				dynamicLists.put("crmsFile.patients", listManager.getDynamicList(getCurrentUser(request),"patient.allPatients"));
			}

		}
*/
		
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
