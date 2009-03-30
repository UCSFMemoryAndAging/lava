package edu.ucsf.lava.core.session;

import edu.ucsf.lava.core.manager.AppInfo;


/**
 * This interface abstracts out the specific servlet container implementation using
 * a delegate pattern.  Any container specific methods should be added to this interface
 * and implemented for each supported container. 
 * @author jhesse
 *
 */
public interface LavaServerInstanceDelegate {

	public String getServerDescription(AppInfo appInfo);
}
