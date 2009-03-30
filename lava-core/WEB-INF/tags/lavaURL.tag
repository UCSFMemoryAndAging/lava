<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- actionURL
	 Generate a lava action URL based on URL components stored in the messages.properties file
	 and the current project.
--%>

<%@ attribute name="projectURL" required="false" 
       description="the escaped code for the project context to use.   If not supplied, will use the $currentProject var" %>
<%@ attribute name="actionCode" required="true"
       description="the message code used to lookup the action path from the messages file"%>
<%@ attribute name="arguments" required="false"
       description="arguments string in the same format as that needed for spring:message tag"%>


<c:if test="${projectURL == null}">
	<c:set var="projectURL" value="${currentProject.urlEncoded}"/>
</c:if>

<c:set var="fullArgs" value="${projectURL},${arguments}"/>

<c:if test="${arguments == null || $arguments == ''}">
	<c:set var="fullArgs" value="${projectURL}"/>
</c:if>

<spring:message code="${actionCode}" arguments="${fullArgs}"/>


