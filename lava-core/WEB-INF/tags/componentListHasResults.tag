<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- component List has results 
  does the body of the tag only when the component list has at least one record
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>

<c:if test="${command.components[component].nrOfElements > 0}">
  <jsp:doBody/>
</c:if>