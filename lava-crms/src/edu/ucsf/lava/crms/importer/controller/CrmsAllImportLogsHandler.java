package edu.ucsf.lava.crms.importer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.importer.controller.AllImportLogsHandler;
import edu.ucsf.lava.crms.importer.model.CrmsImportLog;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class CrmsAllImportLogsHandler extends AllImportLogsHandler {
	
	public CrmsAllImportLogsHandler() {
		super();
//PROB DO NOT NEED THIS SINCE SAME AS SUPERCLASS		
		this.setHandledList("allImportLogs","importLog");
		this.setEntityForStandardSourceProvider(CrmsImportLog.class);
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  CrmsSessionUtils.setFilterProjectContext(sessionManager,request,CrmsImportLog.newFilterInstance(getCurrentUser(request)));
		filter.addDefaultSort("importTimestamp",true);
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
}
