<%@ include file="/WEB-INF/tags/tags-include.jsp" %>
<%-- componentList
	Abstracts out the component implementation from list reference.
	 
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>
<%@ attribute name="index" 
       description="an index into the list" %>
       
<c:choose>
	<c:when test="${empty index}">
       ${command.components[component].pageList}
    </c:when>
    <c:otherwise>
       ${command.components[component].pageList[index]}
    </c:otherwise>
</c:choose>

