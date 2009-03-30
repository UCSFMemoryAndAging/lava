package edu.ucsf.lava.core.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.list.model.BaseListConfig;
import edu.ucsf.lava.core.list.model.EmptyListConfig;
import edu.ucsf.lava.core.list.model.LavaList;
import edu.ucsf.lava.core.list.model.ListRequest;
import edu.ucsf.lava.core.list.model.NoCodesListConfig;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.metadata.model.ViewProperty;


/* this is a class which manages all of the data required to build lists of values,
 *  and builds these lists (typically for UI display purposes, e.g. in "select" 
 *  boxes, as a list of checkboxes, etc.)
 *  
 * list definitions are in a configuration file and this data is used to create lists
 *  in Java data structures.
 *  
 * "static" lists can be built ahead-of-time and are built at application init
 *  
 * "dynamic" lists are built real-time because they take parameter values only known at run time or
 * contain values that may change over time (e.g. lists of entities)
 *  
 * list data typically resides in the database, but can also be provided in the configuration
 * file  
*/

public class ListManager extends LavaManager{
	
	public static String LIST_MANAGER_NAME = "listManager";
	private static final BaseListConfig NoCodesListConfig = null;

	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	
	// list definitions
	ListDefinitions staticListDefinitions;   
	ListDefinitions dynamicListDefinitions;   

	//	cache of list configuration data for entities
	Map<String,Map<String,ListRequest>>entityListRequestCache =	new HashMap<String,Map<String,ListRequest>>();  //[Entity],[RequestId,Request]]  
	Map<String,ListRequest> listRequestCache = new HashMap<String,ListRequest>(); //[RequestId,Request]
	
	
	public ListManager(){
		super(LIST_MANAGER_NAME);
	}
	
	/**
	 * getDynamicList
	 * 
	 * This method is called by controller's for any dynamic lists required by the model/view
	 * for that controller. each dynamic list has a list definition, just like static lists,
	 * and that list definition is used to create the dynamic list (generally speaking, all
	 * dynamic lists are created via a database query)
	 * 
	 * @param user To set filters for data authorization filtering
	 * @param listName The name of the dynamic list
	 * @param paramNames query param names
	 * @param values param values
	 * @param types param param types (SQL types)
	 */
	public Map<String,String> getDynamicList(AuthUser user, String listName, String[] paramNames, Object[] values, Class[] types) {
		
		ListRequest request = new ListRequest();
		request.setListName(listName);
		request.setAuthUser(user);
		
		if(paramNames != null){
			//this filter is just use to create the dao params for the request.  It is not actually the filter that will be used
			//for the underlying query for the list values...
			LavaDaoFilter filter = LavaList.newFilterInstance(); 
			List<LavaDaoParam> daoParams = new ArrayList<LavaDaoParam>(paramNames.length);
			for(int i=0;i<paramNames.length;i++){
				daoParams.add(filter.daoNamedParam(paramNames[i],values[i]));
			}
			request.setParams(daoParams);
		}
		
		BaseListConfig listConfig = dynamicListDefinitions.get(listName);
		if(listConfig==null){
			logger.info("Unable to locate ListConfig for dynamic list "+listName);
			return BaseListConfig.getEmptyList();
		}
		
		return listConfig.getList(request);
	}	
	
	
	
	
	
	public Map<String,String> getStaticListByRequestId(String requestId){
		if(listRequestCache.containsKey(requestId)){
			ListRequest request = listRequestCache.get(requestId);
			return getStaticList(request);
		}else{
			return new EmptyListConfig().getList(NoCodesListConfig);
		}
	}
	
	public Map<String,String> getStaticList(ListRequest request){
		BaseListConfig listConfig = staticListDefinitions.get(request.getListName());
		if(listConfig!=null){
			return listConfig.getList(request);
		}else{
			return new EmptyListConfig().getList(NoCodesListConfig);
		}
	}
	
	public Map<String,Map<String,String>> getStaticListsForEntity(String entityName) {
		//if no list requests for the entity configured then return an empty map. 
		
		if(!entityListRequestCache.containsKey(entityName.toLowerCase())){
			return new HashMap<String,Map<String,String>>();
		}
		Map<String,Map<String,String>> listsForEntity = new HashMap<String,Map<String,String>>(); 
		Map<String,ListRequest> listRequests = entityListRequestCache.get(entityName.toLowerCase());
			for (Entry<String,ListRequest> entry : listRequests.entrySet()){
				ListRequest request = entry.getValue();
				BaseListConfig listConfig = staticListDefinitions.get(request.getListName());
				if(listConfig!=null){
					listsForEntity.put(request.getListRequestId(),listConfig.getList(request));
				}
			}
		return listsForEntity;		
	}
	
	public Map<String,Map<String,String>> getDefaultStaticLists() {
		Map<String,Map<String,String>> defaultStaticLists = new HashMap<String,Map<String,String>>();
		for(Entry<String,BaseListConfig> entry: staticListDefinitions.getDefinitions().entrySet()){
			ListRequest request = new ListRequest(entry.getValue());
			defaultStaticLists.put(request.getListRequestId(), entry.getValue().getList(request));
		}
		return defaultStaticLists;
	}
	
	public Map<String,Map<String,String>> getDefaultStaticListsWithoutCodes() {
		Map<String,Map<String,String>> defaultStaticLists = new HashMap<String,Map<String,String>>();
		for(Entry<String,BaseListConfig> entry: staticListDefinitions.getDefinitions().entrySet()){
			ListRequest request = new ListRequest(entry.getValue());
			request.setCodes(new NoCodesListConfig());//turn off codes
			defaultStaticLists.put(request.getListRequestId(), entry.getValue().getList(request));
		}
		return defaultStaticLists;
	}
	
	public Map<String,String> getDefaultStaticList(String listName) {
		BaseListConfig listConfig = staticListDefinitions.get(listName);
		if(listConfig!=null){
			return listConfig.getList(new ListRequest(listConfig));
		}else{
			return new EmptyListConfig().getList(NoCodesListConfig);
		}
	}
		
	
	public Map<String,String> getDefaultStaticListWithoutCodes(String listName) {
		BaseListConfig listConfig = staticListDefinitions.get(listName);
		if(listConfig!=null){
			ListRequest listRequest = new ListRequest(listConfig);
			listRequest.setCodes(NoCodesListConfig);
			return listConfig.getList(listRequest);
		}else{
			return new EmptyListConfig().getList(NoCodesListConfig);
		}	
	}
	

	public void initStaticLists() {
		
		logger.info("creating static lists");
		for (Entry<String,BaseListConfig> entry: staticListDefinitions.getDefinitions().entrySet()) {
			entry.getValue().refreshStaticList();
		}
	}

	
	
		
	/**
	 * getDynamicList
	 *  
	 * Convenience method when authorization access filtering is needed, there is only one query parameter.
	 */
	public Map<String,String> getDynamicList(AuthUser user, String listName, String paramName, Object value, Class type) {
		return getDynamicList(user, listName, new String[]{paramName}, new Object[]{value}, new Class[]{type});
	}
	
	/**
	 * getDynamicList
	 *  
	 * Convenience method when authorization access filtering is needed, there are no parameters.
	 * note: a dynamic list with no parameters (to make it dynamic) amounts to a static list that needs to be refrehed 
	 */
	public Map<String,String> getDynamicList(AuthUser user, String listName) {
		return getDynamicList(user, listName,	new String[]{}, new Object[]{}, new Class[]{});
	}
	
	/**
	 * getDynamicList
	 *  
	 * Convenience method when authorization filtering is not needed for the list, so currentUser id 
	 * is not needed as an argument.
	 */
	public Map<String,String> getDynamicList(String listName, String[] paramNames, Object[] values, Class[] types) {
		return getDynamicList(null, listName, paramNames, values, types);
	}
	
	/**
	 * getDynamicList
	 *  
	 * Convenience method when authorization filtering is not needed for the list, so currentUser id 
	 * is not needed as an argument, and when there is one parameters.
	 */
	public Map<String,String> getDynamicList(String listName, String paramName, Object value, Class type) {
		return getDynamicList(null, listName, new String[]{paramName}, new Object[]{value}, new Class[]{type});
	}

	/**
	 * getDynamicList
	 *  
	 * Convenience method when authorization filtering is not needed for the list, so currentUser id 
	 * is not needed as an argument, and when there are no parameters.
	 * note: it seems doubtful this would ever be used, because if there are no parameters and the list
	 *       is not subject to authorization filtering, it may as well be a static list. 
	 */
	public Map<String,String> getDynamicList(String listName) {
		return getDynamicList(null, listName, new String[]{}, new Object[]{}, new Class[]{});
	}

	
	
	public Map<Long,String> initializeMetadataListRequestCache(){
			Map<Long,String> viewPropIdRequestIdMap = new HashMap<Long,String>();
			Map<String,ListRequest>requests = new HashMap<String,ListRequest>();
			Map<String,Map<String,ListRequest>> entityRequests = new HashMap<String,Map<String,ListRequest>>();
			
			LavaDaoFilter filter = ViewProperty.newFilterInstance(); //also use for creating named params
			filter.addDaoParam(filter.daoNotNull("list"));
			filter.addDefaultSort("entity", true);
			filter.addDefaultSort("propOrder", true);
			filter.addDefaultSort("property",true);
			
			List<ViewProperty> properties = ViewProperty.MANAGER.get(filter);
			
			String currentEntity = null;
			Map<String,ListRequest> currentEntityRequests = null;
			for(ViewProperty property : properties)	{
				
				//if new entity then save the current requests for the current entity and create the new entity map
				if(!StringUtils.equalsIgnoreCase(property.getEntity(),currentEntity)){
					if(StringUtils.isNotEmpty(currentEntity)){
						entityRequests.put(currentEntity.toLowerCase(), currentEntityRequests);
						requests.putAll(currentEntityRequests);
					}
					currentEntity = property.getEntity();
					currentEntityRequests = new HashMap<String,ListRequest>();
				}
				
				//get the list configuration for the property
				BaseListConfig config = staticListDefinitions.get(property.getList());
				if(config!=null){
					//create a request object to store the custom configuration for the list as specified
					ListRequest request = new ListRequest();
					request.setListName(config.getName());
					
					//get custom attributes for list from the property
					Map<String,String> listParams = property.getListConfigParams();	
					
					//handle standard attributes and treat any other attribute as a named query param.
					for(Entry<String,String> entry : listParams.entrySet()){
						if(entry.getKey().equals(ViewProperty.LIST_SORT_PARAM_NAME)){
							request.setSort(entry.getValue());
						}else if(entry.getKey().equals(ViewProperty.LIST_FORMAT_PARAM_NAME)){
							request.setFormat(entry.getValue());
						}else if(entry.getKey().equals(ViewProperty.LIST_CODES_PARAM_NAME)){
							BaseListConfig codes = staticListDefinitions.get(entry.getValue().concat(".codes"));
							if(codes != null){ 
								request.setCodes(codes);
							}
						}else{
							request.addParam(filter.daoNamedParam(entry.getKey(), entry.getValue()));
						}
					}
					//if custom codes not set and there is a codes list registered for the entity, use that
					BaseListConfig entityCodes = staticListDefinitions.get(property.getEntity().concat(".codes"));
					if(request.getCodes() == null && entityCodes!= null){
						request.setCodes(entityCodes);
					}
					//get the requestId from the request now that it is prepared...this is the unique key 
					//representing the instance of the list requested by the viewProperty definition
					String requestId = request.getListRequestId(); 
					//save the request for later access
					currentEntityRequests.put(requestId, request);
					//record the requestId linked to the propertyId )
					viewPropIdRequestIdMap.put(property.getId(), requestId);
				}
			}
			//need to store the requests for the last entity
			entityRequests.put(currentEntity.toLowerCase(), currentEntityRequests);
			requests.putAll(currentEntityRequests);
			
			//now set the list managers caches with the maps we just configured
			this.listRequestCache = requests;
			this.entityListRequestCache = entityRequests;
			
			//return the property id --> requestId map to the metadata manager to use as the listRequestId metadata property
			return viewPropIdRequestIdMap;
		}
	

	
	
	public ListDefinitions getStaticListDefinitions() {
		return staticListDefinitions;
	}
	public void setStaticListDefinitions(ListDefinitions staticListDefinitions) {
		this.staticListDefinitions = staticListDefinitions;
	}	
	
	public ListDefinitions getDynamicListDefinitions() {
		return dynamicListDefinitions;
	}
	public void setDynamicListDefinitions(ListDefinitions dynamicListDefinitions) {
		this.dynamicListDefinitions = dynamicListDefinitions;
	}
	
	
	
	
}
