package edu.ucsf.lava.crms.protocol.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class PatientProtocolsHandler extends CrmsListComponentHandler {

	public PatientProtocolsHandler() {
		super();
		CrmsSessionUtils.setIsPatientContext(this);
		this.setHandledList("patientProtocols","protocol");
		this.setEntityForStandardSourceProvider(Protocol.class);
	}


	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EnrollmentStatus.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("projName",true);
		return CrmsSessionUtils.setFilterPatientContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}



}
