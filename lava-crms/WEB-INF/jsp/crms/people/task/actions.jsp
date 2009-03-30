<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->
<c:if test="${not empty currentPatient}">
<a href="<tags:actionURL actionId="lava.crms.people.task.task" startMode="add" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.task.task.add"/></a><br>
<a href="<tags:actionURL actionId="lava.crms.people.task.patientTasks" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.task.patientTasks.list"/></a><br>
</c:if>

<!--  project actions -->
<a href="<tags:actionURL actionId="lava.crms.people.task.projectTasks"/>">
<spring:message code="action.lava.crms.people.task.projectTasks.list"/></a>
</body>
</html>