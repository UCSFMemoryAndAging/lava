package edu.ucsf.lava.crms.importer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.importer.controller.AllImportLogsHandler;
import edu.ucsf.lava.crms.importer.model.CrmsImportLog;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class CrmsAllImportLogsHandler extends AllImportLogsHandler {
	
	public CrmsAllImportLogsHandler() {
		super();
		this.setEntityForStandardSourceProvider(CrmsImportLog.class);
	}
	
	/**
	 * The idea here is that if this is a crms application, then we always want
	 * to use the CrmsAllImportLogsHandler instead of the core AllImportLogsHandler.  If scopes
	 * need to extend ImportLog further, then they should subclass and customize this
	 * handler/action. 
	 */
	@Override
	public Event preSetupFlowDirector(RequestContext context) throws Exception {
		return new Event(this,CONTINUE_FLOW_EVENT_ID);
	}
		

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  CrmsSessionUtils.setFilterProjectContext(sessionManager,request,CrmsImportLog.newFilterInstance(getCurrentUser(request)));
		filter.addDefaultSort("importTimestamp",true);
		filter.setAlias("dataFile","dataFile");
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
}
