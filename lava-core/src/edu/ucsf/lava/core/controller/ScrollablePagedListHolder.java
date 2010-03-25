package edu.ucsf.lava.core.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.beans.support.SortDefinition;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.LavaEntity;

public class ScrollablePagedListHolder extends RefreshablePagedListHolder {
	private String listName = "primaryList";
	private Map<String,Object> paramsUsed;
	private boolean viewFilterToggle = false;
	private Object master; //The master object for MasterDetail templates
	// temporarily bind the page size and page until binding is complete. this is necessary 
	// so that binding list items for a given page size and page using pageList is not affected
	// by a changing pageSize or page (because changing pageSize changes the size of pageList)
	private int pageSizeHolder;
	private int pageHolder;
	
	protected int initialElements = DEFAULT_INITIAL_ELEMENTS; //the number of elements to load 
	public static final String FILTER_TOGGLE_PARAM_NAME = "filterToggle";
	public static final int DEFAULT_INITIAL_ELEMENTS = 99; // use array indexing not row numbering --- will get 100 rows
	public static final int FIRST_ELEMENT_INDEX = 0;
	public static final int NAVIGATION_DISPLAY_PAGES_MAX = 50;
	public static final int NAVIGATION_EXTRA_PAGES_MAX = 50;
	
	
	private TreeMap navigationMap;
	
	private int navigationBasePage;
	
	public ScrollablePagedListHolder() {
		super();
		// init pageSizeHolder in case setPageSize not called when creating the list
		setPageSizeHolder(this.getPageSize());
	}

	public Object getMaster() {
		return master;
	}
	
	public void setMaster(Object master) {
		this.master = master;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public ScrollablePagedListHolder(ScrollableListSourceProvider sourceProvider) {
		super(sourceProvider);
	}

	//Checks to make sure all the items in the list are initialized
	public void initPageList() {
		// note that changing the pageSize may require changing the page number, if the new pageSize 
		// results in fewer pages than the current value of page. this happens when getPage
		// is called (it is invoked indirectly here)
		if(currentPageContainsNotLoadedElements()){
			LavaDaoFilter filter = (LavaDaoFilter)this.getFilter();
			int firstElement = this.getFirstElementOnPage();
			int lastElement = this.getLastElementOnPage();
			if (firstElement < 0) {firstElement = 0;}  //first element cannot be less than 1
			if (lastElement > this.getNrOfElements()) {lastElement = this.getNrOfElements() -1;}  //last element cannot exceed the number of elements in the list
			if (lastElement < firstElement){lastElement = firstElement;} //last element cannot be less than first element...fail gracefully by returning the first element
			
			//note: standardize on using array indexes (e.g. 0,1,2,3,4) not row numbers (1,2,3,4)
			
			filter.setRows(firstElement,lastElement);
			loadElements();
			
		}
		
		//is this needed?
		/*List elements = new ArrayList();
		elements.addAll(super.getPageList());
		for (Object element: elements){
			if(elementIsProxy(element)){
				elements.remove(element);
			}
		}
		return elements;
		*/
	}
	
	protected void loadElements(){
		ScrollableListSourceProvider sp = (ScrollableListSourceProvider)this.getSourceProvider();
		List elements = sp.loadElements(this.getLocale(),this.getFilter());
		 //((ScrollableListSourceProvider)this.getSourceProvider())
		//	.loadElements(this.getLocale(),this.getFilter());
		
		LavaDaoFilter filter = (LavaDaoFilter)this.getFilter();
		List source = this.getSource();
		this.addElementsToList(elements,source,filter);
	}
	
    protected boolean currentPageContainsNotLoadedElements(){
        int first = this.getFirstElementOnPage();
        int last = this.getLastElementOnPage();
        for (int i = first; i <= last; i++){
                if(elementIsNotLoaded((ListItem) getSource().get(i))){
                        return true;
                }
        }
        return false;
    }

    protected boolean elementIsNotLoaded(ListItem element){
        return element.getEntity() == null;
    }

	
	public void clearFilterParams(){
		LavaDaoFilter daoFilter = (LavaDaoFilter)this.getFilter();
		daoFilter.clearParams();
		this.paramsUsed = null;
	}
	public boolean getFilterOn(){
		return this.viewFilterToggle;
	}
	public void setFilterOn(boolean toggle){
		this.viewFilterToggle = toggle;
	}
	 public void toggleFilterOn(){
		 this.viewFilterToggle = !this.viewFilterToggle;
	 }
	
	public boolean refresh(){
		LavaDaoFilter daoFilter = (LavaDaoFilter)this.getFilter();
		if (this.paramsUsed != null && daoFilter.paramsNotEqualTo(this.paramsUsed)) {
			doRefresh();
			return true;
		}else{
			return false;
		}
	}
	
	public void doRefresh(){
		doRefresh(true);
	}
	
	public void doRefresh(boolean resetRows){
		LavaDaoFilter daoFilter = (LavaDaoFilter)this.getFilter();
		if (resetRows == true){
			int lastIndex = (this.getPageSize()< DEFAULT_INITIAL_ELEMENTS) ? DEFAULT_INITIAL_ELEMENTS : this.getPageSize();
			daoFilter.setRows(FIRST_ELEMENT_INDEX,lastIndex);
		}
		//clear rowcount
		daoFilter.setResultsCount(LavaDaoFilter.RESULT_COUNT_EMPTY);
		//clear id cache
		daoFilter.clearIdCache();
		super.refresh(true);
		//reload record navigation
		this.getRecordNavigation(true);
		this.paramsUsed = new HashMap(daoFilter.getParams());
		
	}
	


	
	//	ScrollablePageListHolder not sortable on the client side becasue of proxy elements
	public void setSort(SortDefinition sort) {
		
		super.setSort(null);
	}

	public Map getRecordNavigation(){
		return getRecordNavigation(false);
	}
	
	/*
	 * Build a map with pages for table navigation.  If there are more
	 * pages then the maximum, then generate the items around the current page and
	 * add items for the other pages at a higher interval
	 */
	public Map getRecordNavigation(boolean reload){
		//if first time then reload
		if (this.navigationMap == null){
			reload = true;
		}
		//if the current page is outside the range of standard pages then reload map
		if (this.getPage() > (this.navigationBasePage + (this.NAVIGATION_DISPLAY_PAGES_MAX/2)) ||
			this.getPage() < (this.navigationBasePage - (this.NAVIGATION_DISPLAY_PAGES_MAX/2))){
				reload = true;
		}
		if(reload==false){
			return this.navigationMap;
		}
		
		this.navigationMap = new TreeMap();
		//Load the map with the standard elements up to the max range around the current page;
		this.navigationBasePage = this.getPage();		
		int firstPageInRange = this.getPage()-(this.NAVIGATION_DISPLAY_PAGES_MAX/2);
		if (firstPageInRange < 0 ){firstPageInRange = 0;}
		
		int lastPageInRange = firstPageInRange + this.NAVIGATION_DISPLAY_PAGES_MAX;
		if (lastPageInRange > this.getPageCount()){lastPageInRange = this.getPageCount();}
		
		int pagesBeforeRange = firstPageInRange;
		int pagesAfterRange = this.getPageCount() - lastPageInRange;
		int extraPagesCount = pagesBeforeRange + pagesAfterRange;
		int extraPagesInterval = extraPagesCount / this.NAVIGATION_EXTRA_PAGES_MAX;
		if(extraPagesInterval == 0){
			extraPagesInterval = 1;
		}
		
		
		//Load up any pages needed before current page range
		Integer totalItems = new Integer(this.getNrOfElements());
		for(int page = 0; page < firstPageInRange;){
			Integer pageFirst = new Integer(1 + ((page)*this.getPageSize()));
			Integer pageLast = new Integer(pageFirst + (this.getPageSize()*extraPagesInterval)-1);
			if (pageLast > totalItems){
				pageLast = totalItems;
			}
			StringBuffer buffer = new StringBuffer().append(pageFirst).append("-").append(pageLast)
					.append(" of ").append(totalItems);
			
			this.navigationMap.put(new Integer(page),new String(buffer));
			page += extraPagesInterval;
		}
		//load up the pages in the page display range
		for (int page = firstPageInRange; page < lastPageInRange;page++){
			Integer pageFirst = new Integer(1 + ((page)*this.getPageSize()));
			Integer pageLast = new Integer(pageFirst + this.getPageSize()-1);
			if (pageLast > totalItems){
				pageLast = totalItems;
			}
			StringBuffer buffer = new StringBuffer().append(pageFirst).append("-").append(pageLast)
					.append(" of ").append(totalItems);
			
			this.navigationMap.put(new Integer(page),new String(buffer));
		}
//		Load up any pages needed after the page range
		for(int page = lastPageInRange+1; page < this.getPageCount();){
			Integer pageFirst = new Integer(1 + ((page)*this.getPageSize()));
			Integer pageLast = new Integer(pageFirst + (this.getPageSize()*extraPagesInterval)-1);
			if (pageLast > totalItems){
				pageLast = totalItems;
			}
			StringBuffer buffer = new StringBuffer().append(pageFirst).append("-").append(pageLast)
					.append(" of ").append(totalItems);
			
			this.navigationMap.put(new Integer(page),new String(buffer));
			page += extraPagesInterval;
		}
		
		return this.navigationMap;
	}
	
	public static List createSourceList(List loadedElements,LavaDaoFilter filter){
		int resultsCount = filter.getResultsCount();
		List newSourceList = new ArrayList(resultsCount);
		// want to put an id in each listItem so that metadata can be tied to an item even if
		// its entity has not been loaded, e.g. allowing the user to select all items in a list 
		// which includes unloaded items
		// TODO: if the size of the list is larger than the number of initial elements loaded
		// (DEFAULT_INITIAL_ELEMENTS) but smaller than the threshold to use idCache, then a 
		// cursor is used which only retrieves those elements needed for the current page. in 
		// this case, under the current design, the id for unloaded items will not be available 
		// to store in ListItem, as it is when idCache is used. so the design will have to be 
		// modified. possible solution is to use idCache whenever there are unloaded elements.  
		
		//TODO: look at using the List<ListItem> structure to serve as the idCache,
		//so that idCache is not needed in LavaDaoFilter anymore		
		if (filter.getIdCache() != null) {
			int i=0;
			for (Object id : filter.getIdCache()) {
				newSourceList.add(i++, new ListItem((Long)id));
			}
		}
		else {
			int i=0;
			for (Object entity : loadedElements){
				newSourceList.add(i++, new ListItem(((LavaEntity)entity).getId(), entity, Boolean.FALSE));
			}
			
			//add empty listItems to the sourcelist. This is equivalent to the empty placeholders before ListItems were used. 
			for(;i<resultsCount;){
				newSourceList.add(i++, new ListItem((long)0));
			}
		}
		
		return addElementsToList(loadedElements,newSourceList,filter);
	}

	protected static List addElementsToList(List elements, List targetList, LavaDaoFilter filter){
				
		if(elements != null || elements.size() == (filter.rowSetSize())){
			for(int i = filter.getFirstRowNum(), j=0; j < elements.size() && j < targetList.size();i++,j++){
				//TODO: check to make sure hat the id's in the elements recevied match the idcache.  Take appropriate action
				
				//If the element is a LavaEntity and we are using an idCache
				if(elements.get(j).getClass().isAssignableFrom(LavaEntity.class) && filter.getIdCache()!=null){
					//make sure the returned entity id matches the id in the same row of the idCache 
					if (!((LavaEntity)elements.get(j)).getId().equals(filter.getIdCache().get(i))){
						//ids do not match, set an error on the filter...OR WHAT???
						
					}
				}
				if(elements.get(j)!= null){
					((ListItem)targetList.get(i)).setEntity((LavaEntity)elements.get(j));	
				}
			}
		}else{
			//TODO: load failure...what to do?  Can we put a message on the page that gets displayed by the decorator?
			}
		return targetList;
	}
	
	/**
	 * This is a convenience method to allow setting the PagedListHolder source from
	 * a list of entities. Because the pageList source is a List<ListItem>, can not set 
	 * a List<LavaEntity> directly on the PagedListHolder using the setSource method. 
	 * This method takes a List<LavaEntity> and transforms it into a List<ListItem> 
	 * which can then be set as the PagedListHolder source via setSource.  
	 * 
	 * @param entityList
	 */
	public void setSourceFromEntityList(List entityList) {
		List<ListItem> listItemList = new ArrayList<ListItem>(entityList.size());
		for (Object entity : entityList) {
			listItemList.add(new ListItem(entity));
		}
		this.setSource(listItemList);
	}
	
	/**
	 * This is the counterpart to setSourceFromEntityList. The internal source list is
	 * a List<ListItem> and this method converts that to a List of entities. This is
	 * provided for external use that expects a List of entities, such as List reports.
	 * 
	 * @return a List of entities
	 */
	public List getSourceAsEntityList() {
		List entityList = new ArrayList(this.getSource().size());
		for (ListItem listItem : (List<ListItem>) this.getSource()) {
			if (listItem.getEntity() != null) {
				entityList.add(listItem.getEntity());
			}
			else {
				// TODO: this is a big problem for lists with unloaded elements. the client
				// of such a list, e.g. a List report, will error out on the first list 
				// element that has not been loaded. solution is to load all list elements
				// if there are any unloaded list elements. this would be simplified if idCache
				// were used whenever there were unloaded elements (see createSourceList TODO)
				entityList.add(null);
			}
		}
		return entityList;
	}
	
	public int getListSize() {
		List<ListItem> source = (List<ListItem>) this.getSource();
		if (source == null) {
			return 0;
		}
		else {
			return source.size();
		}
    }

	public int getNumSelected() {
		int numSelected = 0;
		List<ListItem> source = (List<ListItem>) this.getSource();
		for (ListItem listItem : source) {
			if (listItem.getSelected()) {
				numSelected++;
			}
		}
		return numSelected;
    }

	public int getNumOnCurrentPage() {
		return this.getLastElementOnPage() - this.getFirstElementOnPage() + 1;
	}
	
	/**
	 * Temporarily binds the page size until binding is complete, and then sets pageSize. this is
	 * necessary so that binding list items for a given page size using pageList is not affected
	 * by a changing pageSize (because changing pageSize changes the size of pageList)
	 */
	public void setPageSizeHolder(int pageSizeHolder) {
		this.pageSizeHolder = pageSizeHolder;
	}

	public int getPageSizeHolder() {
		return pageSizeHolder;
	}
	
	/**
	 *  Override base class to set the pageSizeHolder as well, to keep
	 *  it in sync with pageSize, for the purposes of the navigation UI.
	 */
	public void setPageSize(int pageSize) {
		super.setPageSize(pageSize);
		this.pageSizeHolder = pageSize;
	}

	
	/**
	 * Temporarily binds the page number until binding is complete, and then sets page. this is
	 * necessary so that binding list items for a given page number using pageList is not affected
	 * by a changing page (because changing page could change pageSize if the last page has fewer
	 * items and changing pageSize changes the size of pageList)
	 */
	public void setPageHolder(int pageHolder) {
		this.pageHolder = pageHolder;
	}
	
	public int getPageHolder() {
		return pageHolder;
	}

	/**
	 *  Override base class to set the pageHolder as well, to keep  it in sync with page, 
	 *  for the purposes of the navigation UI.
	 */
	public void setPage(int page) {
		super.setPage(page);
		this.pageHolder = page;
	}
	
	
	public static class ListItem implements Serializable {
		// id allows associating metadata with list items even if they have not been loaded
		private Long id;
		// entity holds loaded list item and is NULL when a list item has not been loaded yet
		// it can also hold objects from a pre-loaded list set via setSourceFromEntityList
		private Object entity;
		private Boolean selected;
		
		// constructor for list items that have not been loaded
		public ListItem(Long id) {
			this.id = id;
			this.entity = null;
			this.selected = Boolean.FALSE;
		}
		
		// constructor for list items that have been loaded
		public ListItem(Long id, Object entity, Boolean selected) {
			this.id = id;
			this.entity = entity;
			this.selected = selected;
		}

		// constructor when the ScrollablePagedListHolder is just being used as a 
		// navigation construct for a pre-existing reference list, i.e. not a list 
		// obtained via the PagedListHolder loadList method. such lists may have
		// objects without an "id" field
		public ListItem(Object entity) {
			// this.id == null
			this.entity = entity;
			this.selected = Boolean.FALSE;
		}
		
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Object getEntity() {
			return entity;
		}

		public void setEntity(Object entity) {
			this.entity = entity;
		}

		public Boolean getSelected() {
			return this.selected;
		}

		public void setSelected(Boolean selected) {
			this.selected = selected;
		}
	}

}
