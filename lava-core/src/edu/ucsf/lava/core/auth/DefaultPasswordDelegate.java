package edu.ucsf.lava.core.auth;

import org.acegisecurity.providers.encoding.ShaPasswordEncoder;

import edu.ucsf.lava.core.auth.model.AuthUser;

public class DefaultPasswordDelegate extends BasePasswordDelegate {

	public DefaultPasswordDelegate() {
		super();
		setAuthenticationType(AuthUser.AUTH_TYPE_LOCAL);
		this.setPasswordValidator(new SimplePasswordValidator());
		this.setPasswordEncoder(new DefaultPasswordEncoder());
		this.setPasswordSaltSource(new DefaultPasswordSaltSource());
		this.setPasswordChangeMessageCode("defaultPasswordDelegate.passwordChangeMessage");
		this.setPasswordResetMessageCode("defaultPasswordDelegate.passwordResetMessage");
	}

	
	
	
	
}
