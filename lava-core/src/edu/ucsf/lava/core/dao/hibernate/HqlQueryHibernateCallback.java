package edu.ucsf.lava.core.dao.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;

// this class is designed for use when an HQL query needs to be dynamically
// contructed such that a named query can not be used

public class HqlQueryHibernateCallback extends LavaHibernateCallback {
	private String hqlQuery;
	private Query query;
		
	public HqlQueryHibernateCallback(String hqlQuery,  LavaDaoFilter filter){
		super(filter);
		this.hqlQuery = hqlQuery;
	}
	
	public void getResultsCount(){
		
	}
	
	public Object doQuery() throws HibernateException, SQLException {
		if(filter.rowSelectorsSet()){
			return this.getResultsByRowSelectors(this.query.scroll());
		}else{
			return this.query.list();
		}
	}

	protected void applyParameter(LavaDaoParam param) {
		if(param.getType().equalsIgnoreCase(LavaDaoParam.TYPE_NAMED)){
			DaoHibernateNamedParam namedParam = (DaoHibernateNamedParam)param;
			query.setParameter(namedParam.getParamName(),namedParam.getParamValue());
		}else if (param.getType().equalsIgnoreCase(LavaDaoParam.TYPE_POSITIONAL)){
			DaoHibernatePositionalParam posParam = (DaoHibernatePositionalParam)param;
			query.setParameter(posParam.getParamPos(),posParam.getParamValue());
		}else{
			logger.warn("Invalid LavaDaoParam type:" + param.getType() + " passed to a NamedQuery");
		}
	}

	protected void applySort(String Property, Boolean Ascending) {
		//do nothing as HQL queries do not have custom sort options,,,	
		return;
	}


	protected void initQuery() {
		this.query = session.createQuery(this.hqlQuery);
		
	}

	@Override
	protected void applyAlias(String collection, String alias) {
		//no aliases in HQL queries
		return;

		
	}
	protected void applyOuterAlias(String collection, String alias) {
		//no aliases in HQL queries
		return;

		
		
	}
	protected void applyProjections() {
		//do nothing as hql queries do not have custom projections	
			return;
		}
	
}

