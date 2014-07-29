package edu.ucsf.lava.core.importer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.auth.model.AuthRole;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.importer.model.ImportDefinition;

public class AllImportDefinitionsHandler extends BaseListComponentHandler { 

	public AllImportDefinitionsHandler() {
		super();
		this.setHandledList("allImportDefinitions","importDefinition");
		this.setEntityForStandardSourceProvider(ImportDefinition.class);
		this.pageSize = 25;
		
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = ImportDefinition.newFilterInstance(getCurrentUser(request));
		filter.addDefaultSort("name",true);
		return filter;
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}
	
}
