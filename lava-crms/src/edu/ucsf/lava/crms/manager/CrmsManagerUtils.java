package edu.ucsf.lava.crms.manager;

import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.enrollment.EnrollmentManager;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.scheduling.VisitManager;




public class CrmsManagerUtils extends CoreManagerUtils {


	public static EnrollmentManager getEnrollmentManager(Managers managers){
		return (EnrollmentManager) managers.get(EnrollmentManager.ENROLLMENT_MANAGER_NAME);
	}
	
	
	public static EnrollmentManager getEnrollmentManager(){
		return getEnrollmentManager(getManagers());
	}
	
	public static InstrumentManager getInstrumentManager(Managers managers){
		return (InstrumentManager) managers.get(InstrumentManager.INSTRUMENT_MANAGER_NAME);
	}
	public static InstrumentManager getInstrumentManager(){
		return getInstrumentManager(getManagers());
	}
	
	public static ProjectManager getProjectManager(Managers managers){
		return (ProjectManager) managers.get(ProjectManager.PROJECT_MANAGER_NAME);
	}
	public static ProjectManager getProjectManager(){
		return getProjectManager(getManagers());
	}
	
	
	
	public static VisitManager getVisitManager(Managers managers){
		return (VisitManager) managers.get(VisitManager.VISIT_MANAGER_NAME);
	}
	
	public static VisitManager getVisitManager(){
		return getVisitManager(getManagers());
	}
	
}
