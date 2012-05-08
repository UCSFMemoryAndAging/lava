package edu.ucsf.lava.crms.protocol.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaNonParamHandler;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.protocol.dto.StatusListItemDto;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolNodeConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public abstract class BaseStatusListHandler extends CrmsListComponentHandler {

	public BaseStatusListHandler() {
		super();
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}

	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		//load up dynamic lists for the list filter
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map<String,Map<String,String>> dynamicLists = getDynamicLists(model);
		Map labelsList = null;
		labelsList = listManager.getDynamicList(getCurrentUser(request),"protocol.allProtocolLabels");		
		dynamicLists.put("protocol.allProtocolLabels", labelsList);
		labelsList = listManager.getDynamicList(getCurrentUser(request),"protocol.allTimepointLabels");		
		dynamicLists.put("protocol.allTimepointLabels", labelsList);
		labelsList = listManager.getDynamicList(getCurrentUser(request),"protocol.allVisitLabels");		
		dynamicLists.put("protocol.allVisitLabels", labelsList);
		labelsList = listManager.getDynamicList(getCurrentUser(request),"protocol.allInstrLabels");		
		dynamicLists.put("protocol.allInstrLabels", labelsList);
		model.put("dynamicLists", dynamicLists);
		return super.addReferenceData(context, command, errors, model);
	}
	
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		//	handle filter events
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		LavaDaoFilter daoFilter = (LavaDaoFilter)plh.getFilter();

		// handle the formatting events
		String event = ActionUtils.getEventName(context);
		if (ActionUtils.getEventName(context).equalsIgnoreCase("timepoint")){
			daoFilter.setParam("fmtGranularity", "Timepoint");
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase("visit")){
			daoFilter.setParam("fmtGranularity", "Visit");
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase("instrument")){
			daoFilter.setParam("fmtGranularity", "Instrument");
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase("summary")){
			daoFilter.setParam("fmtColumns", "Summary");
		} else if (ActionUtils.getEventName(context).equalsIgnoreCase("expanded")){
			daoFilter.setParam("fmtColumns", "Expanded");
		} 
		
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.refresh();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}
	
	
	public abstract static class BaseStatusListSourceProvider extends CrmsListComponentHandler.BaseListSourceProvider {
    	public BaseStatusListSourceProvider(CrmsListComponentHandler listHandler) {
    		super(listHandler, ProtocolTracking.class);
    	}
    	
		public List loadElements(Locale locale, Object filter) {
			String fmtGranularity = (String) ((LavaDaoFilter)filter).getParam("fmtGranularity");
			LavaDaoFilter daoFilter = setupFilter((LavaDaoFilter)filter, fmtGranularity);
			List list = ProtocolTracking.MANAGER.get(daoFilter);
			return convertResultsToDto(list, fmtGranularity);
		}
		
		public List loadList(Locale locale, Object filter) {
			String fmtGranularity = (String) ((LavaDaoFilter)filter).getParam("fmtGranularity");
			LavaDaoFilter daoFilter = setupFilter((LavaDaoFilter)filter, fmtGranularity);
			List list = ProtocolTracking.MANAGER.get(daoFilter);
			return ScrollablePagedListHolder.createSourceList(convertResultsToDto(list, fmtGranularity), daoFilter);
		}
		
		protected abstract LavaDaoFilter setupFilter(LavaDaoFilter filter, String fmtGranularity);
		
		protected abstract List convertResultsToDto(List results, String fmtGranularity);

		
		/**
		 * Utility method for use in setting up LavaDaoFilter.
		 */
		protected boolean sortContainsAlias(String alias, LavaDaoFilter filter){
			if (filter.getSort() != null) {
				for (Object key:filter.getSort().keySet()){
					if(((String)key).startsWith(alias)){
						return true;
					}
				
				}
				return false;
			}
			else {
				return false;
			}
		}
	}	
	
	
	
}
