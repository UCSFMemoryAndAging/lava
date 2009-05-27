package edu.ucsf.lava.core.auth;

import org.acegisecurity.providers.encoding.ShaPasswordEncoder;

/**
 * Default Password Encoder uses SHA-256 encoding provided by spring security. 
 * @author jhesse
 *
 */
public class DefaultPasswordEncoder extends ShaPasswordEncoder {

	public DefaultPasswordEncoder() {
		super(256);
		}



}
