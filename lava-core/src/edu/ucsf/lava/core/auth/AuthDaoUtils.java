package edu.ucsf.lava.core.auth;

import java.util.Date;

import edu.ucsf.lava.core.dao.LavaDaoFilter;
import edu.ucsf.lava.core.dao.LavaDaoParam;

public class AuthDaoUtils {

	
	public static LavaDaoParam getEffectiveDaoParam(String alias, LavaDaoFilter filter){
		return getEffectiveDaoParam(alias,filter, new Date());
	}
	public static LavaDaoParam getEffectiveDaoParam(LavaDaoFilter filter){
		return getEffectiveDaoParam(null,filter, new Date());
	}
	public static LavaDaoParam getEffectiveDaoParam(LavaDaoFilter filter, Date effectiveDate){
		return getEffectiveDaoParam(null,filter, effectiveDate);
	}
	public static LavaDaoParam getEffectiveDaoParam(String alias, LavaDaoFilter filter, Date effectiveDate){
		String effectiveProp = "effectiveDate";
		String expirationProp = "expirationDate";
		if(alias!=null){
			effectiveProp = new StringBuffer(alias).append(".").append(effectiveProp).toString();
			expirationProp = new StringBuffer(alias).append(".").append(expirationProp).toString();
		}
		return filter.daoAnd(
								filter.daoLessThanOrEqualParam(effectiveProp, effectiveDate),
								filter.daoOr(
										filter.daoNull(expirationProp),
										filter.daoGreaterThanParam(expirationProp,effectiveDate)
										)
								);
		}
}
