package edu.ucsf.lava.core.dao.hibernate;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;

public class CriteriaHibernateCallback extends LavaHibernateCallback{

	public static final int USE_ID_CACHE_THRESHOLD = 2000;
	protected Class entityClass;
	protected Criteria criteria;

	
	public CriteriaHibernateCallback(Class entityClass,LavaDaoFilter filter){
		super(filter);
		this.entityClass = entityClass;
		
	}
	
	
	protected void applyParameter(LavaDaoParam param) {
		if(param.getType().equalsIgnoreCase(LavaDaoParam.TYPE_CRITERION)){
			DaoHibernateCriterionParam criterionParam = (DaoHibernateCriterionParam)param;
			criteria.add(criterionParam.getCriterion());
			
		}else{
			logger.warn("Invalid LavaDaoParam type:" + param.getType() + " passed to a CriteriaQuery");
		}
	}


	protected void applySort(String property, Boolean ascending) {
		for (String sort : StringUtils.split(property,',')){
			if(ascending){
				criteria.addOrder(Order.asc(sort));
			}else{
				criteria.addOrder(Order.desc(sort));
			}
		}
	}

	protected void getResultsCount() throws HibernateException, SQLException {
			if(filter.getResultsCount()==LavaDaoFilter.RESULT_COUNT_EMPTY){
				filter.setResultsCount((Integer)criteria.setProjection(Projections.rowCount()).uniqueResult());
				criteria.setProjection(null);
				criteria.setResultTransformer(Criteria.ROOT_ENTITY);
			}
	}		
			
	

	protected Object doQuery() throws HibernateException, SQLException {
		if(filter.rowSelectorsSet()){
			//row count already retrieved with separate query at this point. 
			
			//if no idCache and there are more than 2000 rows and the rowSelection is not equal to the defaults for a
			//scrollable page list then build an idCache 
			if(filter.getIdCache()==null && filter.getResultsCount()>this.USE_ID_CACHE_THRESHOLD
					&& filter.getLastRowNum() >= ScrollablePagedListHolder.DEFAULT_INITIAL_ELEMENTS){
				criteria.setProjection(Projections.id());
				filter.setIdCache(criteria.list());
				criteria.setProjection(null);
				criteria.setResultTransformer(Criteria.ROOT_ENTITY);
			}
			
			
			
			//if we have an id cache, retrieve rows based on an in() clause for just those ids
			
			//Note: when implementing the page size customization feature, we discovered that 
			// having more than 1000 items in the IN clause on SQL Server raises an exception
			// we ended up backing down to 250 max per page, but we might have to consider
			// an altenative query strategy here if that changes in the future.  - joe 
			
			if(filter.getIdCache()!=null){
				criteria.add(((DaoHibernateCriterionParam)filter.daoInIdCacheParam()).getCriterion());
				return criteria.list();
			}
			
			//otherwise, just get the results using a scrolling cursor
			return this.getResultsByRowSelectors(criteria);
		}else{ 
			//no row selectors, just get all
			return criteria.list();
		}
	}


	protected void initQuery() {
		criteria = session.createCriteria(entityClass);
	}


	@Override
	protected void applyAlias(String collection, String alias) {
		criteria.createAlias(collection,alias,Criteria.INNER_JOIN);
		
	}
	
	protected void applyOuterAlias(String collection, String alias) {
		criteria.createAlias(collection,alias,Criteria.LEFT_JOIN);
		
	}
}

