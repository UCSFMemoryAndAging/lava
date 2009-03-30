package edu.ucsf.lava.core.session.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.LavaCustomDateEditor;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.model.LavaSession;

public class LavaSessionHandler extends BaseEntityComponentHandler {
	

	
	public LavaSessionHandler() {
		super();
		setHandledEntity("lavaSession",LavaSession.class);
	}
	
	public void registerPropertyEditors(PropertyEditorRegistry registry) {
		super.registerPropertyEditors(registry);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss a");
		SimpleDateFormat disconnectTimeFormat = new SimpleDateFormat("MM/dd/yyyy h:mma");
		registry.registerCustomEditor(Date.class, "components[lavaSession].createTime", new LavaCustomDateEditor(dateFormat, true));
		registry.registerCustomEditor(Date.class, "components[lavaSession].accessTime", new LavaCustomDateEditor(dateFormat, true));
		registry.registerCustomEditor(Date.class, "components[lavaSession].expireTime", new LavaCustomDateEditor(dateFormat, true));
		registry.registerCustomEditor(Date.class, "components[lavaSession].disconnectTime", new LavaCustomDateEditor(disconnectTimeFormat, true));
	}



	

	public Map getBackingObjects(RequestContext context, Map components) {
		Map backingObjects = super.getBackingObjects(context, components);
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		FlowDefinition flow = context.getActiveFlow();

		/*
		 * This code keeps the current user from editing their own session...This makes sense
		 * but also is becuase we get a stale object exception when we try to save the changes
		 * because the database has been updated by the SessionMonitoringFilter on the submit of 
		 * changes.   I realized after the fact, that I could override onSave to get out the two
		 * properties that can be edited (disconnectTime and disconnectMessage and store them
		 * temporarily while I call refresh on the backing object.  Then reapply the changes and save.
		 * Don't need to do this now that we have decided that you cannot edit your own session.  But
		 * if we wanted to we could. 
		 */
		
		if (flowMode.equals("edit")) {
			LavaSession currentSession = sessionManager.getLavaSession(request.getSession());
			LavaSession sessionToEdit = (LavaSession)backingObjects.get(getDefaultObjectName());
			if (currentSession != null && sessionToEdit!= null && currentSession.getId().equals(sessionToEdit.getId())){
				throw new RuntimeException(metadataManager.getMessage("lavaSession.noEditCurrentSession.message",null, Locale.getDefault()));
				}
		}
		
		return backingObjects;
	}


	protected Event doEdit(RequestContext context, Object command, BindingResult errors) throws Exception {
	
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaSession currentSession = sessionManager.getLavaSession(request.getSession());
		LavaSession sessionToEdit = (LavaSession)((ComponentCommand)command).getComponents().get(getDefaultObjectName());
		if (currentSession != null && sessionToEdit!= null && currentSession.getId().equals(sessionToEdit.getId())){
			CoreSessionUtils.addFormError(sessionManager,request, new String[]{"lavaSession.noEditCurrentSession.message"}, null);
			return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
		}
		return super.doEdit(context, command, errors);
	}



	
	
}
