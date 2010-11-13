package edu.ucsf.lava.core.model;

import java.util.List;

import edu.ucsf.lava.core.auth.model.AuthUser;
import edu.ucsf.lava.core.dao.LavaDao;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.spring.LavaBeanUtils;

public class EntityManagerBase {

		protected LavaDao dao;
		protected String daoBeanId;
		
		
		public EntityManagerBase(){
			this("lavaDao");
		}
		
		public EntityManagerBase(String daoBeanId){
			this.daoBeanId = daoBeanId;
		}
		
		
		protected LavaDao getDao(){
			if(dao!=null){return dao;}
			dao = (LavaDao) LavaBeanUtils.get(daoBeanId);
			return dao;
		}
		
		
		public  Object create(Class entityClass) {
			return getDao().create(entityClass);
		}
		
		public  void delete(Object object) {
			getDao().delete(object);
		}
		public  void evict(Object entity) {
			getDao().evict(entity);
		}
		public  List get(Class entityClass,LavaDaoFilter filter) {
			return getDao().find(entityClass, filter);
		}
		
		public  List get(Class entityClass) {
			return getDao().find(entityClass,newFilterInstance(null));
		}
		public Object getById(Class entityClass, Long id){
			LavaDaoFilter filter = getDao().newFilterInstance();
			return getDao().findOne(entityClass, filter.addIdDaoEqualityParam(id));
		}
		public  Object getById(Class entityClass, Long id, LavaDaoFilter filter) {
			return getDao().findOne(entityClass, filter.addIdDaoEqualityParam(id));
		}
		public  Object getOne(Class entityClass, LavaDaoFilter filter) {
			return getDao().findOne(entityClass,filter);
		}
		
		public void flushAndEvict(Object entity) {
			getDao().flushAndEvict(entity);
		}
		public  Integer getResultCount(Class entityClass,LavaDaoFilter filter) {
			return getDao().getResultCount(entityClass, filter);
		}
		public  void initialize(Object entity) {
			getDao().initialize(entity);
		}
		

		public  LavaDaoFilter newFilterInstance() {
			return newFilterInstance(null);
		}
		public  LavaDaoFilter newFilterInstance(AuthUser user) {
			LavaDaoFilter filter = getDao().newFilterInstance();
			if(user != null){
				filter.setAuthUser(user);
			}
			return filter;
		}
		public  void refresh(Object object, boolean InitializeDependents) {
			getDao().refresh(object, InitializeDependents);
		}
		public  void refresh(Object object) {
			getDao().refresh(object);
		}
		public  void save(Object object) {
			getDao().save(object);
		}
		
		public  List findByNamedQuery(String namedQuery, LavaDaoFilter filter){
			return getDao().findByNamedQuery(namedQuery, filter);
			
		}
		
		public Object findOneByNamedQuery(String namedQuery, LavaDaoFilter filter) {
			List results = getDao().findByNamedQuery(namedQuery, filter);
			return getDao().uniqueResult(results);
		}

		public List<Long> getIds(Class entityClass){
			return getIds(entityClass,newFilterInstance());
		}
		
		public List<Long> getIds(Class entityClass, LavaDaoFilter filter){
			return getDao().getEntityIds(entityClass,filter);
		}
		
		
		public void executeSQLProcedure(String procedureName, Object[] paramValues, int[] paramTypes, char[] paramIOFlags){
			getDao().executeSQLProcedure(procedureName, paramValues, paramTypes, paramIOFlags);
		}
		
}
