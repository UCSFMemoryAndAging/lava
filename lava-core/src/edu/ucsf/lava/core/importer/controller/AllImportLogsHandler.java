package edu.ucsf.lava.core.importer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.importer.model.ImportLog;

public class AllImportLogsHandler extends BaseListComponentHandler { 

	public AllImportLogsHandler() {
		super();
		this.setHandledList("allImportLogs","importLog");
		this.setEntityForStandardSourceProvider(ImportLog.class);
		this.setPageSize(50);
		
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = ImportLog.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("importTimestamp",true);
		filter.setAlias("dataFile","dataFile");
		filter.setAlias("definition","definition");
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
	
	


}
