<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<!-- none -->
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.patient.addPatient" startMode="add"/>">
<spring:message code="action.lava.crms.people.patient.addPatient"/></a><br>

</body>
</html>