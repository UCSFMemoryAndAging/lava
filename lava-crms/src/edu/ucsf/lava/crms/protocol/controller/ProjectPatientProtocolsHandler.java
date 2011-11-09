package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.protocol.model.PatientProtocol;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectPatientProtocolsHandler extends CrmsListComponentHandler {

	public ProjectPatientProtocolsHandler() {
		super();
		this.setHandledList("projectPatientProtocols","patientProtocol");
		this.setEntityForStandardSourceProvider(PatientProtocol.class);
	}


	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EnrollmentStatus.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("patient.fullNameRev",true);
		filter.setAlias("patient", "patient");
// FILTER ON ENROLLED DATE USEFUL?? FILTER USEFUL??		
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
		
		
		// ANY LISTS FOR FILTER DROPDOWNS??
		
		
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
		
}
