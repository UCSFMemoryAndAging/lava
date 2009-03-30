package edu.ucsf.lava.core.action;

import static edu.ucsf.lava.core.session.CoreSessionUtils.CURRENT_ACTION;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.LavaManager;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.scope.ScopeActionDelegate;
import edu.ucsf.lava.core.scope.ScopeManager;
import edu.ucsf.lava.core.session.CoreSessionUtils;
import edu.ucsf.lava.core.session.SessionManager;


public class ActionManager extends LavaManager {
	
	public static String ACTION_MANAGER_NAME="actionManager";
	
	public static String webappInstanceName = initializeWebAppInstanceName();
	protected ActionDefinitions actionDefinitions;
	protected ActionRegistry actionRegistry = new ActionRegistry();
	protected ScopeManager scopeManager;
	protected SessionManager sessionManager;
	/** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

	
	public ActionManager(){
		super(ACTION_MANAGER_NAME);
	}
	
	public void updateManagers(Managers managers) {
		super.updateManagers(managers);
		scopeManager = CoreManagerUtils.getScopeManager(managers);
		sessionManager = CoreManagerUtils.getSessionManager(managers);
	}


	/**
	 *  create a registry (map) of action lookup keys to actions from the 
	 *  actionDefinitions.  Encapsulates the logic for generating the registry
	 *  to the action scope delegates.  This facilitates abstracting knowledge of 
	 *  scope specific default action rules out of the core. 
	 */
	public void reloadActionDefinitions(){
		//Copy all action definitions into temp action registry
		ActionRegistry registry = new ActionRegistry();
		registry.setActions(actionDefinitions.getDefinitionsCopy());
		
		//pass the registry to each scope delgate (in order)
		for(ScopeActionDelegate delegate : scopeManager.getActionDelegates().getOrderedDelegates()){
			registry = delegate.onReloadActionDefinitions(this,registry);
		}
		//set the generated registry as the current actionRegistry
		this.actionRegistry = registry;
		
	}
	
	
	public Action getCurrentAction(HttpServletRequest request){
		return (Action)request.getSession().getAttribute(CURRENT_ACTION);
	}
	
	public void setCurrentAction(HttpServletRequest request, Action action){
		request.getSession().setAttribute(CURRENT_ACTION,action);
	}
	
	
	
	
		
	
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}


	/**
	 * Get the action using the id
	 * would be loaded by getEffectiveRuntimeAction()
	 * @param actionId
	 * @return
	 */
	public Action getAction(String actionId){
		if(actionRegistry.containsAction(actionId)){
			return (Action) actionRegistry.getAction(actionId);
		}
		return null;
	}
	

	/**
	 * Determine the effective "runtime" action based on the actionId passed in and the
	 * current runtime conditions (e.g. what instance is running). 
	 * 
	 * @param request
	 * @param actionId
	 * @return
	 */
	public Action getEffectiveAction(HttpServletRequest request, String actionId)
	{
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(ActionUtils.getScope(actionId));
		if(delegate==null){return null;}
		return delegate.resolveEffectiveAction(this,request,actionId);
		
		

	}
	
	/**
	 * Convenience method that determines the effective action (in the absense
	 * of an active request)
	 * 
	 * @param actionId
	 * @return
	 */
	public Action getEffectiveAction(String actionId)
	{
		return getEffectiveAction(null,actionId);
	}
	
	
   /**
    * Determines the flow id based on the request.  
    * @return
    */
	public String extractFlowIdFromRequest(HttpServletRequest request){
		Action action = this.getEffectiveAction(request, ActionUtils.getActionId(request));
		if(action==null){return null;}
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(action);
			if(delegate==null){return null;}
		return delegate.extractFlowIdFromAction(this, request, action);
	}
	
	
	/**
	 * The most accurate method for determining the instance of the currently running app 
	 * within jboss without access to the servlet context. 
	 * 
	 * @return
	 */
	private static String initializeWebAppInstanceName(){
		//		 determine the context path which is the webapp instance name (e.g. "mac", "examiner")
  	// as this is used to determine if any instance specific FormAction beans should be used
  	// when creating the flows
  	// because the ServletContext is not available here, determine the context path by obtaining
  	// the path to WEB-INF/web.xml and working from there using String methods
      // e.g. in exploded WAR deployment (i.e. development) the path will be something like:
	//   /home/ctoohey/project/webapp/mac.war/WEB-INF/web.xml, the goal is
  	// however, in a WAR deployment, JBoss explodes the WAR into a temporary directory 
	// (../server/SERVER_NAME/tmp/...) and the path will be something like:
	//   /usr/local/jboss-4.0.2/server/dev-ctoohey/tmp/deploy/tmp52223mac.war/WEB-INF/web.xml
	//   where the webapp name is always preceded by "tmpNNNNN.." where N is a digit
	// in either example, the goal is to parse out "mac" as the webapp instance name
	String webAppInstanceName;
  	ResourceLoader resourceLoader = new DefaultResourceLoader();
  	Resource resource = resourceLoader.getResource("WEB-INF/web.xml");
	Log logger = LogFactory.getLog(ActionManager.class);
       try {
	        //logger.info("abolutePath=" + resource.getFile().getAbsolutePath());
	        //logger.info("canonicalPath=" + resource.getFile().getCanonicalPath());
	        logger.info("WEB-INF/web.xml path=" + resource.getFile().getPath());
	        String path = resource.getFile().getPath();

	        // parse out the webapp instance name from the path
	                
	        int endIndex = path.indexOf(".war/WEB-INF");
	        if (endIndex == -1){
		    // on Windows platform look forith path separator of '\'
		    endIndex = path.indexOf(".war\\WEB-INF");
	        }

	        // 	starting from endIndex, look backwards for the beginning index

	        // first look for path separator on Unix platforms, '/'
	        int beginIndex = path.lastIndexOf("/", endIndex);
	        // if on Windows platform, look for '\' instead
	        if (beginIndex == -1) {
	        	beginIndex = path.lastIndexOf("\\", endIndex);
	        }

	        // 	now determine whether this is an exploded WAR deployment or a WAR 
	        // file deployment
	        int tmpIndex = path.indexOf("tmp", beginIndex);
	        if (tmpIndex == -1) {
	        // if not a tmp dir, this is an exploded WAR deployment
	        	webappInstanceName = path.substring(beginIndex+1, endIndex); 
	        }
	        else {
		    // this is a WAR file deployment exploded into a tmp directory where
		    // the webapp directory is "tmpNNNN...WEBAPPNAME.war"
		    // tmpIndex is position at 't', and endIndex is positioned at '.'
	        	String tempStr = path.substring(tmpIndex+3, endIndex); 
	        	// tempStr = NNNN...WEBAPPNAME
	        	// search for the first character after the digits
	        	int i=0;
	        	for (i=0; i<tempStr.length(); i++) {
	        		if (tempStr.charAt(i) < '0' || tempStr.charAt(i) > '9') {
	        			break;
	        		}
	        	}
	        	webappInstanceName = tempStr.substring(i);
	        	// 	the exploded tmp dir may also have "-exp" appended to the name, e.g.
	        	// "tmpNNNN...WEBAPPNAME-exp.war", so look for "-exp" and remove if present
	        	tmpIndex = webappInstanceName.indexOf("-exp");
	        	if (tmpIndex != -1) {
	        		webappInstanceName = webappInstanceName.substring(0,tmpIndex);
	        	}
	        }
       }
       catch (IOException ex) {
      	logger.error("resourceLoader exception trying to find context path", ex);
      }
      logger.info("webappInstanceName=<" + webappInstanceName + ">");
      return webappInstanceName;
	}
	
	
	/*
	 * Given an actionId, determine if there is a corresponding instance specific actionId
	 * for which flows will be built (in which case these instance specific flows will always
	 * be used instead of the corresponding core flows) or whether the core actionId should
	 * be used to build the flows.
	 * 
	 * @return effective actionId used to construct the flow id 
	 */
	public boolean shouldBuildFlowsForAction(String actionId) {
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(ActionUtils.getScope(actionId));
		if(delegate==null){return false;}
		return delegate.shouldBuildFlowsForAction(this,actionId);
	}
		

	
	
	
	public Action getDefaultAction(HttpServletRequest request,Action actionIn){
		ScopeActionDelegate delegate = scopeManager.getActionDelegate(actionIn);
		if(delegate==null){return null;}
		return delegate.getDefaultAction(this,request, actionIn);
	}
	
	
	public Action getDefaultAction(HttpServletRequest request)
	{
		return getDefaultAction(request,CoreSessionUtils.getCurrentAction(sessionManager, request));
		
	}

	

	public ActionDefinitions getActionDefinitions() {
		return actionDefinitions;
	}

	public void setActionDefinitions(ActionDefinitions actionDefinitions) {
		this.actionDefinitions = actionDefinitions;
	}

	
	
	
	
}
