package edu.ucsf.lava.crms.assessment.model;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 *	 *
 * @author jhesse
 *
 */
public class InstrumentConfigBeanPostProcessor implements BeanPostProcessor {

	
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		
		if (InstrumentConfig.class.isAssignableFrom(bean.getClass())){
			InstrumentConfig configBean = (InstrumentConfig)bean;
			if(StringUtils.isEmpty(configBean.getInstrTypeEncoded())){
				configBean.setInstrTypeEncoded(beanName);
			}
		}
		return bean;
	}

	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {

		if (InstrumentConfig.class.isAssignableFrom(bean.getClass())){
			InstrumentConfig configBean = (InstrumentConfig)bean;
			configBean.getInstrumentDefinitions().addInstrumentDefinition(configBean);
		}
		return bean;
	}


}
