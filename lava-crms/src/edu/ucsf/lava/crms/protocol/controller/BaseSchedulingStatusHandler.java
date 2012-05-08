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

public class BaseSchedulingStatusHandler extends BaseStatusListHandler {

	public BaseSchedulingStatusHandler() {
		super();
		defaultEvents.addAll(Arrays.asList("timepoint","visit","summary","expanded"));
		this.setSourceProvider(new BaseSchedulingSourceProvider(this));
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = Protocol.newFilterInstance(getCurrentUser(request));
		
		// Quick Filters are configured in setupFilter method, as they are dependent upon the fmtGranularity
		// the user has chosen
		// default to showing scheduling that is overdue, sorted in order of most overdue timepoints first
		filter.setActiveQuickFilter("Pending Late");
		// note that in cases where a patient has not started the protocol yet, the schedWinStart will
		// be null, so sort by schedWinStart will sort those at the end. the user can view these is 
		// isolation with the "Not Started" QuickFilter
		filter.addDefaultSort("schedWinStart",true);
		
		// default for fmtGranularity and fmtColumns here
		filter.setParam("fmtGranularity", "Timepoint");
		filter.addParamHandler(new LavaNonParamHandler("fmtGranularity"));
		filter.setParam("fmtColumns", "Summary");
		filter.addParamHandler(new LavaNonParamHandler("fmtColumns"));
		
		return filter;
	}
	
	public static class BaseSchedulingSourceProvider extends BaseStatusListHandler.BaseStatusListSourceProvider {
    	public BaseSchedulingSourceProvider(CrmsListComponentHandler listHandler) {
    		super(listHandler);
    	}
    
		protected LavaDaoFilter setupFilter(LavaDaoFilter daoFilter, String fmtGranularity) {
			daoFilter.clearAliases();
			daoFilter.setAlias("patient", "patient");
			daoFilter.setAlias("config", "timepointConfig");
			daoFilter.setAlias("parent", "protocol");
			daoFilter.setAlias("protocol.config", "protocolConfig");
			
			daoFilter.clearDaoProjections();
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("id"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("timepointConfig.id"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("patient.fullNameRevNoSuffix"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("projName"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("protocolConfig.label"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("protocol.currStatus"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("protocol.currReason"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("protocol.currNote"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("timepointConfig.label"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("timepointConfig.optional"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("schedWinStart"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("schedWinEnd"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("schedWinAnchorDate"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("schedWinStatus"));
			
			daoFilter.clearQuickFilters();
			// for quick filters. comparisons can be made strictly on Date, so remove Time from the equation
			Calendar currCalDate = Calendar.getInstance();
			currCalDate.set(Calendar.HOUR_OF_DAY, 0);
			currCalDate.set(Calendar.MINUTE, 0);
			currCalDate.set(Calendar.SECOND, 0);
			currCalDate.set(Calendar.MILLISECOND, 0);
			Date currDate = currCalDate.getTime();
			
			List<ProtocolTracking> list = null;
			if (fmtGranularity.equals("Timepoint")) {
				if (this.sortContainsAlias("visitConfig", daoFilter)) {
					daoFilter.clearSort();
				}
				// remove any filter parameters specific to "Visit" granularity that may be set,
				// since they are not part of a Timepoint query 
				daoFilter.clearParam("visitConfig.label");
				
				// setup QuickFilter
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("schedWinStatus","Pending"));
				
				daoFilter.addQuickFilter("Pending Late", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("schedWinStatus","Pending"),
							daoFilter.daoLessThanParam("schedWinEnd", currDate)));

				daoFilter.addQuickFilter("Pending Now", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("schedWinStatus","Pending"),
							daoFilter.daoAnd(
								daoFilter.daoLessThanOrEqualParam("schedWinStart", currDate),	
								daoFilter.daoGreaterThanOrEqualParam("schedWinEnd", currDate))
							)
						);
			
				daoFilter.addQuickFilter("Pending Early", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("schedWinStatus","Pending"),
								daoFilter.daoGreaterThanParam("schedWinStart", currDate)));
				
				
				currCalDate.add(Calendar.MONTH, -6); // subtract another six months
				daoFilter.addQuickFilter("Pending > 1 Year Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("schedWinStatus","Pending"),
								daoFilter.daoLessThanParam("schedWinEnd", currCalDate.getTime())));
				
				currCalDate.add(Calendar.MONTH, -6);
				daoFilter.addQuickFilter("Pending > 6 Months Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("schedWinStatus","Pending"),
								daoFilter.daoLessThanParam("schedWinEnd", currCalDate.getTime())));
				
				daoFilter.addQuickFilter("Not Started", daoFilter.daoEqualityParam("schedWinStatus","Not Started"));
				
				daoFilter.addQuickFilter("Scheduled", daoFilter.daoInParam("schedWinStatus",new Object[]{"Scheduled","Early","Late"}));
			}
			else if (fmtGranularity.equals("Visit")) {
				daoFilter.setAlias("children", "visit");
				daoFilter.setAlias("visit.config", "visitConfig");
				daoFilter.setAlias("visit.visit", "assignedVisit");
				
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.label"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.optional"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("assignedVisit.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.summary"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.schedWinStatus"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.schedWinReason"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.schedWinNote"));
				
				// setup QuickFilter
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("visit.schedWinStatus","Pending"));
				
				daoFilter.addQuickFilter("Pending Late", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("visit.schedWinStatus","Pending"),
							daoFilter.daoLessThanParam("schedWinEnd", currDate)));

				daoFilter.addQuickFilter("Pending Now", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("visit.schedWinStatus","Pending"),
							daoFilter.daoAnd(
								daoFilter.daoLessThanOrEqualParam("schedWinStart", currDate),	
								daoFilter.daoGreaterThanOrEqualParam("schedWinEnd", currDate))
							)
						);
			
				daoFilter.addQuickFilter("Pending Early", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("visit.schedWinStatus","Pending"),
								daoFilter.daoGreaterThanParam("schedWinStart", currDate)));
				
				
				currCalDate.add(Calendar.MONTH, -6); // subtract another six months
				daoFilter.addQuickFilter("Pending > 1 Year Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("visit.schedWinStatus","Pending"),
								daoFilter.daoLessThanParam("schedWinEnd", currCalDate.getTime())));
				
				currCalDate.add(Calendar.MONTH, -6);
				daoFilter.addQuickFilter("Pending > 6 Months Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("visit.schedWinStatus","Pending"),
								daoFilter.daoLessThanParam("schedWinEnd", currCalDate.getTime())));
				
				daoFilter.addQuickFilter("Not Started", daoFilter.daoEqualityParam("visit.schedWinStatus","Not Started"));
				
				daoFilter.addQuickFilter("Scheduled", daoFilter.daoInParam("visit.schedWinStatus",new Object[]{"Scheduled","Early","Late"}));
			}

			daoFilter.convertParamsToDaoParams();
			// query for ProtocolTimepoints only. these may be joined to ProtocolVisits and ProtocolInstruments
			// via filter aliases. the end result, since using a projection, is that the query will return a
			// row for every row of the query result, i.e. rather than just rows for each ProtocolTimepoint, it
			// returns rows for ProtocolTimepoint joined to all of its ProtocolVisits and each ProtocolVisit
			// joined to all of its ProtocolInstruments
			daoFilter.addDaoParam(daoFilter.daoEqualityParam("nodeType", ProtocolNodeConfig.TIMEPOINT_NODE));
			
			return daoFilter;
		}
		
		
		protected List convertResultsToDto(List results, String fmtGranularity) {
			ArrayList dtoList = new ArrayList(results.size());
			Boolean optional;
			for(Object result:results){
				if(result.getClass().isArray()){
					Object[] resultArray = (Object[])result;
					StatusListItemDto dto = new StatusListItemDto();
					int i = 0;
					dto.setId((Long)resultArray[i++]);
					dto.setConfigId((Long)resultArray[i++]);
					dto.setFullNameRevNoSuffix((String)resultArray[i++]);
					dto.setProjName((String)resultArray[i++]);
					dto.setProtocolLabel((String)resultArray[i++]);
					dto.setCurrStatus((String)resultArray[i++]);
					dto.setCurrReason((String)resultArray[i++]);
					dto.setCurrNote((String)resultArray[i++]);
					dto.setTimepointLabel((String)resultArray[i++]);
					optional = (Boolean)resultArray[i++];
					if (optional != null && optional) {
						dto.setTimepointOptional("Yes");
					}
					dto.setTpSchedWinStart((Date)resultArray[i++]);
					dto.setTpSchedWinEnd((Date)resultArray[i++]);
					dto.setTpSchedWinAnchorDate((Date)resultArray[i++]);
					dto.setTpSchedWinStatus((String)resultArray[i++]);
					if (fmtGranularity.equals("Visit")) {
						dto.setVisitId((Long)resultArray[i++]);
						dto.setVisitConfigId((Long)resultArray[i++]);
						dto.setVisitLabel((String)resultArray[i++]);
						optional = (Boolean)resultArray[i++];
						if (optional != null && optional) {
							dto.setVisitOptional("Yes");
						}
						dto.setAssignedVisitId((Long)resultArray[i++]);
						dto.setVisitSummary((String)resultArray[i++]);
						dto.setVisitSchedWinStatus((String)resultArray[i++]);
						dto.setVisitSchedWinReason((String)resultArray[i++]);
						dto.setVisitSchedWinNote((String)resultArray[i++]);
					}
					dtoList.add(dto);
				}
			}
			return dtoList;
		}
		
	}	
	
}
