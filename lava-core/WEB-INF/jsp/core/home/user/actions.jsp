<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>

<c:if test="${not empty currentUser}">

<a href="<tags:actionURL actionId="lava.core.home.user.changePassword"/>">
<spring:message code="action.lava.core.home.user.changePassword.change"/></a><br/>


</c:if>
</body>
</html>