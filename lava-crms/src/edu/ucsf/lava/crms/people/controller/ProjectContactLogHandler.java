package edu.ucsf.lava.crms.people.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.people.model.ContactLog;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectContactLogHandler extends CrmsListComponentHandler {
	
	public ProjectContactLogHandler(){
		this.setHandledList("projectContactLog","contactLog");
		this.setEntityForStandardSourceProvider(ContactLog.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = CrmsSessionUtils.setFilterProjectContext(sessionManager,request,ContactLog.newFilterInstance(getCurrentUser(request)));
		filter.setAlias("patient","patient");
		filter.addDefaultSort("logDate",false);
		filter.addDefaultSort("logTime",false);
		filter.addParamHandler(new LavaDateRangeParamHandler("logDate"));
		return filter;
	}

	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
		

	
}
