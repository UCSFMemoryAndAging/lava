package edu.ucsf.lava.core.logiccheck;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import edu.ucsf.lava.core.logiccheck.model.LogicCheckEntityConfig;

public class LogicCheckEntityConfigBeanPostProcessor implements BeanPostProcessor {

	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (LogicCheckEntityConfig.class.isAssignableFrom(bean.getClass())){
			LogicCheckEntityConfig configBean = (LogicCheckEntityConfig)bean;
			/*
			if(StringUtils.isEmpty(configBean.getEntityName())){
				configBean.setEntityName(beanName);
			}
		*/
		}
		return bean;
	}
	
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {

		if (LogicCheckEntityConfig.class.isAssignableFrom(bean.getClass())){
			LogicCheckEntityConfig configBean = (LogicCheckEntityConfig)bean;
			configBean.getEntityConfigs().addLogicCheckEntityConfig(configBean);
		}
		return bean;
	}


}
