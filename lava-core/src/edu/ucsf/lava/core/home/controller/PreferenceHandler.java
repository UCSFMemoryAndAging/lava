package edu.ucsf.lava.core.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.home.model.Preference;
import edu.ucsf.lava.core.home.model.Preference.Manager;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.EntityPlaceholder;
import edu.ucsf.lava.core.session.CoreSessionUtils;

public class PreferenceHandler extends BaseEntityComponentHandler {

	public PreferenceHandler() {
		super();
		this.setHandledEntity("preference", Preference.class);
	}


	@Override
	public Map getBackingObjects(RequestContext context, Map components) {
		String flowMode = ActionUtils.getFlowMode(context.getActiveFlow().getId()); 
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager, request);
		Map backingObjects = new HashMap<String,Object>();
		String id = context.getFlowScope().getString("id");
		if(StringUtils.isNotEmpty(id)) {
			Preference pref = (Preference)EntityBase.MANAGER.getOne(getDefaultObjectBaseClass(), getFilterWithId(request,Long.valueOf(id)));
			if (pref!=null){
				if (flowMode.equals("edit") && pref.isDefault()){
					// cannot edit a default preference so create a new
					//   user preference from the supplied default
					pref = Preference.MANAGER.createUserPref(user, pref);
				}
				backingObjects.put(getDefaultObjectName(),pref);
			} else {
				throw new RuntimeException(getMessage("idInvalid.command", new Object[]{getDefaultObjectName()}));
			}
		}else{
			throw new RuntimeException(getMessage("idMissing.command", new Object[]{getDefaultObjectName()}));
		}
		
		return backingObjects;
	}


	//override and throw errors on the most critical methods that 
	//we want to make sure are never called by this handler. 
	@Override
	protected Event doAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}
	@Override
	protected Event doDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}
	@Override
	protected Event doSaveAdd(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}

	@Override
	protected Event doConfirmDelete(RequestContext context, Object command, BindingResult errors) throws Exception {
		return new Event(this,this.ERROR_FLOW_EVENT_ID);
	}

	
}
