package edu.ucsf.lava.core.manager;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
/**
 * A bean post processor that registers ManagersAware beans with the managers
 * bean using an observer pattern.  This post processor is designed to make 
 * the spring configuration as simple as possible, in that there is no need
 * to inject the managers bean as a property on ManagersAware beans and there
 * is no assumption about precidence of bean loading.  
 * 
 * 
 * @author jhesse
 *
 */
public class ManagersAwareBeanPostProcessor implements BeanPostProcessor {

	
	Managers managers;
	
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		
		return bean;
	}
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {

		if(bean instanceof LavaManager){
			LavaManager manager = (LavaManager)bean;
			managers.AddManager(manager);
		}
		
		if(ManagersAware.class.isAssignableFrom(bean.getClass())){
			ManagersObserver observer = new ManagersObserver((ManagersAware)bean);
			managers.addObserver(observer);
			observer.update(managers, null);
		}
		return bean;
	}


	public void setManagers(Managers managers) {
		this.managers = managers;
	}
	
	

}
