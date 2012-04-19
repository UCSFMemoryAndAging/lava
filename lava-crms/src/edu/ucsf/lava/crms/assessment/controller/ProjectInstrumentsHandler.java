package edu.ucsf.lava.crms.assessment.controller;

import java.util.ArrayList;
import java.util.Iterator;
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
import edu.ucsf.lava.crms.assessment.model.InstrumentConfig;
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
	
	
	/**
	 * Exclude hidden instruments.
	 */
	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter) {
		Map<String,InstrumentConfig> instrConfigMap = instrumentManager.getInstrumentConfig();
		List<String> dxInstrTypes = null;
		for (InstrumentConfig instrConfig : instrConfigMap.values()) {
			if (instrConfig.getHidden()) {
				daoFilter.addDaoParam(daoFilter.daoNot(daoFilter.daoEqualityParam("instrType", instrConfig.getInstrType())));
			}
		}
		return daoFilter;
	}
	
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model) {
		model.put("instrumentConfig", instrumentManager.getInstrumentConfig());
		return super.addReferenceData(context, command, errors, model);
	}
	
	/**
	 * Generate the export as a String and return it. Also, set the default export
	 * filename and return via the defaultFilename argument.
	 * 
	 * This is more or less a stopgap until the LAVA Query tool is ready.
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
 			InstrumentTracking firstInstrTrackingObj = (InstrumentTracking) ((ListItem)plh.getSource().iterator().next()).getEntity();
			instrFilter.addIdDaoEqualityParam(firstInstrTrackingObj.getId());
 			instrSpecificInstrument = (Instrument) Instrument.MANAGER.getOne(this.instrumentManager.getInstrumentClass(firstInstrTrackingObj.getInstrTypeEncoded()), instrFilter);
 			if (instrSpecificInstrument.isFilterInstrSpecific(filterInstrType)) {
 				instrSpecificExport = true;
 			}
 		}
 		
 		StringBuffer csvString = new StringBuffer();
		// obtain a full list of either the instrument specfic instrument objects, or if the list is
		// not instrument specific, a list of InstrumentTracking objects (while ScrollablePagedListHolder
		// already has a List<InstrumentTracking> it may not be completely populated since only the first
		// N list objects are retrieved for performance purposes)
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
 		if (instrSpecificExport) {
 			fullInstrList = Instrument.MANAGER.get(instrSpecificInstrument.getClass(), instrFilter);
 		}
 		else {
 			fullInstrList = Instrument.MANAGER.get(InstrumentTracking.class, instrFilter);
 		}
 		// iterate over the instrument list. for each instrument, append a CSV record
 		boolean firstRecord = true;
		for (Instrument instr : fullInstrList) {
			if (firstRecord) {
		 		// default export filename
	 			defaultFilename.append(instr.getExportListDefaultFilename()).append(".csv");
		 		
	 			// column headers
	 	 		csvString.append(StringUtils.arrayToCommaDelimitedString(instr.getExportCommonColHeaders()));
 	 			csvString.append(",").append(StringUtils.arrayToCommaDelimitedString(instr.getExportSummaryColHeaders()));
	 	 		csvString.append("\n");
	 	 		
	 	 		firstRecord = false;
			}
 			csvString.append(StringUtils.arrayToCommaDelimitedString(instr.getExportCommonData()));
 			csvString.append(",").append(StringUtils.arrayToCommaDelimitedString(instr.getExportSummaryData()));
	 		csvString.append("\n");
		}

 		return csvString.toString();
	}
	
	
}
