package edu.ucsf.lava.core.environment;

import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.metadata.ViewPropertyMessageSource;

public class EnvironmentManager extends LavaManager {
	public static String ENVIRONMENT_MANAGER_NAME = "environmentManager"; 
	protected ViewPropertyMessageSource messageSource;
	protected ApplicationServerDelegate applicationServerDelegate;
	protected String instanceName;
	

	public EnvironmentManager() {
		super(ENVIRONMENT_MANAGER_NAME);
	}
	


	public String getInstanceName(){
		if(applicationServerDelegate == null && instanceName==null){return "";}
		if(instanceName==null){
			instanceName = applicationServerDelegate.getInstanceName();
		}
		return instanceName;
	}

	
	public ApplicationServerDelegate getApplicationServerDelegate() {
		return applicationServerDelegate;
	}

	public void setApplicationServerDelegate(ApplicationServerDelegate applicationServerDelegate) {
		this.applicationServerDelegate = applicationServerDelegate;
	}
	
	
	
	
}
