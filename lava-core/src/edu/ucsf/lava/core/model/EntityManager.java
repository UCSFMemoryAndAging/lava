package edu.ucsf.lava.core.model;


import java.util.List;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.home.model.Preference;

public interface EntityManager {

		public Class getEntityClass();
		public Object create();
		public void delete(Object object);
		public void evict(Object entity);
		public void lock(Object entity);
		public List get(LavaDaoFilter filter);
		public List getAndInitialize(Class clazz, LavaDaoFilter filter);
		public List get();
		public Object getById(Long id, LavaDaoFilter filter);
		public Object getById(Long id);
		public Object getOne(LavaDaoFilter filter);
		public void flushAndEvict(Object entity);
		public Integer getResultCount(LavaDaoFilter filter);
		public void initialize(Object entity);
		public LavaDaoFilter newFilterInstance();
		public LavaDaoFilter newFilterInstance(AuthUser user);
		public void refresh(Object object, boolean InitializeDependents);
		public  void refresh(Object object);
		public  void save(Object object);
		public  List findByNamedQuery(String namedQuery, LavaDaoFilter filter);
		public  Object findOneByNamedQuery(String namedQuery, LavaDaoFilter filter);
		public List<Long> getIds(LavaDaoFilter filter);
		public void executeSQLProcedure(String procedureName, Object[] paramValues, int[] paramTypes, char[] paramIOFlags);
		//useful methods for when we are working with an unknown (at compile time) subclass of EntityBase by specifying the class
		public  Object create(Class clazz);
		public  Object getOne(Class clazz, LavaDaoFilter filter);
		public  List get(Class clazz, LavaDaoFilter filter);
		public  Object getById(Class clazz,Long id);
		public  Object getById(Class clazz,Long id, LavaDaoFilter filter);
		public List<Long> getIds(Class entityClass, LavaDaoFilter filter);
			
}
