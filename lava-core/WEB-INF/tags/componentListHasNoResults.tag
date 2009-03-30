<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- component List has no results 
  does the body of the tag only when the component list has no records
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>

<c:if test="${command.components[component].nrOfElements == 0}">
  <jsp:doBody/>
</c:if>