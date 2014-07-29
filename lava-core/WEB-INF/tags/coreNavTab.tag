<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- coreNavTag

	 Create a navigation tab that links to the default action for the module.
	 --%>
<%@ attribute name="module" required="true"  
              description="the module that the tab represents" %>
<%@ attribute name="text" required="true"
				description="the text for the tab" %>              
<%@ attribute name="defaultTargetIdentifier" required="false"  
              description="the default target identifier to use for the default module action" %>
<%@ attribute name="idParam" required="false"  
              description="optional id param for the action" %>
<%@ attribute name="startMode" required="false"
			  description="flow Mode, defaults to 'view'"%>
<%@ attribute name="disabled" required="false"  
              description="indicates that the tab should be disabled (e.g. modal state)" %>
    
<tags:navTab text="${text}" selected="${currentAction.module == module ? true : false}" actionId="lava.defaultScope.${module}.${empty defaultTargetIdentifier ? 'defaultAction' : defaultTargetIdentifier}" startMode="${startMode}" idParam="${idParam}" disabled="${disabled}"/>