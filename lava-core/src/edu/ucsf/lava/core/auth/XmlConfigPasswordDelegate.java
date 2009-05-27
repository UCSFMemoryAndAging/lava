package edu.ucsf.lava.core.auth;

public class XmlConfigPasswordDelegate extends BasePasswordDelegate {

	
	

		public static final String AUTH_TYPE_XML_CONFIG = "XML CONFIG";
		
		public XmlConfigPasswordDelegate() {
			super();
			setAuthenticationType(AUTH_TYPE_XML_CONFIG);
			setSupportsPasswordChange(false);
			setSupportsPasswordReset(false);
			setPasswordResetMessageCode("xmlConfigPasswordDelegate.resetPassword");
			setPasswordChangeMessageCode("xmlConfigPasswordDelegate.changePassword");
		}


}
