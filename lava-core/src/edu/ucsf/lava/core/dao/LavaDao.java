package edu.ucsf.lava.core.dao;

import java.util.List;

public interface LavaDao {
	public String getDatabaseName();
	public void save(Object object) ;
	public Object create(Class clazz) ;
	public void delete(Object object) ;
	public void refresh(Object object);
	public void refresh(Object object, boolean InitializeDependents);
	public Object findOne(Class clazz, LavaDaoFilter filter);
	public List find(Class entityClass, LavaDaoFilter filter);
	public List findByNamedQuery(String namedQuery, LavaDaoFilter filter);
	public Object uniqueResult(List results);
	public LavaDaoFilter newFilterInstance();
	public Integer getResultCount(Class clazz, LavaDaoFilter filter);	
	public void flushAndEvict(Object entity);
	public void evict(Object entity);
	public void initialize(Object entity);
	public List<Long> getEntityIds(Class entityClass,LavaDaoFilter filter);
	public void executeSQLProcedure(String procedureName, Object[] paramValues, int[] paramTypes, char[] paramIOFlags);
}
