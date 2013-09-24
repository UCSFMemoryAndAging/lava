package edu.ucsf.lava.core.dao.hibernate;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.dao.LavaDaoProjection;
import edu.ucsf.lava.core.dao.LavaNonParamHandler;
import edu.ucsf.lava.core.dao.LavaParamHandler;


public class LavaDaoFilterHibernateImpl implements LavaDaoFilter {

	//TODO: handle associations
	private HashMap<String,String> aliases = new HashMap();
	private HashMap<String,String> outerAliases = new HashMap();
	private LinkedHashMap<String,Boolean> sorts = new LinkedHashMap();
	private LinkedHashMap<String,Boolean> defaultSorts = new LinkedHashMap();
	private AuthUser user;
	private HashMap<String,Map<String,Object>> contextFilters = new HashMap();
	private HashMap<String,Object> params = new HashMap();
	private HashMap<String,LavaDaoParam> quickFilters = new HashMap<String,LavaDaoParam>();
	private String activeQuickFilter;
	private ArrayList<LavaDaoParam> daoParams = new ArrayList();
	private ArrayList<LavaParamHandler> paramHandlers = new ArrayList();
	private ArrayList<LavaDaoProjection> daoProjections = new ArrayList();
	private Integer firstRowNum;
	private Integer lastRowNum;
	private Integer resultsCount;
	private List<Object> idCache;
	
	
	//Authorization Methods
	public AuthUser getAuthUser() {
		return this.user;
	}

	public void setAuthUser(AuthUser user) {
		this.user = user;
	}

	//Context Methods
	public Map<String,Map<String,Object>> getContextFilters() {
		return contextFilters;
		
	}

	public Object getContextFilter(String contextKey) {
		if (contextFilters.containsKey(contextKey)){
			return contextFilters.get(contextKey);
		}
		return null;
	}

	public LavaDaoFilter setContextFilter(String contextKey, String paramName,Object paramValue) {
		Map<String,Object>params = new HashMap<String,Object>();
		params.put(paramName, paramValue);
		return setContextFilter(contextKey,params);
	}
		
	public LavaDaoFilter setContextFilter(String contextDesc, Map<String,Object> params) {
		contextFilters.put(contextDesc,params);
		return this;
	}

	public LavaDaoFilter clearContextFilter(String contextKey) {
		if (contextFilters.containsKey(contextKey)){
			contextFilters.remove(contextKey);
		}
		return this;
	}

	//sort methods
	
	//add a sort with a default ascending order.  If it is already set, toggle the order
	public LavaDaoFilter setSort(String propertyName) {
		if(sorts.containsKey(propertyName)){
			addSort(propertyName, !(sorts.get(propertyName)));
		}else{
			addSort(propertyName,true);
		}
		return this;
	}
	

	public LavaDaoFilter addSort(String propertyName, boolean ascending) {
		sorts.put(propertyName,new Boolean(ascending));
		return this;
	}

	public Map getSort() {
		return sorts;
	}
	

		public LavaDaoFilter clearSort() {
		sorts.clear();
		return this;
	}

	//default query sorts --- avoids conflict with the custom user sorting	
		public LavaDaoFilter addDefaultSort(String propertyName, boolean ascending) {
			defaultSorts.put(propertyName,new Boolean(ascending));
			return this;
		}

		public Map getDefaultSort() {
			return defaultSorts;
		}
		

		public LavaDaoFilter clearDefaultSort() {
			defaultSorts.clear();
			return this;
		}	
	//alias methods
		public Map getOuterAliases() {
			return outerAliases;
		}
		public LavaDaoFilter clearOuterAlias(String collectionName) {
			if (outerAliases.containsKey(collectionName)){
				outerAliases.remove(collectionName);
			}
			return this;
		}
		public LavaDaoFilter clearOuterAliases() {
			outerAliases.clear();
			
			return this;
		}
		public LavaDaoFilter setOuterAlias(String collectionName, String alias) {
			outerAliases.put(collectionName,alias);
			return this;

		}
		public Object getOuterAlias(String collectionName) {
			if(outerAliases.containsKey(collectionName)){
				return outerAliases.get(collectionName);
			}
			return null;
		}
		
		
		public Map getAliases() {
			return aliases;
		}
		public LavaDaoFilter clearAlias(String collectionName) {
			if (aliases.containsKey(collectionName)){
				aliases.remove(collectionName);
			}
			return this;
		}
		public LavaDaoFilter clearAliases() {
			aliases.clear();
			
			return this;
		}
		public LavaDaoFilter setAlias(String collectionName, String alias) {
			aliases.put(collectionName,alias);
			return this;

		}
		public Object getAlias(String collectionName) {
			if(aliases.containsKey(collectionName)){
				return aliases.get(collectionName);
			}
			return null;
		}
		
    //param methods
		
	public Map getParams() {
		return params;
	}
	public Map getQuickFilters() {
		return quickFilters;
	}
	public LavaDaoFilter clearParam(String paramName) {
		if (params.containsKey(paramName)){
			params.remove(paramName);
		}
		return this;
	}
	public LavaDaoFilter clearParams() {
		// do not want "non params" removed as want them to retain their value. difficulty
		// with iterating over a Map and (selectively) removing items from that Map due to
		// ConcurrentModificationException, so instead save away the "non params" then clear
		// the Map and restore them
		Map<String,Object> saveNonParams = new HashMap<String,Object>();
		for (String param : params.keySet()) {
			for (LavaParamHandler handler : paramHandlers) {
				if (handler.handleParam(this, param)) {
					if (handler instanceof LavaNonParamHandler) {
						saveNonParams.put(param, this.getParam(param));
					}
					break;
				}
			}
		}
		this.params.clear();
		// restore non params
		this.params.putAll(saveNonParams);
		return this;
	}
	
	public boolean isParamEmpty(String name){
		if(params.get(name) != null && StringUtils.isNotEmpty(params.get(name).toString())){
			return false;
		}
		return true;
	}
	
	public LavaDaoFilter setParam(String paramName, Object paramValue) {
		params.put(paramName,paramValue);
		return this;

	}
	
	public LavaDaoFilter addQuickFilter(String quickFilterName, LavaDaoParam quickFilterParam){
		quickFilters.put(quickFilterName,quickFilterParam);
		return this;

	}
	
	public LavaDaoFilter clearQuickFilters(){
		this.quickFilters.clear();
		return this;
	}
	
	
	public Object getParam(String paramName) {
		if(params.containsKey(paramName)){
			return params.get(paramName);
		}
		return null;
	}
	
	public String getActiveQuickFilter() {
		return activeQuickFilter;
	}

	public void setActiveQuickFilter(String quickFilterName) {
		this.activeQuickFilter = quickFilterName;
	}

	public boolean paramsNotEqualTo(Map<String,Object> oldParams){
		for(String key:this.params.keySet()){
			//if the item does not exist in the other map then not equal
			if(!oldParams.containsKey(key)) {return true;}
			Object currentValue = this.params.get(key);
			Object oldValue = oldParams.get(key);
			//if the values are not both blank (null or "")
			if(!((oldValue == null || StringUtils.isEmpty(oldValue.toString())) && (currentValue == null || StringUtils.isEmpty(currentValue.toString())))){
				//if one of the values is null then not equal
				if(oldValue == null || currentValue == null){return true;}
				//if both are non null and their string equivelents are not equal then not equal
				if (!currentValue.toString().equalsIgnoreCase(oldValue.toString())){
					return true;
				}
			}
		}
		for(String key: oldParams.keySet()){
//			if the item does not exist in the other map then not equal
			if(!this.params.containsKey(key)) {return true;}
			Object currentValue = this.params.get(key);
			Object oldValue = oldParams.get(key);
//			if the values are not both blank (null or "")
			if(!((oldValue == null || StringUtils.isEmpty(oldValue.toString())) && (currentValue == null || StringUtils.isEmpty(currentValue.toString())))){
				//if one of the values is null then not equal
				if(oldValue == null || currentValue == null){return true;}
				//if both are non null and their string equivelents are not equal then not equal
				if (!currentValue.toString().equalsIgnoreCase(oldValue.toString())){
					return true;
				}
			}
		}
		return false;
	}
	//rowset definition methods
	public LavaDaoFilter setRows(int firstRowNum, int lastRowNum)
	{
		this.firstRowNum = Integer.valueOf(firstRowNum);
		this.lastRowNum = Integer.valueOf(lastRowNum);
		return this;
	}
	public LavaDaoFilter clearRows(){
		this.firstRowNum = null;
		this.lastRowNum = null;
		return this;
	}
	public int getFirstRowNum(){
			if(this.firstRowNum == null){
				return -1;
			}
			return this.firstRowNum.intValue();
	}
	public int getLastRowNum(){
		if(this.lastRowNum == null){
			return -1;
		}
		return this.lastRowNum.intValue();
	}
	
	public boolean rowSelectorsSet(){
		if(this.firstRowNum == null || this.lastRowNum == null){
			return false;
		}
		return true;
	}
	public int rowSetSize(){
		if(this.firstRowNum == null || this.lastRowNum == null){
			return -1;
		}
		return this.lastRowNum.intValue() - this.firstRowNum.intValue();
	}
	public LavaDaoFilter setResultsCount(int resultsCount){
		if (resultsCount < 0){resultsCount = 0;}
		this.resultsCount = Integer.valueOf(resultsCount); 
		return this;
	}
	public int getResultsCount(){
		if (this.resultsCount == null) {return -1;}
		return this.resultsCount.intValue();
	}

	//Dao Projection Methods
	
	public void clearDaoProjections(){
		daoProjections.clear();
	}
	
	
	public void addDaoProjection(LavaDaoProjection projection){
		daoProjections.add(projection);
	}
	
	public void addDaoProjection(int index, LavaDaoProjection projection){
		daoProjections.add(index, projection);
	}
	
	public List<LavaDaoProjection> getDaoProjections(){
		return daoProjections;
	}
	
	
	public LavaDaoProjection daoAliasProjection(LavaDaoProjection projection, String alias){
		return new DaoHibernateCriterionProjection(Projections.alias(((DaoHibernateCriterionProjection)projection).getProjection(), alias));
	}
	public LavaDaoProjection daoAvgProjection(String property){
		return new DaoHibernateCriterionProjection(Projections.avg(property));
	}
	public LavaDaoProjection daoCountProjection(String property){
		return new DaoHibernateCriterionProjection(Projections.count(property));
	}
	public LavaDaoProjection daoDistinctProjection(LavaDaoProjection projection){
		return new DaoHibernateCriterionProjection(Projections.distinct(((DaoHibernateCriterionProjection)projection).getProjection()));
	}
	public LavaDaoProjection daoGroupProjection(String property){
		return new DaoHibernateCriterionProjection(Projections.groupProperty(property));
	
	}
	public LavaDaoProjection daoIdProjection(){
		return new DaoHibernateCriterionProjection(Projections.id());
	}
	
	public LavaDaoProjection daoMaxProjection(String property){
		return new DaoHibernateCriterionProjection(Projections.max(property));
	}
	public LavaDaoProjection daoMinProjection(String property){
		return new DaoHibernateCriterionProjection(Projections.min(property));
	}
	public LavaDaoProjection daoRowCountProjection(){
		return new DaoHibernateCriterionProjection(Projections.rowCount());
	}
	public LavaDaoProjection daoSumProjection(String property){
		return new DaoHibernateCriterionProjection(Projections.sum(property));
	}
	public LavaDaoProjection daoProjection(String property){
		return new DaoHibernateCriterionProjection(Projections.property(property));
	}
	
	//Dao Params methods
	public void clearDaoParams() {
		daoParams.clear();
	}

	public LavaDaoParam daoBetweenParam(String propertyName, Object lo, Object high) {
		return new DaoHibernateCriterionParam(Restrictions.between(propertyName,lo,high));
	}

	public void addDaoParam(LavaDaoParam param) {
		daoParams.add(param);
	}
	
	public LavaDaoParam daoInParam(String propertyName, Object[] values){
		return new DaoHibernateCriterionParam(Restrictions.in(propertyName, values));
	}
	
	public LavaDaoParam daoNot(LavaDaoParam param){
		return new DaoHibernateCriterionParam(
				Restrictions.not(((DaoHibernateCriterionParam)param).getCriterion()));
	}
	
	public LavaDaoParam daoOr(LavaDaoParam param, LavaDaoParam param2) {
		return new DaoHibernateCriterionParam(Restrictions.or(
				((DaoHibernateCriterionParam)param).getCriterion(),
				((DaoHibernateCriterionParam)param2).getCriterion()));
	}
	

	
	public LavaDaoParam daoAnd(LavaDaoParam param, LavaDaoParam param2){
		return new DaoHibernateCriterionParam(Restrictions.and(
				((DaoHibernateCriterionParam)param).getCriterion(),
				((DaoHibernateCriterionParam)param2).getCriterion()));
	}
	
	public LavaDaoParam daoNull(String propertyName){
		return new DaoHibernateCriterionParam(Restrictions.isNull(propertyName));
	}
	
	
	public LavaDaoParam daoNotNull(String propertyName){
		return new DaoHibernateCriterionParam(Restrictions.isNotNull(propertyName));
	}

	public LavaDaoParam daoEqualityParam(String propertyName, Object param) {
		return new DaoHibernateCriterionParam(Restrictions.eq(propertyName,param));
	}

	public LavaDaoParam daoLikeParam(String propertyName, String pattern) {
		return new DaoHibernateCriterionParam(Restrictions.ilike(propertyName,pattern,MatchMode.START));
	}

	public LavaDaoParam daoLikeAnywhereParam(String propertyName, String pattern) {
		return new DaoHibernateCriterionParam(Restrictions.ilike(propertyName,pattern,MatchMode.ANYWHERE));
	}
	
	public LavaDaoParam daoGreaterThanParam(String propertyName, Object value) {
		return new DaoHibernateCriterionParam(Restrictions.gt(propertyName, value));
	}

	public LavaDaoParam daoGreaterThanOrEqualParam(String propertyName, Object value) {
		return new DaoHibernateCriterionParam(Restrictions.ge(propertyName, value));
	}
	
	public LavaDaoParam daoLessThanParam(String propertyName, Object value) {
		return new DaoHibernateCriterionParam(Restrictions.lt(propertyName, value));
	}

	public LavaDaoParam daoLessThanOrEqualParam(String propertyName, Object value) {
		return new DaoHibernateCriterionParam(Restrictions.le(propertyName, value));
	}
	
	public LavaDaoParam daoNamedParam(String propertyName, Object param) {
		return new DaoHibernateNamedParam(propertyName, param);

	}
	


	public LavaDaoParam daoPositionalParam(int position,Object value) {
		return new DaoHibernatePositionalParam(position,value);
	}
	
		
	public List<LavaDaoParam> getDaoParams() {
		return daoParams;
	}

	public LavaDaoParam daoPositionalParam(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	public LavaDaoFilter addIdDaoEqualityParam(Long id){
		this.addDaoParam(this.daoEqualityParam("id",id));
		return this;
	}

	public LavaDaoFilter addParamHandler(LavaParamHandler handler) {
		paramHandlers.add(handler);
		return this;
		
	}

	public void clearParamHandlers() {
		paramHandlers.clear();
	}
	
	public ArrayList<LavaParamHandler> getParamHandlers(){
		return paramHandlers;
	}

	public void convertParamsToDaoParams() {
		this.clearDaoParams();
		for (String name: params.keySet()){
			boolean handled=false;
			for (LavaParamHandler handler: paramHandlers){
				// handleParam adds the param to daoParams (unless the handler is LavaIgnoreParamHandler/LavaNonParamHandler)
				if(handled==false && handler.handleParam(this,name)){
					handled=true;
				}
			}
			if (!handled){defaultParamHandler(name);}
			
		}
		
		// convert active quick filter param to DAO params
		// TODO: move this code to a separate method and add a call to that method 
		// following the convertParamsToDaoParams call in the component handler subclasses
		if (activeQuickFilter != null && quickFilters.containsKey(activeQuickFilter)) {
			LavaDaoParam quickFilterParam = quickFilters.get(activeQuickFilter);
			if(quickFilterParam!=null){
				this.addDaoParam(quickFilterParam);
			}
		}
		
	}
		
	
	

	public LavaDaoFilter setIdCache(List<Object> idCache) {
		this.idCache = idCache;
		return this;
	}
	
	public List<Object> getIdCache() {
		return this.idCache;
	}

	public LavaDaoFilter clearIdCache(){
		this.idCache = null;
		return this;
	}
	
	public LavaDaoParam daoInIdCacheParam(){
		if(this.firstRowNum != null && this.lastRowNum != null){
			return daoInIdCacheParam(this.firstRowNum,this.lastRowNum);
		}
		return null;
	}
		
	
	public LavaDaoParam daoInIdCacheParam(int firstRow, int lastRow){
		if(this.idCache!=null){
			//note: everything using array indexing (e.g. 0,1,2) not row numbers (1,2,3)
			int firstIndex = (firstRow < idCache.size()) ? firstRow : idCache.size()-1;
			if (firstIndex < 0) firstIndex = 0;
			
			int lastIndex = (lastRow >= idCache.size()) ? idCache.size()-1 : lastRow;
			if(lastRow < 0) lastIndex = 0;
			
			return daoInParam("id",idCache.subList(firstIndex,lastIndex+1).toArray());
			}
		return null;	
	}

	protected void defaultParamHandler(String name){
		if(params.get(name) != null && StringUtils.isNotEmpty(params.get(name).toString())){
		 	this.addDaoParam(this.daoLikeParam(name,(String)params.get(name).toString()));
		}
	}

	public LavaDaoParam daoDateAndTimeBetweenParam(String datePropertyName, String timePropertyName, Date startDateParam, Time startTimeParam, Date endDateParam, Time endTimeParam) {
		return new DaoHibernateCriterionParam(
					LavaDateTimeHibernateCriteriaUtils.daoBetween(datePropertyName, timePropertyName, startDateParam, startTimeParam, endDateParam, endTimeParam));
	}

	public LavaDaoParam daoDateAndTimeEqualityParam(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		return new DaoHibernateCriterionParam(
				LavaDateTimeHibernateCriteriaUtils.daoEquality(datePropertyName, timePropertyName, dateParam, timeParam));
	}

	public LavaDaoParam daoDateAndTimeGreaterThanOrEqualParam(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		return new DaoHibernateCriterionParam(
				LavaDateTimeHibernateCriteriaUtils.daoGreaterThanOrEqual(datePropertyName, timePropertyName, dateParam, timeParam));
	}

	public LavaDaoParam daoDateAndTimeGreaterThanParam(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoGreaterThan(datePropertyName, timePropertyName, dateParam, timeParam));
	}

	public LavaDaoParam daoDateAndTimeLessThanOrEqualParam(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoLessThanOrEqual(datePropertyName, timePropertyName, dateParam, timeParam));
	}

	public LavaDaoParam daoDateAndTimeLessThanParam(String datePropertyName, String timePropertyName, Date dateParam, Time timeParam) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoLessThan(datePropertyName, timePropertyName, dateParam, timeParam));
	}

	public LavaDaoParam daoDateAndTimeNotNull(String datePropertyName, String timePropertyName) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoNotNull(datePropertyName, timePropertyName));
	}

	public LavaDaoParam daoDateAndTimeNull(String datePropertyName, String timePropertyName) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoNull(datePropertyName, timePropertyName));
	}

	public LavaDaoParam daoDateAndTimeBetweenParam(String datePropertyName, String timePropertyName, Date startDateParam, Date endDateParam) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoBetween(datePropertyName, timePropertyName, startDateParam, endDateParam));
	}

	public LavaDaoParam daoDateAndTimeBetweenParam(String startDatePropertyName, String startTimePropertyName, String endDatePropertyName, String endTimePropertyName, Date dateParam, Time timeParam) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoBetween(startDatePropertyName, startTimePropertyName, endDatePropertyName, endTimePropertyName, dateParam, timeParam));
	}

	public LavaDaoParam daoDateAndTimeOverlapsParam(String startDatePropertyName, String startTimePropertyName, String endDatePropertyName, String endTimePropertyName, Date startDateParam, Time startTimeParam, Date endDateParam, Time endTimeParam) {
		return new DaoHibernateCriterionParam( LavaDateTimeHibernateCriteriaUtils.daoOverlaps(startDatePropertyName, startTimePropertyName, endDatePropertyName, endTimePropertyName, startDateParam, startTimeParam, endDateParam, endTimeParam));
	}


	
}
