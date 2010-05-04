package edu.ucsf.lava.crms.assessment.controller;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder.ListItem;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.crms.assessment.model.Instrument;
import edu.ucsf.lava.crms.assessment.model.InstrumentTracking;
import edu.ucsf.lava.crms.controller.CrmsCalendarComponentHandler;
import edu.ucsf.lava.crms.session.CrmsSessionUtils;

public class ProjectInstrumentsHandler extends CrmsCalendarComponentHandler {

	public ProjectInstrumentsHandler() {
		super();
		this.setHandledList("projectInstruments","instrument");
		this.setDatePropertyName("dcDate");
		this.setEntityForStandardSourceProvider(InstrumentTracking.class);
	}

	public LavaDaoFilter prepareFilter(RequestContext context, LavaDaoFilter filter, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		filter.addDefaultSort("dcDate",true);
		filter.addDefaultSort("patient.fullNameRevNoSuffix", true);
		filter.addParamHandler(new LavaDateRangeParamHandler("dcDate"));
		filter.addParamHandler(new LavaDateRangeParamHandler("deDate"));
		filter.addDefaultSort("instrType",true);
		filter.setAlias("visit", "visit");
		filter.setAlias("patient", "patient");
		return CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components){
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		CrmsSessionUtils.setFilterProjectContext(sessionManager,request,filter);
	}
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		model.put("instrumentConfig", instrumentManager.getInstrumentConfig());
		return super.addReferenceData(context, command, errors, model);
	}
	
	/**
	 * Generate the export as a String and return it. Also, set the default export
	 * filename and return via the defaultFilename argument.
	 * 
	 * @param context
	 * @param command
	 * @param errors
	 * @param defaultFilename
	 * @return
	 * @throws Exception
	 */
	protected String getExportContent(RequestContext context, Object command, BindingResult errors, StringBuffer defaultFilename) throws Exception{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		
		// for now, only .csv export is supported

		// iterate thru the ScrollablePagedListHolder, creating a csv String for each record
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		LavaDaoFilter filter = (LavaDaoFilter) plh.getFilter();
		List<InstrumentTracking> instrList = plh.getSourceAsEntityList();
		List list = plh.getSource();
		
		// when the Filter is used to specify an instrument type, such that all of the instruments in
		// the list are the same (or related) it is possible that an instrument specific export could 
		// be done, with instrument specific default export filename, instrument specific column headers 
		// (beyond the standard column headers), and instrument specific summary properties

		// if an instrument specific export can not be done, a generic export is done, exporting the 
		// summary property for each instrument, along with the common properties from InstrumentTracking
		
		
		// rather than iterating thru the List<InstrumentTracking> and checking each item to determine
		// if they are all of same type (which could require additional retrieval for list items that
		// have not been loaded yet), easier just to see if the first item in the list matches the
		// Filter "Instrument Type". instruments define a method to determine this and will only
		// return true if the "Instrument Type" is specific enough to denote just that type of
		// instrument (and possibly aliases)
		
		// note: already know the list is not empty, or this method would not have been called
		boolean instrSpecificExport = false;
		Instrument instrSpecificInstrument = null;
 		String filterInstrType = (String) filter.getParams().get("instrType");
 		if (!org.apache.commons.lang.StringUtils.isEmpty(filterInstrType)) {
 			LavaDaoFilter instrFilter = InstrumentTracking.newFilterInstance(CoreSessionUtils.getCurrentUser(this.sessionManager, request));
 			Instrument instrument = (Instrument) ((ListItem) list.iterator().next()).getEntity();
			instrFilter.addIdDaoEqualityParam(instrument.getId());
 			instrSpecificInstrument = (Instrument) Instrument.MANAGER.getOne(this.instrumentManager.getInstrumentClass(instrument.getInstrTypeEncoded()), instrFilter);
 			if (instrSpecificInstrument.isFilterInstrSpecific(filterInstrType)) {
 				instrSpecificExport = true;
 			}
 		}
 		
 		// default export filename
 		if (instrSpecificExport) {
 			defaultFilename.append(instrSpecificInstrument.getExportListDefaultFilename()).append(".csv");
 		}
 		else {
 			// use the instrument tracking object
 			defaultFilename.append(((Instrument)((ListItem) list.iterator().next()).getEntity()).getExportListDefaultFilename()).append(".csv");
 		}
 		

 		StringBuffer csvString = new StringBuffer();
 		
		// column headers
 		csvString.append(StringUtils.arrayToCommaDelimitedString(((Instrument)((ListItem) list.iterator().next()).getEntity()).getExportCommonColHeaders()));
 		if (instrSpecificExport) {
 			csvString.append(",").append(StringUtils.arrayToCommaDelimitedString(instrSpecificInstrument.getExportSummaryColHeaders()));
 		}
 		else {
 			csvString.append(",").append(StringUtils.arrayToCommaDelimitedString(((Instrument)((ListItem) list.iterator().next()).getEntity()).getExportSummaryColHeaders()));
 		}
 		csvString.append("\n");
		
 		// column data
 		List<Instrument> fullInstrList;
		LavaDaoFilter instrFilter = EntityBase.newFilterInstance(getCurrentUser(request));
		instrFilter = prepareFilter(context,instrFilter,((ComponentCommand)command).getComponents());
		// transfer the sorts and daoParams over to get user settings
		for (LavaDaoParam daoParam : filter.getDaoParams()) {
			instrFilter.addDaoParam(daoParam);
		}
		for (Iterator iter = filter.getSort().entrySet().iterator(); iter.hasNext(); ) {
			Entry entry = (Entry) iter.next();
			instrFilter.addSort((String)entry.getKey(), ((Boolean)entry.getValue()).booleanValue());
		}
		// obtain a full list of either the instrument specfic instrument objects, or if the list is
		// not instrument specific, and list of InstrumentTracking objects (while ScrollablePagedListHolder
		// already has a List<InstrumentTracking> it may not be completely populated since only the first
		// N list objects are retrieved for performance purposes)
 		if (instrSpecificExport) {
 			fullInstrList = Instrument.MANAGER.get(instrSpecificInstrument.getClass(), instrFilter);
 		}
 		else {
 			fullInstrList = Instrument.MANAGER.get(InstrumentTracking.class, instrFilter);
 		}
 		// for each instrument, append a CSV record
		for (Instrument instr : fullInstrList) {
 			csvString.append(StringUtils.arrayToCommaDelimitedString(instr.getExportCommonData()));
 			csvString.append(",").append(StringUtils.arrayToCommaDelimitedString(instr.getExportSummaryData()));
	 		csvString.append("\n");
		}

 		return csvString.toString();
	}
	
	
}
