package edu.ucsf.lava.core.auth;

import edu.ucsf.lava.core.scope.AbstractScopeAuthorizationContext;

/**
 * The core scope has no authorization context, so this simply sets the 
 * scope identifier. 
 * @author jhesse
 *
 */
public class CoreAuthorizationContext extends AbstractScopeAuthorizationContext {
	
	public static final String CORE_SCOPE = "core";
	
	public CoreAuthorizationContext() {
		super(CORE_SCOPE);
	}

}
