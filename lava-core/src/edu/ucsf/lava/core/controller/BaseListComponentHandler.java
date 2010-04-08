package edu.ucsf.lava.core.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.support.SortDefinition;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.EntityBase;
import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.session.CoreSessionUtils;

/*
 * This class serves as the base class for classes that handle scrollable list -based "components" that
 * are included in views.  
 * 
 */
public abstract class BaseListComponentHandler extends LavaComponentHandler {

	protected int pageSize = 10;
	protected ScrollableListSourceProvider sourceProvider; // every handler subclass must set this
	protected Set<String> listedEntityNames = new HashSet<String>();
	
	public BaseListComponentHandler() {
		super();
		this.setDefaultMode("lv");
		defaultEvents = new ArrayList(Arrays.asList(new String[]{"applyFilter","clearFilter","toggleFilter","clearSort","prevPage","nextPage",
				"recordNav","pageSize","refresh","refreshReturnToPage","close","export"}));
		if(supportsAttachedFiles()){
			defaultEvents.add("download");
		}
		authEvents = new ArrayList(); // none of the list events require explicit permission
	}

	/**
	 * Sets the name of the handled list and the "metadata" name of the entity
	 * that is contained in the list. 
	 * @param name 
	 * @param entityName Used to lookup metadata to add lookup lists to the model
	 */
	protected void setHandledList(String name,String entityName){
		setHandledList(name,ScrollablePagedListHolder.class, entityName);
	}
	
	protected void setHandledList(String name, Class clazz, String entityName){
		Map<String,Class> objectMap = new HashMap<String,Class>();
		objectMap.put(name,clazz);
		addListedEntityName(entityName);
		this.setHandledObjects(objectMap);
	}
	
	
	protected void setEntityForStandardSourceProvider(Class entityClass){
		sourceProvider = new BaseListSourceProvider(this,entityClass);
		}
	
	

	/* 
	 * override default behavior to take any objectName_sort_[fieldname] event for this list
	 */
	@Override
	public boolean handlesEvent(RequestContext context) {
		String event = ActionUtils.getEventName(context);
		String eventObject = ActionUtils.getEventObject(context);
		if (event.startsWith("sort_")){
			for (String handledObject: handledObjects.keySet()){
				if (StringUtils.equalsIgnoreCase(handledObject,eventObject)){
					return true;
				}
			}
		}
		return super.handlesEvent(context);
	}


	public Set<String> getListedEntityNames() {
		return listedEntityNames;
	}

	public void setListedEntityNames(Set<String> listedEntityNames) {
		this.listedEntityNames = listedEntityNames;
	}
	
	public void addListedEntityName(String entityName){
		this.listedEntityNames.add(entityName);
	}

	public abstract LavaDaoFilter extractFilterFromRequest(RequestContext context, Map components);
	
	// opportunity to update filter from context on a refresh, while keeping other existing
	// filter settings intact, e.g. project lists should implement this to re-set the project 
	// context in the filter on a project context change
	public abstract void updateFilterFromContext(LavaDaoFilter filter, RequestContext context, Map components);

	
	//AUTHORIZATION METHODS
	
	public Event authorizationCheck(RequestContext context, Object command) throws Exception {
		HttpServletRequest request = ((ServletExternalContext)context.getExternalContext()).getRequest();
		Action action = actionManager.getCurrentAction(request);
		AuthUser user = CoreSessionUtils.getCurrentUser(sessionManager,request);

		if (!authManager.isAuthorized(user,action,(ScrollablePagedListHolder)null)) {
			if (context.getFlowExecutionContext().getActiveSession().isRoot()) {
				throw new RuntimeException(this.getMessage(COMMAND_AUTHORIZATION_ERROR_CODE));
			}else{
				CoreSessionUtils.addFormError(sessionManager, request, new String[]{COMMAND_AUTHORIZATION_ERROR_CODE}, null);
				return new Event(this,this.UNAUTHORIZED_FLOW_EVENT_ID);
			}
		}
		
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	
	public Event initMostRecentViewState(RequestContext context) throws Exception {
		// the most recent view state is used when a view state transitions to a subflow which is 
		// unauthorized such that the subflow is terminated and control returns to the parent flow. the
		// parent flow then uses the "mostRecentViewState" attribute in flow scope to determine which
		// view state to return to. initially, "mostRecentViewState" should be set to the initial
		// view state of the flow, which for list flows is named after the flow event, aka action, 
		// aka mode, e.g. "view" for standard lists
		String initialViewState = ActionUtils.getFlowMode(context.getActiveFlow().getId());
		context.getFlowScope().put("mostRecentViewState", initialViewState);
		return new Event(this,this.SUCCESS_FLOW_EVENT_ID);
	}
	

	
	//LIST COMMAND OBJECT METHODS
	
	
	/**
	 * This base method returns the main entity list.  
	 * 
	 * Also, if there are additional objects to load, then override calling the superclass first to load the primary object. 
	 */
	public Map getBackingObjects(RequestContext context, Map components) {
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		Map backingObjects = new HashMap<String,Object>();
		ScrollablePagedListHolder entityList = new ScrollablePagedListHolder();  //By definition a List Component Handler has a paged list as the default object
		entityList.setSourceProvider(getSourceProvider());
		entityList.setLocale(Locale.US);
		entityList.setFilter(extractFilterFromRequest(context,components));
		entityList.setPageSize(pageSize);
		entityList.doRefresh();
		backingObjects.put(this.getDefaultObjectName(), entityList);
		return backingObjects;
	}


	
	// this is called when a parent flow resumes after a subflow has completed, so that
	// any changes made in the subflow(s) that may affect the backing objects in the parent
	// flow are reflected
	// note: this is not an event handler. it is a flow action state action (initiated by returning
	// from a subflow), which is why it is a separate method from handleRefreshEvent. also, the
	// FormAction method that calls this always returns success, because once back in the parent
	// flow it is too late to prevent returning from the subflow by returning an error. any exceptions
	// will be displayed as error messages in the parent view.
	public void refreshBackingObjects(RequestContext context, Object command, BindingResult errors) throws Exception {
		doRefreshReturnToPage(context,command,errors);
	}

	
	protected SortDefinition getSortDefinition(HttpServletRequest request){
		return null;
	}

	
    /*
     * The oportunity for this handler to modify the controller binder default action is to add
     * any required fields to the binder.  This is made more complicated by the multiple component 
     * support that we need to have now, so we need to take action to preserve existing required fields
     * specified by other components. 
     */
		
	 public void initBinder(RequestContext context, Object command, DataBinder binder) {
		 // only set validation for required fields on events where displaying field errors makes sense, i.e. 
		 // an add or save event
		String event = ActionUtils.getEventName(context);
		if(event.equals("add") || event.equals("save")) {
			if(requiredFields != null && requiredFields.length != 0){
				List existing = Arrays.asList(binder.getRequiredFields());
				HashSet fields = new HashSet(existing);
				//TODO: iterate thru requiredFields list, and converting each to include the full data structure,
				// e.g. "kitNumber" to components['someListComponent'].pageList[0].fieldA thru components['someListComponent'].pageList[n].fieldA
				// where n is the length of the components['studyKitTracking'].pageList List
				// may want to add beginIndex, endIndex to allow specifying the rows in which the field should be required
				fields.addAll(Arrays.asList(getRequiredFields()));
				binder.setRequiredFields((String[])fields.toArray());
			}
		}
	}
	
	
	public void prepareToRender(RequestContext context, Object command, BindingResult errors) {
		// basically set list components view and mode for a flow with "view" mode, regardless
		// can not rely on flowMode, because when it is "edit", can not distinguish between a flow 
		// for edit list, and a flow for edit entity with a view list component
		// so if a list is an edit list, it's handler should override this method and set mode to "le"
		// and view to "edit"
		// alternatively, could check flow id, states and events to determine when list component
		// view should be "view" vs. "edit"
		HttpServletRequest request =  ((ServletExternalContext)context.getExternalContext()).getRequest();
		setComponentMode(request, getDefaultObjectName(), "lv");
		setComponentView(request, getDefaultObjectName(), "list");

		// for edit list use:
		//setComponentMode(request, getDefaultObjectName(), "le");
		//setComponentView(request, getDefaultObjectName(), "edit");
	}
		
	
	public Map addReferenceData(RequestContext context, Object command, BindingResult errors, Map model)
	{
		StateDefinition state = context.getCurrentState();
		
	 	super.addReferenceData(context, command, errors, model);
		
	 	//add static lists for the entity in the list
	 	for(String entityName : listedEntityNames){
	 		this.addListsToModel(model, listManager.getStaticListsForEntity(entityName));
	 	}
	 	
	 	// if entering a report generated  view state, need to add the list as the report dataSource. since
		// List is a Collection, it can serve as the dataSource directly
	 	// note that exporting a list, i.e. download, to a .csv file is done via the download event mechanism
	 	// rather than via reporting, e.g. see ProjectInstrumentsHandler
		if (state.getId().equals("print")) {
			model.put("listReportDataSource", ((ScrollablePagedListHolder)((ComponentCommand)command).getComponents().get(getDefaultObjectName())).getSourceAsEntityList());
			model.put("format", "pdf");
	 		// pass the handler itself as a report parameter
	 		model.put("handler", this);
		}
			
		return model;
	}
		
	
		
	public Event handleFlowEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		String event = ActionUtils.getEventName(context);
		// note: do not use "event = ActionUtils.getEvent(request)" because it is possible for a flow
		//  to set an overrideEvent which will be different from the event request parameter
		
		// handleDownload is called directly from the FormAction (if a handler overrides
		// supportsAttachedFiles() and returns true), so no need to handle download event here
		
		if(event.equals("applyFilter")){
			return this.handleApplyFilterEvent(context,command,errors);
		}
		else if(event.equals("clearFilter")){
			return this.handleClearFilterEvent(context,command,errors);
		}	
		else if(event.equals("toggleFilter")){
			return this.handleToggleFilterEvent(context,command,errors);
		}
		else if(event.equals("clearSort")){
			return this.handleClearSortEvent(context,command,errors);
		}	
		else if(event.startsWith("sort_")){
			return this.handleSortEvent(context,command,errors);
		}	
		else if(event.equals("prevPage")){
			return this.handlePrevPageEvent(context,command,errors);
		}	
		else if(event.equals("nextPage")){
			return this.handleNextPageEvent(context,command,errors);
		}	
		else if(event.equals("recordNav")){
			return this.handleRecordNavEvent(context,command,errors);
		}	
		else if(event.equals("pageSize")){
			return this.handlePageSizeEvent(context,command,errors);
		}	
		else if(event.startsWith("close")){
			return this.handleCloseEvent(context,command,errors);
		}	
		else if(event.equals("refresh")){
			return this.handleRefreshEvent(context,command,errors);
		}	
		else if(event.equals("refreshReturnToPage")){
			return this.handleRefreshReturnToPageEvent(context,command,errors);
		}	
		else {
			return this.handleCustomEvent(context,command,errors);
		}
	}
	
	
//CUSTOM EVENT METHOD

	//override this in subclass to handle custom action buttons. 
	protected Event handleCustomEvent(RequestContext context, Object command, BindingResult errors)
     throws Exception{
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

//CLOSE EVENT METHOD 
	public Event handleCloseEvent(RequestContext context, Object command, BindingResult errors){
		return doClose(context, command,errors);
	}
		
	//Override this in subclass to provide custom close handler 
	protected Event doClose(RequestContext context, Object command, BindingResult errors){
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

//VIEW EVENT METHODS
	
	public Event handleViewEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doView(context,command,errors);
	}
	
	//Override this in subclass to provide custom "view" handler 
	protected Event doView(RequestContext context, Object command, BindingResult errors) throws Exception{
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}

	
//EDIT EVENT METHODS
	
	public Event handleEditEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doEdit(context,command,errors);
	}
	
	//Override this in subclass to provide custom edit handler 
	protected Event doEdit(RequestContext context, Object command, BindingResult errors) throws Exception{
		return new Event(this,SUCCESS_FLOW_EVENT_ID);
	}
	
//APPLY FILTER EVENT METHODS
	public Event handleApplyFilterEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doApplyFilter(context,command,errors);
	}
	
	//The default apply filter action 
	protected Event doApplyFilter(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		// because SourceProvider is part of command object and must be Serializable so that web flow
		// continuations work, yet its listHandler field is transient because certain superclasses beyond
		// the scope of our app are not Serializable (e.g. HibernateDaoSupport class), the listHandler
		// field needs to be explicitly reset before each list refresh, because upon deserialization it
		// is null
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.doRefresh();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}
		
		
//CLEAR FILTER EVENT METHODS
		
	public Event handleClearFilterEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doClearFilter(context,command,errors);
	}
		
	//The default clear filter action
	protected Event doClearFilter(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		plh.clearFilterParams();
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.doRefresh();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}
			
//TOGGLE FILTER EVENT METHODS
			
	public Event handleToggleFilterEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doToggleFilter(context,command,errors);
	}
		
	//The default clear filter action
	protected Event doToggleFilter(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		plh.toggleFilterOn();
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		if(!plh.getFilterOn()){
			plh.clearFilterParams();
			((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
			try {
				plh.doRefresh();
			}
			catch (Exception e) {
				addObjectErrorForException(errors, e);
				returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
			}
		}
		return returnEvent;
	}
				

//CLEAR SORT EVENT METHODS
				
	public Event handleClearSortEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doClearSort(context,command,errors);
	}
				
	//The default clear sort action
	protected Event doClearSort(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		((LavaDaoFilter)plh.getFilter()).clearSort();
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.doRefresh();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}
					
//SORT EVENT METHODS
					
	public Event handleSortEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doSort(context,command,errors);
	}
					
	//The default sort action
	protected Event doSort(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		String sort = ActionUtils.getEventName(context).substring(5);
		((LavaDaoFilter)plh.getFilter()).setSort(sort);
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.doRefresh();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}
	
//PREV PAGE EVENT METHODS
						
	public Event handlePrevPageEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doPrevPage(context,command,errors);
	}
						
	//The default prev page action
	protected Event doPrevPage(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());

		plh.previousPage();
		plh.setPageHolder(plh.getPage()); // keep the holder in sync 
		
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		// restore the listHandler field due to web flow continuations deserialization, as the
		// jsp invokes getPageList which calls loadElements which needs listHandler
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			// this must be called now while sourceProvider's transient listHandler field has a value,
			// rather than by the jsp, because the jsp is processed in a separate request due to POST-REDIRECT-GET 
			// and uses a deserialized command object with null listHandler field (because of web flow
			// continuations which serializes state for each request)
			plh.initPageList();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}

//NEXT PAGE EVENT METHODS
							
	public Event handleNextPageEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doNextPage(context,command,errors);
	}
							
	//The default next page action
	protected Event doNextPage(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		
		plh.nextPage();
		plh.setPageHolder(plh.getPage()); // keep the holder in sync 
		
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		// restore the listHandler field due to web flow continuations deserialization, as the
		// jsp invokes getPageList which calls loadElements which needs listHandler
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.initPageList();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}

	public int getPageSize() {
		return pageSize;
	}

	/** 
	 * Used to set the page size on the PagedListHolder. Handlers can override the default by
	 * calling setPageSize in their constructor. They should not call it otherwise because it is an instance
	 * property, and therefore not thread-safe. But it is ok to set in the constructor since handlers
	 * are singletons.
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


//RECORDNAV EVENT METHODS
	public Event handleRecordNavEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doRecordNav(context,command,errors);
	}
							
	//The default recordNav action, i.e. user navigates to a different set of records
	protected Event doRecordNav(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		
        // the pageHolder must be assigned to the page now. this is so that the page value is                                        
        // not changed until after binding, because if binding editable list items that are in                                       
        // pageList, binding could fail if the size of pageList changes                
		plh.setPage(plh.getPageHolder());
		
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		// restore the listHandler field due to web flow continuations deserialization, as the
		// jsp invokes getPageList which calls loadElements which needs listHandler
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			plh.initPageList();
			
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		
		// nothing else needs to be done here, because the record nav binds the page number on the
		// the ScrollablePagedListHolder (setPage) which determines the set of records returned by getPageList
		return returnEvent;
	}

	public Event handlePageSizeEvent(RequestContext context, Object command, BindingResult errors) throws Exception{
		return doPageSize(context,command,errors);
	}

	//PAGE SIZE EVENT
	//The default page size action, i.e. user changes the number of items per page to display
	protected Event doPageSize(RequestContext context, Object command, BindingResult errors) throws Exception{
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		
        // the pageSizeHolder must be assigned to the pageSize now. this is so that the pageSize                                     
        // value is not changed until after binding, because if binding editable list items that                                     
		// are in pageList, binding could fail if the size of pageList changes                     
		plh.setPageSize(plh.getPageSizeHolder());
		// setPageSize could change page to 0, so need to set page to bound value
		plh.setPage(plh.getPageHolder());
		
		// restore the listHandler field due to web flow continuations deserialization, as the
		// jsp invokes getPageList which calls loadElements which needs listHandler
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
		try {
			// note that changing the pageSize may require changing page, if the new pageSize 
			// results in fewer pages than the current value of page. this happens in initPageList.
			plh.initPageList();
			//need to rebuild the navigation because the page size has changes
			plh.getRecordNavigation(true);
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}

		// setting the page size changes the number of pages, so if the current page would be
		// outside of this new number, initPageList will have updated the page, so update pageHolder
		plh.setPageHolder(plh.getPage());
		
		// nothing else needs to be done here, because the submit binds the page size on the
		// the ScrollablePagedListHolder (setPage) which determines the number of records returned by getPageList
		
		return returnEvent;
	}

//REFRESH EVENT 
								
	public Event handleRefreshEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doRefresh(context,command,errors);
	}

	//Override this in subclass to provide custom "view" handler 
	protected Event doRefresh(RequestContext context, Object command, BindingResult errors) throws Exception {
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		updateFilterFromContext((LavaDaoFilter)plh.getFilter(), context, componentCommand.getComponents());
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		try {
			// because SourceProvider is part of command object and must be Serializable so that web flow
			// continuations work, yet its listHandler field is transient because certain superclasses beyond
			// the scope of our app are not Serializable (e.g. HibernateDaoSupport class), the listHandler
			// field needs to be explicitly reset before each list refresh, because upon deserialization it
			// is null
			((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
			plh.doRefresh();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			returnEvent = new Event(this,ERROR_FLOW_EVENT_ID);
		}
		return returnEvent;
	}

// REFRESH RETURN TO PAGE EVENT
	public Event handleRefreshReturnToPageEvent(RequestContext context, Object command, BindingResult errors) throws Exception {
		return doRefreshReturnToPage(context,command,errors);
	}

	//Override this in subclass to provide custom "view" handler 
	protected Event doRefreshReturnToPage(RequestContext context, Object command, BindingResult errors) throws Exception {
		ComponentCommand componentCommand = (ComponentCommand) command;
		ScrollablePagedListHolder plh = (ScrollablePagedListHolder) componentCommand.getComponents().get(this.getDefaultObjectName());
		int currentPage = plh.getPage();
		if (currentPage > plh.getPageCount() && currentPage > 0) {
			currentPage--;
		}
		updateFilterFromContext((LavaDaoFilter)plh.getFilter(), context, componentCommand.getComponents());
		Event returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		try {
			((BaseListSourceProvider)plh.getSourceProvider()).setListHandler(this);
			plh.doRefresh();
			plh.setPage(currentPage);
			plh.initPageList();
		}
		catch (Exception e) {
			addObjectErrorForException(errors, e);
			// return SUCCESS even if there is an error, because this event is handled after transitioning
			// from an entity subflow back to a list flow, so in case of error here on refreshing the list, do 
			// not want to remain in the entity subflow; rather, want to return to the list flow and present
			// the error, which has been added to the command object for the list flow, not the entity subflow
			returnEvent = new Event(this,SUCCESS_FLOW_EVENT_ID);
		}
		
		// if this is a list with selected items, restore the items that were selected before the refresh. 
		// this only applies when this is a refresh after returning from a group subflow, in which case
		// the group subflow supplies the group via an output mapper
		
		List<LavaEntity> group = (List<LavaEntity>) context.getFlowScope().get(BaseGroupComponentHandler.GROUP_MAPPING);
		if (group != null) {
			for (LavaEntity entity : group) {
				// iterate thru the list of selected items, which has nothing selected at this point,
				// and select each entity that is in the group
                for (ScrollablePagedListHolder.ListItem listItem : (List<ScrollablePagedListHolder.ListItem>) plh.getSource()) {
                	if (entity.getId().equals(((LavaEntity)listItem.getEntity()).getId())) {
                		listItem.setSelected(true);
                	}
                }
			}
		}
		
		return returnEvent;
	}


	public ScrollableListSourceProvider getSourceProvider() {
		return this.sourceProvider;
	}
	
	public void setSourceProvider(BaseListSourceProvider sourceProvider) {
		this.sourceProvider = sourceProvider;
	}
	
	/*
	 * Override this method to modify the filter prior to conversion of view layer params to daoParams
	 */
	public LavaDaoFilter onPreFilterParamConversion(LavaDaoFilter daoFilter){
		return daoFilter;		
	}
	
	/*
	 * Override this method to modify the filter after conversion of view layer params to daoParams
	 * and before the loading of list elements
	 */
	public LavaDaoFilter onPostFilterParamConversion(LavaDaoFilter daoFilter){
		return daoFilter;
	}
	
	public List getList(Class entityClass, LavaDaoFilter daoFilter){
		return EntityBase.MANAGER.get(entityClass,daoFilter);
	}
	
	/**
	 * Instances of subclasses of this class are set as the source provider field of a 
	 * ScrollablePagedListHolder object to provider the list data. This class must be separated 
	 * out into a separate class from the handler class because ScrollablePagedListHolder must
	 * be Serializable as it is a component command object which gets serialized between requests 
	 * as part of the flow execution to support web flow continuations. 
	 */
	public static class BaseListSourceProvider implements ScrollableListSourceProvider, Serializable {
		// the listHandler field is made transient because handlers are not Serializable because they
		// involve certain Spring classes and fields which are not Serializable, and they have services
		// as fields which in turn have DAO classes as fields which have Spring Hibernate classes which
		// are not Serializable, e.g. HibernateDaoSupport and its hibernateTemplate field
		// the key to making this work is that this listHandler field's value must be restored whenever
		// it is needed because it is set to null when the flow execution context is serialized and
		// then deserialized, so in the BaseListComponentHandler prior to whenever a list is refreshed,
		// the listHandler field is set again
		protected transient BaseListComponentHandler listHandler;
		protected Class entityClass;
		public BaseListSourceProvider(BaseListComponentHandler listHandler) {
			this.listHandler = listHandler;
		}

		public BaseListSourceProvider(BaseListComponentHandler listHandler, Class entityClass) {
			this(listHandler);
			this.entityClass = entityClass;
		}
		public BaseListComponentHandler getListHandler() {
			return listHandler;
		}

		public void setListHandler(BaseListComponentHandler listHandler) {
			this.listHandler = listHandler;
		}
	
		
		
		
		public List loadElements(Locale locale, Object filter) {
			LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			daoFilter = listHandler.onPreFilterParamConversion(daoFilter);
			daoFilter.convertParamsToDaoParams();
			daoFilter = listHandler.onPostFilterParamConversion(daoFilter);
			return listHandler.getList(entityClass,daoFilter);
		}

    	public List loadList(Locale locale, Object filter) {
    		LavaDaoFilter daoFilter = (LavaDaoFilter)filter;
			daoFilter = listHandler.onPreFilterParamConversion(daoFilter);
			daoFilter.convertParamsToDaoParams();
			daoFilter = listHandler.onPostFilterParamConversion(daoFilter);
			return ScrollablePagedListHolder.createSourceList(
					listHandler.getList(entityClass,daoFilter),daoFilter);
    	}
    	
    	
	}
}
