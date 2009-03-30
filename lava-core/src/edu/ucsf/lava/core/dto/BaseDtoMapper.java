package edu.ucsf.lava.core.dto;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BaseDtoMapper {

	protected static final Log logger = LogFactory.getLog(BaseDtoMapper.class);
	
	public static boolean mapModelFromDto(Object model,Object dto){
		return mapObjectFromObject(model,dto);
	}
	
	public static boolean mapDtoFromModel(Object dto,Object model){
		return mapObjectFromObject(dto,model);
	}
	
	public static boolean mapObjectFromObject(Object to,Object from){
		try{
			PropertyUtils.copyProperties(to, from);
			return true;
		}catch(Exception e){
			logger.error(e.toString());
			return false;
		}
	}
}
