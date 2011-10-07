<%@ include file="/WEB-INF/jsp/includes/include.jsp" %>
<html>
<body>
<!--  patient actions -->

<a href="<tags:actionURL actionId="lava.crms.people.findPatient.findPatient"/>">
<spring:message code="action.lava.crms.people.findPatient.findPatient"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.people.patient.addPatient" startMode="add"/>">
<spring:message code="action.lava.crms.people.patient.addPatient"/></a><br>

<c:if test="${not empty currentPatient}">

<a href="<tags:actionURL actionId="lava.crms.people.patient.patient" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.patient.patient.view"/></a><br>

<a href="<tags:actionURL actionId="lava.crms.people.patient.patient" startMode="delete" idParam="${currentPatient.id}"/>">
<spring:message code="action.lava.crms.people.patient.patient.delete"/></a><br>
</c:if>

<!--  project actions -->



<a href="<tags:actionURL actionId="lava.crms.people.patient.projectPatients"/>">
<spring:message code="action.lava.crms.people.patient.projectPatients.list"/></a>

</body>
</html>