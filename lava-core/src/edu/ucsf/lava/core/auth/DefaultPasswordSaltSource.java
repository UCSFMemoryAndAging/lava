package edu.ucsf.lava.core.auth;

import org.acegisecurity.providers.dao.salt.ReflectionSaltSource;

/**
 * Default salt source for password encryption is the user id.
 * @author jhesse
 *
 */
public class DefaultPasswordSaltSource extends ReflectionSaltSource {

	public DefaultPasswordSaltSource() {
		super();
		//There is a bug in the version we are using where you have to specify getId instead of id, in the current version you can specify just "id"  we can change this when we update. 
		this.setUserPropertyToUse("getId");
	}

}
