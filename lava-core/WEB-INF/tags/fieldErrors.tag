<%@ include file="/WEB-INF/tags/tags-include.jsp" %>


<%-- fieldErrors

     Output HTML formatted error messages, where each error messages in the specified
     list is a list item.
--%>

<%@ attribute name="errorMessages" type="java.util.List" required="true" 
              description="array of error messages" %>

<ul class="fieldErrorList">
	<c:forEach items="${errorMessages}" var="errorMessage">
    	<li class="errorListItem">${errorMessage}</li>
	</c:forEach>
</ul>
    