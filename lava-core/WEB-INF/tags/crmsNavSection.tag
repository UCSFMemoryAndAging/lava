<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- crmsNavSection

	 Create a navigation section that links to the default action for the crms section.
	 --%>
<%@ attribute name="module" required="true"  
              description="the module that the section is within" %>
<%@ attribute name="section" required="true"  
              description="the section that the link represents" %>          
<%@ attribute name="text" required="false"
				description="the text for the section" %>    
<%@ attribute name="textCode" required="false"
				description="a lookup code for the text for the section. If present overrides text" %>            
<%@ attribute name="defaultTargetIdentifier" required="false"  
              description="the default target identifier to use for the default module action" %>
<%@ attribute name="idParam" required="false"  
              description="optional id param for the action" %>
<%@ attribute name="startMode" required="false"
       description="flow Mode, defaults to 'view'"%>
<%@ attribute name="disabled" required="false"  
              description="indicates that the section link should be disabled (e.g. modal state)" %>
<%@ attribute name="lastSection" required="false" 
			  description="indicates that this section is the last one for the module and no trailing '|' should be displayed"%>
     
<c:if test="${empty defaultTargetIdentifier}">
	<c:if test="${not empty currentPatient}">
		<c:set var="defaultTargetIdentifier" value="defaultPatientAction"/>
	</c:if>
</c:if>

<c:if test="${empty idParam}">
	<c:set var="idParam" value="${not empty currentPatient ? currentPatient.id : ''}"/>
</c:if>

<tags:coreNavSection text="${text}" textCode="${textCode}" module="${module}" section="${section}" 
	defaultTargetIdentifier="${defaultTargetIdentifier}" startMode="${startMode}" idParam="${idParam}" disabled="${disabled}" lastSection="${lastSection}"/>