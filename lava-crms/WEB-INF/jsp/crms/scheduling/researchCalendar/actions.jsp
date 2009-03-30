<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.scheduling.visit.visit" startMode="add"/>">
<spring:message code="action.lava.crms.scheduling.visit.visit.add"/></a><br>
</c:if>

<!--  project actions -->
<c:if test="${not empty currentProject}">

</c:if>
</body>
</html>