<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- component
	 abstract out the component implementation from references to list property
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>
<%@ attribute name="property" required="true" 
       description="the property" %>
<%@ attribute name="property2" 
       description="a second property to dereference" %>
<%@ attribute name="listIndex"
       description="list index for derefencing the entity from the list'"%>
       

<c:choose>
	<c:when test="${empty property2}">
		${command.components[component].pageList[$listIndex][property]}
	</c:when>
	<c:otherwise>
		${command.components[component].pageList[$listIndex][property][property2]}
	</c:otherwise>
</c:choose>
