package edu.ucsf.lava.core.action;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.execution.RequestContext;

import edu.ucsf.lava.core.action.model.Action;

public class ActionUtils {

	public static final int INSTANCE_ID_INDEX=0;
	public static final int SCOPE_ID_INDEX=1;
	public static final int MODULE_ID_INDEX=2;
	public static final int SECTION_ID_INDEX=3;
	public static final int TARGET_ID_INDEX=4;
	public static final int MODE_ID_INDEX=5; // for flow id's

	public static final int INSTANCE_URL_INDEX=0;
	public static final int SCOPE_URL_INDEX=1;
	public static final int MODULE_URL_INDEX=2;
	public static final int SECTION_URL_INDEX=3;
	public static final int TARGET_URL_INDEX=4;
	public static final int PARAMS_URL_INDEX=5;
	
	public static final String MODE_PARAMETER_NAME = "_do";
	public static final String EVENT_PARAMETER_NAME = "_eventId";
	public static final String OBJECT_EVENT_SEPARATOR = "__";
	
	public static final String LAVA_INSTANCE_IDENTIFIER = "lava";
	public static final String DEFAULT_LOCAL_IDENTIFIER = "local";
	public static final String CORE_SCOPE_IDENTIFIER = "core";

	public static final String DEFAULT_ACTION_SCOPE_IDENTIFIER = "defaultScope";
	public static final String DEFAULT_ACTION_IDENTIFIER = "defaultAction";
	public static final String MODULE_DEFAULT_SECTION_IDENTIFIER = "module";
	public static final String EVENT_FORMAT = "(.*)".concat(OBJECT_EVENT_SEPARATOR).concat("(.*)");
	public static final String ACTION_PATH_DELIMITER = "/";
	public static final String ACTION_TARGET_EXTENSION = ".lava";
	public static final String ACTION_ID_DELIMITER = ".";
	

	public static final String ACTIONID_FORMAT = "(.*).(.*).(.*).(.*).(.*)";
	public static final String ACTIONURL_FORMAT = "(.*)/(.*)/(.*)/(.*)/(.*)\\.lava(.*)";
	public static final Pattern FLOWID_PATTERN = Pattern.compile("(.*)\\.(.*)\\.(.*)\\.(.*)\\.(.*)\\.(.*)");

	/** Logger for this class and subclasses */
    protected static final Log logger = LogFactory.getLog(ActionUtils.class);

    // interface methods

    public static String getInstance(String idOrPath){
		return parseAction(idOrPath,ActionUtils.INSTANCE_ID_INDEX);
	}
	
    public static String getScope(String idOrPath){
		return parseAction(idOrPath,ActionUtils.SCOPE_ID_INDEX);
	}
	public static String getModule(String idOrPath){
		return parseAction(idOrPath,ActionUtils.MODULE_ID_INDEX);
		
	}
	public static String getSection(String idOrPath){
		return parseAction(idOrPath,ActionUtils.SECTION_ID_INDEX);
	}
	
	public static String getTarget(String idOrPath){
		return parseAction(idOrPath,ActionUtils.TARGET_ID_INDEX);
	}

	
	

	/**
	 * get the action path (suitable for looking up resources)
	 * e.g. lava/core/admin/auth/authUsers
	 * 	
	 * @param idOrPath action id or request URI 
	 * @return
	 */
	public static String getActionPath(String idOrPath){
		String[] actionParts = parseAction(idOrPath);
		if(actionParts.length==0){
			return new String();
		}	
		return extractActionPath(actionParts);
	}
	
	/**
	 * Formats the actionId (or path) into a URL format suitable for a URL based redirection.  
	 * @param idOrPath action id or request URI
	 * @return
	 */
	public static String getActionUrl(String idOrPath){
		String[] actionParts = parseAction(idOrPath);
		if(actionParts.length==0){
			return new String();
		}	
		return extractActionUrl(actionParts);
	}
	
	
	
	
	
		
	/**
	 * Return the actionId formated with the instance name passed in.  Facilitates looking up to see 
	 * if there is an instance specific customization of a standard action.
	 * @param inActionId  the action id
	 * @param instance the instance to use for the returned id.
	 * @return
	 */
	public static String getActionIdWithInstance(String inActionId, String instance){
		String[] actionParts = parseAction(inActionId);
		StringBuffer actionId = new StringBuffer(instance)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractScope(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractModule(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractSection(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractTarget(actionParts));
		return new String(actionId);
	
	}

	/**
	 * Return the actionId formated with the lava instance identifier.  This is particularly 
	 * useful if you need to convert a current or default action into a flow-id (flow-ids always
	 * have the lava instance identifier 
	 * @param inActionId  the action id
	 * @return
	 */
	public static String getActionIdWithLavaInstance(String inActionId){
		return getActionIdWithInstance(inActionId,LAVA_INSTANCE_IDENTIFIER); 
	
	}
		
	/**
	 * Get the action id from the request URI.  
	 * @param request
	 * @return
	 */
	public static String getActionId(HttpServletRequest request){
		String[] actionParts = parseActionUrl(request.getRequestURI());
		return extractActionId(actionParts);
	}
	
	
	/**
	 * Determines if the actionId passed in is a default action alias used in the 
	 * action registry to indicate the default actions for the sections and modules.
	 * 
	 * All of these actions are characterized as having the DEFAULT_ACTION_SCOPE_IDENTIFIER in
	 * the id
	 * @return
	 */
	public static boolean isDefaultActionKey(String actionId){
		if(actionId.contains(DEFAULT_ACTION_SCOPE_IDENTIFIER)){
			return true;
		}
		return false;
	}
	
	
	public static String getActionIdWithScope(String idOrPath,String scope){
		String[] actionParts = parseAction(idOrPath);
		StringBuffer actionId = new StringBuffer(extractInstance(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(scope)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractModule(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractSection(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractTarget(actionParts));
		return new String(actionId);
	}
	
	
	public static String getDefaultActionKeyWithInstance(String idOrPath, String webAppInstanceName){
		return getDefaultActionKeyWithInstance(idOrPath,webAppInstanceName, DEFAULT_ACTION_IDENTIFIER);
	}
	
	public static String getDefaultActionKeyWithInstance(String idOrPath, String webAppInstanceName, String defaultIdentifier){
		String[] actionParts = parseAction(idOrPath);
		StringBuffer actionId = new StringBuffer(webAppInstanceName)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(DEFAULT_ACTION_SCOPE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractModule(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractSection(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(defaultIdentifier);
		return new String(actionId);
	}
	
	
	public static String getDefaultActionKey(String idOrPath){
		return getDefaultActionKey(idOrPath, DEFAULT_ACTION_IDENTIFIER);
	}
		
	public static String getDefaultActionKey(String idOrPath, String defaultIdentifier){
		String[] actionParts = parseAction(idOrPath);
		StringBuffer actionId = new StringBuffer(LAVA_INSTANCE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(DEFAULT_ACTION_SCOPE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractModule(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractSection(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(defaultIdentifier);
		return new String(actionId);
	}
	
	
	public static String getDefaultModuleActionKey(String idOrPath){
		return getDefaultModuleActionKey(idOrPath,DEFAULT_ACTION_IDENTIFIER);
	}
	public static String getDefaultModuleActionKey(String idOrPath, String defaultIdentifier){
		String[] actionParts = parseAction(idOrPath);
		StringBuffer actionId = new StringBuffer(LAVA_INSTANCE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(DEFAULT_ACTION_SCOPE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(extractModule(actionParts))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(defaultIdentifier);
		return new String(actionId);
	}

	public static String getDefaultModuleActionKeyWithInstance(String idOrPath, String webAppInstanceName){
		return getDefaultModuleActionKeyWithInstance(idOrPath, webAppInstanceName,DEFAULT_ACTION_IDENTIFIER);
	}
	
	public static String getDefaultModuleActionKeyWithInstance(String idOrPath, String webAppInstanceName, String defaultIdentifier){
		String[] actionParts = parseAction(idOrPath);
		StringBuffer actionId = new StringBuffer(webAppInstanceName)
			.append(ActionUtils.ACTION_ID_DELIMITER).append(DEFAULT_ACTION_SCOPE_IDENTIFIER)
			.append(ActionUtils.ACTION_ID_DELIMITER).append(extractModule(actionParts))
			.append(ActionUtils.ACTION_ID_DELIMITER).append(defaultIdentifier);
		return new String(actionId);
	}
	
	public static String getDefaultHomeActionKey(){
		return getDefaultHomeActionKey(DEFAULT_ACTION_IDENTIFIER);
	}
	
	public static String getDefaultHomeActionKey(String defaultIdentifier){
		StringBuffer actionId = new StringBuffer(LAVA_INSTANCE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(DEFAULT_ACTION_SCOPE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(defaultIdentifier);
		return new String(actionId);
	}

	public static String getDefaultHomeActionKeyWithInstance(String idOrPath, String webAppInstanceName){
		return getDefaultHomeActionKeyWithInstance(idOrPath,webAppInstanceName,DEFAULT_ACTION_IDENTIFIER);	
	}
	public static String getDefaultHomeActionKeyWithInstance(String idOrPath, String webAppInstanceName, String defaultIdentifier){
		String[] actionParts = parseAction(idOrPath);
		StringBuffer actionId = new StringBuffer(webAppInstanceName)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(DEFAULT_ACTION_SCOPE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(defaultIdentifier);
		return new String(actionId);
	}
	
	
		
	
	
	
	public static String getActionMode(HttpServletRequest request){
		if(StringUtils.isNotEmpty(request.getParameter(MODE_PARAMETER_NAME))){
			return request.getParameter(MODE_PARAMETER_NAME);
		}else{
			
			Map parameters = request.getParameterMap();
			for(Object key: parameters.keySet()){
				String paramName = (String)key;
				if(paramName.matches(MODE_PARAMETER_NAME.concat("_.*"))){
					return paramName.substring(MODE_PARAMETER_NAME.length()+1);
				}
			}
			return "";
		}
	}
	
	public static String createRedirectUrl(HttpServletRequest request,Action actionInstance){
		StringBuffer redirect = new StringBuffer("redirect:/").append(actionInstance.getActionUrlWithIdParam());
		return new String(redirect);
		}
	
	public static String createRedirectUrlWithParams(HttpServletRequest request,Action actionInstance){
		StringBuffer redirect = new StringBuffer("redirect:/").append(actionInstance.getActionUrlWithParams());
		return new String(redirect);
	}
	
	
	
	
	// web flow utility methods
	
    // the eventId is in the format [objectName]__[eventName]
	public static String getEventId(RequestContext context) {
		String eventId = (String) context.getFlashScope().get("eventOverride");
		if (eventId == null) {
			// the event is put into conversatoin scope by FlowListener
			eventId = context.getConversationScope().getString("lastEventId");
			if (eventId == null) {
				// on entry to root flow, there will be no event
				eventId = new String(); 
			}
		}
		return eventId;
	}

	// get the object name portion of the eventId, i.e. the string preceding OBJECT_EVENT_SEPARATOR
	public static String getEventObject(RequestContext context){
		String eventId = getEventId(context); 
		Matcher eventMatcher = Pattern.compile(EVENT_FORMAT).matcher(eventId);
		if (!eventMatcher.matches()){
			logger.warn("unable to parse object from eventId: " + eventId);
			return new String();
		}
		return eventMatcher.group(1);
	}
	
	// get the event name portion of the eventId, i.e. the string following to OBJECT_EVENT_SEPARATOR
	public static String getEventName(RequestContext context){
		String eventId = getEventId(context); 
		Matcher eventMatcher = Pattern.compile(EVENT_FORMAT).matcher(eventId);
		if (!eventMatcher.matches()){
			return new String();
		}
		return eventMatcher.group(2);
	}
	
	public static String getDefaultActionIdFromFlowId(String flowId, StringBuffer modePart) {
		Matcher flowIdMatcher = FLOWID_PATTERN.matcher(flowId);
		flowIdMatcher.matches(); // must call matches to do the actual match
		// the "default" action id has scope LAVA_SCOPE_IDENTIFIER
		StringBuffer actionId = new StringBuffer(ActionUtils.LAVA_INSTANCE_IDENTIFIER)
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.SCOPE_ID_INDEX+1))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.MODULE_ID_INDEX+1))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.SECTION_ID_INDEX+1))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.TARGET_ID_INDEX+1));
		// store the flowId mode part, which can be used as the Action mode parameter
		modePart.append(flowIdMatcher.group(MODE_ID_INDEX+1));
		return actionId.toString();
	}

	
	public static String getActionIdFromFlowId(String flowId, StringBuffer modePart) {
		Matcher flowIdMatcher = FLOWID_PATTERN.matcher(flowId);
		flowIdMatcher.matches(); // must call matches to do the actual match
		StringBuffer actionId = new StringBuffer(flowIdMatcher.group(ActionUtils.INSTANCE_ID_INDEX+1))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.SCOPE_ID_INDEX+1))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.MODULE_ID_INDEX+1))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.SECTION_ID_INDEX+1))
		.append(ActionUtils.ACTION_ID_DELIMITER).append(flowIdMatcher.group(ActionUtils.TARGET_ID_INDEX+1));
		// store the flowId mode part, which can be used as the Action mode parameter
		modePart.append(flowIdMatcher.group(MODE_ID_INDEX+1));
		return actionId.toString();
	}
	
	
	public static String getFlowMode(String flowId) {
		Matcher flowIdMatcher = FLOWID_PATTERN.matcher(flowId);
		flowIdMatcher.matches(); // must call matches to do the actual match
		return flowIdMatcher.group(MODE_ID_INDEX+1);
	}
	
	
	
	//internal methods
	protected static String extractInstance(String[] actionParts){
		return actionParts[ActionUtils.INSTANCE_ID_INDEX];
	}
	
	protected static String extractScope(String[] actionParts){
		return actionParts[ActionUtils.SCOPE_ID_INDEX];
	}
	
	protected static String extractModule(String[] actionParts){
		return actionParts[ActionUtils.MODULE_ID_INDEX];
	}
	protected static String extractSection(String[] actionParts){
		return actionParts[ActionUtils.SECTION_ID_INDEX];
	}
	
	
	
	protected static String extractTarget(String[] actionParts){
		return actionParts[ActionUtils.TARGET_ID_INDEX];
	}
	
	protected static String extractView(String[] actionParts){
		return actionParts[TARGET_ID_INDEX];
	}
	
	protected static String extractActionId(String[] actionParts){
		return new String(new StringBuffer(ActionUtils.LAVA_INSTANCE_IDENTIFIER).append(ActionUtils.ACTION_ID_DELIMITER)
				.append(actionParts[SCOPE_ID_INDEX]).append(ActionUtils.ACTION_ID_DELIMITER)
				.append(actionParts[MODULE_ID_INDEX]).append(ActionUtils.ACTION_ID_DELIMITER)
				.append(actionParts[SECTION_ID_INDEX]).append(ActionUtils.ACTION_ID_DELIMITER)
				.append(actionParts[TARGET_ID_INDEX]));
	}
	
	
	
	/**
	 * Format the action path (suitable for looking up resources) from the parsed action parts as
	 * [instance]/[scope]/[module]/[section]/[target]
	 * e.g. lava/core/admin/auth/authUsers
	 * 	
	 * @param actionParts
	 * @return
	 */
	protected static String extractActionPath(String[] actionParts){	
		return new String(new StringBuffer(extractActionPathWithoutView(actionParts))
				.append(ActionUtils.ACTION_PATH_DELIMITER)
				.append(extractView(actionParts)));
	}
	
		
		
	/**
	 * Format an action URL from the parsed action parts as
	 * [scope].[module].[section].[target].lava
	 * e.g. core.admin.auth.authUsers.lava
	 * @param actionParts
	 * @return
	 */
	protected static String extractActionUrl(String[] actionParts){	
		return new String(new StringBuffer(actionParts[SCOPE_ID_INDEX])
				.append(ActionUtils.ACTION_PATH_DELIMITER)
				.append(actionParts[MODULE_ID_INDEX])
				.append(ActionUtils.ACTION_PATH_DELIMITER)
				.append(actionParts[SECTION_ID_INDEX])
				.append(ActionUtils.ACTION_PATH_DELIMITER)
				.append(actionParts[TARGET_ID_INDEX])
				.append(ACTION_TARGET_EXTENSION));
	
		
	}
	/*
	 * Depending upon scope (e.g. lava, web app instance name (which is the Servlet Spec context path), 
	 * or project specific) return appropriate action path. 
	 */
	protected static String extractActionPathWithoutView(String[] actionParts){	
		if (actionParts[INSTANCE_ID_INDEX].equalsIgnoreCase(LAVA_INSTANCE_IDENTIFIER)){
			return new String(new StringBuffer(actionParts[SCOPE_ID_INDEX])
				.append(ActionUtils.ACTION_PATH_DELIMITER)
				.append(actionParts[MODULE_ID_INDEX])
				.append(ActionUtils.ACTION_PATH_DELIMITER)
				.append(actionParts[SECTION_ID_INDEX]));
		}else {
			// add the local identifier and instance identifier to the action path, so 
			// that the instance specific jsp can be located 
			return new String(new StringBuffer(DEFAULT_LOCAL_IDENTIFIER)
			.append(ActionUtils.ACTION_PATH_DELIMITER)
			.append(actionParts[INSTANCE_ID_INDEX])
			.append(ActionUtils.ACTION_PATH_DELIMITER)
			.append(actionParts[SCOPE_ID_INDEX])
			.append(ActionUtils.ACTION_PATH_DELIMITER)
			.append(actionParts[MODULE_ID_INDEX])
			.append(ActionUtils.ACTION_PATH_DELIMITER)
			.append(actionParts[SECTION_ID_INDEX]));
		}
		
	}
	
	protected static String parseAction(String idOrPath, int idIndex){
		String[] actionParts = parseAction(idOrPath);
		if (actionParts.length < idIndex + 1 ){
			return new String();
		}
		return actionParts[idIndex];
	}
	
	protected static String[] parseAction(String idOrPath){
		if(idOrPath.matches(ActionUtils.ACTIONID_FORMAT)){
			return(parseActionId(idOrPath));
		}else if (idOrPath.matches(ActionUtils.ACTIONURL_FORMAT)){
			return (parseActionUrl(idOrPath));
		}else{
			logger.warn("invalid action id or url passed to parseAction()");
			return new String[0];
		}
			
	}
	
	protected static String[] parseActionId(String id){
		
		if(id.matches(ActionUtils.ACTIONID_FORMAT)){
			return (String[]) Pattern.compile("\\.").split(id);
		}else{
			return new String[0];
		}
			
	}
	
	protected static String[] parseActionUrl(String url){
		// the scope will always be LAVA_SCOPE_IDENTIFIER when parsing the URL.
		// this is NOT based on the context path of the webapp, which may also happen
		// to be /lava but could be anything else
		// rather, LAVA_SCOPE_IDENTIFIER is a hard-coded value that represents the standard
		// action for a given MODULE/SECTION/TARGET and is just a placeholder 
		// until the customization algorithm determines what the true scope for the
		// action should be, e.g. "adrc_outreach" or "clinic" or "ftdppg_any" or "instance"
		// or if there is not customization at all, the scope will remain "lava"
		Matcher actionUrlMatcher = Pattern.compile(ActionUtils.ACTIONURL_FORMAT).matcher(url);
		if (!actionUrlMatcher.matches()){
			logger.warn("unable to parse action from url: " + url);
			return new String[0];
		}
		
		String[] actionParts = {LAVA_INSTANCE_IDENTIFIER,
								actionUrlMatcher.group(ActionUtils.SCOPE_URL_INDEX+1),
								actionUrlMatcher.group(ActionUtils.MODULE_URL_INDEX+1),
								actionUrlMatcher.group(ActionUtils.SECTION_URL_INDEX+1),
								actionUrlMatcher.group(ActionUtils.TARGET_URL_INDEX+1)};
		return actionParts;
	}
	
	
	
}
