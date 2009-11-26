package edu.ucsf.lava.core.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.ucsf.lava.core.dao.LavaDao;
import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.model.LavaEntity;

public class LavaDaoHibernateImpl extends HibernateDaoSupport implements LavaDao {
	
	  public String getDatabaseName() {
			String queryString = "select database() as dbName";
	                String dbName = null;
			try {
			    List result = getSession().createSQLQuery(queryString).addScalar("dbName", org.hibernate.Hibernate.STRING).list();
			    if (result != null) {
			    	dbName = (String) result.get(0);
			    }
			    else {
			    	logger.error("getDatabaseName query returned no results");
			    }
			}
			catch (HibernateException he) {
				logger.error("getDatabaseName exception, he=" + he.getMessage());
			}
			return dbName;
	    }
	
	
	  
	public void save(Object object)  {
		if(LavaEntity.class.isAssignableFrom(object.getClass())){
			LavaEntity lavaEntity = (LavaEntity)object;
			if(lavaEntity.getId()==null){
				saveLavaEntity(lavaEntity);
			}else{
				updateLavaEntity(lavaEntity);
			}
		}else{
			getHibernateTemplate().saveOrUpdate(object);
			getHibernateTemplate().flush();
		}
	}
		
	protected void saveLavaEntity(LavaEntity entity){
		entity.beforeCreate();
		getHibernateTemplate().saveOrUpdate(entity);
		boolean resave = entity.afterCreate();
		if(resave){
			getHibernateTemplate().saveOrUpdate(entity);
		}
		getHibernateTemplate().flush();
	}

	protected void updateLavaEntity(LavaEntity entity){
		entity.beforeUpdate();
		getHibernateTemplate().saveOrUpdate(entity);
		boolean resave = entity.afterUpdate();
		if(resave){
			getHibernateTemplate().saveOrUpdate(entity);
		}
		getHibernateTemplate().flush();
	}
	
	

	
	//by default always refresh dependent objects
	public void refresh(Object object) {
		refresh(object,true);
	}
	
	public void refresh(Object object, boolean refreshDependents) {
		getHibernateTemplate().refresh(object);
		if(refreshDependents){
			doInitializeObject(object,"refresh");
		}
		
	}
	public void delete(Object object) {
		if(LavaEntity.class.isAssignableFrom(object.getClass())){
			deleteLavaEntity((LavaEntity)object);
		}else{
			getHibernateTemplate().delete(object);
			getHibernateTemplate().flush();
		}
	}

	protected void deleteLavaEntity(LavaEntity entity){
		entity.beforeDelete();
		getHibernateTemplate().delete(entity);
		getHibernateTemplate().flush();
		entity.afterDelete();
	}
	
	
	public Object create(Class clazz) {
		try{
			Object object = clazz.newInstance();
			return doPostCreate(object);	
		}catch(Exception e){
			logger.error(e.toString());
		}
		return null;
	}
	
	public List find(Class entityClass, LavaDaoFilter filter) {
		return doInitializeObjectList(new ArrayList((getHibernateTemplate()
		.executeFind(new CriteriaHibernateCallback(entityClass, filter)))),"find");
	}

	

	public Object findOne(Class entityClass, LavaDaoFilter filter){
		Object obj = getHibernateTemplate().execute(new GetUniqueCriteriaHibernateCallback(entityClass,filter));
		
		if (obj == null) {
			return obj;
		}
		else {
			return doInitializeObject(obj,"findById");
		}
	}
		

	protected Object doPostCreate(Object object) throws Exception{
		if(LavaEntity.class.isAssignableFrom(object.getClass())){
			return ((LavaEntity)object).postCreate();
		}
		return object;
	}
	
	public void initialize(Object entity){
		getHibernateTemplate().initialize(entity);
	}
	

	protected Object doInitializeObject(Object object, String method){
		if(LavaEntity.class.isAssignableFrom(object.getClass())){
			Object[] associations = ((LavaEntity)object).getAssociationsToInitialize(method);
			for(int i=0;i<associations.length;i++){
				initialize(associations[i]);
			}
		}
		return object;
	}
	
	protected List doInitializeObjectList(List objectList, String method){
		
		for(Object o: objectList){
			if(LavaEntity.class.isAssignableFrom(o.getClass()) &&
				((LavaEntity)o).initializeAssocationsForObjectLists(method)){
			doInitializeObject(o,method);
			}
		}
		return objectList;
	}

	
	public List findByNamedQuery(String namedQuery, LavaDaoFilter filter){
		return doInitializeObjectList(new ArrayList(getHibernateTemplate()
				.executeFind(new NamedQueryHibernateCallback(namedQuery,filter))),namedQuery);
	
		
	}
	
	public Object uniqueResult(List results) {
		return DataAccessUtils.uniqueResult(results);
	}
	
	protected List findByHqlQuery(String hqlQuery, LavaDaoFilter filter){
		return doInitializeObjectList(new ArrayList(getHibernateTemplate()
				.executeFind(new HqlQueryHibernateCallback(hqlQuery,filter))),hqlQuery);
	}
	
	
	public LavaDaoFilter newFilterInstance(){
		return new LavaDaoFilterHibernateImpl();
	}
	
	public Integer getResultCount(Class entityClass, LavaDaoFilter filter) {
		return (Integer)getHibernateTemplate().execute(new CriteriaResultCountHibernateCallback(entityClass,filter));
	}

	public void flushAndEvict(Object entity){
		getHibernateTemplate().flush();
		getHibernateTemplate().evict(entity);
	}

	public void evict(Object entity){
		getHibernateTemplate().evict(entity);
	}
	
	public List<Long> getEntityIds(Class entityClass, LavaDaoFilter filter){
		// the use of a select clause with the "entity" alias in the following query is necessary so
		// that for entities with with details, only the master entities are returned
		StringBuffer hqlQuery = new StringBuffer("select new java.lang.Long(entity.id) from ").append(entityClass.getName()).append(" entity where entity.id > 0");
		return findByHqlQuery(hqlQuery.toString(),filter);
	}
	
	
	
	public void executeSQLProcedure(String procedureName, Object[] paramValues, int[] paramTypes, char[] paramIOFlags){
		String procCall = this.formatSQLProcedureCall(procedureName, paramValues, paramTypes, paramIOFlags);
		getHibernateTemplate().execute(new StoredProcHibernateCallback(procCall,paramValues,paramTypes,paramIOFlags));
	}
	
	protected String formatSQLProcedureCall(String procedureName, Object[] paramValues, int[] paramTypes, char[] paramIOFlags){
		StringBuffer procCall = new StringBuffer("{call ").append(procedureName);
		if(paramValues.length>0){
			procCall.append("(");
			for(int i=0;i<paramValues.length;i++){
				procCall.append("?");
				if(i!=paramValues.length-1){
					procCall.append(",");
				}
			}
			procCall.append(")");
		}
		procCall.append("}");
		return procCall.toString();
	}


	
	

	
	
}