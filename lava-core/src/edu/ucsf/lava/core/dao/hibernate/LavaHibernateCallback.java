package edu.ucsf.lava.core.dao.hibernate;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;

public abstract class LavaHibernateCallback implements HibernateCallback {
	
	LavaDaoFilter filter;
	Session session;
	
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

	public LavaHibernateCallback(LavaDaoFilter filter) {
		this.filter=filter;
		
	}

	public Object doInHibernate(Session session) throws HibernateException,
	SQLException {
		this.session = session;
		initQuery();
		enableFilters();
		applyAliases();
		applyParameters();
		if(filter.rowSelectorsSet())
			getResultsCount();
		applySorts();
		Object results = doQuery();	
		disableFilters();
		return results;
	}

	protected void applyParameters(){
		List<LavaDaoParam> params = filter.getDaoParams();
		for (LavaDaoParam param : params){
			applyParameter(param);
		}
	}
	
	protected void applyAliases(){
		Map<String,String> aliases = filter.getAliases();
		for(String collection : aliases.keySet()){
			applyOuterAlias(collection,aliases.get(collection));
		}
		Map<String,String> outerAliases = filter.getOuterAliases();
		for(String collection : outerAliases.keySet()){
			applyOuterAlias(collection,outerAliases.get(collection));
		}
	}
	protected void applySorts(){
		Map<String,Boolean> sorts = filter.getSort();
	
		if (sorts.isEmpty()){
			//use default query sorts if no custom user sort defined
			sorts = filter.getDefaultSort();
		}
		for (String property : sorts.keySet()){
			applySort(property,sorts.get(property));
		}
	}

	protected abstract void initQuery();
	protected abstract void applySort(String property, Boolean ascending);
	protected abstract void applyAlias(String collection, String alias);
	protected abstract void applyOuterAlias(String collection, String alias);
	protected abstract void applyParameter(LavaDaoParam param);
	protected abstract Object doQuery() throws HibernateException,SQLException ;
	protected abstract void getResultsCount() throws HibernateException,SQLException ;
	protected void enableFilters(){
		
		
		
		// authorization filters are always applied (to entities which have the filters in their mapping)
		AuthUser user = filter.getAuthUser();
		if (user != null){
			Map<String,Map<String,Object>>authFilters = user.getAuthDaoFilters();
			
			//for each entry enable the filter and add any params.
			for(Entry<String,Map<String,Object>>entry:authFilters.entrySet()){
				Filter authFilter = session.enableFilter(entry.getKey());
				if(entry.getValue()!=null){
					for (Entry<String,Object>param:entry.getValue().entrySet()){
						if(param.getValue()!=null && param.getKey()!=null){
							if(param.getValue().getClass().isInstance(Collection.class)){
								authFilter.setParameterList(param.getKey(), (Collection)param.getValue());
							}else if(param.getValue().getClass().isArray()){
								authFilter.setParameterList(param.getKey(),(Object[])param.getValue());
							}else{
								authFilter.setParameter(param.getKey(), param.getValue());
							}
						}
					}
				}
			}
		}	
		
		Map<String,Map<String,Object>>contextFilters = filter.getContextFilters();
			
			//for each entry enable the filter and add any params.
			for(Entry<String,Map<String,Object>>entry:contextFilters.entrySet()){
				Filter contextFilter = session.enableFilter(entry.getKey());
				if(entry.getValue()!=null){
					for (Entry<String,Object>param:entry.getValue().entrySet()){
						if(param.getValue()!=null && param.getKey()!=null){
							if(param.getValue().getClass().isInstance(Collection.class)){
								contextFilter.setParameterList(param.getKey(), (Collection)param.getValue());
							}else if(param.getValue().getClass().isArray()){
								contextFilter.setParameterList(param.getKey(),(Object[])param.getValue());
							}else{
								contextFilter.setParameter(param.getKey(), param.getValue());
							}
						}
					}
				}
			}
		
		
	}
	/**
	 * Only disable the filters that we have explicitly set ourselves
	 *
	 */
	protected void disableFilters(){
		//first disable any context filters
		Map<String,Map<String,Object>> contextfilters = filter.getContextFilters();
		for (String key : contextfilters.keySet()){
			session.disableFilter(key);
		}
		//if no authUser, then no auth filters
		if(filter.getAuthUser()==null){return;}
		
		//disable any auth filters
		Map<String,Map<String,Object>> authfilters = filter.getAuthUser().getAuthDaoFilters();
		for (String key : authfilters.keySet()){
			session.disableFilter(key);
		}
		
	}
	
	protected void setConnectionReadOnly(boolean readOnly){
		try{
			session.connection().setReadOnly(readOnly);
		}catch(Exception e){logger.warn(e.toString());}
	}
	
	protected List getResultsByRowSelectors(Criteria criteria){
		List results = getResultsByRowSelectors(criteria.scroll());
		return results;
	}
	protected List getResultsByRowSelectors(Query query){
	//	setConnectionReadOnly(true);
		query.setFirstResult(filter.getFirstRowNum());
		if(filter.getResultsCount()!= LavaDaoFilter.RESULT_COUNT_EMPTY){
			query.setMaxResults(filter.getLastRowNum()-filter.getFirstRowNum()+1);
		}
		List results = getResultsByRowSelectors(query.scroll());
	//	setConnectionReadOnly(false);
		return results;
	}
	protected List getResultsByRowSelectors(ScrollableResults cursor){
		List results = new ArrayList();

		try{
						
			if(!cursor.setRowNumber(filter.getFirstRowNum())){
				return results;
			}
			
			for(int i = filter.getFirstRowNum(); i <= filter.getLastRowNum();i++){
				
				results.add(cursor.get(0));	
				if (!cursor.next()){
					return results;
				}
			}
			return results;
		}catch(Exception e){
			logger.error(e.toString());
			if(results.size() > 0){
				
				return results;
			}else{
				return new ArrayList();
			}
		}finally{
			if (this.filter.getResultsCount() == LavaDaoFilter.RESULT_COUNT_EMPTY)
				{
				cursor.last();
				this.filter.setResultsCount(Integer.valueOf(cursor.getRowNumber()+1));
				}
			cursor.close();
		}
	}
}
