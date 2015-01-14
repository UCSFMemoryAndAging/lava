<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- crmsNavTag

	 Create a navigation tab that links to the default action for the crms module.
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

<c:if test="${empty defaultTargetIdentifier}">
	<c:if test="${not empty currentPatient}">
		<c:set var="defaultTargetIdentifier" value="defaultPatientAction"/>
	</c:if>
</c:if>

<c:if test="${empty idParam}">
	<c:set var="idParam" value="${not empty currentPatient ? currentPatient.id : ''}"/>
</c:if>
				  
<tags:coreNavTab text="${text}" module="${module}" defaultTargetIdentifier="${defaultTargetIdentifier}" startMode="${startMode}" idParam="${idParam}" disabled="${disabled}"/>