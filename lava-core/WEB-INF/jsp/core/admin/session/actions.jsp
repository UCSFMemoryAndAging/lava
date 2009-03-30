<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<a href="<tags:actionURL actionId="lava.core.admin.session.currentLavaSessions"/>">
<spring:message code="action.lava.core.admin.session.currentLavaSessions.list"/></a><br>

<a href="<tags:actionURL actionId="lava.core.admin.session.lavaSessions"/>">
<spring:message code="action.lava.core.admin.session.lavaSessions.list"/></a><br>


</body>
</html>