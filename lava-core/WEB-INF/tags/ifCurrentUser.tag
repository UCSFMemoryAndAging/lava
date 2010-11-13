<%@ include file="/WEB-INF/tags/tags-include.jsp" %>

<%-- ifAuthUser
	
	 only do body of tag if current user matches user name passed in
--%>
<%@ attribute name="component" required="true" 
       description="the component" %>
<%@ attribute name="userIdProperty" required="true" 
       description="a property containing a user ID" %>

<c:if test="${currentUser.id == command.components[component][userIdProperty]}">
	<jsp:doBody/>
</c:if>
 