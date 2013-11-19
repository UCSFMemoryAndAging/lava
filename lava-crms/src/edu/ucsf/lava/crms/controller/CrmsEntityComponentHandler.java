package edu.ucsf.lava.crms.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.auth.CrmsAuthUtils;
import edu.ucsf.lava.crms.enrollment.EnrollmentManager;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.people.model.Patient;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.scheduling.VisitManager;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


public class CrmsEntityComponentHandler extends BaseEntityComponentHandler {

	
	protected InstrumentManager instrumentManager;
	protected EnrollmentManager enrollmentManager;
	protected VisitManager visitManager; 
	protected ProjectManager projectManager; 
	
	public void updateManagers(Managers managers){
		super.updateManagers(managers);
		this.enrollmentManager = CrmsManagerUtils.getEnrollmentManager(managers);
		this.instrumentManager = CrmsManagerUtils.getInstrumentManager(managers);
		this.projectManager = CrmsManagerUtils.getProjectManager(managers);
		this.visitManager = CrmsManagerUtils.getVisitManager(managers);
	}
	
	public  Map<String,String> filterVisitListByPermission(AuthUser user, Action action, 
			Map<String,String> visitList, boolean removeLockedVisits) {
		return CrmsAuthUtils.filterVisitListByPermission(user, action, visitList, removeLockedVisits);
	}
	
	public Map<String,String> filterVisitListByPermission(AuthUser user, Action action, Map<String,String> visitList) {
		return filterVisitListByPermission(user, action, visitList, false);
	}
	
	// helper method. useful in situations where an entity is to be loaded via an id, but the id 
	// could be a patient id or an entity id, depending upon the user interface constructs used to
	// load the entity (e.g. if an entity CRUD action is a section default, the entity id is not 
	// available, so the currentPatient id is supplied, while if the entity CRUD action is a list 
	// action then the entity id is supplied)
	protected boolean isIdPatientId(HttpServletRequest request, Long id) {
		Patient currentPatient = CrmsSessionUtils.getCurrentPatient(sessionManager, request);
		if (currentPatient == null) {
			return false;
		}
		else {
			LavaDaoFilter filter = Patient.newFilterInstance(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request));
			filter.addDaoParam(filter.daoEqualityParam("id", id));
			Patient patient = (Patient)Patient.MANAGER.getById(id, filter);
			if (patient == null) {
				return false;
			}
			else {
				// cover the extremely unlikely case that that the patient and a given entity have the same id
				// by making sure the patient just retrieved is the currentPatient in context
				//TODO: implement equals for Patient because the default does not work. for now compare on fullName
				if (patient.getFullName().equals(currentPatient.getFullName())) {
					return true;
				}
				else {
					return false; 
				}
			}
		}
	}

}
