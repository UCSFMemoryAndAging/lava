package edu.ucsf.lava.core.session;

import static edu.ucsf.lava.core.session.CoreSessionUtils.CORE_SCOPE;
import static edu.ucsf.lava.core.session.CoreSessionUtils.CURRENT_ACTION;
import static edu.ucsf.lava.core.session.CoreSessionUtils.CURRENT_USER;
import static edu.ucsf.lava.core.session.CoreSessionUtils.INTER_FLOW_FORM_ERRORS;

import javax.servlet.http.HttpServletRequest;

import edu.ucsf.lava.core.scope.AbstractScopeSessionAttributeHandler;

public class CoreSessionAttributeHandler extends AbstractScopeSessionAttributeHandler {
	
	public CoreSessionAttributeHandler() {
		super();
		this.setOrder(new Long(500));
		this.setHandledScope(CORE_SCOPE);
		this.addHandledAttribute(CURRENT_USER);
		this.addHandledAttribute(CURRENT_ACTION);
		this.addHandledAttribute(INTER_FLOW_FORM_ERRORS);
	}


	

	
	
	
}
