package edu.ucsf.lava.core.auth;

import edu.ucsf.lava.core.auth.model.AuthUser;

public class UcsfActiveDirectoryPasswordDelegate extends BasePasswordDelegate {

	public static final String AUTH_TYPE_UCSF_AD = "UCSF AD";
	
	public UcsfActiveDirectoryPasswordDelegate() {
		super();
		setAuthenticationType(AUTH_TYPE_UCSF_AD);
		setSupportsPasswordChange(false);
		setSupportsPasswordReset(false);
		setPasswordResetMessageCode("ucsfActiveDirectoryPasswordDelegate.resetPassword");
		setPasswordChangeMessageCode("ucsfActiveDirectoryPasswordDelegate.changePassword");
	}
}
