package edu.ucsf.lava.core.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import edu.ucsf.lava.core.dao.hibernate.LavaDaoHibernateImpl;

public class LavaBeanUtils implements ApplicationContextAware{

	/** Logger for this class and subclasses */
    protected static final Log logger = LogFactory.getLog(LavaDaoHibernateImpl.class);
	static private ApplicationContext context; //used to get reference to transaction proxy of this singleton 
	
	
	static public Object get(String beanId){
	
		try{
			return context.getBean(beanId);
		}catch(Exception e){
			//we will blow up here so don't worry about anything else....this is a critical failure of the system
			logger.error("Unable to load bean '".concat(beanId).concat("' from application context").concat(e.toString()));
			return null;
		}		
	
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext; 
	}


	
	
}
