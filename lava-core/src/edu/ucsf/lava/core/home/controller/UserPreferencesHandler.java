package edu.ucsf.lava.core.home.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.calendar.CalendarDaoUtils;
import edu.ucsf.lava.core.calendar.model.Appointment;
import edu.ucsf.lava.core.controller.BaseListComponentHandler;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeOverlapParamHandler;
import edu.ucsf.lava.core.dao.LavaEqualityParamHandler;
import edu.ucsf.lava.core.dao.LavaIgnoreParamHandler;
import edu.ucsf.lava.core.home.model.Preference;
import edu.ucsf.lava.core.home.model.PreferenceUtils;
import edu.ucsf.lava.core.model.EntityBase;

public class UserPreferencesHandler extends BaseListComponentHandler {

	public UserPreferencesHandler() {
		this.setHandledList("userPreferences", "preference");
		this.setEntityForStandardSourceProvider(Preference.class);
	}

	@Override
	public LavaDaoFilter extractFilterFromRequest(RequestContext context,
			Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EntityBase.newFilterInstance(getCurrentUser(request));
		return filter;
	}
	

	@Override
	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter) {
		LavaDaoFilter filter  = super.onPostFilterParamConversion(daoFilter);
		filter.setAlias("user", "user");
		filter.addDaoParam(filter.daoOr(filter.daoEqualityParam("user.id", filter.getAuthUser().getId()), filter.daoNull("user")));
		filter.addDaoParam(filter.daoEqualityParam("visible", Short.valueOf("1")));
		return filter;
	}

	@Override
	public void updateFilterFromContext(LavaDaoFilter filter,
			RequestContext context, Map components) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Event handleCustomEvent(RequestContext context, Object command,
			BindingResult errors) throws Exception {
		String eventName = ActionUtils.getEventName(context);
		if(eventName.equals("custom")){
			return resetPref(context,command,errors);
		} else {
			return new Event(this,this.ERROR_FLOW_EVENT_ID);
		}
	}

	protected Event resetPref(RequestContext context, Object command, BindingResult errors) throws Exception {
		// delete user preference in order to reveal underlying default pref in list
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		String id = "";
		if(request.getParameterMap().containsKey("id")){
			id = request.getParameter("id");
		}
		if(StringUtils.isNotEmpty(id)) {
			Preference pref = (Preference)Preference.MANAGER.getById(Long.valueOf(id));
			if (pref!=null){
				if (!pref.isDefault()){
					pref.delete();
					this.subFlowReturnHook(context, command, errors);
				}
			} else {
				throw new RuntimeException(getMessage("idInvalid.command", new Object[]{getDefaultObjectName()}));
			}
		} else {
			throw new RuntimeException(getMessage("idMissing.command", new Object[]{getDefaultObjectName()}));
		}
		
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	@Override
	public List getList(Class entityClass, LavaDaoFilter daoFilter) {
		// remove default preferences where equivalent user preferences exist
		
		List prefs = super.getList(entityClass, daoFilter);
		List filteredPrefs = PreferenceUtils.removeOverriddenDefaults(prefs);
		daoFilter.setResultsCount(filteredPrefs.size());
		return filteredPrefs;
		
	}
	
	

}
