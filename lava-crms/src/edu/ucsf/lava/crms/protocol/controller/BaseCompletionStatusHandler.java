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
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.controller.CrmsListComponentHandler;
import edu.ucsf.lava.crms.enrollment.model.EnrollmentStatus;
import edu.ucsf.lava.crms.protocol.dto.StatusListItemDto;
import edu.ucsf.lava.crms.protocol.model.Protocol;
import edu.ucsf.lava.crms.protocol.model.ProtocolNodeConfig;
import edu.ucsf.lava.crms.protocol.model.ProtocolTracking;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class BaseCompletionStatusHandler extends BaseStatusListHandler {

	public BaseCompletionStatusHandler() {
		super();
		defaultEvents.addAll(Arrays.asList("timepoint","visit","instrument","summary","expanded"));
		this.setSourceProvider(new BaseCompletionSourceProvider(this));
	}
	
	public LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		LavaDaoFilter filter = Protocol.newFilterInstance(getCurrentUser(request));
		
		// Quick Filters are configured in setupFilter method, as they are dependent upon the fmtGranularity
		// the user has chosen
		// default to showing those that are not complete, sorted in order of most overdue timepoints first
		filter.setActiveQuickFilter("Not Completed");
		filter.addDefaultSort("collectWinStart",true);
		
		// default for fmtGranularity and fmtColumns here
		filter.setParam("fmtGranularity", "Timepoint");
		filter.addParamHandler(new LavaNonParamHandler("fmtGranularity"));
		filter.setParam("fmtColumns", "Summary");
		filter.addParamHandler(new LavaNonParamHandler("fmtColumns"));
		
		return filter;
	}
	
	public static class BaseCompletionSourceProvider extends BaseStatusListHandler.BaseStatusListSourceProvider {
    	public BaseCompletionSourceProvider(CrmsListComponentHandler listHandler) {
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
			daoFilter.addDaoProjection(daoFilter.daoGroupProjection("compStatus"));
			
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
				daoFilter.addQuickFilter("Not Started", daoFilter.daoEqualityParam("compStatus","Not Started"));
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("compStatus","Pending"));
				daoFilter.addQuickFilter("In Progress", daoFilter.daoEqualityParam("compStatus","In Progress"));
				daoFilter.addQuickFilter("Not Completed", daoFilter.daoEqualityParam("compStatus","Not Completed"));
				daoFilter.addQuickFilter("Completed", daoFilter.daoInParam("compStatus",new Object[]{"Completed","Partial"}));
				daoFilter.addQuickFilter("Completed (Partial)", daoFilter.daoEqualityParam("compStatus","Partial"));
			}
			else if (fmtGranularity.equals("Visit")) {
				if (this.sortContainsAlias("instrConfig", daoFilter)) {
					daoFilter.clearSort();
				}
				// remove any filter parameters specific to "Instrument" granularity that may be set,
				// since they are not part of a "Vist" granularity query 
				daoFilter.clearParam("instrConfig.label");

				daoFilter.setAlias("children", "visit");
				daoFilter.setAlias("visit.config", "visitConfig");
				daoFilter.setAlias("visit.visit", "assignedVisit");
				
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.label"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visitConfig.optional"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("assignedVisit.id"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.summary"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("visit.compStatus"));
				
				// quick filter settings
				daoFilter.addQuickFilter("Not Started", daoFilter.daoEqualityParam("visit.compStatus","Not Started"));
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("visit.compStatus","Pending"));
				daoFilter.addQuickFilter("In Progress", daoFilter.daoEqualityParam("visit.compStatus","In Progress"));
				daoFilter.addQuickFilter("Not Completed", daoFilter.daoEqualityParam("visit.compStatus","Not Completed"));
				daoFilter.addQuickFilter("Completed", daoFilter.daoInParam("visit.compStatus",new Object[]{"Completed","Partial"}));
				daoFilter.addQuickFilter("Completed (Partial)", daoFilter.daoEqualityParam("visit.compStatus","Partial"));
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
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.compStatus"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.compReason"));
				daoFilter.addDaoProjection(daoFilter.daoGroupProjection("instr.compNote"));
				
				// quick filter settings
				daoFilter.addQuickFilter("Not Started", daoFilter.daoEqualityParam("instr.compStatus","Not Started"));
				daoFilter.addQuickFilter("Pending", daoFilter.daoEqualityParam("instr.compStatus","Pending"));
				daoFilter.addQuickFilter("In Progress", daoFilter.daoEqualityParam("instr.compStatus","In Progress"));
				daoFilter.addQuickFilter("Not Completed", daoFilter.daoEqualityParam("instr.compStatus","Not Completed"));
				daoFilter.addQuickFilter("Completed", daoFilter.daoInParam("instr.compStatus",new Object[]{"Completed","Partial"}));
				daoFilter.addQuickFilter("Completed (Partial)", daoFilter.daoEqualityParam("instr.compStatus","Partial"));
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
					dto.setTpCompStatus((String)resultArray[i++]);
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
						dto.setVisitCompStatus((String)resultArray[i++]);
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
						dto.setInstrCompStatus((String)resultArray[i++]);
						dto.setInstrCompReason((String)resultArray[i++]);
						dto.setInstrCompNote((String)resultArray[i++]);
					}
					dtoList.add(dto);
				}
			}
			return dtoList;
		}
		
	}	
	
}
