<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- component
	 abstract out the component implementation from references to list filter properties
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>
<%@ attribute name="property" required="true" 
       description="the property" %>
<%@ attribute name="property2" 
       description="a second property to dereference" %>

<c:choose>
	<c:when test="${empty property2}">
		${command.components[component].filter[property]}
	</c:when>
	<c:otherwise>
		${command.components[component].filter[property][property2]}
	</c:otherwise>
</c:choose>

