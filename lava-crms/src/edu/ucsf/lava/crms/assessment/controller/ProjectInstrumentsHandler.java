package edu.ucsf.lava.crms.assessment.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder.ListItem;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDateRangeParamHandler;
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
	
	public boolean supportsAttachedFiles() {
		return true;
	}

	// override default implementation because not dealing with a physical file; rather, the file contents
	// are generated and just put into the output stream
	public Event handleDownload(RequestContext context, Object command, BindingResult errors) throws Exception {
		Map components = ((ComponentCommand)command).getComponents();
		HttpServletResponse response = ((ServletExternalContext)context.getExternalContext()).getResponse();
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) components.get(this.getDefaultObjectName());
		if (plh.getListSize() == 0) {
//TODO: set object error msg
			return new Event(this,ERROR_FLOW_EVENT_ID);
		}

		StringBuffer csvDefaultFilename = new StringBuffer();
		String csvString = getCsvExport(context, command, errors, csvDefaultFilename); 
		response.setHeader("Content-Disposition","attachment; filename=\"" + csvDefaultFilename.toString() + "\"");
		//response.setContentType(file.getContentType());
		response.setContentLength(csvString.length());
		FileCopyUtils.copy(csvString.getBytes(),response.getOutputStream());
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
	
	/**
	 * Generate the csv export as a csv String and return it. Also, set the default csv export
	 * filename and return via the csvDefaultFilename argument.
	 * 
	 * @param context
	 * @param command
	 * @param errors
	 * @param csvDefaultFilename
	 * @return
	 * @throws Exception
	 */
	protected String getCsvExport(RequestContext context, Object command, BindingResult errors, StringBuffer csvDefaultFilename) throws Exception{
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();

		// iterate thru the ScrollablePagedListHolder, creating a csv String for each record
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) ((ComponentCommand)command).getComponents().get(this.getDefaultObjectName());
		LavaDaoFilter filter = (LavaDaoFilter) plh.getFilter();
		List<InstrumentTracking> instrList = plh.getSourceAsEntityList();
		List list = plh.getSource();
		
		// when the filter is used to specify an instrument type, it is possible that an instrument 
		// specific export could be done, e.g. with instrument specific default export filename,
		// instrument specific column headers (beyond the standard column headers), and instrument
		// specific parsing of the instrument summary to generate a .csv with individual summary values
		
		// because the source list is of InstrumentTracking objects, they can not be used for
		// this instrument specific functionality. however, since the functionality would be the same
		// for all instruments of a given type, just load the first instrument in the list and use it 
		// for this instrument specific functionality
		
		// note: already know the list is not empty, or this method would not have been called
		boolean instrSpecificExport = false;
		Instrument instrSpecificInstrument = null;
 		String filterInstrType = (String) filter.getParams().get("instrType");
 		if (!StringUtils.isEmpty(filterInstrType)) {
 			LavaDaoFilter instrFilter = InstrumentTracking.newFilterInstance(CoreSessionUtils.getCurrentUser(this.sessionManager, request));
 			Instrument instrument = (Instrument) ((ListItem) list.iterator().next()).getEntity();
			instrFilter.addIdDaoEqualityParam(instrument.getId());
 			instrSpecificInstrument = (Instrument) Instrument.MANAGER.getOne(this.instrumentManager.getInstrumentClass(instrument.getInstrTypeEncoded()), instrFilter);
 			if (instrSpecificInstrument.isFilterInstrSpecific(filterInstrType)) {
 				instrSpecificExport = true;
 			}
 		}
 		
 		//TODO: if instrSpecificInstrument is true, instead of calling getCsvSummaryData to parse the
 		// individual data values for that instrument out of the summary, just retrieve a new instrument list 
 		// of objects of that instrument type (subject to other user filter settings). this will allow
 		// us to export values that are not part of the summary and not have to parse. additionally, will
 		// not need to continually call doNext when run out of loaded items 
		
 		
 		// default csv filename
 		if (instrSpecificExport) {
 			csvDefaultFilename.append(instrSpecificInstrument.getCsvListDefaultFilename());
 		}
 		else {
 			// use the instrument tracking object
 			csvDefaultFilename.append(((Instrument)((ListItem) list.iterator().next()).getEntity()).getCsvListDefaultFilename());
 		}
 		
 		StringBuffer csvString = new StringBuffer();
		// column headers
 		csvString.append(((Instrument)((ListItem) list.iterator().next()).getEntity()).getCsvCommonColumnHeaders());
 		if (instrSpecificExport) {
 			csvString.append(instrSpecificInstrument.getCsvSummaryColumnHeaders());
 		}
 		else {
 			csvString.append(((Instrument)((ListItem) list.iterator().next()).getEntity()).getCsvSummaryColumnHeaders());
 		}
 		csvString.append("\n");
 		
		// data
 		for(Object listItem : list){
 			// when get to entities that have not been loaded yet (due to ScrollablePagedListHolder
 			// efficiency), issue next page commands until they are loaded (may take several of these,
 			// e.g. given 473 items, first 100 are loaded, 25 per page
 			// when get to item 101, the entity = null, so need to load more
 			// the first call to doNextPage will check if items 26 thru 50 are loaded, and they are, so 
 			// nothing happens, the second call checks 51 thru 75, and so on, so it will not be until
 			// the fourth call that the next items beyond 100 are loaded
 			while (((ListItem)listItem).getEntity() == null) {
 	 			this.doNextPage(context, command, errors);
 			}
 			
 			InstrumentTracking instrument = (InstrumentTracking) ((ListItem)listItem).getEntity();
 			csvString.append(instrument.getCsvCommonData());
 			
 	 		if (instrSpecificExport) {
 	 			csvString.append(instrSpecificInstrument.getCsvSummaryData(instrument));
 	 		}
 	 		else {
	 			csvString.append(((Instrument)((ListItem) list.iterator().next()).getEntity()).getCsvSummaryData(instrument));
 	 		}
	 		csvString.append("\n");
 		}

 		return csvString.toString();
	}
	
	
}
