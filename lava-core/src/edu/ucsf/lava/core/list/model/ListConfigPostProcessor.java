package edu.ucsf.lava.core.list.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;



/**
 *
 * @author jhesse
 *
 */
public class ListConfigPostProcessor implements BeanPostProcessor {

	
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		
		if (BaseListConfig.class.isAssignableFrom(bean.getClass())){
			BaseListConfig listBean = (BaseListConfig)bean;
			if(StringUtils.isEmpty(listBean.getName())){
				if(beanName.startsWith(BaseListConfig.LIST_CONFIG_BEAN_NAME_PREFIX)){
					listBean.setName(beanName.substring(BaseListConfig.LIST_CONFIG_BEAN_NAME_PREFIX.length()));
				}else{
					listBean.setName(beanName);
				}
			}
		
		}
		
		
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		if (BaseListConfig.class.isAssignableFrom(bean.getClass())){
			BaseListConfig listBean = (BaseListConfig)bean;
			listBean.getListDefinitions().addListDefinition(listBean);
		}
		return bean;
	}

}
