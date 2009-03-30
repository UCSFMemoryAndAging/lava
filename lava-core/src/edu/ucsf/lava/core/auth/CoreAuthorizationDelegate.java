package edu.ucsf.lava.core.auth;

import edu.ucsf.lava.core.model.LavaEntity;
import edu.ucsf.lava.core.scope.AbstractScopeAuthorizationDelegate;
import edu.ucsf.lava.core.session.CoreSessionUtils;

public class CoreAuthorizationDelegate extends
		AbstractScopeAuthorizationDelegate {


	public CoreAuthorizationDelegate(){
		super();
		this.handledScope = CoreSessionUtils.CORE_SCOPE;
	}
	
	public AuthorizationContext newAuthorizationContext() {
		return new CoreAuthorizationContext();
	}

	public AuthorizationContext newAuthorizationContext(LavaEntity entity) {
		return newAuthorizationContext();
	}

	public AuthorizationContext newMatchesAllAuthorizationContext() {
		return newAuthorizationContext();
	}

}
