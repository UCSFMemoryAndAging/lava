<%@ include file="/WEB-INF/tags/tags-include.jsp" %>





<%-- iflistFilterFieldUsed
	Abstracts out the implementation of list filters from determining whether any 
	criteria have been specified for a particular filter field
--%> 

<%@ attribute name="property" required="true" 
       description="The name of the property in the filter.params[] map'" %>
<%@ attribute name="component" required="true" 
       description="the component name" %>

<c:if test="${!empty command.components[component].filter.params[property]}">
	<jsp:doBody/>
</c:if>