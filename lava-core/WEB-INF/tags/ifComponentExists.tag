<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- ifComponentExists
	 if the component exists in the command.components[] map then dobody
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>
<c:catch var="exception">
	<c:set var="test" value="${command.components[component]}"/>
</c:catch>
<c:if test="${empty exception}">
	<jsp:doBody/>
</c:if>
