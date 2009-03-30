<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- component
	 abstract out the component implementation from simple property references
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>
<%@ attribute name="property" required="true" 
       description="the property" %>
<%@ attribute name="property2" 
       description="an optional second property" %>  

<c:choose>
	<c:when test="${empty property2}">
		${command.components[component][property]}
	</c:when>
	<c:otherwise>
		<c:if test="${not empty command.components[component][property]}">
			${command.components[component][property][property2]}
		</c:if>
	</c:otherwise>
</c:choose>

