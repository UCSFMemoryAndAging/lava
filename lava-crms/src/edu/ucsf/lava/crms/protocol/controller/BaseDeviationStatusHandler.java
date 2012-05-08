package edu.ucsf.lava.crms.protocol.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaNonParamHandler;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.protocol.dto.StatusListItemDto;
import edu.ucsf.lava.crms.protocol.model.ProtocolNodeConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;

public class BaseDeviationStatusHandler extends BaseStatusListHandler {

	public BaseDeviationStatusHandler() {
		super();
		defaultEvents.addAll(Arrays.asList("timepoint","visit","instrument","summary","expanded"));
		this.setSourceProvider(new BaseDeviationSourceProvider(this));
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EnrollmentStatus.newFilterInstance(getCurrentUser(request));

		// Quick Filters are configured in setupFilter method, as they are dependent upon the fmtGranularity
		// the user has chosen
// default to showing collection that is overdue, sorted in order of most overdue timepoints first
//		filter.setActiveQuickFilter("Pending Late");
		// note that in cases where a patient has not started the protocol yet, the collectWinStart will
		// be null, so sort by collectWinStart will sort those at the end. the user can view these is 
		// isolation with the "Not Started" QuickFilter
		filter.addDefaultSort("collectWinStart",true);
		
		// default for fmtGranularity and fmtColumns here
		filter.setParam("fmtGranularity", "Timepoint");
		filter.addParamHandler(new LavaNonParamHandler("fmtGranularity"));
		filter.setParam("fmtColumns", "Summary");
		filter.addParamHandler(new LavaNonParamHandler("fmtColumns"));
		
		return filter;
	}
	
	public static class BaseDeviationSourceProvider extends BaseStatusListHandler.BaseStatusListSourceProvider {
    	public BaseDeviationSourceProvider(CrmsListComponentHandler listHandler) {
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
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("idealSchedWinStart"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("idealSchedWinEnd"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("schedWinStatus"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("collectWinStart"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("collectWinEnd"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("idealCollectWinStart"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("idealCollectWinEnd"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("collectWinStatus"));
			
			daoFilter.clearQuickFilters();
			
			List<ProtocolTracking> list = null;
			if (fmtGranularity.equals("Timepoint")) {
				if (this.sortContainsAlias("visitConfig", daoFilter) || this.sortContainsAlias("instrConfig", daoFilter)) {
					daoFilter.clearSort();
				}
				// remove any filter parameters specific to "Visit" or "Instrument" granularity that may be set,
				// since they are not part of a Timepoint query 
				daoFilter.clearParam("visitConfig.label");
				daoFilter.clearParam("instrConfig.label");
				
				// quick filter settings
				daoFilter.addQuickFilter("Early", 
						daoFilter.daoOr(
							daoFilter.daoEqualityParam("collectWinStatus","Early"),
							daoFilter.daoEqualityParam("schedWinStatus", "Early")));

				daoFilter.addQuickFilter("Late", 
						daoFilter.daoOr(
							daoFilter.daoEqualityParam("collectWinStatus","Late"),
							daoFilter.daoEqualityParam("schedWinStatus", "Late")));

				daoFilter.addQuickFilter("Scheduled Early",  daoFilter.daoEqualityParam("schedWinStatus","Early"));

				daoFilter.addQuickFilter("Scheduled Late",  daoFilter.daoEqualityParam("schedWinStatus","Late"));

				daoFilter.addQuickFilter("Collected Early",  daoFilter.daoEqualityParam("collectWinStatus","Early"));

				daoFilter.addQuickFilter("Collected Late",  daoFilter.daoEqualityParam("collectWinStatus","Late"));
			}
			else if (fmtGranularity.equals("Visit")) {
				if (this.sortContainsAlias("instrConfig", daoFilter)) {
					daoFilter.clearSort();
				}
				// remove any filter parameters specific to "Instrument" granularity that may be set,
				// since they are not part of a "Vist" granularity query 
				daoFilter.clearParam("instrConfig.label");

// the purpose of these aliases is to join to ProtocolVisit collection so the list query can filter 
// and sort on ProtocolVisit properties. the aliases do not result in the eager fetching of the ProtocolVisit
// collection (also tried Hibernate Criteria API setFetchMode to JOIN but that doesn't seem to work and would 
// be an outer join and even if it did, we want an inner join), so that is why there is a custom Dao
// method called below to load the collection (aka initialize)
// UPDATE: no, not doing it that way. rather, using a projection query so that all joins are performed,
// essentially eagerly fetching children				
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
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.collectWinStatus"));

				// quick filter settings
				daoFilter.addQuickFilter("Early", 
						daoFilter.daoOr(
							daoFilter.daoEqualityParam("visit.collectWinStatus","Early"),
							daoFilter.daoEqualityParam("visit.schedWinStatus", "Early")));

				daoFilter.addQuickFilter("Late", 
						daoFilter.daoOr(
							daoFilter.daoEqualityParam("visit.collectWinStatus","Late"),
							daoFilter.daoEqualityParam("visit.schedWinStatus", "Late")));

				daoFilter.addQuickFilter("Scheduled Early",  daoFilter.daoEqualityParam("visit.schedWinStatus","Early"));

				daoFilter.addQuickFilter("Scheduled Late",  daoFilter.daoEqualityParam("visit.schedWinStatus","Late"));

				daoFilter.addQuickFilter("Collected Early",  daoFilter.daoEqualityParam("visit.collectWinStatus","Early"));

				daoFilter.addQuickFilter("Collected Late",  daoFilter.daoEqualityParam("visit.collectWinStatus","Late"));
			}
			else if (fmtGranularity.equals("Instrument")) {
				daoFilter.setAlias("children", "visit");
				daoFilter.setAlias("visit.config", "visitConfig");
				daoFilter.setAlias("visit.children", "instr");
				daoFilter.setAlias("visit.visit", "assignedVisit");
				daoFilter.setAlias("instr.config", "instrConfig");
				daoFilter.setAlias("instr.instrument", "assignedInstr");
	
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.label"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.optional"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("assignedVisit.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.summary"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instrConfig.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instrConfig.label"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instrConfig.optional"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("assignedInstr.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("assignedInstr.instrType"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("assignedInstr.instrVer"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.summary"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.instrCollectWinStart"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.instrCollectWinEnd"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.idealInstrCollectWinStart"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.idealInstrCollectWinEnd"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.collectWinStatus"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.collectWinReason"));

				// quick filter settings
				// because there is no schedWinStatus at the instrument level, there are only "Early" and "Late" 
				// quick filters that are based on collectWinStatus
				daoFilter.addQuickFilter("Early",  daoFilter.daoEqualityParam("instr.collectWinStatus","Early"));

				daoFilter.addQuickFilter("Late",  daoFilter.daoEqualityParam("instr.collectWinStatus","Late"));
			}
			
			daoFilter.convertParamsToDaoParams();
			// query for ProtocolTimepoints only. these may be joined to ProtocolVisits and ProtocolInstruments
			// via filter aliases. the end result, since using a projection, is that the query will return a
			// row for every row of the query result, i.e. rather than just rows for each ProtocolTimepoint, it
			// returns rows for ProtocolTimepoint joined to all of its ProtocolVisits and each ProtocolVisit
			// joined to all of its ProtocolInstruments
			daoFilter.addDaoParam(daoFilter.daoEqualityParam("nodeType", ProtocolNodeConfig.TIMEPOINT_NODE));

			// only include results where the status is a deviation, i.e. "Early" or "Late"
			// if there is a quickfilter set, do not need to do this
			if (daoFilter.getActiveQuickFilter() == null) {
				if (fmtGranularity.equals("Timepoint")) {
					daoFilter.addDaoParam(
						daoFilter.daoOr(
							daoFilter.daoInParam("schedWinStatus",new Object[]{"Early","Late"}),
							daoFilter.daoInParam("collectWinStatus",new Object[]{"Early","Late"}))	
						);
				}
				else if (fmtGranularity.equals("Visit")) {
					daoFilter.addDaoParam(
							daoFilter.daoOr(
								daoFilter.daoInParam("visit.schedWinStatus",new Object[]{"Early","Late"}),
								daoFilter.daoInParam("visit.collectWinStatus",new Object[]{"Early","Late"}))	
							);
				}
				else if (fmtGranularity.equals("Instrument")) {
					// note: schedWinStatus not computed at instrument level since scheduling window at visit level
					daoFilter.addDaoParam(daoFilter.daoInParam("instr.collectWinStatus",new Object[]{"Early","Late"}));
				}
			}
//TODO: document at top that only Early/Late statuses are shown by this handler, i.e. by default if no filtering,
//by any quick filter, and on top of any Filter that is defined
//have QuickFilters for: Early/Late Scheduling only, Early/Late Collection only, Early Scheduling, Late Scheduling, Early Collection, Late Collection
//?? not sure what default sort should be, schedWinStart or collectWinStart? for QuickFilters should have default sort
//based on whether the QuickFilter is for Scheduilng or Colletion			
			
			
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
					dto.setTpIdealSchedWinStart((Date)resultArray[i++]);
					dto.setTpIdealSchedWinEnd((Date)resultArray[i++]);
					dto.setTpSchedWinStatus((String)resultArray[i++]);
					dto.setTpCollectWinStart((Date)resultArray[i++]);
					dto.setTpCollectWinEnd((Date)resultArray[i++]);
					dto.setTpIdealCollectWinStart((Date)resultArray[i++]);
					dto.setTpIdealCollectWinEnd((Date)resultArray[i++]);
					dto.setTpCollectWinStatus((String)resultArray[i++]);
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
						dto.setVisitCollectWinStatus((String)resultArray[i++]);
					}
					else if (fmtGranularity.equals("Instrument")) {
						dto.setVisitId((Long)resultArray[i++]);
						dto.setVisitConfigId((Long)resultArray[i++]);
						dto.setVisitLabel((String)resultArray[i++]);
						optional = (Boolean)resultArray[i++];
						if (optional != null && optional) {
							dto.setVisitOptional("Yes");
						}
						dto.setAssignedVisitId((Long)resultArray[i++]);
						dto.setVisitSummary((String)resultArray[i++]);
						dto.setInstrId((Long)resultArray[i++]);
						dto.setInstrConfigId((Long)resultArray[i++]);
						dto.setInstrLabel((String)resultArray[i++]);
						optional = (Boolean)resultArray[i++];
						if (optional != null && optional) {
							dto.setInstrOptional("Yes");
						}
						dto.setAssignedInstrId((Long)resultArray[i++]);
						String instrType = (String)resultArray[i++];
						String instrVer = (String)resultArray[i++];
						dto.setAssignedInstrTypeEncoded(new Instrument(instrType, instrVer){}.getInstrTypeEncoded());
						dto.setInstrSummary((String)resultArray[i++]);
						dto.setInstrCollectWinStart((Date)resultArray[i++]);
						dto.setInstrCollectWinEnd((Date)resultArray[i++]);
						dto.setInstrIdealInstrCollectWinStart((Date)resultArray[i++]);
						dto.setInstrIdealInstrCollectWinEnd((Date)resultArray[i++]);
						dto.setInstrCollectWinStatus((String)resultArray[i++]);
						dto.setInstrCollectWinReason((String)resultArray[i++]);
					}
					dtoList.add(dto);
				}
			}
			return dtoList;
		}
		
	}	
	
}
