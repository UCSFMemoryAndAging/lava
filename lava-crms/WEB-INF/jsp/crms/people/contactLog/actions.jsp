<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.people.contactLog.contactLog" startMode="add" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.contactLog.contactLog.add"/></a><br>
<a href="<tags:actionURL actionId="lava.crms.people.contactLog.patientContactLog" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.contactLog.patientContactLog.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.contactLog.projectContactLog"/>">
<spring:message code="action.lava.crms.people.contactLog.projectContactLog.list"/></a>
</body>
</html>