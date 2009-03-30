package edu.ucsf.lava.core.manager;


/**
 * Classes that implement this interface will be registered with 
 * the Managers instance using an observer pattern.  Every time a 
 * new manager instance is added to the Managers instance, each 
 * ManagersAware object will be called with the updateManagers method().
 * 
 * This allows ManagersAware objects to initialize local manager references
 * as the managers are being loaded into the application context by spring. 
 * 
 * There is no gaurantee that all managers will be initialized during each 
 * call to updateManagers().  The only gaurantee is that the last call to 
 * updateManagers() will have the full set of managers loaded.  
 * 
 * In practice, it is best to simply code the updateManagers method to re-set
 * any local manager references every time the method is called (e.g...
 * 
 * 		public void updateManagers(Manager managers){
 * 			this.actionManager = CoreManagerUtils.getActionManager(managers);
 * 			this.sessionManager = CoreManagerUtils.getActionManager(managers);
 * 		}
 * 
 * @author jhesse
 *
 */
public interface ManagersAware {
	
	/**
	 * This method is called on each observer every time a manager instance is 
	 * added to the managers instance.  
	 * 
	 * @param managers
	 */
	public void updateManagers(Managers managers);
}
