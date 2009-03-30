package edu.ucsf.lava.core.manager;


public class LavaManager implements ManagersAware{

	protected String name;  //unique name for the manager 
	
	public LavaManager(){
		super();
	}
	
	public LavaManager(String name){
		this();
		this.name = name;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Override in specific subclasses to initialize local manager references.
	 * @param managers
	 */
	public void updateManagers(Managers managers) {
	}

	
	
}
