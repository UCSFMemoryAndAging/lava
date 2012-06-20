package edu.ucsf.lava.crms.reporting.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.reporting.controller.ReportComponentHandler;
import edu.ucsf.lava.core.reporting.model.ReportSetup;
import edu.ucsf.lava.crms.assessment.InstrumentManager;
import edu.ucsf.lava.crms.auth.model.CrmsAuthUser;
import edu.ucsf.lava.crms.enrollment.EnrollmentManager;
import edu.ucsf.lava.crms.manager.CrmsManagerUtils;
import edu.ucsf.lava.crms.project.ProjectManager;
import edu.ucsf.lava.crms.scheduling.VisitManager;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;


/**
 * A Crms specific implementation of the base Report Component handler.  Adds functionality for 
 * Auth filtering by project (used in direct jdbc sql to ensure that only appropriate data is displayed) 
 * as well as functionality for specifying "selected project" criteria for Crms reports. 
 * @author jhesse
 *
 */
public class CrmsReportComponentHandler extends ReportComponentHandler {
	
	protected InstrumentManager instrumentManager;
	protected EnrollmentManager enrollmentManager;
	protected VisitManager visitManager; 
	protected ProjectManager projectManager; 

	
	protected Boolean projectCriteria;

	public CrmsReportComponentHandler(){
		super();
		this.projectCriteria = false;
		
	}
	
	public void updateManagers(Managers managers){
		super.updateManagers(managers);
		this.enrollmentManager = CrmsManagerUtils.getEnrollmentManager(managers);
		this.instrumentManager = CrmsManagerUtils.getInstrumentManager(managers);
		this.projectManager = CrmsManagerUtils.getProjectManager(managers);
		this.visitManager = CrmsManagerUtils.getVisitManager(managers);
	}
	
	/**
	 * Adds Crms specific reference data to the model for the report. 
	 */
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		StateDefinition state = context.getCurrentState();
		ReportSetup reportSetup = (ReportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		model = super.addReferenceData(context, command, errors, model);
		
		if (state.getId().equals("reportSetup")) {
			model.put("projectCriteria", this.projectCriteria);

		    //load up dynamic lists
		    if (this.projectCriteria) {
		    	Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		    	dynamicLists.put("context.projectList", 
		    			listManager.getDynamicList(CrmsSessionUtils.getCrmsCurrentUser(sessionManager, request), "context.projectList"));
		    	model.put("dynamicLists", dynamicLists);
		    }
		}
		if (state.getId().equals("reportGen")) {
		 	
			CrmsAuthUser user = CrmsSessionUtils.getCrmsCurrentUser(sessionManager,request);
			if (user != null){
				model.put("allPatientAccess",new Boolean(user.getAllPatientProjectAccess()));
				model.put("authPatientProjectAccessList",user.getPatientProjectAccessList());
				model.put("authAllProjectAccess",new Boolean(user.getAllProjectAccess()));
				model.put("authProjectAccessList",user.getProjectAccessList());
				}
			
			if (this.projectCriteria) {
			    // within the report design, parameter must be defined and named this.selectedProjListPropName 
				List<String> selectedProjList = (List<String>) reportSetup.getFilter().getParam(this.selectedProjListPropName);
			    model.put(this.selectedProjListPropName, selectedProjList);
			}
		}
		
		return model;
	}

	/**
	 * Add project filter criteria to the report filter
	 */
	protected LavaDaoFilter setupReportFilter(RequestContext context, LavaDaoFilter filter) {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		filter = super.setupReportFilter(context, filter);
		
	 	
		
		if (this.projectCriteria) {
			// initialize the projectList filter field to the current project
			// it is critical that the filter param is intialized as a java.util.List here so that the 
			// CustomCollectionEditor registered on java.util.List is used when binding the multiple select
			// box used for projectList
			List<String> selectedProjList = new ArrayList<String>();
			if (CrmsSessionUtils.getCurrentProject(sessionManager,request) != null) {
				selectedProjList.add(CrmsSessionUtils.getCurrentProject(sessionManager,request).getProjUnitDesc());
			}
			filter.setParam(this.selectedProjListPropName, selectedProjList);
		}
		
		return filter;
	}


	/**
	 * set required fields for Project Criteria queries
	 */
	protected String[] defineRequiredFields(RequestContext context, Object command) {
		ReportSetup reportSetup = (ReportSetup) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		String[] required = super.defineRequiredFields(context, command);

		if (this.projectCriteria) {
			required = StringUtils.addStringToArray(required, this.selectedProjListPropName);
		}
		
		setRequiredFields(required);
		return getRequiredFields();
	}

	

	
	
	public Boolean getProjectCriteria() {
		return projectCriteria;
	}

	public void setProjectCriteria(Boolean projectCriteria) {
		this.projectCriteria = projectCriteria;
	}
	
	
}
