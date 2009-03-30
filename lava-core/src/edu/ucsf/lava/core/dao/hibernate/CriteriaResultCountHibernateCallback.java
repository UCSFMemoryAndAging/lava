package edu.ucsf.lava.core.dao.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Projections;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
public class CriteriaResultCountHibernateCallback extends
		CriteriaHibernateCallback {

	public CriteriaResultCountHibernateCallback(Class entityClass,
			LavaDaoFilter filter) {
		super(entityClass, filter);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object doQuery() throws HibernateException, SQLException {
		
		return criteria.setProjection(Projections.rowCount()).uniqueResult();
	}
	
	
}
