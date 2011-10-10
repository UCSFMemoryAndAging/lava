package edu.ucsf.lava.core.dao.hibernate;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;

import edu.ucsf.lava.core.controller.ScrollablePagedListHolder;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;
import edu.ucsf.lava.core.dao.LavaDaoProjection;


public class CriteriaHibernateCallback extends LavaHibernateCallback{

	public static final int USE_ID_CACHE_THRESHOLD = 2000;
	protected Class entityClass;
	protected Criteria criteria;
	protected Projection projection;

	
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

	protected void applyProjections() {
		ProjectionList projectionList = Projections.projectionList();
		for(LavaDaoProjection projection:filter.getDaoProjections()){
			if(projection.getType().equalsIgnoreCase(LavaDaoProjection.TYPE_CRITERION)){
				DaoHibernateCriterionProjection criterionProjection = (DaoHibernateCriterionProjection)projection;
				projectionList.add(criterionProjection.getProjection());
			}else{
			logger.warn("Invalid LavaDaoProjection type:" + projection.getType() + " passed to a CriteriaQuery");
			}
		}
		this.projection = projectionList;
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

	/**
	 * This method gets the count of the results for the query (currently does this by 
	 * retrieving an idCache for the resultset and using the size of that cache as 
	 * the resultcount...see below).... 
	 * 
	 * This is used for situations where we are backing a paged list and specific rows have
	 * been requested in the query.  We used to do this by getting a count(*), 
	 * but it turns out that the most performant way for us to back paged lists
	 * is to retrieve an idCache for the query (essentially the id list for the 
	 * resultset sorted as requested by the query).  We then use this idCache to 
	 * retrieve the page with an IN(...) clause that includes the ids for the 
	 * specific rows requested.    This may not be the most performant way to 
	 * do this against MySQL but against other older db environments this was the
	 * best way.  We also get other programmtic benefits from having an idCache. 
	 * 
	 * If MySQL LIMIT/OFFSET mechanism (e.g. setInitialResult, setMaxResults) turns out
	 * to be the best performance approach to returning the records then we could 
	 * move away from the idCache.  
	 * 
	 * It is worth noting that getting an idCache and doing count(*) are not that different
	 * in terms of performance and we get so much more from the idCache. 
	 */
	protected void getResultsCount() throws HibernateException, SQLException {
			if(filter.getResultsCount()==LavaDaoFilter.RESULT_COUNT_EMPTY){
				criteria.setProjection(Projections.id());
				filter.setIdCache(criteria.list());
				filter.setResultsCount(filter.getIdCache().size());
				criteria.setProjection(null);
				criteria.setResultTransformer(Criteria.ROOT_ENTITY);
			}
	}		
			
	

	protected Object doQuery() throws HibernateException, SQLException {
		if(filter.rowSelectorsSet()){
			
			//if we have an id cache, retrieve rows based on an in() clause for just those ids
			
			//Note: when implementing the page size customization feature, we discovered that 
			// having more than 1000 items in the IN clause on SQL Server raises an exception
			// we ended up backing down to 250 max per page, but we might have to consider
			// an altenative query strategy here if that changes in the future.  - joe 
			if(this.projection!=null){
				criteria.setProjection(projection);
			}
			
			if(filter.getIdCache()!=null && filter.getIdCache().size()!=0){
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

