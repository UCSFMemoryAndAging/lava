package edu.ucsf.lava.core.dao.hibernate;

import java.sql.SQLException;

import org.hibernate.HibernateException;

import edu.ucsf.lava.core.dao.LavaDaoFilter;

public class GetUniqueCriteriaHibernateCallback extends CriteriaHibernateCallback {

	

	public GetUniqueCriteriaHibernateCallback(Class entityClass, LavaDaoFilter filter){
		super(entityClass,filter);
	}


	protected Object doQuery() throws HibernateException, SQLException {
		// TODO Auto-generated method stub
		return criteria.uniqueResult();
	}
	
	
}


