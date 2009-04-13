package edu.ucsf.lava.core.webflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionInterceptor;
import org.springframework.webflow.engine.RequestControlContext;
import org.springframework.webflow.engine.support.TransitionExecutingStateExceptionHandler;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.ViewSelection;

import edu.ucsf.lava.core.audit.AuditManager;

// this is the handler for exceptions which propagate unhandled to the flow, i.e. 
// this is the catch-all for any exceptions which are not handled.

// ideally, every exception should be handled, resulting in an ObjectError being created and
// a failure event being returned so that the flow remains in the current view state with the
// text from the ObjectError displayed. however, if this is not being done, this handler
// will catch the exception and the flow will transition to the error page, displaying the
// root cause of the exception. the user will only be able to use the navigation tabs
// to get somewhere which will terminate the current flow and start a new flow conversation,
// or, use the Back button, which should keep them in the current flow.
// note: the exception should be handled in the MVC layer, not within the service/DAO layers
// because it is the exception passing out of the service layer that signals the transaction
// handling to rollback transations in the database

public class CustomFlowExceptionHandler extends TransitionExecutingStateExceptionHandler{
	
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
	
	
	public CustomFlowExceptionHandler() {
		add(Exception.class, "error");
	}
	
	public boolean handles(FlowExecutionException ex) {
		return true;
	}
	
	public ViewSelection handle(FlowExecutionException exception, RequestControlContext context) {
		logger.error(exception.getMessage(), exception);
		
		// this is **critical**
		// if there is a transaction in progress, the transaction status must be set to rollbackOnly
		// so that there is not an attempt to commit the transaction. committing the transaction results
		// in an UnexpectedRollbackException thrown in a servlet filter, after the response has been
		// generated, so the response is not preserved. note that the transaction itself is already
		// marked for rollback by the transaction manager AOP code in the service layer and that the
		// transaction will be rolled back in any case, whether we mark the transaction status as
		// rollbackOnly or not --- but, instead of our error view page being displayed, a server
		// exception page is displayed and the user has no navigation options to get anywhere
		
		try{
			TransactionStatus status = TransactionInterceptor.currentTransactionStatus();
			if (status != null) {
				status.setRollbackOnly();
			}
		}catch(Exception e){
			logger.error("TransactionStatus null exception within CustomFlowExceptionHandler");
			
			//if we cause an exception while trying to rollback the transation then we likely had
			//no transaction in scope...so ignore...
		}
		
		//Need to clear any current "AuditEvent" upon an unhandled exception
		AuditManager.unhandledException(exception);
		
		// the superclass behavior is to get the state mapped to the given exception, create a
		// Transition to the state (which always matches and always executes), execute any transition
		// actions (i.e. any actions in getActionsList()) and then any render actions for the
		// target state, and Transition execute returns the view needed to render the results of
		// transition execution
		// e.g. transition to the error view-state, execute any render actions, and return the 
		// view for the error view-state
		// note: calling exposeException followed by
		// return new ApplicationView("/error", context.getModel().asMap()); will not work
		// because although the webflow context gets exposed to the view via the model, the 
		// Lava-specific reference data does not since prepareToRender is not called. thus, must
		// transition to a view-state instead, so that render actions can be executed 
		
		return super.handle(exception, context);
		//return null;
	}
	



	
}
