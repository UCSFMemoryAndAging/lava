<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- tableRow

	 Row in a table
--%>
<%@ attribute name="styleClass" required="false"  
              description="the style class to be applied to the row" %>
<%@ attribute name="height" required="false"  
              description="the height of the row--anything that can be evaluted in a TR row attribute"  %>              
              
<tr <c:if test="${not empty styleClass}">class="${styleClass}"</c:if> <c:if test="${not empty height}">height="${height}"</c:if>>
  <jsp:doBody/>
</tr>
