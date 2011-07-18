package edu.ucsf.lava.core.list.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.list.ListDefinitions;


/**
 * @author jhesse
 *
 */
public class BaseListConfig {
	/** Logger for this class and subclasses */
    protected static final Log logger = LogFactory.getLog(LavaList.class);
    protected static Map<String,String> emptyList;
    protected ListDefinitions listDefinitions;
    
    
    public static String LIST_CONFIG_BEAN_NAME_PREFIX = "list.";
	public static String GENERIC_LIST_VALUE_QUERY = "GenericListValueQuery";
	public static String GENERIC_LIST_VALUE_QUERY_PARAM_NAME = "listName";
	public static String GENERIC_LIST_VALUE_QUERY_NULL_DESC = "GenericListValueQueryNullDesc";
	public static String GENERIC_LIST_VALUE_QUERY_DECIMAL = "GenericListValueQueryDecimal";
	public static String LIST_VALUE_QUERY_DECIMAL_CDR_NO_POINT_5 = "ListValueQueryDecimalCDRScaleNoPoint5";
	
	public static String FORMAT_LABEL = "label";
	   public static String FORMAT_LABEL_WITH_INITIAL = "labelWithInitial";  // e.g. 'l | label' : first char of the label is concatenated to the label separated by '|' (useful for multi-column select boxes
	public static String FORMAT_VALUE_IS_LABEL = "valueIsLabel";  // e.g. 'value' :  use the value as the label (usually because the label is null in the database). this is redundant if using GenericListValueQueryNullDesc, since that query populates the label with the value (aka valueKey)
	public static String FORMAT_VALUE_CONCAT_LABEL = "valueConcatLabel"; // e.g. 'value | label' :  the label is a concatenation of the value and the label
	public static String FORMAT_TWO_DECIMAL_PLACES = "twoDecimalPlaces"; // only applies to DecimalRange lists
    public static String FORMAT_SEPARATOR = " | ";
    public static String FORMAT_CONCATONATOR = " - ";
    
    public static String SORT_LABEL_STRING = "label";
    public static String SORT_VALUE_STRING = "value";
    public static String SORT_VALUE_NUMERIC = "valueNumeric";
    public static String SORT_VALUE_DECIMAL = "valueDecimal";
    public static String SORT_ORDER_INDEX = "orderIndex"; 
    public static String SORT_ORDER_INDEX_VALUE = "orderIndexValue";
    public static String SORT_ORDER_INDEX_LABEL = "orderIndexLabel";
    public static String SORT_ORDER_INDEX_VALUE_NUMERIC = "orderIndexValueNumeric";
    public static String SORT_ORDER_INDEX_VALUE_DECIMAL = "orderIndexValueDecimal";
    public static String SORT_NONE = "none";
    
    
	
	protected String name; //The name of the list -- as referenced in the metadata (this is distinct from dblistName defined above--the name of the list in the List database table)
	protected String query; //the query name 
	protected String dbListName; //list name from the database List table
	protected Boolean dynamic; //static / dynamic flag
	protected List<LabelValueBean> data; // data list supplied in the xml configuration;
	protected List<BaseListConfig> staticReferenceLists; //static lists that are combined with the primary list definition to create the list
	protected List<BaseListConfig> dynamicReferenceLists; //dynamic lists that are combined with the primary list definition to create the list
	
	protected List<LabelValueBean> internalList;
	protected List<LabelValueBean> internalListWithCodes;
	protected Map<String,String> defaultExternalList;
	protected Map<String,String> defaultExternalListWithCodes;
	protected Map<String,Map<String,String>> cache = new HashMap<String,Map<String,String>>();
	
	protected Date staticListRefreshed;
	
	protected String defaultFormat; //default presenation format
	protected String defaultSort; //default sort to use
	protected BaseListConfig defaultCodes;  //default missing data codes to use if not specified 
	protected List<LavaDaoParam> defaultParams; //default params to use for the query
	
	
	
	public BaseListConfig() {
		super();
		this.dynamic = false;
		}
	
	
	/**
	 * Override this method to introduce custom handling of the request paramters in 
	 * subclasses of ListConfig (e.g. convert String param values to Long instances)
	 * @param request
	 * @return
	 */
	protected ListRequest onGetList(ListRequest request){
		return request;
	}
	

	/**
	 * primary method used to get the list 
	 * @param ListRequest request
	 */
	public Map<String,String> getList(ListRequest requestIn){
		
		//combine request passed in with the defaults
		ListRequest request = new ListRequest(requestIn,this);
		
		//if not a dynamic list, make sure the static list has been initialized
		if(!dynamic && staticListRefreshed==null){
			refreshStaticList();
		}
		
		request = onGetList(request);
		
		//look for the list in the cache (only static lists are cached), if found simply return
		String cacheKey = request.getListRequestId();
		if(cached(cacheKey)){
			return getCachedList(cacheKey);
		}
		
		//treat dynamic and parameterized static lists the same, except we cache the static lists for reuse and
		//always re-execute dynamic list requests.
		if(dynamic || request.hasParams()){
			// load the primary list 
			List<LabelValueBean> dynamicList = getDynamicInternalList(request);
			
			//if necessary, add codes
			if(request.hasCodes()){
				dynamicList = merge(dynamicList,request.getCodes().getInternalList(),false);
			}
			
			Map<String,String> list = createExternalList(dynamicList,request.getFormat(),request.getSort());
			if(!dynamic){
				cacheList(cacheKey,list);
			}
			return list;
		}

		//handle static, non-default, not-yet-cached list requests
		
		//if no codes then use the "without codes" lists;
		if(!request.hasCodes()){
			return cacheList(cacheKey,createExternalList(internalList,request.getFormat(),request.getSort()));
		//if the codes requested are the default codes for the configuration then use the pre-prepared internal list
		}else if(request.getCodes().getName().equals(defaultCodes.getName())){
			return cacheList(cacheKey,createExternalList(internalListWithCodes,request.getFormat(),request.getSort()));
		//if the codes requested are not the default codes then create a new internal format list with the new codes
		}else{
			List<LabelValueBean> listWithCodes = merge(copyList(internalList),request.getCodes().getInternalList(),false);
			return cacheList(cacheKey,createExternalList(listWithCodes,request.getFormat(),request.getSort()));
		}
	}
		

	
	
	
	
	
	
	/**
	 * is the requested list in the listCache
	 * @param cacheKey the key into the cache
	 * 
	 */
	protected boolean cached(String cacheKey){
		return cache.containsKey(cacheKey);
	}
	
	/**
	 * get a list from the cache
	 * @param cacheKey the key into the cache
	 * 
	 */
	protected Map<String,String> getCachedList (String cacheKey){
		if(!cached(cacheKey)){return getEmptyList();}
		return cache.get(cacheKey);
	}
	
	
	/** 
	 * put the list in the cache
	 * @param cacheKey the key for the cache
	 * @param list the static list to cache in external list format
	 * @return returns the list passed in for convenient use 
	 */
	protected Map<String,String> cacheList(String cacheKey, Map<String,String>list){
		cache.put(cacheKey, list);
		return list;
	}
	
	
	protected void clearCache(){
		cache.clear();
	}
	
	/**
	 * add the default static list with and without codes to the cache
	 *
	 */
	protected void addDefaultsToCache(){
		
		ListRequest request = new ListRequest(this);
		//get cache key using default codes
		String cacheKey = request.getListRequestId();
		cacheList(cacheKey,defaultExternalListWithCodes);
		
		//if the defaultCodes for this config is the NoCodesListConfig, then we don't need to add a
		//without codes default to the cache because the default with codes = default without codes
		
		if(defaultCodes!=null && !defaultCodes.getName().equals(NoCodesListConfig.NO_CODES_CONFIG_NAME)){
			
			//set request codes = no codes
			request.setCodes(new NoCodesListConfig());
			cacheKey = request.getListRequestId();
			cacheList(cacheKey,defaultExternalList);
		}
	}
	
	
	/**
	 * Utility method to create a blank list.  Used by this class as the first step 
	 * in generating an external list.  Also used by other classes when a runtime 
	 * problem results in no list generation (e.g. listconfig name not found) and
	 * we don't want to throw a runtime exception. 
	 * @param size
	 * @return
	 */
	public static Map<String,String>getEmptyList(int size){
		return addBlankListEntry(new LinkedHashMap<String,String>(size+1));
		}
	
	public static Map<String,String>getEmptyList(){
		return getEmptyList(0);
		}
	
	
	public static Map<String,String>addBlankListEntry(Map<String,String>list){
		//	  every list should have a blank element that is the first element of the list. 
		//  this processing is done after the list is created to ensure that the blank is the first element
		//  the blank element is a user interface feature, representing that nothing in the list has been selected
		//  and allowing a user to select a blank element
		// note: it is much easier to do this here to the external lists than to the internal lists 
    	//       because internal lists can reference other internal lists so there is complexity of 
    	//       making sure there is only one blank 
    	// note: custom editors cause any field submitted with the empty string as the value to 
    	//       result in a null being assigned to the property associated with the field
		
		list.put("","");
		return list;
	}
	
	
	
	
	public List<LabelValueBean> getDynamicInternalList(ListRequest request){
		
		
		
		List<LabelValueBean> dynamicInternalList = loadPrimaryQueryList(request.getDaoFilter());
		
		//next merge in any dynamic reference lists
		//TODO: do we need to have distinct named parameter sets to support each distinct dynamic query?
		if(dynamicReferenceLists!=null){
			for(BaseListConfig referenceList : dynamicReferenceLists){
				dynamicInternalList = merge(dynamicInternalList,referenceList.getDynamicInternalList(request),false);
			}
		}
		
		//merge in all static reference lists
		if(staticReferenceLists!=null){
			for(BaseListConfig referenceList : staticReferenceLists){
				dynamicInternalList = merge(dynamicInternalList,referenceList.getInternalList(),false);
			}
		}
		
		// merge-replace any data specified in the configuration
		if(data!=null){
			dynamicInternalList = merge(dynamicInternalList,data,true);
		}
		
		return dynamicInternalList;
	}
	
	
	
	/**
	 * This is the main contruction routine for static lists upon initialization.  The list construction logic is:
	 *   	1) loads the primary list for this configuration.  
	 * 		2) merges in all reference lists by calling their getInternalList method (does not replace matching list items)
	 * 		3) merges in any data supplied with this list configuration (does replace matching list items...this allows the 
	 * 			data confugration option to be used as an override..not sure if this is really the best behavior)
	 * 		4) updates internal list properties 
	 * 		5) generates default external lists properties (using default sort and format)
	 * 		5) sets the internal refreshed timestamp.
	 * 		Note: this routine does not protect against access by other threads while the lists are being refreshed.  
	 * 			This is probably OK as the worst thing that would happen(?) is a slighly out of date or null list
	 * 
	 */
	public void refreshStaticList(){
		
		ListRequest request = new ListRequest(this);
		logger.debug("refreshStaticList="+this.getName());
		List<LabelValueBean> list = loadPrimaryQueryList(request.getDaoFilter());
		
		/* Note: Static lists cannot contain dynamic reference lists, because the method signatures for 
		 * static lists do not include LavaDaoFilter parameters.
		 */ 
		
		//merge in any static reference lists
		if(staticReferenceLists!=null){
			for(BaseListConfig referenceList : staticReferenceLists){
				list = merge(list,referenceList.getInternalList(),false);
			}
		}
		
		//merge-replace any data specified in the configuration
		if(data!=null){
			list = merge(list,data,true);
		}
		
		//set internal list properties
		internalList = list;
		if(!request.hasCodes()){
			internalListWithCodes = internalList;
		}else{
			//share the underlying labelvaluebeans between the internalLists...don't really need separate copies
			//and with really large lists this will save resources. 
			internalListWithCodes = merge(copyList(internalList,false),defaultCodes.getInternalList(),false);
		}
		
		//generate default external list properties
		defaultExternalList = createExternalList(internalList,defaultFormat,defaultSort);
		if(!request.hasCodes()){
			defaultExternalListWithCodes = defaultExternalList;
		}else{
			defaultExternalListWithCodes = createExternalList(internalListWithCodes,defaultFormat,defaultSort);
		}
		
		clearCache();
		addDefaultsToCache();
		staticListRefreshed = new Date();
	}
	
	
	
	/**
	 * load the primary list by executing the supplied named query and dbListName (if configured).
	 *  
	 * @param filter LavaDaoFilter with all the params for the named query configured.
	 * 
	 */
	protected List<LabelValueBean> loadPrimaryQueryList(LavaDaoFilter filter) {
		java.util.List<LabelValueBean> resultList = new ArrayList<LabelValueBean>();
		
		if(StringUtils.isEmpty(this.query)){ return resultList;}
		
		if(query.equals(GENERIC_LIST_VALUE_QUERY) || query.equals(GENERIC_LIST_VALUE_QUERY_NULL_DESC) ||
				query.equals(GENERIC_LIST_VALUE_QUERY_DECIMAL) || query.equals(LIST_VALUE_QUERY_DECIMAL_CDR_NO_POINT_5)){
			filter.addDaoParam(filter.daoNamedParam(GENERIC_LIST_VALUE_QUERY_PARAM_NAME,dbListName));
		}
		try {
			resultList = LavaList.MANAGER.findByNamedQuery(query,filter);
			if (resultList == null) {
				logger.info("resultList null in loadList query=<" + query + ">");
			}
		}
		catch (DataAccessException dae) {
			logger.info("DataAccessException in loadList, query=<" + query + ">");
		}
		return resultList;
	}
 
	
	/**
	 * Create a copy of an internal formatted list.  if deepCopy then create new
	 * instances of the LabelvalueBeans in the new list. This facilitaes using the 
	 * internal lvb format for formatting and sorting without modifying the common
	 * internal list properties of the config. 
	 * @param list
	 * @param deepCopy TODO
	 * @return
	 */
	protected List<LabelValueBean> copyList(List<LabelValueBean> list, boolean deepCopy){
		ArrayList<LabelValueBean> copy = new ArrayList(list.size());
		for(LabelValueBean lvb:list){
			copy.add(new LabelValueBean(lvb));
		}
		return copy;
	}
	
	
	/**
	 * Convenience function...default copy is a deepCopy...
	 * @param list
	 * @return
	 */
	protected List<LabelValueBean> copyList(List<LabelValueBean> list){
		return copyList(list,true);
	}
	
	/**
	 * Merge lists.  
	 * @param mergeTo The list to merge into (in LabelValueBean format) 
	 * @param mergeFrom The list to merge from (in LabelValueBean format)
	 * @param replace Whether to replace items in the mergeTo list with matching items in the mergeFrom list.  Match is
	 * 		defined by comparing Labels (ToDo: is this correct behavior?)
	 * @return
	 */
	protected List<LabelValueBean> merge(List<LabelValueBean> mergeTo, List<LabelValueBean> mergeFrom, boolean replace) {
		
		for (LabelValueBean mergeFromLVB : mergeFrom) {
			int mergeToIndex = 0;
			boolean found = false;
			
			for (LabelValueBean mergeToLVB : mergeTo) {
				if (mergeFromLVB.compareTo(mergeToLVB) == 0) {
					found = true;
					break;
				}
				mergeToIndex++;
			}
			if (found && replace) {
				mergeTo.set(mergeToIndex, mergeFromLVB);
			}
			else {
				mergeTo.add(mergeFromLVB);
			}
		}
		
		return mergeTo;
	}
	
	
	/**
	 * utility method to transfer a list from the internal list structure to the external list structure
	 * @param list
	 * @return
	 */
	private Map<String,String> createExternalList(List<LabelValueBean> list, String format, String sort) {
		
		List<LabelValueBean> workList = copyList(list);
		workList = formatList(workList,format);
		workList = sortList(workList,sort);
		
		Map<String,String> extList = getEmptyList(workList.size());
		
		for(LabelValueBean lvb: workList){
    		extList.put(lvb.getValue(), lvb.getLabel());
    	}
		return extList;
	}
	

	
	protected List<LabelValueBean> sortList(List <LabelValueBean> list,String sort){
			if (sort.equals(SORT_NONE)) {
				return list;
			}else if (sort.equals(SORT_LABEL_STRING)) {
				// natural sort order, i.e. by "value" field of LabelValueBean
				Collections.sort(list);	
			}
			else if (sort.equals(SORT_VALUE_STRING)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.valueComparator);
			}
			else if (sort.equals(SORT_VALUE_NUMERIC)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.valueNumericComparator);
			}
			else if (sort.equals(SORT_VALUE_DECIMAL)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.valueDecimalComparator);
			}
			else if (sort.equals(SORT_ORDER_INDEX)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.orderIndexComparator);
			}
			else if (sort.equals(SORT_ORDER_INDEX_VALUE)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.orderIndexValueComparator);
			}
			else if (sort.equals(SORT_ORDER_INDEX_LABEL)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.orderIndexLabelComparator);
			}
			else if (sort.equals(SORT_ORDER_INDEX_VALUE_NUMERIC)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.orderIndexValueNumericComparator);
			}
			else if (sort.equals(SORT_ORDER_INDEX_VALUE_DECIMAL)) {
				// use a Comparator to sort
				Collections.sort(list, LabelValueBean.orderIndexValueDecimalComparator);
			}
			else {
				logger.error("Invalid sort field=<" + sort + ">");
		
			}
		
		return list;
	}
	
	
	
	private List<LabelValueBean> formatList(List<LabelValueBean> list, String format){
		
		if (format==null || list==null || format.equals(FORMAT_LABEL)) {return list;}
		
		if (format.equals(FORMAT_VALUE_IS_LABEL)) {
			for(LabelValueBean lvb : list){
				lvb.setLabel(lvb.getValue());
			}
		}else if (format.equals(FORMAT_LABEL_WITH_INITIAL)) {
			for(LabelValueBean lvb : list){
				lvb.setLabel(new StringBuffer(lvb.getLabel().substring(0,1)).append(FORMAT_SEPARATOR).append(lvb.getLabel()).toString());
			}
		}else if (format.equals(FORMAT_VALUE_CONCAT_LABEL)) {
			for(LabelValueBean lvb : list){
				lvb.setLabel(new StringBuffer(lvb.getValue()).append(this.FORMAT_CONCATONATOR).append(lvb.getLabel()).toString());
			}
		}
		return list;
	}

	
	



	public List<LabelValueBean> getData() {
		return data;
	}


	public void setData(List<LabelValueBean> data) {
		this.data = data;
	}


	public String getDbListName() {
		return dbListName;
	}


	public void setDbListName(String dbListName) {
		this.dbListName = dbListName;
	}



	public BaseListConfig getDefaultCodes() {
		return defaultCodes;
	}

	public void setDefaultCodes(BaseListConfig defaultCodes) {
		this.defaultCodes = defaultCodes;
	}

	public String getDefaultFormat() {
		return defaultFormat;
	}


	public void setDefaultFormat(String defaultFormat) {
		this.defaultFormat = defaultFormat;
	}


	public String getDefaultSort() {
		return defaultSort;
	}


	public void setDefaultSort(String defaultSort) {
		this.defaultSort = defaultSort;
	}





	public List<LabelValueBean> getInternalList() {
		if(internalList!=null){return internalList;}
		if(staticListRefreshed==null){
			refreshStaticList();
		}
		return internalList;
	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getQuery() {
		return query;
	}


	public void setQuery(String query) {
		this.query = query;
	}

	




	public Boolean isDynamic() {
		return dynamic;
	}



	public void setDynamic(Boolean dynamic) {
		this.dynamic = dynamic;
	}



	public List<BaseListConfig> getDynamicReferenceLists() {
		return dynamicReferenceLists;
	}



	public void setDynamicReferenceLists(List<BaseListConfig> dynamicReferenceLists) {
		this.dynamicReferenceLists = dynamicReferenceLists;
	}



	public List<BaseListConfig> getStaticReferenceLists() {
		return staticReferenceLists;
	}



	public void setStaticReferenceLists(List<BaseListConfig> staticReferenceLists) {
		this.staticReferenceLists = staticReferenceLists;
	}



	public Date getStaticListRefreshed() {
		return staticListRefreshed;
	}





	public ListDefinitions getListDefinitions() {
		return listDefinitions;
	}





	public void setListDefinitions(ListDefinitions listDefinitions) {
		this.listDefinitions = listDefinitions;
	}


	public List<LavaDaoParam> getDefaultParams() {
		return defaultParams;
	}


	public void setDefaultParams(List<LavaDaoParam> defaultParams) {
		this.defaultParams = defaultParams;
	}


	
	
	
	
}
