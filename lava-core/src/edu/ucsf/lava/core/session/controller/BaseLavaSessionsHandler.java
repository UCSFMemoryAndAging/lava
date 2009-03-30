package edu.ucsf.lava.core.session.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.LavaCustomDateEditor;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.session.SessionManager;
import edu.ucsf.lava.core.session.model.LavaServerInstance;
import edu.ucsf.lava.core.session.model.LavaSession;

public abstract class BaseLavaSessionsHandler extends BaseListComponentHandler {
	

	
	public void registerPropertyEditors(PropertyEditorRegistry registry) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		registry.registerCustomEditor(Date.class, new LavaCustomDateEditor(dateFormat, true));
	}

	public void setSessionMonitor(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public LavaServerInstance getServerInstance(){
		return sessionManager.getLavaServerInstance();
	}

	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter =  LavaSession.MANAGER.newFilterInstance(null);
		filter.addDefaultSort("createTime", false);
		return filter;
	}
	
//	don't need to do anything.
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
	}


	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		LavaSession currentSession = sessionManager.getLavaSession(((ServletExternalContext)context.getExternalContext()).getRequest().getSession());
		if(currentSession != null){
			model.put("currentLavaSessionId",currentSession.getId());
		}
		model.put("lavaServerInstance", getServerInstance());
		return super.addReferenceData(context, command, errors, model);
	}


}
