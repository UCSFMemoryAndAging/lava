package edu.ucsf.lava.core.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;
import edu.ucsf.lava.core.session.SessionManager;



public class CustomExceptionResolver implements HandlerExceptionResolver, ManagersAware {
	
	/** Logger for this class and subclasses */
        protected final Log logger = LogFactory.getLog(getClass());
        protected SessionManager sessionManager;
        protected ActionManager actionManager; 
       
    
    private String getStackTrace(Throwable t)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        t.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }

	 public ModelAndView resolveException(HttpServletRequest request,
				       HttpServletResponse response,
				       Object handler,
				       Exception ex) {

		 HashMap model = new HashMap();
		 model.put("request", request);
		 
		 
		 if (ex.getMessage() != null) {
			 model.put("exceptionMessage", ex.getMessage());
			 logger.error(ex.getMessage());
		 }
		 else {
			 model.put("exceptionMessage", ex.toString());
			 logger.error(ex.toString());
		 }
		 
		 model.put("exceptionStackTrace", getStackTrace(ex));
		 logger.error(getStackTrace(ex));
		  
		 // so that view will know whether there is a current patient and
		 // tab/subtab links can resolve default actions to URLs
		 model.put("actions",actionManager.getActionRegistry().getActions());
		 model.putAll(sessionManager.getContextFromSession(request));
		 
		 // this is **critical**
		 // if there is a transaction in progress, the transaction status must be set to rollbackOnly
		 // so that there is not an attempt to commit the transaction. because the transaction itself has been
		 // flagged to rollback, an UnexpectedRollbackException would be thrown during the commit, and
		 // because propagation REQUIRED is used for transactions (meaning the entire request is enclosed within
		 // a single transaction), the commit is not done until after the request is processed (i.e. in 
		 // a servlet filter), so an exception would not preserve the response, i.e. an error view with the
		 // error message would not get displayed. therefore, the way to prevent this commit from being
 		 // attempted so that the response is preserved is to set the transaction status to rollback.
		 try {
			 TransactionStatus status = TransactionInterceptor.currentTransactionStatus();
			 if (status != null) {
				 status.setRollbackOnly();
			 }
		 }
		 catch (Exception ex2) {
			 logger.error("TransactionStatus null exception within CustomExceptionResolver");
		 }

		 // TODO:
		 // determine url for them to go back to, either next to most recent link on
		 //  most recent items list, or home page. currently, this is being done
		 //  in the error.jsp itself, using prior action history

	     return new ModelAndView("/error", model);

	 }



	 
		


	 public void updateManagers(Managers managers) {
			this.actionManager = CoreManagerUtils.getActionManager(managers);
			this.sessionManager = CoreManagerUtils.getSessionManager(managers);
		}
		

	

}
