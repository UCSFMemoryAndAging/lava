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

public class BaseCollectionStatusHandler extends BaseStatusListHandler {

	public BaseCollectionStatusHandler() {
		super();
		defaultEvents.addAll(Arrays.asList("timepoint","visit","instrument","summary","expanded"));
		this.setSourceProvider(new BaseCollectionSourceProvider(this));
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = EnrollmentStatus.newFilterInstance(getCurrentUser(request));
		
		// Quick Filters are configured in setupFilter method, as they are dependent upon the fmtGranularity
		// the user has chosen
		// default to showing collection that is overdue, sorted in order of most overdue timepoints first
		filter.setActiveQuickFilter("Pending Late");
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
	
	public static class BaseCollectionSourceProvider extends BaseStatusListHandler.BaseStatusListSourceProvider {
    	public BaseCollectionSourceProvider(CrmsListComponentHandler listHandler) {
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
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("collectWinStart"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("collectWinEnd"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("collectWinAnchorDate"));
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("collectWinStatus"));
			
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
				if (this.sortContainsAlias("visitConfig", daoFilter) || this.sortContainsAlias("instrConfig", daoFilter)) {
					daoFilter.clearSort();
				}
				// remove any filter parameters specific to "Visit" or "Instrument" granularity that may be set,
				// since they are not part of a Timepoint query 
				daoFilter.clearParam("visitConfig.label");
				daoFilter.clearParam("instrConfig.label");
				
				// quick filter settings
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("collectWinStatus","Pending"));
				
				daoFilter.addQuickFilter("Pending Late", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("collectWinStatus","Pending"),
							daoFilter.daoLessThanParam("collectWinEnd", currDate)));

				daoFilter.addQuickFilter("Pending Now", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("collectWinStatus","Pending"),
							daoFilter.daoAnd(
								daoFilter.daoLessThanOrEqualParam("collectWinStart", currDate),	
								daoFilter.daoGreaterThanOrEqualParam("collectWinEnd", currDate))
							)
						);
			
				daoFilter.addQuickFilter("Pending Early", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("collectWinStatus","Pending"),
								daoFilter.daoGreaterThanParam("collectWinStart", currDate)));
				
				
				currCalDate.add(Calendar.MONTH, -6); // subtract another six months
				daoFilter.addQuickFilter("Pending > 1 Year Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("collectWinStatus","Pending"),
								daoFilter.daoLessThanParam("collectWinEnd", currCalDate.getTime())));
				
				currCalDate.add(Calendar.MONTH, -6);
				daoFilter.addQuickFilter("Pending > 6 Months Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("collectWinStatus","Pending"),
								daoFilter.daoLessThanParam("collectWinEnd", currCalDate.getTime())));
				
				daoFilter.addQuickFilter("Not Started",  daoFilter.daoEqualityParam("collectWinStatus","Not Started"));
				
				daoFilter.addQuickFilter("Collected", daoFilter.daoInParam("collectWinStatus",new Object[]{"Collected","Early","Late"}));
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
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.collectWinStatus"));
				
				// quick filter settings
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("visit.collectWinStatus","Pending"));
				
				daoFilter.addQuickFilter("Pending Late", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("visit.collectWinStatus","Pending"),
							daoFilter.daoLessThanParam("collectWinEnd", currDate)));

				daoFilter.addQuickFilter("Pending Now", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("visit.collectWinStatus","Pending"),
							daoFilter.daoAnd(
								daoFilter.daoLessThanOrEqualParam("collectWinStart", currDate),	
								daoFilter.daoGreaterThanOrEqualParam("collectWinEnd", currDate))
							)
						);
			
				daoFilter.addQuickFilter("Pending Early", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("visit.collectWinStatus","Pending"),
								daoFilter.daoGreaterThanParam("collectWinStart", currDate)));
				
				
				currCalDate.add(Calendar.MONTH, -6); // subtract another six months
				daoFilter.addQuickFilter("Pending > 1 Year Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("visit.collectWinStatus","Pending"),
								daoFilter.daoLessThanParam("collectWinEnd", currCalDate.getTime())));
				
				currCalDate.add(Calendar.MONTH, -6);
				daoFilter.addQuickFilter("Pending > 6 Months Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("visit.collectWinStatus","Pending"),
								daoFilter.daoLessThanParam("collectWinEnd", currCalDate.getTime())));
				
				daoFilter.addQuickFilter("Not Started",  daoFilter.daoEqualityParam("visit.collectWinStatus","Not Started"));
				
				daoFilter.addQuickFilter("Collected", daoFilter.daoInParam("visit.collectWinStatus",new Object[]{"Collected","Early","Late"}));
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
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.instrCollectWinAnchorDate"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.collectWinStatus"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.collectWinReason"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.collectWinNote"));
				
				// quick filter settings
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("instr.collectWinStatus","Pending"));
				
				daoFilter.addQuickFilter("Pending Late", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("instr.collectWinStatus","Pending"),
							daoFilter.daoLessThanParam("instr.instrCollectWinEnd", currDate)));

				daoFilter.addQuickFilter("Pending Now", 
						daoFilter.daoAnd(
							daoFilter.daoEqualityParam("instr.collectWinStatus","Pending"),
							daoFilter.daoAnd(
								daoFilter.daoLessThanOrEqualParam("instr.instrCollectWinStart", currDate),	
								daoFilter.daoGreaterThanOrEqualParam("instr.instrCollectWinEnd", currDate))
							)
						);
			
				daoFilter.addQuickFilter("Pending Early", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("instr.collectWinStatus","Pending"),
								daoFilter.daoGreaterThanParam("instr.instrCollectWinStart", currDate)));
				
				
				currCalDate.add(Calendar.MONTH, -6); // subtract another six months
				daoFilter.addQuickFilter("Pending > 1 Year Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("instr.collectWinStatus","Pending"),
								daoFilter.daoLessThanParam("instr.instrCollectWinEnd", currCalDate.getTime())));
				
				currCalDate.add(Calendar.MONTH, -6);
				daoFilter.addQuickFilter("Pending > 6 Months Late", 
						daoFilter.daoAnd(
								daoFilter.daoEqualityParam("instr.collectWinStatus","Pending"),
								daoFilter.daoLessThanParam("instr.instrCollectWinEnd", currCalDate.getTime())));
				
				daoFilter.addQuickFilter("Not Started",  daoFilter.daoEqualityParam("instr.collectWinStatus","Not Started"));
				
				daoFilter.addQuickFilter("Collected", daoFilter.daoInParam("instr.collectWinStatus",new Object[]{"Collected","Early","Late"}));
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
					dto.setTpCollectWinStart((Date)resultArray[i++]);
					dto.setTpCollectWinEnd((Date)resultArray[i++]);
					dto.setTpCollectWinAnchorDate((Date)resultArray[i++]);
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
						dto.setInstrCollectWinAnchorDate((Date)resultArray[i++]);
						dto.setInstrCollectWinStatus((String)resultArray[i++]);
						dto.setInstrCollectWinReason((String)resultArray[i++]);
						dto.setInstrCollectWinNote((String)resultArray[i++]);
					}
					dtoList.add(dto);
				}
			}
			return dtoList;
		}
		
	}	
	
}
