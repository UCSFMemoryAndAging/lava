package edu.ucsf.lava.core.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
/**
 * This is a central repository for LavaManager instances.  It is implemented with an 
 * observer-observable pattern so that as specific managers are added to the repository
 * all other managers are notified (this facilitates intra-manager references and functionality).
 * 
 * LavaManagerUtils is a class with static methods that simplify and standardize location of 
 * manager instances. 
 * @author jhesse
 *
 */
public class Managers extends Observable{
	
	protected Map<String,LavaManager> managers = new HashMap<String,LavaManager>();
	
	public Map<String, LavaManager> getManagers() {
		return managers;
	}
	/**
	 * Adds a manager to the managers collection.  The current assumption is that each 
	 * manager in the system will have a unique name.  This method overrights
	 * existing managers. 
	 * @param manager
	 */
	public void AddManager(LavaManager manager){
		if(manager !=null){
			managers.put(manager.getName(), manager);
			this.setChanged();
			this.notifyObservers(manager);
		}
	}
	
	public LavaManager get(String manager){
		if(managers.containsKey(manager)){
			return managers.get(manager);
		}
		return null;
	}
	
		
	
	
	
}
