package edu.ucsf.lava.core.dao.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.ThrowsAdvice;

/**
 * This is a bean configured on the AOP transction proxy which uses AOP to wrap DAO calls
 * in a transaction. If there is an exception within the DAO the afterThrowing method is
 * invoked, giving a chance to log details about the exception thrown. Otherwise, such
 * details are lost (in particular the stack trace) when the exception bubbles up thru
 * the AOP wrapper. 
 * 
 * @author ctoohey
 *
 */

public class LavaThrowAdvice implements ThrowsAdvice {
	protected final Log logger = LogFactory.getLog(getClass());
	 
	public void afterThrowing(Exception ex) throws Throwable {
		logger.error("LavaThrowAdvice Exception Details:", ex);
	}
	
}
