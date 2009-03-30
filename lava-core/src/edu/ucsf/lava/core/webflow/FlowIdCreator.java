package edu.ucsf.lava.core.webflow;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.FlowExecutionContext;
import org.springframework.webflow.execution.support.FlowDefinitionRedirect;
import org.springframework.webflow.executor.support.RequestParameterFlowExecutorArgumentHandler;

import edu.ucsf.lava.core.action.ActionManager;
import edu.ucsf.lava.core.action.ActionUtils;
import edu.ucsf.lava.core.action.model.Action;
import edu.ucsf.lava.core.manager.CoreManagerUtils;
import edu.ucsf.lava.core.manager.Managers;
import edu.ucsf.lava.core.manager.ManagersAware;


// this class creates flow id's from request URL's based on the actionId of the
// requestURL, and optionally a mode request parameter (_do)
// it also contains a method used by Spring Web Flow to construct URL's from
// flowId's for using flowRedirect syntax in flow definition views
public class FlowIdCreator extends RequestParameterFlowExecutorArgumentHandler implements ManagersAware {
	  protected ActionManager actionManager; 
	
	/**
	 * URL path separator ("/").
	 */
	private static final char PATH_SEPARATOR_CHARACTER = '/';

	public boolean isFlowIdPresent(ExternalContext context) {
		String dispatcherPathInfo = context.getDispatcherPath();
		boolean hasFileName = StringUtils.hasText(WebUtils.extractFilenameFromUrlPath(dispatcherPathInfo));
		return hasFileName || super.isFlowIdPresent(context);
	}

	public String extractFlowId(ExternalContext context) {
		return actionManager.extractFlowIdFromRequest(((ServletExternalContext)context).getRequest());
		
	}

	
	// this is called to support the "restart flow" and "redirect to flow" use cases. in the 
	// latter case, when a view is specified using "flowRedirect:FLOW_ID?OPTIONAL_PARAMS" syntax, 
	// this method converts the FLOW_ID to a URL that can be used to redirect to a new flow.
	// then the web flow framework calls extractFlowId is called to extract the flow id from this 
	// URL and create the new flow.
	public String createFlowDefinitionUrl(FlowDefinitionRedirect flowDefinitionRedirect, ExternalContext context) {
		HttpServletRequest request = ((ServletExternalContext)context).getRequest();
		
		// example of return value: /lava/crms/scheduling/patientCalendar/patientVisits.lava?id=3653
		
		StringBuffer flowUrl = new StringBuffer();
		flowUrl.append(context.getContextPath()).append(PATH_SEPARATOR_CHARACTER);
		
		// the flow id to redirect to determines the Url scope/module/section/target
		// e.g. lava.crms.scheduling.patientCalendar.patientVisits.view
		String flowId = flowDefinitionRedirect.getFlowDefinitionId();
		
		// convert the flow id to an action id
		// e.g. lava.scheduling.patientCalendar.patientVisits
		// also, save the flowId mode part, because if it is not "view" it will need to 
		// be set as the mode request parameter (_do)
		StringBuffer flowIdModePart = new StringBuffer();
		String defaultActionId = ActionUtils.getDefaultActionIdFromFlowId(flowId, flowIdModePart);
		Action action = actionManager.getAction(defaultActionId);
		
		// convert the actionId to a URL
		flowUrl.append(ActionUtils.getActionUrl(defaultActionId));
		
		if (!flowDefinitionRedirect.getExecutionInput().isEmpty() || !flowIdModePart.toString().equals(action.getDefaultFlowMode())) {
			flowUrl.append('?');
			if (!flowIdModePart.toString().equals(action.getDefaultFlowMode())) {
				flowUrl.append(ActionUtils.MODE_PARAMETER_NAME).append("=").append(flowIdModePart);
				flowUrl.append('&');
			}
			appendQueryParameters(flowUrl, flowDefinitionRedirect.getExecutionInput());
		}
		return flowUrl.toString();
	}

	// this is used to support the "flow execution redirect" use case, i.e. post-redirect-get semantics,
	// where an application view is refreshed by redirecting to an existing, active Spring Web Flow execution 
	// at a unique SWF-specific flow execution URL. once the redirect response is issued a new request is 
	// initiated by the browser targeted at the flow execution URL. The URL is stabally refreshable (and 
	// bookmarkable) while the conversation remains active
	public String createFlowExecutionUrl(String flowExecutionKey, FlowExecutionContext flowExecution,
			ExternalContext context) {
		// just default to the contextPath and dispatcherPath of the current request with the flowExecutionKey
		// as a request parameter in addition to any other request parameters specified.
		// the contextPath will get the request to this webapp (via JBoss/Tomcat), the .lava will get it to 
		// the Spring DispatcherServlet (web.xml), the Spring DispatcherServlet will dispatch it to the Spring
		// Web Flow FlowController (lava-mappings.xml), and since a flowExecutionKey is present, Spring Web Flow 
		// will ignore the path in the request URL and get to the current state of the flow
		return super.createFlowExecutionUrl(flowExecutionKey, flowExecution, context);
	}


	
	public void updateManagers(Managers managers) {
		this.actionManager = CoreManagerUtils.getActionManager(managers);
	}

	protected void appendFlowExecutorPath(StringBuffer url, ExternalContext context) {
		url.append(context.getContextPath());
		url.append(context.getDispatcherPath());
	}

}