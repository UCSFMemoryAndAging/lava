package edu.ucsf.lava.core.audit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.collection.PersistentBag;
import org.hibernate.collection.PersistentList;
import org.hibernate.collection.PersistentMap;
import org.hibernate.collection.PersistentSet;
import org.hibernate.event.Initializable;
import org.hibernate.event.PostDeleteEvent;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.PostLoadEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.type.Type;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.ucsf.lava.core.audit.model.AuditEntity;
import edu.ucsf.lava.core.model.EntityBase;

public class AuditHibernateListener implements PostDeleteEventListener, PostUpdateEventListener, PostInsertEventListener, 
PostLoadEventListener, Initializable, ApplicationContextAware{

	protected final Log logger = LogFactory.getLog(getClass());
	
	private AuditManager auditManager;
	private ApplicationContext context;
	
	

	

    public final void initialize(final Configuration cfg) {
    }

    

	public void onPostLoad(PostLoadEvent event) {
		if(!getAuditMonitor().objectIsAudited(event.getEntity())){return;}
		
		EntityBase entity = (EntityBase)event.getEntity();
		//store initial object state in entity for reference on save, update or delete events
		Object[] values = event.getPersister().getPropertyValues(entity, event.getPersister().guessEntityMode(entity));
		String[] names = event.getPersister().getPropertyNames();
		Type[] types = event.getPersister().getPropertyTypes();
		
		//loop through values changing PersistentCollections to the underlying java collections
		for(int i=0;i<values.length;i++){
			if(values[i] instanceof PersistentMap){
				Map map = new HashMap();
//				//only copy the values if already initialized, otherwise we end up forcing a lazy initialization on it. 
				if(((PersistentMap)values[i]).wasInitialized()){
						map.putAll((Map)values[i]);
				}
				values[i] = map;
				
			}else if (values[i] instanceof PersistentSet){
				Set set = new HashSet();
//				only copy the values if already initialized, otherwise we end up forcing a lazy initialization on it. 
				if(((PersistentSet)values[i]).wasInitialized()){
					set.addAll((Collection)values[i]);
				}
				values[i] = set;
			}else if (values[i] instanceof PersistentList){
				List list = new ArrayList();
//				only copy the values if already initialized, otherwise we end up forcing a lazy initialization on it. 
				if(((PersistentList)values[i]).wasInitialized()){
					list.addAll((Collection)values[i]);
				}
				values[i] = list;
			}else if (values[i] instanceof PersistentBag){
				List list = new ArrayList();
//				only copy the values if already initialized, otherwise we end up forcing a lazy initialization on it. 
				if(((PersistentBag)values[i]).wasInitialized()){
					list.addAll((Collection)values[i]);
				}
				values[i] = list;
			//handle simple arrays of objects by cloning them into a new array...	
			}else if (values[i]!=null && values[i].getClass().isArray()){
				List list = new ArrayList();
				Object[] array = (Object[])values[i];
				for(int j = 0;j<array.length;j++){
					list.add(array[j]);
				}
				values[i] = list.toArray();
			}
		}
		
		
		entity.setAuditState(values, names, types);
		return; 
		
	}

	
	public void onPostDelete(PostDeleteEvent event) {
		if(!getAuditMonitor().isCurrentEventAudited() || !getAuditMonitor().objectIsAudited(event.getEntity())){return;}
		
		EntityBase entity = (EntityBase)event.getEntity();
		 
		AuditEntity auditEntity = getAuditMonitor().auditEntity(entity, AuditEntity.AUDIT_TYPE_DELETE);
		if(auditEntity==null){return;}
			getAuditMonitor().auditProperties(auditEntity, entity, null, entity.getAuditStateValues(), entity.getAuditStateNames(), null, null);
		
		return; 
		
	}

	
	public void onPostUpdate(PostUpdateEvent event) {
		if(!getAuditMonitor().isCurrentEventAudited() || !getAuditMonitor().objectIsAudited(event.getEntity())){return;}

	
		EntityBase entity = (EntityBase)event.getEntity();
		  
		AuditEntity auditEntity = getAuditMonitor().auditEntity(entity, AuditEntity.AUDIT_TYPE_UPDATE);
		if(auditEntity==null){return;}
		
		Object[] values = event.getPersister().getPropertyValues(entity, event.getPersister().guessEntityMode(entity));
		String[] names = event.getPersister().getPropertyNames();
		
		getAuditMonitor().auditProperties(auditEntity, entity, null, entity.getAuditStateValues(), entity.getAuditStateNames(), values,names);
		
		return; 
		
	}




	public void onPostInsert(PostInsertEvent event) {
		if(!getAuditMonitor().isCurrentEventAudited() || !getAuditMonitor().objectIsAudited(event.getEntity())){return;}

		EntityBase entity = (EntityBase)event.getEntity();
		  
		AuditEntity auditEntity = getAuditMonitor().auditEntity(entity, AuditEntity.AUDIT_TYPE_INSERT);
		if(auditEntity==null){return;}
		Object[] values = event.getPersister().getPropertyValues(entity, event.getPersister().guessEntityMode(entity));
		String[] names = event.getPersister().getPropertyNames();
		getAuditMonitor().auditProperties(auditEntity, entity, null, entity.getAuditStateValues(), entity.getAuditStateNames(), values,names);
		return; 
		
	}


	



	
	public AuditManager getAuditMonitor() {
		if (auditManager != null){return auditManager;}
		try{
			auditManager = (AuditManager)context.getBean("auditManager");
		}catch(Exception e){
			//we will blow up here so don't worry about anything else....
			logger.error("Unable to load 'auditManager' bean from application context".concat(e.toString()));
		}
		return auditManager;
	}






	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}


	
}
