package edu.ucsf.lava.core.list.model;

import java.util.ArrayList;
import java.util.List;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoNamedParam;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.dao.LavaDaoPositionalParam;
import edu.ucsf.lava.core.dao.hibernate.DaoHibernateNamedParam;
import edu.ucsf.lava.core.dao.hibernate.DaoHibernatePositionalParam;

public class ListRequest {

	public static String CACHE_KEY_SEPARATOR = "|";
    public static String CACHE_KEY_LEFT_VALUE_DELIMITER = "{";
    public static String CACHE_KEY_RIGHT_VALUE_DELIMITER = "}";
    
	
	private String listName;
	private String sort;
	private String format;
	private BaseListConfig codes;
	private List<LavaDaoParam> params;
	private AuthUser authUser;
	
	public ListRequest(){
		this(null,null,null,null,null,null);
	}
	
	public ListRequest(String listName) {
		this(listName,null,null,null,null,null);
	}

	public ListRequest(String listName, String sort, String format) {
		this(listName,sort,format,null,null,null);
	}

	
	public ListRequest(String listName, String sort, String format, BaseListConfig codes) {
		this(listName,sort,format,codes,null,null);
	}
	
	public ListRequest(String listName, String sort, String format, BaseListConfig codes, List<LavaDaoParam> params) {
		this(listName,sort,format,codes,params,null);
	}
	/**
	 * Copy constructor
	 * @param request
	 */
	public ListRequest(ListRequest request) {
		this(request.getListName(),
				request.getSort(),
				request.getFormat(),
				request.getCodes(),
				request.getParams(),
				request.getAuthUser());
	}
	
	public ListRequest(String listName, String sort, String format, BaseListConfig codes, List<LavaDaoParam> params, AuthUser authUser) {
		super();
		this.listName = listName;
		this.codes = codes; 
		this.sort = sort;
		this.format = format;
		this.params = params;
		this.authUser = authUser;
	}
	
	
	public ListRequest (BaseListConfig listConfig){
		this(new ListRequest(),listConfig);

	}
	
	/**
	 * A constructor that merges the list request with the default list confguration
	 * @param request
	 * @param listConfig
	 */
	public ListRequest (ListRequest request, BaseListConfig listConfig){
		this(request);
		
		if(this.listName==null){this.listName = listConfig.getName();}
		if(this.codes==null){this.codes = listConfig.getDefaultCodes();}
		if(this.sort==null){this.sort = listConfig.getDefaultSort();}
		if(this.format == null){this.format = listConfig.getDefaultFormat();}
		if(this.params == null){this.params = listConfig.getDefaultParams();}
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public List<LavaDaoParam> getParams() {
		return params;
	}
	public void setParams(List<LavaDaoParam> params) {
		this.params = params;
	}
	
	public void addParam(LavaDaoParam param){
		if(this.params==null){this.params = new ArrayList<LavaDaoParam>();}
		this.params.add(param);
	}
	
	public boolean hasParams(){
		if(this.params==null){return false;}
		return !this.params.isEmpty();
	}

	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}

	public BaseListConfig getCodes() {
		return codes;
	}

	public void setCodes(BaseListConfig codes) {
		this.codes = codes;
	}
	
	public boolean hasCodes(){
		return (this.codes!=null && !this.codes.getName().equals(NoCodesListConfig.NO_CODES_CONFIG_NAME));
	}

	public AuthUser getAuthUser() {
		return authUser;
	}

	public void setAuthUser(AuthUser authUser) {
		this.authUser = authUser;
	}
	
	public LavaDaoFilter getDaoFilter(){
		LavaDaoFilter filter = LavaList.newFilterInstance((AuthUser)this.authUser);
		if(hasParams()){
			filter.getDaoParams().addAll(this.params);
		}
		return filter;
	}
	
	/**
	 * create a key string representing the listname with all the specific request parameters   
	 * @param filter LavaDaoFilter with any params for the list query specified.
	 * @param codesToUse A list configuration object with codes (missing/skip) to add to the list
	 * @param format The label format
	 * @param sort The sort method
	 * @return
	 */
	public String getListRequestId(){
		//create key base
		StringBuffer key = new StringBuffer(listName);
		if(getCodes()!=null){
			key.append(CACHE_KEY_SEPARATOR).append(getCodes().getName());
		}
		if(getFormat()!=null){
			key.append(CACHE_KEY_SEPARATOR).append(getFormat());
		}
		if(getSort()!=null){
			key.append(CACHE_KEY_SEPARATOR).append(getSort());
		}
		
		//add custom params to cacheKey
		if(hasParams()){
			List<LavaDaoParam> params = getParams();
			for(LavaDaoParam param: params){
				if(param.getType().equalsIgnoreCase(LavaDaoParam.TYPE_NAMED)){
					LavaDaoNamedParam namedParam = (DaoHibernateNamedParam)param;
					key.append(CACHE_KEY_SEPARATOR).append(namedParam.getParamName())
						.append(CACHE_KEY_LEFT_VALUE_DELIMITER).append(namedParam.getParamValue().toString())
						.append(CACHE_KEY_RIGHT_VALUE_DELIMITER);
				
				}else if (param.getType().equalsIgnoreCase(LavaDaoParam.TYPE_POSITIONAL)){
					LavaDaoPositionalParam posParam = (DaoHibernatePositionalParam)param;
					key.append(CACHE_KEY_SEPARATOR).append(posParam.getParamPos())
						.append(CACHE_KEY_LEFT_VALUE_DELIMITER).append(posParam.getParamValue().toString())
						.append(CACHE_KEY_RIGHT_VALUE_DELIMITER);
				}
			}
		}
									
		
		return key.toString();
	}
	
}
