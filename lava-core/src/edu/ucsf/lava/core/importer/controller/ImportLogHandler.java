package edu.ucsf.lava.core.importer.controller;

import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.controller.BaseEntityComponentHandler;
import edu.ucsf.lava.core.controller.ComponentCommand;
import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.file.model.ImportFile;
import edu.ucsf.lava.core.file.model.LavaFile;
import edu.ucsf.lava.core.importer.model.ImportLog;

/**
 * ImportLogHandler
 * 
 * Handles the CRUD for ImportLog. Only view is supported for ImportLog, i.e. no add, edit, delete
 * as each ImportLog is created by the import action, and being a log, is not editable thereafter.
 * 
 * @author ctoohey
 *
 */
public class ImportLogHandler extends BaseEntityComponentHandler {

	public ImportLogHandler() {
		super();
		setHandledEntity("importLog", ImportLog.class);
		this.setSupportsAttachedFiles(true);
	}
	
	

	@Override
	public Map getBackingObjects(RequestContext context, Map components) {
		Map backingObjects = super.getBackingObjects(context, components);
		// put individual log messages in a component for the view to display
	 	ImportLog importLog = (ImportLog) backingObjects.get("importLog");
		ScrollablePagedListHolder logMessagesListHolder = new ScrollablePagedListHolder();
		logMessagesListHolder.setPageSize(250);
		logMessagesListHolder.setSourceFromEntityList(importLog.getMessages());
		backingObjects.put("importLogMessages", logMessagesListHolder);
		return backingObjects;
	}


	/**
	 * Override so that the download event downloads the importLog data file.
	 */
	protected LavaFile getLavaFileBackingObject(RequestContext context, Map components, BindingResult errors) throws Exception{
		ImportLog importLog = (ImportLog) components.get(getDefaultObjectName());
		if(ImportFile.class.isAssignableFrom(importLog.getDataFile().getClass())){
			return (ImportFile)importLog.getDataFile();
		}
		return null;
	}
	
	protected void setDefaultHandledEvents(String defaultObjectName){
		super.setDefaultHandledEvents(defaultObjectName);
		
		// add special handling 
		this.handledEvents.add("importLogMessages__prevPage");
		this.handledEvents.add("importLogMessages__nextPage");
		this.handledEvents.add("importLogMessages__recordNav");
		this.handledEvents.add("importLogMessages__pageSize");
	}
	
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		String event = ActionUtils.getEventName(context);
		String eventComponent = ActionUtils.getEventObject(context);

		if(event.equals("prevPage")){
			return this.handlePrevPageEvent(context,command,errors,eventComponent);
		}
		else if(event.equals("nextPage")){
			return this.handleNextPageEvent(context,command,errors,eventComponent);
		}
		else if(event.equals("recordNav")){
			return this.handleRecordNavEvent(context,command,errors,eventComponent);
		}
		else if(event.equals("pageSize")){
			return this.handlePageSizeEvent(context,command,errors,eventComponent);
		}

		errors.addError(new ObjectError(errors.getObjectName(),	new String[]{"flowEventNotHandled.command"}, new Object[] {event}, ""));
		return new Event(this,ERROR_FLOW_EVENT_ID);
	}

	
	// detail record EVENT HANDLING METHODS
		// these support page lists of instrument detail records, where the listing of detail records only requires
		// basic navigation, i.e. no filtering and sorting such that a separate list handler would be needed for the
		// detail component
		// note: the detail records are added as the "instrumentDetails" component of the ComponentCommand structure
		// in the instrument specific FileLoaded bean's loadFile method
		
		public Event handlePrevPageEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			return doPrevPage(context,command,errors,component);
		}
							
		//The default prev page action
		protected Event doPrevPage(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			ComponentCommand componentCommand = (ComponentCommand) command;
			ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);

			// the plh does not have a sourceProvider; instead, the source has been set to a List. so there
			// is no database interaction to retrieved items in the list, and thus no need to initPageList here
			// or catch exceptions
			plh.previousPage();
			plh.setPageHolder(plh.getPage()); // keep the holder in sync				
			return new Event(this,SUCCESS_FLOW_EVENT_ID);
		}

		public Event handleNextPageEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			return doNextPage(context,command,errors,component);
		}
								
		//The default next page action
		protected Event doNextPage(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			ComponentCommand componentCommand = (ComponentCommand) command;
			ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);
			plh.nextPage();
			plh.setPageHolder(plh.getPage()); // keep the holder in sync		
			return new Event(this,SUCCESS_FLOW_EVENT_ID);
		}
		
		public Event handleRecordNavEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			return doRecordNav(context,command,errors,component);
		}
								
		//The default record navigationnext page action
		protected Event doRecordNav(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			ComponentCommand componentCommand = (ComponentCommand) command;
			ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);

	        // the pageHolder must be assigned to the page now. this is so that the page value is                                        
	        // not changed until after binding, because if binding editable list items that are in                                       
	        // pageList, binding could fail if the size of pageList changes                
			plh.setPage(plh.getPageHolder());
			return new Event(this,SUCCESS_FLOW_EVENT_ID);
		}

		public Event handlePageSizeEvent(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			return doPageSize(context,command,errors,component);
		}
								
		//The default page size action
		protected Event doPageSize(RequestContext context, Object command, BindingResult errors, String component) throws Exception{
			ComponentCommand componentCommand = (ComponentCommand) command;
			ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(component);

	        // the pageSizeHolder must be assigned to the pageSize now. this is so that the pageSize                                     
	        // value is not changed until after binding, because if binding editable list items that                                     
			// are in pageList, binding could fail if the size of pageList changes                     
			plh.setPageSize(plh.getPageSizeHolder());
			// this is a bit tricky. getPage() actually sets the correct page number based on the pageSize,
			// so it must be called so set the page. then set pageHolder so the view will display the correct page
			plh.setPageHolder(plh.getPage());
			
			return new Event(this,SUCCESS_FLOW_EVENT_ID);
		}

}
