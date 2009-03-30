package edu.ucsf.lava.core.action.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


/**
 *
 * @author jhesse
 *
 */
public class ActionBeanPostProcessor implements BeanPostProcessor {

	
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		
		if (BaseAction.class.isAssignableFrom(bean.getClass())){
			BaseAction actionBean = (BaseAction)bean;
			if(StringUtils.isEmpty(actionBean.getId())){
				actionBean.setId(beanName);
			}
		}
		
		
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (BaseAction.class.isAssignableFrom(bean.getClass())){
			BaseAction actionBean = (BaseAction)bean;
			actionBean.getActionDefinitions().addActionDefinition(actionBean);
		}
		return bean;
	}

}
