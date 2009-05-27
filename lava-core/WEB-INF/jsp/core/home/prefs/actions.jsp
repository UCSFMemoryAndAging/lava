<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>

<c:if test="${not empty currentUser}">

<a href="<tags:actionURL actionId="lava.core.home.prefs.changePassword"/>">
<spring:message code="action.lava.core.home.prefs.changePassword.change"/></a><br/>


</c:if>
</body>
</html>