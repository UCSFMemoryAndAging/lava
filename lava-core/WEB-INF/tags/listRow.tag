<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- tableRow

	 Row in a table
--%>
<%@ attribute name="styleClass" required="false"  
              description="the style class to be applied to the row" %>
<tr <c:if test="${not empty styleClass}">class="${styleClass}"</c:if>>
  <jsp:doBody/>
</tr>
