package edu.ucsf.lava.crms.enrollment.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectEnrollmentStatusHandler extends CrmsListComponentHandler {

	public ProjectEnrollmentStatusHandler() {
		super();
		this.setHandledList("projectEnrollmentStatus","enrollmentStatus");
		this.setEntityForStandardSourceProvider(EnrollmentStatus.class);
	}


	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EnrollmentStatus.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("patient.fullNameRev",true);
		filter.setAlias("patient", "patient");
		filter.addParamHandler(new LavaDateRangeParamHandler("enrolledDate"));
		return CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		//load up dynamic lists for the list filter
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);		
		String project = CrmsSessionUtils.getCurrentProject(sessionManager,request)==null ? "%": CrmsSessionUtils.getCurrentProject(sessionManager,request).getName();
		dynamicLists.put("enrollmentStatus.projectStatus", listManager.getDynamicList("enrollmentStatus.projectStatus", 
				"projectName",project,String.class));
		//if no entries found (size is 1 because "","" entry added to all dynamic lists), then use 'GENERAL' as the project name to get general enrollment statuses. 
		if (dynamicLists.get("enrollmentStatus.projectStatus").size()==1){
			dynamicLists.put("enrollmentStatus.projectStatus", listManager.getDynamicList("enrollmentStatus.projectStatus", 
					"projectName","GENERAL",String.class));
		}
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
		
}
