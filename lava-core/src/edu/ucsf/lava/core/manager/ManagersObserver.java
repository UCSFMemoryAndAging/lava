package edu.ucsf.lava.core.manager;

import java.util.Observable;
import java.util.Observer;

public class ManagersObserver implements Observer {

	ManagersAware aware;
	public ManagersObserver(ManagersAware aware){
		this.aware = aware;
	}
	
	public void update(Observable managers, Object notUsed) {
		if(this.aware!=null && managers!=null && Managers.class.isAssignableFrom(managers.getClass())){
			aware.updateManagers((Managers)managers);
		}

	}

}
